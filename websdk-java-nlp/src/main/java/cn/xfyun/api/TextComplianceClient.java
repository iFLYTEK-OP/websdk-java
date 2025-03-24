package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 文本合规 API
 *
 * @author zyding
 * @version 1.0
 * @date 2025/3/13 15:32
 */
public class TextComplianceClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(TextComplianceClient.class);

    /**
     * 是否全匹配：
     * 1 代表是
     * 0 代表否
     * 默认取值0，匹配到敏感词则不再匹配，不会返回所有敏感分类。
     */
    private final boolean isMatchAll;

    /**
     * 指定检测的敏感分类：
     * pornDetection 色情
     * violentTerrorism 暴恐
     * political 涉政
     * lowQualityIrrigation 低质量灌水
     * contraband 违禁
     * advertisement 广告
     * uncivilizedLanguage 不文明用语
     */
    private final List<String> categories;

    /**
     * 指定自定义词库id列表，通过接口创建词库后返回，可以同时携带多个黑白名单id：
     */
    private final List<String> libIds;

    public TextComplianceClient(Builder builder) {
        super(builder);
        this.isMatchAll = builder.isMatchAll;
        this.categories = builder.categories;
        this.libIds = builder.libIds;
    }

    public boolean isMatchAll() {
        return isMatchAll;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getLibIds() {
        return libIds;
    }


    public String send(String text) throws Exception {
        return send(text, null, null, null);
    }

    public String send(String text, List<String> libIds) throws Exception {
        return send(text, libIds, null, null);
    }

    public String send(String text, List<String> libIds, List<String> categories) throws Exception {
        return send(text, libIds, categories, null);
    }

    public String send(String text, List<String> libIds, boolean isMatchAll) throws Exception {
        return send(text, libIds, null, isMatchAll);
    }

    public String send(String text, boolean isMatchAll) throws Exception {
        return send(text, null, null, isMatchAll);
    }

    public String send(String text, List<String> libIds, List<String> categories, Boolean isMatchAll) throws Exception {
        if (StringUtils.isNullOrEmpty(text)) {
            throw new BusinessException("text不能为空");
        }
        Map<String, String> parameters = Signature.getAuth(appId, apiKey, apiSecret);
        return sendPost(hostUrl, JSON, null, buildParam(text, libIds, categories, isMatchAll), parameters);
    }

    private String buildParam(String text, List<String> libIds, List<String> categories, Boolean isMatchAll) {
        // 发送数据,求数据均为json字符串
        HashMap<String, Object> request = new HashMap<>();
        request.put("is_match_all", (null == isMatchAll ? this.isMatchAll : isMatchAll) ? 1 : 0);
        request.put("content", text);
        request.put("lib_ids", null == libIds || libIds.isEmpty() ? this.libIds : libIds);
        request.put("categories", null == categories || categories.isEmpty() ? this.categories : categories);
        return StringUtils.toJson(request);
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://audit.iflyaisol.com/audit/v2/syncText";
        private Boolean isMatchAll = false;
        private List<String> categories;
        private List<String> libIds;

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
        }

        @Override
        public TextComplianceClient build() {
            TextComplianceClient client = new TextComplianceClient(this);
            return client;
        }

        public Builder isMatchAll(Boolean isMatchAll) {
            this.isMatchAll = isMatchAll;
            return this;
        }

        public Builder categories(List<String> categories) {
            this.categories = categories;
            return this;
        }

        public Builder libIds(List<String> libIds) {
            this.libIds = libIds;
            return this;
        }
    }
}
