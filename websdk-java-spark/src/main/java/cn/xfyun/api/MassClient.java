package cn.xfyun.api;

import cn.xfyun.base.webscoket.WebSocketClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.RoleContent;
import cn.xfyun.model.finetuning.request.FTTHttpRequest;
import cn.xfyun.model.finetuning.request.MassReqeust;
import cn.xfyun.model.sign.AbstractSignature;
import cn.xfyun.service.finetuning.AbstractMassWebSocketListener;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import okhttp3.*;
import okhttp3.internal.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 星辰Mass Client
 *
 * @author zyding6
 **/
public class MassClient extends WebSocketClient {

    private static final Logger logger = LoggerFactory.getLogger(MassClient.class);

    /**
     * 调用微调大模型时必传,否则不传。对应为模型服务卡片上的resourceId
     */
    private final List<String> patchId;

    /**
     * 调用微调大模型时，对应为模型服务卡片上的serviceid
     * serviceId可从星辰网页获取 <a href="https://training.xfyun.cn/model/add">...</a>
     */
    private final String domain;

    /**
     * 核采样阈值。用于决定结果随机性，取值越高随机性越强即相同的问题得到的不同答案的可能性越高
     * 取值：[0,1]
     * 默认值：0.5
     */
    private final Float temperature;

    /**
     * 从k个候选中随机选择一个（非等概率）
     * 取值：[1, 6]
     * 默认值：4
     */
    private final Integer topK;

    /**
     * 回答的tokens的最大长度
     * 取值：[1,32768]
     * 默认值：2048
     * 限制生成回复的最大 token 数量，
     * max_tokens的限制需要满足输入promptToken + 设置参数max_tokens <= 32768 - 1,
     * 参数设置过大可能导致回答中断，请酌情调整，建议取值16384以下
     */
    private final Integer maxTokens;

    /**
     * 用于关联用户会话
     * 需保障用户下的唯一性
     */
    private final String chatId;

    /**
     * 请求地址
     * 部分模型因部署配置不同，其请求地址可能略有差异，具体可参考服务管控>模型服务列表右侧调用信息
     */
    private final String requestUrl;

    private final String auditing;

    public MassClient(Builder builder) {
        this.okHttpClient = new OkHttpClient
                .Builder()
                .callTimeout(builder.callTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(builder.callTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(builder.readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(builder.writeTimeout, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(builder.retryOnConnectionFailure)
                .build();
        if (null != builder.wsUrl) {
            this.originHostUrl = builder.wsUrl.replace("ws://", "http://").replace("wss://", "https://");
        }
        this.requestUrl = builder.requestUrl;
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.apiSecret = builder.apiSecret;

        this.patchId = builder.patchId;
        this.domain = builder.domain;
        this.temperature = builder.temperature;
        this.topK = builder.topK;
        this.maxTokens = builder.maxTokens;
        this.chatId = builder.chatId;
        this.auditing = builder.auditing;

        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.callTimeout = builder.callTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
    }

    public String getOriginHostUrl() {
        return originHostUrl;
    }

    public String getAuditing() {
        return auditing;
    }

    public List<String> getPatchId() {
        return patchId;
    }

    public String getDomain() {
        return domain;
    }

    public Float getTemperature() {
        return temperature;
    }

    public Integer getTopK() {
        return topK;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public String getChatId() {
        return chatId;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public OkHttpClient getClient() {
        return okHttpClient;
    }

    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }

    public int getCallTimeout() {
        return callTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public int getPingInterval() {
        return pingInterval;
    }

    public AbstractSignature getSignature() {
        return signature;
    }

    /**
     * 精炼文本大模型ws请求方式
     *
     * @param text              对话信息
     *                          [
     *                          {"role": "user", "content": "你好"},
     *                          {"role": "assistant", "content": "你好！"},
     *                          {"role": "user", "content": "你是谁？"},
     *                          {"role": "assistant", "content": "我是 Spark API。"},
     *                          {"role": "user", "content": "你会做什么？"}
     *                          ]
     *                          历史记录最大上限1.2W左右
     *                          按 user -> assistant -> user -> assistant 顺序传递历史记录，最后一条为当前问题
     * @param webSocketListener ftt抽象监听类
     * @throws UnsupportedEncodingException 编码异常
     */
    public void sendWs(List<RoleContent> text, AbstractMassWebSocketListener webSocketListener) throws UnsupportedEncodingException, MalformedURLException, SignatureException {
        // 参数校验
        text = checkText(text);

        // 初始化链接client
        createWebSocketConnect(webSocketListener);

        try {
            String jsonStr = buildParam(text);
            logger.debug("精调文本大模型ws请求URL：{}，参数：{}", this.hostUrl, jsonStr);
            // 发送合成文本
            webSocket.send(jsonStr);
        } catch (Exception e) {
            logger.error("ws消息发送失败：{}", e.getMessage(), e);
        }
    }

    /**
     * 精炼文本大模型post请求方式
     *
     * @param messages 会话记录
     */
    public String sendPost(List<RoleContent> messages) throws IOException {
        // 参数校验
        messages = checkText(messages);

        // 构建入参
        String body = buildPostParam(messages, false);
        logger.debug("精调文本大模型post请求URL：{}，参数：{}", this.requestUrl, body);

        // 构建请求Request
        Request requestUrl = getRequest(body);
        try (Response response = okHttpClient.newCall(requestUrl).execute()) {
            return Objects.requireNonNull(response.body(), "精调文本大模型post请求返回结果为空").string();
        }
    }

    /**
     * 精炼文本大模型sse请求方式
     *
     * @param messages 会话记录
     * @param callback sse回调函数
     */
    public void sendStream(List<RoleContent> messages, Callback callback) {
        // 参数校验
        messages = checkText(messages);

        // 构建入参
        String body = buildPostParam(messages, true);
        logger.debug("精调文本大模型sse请求URL：{}，参数：{}", this.requestUrl, body);

        // 构建sse请求
        Request sseRequest = getSseRequest(body);
        okHttpClient.newCall(sseRequest).enqueue(callback);
    }

    /**
     * 构建sse请求Request
     *
     * @return sseRequest
     */
    private Request getSseRequest(String body) {
        HttpUrl.Builder urlBuilder = Objects
                .requireNonNull(HttpUrl.parse(this.requestUrl), "请求地址错误：" + this.requestUrl)
                .newBuilder();
        Request.Builder builder = new Request
                .Builder()
                .url(urlBuilder.build().toString())
                .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), body));
        builder.addHeader("Authorization", "Bearer " + apiKey);
        builder.addHeader("Accept", "text/event-stream");
        return builder.build();
    }

    /**
     * 构建请求Request
     *
     * @param body 请求体
     * @return Request
     */
    private Request getRequest(String body) {
        HttpUrl.Builder urlBuilder = Objects
                .requireNonNull(HttpUrl.parse(this.requestUrl), "请求地址错误：" + this.requestUrl)
                .newBuilder();
        Request.Builder builder = new Request
                .Builder()
                .url(urlBuilder.build().toString())
                .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), body));
        builder.addHeader("Authorization", "Bearer " + apiKey);
        return builder.build();
    }

    /**
     * 构建请求参数
     *
     * @param text 会话记录
     */
    private String buildParam(List<RoleContent> text) {
        // 发送数据,求数据均为json字符串
        MassReqeust request = new MassReqeust();
        // 请求头
        MassReqeust.Header header = new MassReqeust.Header();
        header.setAppId(appId);
        header.setUid(UUID.randomUUID().toString().substring(0, 10));
        header.setPatchId(patchId);
        request.setHeader(header);

        // 请求参数
        MassReqeust.Parameter parameter = new MassReqeust.Parameter(this);
        request.setParameter(parameter);

        // 请求体
        MassReqeust.Payload payload = new MassReqeust.Payload();
        payload.getMessage().setText(text);
        request.setPayload(payload);
        return StringUtils.gson.toJson(request);
    }

    /**
     * 构建post请求的参数
     *
     * @param messages 会话记录
     * @param stream   是否流式返回
     * @return 流式返回
     */
    private String buildPostParam(List<RoleContent> messages, boolean stream) {
        // 构建完整的URL
        FTTHttpRequest request = new FTTHttpRequest();
        request.setModel(domain);
        request.setMessages(messages);
        request.setStream(stream);
        request.setMaxTokens(maxTokens);
        request.setTemperature(temperature);
        request.setExtraHeaders(StringUtils.gson.fromJson("{\"lora_id\": \"0\"}", JsonObject.class));
        request.setStreamOptions(StringUtils.gson.fromJson("{\"include_usage\": true}", JsonObject.class));

        return StringUtils.gson.toJson(request);
    }

    /**
     * 会话记录参数校验
     *
     * @param text 会话记录
     */
    private static List<RoleContent> checkText(List<RoleContent> text) {
        if (text == null || text.isEmpty()) {
            throw new BusinessException("文本内容不能为空");
        } else if (text.size() > 12000) {
            // 历史记录最大上线1.2W左右，需要判断是能能加入历史
            int startIndex = text.size() - 12000 + 5;
            text = text.subList(startIndex, text.size());
            logger.warn("历史记录长度已截取：{}-{}", startIndex, text.size());
        }
        return text;
    }

    public static final class Builder {
        // websocket相关
        boolean retryOnConnectionFailure = true;
        int callTimeout = 0;
        int connectTimeout = 30000;
        int readTimeout = 30000;
        int writeTimeout = 30000;
        int pingInterval = 0;
        private String appId;
        private String apiKey;
        private String apiSecret;
        /**
         * 部分模型因部署配置不同，其请求地址可能略有差异，具体可参考服务管控>模型服务列表右侧调用信息
         */
        private String wsUrl = "wss://maas-api.cn-huabei-1.xf-yun.com/v1.1/chat";
        /**
         * 若使用 http client 的方式，直接发起request请求，地址如下：
         * <a href="https://maas-api.cn-huabei-1.xf-yun.com/v1/chat/completions">...</a>
         * <p>
         * 若使用 openai sdk，url地址如下：
         * <a href="https://maas-api.cn-huabei-1.xf-yun.com/v1">...</a>
         * <p>
         * 部分模型因部署配置不同，其请求地址可能略有差异，具体可参考服务管控>模型服务列表右侧调用信息
         */
        private String requestUrl = "https://maas-api.cn-huabei-1.xf-yun.com/v1";
        private List<String> patchId;
        private String domain;
        private Float temperature = 0.5f;
        private Integer topK = 4;
        private Integer maxTokens = 2048;
        private String chatId;
        private final String auditing = "default";

        public MassClient build() {
            return new MassClient(this);
        }

        public Builder signatureWs(String resourceId, String serviceId, String appId, String apiKey, String apiSecret) {
            this.appId = appId;
            this.apiKey = apiKey;
            this.apiSecret = apiSecret;
            this.patchId = Collections.singletonList(resourceId);
            this.domain = serviceId;
            return this;
        }

        public Builder signatureHttp(String resourceId, String serviceId, String apiKey) {
            this.apiKey = apiKey;
            this.patchId = Collections.singletonList(resourceId);
            this.domain = serviceId;
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

        public Builder patchId(List<String> patchId) {
            this.patchId = patchId;
            return this;
        }

        public Builder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public Builder temperature(Float temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public Builder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public Builder chatId(String chatId) {
            this.chatId = chatId;
            return this;
        }

        public Builder wsUrl(String wsUrl) {
            this.wsUrl = wsUrl;
            return this;
        }

        public Builder requestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }
    }
}
