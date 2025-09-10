package cn.xfyun.api;

import cn.xfyun.base.websocket.AbstractClient;
import cn.xfyun.base.websocket.WebsocketBuilder;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sparkmodel.SparkChatParam;
import cn.xfyun.model.sparkmodel.request.SparkSendRequest;
import cn.xfyun.util.StringUtils;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.security.SignatureException;

/**
 * 星火助手 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/spark/SparkAssistantAPI.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 **/
public class SparkAssistantClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(SparkAssistantClient.class);

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
     */
    private final String domain;

    /**
     * 助手Id
     */
    private final String assistantId;

    public SparkAssistantClient(Builder builder) {
        super(builder);
        this.originHostUrl = builder.hostUrl;

        this.assistantId = builder.assistantId;
        this.temperature = builder.temperature;
        this.maxTokens = builder.maxTokens;
        this.topK = builder.topK;
        this.domain = builder.domain;
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

    public String getAssistantId() {
        return assistantId;
    }

    /**
     * 发送请求
     * 多轮交互需要将之前的交互历史按照
     * user(image)->user->assistant->user->assistant规则进行拼接，保证最后一条是user的当前问题
     *
     * @param sparkChatParam    请求参数
     * @param webSocketListener 用户自定义ws监听类(AbstractSparkModelWebSocketListener)
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

    public static final class Builder extends WebsocketBuilder<Builder> {

        private String assistantId;
        private String hostUrl = "https://spark-openapi.cn-huabei-1.xf-yun.com/v1/assistants/";
        private float temperature = 0.5F;
        private int maxTokens = 2048;
        private int topK = 4;
        private String domain = "generalv3";

        public SparkAssistantClient build() {
            return new SparkAssistantClient(this);
        }

        public Builder signature(String appId, String apiKey, String apiSecret, String assistantId) {
            super.signature(appId, apiKey, apiSecret);
            this.assistantId = assistantId;
            this.hostUrl = this.hostUrl + assistantId;
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
