package cn.xfyun.api;

import cn.xfyun.base.websocket.AbstractClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.model.sparkmodel.FileContent;
import cn.xfyun.model.sparkmodel.FunctionCall;
import cn.xfyun.model.sparkmodel.request.KnowledgeFileUpload;
import cn.xfyun.model.sparkmodel.request.SparkCustomRequest;
import cn.xfyun.util.OkHttpUtils;
import cn.xfyun.util.StringUtils;
import okhttp3.*;
import okhttp3.internal.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 星火大模型可定制化 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/spark/OptionalAPI.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 **/
public class SparkCustomClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(SparkCustomClient.class);

    /**
     * 指定访问的模型版本: 指向Max版本
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
     * 自定义插件
     * 仅Spark Pro, Spark Max和Spark 4.0Ultra支持[搜索]、[天气]、[日期]、[诗词]、[字词]、[股票]等内置插件；
     */
    private final List<FunctionCall> functions;

    /**
     * 创建知识库的url
     */
    private final String createKnowledgeUrl;

    /**
     * 知识库文件上传接口url
     */
    private final String uploadFileUrl;

    /**
     * 每个用户的id，非必传字段，用于后续扩展
     */
    private final String userId;

    public SparkCustomClient(Builder builder) {
        if (builder.okHttpClient != null) {
            // 使用用户提供的okHttpClient
            this.okHttpClient = builder.okHttpClient;
        } else {
            // 复用全局的okHttpClient
            this.okHttpClient = OkHttpUtils.client.newBuilder()
                    .connectTimeout(builder.connectTimeout, TimeUnit.MILLISECONDS)
                    .readTimeout(builder.readTimeout, TimeUnit.MILLISECONDS)
                    .writeTimeout(builder.writeTimeout, TimeUnit.MILLISECONDS)
                    .callTimeout(builder.callTimeout, TimeUnit.MILLISECONDS)
                    .pingInterval(builder.pingInterval, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(builder.retryOnConnectionFailure)
                    .build();
        }
        this.originHostUrl = builder.hostUrl;
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.apiSecret = builder.apiSecret;

        this.domain = builder.domain;
        this.temperature = builder.temperature;
        this.topK = builder.topK;
        this.maxTokens = builder.maxTokens;
        this.functions = builder.functions;
        this.createKnowledgeUrl = builder.createKnowledgeUrl;
        this.uploadFileUrl = builder.uploadFileUrl;
        this.userId = builder.userId;

        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.callTimeout = builder.callTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
    }

    public String getCreateKnowledgeUrl() {
        return createKnowledgeUrl;
    }

    public String getUploadFileUrl() {
        return uploadFileUrl;
    }

    public List<FunctionCall> getFunctions() {
        return functions;
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

    public String getUserId() {
        return userId;
    }

    /**
     * 星火自定义大模型ws请求方式
     *
     * @param text              会话记录
     * @param functions         函数调用
     * @param webSocketListener 抽象监听类(AbstractSparkModelWebSocketListener)
     */
    public void send(List<FileContent> text,
                     List<FunctionCall> functions,
                     WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        // 参数校验
        textCheck(text);

        // 初始化链接client
        WebSocket webSocket = newWebSocket(webSocketListener);

        try {
            String jsonStr = buildParam(text, functions);
            logger.info("星火自定义大模型ws参数：{}", jsonStr);
            // 发送合成文本
            webSocket.send(jsonStr);
        } catch (Exception e) {
            logger.error("ws消息发送失败", e);
        }
    }

    /**
     * 创建知识库
     * 上传文件前需要先创建知识库，向知识库中上传文件。
     *
     * @param knowledgeName 知识库名称
     *                      仅支持：中文 英文 数字 _-
     *                      需要保证唯一性，建议加上业务前缀
     *                      长度不超过32，合法字符
     */
    public String create(String knowledgeName) throws IOException {
        // 参数校验
        createCheck(knowledgeName);

        // 通用鉴权
        String realUrl = Signature.signHostDateAuthorization(
                createKnowledgeUrl, "POST", apiKey, apiSecret);

        // 构建请求Request
        Request requestUrl = getRequest(knowledgeName, realUrl);

        // 发送请求
        try (Response response = okHttpClient.newCall(requestUrl).execute()) {
            return Objects.requireNonNull(response.body(), "创建知识库请求返回结果为空").string();
        }
    }

    /**
     * 上传文件到知识库
     * 上传文件前需要先创建知识库，向知识库中上传文件。
     *
     * @param upload 上传入参
     */
    public String upload(KnowledgeFileUpload upload) throws IOException {
        // 参数校验
        uploadCheck(upload);

        // 通用鉴权
        String realUrl = Signature.signHostDateAuthorization(
                uploadFileUrl, "POST", apiKey, apiSecret);

        // 构建入参
        Request requestUrl = getFormDataRequest(upload, realUrl);

        // 发送请求
        try (Response response = okHttpClient.newCall(requestUrl).execute()) {
            return Objects.requireNonNull(response.body(), "上传文件到知识库请求返回结果为空").string();
        }
    }

    /**
     * 创建知识库参数校验
     */
    private void uploadCheck(KnowledgeFileUpload upload) {
        if (upload == null) {
            throw new BusinessException("上传文件参数不能为空");
        }
        upload.selfCheck();
    }

    /**
     * 创建知识库参数校验
     */
    private void createCheck(String knowledgeName) {
        if (StringUtils.isNullOrEmpty(knowledgeName)) {
            throw new BusinessException("知识库名称不能为空");
        } else if (knowledgeName.length() > 32) {
            throw new BusinessException("知识库名称长度不能超过32字符");
        }
    }

    /**
     * 会话请求参数校验
     */
    private void textCheck(List<FileContent> text) {
        if (text == null || text.isEmpty()) {
            throw new BusinessException("文本内容不能为空");
        }
    }

    /**
     * 构建会话参数
     *
     * @param text      会话内容
     * @param functions 会话使用的函数
     */
    private String buildParam(List<FileContent> text, List<FunctionCall> functions) {
        // 发送数据,请求数据均为json字符串
        SparkCustomRequest request = new SparkCustomRequest();
        // 请求头
        SparkCustomRequest.Header header = new SparkCustomRequest.Header(appId, userId);
        request.setHeader(header);

        // 请求参数
        SparkCustomRequest.Parameter parameter = new SparkCustomRequest.Parameter(this);
        request.setParameter(parameter);

        // 请求体
        SparkCustomRequest.Payload payload = new SparkCustomRequest.Payload();
        SparkCustomRequest.Payload.Message message = new SparkCustomRequest.Payload.Message();
        message.setText(text);
        payload.setMessage(message);
        // 配置函数调用
        functions = (null != functions) && !functions.isEmpty() ? functions : this.functions;
        if (null != functions && !functions.isEmpty()) {
            SparkCustomRequest.Payload.Function function = new SparkCustomRequest.Payload.Function();
            function.setText(functions);
            payload.setFunctions(function);
        }
        request.setPayload(payload);
        return StringUtils.gson.toJson(request);
    }

    /**
     * 获取formData的body
     *
     * @param param 上传信息
     * @return RequestBody
     */
    private Request getFormDataRequest(KnowledgeFileUpload param, String url) {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("purpose", param.getPurpose())
                .addFormDataPart("kb_id", param.getKnowledgeName())
                .addFormDataPart("file", param.getFile().getName(),
                        RequestBody.create(MediaType.parse("text/plain"), param.getFile()))
                .build();
        // 构建请求Request
        return new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("x-appid", appId)
                .build();
    }

    /**
     * 构建请求Request
     *
     * @param knowledgeName 知识库名称
     * @param url           请求地址
     * @return Request
     */
    private Request getRequest(String knowledgeName, String url) {
        RequestBody requestBody = new FormBody.Builder()
                .add("kb_id", knowledgeName)
                .build();
        return new Request.Builder()
                .url(url)
                .method("POST", requestBody)
                .addHeader("x-appid", appId)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
    }

    public static final class Builder {

        /**
         * websocket相关
         */
        boolean retryOnConnectionFailure = true;
        int callTimeout = 0;
        int connectTimeout = 30000;
        int readTimeout = 30000;
        int writeTimeout = 30000;
        int pingInterval = 0;
        private String appId;
        private String apiKey;
        private String apiSecret;
        private String hostUrl = "https://sparkcube-api.xf-yun.com/v1/customize";
        private String createKnowledgeUrl = "https://sparkcube-api.xf-yun.com/v1/knowledge/create";
        private String uploadFileUrl = "https://sparkcube-api.xf-yun.com/v1/files";
        private String domain = "max";
        private Float temperature = 0.5f;
        private Integer topK = 4;
        private Integer maxTokens = 4096;
        private List<FunctionCall> functions;
        private String userId;
        private OkHttpClient okHttpClient;

        public SparkCustomClient build() {
            return new SparkCustomClient(this);
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

        public Builder hostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        public Builder createKnowledgeUrl(String createKnowledgeUrl) {
            this.createKnowledgeUrl = createKnowledgeUrl;
            return this;
        }

        public Builder uploadFileUrl(String uploadFileUrl) {
            this.uploadFileUrl = uploadFileUrl;
            return this;
        }

        public Builder functions(List<FunctionCall> functions) {
            this.functions = functions;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder okHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }
    }
}
