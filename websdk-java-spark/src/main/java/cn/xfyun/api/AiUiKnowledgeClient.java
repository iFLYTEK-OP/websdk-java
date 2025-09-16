package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.config.AiUiKnowledge;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.aiui.knowledge.*;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonElement;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 个性化知识库 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/spark/Interact_KM.html#%E4%B8%80%E3%80%81%E6%8E%A5%E5%8F%A3%E8%AF%B4%E6%98%8E">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class AiUiKnowledgeClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(AiUiKnowledgeClient.class);

    private final String apiPassword;

    public AiUiKnowledgeClient(Builder builder) {
        super(builder);
        this.apiPassword = builder.apiPassword;
    }

    public String getApiPassword() {
        return apiPassword;
    }

    /**
     * 用户知识库创建(使用默认spark-1024向量库)
     */
    public String create(CreateParam param) throws IOException {
        // 非空校验
        nullCheck(param);

        // 参数校验
        param.createCheck();

        // 构建JSON类型请求体
        RequestBody body = RequestBody.create(JSON, param.toJSONString());

        // 发送请求
        return send(AiUiKnowledge.CREATE, body, null);
    }

    /**
     * 知识库追加文件上传(内置解析拆分参数并自动构建)
     */
    public String upload(UploadParam param) throws IOException {
        // 非空校验
        nullCheck(param);

        // 参数校验
        param.uploadCheck();

        // 发送请求
        return send(AiUiKnowledge.UPLOAD, param.toFormDataBody(), null);
    }

    /**
     * 删除用户知识库或某个文件
     */
    public String delete(DeleteParam param) throws IOException {
        // 非空校验
        nullCheck(param);

        // 参数校验
        param.deleteCheck();

        // 发送请求
        return send(AiUiKnowledge.DELETE, null, param);
    }

    /**
     * 查询应用已绑定知识库列表及全量知识库列表
     */
    public String list(SearchParam param) throws IOException {
        // 非空校验
        nullCheck(param);

        // 参数校验
        param.searchCheck();

        // 发送请求
        return send(AiUiKnowledge.LIST, null, param);
    }

    /**
     * 用户应用关联绑定知识库
     */
    public String link(LinkParam param) throws IOException {
        // 非空校验
        nullCheck(param);

        // 参数校验
        param.linkCheck();

        // 构建JSON类型请求体
        RequestBody body = RequestBody.create(JSON, param.toJSONString());

        // 发送请求
        return send(AiUiKnowledge.LINK, body, null);
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
    private String send(AiUiKnowledge aiUiKnowledge, RequestBody body, Object param) throws IOException {
        // 请求头
        Map<String, String> header = new HashMap<>(6);
        header.put("Authorization", "Bearer " + this.apiPassword);

        // 拼接参数获取url
        String url = aiUiKnowledge.getUrl();
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(url), "非法的请求路径: " + url).newBuilder();
        if (param != null) {
            JsonElement jsonTree = StringUtils.gson.toJsonTree(param);
            for (Map.Entry<String, JsonElement> entry : jsonTree.getAsJsonObject().entrySet()) {
                if (entry.getValue().isJsonNull()) {
                    continue;
                }
                builder.addQueryParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        url = builder.toString();

        // 请求结果
        logger.debug("{}请求URL：{}，入参：{}", aiUiKnowledge.getDesc(), url, null == body ? "" : body.toString());
        return sendRequest(url, aiUiKnowledge.getMethod(), header, body);
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://sparkcons-rag.cn-huabei-1.xf-yun.com/aiuiKnowledge/aiuiKnowledge/";
        private final String apiPassword;

        public Builder(String apiPassword) {
            super(HOST_URL, null, null, null);
            this.apiPassword = apiPassword;
            // 文件上传有耗时操作, 客户端等待服务器响应的时间调整为120s
            this.readTimeout(120);
        }

        @Override
        public AiUiKnowledgeClient build() {
            return new AiUiKnowledgeClient(this);
        }
    }
}
