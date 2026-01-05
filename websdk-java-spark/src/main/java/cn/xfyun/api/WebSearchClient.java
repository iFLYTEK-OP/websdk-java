package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.websearch.WebSearchParam;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 聚合搜索 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/spark/Search_API/search_API.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class WebSearchClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(WebSearchClient.class);

    private final String apiPassword;

    public String getApiPassword() {
        return apiPassword;
    }

    public WebSearchClient(Builder builder) {
        super(builder);
        this.apiPassword = builder.apiPassword;
    }

    /**
     * 搜索
     * 1. 支持全文检索、结果重排、时效性识别、内容安全分类等高级能力；
     * 2. 后续将支持对接自建知识库、定制化检索链路等功能；
     * 3. 当前仅对已授权用户开放，需在控制台开通权限
     *
     * @param param 请求参数
     * @return 返回结果
     * @throws IOException 请求异常信息
     */
    public String send(WebSearchParam param) throws IOException {
        // 参数校验
        paramCheck(param);

        // 获取鉴权头
        Map<String, String> header = buildHeader();

        // 构建请求体
        String body = buildParam(param);

        // 发送请求
        return sendPost(hostUrl, JSON, header, body);
    }

    /**
     * 构建请求头
     */
    private Map<String, String> buildHeader() {
        Map<String, String> header = new HashMap<>(6);
        header.put("Authorization", String.format("Bearer %s", apiPassword));
        header.put("Content-Type", "application/json");
        return header;
    }

    /**
     * 参数校验
     */
    private void paramCheck(WebSearchParam param) {
        if (null == param) {
            throw new BusinessException("请求参数不能为空");
        }

        if (StringUtils.isNullOrEmpty(param.getQuery())) {
            throw new BusinessException("搜索内容不能为空");
        }
    }

    /**
     * 构建参数
     */
    private String buildParam(WebSearchParam param) {
        JsonObject request = new JsonObject();

        JsonObject searchParams = new JsonObject();
        searchParams.addProperty("query", param.getQuery());
        searchParams.addProperty("limit", param.getLimit());

        JsonObject enhance = new JsonObject();
        enhance.addProperty("open_rerank", param.getOpenRerank());
        enhance.addProperty("open_full_text", param.getOpenFullText());
        searchParams.add("enhance", enhance);

        request.add("search_params", searchParams);
        String json = StringUtils.gson.toJson(request);
        logger.debug("appId: {}, 聚合搜索URL: {}, 请求体: {}, ", appId, hostUrl, json);
        return json;
    }


    public static class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://search-api-open.cn-huabei-1.xf-yun.com/v2/search";
        private String apiPassword;

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
            this.readTimeout(60);
        }

        public Builder(String appId, String apiPassword) {
            super(HOST_URL, appId, null, null);
            this.apiPassword = apiPassword;
        }

        @Override
        public WebSearchClient build() {
            return new WebSearchClient(this);
        }
    }
}
