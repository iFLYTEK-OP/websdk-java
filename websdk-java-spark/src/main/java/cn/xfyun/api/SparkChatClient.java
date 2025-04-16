package cn.xfyun.api;

import cn.xfyun.base.webscoket.AbstractClient;
import cn.xfyun.config.SparkModel;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sparkmodel.FunctionCall;
import cn.xfyun.model.sparkmodel.SparkChatParam;
import cn.xfyun.model.sparkmodel.WebSearch;
import cn.xfyun.model.sparkmodel.request.*;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
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
 * 星火大模型 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/spark/Web.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 **/
public class SparkChatClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(SparkChatClient.class);

    /**
     * 指定访问的模型版本:
     * lite 指向Lite版本;
     * generalv3 指向Pro版本;
     * pro-128k 指向Pro-128K版本;
     * generalv3.5 指向Max版本;
     * max-32k 指向Max-32K版本;
     * 4.0Ultra 指向4.0 Ultra版本;
     * kjwx 指向科技文献大模型（重点优化论文问答、写作等垂直领域）;
     */
    private final SparkModel sparkModel;

    /**
     * 核采样阈值。取值越高随机性越强，即相同的问题得到的不同答案的可能性越大
     * ws 取值范围 (0，1] ，默认值0.5
     * http 取值范围[0, 2] 默认值1.0
     */
    private final float temperature;

    /**
     * 模型回答的tokens的最大长度
     * Pro、Max、Max-32K、4.0 Ultra 取值为[1,8192]，默认为4096;
     * Lite、Pro-128K 取值为[1,4096]，默认为4096。
     */
    private final int maxTokens;

    /**
     * 从k个候选中随机选择⼀个（⾮等概率）
     * 取值为[1，6],默认为4
     */
    private final int topK;

    /**
     * 联网搜索插件
     * 当前仅支持web_search联网搜索
     * 仅Pro、Max、Ultra系列模型支持
     */
    private final WebSearch webSearch;

    /**
     * 自定义插件
     * 仅Spark Pro, Spark Max和Spark 4.0Ultra支持[搜索]、[天气]、[日期]、[诗词]、[字词]、[股票]等内置插件；
     */
    private final List<FunctionCall> functions;

    /**
     * 生成过程中核采样方法概率阈值
     * 例如，取值为0.8时，仅保留概率加起来大于等于0.8的最可能token的最小集合作为候选集。
     * 取值越大，生成的随机性越高；取值越低，生成的确定性越高。
     * 取值范围(0, 1] 默认值1
     */
    private final Integer topP;

    /**
     * 重复词的惩罚值
     * 取值范围[-2.0,2.0] 默认0
     */
    private final Float presencePenalty;

    /**
     * 频率惩罚值
     * 取值范围[-2.0,2.0] 默认0
     */
    private final Float frequencyPenalty;

    /**
     * 设置为true时，触发function call结果中tool_calls以数组格式返回，默认为 false，则以json格式返回
     * 默认表示关闭
     */
    private final Boolean toolCallsSwitch;

    /**
     * 设置模型自动选择调用的函数：
     * auto：传了tool时默认为auto，模型自动选择调用的函数
     * none：模型禁用函数调用
     * required：模型始终选择一个或多个函数进行调用
     * {"type": "function", "function": {"name": "my_function"}} ：模型强制调用指定函数
     */
    private final Object toolChoice;

    /**
     * { "type": "json_object" } 指定模型输出json格式
     * 使用 JSON 模式时，请始终指示模型通过对话中的某些消息（例如通过系统或用户消息）生成 JSON
     */
    private final String responseType;

    /**
     * 不使用的插件
     */
    private final List<String> suppressPlugin;

    public SparkChatClient(Builder builder) {
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

        this.sparkModel = builder.sparkModel;
        this.temperature = builder.temperature;
        this.maxTokens = builder.maxTokens;
        this.topK = builder.topK;
        this.webSearch = builder.webSearch;
        this.functions = builder.functions;
        this.topP = builder.topP;
        this.presencePenalty = builder.presencePenalty;
        this.frequencyPenalty = builder.frequencyPenalty;
        this.toolCallsSwitch = builder.toolCallsSwitch;
        this.toolChoice = builder.toolChoice;
        this.responseType = builder.responseType;
        this.suppressPlugin = builder.suppressPlugin;

        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.callTimeout = builder.callTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
    }

    public String getResponseType() {
        return responseType;
    }

    public List<String> getSuppressPlugin() {
        return suppressPlugin;
    }

    public Integer getTopP() {
        return topP;
    }

    public Float getPresencePenalty() {
        return presencePenalty;
    }

    public Float getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public Boolean getToolCallsSwitch() {
        return toolCallsSwitch;
    }

    public Object getToolChoice() {
        return toolChoice;
    }

    public SparkModel getSparkModel() {
        return sparkModel;
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

    public WebSearch getWebSearch() {
        return webSearch;
    }

    public List<FunctionCall> getFunctions() {
        return functions;
    }

    /**
     * 星火大模型会话ws请求方式
     * 1. 计费包含接口的输入和输出内容；
     * 2. 1 token约等于1.5个中文汉字 或者 0.8个英文单词；
     * 3. 仅Spark Pro, Spark Max和Spark 4.0Ultra支持[搜索]、[天气]、[日期]、[诗词]、[字词]、[股票]等内置插件；
     * 4. Spark 4.0Ultra、Max现已支持system、Function Call功能；
     * 5. 当前仅Spark 4.0Ultra、Max版本支持返回联网检索的信源标题及地址
     * 6. 多语种当前仅支持日、韩、俄、阿、法、西、葡、德 8种语言
     *
     * @param param             请求参数
     * @param webSocketListener 用户自定义ws监听类 (AbstractSparkModelWebSocketListener)
     */
    public void send(SparkChatParam param, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        // 参数校验
        paramCheck(param, false);

        // 初始化ws链接
        WebSocket socket = newWebSocket(webSocketListener);

        try {
            // 构建请求参数
            String body = buildParam(param);
            logger.debug("星火文本大模型ws请求参数：{}", body);
            // 发送请求
            socket.send(body);
        } catch (Exception e) {
            logger.error("ws消息发送失败", e);
        }
    }

    /**
     * 星火大模型会话http请求方式
     * 1. 计费包含接口的输入和输出内容；
     * 2. 1 token约等于1.5个中文汉字 或者 0.8个英文单词；
     * 3. 仅Spark Pro, Spark Max和Spark 4.0Ultra支持[搜索]、[天气]、[日期]、[诗词]、[字词]、[股票]等内置插件；
     * 4. Spark 4.0Ultra、Max现已支持system、Function Call功能；
     * 5. 当前仅Spark 4.0Ultra、Max版本支持返回联网检索的信源标题及地址
     * 6. 多语种当前仅支持日、韩、俄、阿、法、西、葡、德 8种语言
     *
     * @param param 请求参数
     */
    public String send(SparkChatParam param) throws IOException {
        // 参数校验
        paramCheck(param, true);

        // 构建入参
        String body = buildPostParam(param, false);
        logger.debug("{}post请求URL：{}，参数：{}", sparkModel.getDesc(), this.originHostUrl, body);

        // 构建请求Request
        Request requestUrl = getRequest(body, false);
        try (Response response = okHttpClient.newCall(requestUrl).execute()) {
            return Objects.requireNonNull(response.body(), sparkModel.getDesc() + "post请求返回结果为空").string();
        }
    }

    /**
     * 星火大模型会话sse请求方式
     * 1. 计费包含接口的输入和输出内容；
     * 2. 1 token约等于1.5个中文汉字 或者 0.8个英文单词；
     * 3. 仅Spark Pro, Spark Max和Spark 4.0Ultra支持[搜索]、[天气]、[日期]、[诗词]、[字词]、[股票]等内置插件；
     * 4. Spark 4.0Ultra、Max现已支持system、Function Call功能；
     * 5. 当前仅Spark 4.0Ultra、Max版本支持返回联网检索的信源标题及地址
     * 6. 多语种当前仅支持日、韩、俄、阿、法、西、葡、德 8种语言
     *
     * @param param 请求参数
     */
    public void send(SparkChatParam param, Callback callback) {
        // 参数校验
        paramCheck(param, true);

        // 构建入参
        String body = buildPostParam(param, true);
        logger.debug("{}post请求URL：{}，参数：{}", sparkModel.getDesc(), this.originHostUrl, body);

        // 构建sse请求
        Request sseRequest = getRequest(body, true);
        okHttpClient.newCall(sseRequest).enqueue(callback);
    }

    /**
     * 参数校验
     */
    private void paramCheck(SparkChatParam param, boolean isPost) {
        // 非空校验
        if (param == null) {
            throw new BusinessException("参数不能为空");
        }

        // 验证消息不能为空
        if (param.getMessages() == null || param.getMessages().isEmpty()) {
            throw new BusinessException("文本内容不能为空");
        }

        // 验证模型不支持POST请求的情况
        if (sparkModel == SparkModel.CHAT_MULTILANG && isPost) {
            throw new BusinessException(sparkModel.getDesc() + "暂不支持post请求");
        }

        // 验证模型不支持联网搜索的情况
        if (null != param.getWebSearch() && param.getWebSearch().isEnable() && !sparkModel.isWebSearchEnable()) {
            throw new BusinessException(sparkModel.getDesc() + "暂不支持联网搜索");
        }

        // 验证模型不支持函数调用的情况
        if (null != param.getFunctions() && !sparkModel.isFunctionEnable()) {
            throw new BusinessException(sparkModel.getDesc() + "暂不支持function调用");
        }
    }

    /**
     * 构建请求Request
     *
     * @param body 请求体
     * @return Request
     */
    private Request getRequest(String body, boolean isStream) {
        HttpUrl.Builder urlBuilder = Objects
                .requireNonNull(HttpUrl.parse(this.originHostUrl), "请求地址错误：" + this.originHostUrl)
                .newBuilder();
        Request.Builder builder = new Request
                .Builder()
                .url(urlBuilder.build().toString())
                .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), body));
        builder.addHeader("Authorization", "Bearer " + apiKey);
        if (isStream) {
            // sse流式请求
            builder.addHeader("Accept", "text/event-stream");
        }
        return builder.build();
    }

    /**
     * 构建post请求参数
     *
     * @param param 请求参数
     * @param stream  是否流式输出
     * @return 入参
     */
    private String buildPostParam(SparkChatParam param, boolean stream) {
        // 封装请求参数
        SparkChatPostRequest sendRequest = new SparkChatPostRequest(this);
        sendRequest.setModel(sparkModel.getDomain());
        sendRequest.setStream(stream);
        sendRequest.setUser(param.getUserId());
        sendRequest.setMessages(param.getMessages());

        // 封装用户使用的工具
        List<Object> toolList = new ArrayList<>();
        // 搜索插件
        WebSearch webSearch = param.getWebSearch();
        webSearch = (null != webSearch) ? webSearch : this.webSearch;
        if (null != webSearch) {
            JsonObject tool = new JsonObject();
            tool.addProperty("type", "web_search");
            tool.add("web_search", StringUtils.gson.toJsonTree(webSearch));
            toolList.add(tool);
        }

        // 配置函数调用
        List<FunctionCall> functions = param.getFunctions();
        functions = (null != functions) && !functions.isEmpty() ? functions : this.functions;
        if (null != functions && !functions.isEmpty()) {
            functions.forEach(fun -> {
                JsonObject obj = new JsonObject();
                obj.addProperty("type", "function");
                obj.add("function", StringUtils.gson.toJsonTree(fun));
                toolList.add(obj);
            });
        }
        if (!toolList.isEmpty()) {
            sendRequest.setTools(toolList);
        }

        // 指定模型输出json格式
        if (!StringUtils.isNullOrEmpty(responseType)) {
            sendRequest.setResponseFormat(new SparkChatPostRequest.ResponseFormat());
        }
        return StringUtils.gson.toJson(sendRequest);
    }

    /**
     * 构建参数
     */
    private String buildParam(SparkChatParam param) {
        // 发送数据,求数据均为json字符串
        SparkChatRequest sendRequest = new SparkChatRequest();

        // 请求头
        String userId = param.getUserId();
        if (null == userId) {
            //用户没传输参数使用默认UUID生成
            userId = UUID.randomUUID().toString().substring(0, 10);
        }
        SparkChatRequest.Header header = new SparkChatRequest.Header(appId, userId);
        sendRequest.setHeader(header);

        // 请求参数
        SparkChatRequest.Parameter parameter = new SparkChatRequest.Parameter();
        SparkChatRequest.Parameter.Chat chat = new SparkChatRequest.Parameter.Chat();
        chat.setDomain(sparkModel.getDomain());
        chat.setTemperature(temperature);
        chat.setMaxTokens(maxTokens);
        chat.setTopK(topK);
        // 配置搜索插件
        WebSearch webSearch = param.getWebSearch();
        webSearch = (null != webSearch) ? webSearch : this.webSearch;
        if (null != webSearch && sparkModel.isWebSearchEnable()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "web_search");
            obj.add("web_search", StringUtils.gson.toJsonTree(webSearch));
            chat.setTools(Collections.singletonList(obj));
        }
        chat.setChatId(param.getChatId());
        parameter.setChat(chat);
        sendRequest.setParameter(parameter);

        // 请求体
        List<FunctionCall> functions = param.getFunctions();
        functions = (null != functions) && !functions.isEmpty() ? functions : this.functions;
        SparkChatRequest.Payload payload = new SparkChatRequest.Payload();
        SparkChatRequest.Payload.Message message = new SparkChatRequest.Payload.Message();
        message.setText(param.getMessages());
        // 配置函数调用
        if (null != functions && !functions.isEmpty() && sparkModel.isFunctionEnable()) {
            SparkChatRequest.Payload.Function function = new SparkChatRequest.Payload.Function();
            function.setText(functions);
            payload.setFunctions(function);
        }
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
        private int readTimeout = 60000;
        private int writeTimeout = 30000;
        private int pingInterval = 0;
        private String appId;
        private String apiKey;
        private String apiSecret;
        private SparkModel sparkModel;
        private String hostUrl = "https://spark-api-open.xf-yun.com/v1/chat/completions";
        private float temperature = 0.5F;
        private int maxTokens = 4096;
        private int topK = 4;
        private WebSearch webSearch;
        private List<FunctionCall> functions;
        private Integer topP;
        private Float presencePenalty;
        private Float frequencyPenalty;
        private Boolean toolCallsSwitch;
        private Object toolChoice;
        private String responseType;
        private List<String> suppressPlugin;

        public SparkChatClient build() {
            return new SparkChatClient(this);
        }

        public Builder signatureWs(String appId, String apiKey, String apiSecret, SparkModel model) {
            this.appId = appId;
            this.apiKey = apiKey;
            this.apiSecret = apiSecret;
            this.sparkModel = model;
            this.hostUrl = model.getUrl();
            return this;
        }

        public Builder signatureHttp(String apiPassword, SparkModel model) {
            this.apiKey = apiPassword;
            this.sparkModel = model;
            this.temperature = 1;
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

        public Builder webSearch(WebSearch webSearch) {
            this.webSearch = webSearch;
            return this;
        }

        public Builder functions(List<FunctionCall> functions) {
            this.functions = functions;
            return this;
        }

        public Builder hostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        public Builder topP(int topP) {
            this.topP = topP;
            return this;
        }

        public Builder presencePenalty(float presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this;
        }

        public Builder frequencyPenalty(float frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public Builder toolCallsSwitch(boolean toolCallsSwitch) {
            this.toolCallsSwitch = toolCallsSwitch;
            return this;
        }

        public Builder toolChoice(Object toolChoice) {
            this.toolChoice = toolChoice;
            return this;
        }

        public Builder responseType(String responseType) {
            this.responseType = responseType;
            return this;
        }

        public Builder suppressPlugin(List<String> suppressPlugin) {
            this.suppressPlugin = suppressPlugin;
            return this;
        }
    }
}
