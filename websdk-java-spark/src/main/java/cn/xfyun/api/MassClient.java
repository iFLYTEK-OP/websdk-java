package cn.xfyun.api;

import cn.xfyun.base.websocket.AbstractClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.mass.MassParam;
import cn.xfyun.model.mass.request.MassHttpRequest;
import cn.xfyun.model.mass.request.MassReqeust;
import cn.xfyun.util.StringUtils;
import okhttp3.*;
import okhttp3.internal.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 星辰Mass Client
 *
 * @author <zyding6@ifytek.com>
 **/
public class MassClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(MassClient.class);

    /**
     * 调用微调大模型时必传,否则不传。对应为模型服务卡片上的resourceId
     */
    private final List<String> patchId;

    /**
     * 调用微调大模型时，对应为模型服务卡片上的modelId
     * modelId可从星辰网页获取 <a href="https://training.xfyun.cn/model/add">...</a>
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
     * 请求地址
     * 部分模型因部署配置不同，其请求地址可能略有差异，具体可参考服务管控>模型服务列表右侧调用信息
     */
    private final String requestUrl;

    private final String auditing;

    /**
     * 关闭联网检索功能
     * 取值：[true,false]；默认值：true
     * 该参数仅DeepSeek-R1和DeepSeek-V3支持
     */
    private final boolean searchDisable;

    /**
     * 展示检索信源信息
     * 取值：[true,false]；默认值：false
     * 该参数仅DeepSeek-R1和DeepSeek-V3支持。
     * 开启联网检索功能后当该参数设置为true，且触发了联网检索功能，会先返回检索信源列表，
     * 然后再返回大模型回复结果，否则仅返回大模型回复结果
     */
    private final boolean showRefLabel;

    /**
     * 针对流式响应模式的扩展配置，如控制是否在响应中包含API调用统计信息等附加数据
     * 默认值为{"include_usage": True}
     */
    private final Map<String, Object> streamOptions;

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
        this.auditing = builder.auditing;
        this.searchDisable = builder.searchDisable;
        this.showRefLabel = builder.showRefLabel;
        this.streamOptions = builder.streamOptions;

        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.callTimeout = builder.callTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
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

    public String getRequestUrl() {
        return requestUrl;
    }

    public boolean isSearchDisable() {
        return searchDisable;
    }

    public boolean isShowRefLabel() {
        return showRefLabel;
    }

    public Map<String, Object> getStreamOptions() {
        return streamOptions;
    }

    /**
     * 精炼文本大模型ws请求方式
     *
     * @param param             请求参数
     * @param webSocketListener 自定义抽象监听类 (AbstractMassWebSocketListener)
     */
    public void send(MassParam param, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        // 参数校验
        textCheck(param);

        // 初始化链接client
        WebSocket socket = newWebSocket(webSocketListener);

        try {
            String jsonStr = buildParam(param);
            logger.debug("精调文本大模型ws请求参数：{}", jsonStr);
            // 发送合成文本
            socket.send(jsonStr);
        } catch (Exception e) {
            logger.error("ws消息发送失败", e);
        }
    }

    /**
     * 精炼文本大模型post请求方式
     *
     * @param param 请求参数
     */
    public String send(MassParam param) throws IOException {
        // 参数校验
        textCheck(param);

        // 构建入参
        String body = buildPostParam(param, false);

        // 构建请求Request
        Request requestUrl = getRequest(body);
        try (Response response = okHttpClient.newCall(requestUrl).execute()) {
            return Objects.requireNonNull(response.body(), "精调文本大模型post请求返回结果为空").string();
        }
    }

    /**
     * 精炼文本大模型sse请求方式
     *
     * @param param    请求参数
     * @param callback sse回调函数
     */
    public void send(MassParam param, Callback callback) {
        // 参数校验
        textCheck(param);

        // 构建入参
        String body = buildPostParam(param, true);

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
     * @param param 请求参数
     */
    private String buildParam(MassParam param) {
        // 发送数据,请求数据均为json字符串
        MassReqeust request = new MassReqeust();
        // 请求头
        MassReqeust.Header header = new MassReqeust.Header();
        header.setAppId(appId);
        header.setUid(param.getUserId());
        header.setPatchId(patchId);
        request.setHeader(header);

        // 请求参数
        MassReqeust.Parameter parameter = new MassReqeust.Parameter(this);
        parameter.getChat().setChatId(param.getChatId());
        parameter.getChat().setSearchDisable(searchDisable);
        parameter.getChat().setShowRefLabel(showRefLabel);
        request.setParameter(parameter);

        // 请求体
        MassReqeust.Payload payload = new MassReqeust.Payload();
        payload.getMessage().setText(param.getMessages());
        request.setPayload(payload);
        return StringUtils.gson.toJson(request);
    }

    /**
     * 构建post请求的参数
     *
     * @param param  请求参数
     * @param stream 是否流式返回
     * @return 流式返回
     */
    private String buildPostParam(MassParam param, boolean stream) {
        MassHttpRequest request = new MassHttpRequest();
        request.setModel(domain);
        request.setMessages(param.getMessages());
        request.setStream(stream);
        request.setMaxTokens(maxTokens);
        request.setTemperature(temperature);

        // 设置请求头
        Map<String, Object> headers = param.getExtraHeaders();
        if (headers != null && !headers.isEmpty()) {
            request.setExtraHeaders(StringUtils.gson.toJsonTree(headers));
        }

        // 构造请求体
        Map<String, Object> body = Optional.ofNullable(param.getExtraBody()).orElseGet(HashMap::new);
        if (!body.containsKey("show_ref_label")) {
            body.put("show_ref_label", showRefLabel);
        }
        if (!body.containsKey("search_disable")) {
            body.put("search_disable", searchDisable);
        }
        request.setExtraBody(StringUtils.gson.toJsonTree(body));

        // 设置流式选项
        if (streamOptions != null && !streamOptions.isEmpty()) {
            request.setStreamOptions(StringUtils.gson.toJsonTree(streamOptions));
        }

        // 转换为 JSON 字符串并打印日志
        String json = StringUtils.gson.toJson(request);
        logger.debug("精调文本大模型 http 请求 URL：{}，参数：{}", this.requestUrl, body);

        return json;
    }

    /**
     * 会话记录参数校验
     *
     * @param param 参数
     */
    private void textCheck(MassParam param) {
        if (param == null) {
            throw new BusinessException("参数不能为空");
        } else if (param.getMessages() == null || param.getMessages().isEmpty()) {
            throw new BusinessException("文本内容不能为空");
        }
    }

    public static final class Builder {

        /**
         * websocket相关
         */
        boolean retryOnConnectionFailure = true;
        int callTimeout = 0;
        int connectTimeout = 30000;
        int readTimeout = 60000;
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
        private final String auditing = "default";
        private boolean searchDisable = true;
        private boolean showRefLabel = false;
        private Map<String, Object> streamOptions;

        public MassClient build() {
            return new MassClient(this);
        }

        public Builder signatureWs(String resourceId, String modelId, String appId, String apiKey, String apiSecret) {
            this.appId = appId;
            this.apiKey = apiKey;
            this.apiSecret = apiSecret;
            this.patchId = Collections.singletonList(resourceId);
            this.domain = modelId;
            return this;
        }

        public Builder signatureHttp(String resourceId, String modelId, String apiKey) {
            this.apiKey = apiKey;
            this.patchId = Collections.singletonList(resourceId);
            this.domain = modelId;
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

        public Builder wsUrl(String wsUrl) {
            this.wsUrl = wsUrl;
            return this;
        }

        public Builder requestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }

        public Builder searchDisable(boolean searchDisable) {
            this.searchDisable = searchDisable;
            return this;
        }

        public Builder showRefLabel(boolean showRefLabel) {
            this.showRefLabel = showRefLabel;
            return this;
        }

        public Builder streamOptions(Map<String, Object> streamOptions) {
            this.streamOptions = streamOptions;
            return this;
        }
    }
}
