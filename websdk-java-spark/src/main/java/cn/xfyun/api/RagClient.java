package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.config.RagEnum;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.rag.*;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.*;

/**
 * 知识库问答 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/spark/ChatDoc-API.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class RagClient extends PlatformHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(RagClient.class);

    /**
     * 知识库会话请求地址
     */
    private final String chatUrl;

    /**
     * 新版本向量化请求地址
     */
    private final String embeddingUrl;

    public String getChatUrl() {
        return chatUrl;
    }

    public String getEmbeddingUrl() {
        return embeddingUrl;
    }

    public RagClient(Builder builder) {
        super(builder);
        this.chatUrl = builder.chatUrl;
        this.embeddingUrl = builder.embeddingUrl;
    }

    /**
     * 文档上传
     */
    public String fileUpload(RepoUpload upload) throws IOException {
        // 非空校验
        nullCheck(upload);

        // 构建JSON类型请求体
        RequestBody body = upload.toFormDataBody();

        // 发送请求
        return send(RagEnum.FILE_UPLOAD, body, null);
    }

    /**
     * 文档状态查询
     */
    public String fileStatus(FileStatus status) throws IOException {
        // 非空校验
        nullCheck(status);

        // 构建JSON类型请求体
        RequestBody body = status.toFormDataBody();

        // 发送请求
        return send(RagEnum.FILE_STATUS, body, null);
    }

    /**
     * 文档问答
     */
    public void chat(FileChat chat, WebSocketListener listener) throws MalformedURLException, SignatureException {
        // 参数校验
        chatCheck(chat);

        // 初始化链接client
        WebSocket webSocket = newWebSocket(listener);

        try {
            // 构建请求参数
            String param = buildParam(chat);
            logger.debug("知识库问答ws请求参数：{}", param);
            // 发送请求
            webSocket.send(param);
        } catch (Exception e) {
            logger.error("知识库问答ws消息发送失败", e);
        }
    }

    /**
     * 提交萃取任务
     * 针对单个文档提交萃取任务。 注：只有vectored 状态的文档才可以提交萃取任务
     */
    public String fileExtract(FileExtract extract) throws IOException {
        // 非空校验
        nullCheck(extract);

        // 构建请求体
        RequestBody requestBody = RequestBody.create(JSON, extract.toJSONString());

        // 发送请求
        return send(RagEnum.EXTRACT_CREATE, requestBody, null);
    }

    /**
     * 文件萃取状态查询
     * 根据文件id，查询文件的萃取状态
     */
    public String fileExtractStatus(FileResult result) throws IOException {
        // 非空校验
        nullCheck(result);

        // 构建请求参数
        Map<String, String> parameter = new HashMap<>();
        parameter.put("fileId", result.getFileId());

        // 发送请求
        return send(RagEnum.EXTRACT_STATUS, null, parameter);
    }

    /**
     * 获取萃取结果
     * 根据萃取任务id或者文件id，获取萃取结果
     */
    public String fileExtractResult(FileResult result) throws IOException {
        // 非空校验
        nullCheck(result);

        // 构建请求参数
        Map<String, String> parameter = new HashMap<>();
        parameter.put("taskId", result.getTaskId());
        parameter.put("fileId", result.getFileId());

        // 发送请求
        return send(RagEnum.EXTRACT_RESULT, null, parameter);
    }

    /**
     * QA对应用
     * 将问题和答案作为QA对应用至文件或者知识库
     */
    public String qaApply(QaApply qaApply) throws IOException {
        // 非空校验
        nullCheck(qaApply);

        // 构建请求体
        RequestBody requestBody = RequestBody.create(JSON, qaApply.toJSONString());

        // 发送请求
        return send(RagEnum.QA_APPLY, requestBody, null);
    }

    /**
     * QA对更新
     * 根据QA对id更新QA对
     */
    public String qaUpdate(QaApply qaApply) throws IOException {
        // 非空校验
        nullCheck(qaApply);

        // 构建请求体
        RequestBody requestBody = RequestBody.create(JSON, qaApply.toJSONString());

        // 发送请求
        return send(RagEnum.QA_UPDATE, requestBody, null);
    }

    /**
     * QA对删除
     * 删除采用的QA对
     */
    public String qaDelete(List<String> ids) throws IOException {
        // 非空校验
        nullCheck(ids);

        // 构建请求体
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("ids", String.join(",", ids));
        RequestBody requestBody = builder.build();

        // 发送请求
        return send(RagEnum.QA_DELETE, requestBody, null);
    }

    /**
     * QA对查询
     * 查询文件或者知识库添加的问答对
     */
    public String qaQuery(QaQuery qaQuery) throws IOException {
        // 非空校验
        nullCheck(qaQuery);

        // 构建请求体
        RequestBody requestBody = RequestBody.create(JSON, qaQuery.toJSONString());

        // 发送请求
        return send(RagEnum.QA_PAGE, requestBody, null);
    }

    /**
     * 发起文档总结
     * 针对单个文档发起内容总结/概要。 注：只有 splited、vectoring、vectored 状态的文档才可以发起总结
     */
    public String fileSummaryCreate(String fileId) throws IOException {
        // 非空校验
        nullCheck(fileId);

        // 构建请求体
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("fileId", fileId);
        RequestBody requestBody = builder.build();

        // 发送请求
        return send(RagEnum.FILE_SUMMARY_CREATE, requestBody, null);
    }

    /**
     * 获取文档总结信息
     * 查询文档总结/概要信息
     */
    public String fileSummaryQuery(String fileId) throws IOException {
        // 非空校验
        nullCheck(fileId);

        // 构建请求体
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("fileId", fileId);
        RequestBody requestBody = builder.build();

        // 发送请求
        return send(RagEnum.FILE_SUMMARY_QUERY, requestBody, null);
    }

    /**
     * 文档切分
     * 根据切分符对文档进行切分，上传文件之后会做一次切分，若切分效果不满意可调用此方法重新切分
     */
    public String fileSplit(FileSplit fileSplit) throws IOException {
        // 非空校验
        nullCheck(fileSplit);

        // 构建请求体
        RequestBody requestBody = RequestBody.create(JSON, fileSplit.toJSONString());

        // 发送请求
        return send(RagEnum.FILE_SPLIT, requestBody, null);
    }

    /**
     * 文本向量化
     * 大模型 embedding 能力
     */
    public String embedding(EmbeddingParam param) throws IOException {
        // 非空校验
        nullCheck(param);
        param.selfCheck();

        // 获取鉴权的URL
        String realUrl = Signature.signHostDateAuthorization(embeddingUrl, "POST", param.getApiKey(), param.getApiSecret());

        // 构建请求体
        String body = buildEmbeddingParam(param);

        // 发送请求
        return sendPost(realUrl, JSON, null, body);
    }

    /**
     * 文档向量化
     * 对文档切分的文本块进行向量化操作（embedding），对于文件状态处于splited的文件，可以调用该方法进行向量处理。只有向量化之后的文件才可以发起问答
     */
    public String fileEmbeddingV2(List<String> fileIds) throws IOException {
        // 非空校验
        nullCheck(fileIds);

        // 构建请求体
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("fileIds", String.join(",", fileIds));
        RequestBody requestBody = builder.build();

        // 发送请求
        return send(RagEnum.FILE_EMBEDDING, requestBody, null);
    }

    /**
     * 文档内容相似度检测
     * 根据输入的文本，检索拆分后的相关原始文本块
     */
    public String fileVector(FileVector vector) throws IOException {
        // 非空校验
        nullCheck(vector);

        // 构建请求体
        RequestBody requestBody = RequestBody.create(JSON, vector.toJSONString());

        // 发送请求
        return send(RagEnum.FILE_COMPARE, requestBody, null);
    }

    /**
     * 文档分块内容获取
     * 获取文件分块内容
     */
    public String fileChunks(String fileId) throws IOException {
        // 非空校验
        nullCheck(fileId);

        // 构建请求体
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("fileId", fileId);
        RequestBody requestBody = builder.build();

        // 发送请求
        return send(RagEnum.FILE_CHUNKS, requestBody, null);
    }

    /**
     * 文档详情
     * 查询文件详情
     */
    public String fileInfo(String fileId) throws IOException {
        // 非空校验
        nullCheck(fileId);

        // 构建请求体
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("fileId", fileId);
        RequestBody requestBody = builder.build();

        // 发送请求
        return send(RagEnum.FILE_INFO, requestBody, null);
    }

    /**
     * 文档列表
     * 查询appId下的文件列表
     */
    public String fileList(FileList fileList) throws IOException {
        // 非空校验
        nullCheck(fileList);

        // 构建请求体
        RequestBody requestBody = RequestBody.create(JSON, fileList.toJSONString());

        // 发送请求
        return send(RagEnum.FILE_LIST, requestBody, null);
    }

    /**
     * 文档删除
     * 删除文档，该操作会删除文件向量等相关信息
     */
    public String fileDelete(List<String> fileIds) throws IOException {
        // 非空校验
        nullCheck(fileIds);

        // 构建请求体
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("fileIds", String.join(",", fileIds));
        RequestBody requestBody = builder.build();

        // 发送请求
        return send(RagEnum.FILE_DEL, requestBody, null);
    }

    /**
     * 知识库创建
     * 创建一个新的知识库
     */
    public String repoCreate(RepoCreate create) throws IOException {
        // 非空校验
        nullCheck(create);

        // 构建请求体
        RequestBody requestBody = RequestBody.create(JSON, create.toJSONString());

        // 发送请求
        return send(RagEnum.REPO_CREATE, requestBody, null);
    }

    /**
     * 知识库添加文件
     * 向知识库中添加文件，每次操作最多可添加20个文件进知识库，文件较多时，
     * 需要分多次添加。且同一个文件最多可存在于10个不同知识库。 注：仅文档状态为vectored的文件才可以进行问答
     */
    public String repoAddFile(RepoFile addFile) throws IOException {
        // 非空校验
        nullCheck(addFile);

        // 构建请求体
        RequestBody requestBody = RequestBody.create(JSON, addFile.toJSONString());

        // 发送请求
        return send(RagEnum.REPO_ADD_FILE, requestBody, null);
    }

    /**
     * 知识库移除文件
     * 从知识库中移除文件，一次最多操作20个文件
     */
    public String repoRemoveFile(RepoFile addFile) throws IOException {
        // 非空校验
        nullCheck(addFile);

        // 构建请求体
        RequestBody requestBody = RequestBody.create(JSON, addFile.toJSONString());

        // 发送请求
        return send(RagEnum.REPO_REMOVE_FILE, requestBody, null);
    }

    /**
     * 知识库列表
     * 查询appid下知识库列表
     */
    public String repoList(RepoQuery repoQuery) throws IOException {
        // 非空校验
        nullCheck(repoQuery);

        // 构建请求体
        RequestBody requestBody = RequestBody.create(JSON, repoQuery.toJSONString());

        // 发送请求
        return send(RagEnum.REPO_LIST, requestBody, null);
    }

    /**
     * 知识库详情
     * 查询知识库详情
     */
    public String repoInfo(String repoId) throws IOException {
        // 非空校验
        nullCheck(repoId);

        // 构建请求体
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("repoId", repoId);
        RequestBody requestBody = builder.build();

        // 发送请求
        return send(RagEnum.REPO_INFO, requestBody, null);
    }

    /**
     * 知识库文件列表
     * 查询知识库下所有文件
     */
    public String repoFileList(RepoFileList repoFileList) throws IOException {
        // 非空校验
        nullCheck(repoFileList);

        // 构建请求体
        RequestBody requestBody = RequestBody.create(JSON, repoFileList.toJSONString());

        // 发送请求
        return send(RagEnum.REPO_FILE_LIST, requestBody, null);
    }

    /**
     * 知识库删除
     * 删除知识库
     */
    public String repoDelete(String repoId) throws IOException {
        // 非空校验
        nullCheck(repoId);

        // 构建请求体
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("repoId", repoId);
        RequestBody requestBody = builder.build();

        // 发送请求
        return send(RagEnum.REPO_DEL, requestBody, null);
    }

    /**
     * 构建会话参数
     */
    private String buildParam(FileChat chat) {
        JsonObject sendRequest = new JsonObject();
        sendRequest.addProperty("repoId", chat.getRepoId());
        sendRequest.add("repoIds", StringUtils.gson.toJsonTree(chat.getRepoIds()));
        sendRequest.add("fileIds", StringUtils.gson.toJsonTree(chat.getFileIds()));
        sendRequest.addProperty("llmVersion", chat.getLlmVersion());
        sendRequest.addProperty("topN", chat.getTopN());
        sendRequest.add("messages", StringUtils.gson.toJsonTree(chat.getMessages()));
        sendRequest.add("chatExtends", StringUtils.gson.toJsonTree(chat.getChatExtends()));

        String json = StringUtils.gson.toJson(sendRequest);
        logger.debug("知识库文档请求入参: {}", json);
        return json;
    }

    /**
     * 文本向量化请求参数
     */
    private String buildEmbeddingParam(EmbeddingParam param) {
        JsonObject sendRequest = new JsonObject();
        JsonObject header = new JsonObject();
        JsonObject parameter = new JsonObject();
        JsonObject payload = new JsonObject();

        // 构建请求头
        header.addProperty("app_id", param.getAppId());
        header.addProperty("uid", param.getUserId());
        header.addProperty("status", status);
        sendRequest.add("header", header);

        // 构建parameter
        JsonObject emb = new JsonObject();
        JsonObject feature = new JsonObject();
        feature.addProperty("encoding", encoding);
        feature.addProperty("compress", compress);
        feature.addProperty("format", format);
        emb.addProperty("domain", param.getDomain());
        emb.add("feature", feature);
        parameter.add("emb", emb);
        sendRequest.add("parameter", parameter);

        // 构建payload
        JsonObject messages = new JsonObject();
        messages.addProperty("encoding", encoding);
        messages.addProperty("compress", compress);
        messages.addProperty("format", format);
        messages.addProperty("status", status);
        messages.addProperty("text", Base64.getEncoder().encodeToString(StringUtils.gson.toJson(param.getMessages()).getBytes(StandardCharsets.UTF_8)));
        payload.add("messages", messages);
        sendRequest.add("payload", payload);

        String json = StringUtils.gson.toJson(sendRequest);
        logger.debug("文本向量化请求入参: {}", json);
        return json;
    }

    /**
     * 新建webSocket
     *
     * @param listener WebSocketListener对象
     * @return websocket对象
     */
    private WebSocket newWebSocket(WebSocketListener listener) {
        // 鉴权参数
        Long timestamp = System.currentTimeMillis() / 1000;
        String signature = Signature.generateSignature(appId, timestamp, apiSecret);
        // 构建请求参数
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(chatUrl)).newBuilder();
        urlBuilder.addQueryParameter("appId", appId);
        urlBuilder.addQueryParameter("signature", signature);
        urlBuilder.addQueryParameter("timestamp", String.valueOf(timestamp));
        // 获取Url
        String url = urlBuilder.build().toString();
        url = url.replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();
        // 创建websocket连接
        return okHttpClient.newWebSocket(request, listener);
    }

    /**
     * 会话参数校验
     */
    private void chatCheck(FileChat param) {
        if (param == null) {
            throw new BusinessException("参数不能为空");
        }
    }

    /**
     * 非空参数校验
     */
    private void nullCheck(Object param) {
        if (param == null) {
            throw new BusinessException("参数不能为空");
        }
    }

    /**
     * 发送请求
     */
    private String send(RagEnum ragEnum, RequestBody body, Map<String, String> parameters) throws IOException {
        // 请求头
        Map<String, String> header = new HashMap<>(6);
        Long timestamp = System.currentTimeMillis() / 1000;
        String signature = Signature.generateSignature(appId, timestamp, apiSecret);
        header.put("signature", signature);
        header.put("appId", appId);
        header.put("timestamp", String.valueOf(timestamp));

        // 拼接参数获取url
        String url = hostUrl + ragEnum.getUrl();
        logger.debug("{}请求URL：{}，入参：{}", ragEnum.getDesc(), url, null == body ? "" : body.toString());

        // 请求结果
        return sendRequest(url, ragEnum.getMethod(), header, body, parameters);
    }

    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://chatdoc.xfyun.cn/openapi/v1";
        private String chatUrl = "https://chatdoc.xfyun.cn/openapi/chat";
        private String embeddingUrl = "https://emb-cn-huabei-1.xf-yun.com/";

        public Builder(String appId, String apiSecret) {
            super(HOST_URL, null, appId, null, apiSecret);
        }

        @Override
        public RagClient build() {
            return new RagClient(this);
        }

        public Builder chatUrl(String chatUrl) {
            this.chatUrl = chatUrl;
            return this;
        }

        public Builder embeddingUrl(String embeddingUrl) {
            this.embeddingUrl = embeddingUrl;
            return this;
        }
    }
}
