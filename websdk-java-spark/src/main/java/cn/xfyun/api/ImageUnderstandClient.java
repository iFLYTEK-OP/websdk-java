package cn.xfyun.api;

import cn.xfyun.base.websocket.AbstractClient;
import cn.xfyun.config.Role;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sparkmodel.RoleContent;
import cn.xfyun.model.sparkmodel.SparkChatParam;
import cn.xfyun.model.sparkmodel.request.SparkSendRequest;
import cn.xfyun.model.sparkmodel.response.ImageUnderstandResponse;
import cn.xfyun.service.sparkmodel.AbstractImgUnderstandWebSocketListener;
import cn.xfyun.util.StringUtils;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.security.SignatureException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

/**
 * 图像理解 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/spark/ImageUnderstanding.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 **/
public class ImageUnderstandClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(ImageUnderstandClient.class);

    /**
     * 核采样阈值。取值越高随机性越强，即相同的问题得到的不同答案的可能性越大
     * ws 取值范围 (0，1] ，默认值0.5
     */
    private final float temperature;

    /**
     * 模型回答的tokens的最大长度
     * 最小值是1, 最大值是8192，默认值2048
     */
    private final int maxTokens;

    /**
     * 从k个候选中随机选择⼀个（⾮等概率）
     * 取值为[1，6],默认为4
     */
    private final int topK;

    /**
     * 模型版本
     * general (基础版)
     * imagev3(高级版)
     * 1、高级版本效果更优；
     * 2、针对相同图片token计量不同，高级版token为动态计量，图片内容越复杂，token消耗越高。
     * 请根据业务选择
     */
    private final String domain;

    public ImageUnderstandClient(Builder builder) {
        this.okHttpClient = new OkHttpClient
                .Builder()
                .callTimeout(builder.callTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(builder.callTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(builder.readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(builder.writeTimeout, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(builder.retryOnConnectionFailure)
                .build();
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.apiSecret = builder.apiSecret;
        this.originHostUrl = builder.hostUrl;

        this.temperature = builder.temperature;
        this.maxTokens = builder.maxTokens;
        this.topK = builder.topK;
        this.domain = builder.domain;

        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.callTimeout = builder.callTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
    }

    public String getDomain() {
        return domain;
    }

    public float getTemperature() {
        return temperature;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public int getTopK() {
        return topK;
    }

    /**
     * 一次性问答
     *
     * @param question  用户问题
     * @param imgBase64 图片信息
     */
    public String send(String question, String imgBase64) throws IOException, SignatureException {
        // 封装用户问题
        SparkChatParam param = paramHandler(question, imgBase64);

        // 结束标志位
        CountDownLatch latch = new CountDownLatch(1);

        // 错误信息记录
        AtomicReference<String> errorMessage = new AtomicReference<>();

        // 大模型返回的内容
        StringBuffer result = new StringBuffer();

        // 发送请求
        send(param, listenerHandler(latch, errorMessage, result));

        // 等待socket完成
        waitForSuccess(latch, errorMessage);
        return result.toString();
    }

    /**
     * 图像理解ws请求方式
     * 多轮交互需要将之前的交互历史按照
     * user(image)->user->assistant->user->assistant规则进行拼接，保证最后一条是user的当前问题
     *
     * @param sparkChatParam    请求参数
     * @param webSocketListener 用户自定义ws监听类(AbstractImgUnderstandWebSocketListener)
     */
    public void send(SparkChatParam sparkChatParam, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        // 参数校验
        paramCheck(sparkChatParam);

        // 初始化链接client
        WebSocket webSocket = newWebSocket(webSocketListener);

        try {
            // 构建请求参数
            String param = buildParam(sparkChatParam);
            logger.debug("图像理解ws请求参数：{}", param);
            // 发送请求
            webSocket.send(param);
        } catch (Exception e) {
            logger.error("ws消息发送失败", e);
        }
    }

    /**
     * 等待文件写入成功
     *
     * @param latch        门栓
     * @param errorMessage 错误信息
     */
    private void waitForSuccess(CountDownLatch latch, AtomicReference<String> errorMessage) throws SocketTimeoutException {
        try {
            // 定义等待时间
            int waitTime = (this.readTimeout == 0) ? Integer.MAX_VALUE : this.readTimeout;
            // 阻塞等待socket完成
            boolean await = latch.await(waitTime, TimeUnit.MILLISECONDS);
            if (!await) {
                throw new SocketTimeoutException("请求超时");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 判断是否有异常
        if (errorMessage.get() != null) {
            throw new BusinessException(errorMessage.get());
        }
    }

    /**
     * 封装用户提问
     *
     * @param question  用户问题
     * @param imgBase64 图片信息
     */
    private SparkChatParam paramHandler(String question, String imgBase64) {
        List<RoleContent> messages = new ArrayList<>();
        RoleContent image = RoleContent.builder()
                .role(Role.USER.getValue())
                .content(imgBase64)
                .contentType("image")
                .build();
        RoleContent q = RoleContent.builder()
                .role(Role.USER.getValue())
                .content(question)
                .build();
        // 图片信息
        messages.add(image);
        // 用户问题
        messages.add(q);
        return SparkChatParam.builder()
                .messages(messages)
                .chatId(null)
                .userId(null)
                .build();
    }

    /**
     * 定义socket监听类
     *
     * @param latch        门栓
     * @param errorMessage 错误消息
     * @param result       大模型回复内容
     * @return AbstractImgUnderstandWebSocketListener
     */
    private AbstractImgUnderstandWebSocketListener listenerHandler(CountDownLatch latch,
                                                                   AtomicReference<String> errorMessage,
                                                                   StringBuffer result) {
        return new AbstractImgUnderstandWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, ImageUnderstandResponse resp) {
                if (resp.getHeader().getCode() != 0) {
                    logger.error("code=>{}，error=>{}，sid=>{}", resp.getHeader().getCode(), resp.getHeader().getMessage(), resp.getHeader().getSid());
                    errorMessage.set(resp.getHeader().getCode() + ":" + resp.getHeader().getMessage());
                    latch.countDown();
                    webSocket.close(1011, resp.getHeader().getMessage());
                    return;
                }

                if (null != resp.getPayload()) {
                    if (null != resp.getPayload().getChoices()) {
                        List<ImageUnderstandResponse.Payload.Choices.Text> text = resp.getPayload().getChoices().getText();
                        if (null != text && !text.isEmpty()) {
                            IntStream.range(0, text.size()).forEach(index -> {
                                String content = resp.getPayload().getChoices().getText().get(index).getContent();
                                if (!StringUtils.isNullOrEmpty(content)) {
                                    result.append(content);
                                }
                            });
                        }

                        if (resp.getPayload().getChoices().getStatus() == 2) {
                            latch.countDown();
                            webSocket.close(1000, "");
                        }
                    }
                }
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                logger.error(t.getMessage(), t);
                errorMessage.set(t.getMessage());
                latch.countDown();
            }

            @Override
            public void onClose(WebSocket webSocket, int code, String reason) {
                latch.countDown();
            }
        };
    }

    /**
     * 参数校验
     */
    private void paramCheck(SparkChatParam param) {
        // 验证消息不能为空
        if (param.getMessages() == null || param.getMessages().isEmpty()) {
            throw new BusinessException("文本内容不能为空");
        }
    }

    /**
     * 构建参数
     */
    private String buildParam(SparkChatParam param) {
        // 发送数据,请求数据均为json字符串
        SparkSendRequest sendRequest = new SparkSendRequest();

        // 请求头
        SparkSendRequest.Header header = new SparkSendRequest.Header(appId, param.getUserId());
        sendRequest.setHeader(header);

        // 请求参数
        SparkSendRequest.Parameter parameter = new SparkSendRequest.Parameter();
        SparkSendRequest.Parameter.Chat chat = new SparkSendRequest.Parameter.Chat();
        chat.setDomain(domain);
        chat.setTemperature(temperature);
        chat.setMaxTokens(maxTokens);
        chat.setTopK(topK);
        chat.setChatId(param.getChatId());
        parameter.setChat(chat);
        sendRequest.setParameter(parameter);

        // 请求体
        SparkSendRequest.Payload payload = new SparkSendRequest.Payload();
        SparkSendRequest.Payload.Message message = new SparkSendRequest.Payload.Message();
        message.setText(param.getMessages());
        payload.setMessage(message);
        sendRequest.setPayload(payload);
        return StringUtils.gson.toJson(sendRequest);
    }

    public static final class Builder {

        /**
         * websocket相关
         */
        private boolean retryOnConnectionFailure = true;
        private int callTimeout = 0;
        private int connectTimeout = 30000;
        private int readTimeout = 30000;
        private int writeTimeout = 30000;
        private int pingInterval = 0;
        private String appId;
        private String apiKey;
        private String apiSecret;
        private String hostUrl = "https://spark-api.cn-huabei-1.xf-yun.com/v2.1/image";
        private float temperature = 0.5F;
        private int maxTokens = 2028;
        private int topK = 4;
        private String domain = "imagev3";

        public ImageUnderstandClient build() {
            return new ImageUnderstandClient(this);
        }

        public Builder signature(String appId, String apiKey, String apiSecret) {
            this.appId = appId;
            this.apiKey = apiKey;
            this.apiSecret = apiSecret;
            return this;
        }

        public Builder callTimeout(long timeout, TimeUnit unit) {
            this.callTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder connectTimeout(long timeout, TimeUnit unit) {
            this.connectTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder readTimeout(long timeout, TimeUnit unit) {
            this.readTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder writeTimeout(long timeout, TimeUnit unit) {
            this.writeTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder pingInterval(long interval, TimeUnit unit) {
            this.pingInterval = Util.checkDuration("interval", interval, unit);
            return this;
        }

        public Builder retryOnConnectionFailure(boolean retryOnConnectionFailure) {
            this.retryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }

        public Builder temperature(float temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder maxTokens(int maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public Builder topK(int topK) {
            this.topK = topK;
            return this;
        }

        public Builder hostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        public Builder domain(String domain) {
            this.domain = domain;
            return this;
        }
    }
}
