package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.model.compliance.text.TextCompParam;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 文本合规 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/nlp/TextModeration/API.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class TextComplianceClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(TextComplianceClient.class);

    /**
     * 是否全匹配：
     * 1 代表是
     * 0 代表否
     * 默认取值0，匹配到敏感词则不再匹配，不会返回所有敏感分类。
     */
    private final int isMatchAll;

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

    public int getIsMatchAll() {
        return isMatchAll;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getLibIds() {
        return libIds;
    }

    public String send(TextCompParam param) throws Exception {
        // 参数校验
        paramCheck(param);

        // 构建鉴权参数
        Map<String, String> parameters = Signature.getAuth(appId, apiKey, apiSecret);

        // 构建请求体
        String body = paramHandler(param);

        // 发送请求
        return sendPost(hostUrl, JSON, null, body, parameters);
    }

    /**
     * 参数校验
     */
    private void paramCheck(TextCompParam param) {
        if (param == null) {
            throw new BusinessException("参数不能为空");
        }
        param.selfCheck();
    }

    /**
     * 参数封装
     */
    private String paramHandler(TextCompParam param) {
        // 封装参数
        if (null == param.getIsMatchAll()) {
            param.setIsMatchAll(this.isMatchAll);
        }
        if (null == param.getCategories()) {
            param.setCategories(this.categories);
        }
        if (null == param.getLibIds()) {
            param.setLibIds(this.libIds);
        }
        String json = StringUtils.gson.toJson(param);
        logger.debug("文本合规请求参数: {}", json);
        return json;
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://audit.iflyaisol.com/audit/v2/syncText";
        private int isMatchAll = 0;
        private List<String> categories;
        private List<String> libIds;

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
        }

        @Override
        public TextComplianceClient build() {
            return new TextComplianceClient(this);
        }

        public Builder isMatchAll(int isMatchAll) {
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
