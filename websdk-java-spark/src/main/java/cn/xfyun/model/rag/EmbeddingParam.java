package cn.xfyun.model.rag;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sparkmodel.RoleContent;
import cn.xfyun.util.StringUtils;

import java.util.List;

/**
 * 知识库文件问答请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class EmbeddingParam {

    /**
     * 需要向量化的数据
     */
    private List<RoleContent> messages;

    /**
     * query-用户问题向量化 ; para-知识原文向量化
     */
    private String domain;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * appId
     */
    private String appId;

    /**
     * apiKey
     */
    private String apiKey;

    /**
     * apiSecret
     */
    private String apiSecret;

    public EmbeddingParam(Builder builder) {
        this.messages = builder.messages;
        this.domain = builder.domain;
        this.userId = builder.userId;
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.apiSecret = builder.apiSecret;
    }

    public EmbeddingParam() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<RoleContent> getMessages() {
        return messages;
    }

    public void setMessages(List<RoleContent> messages) {
        this.messages = messages;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void selfCheck() {
        if (StringUtils.isNullOrEmpty(appId) || StringUtils.isNullOrEmpty(apiKey) || StringUtils.isNullOrEmpty(apiSecret)) {
            throw new BusinessException("appId,apiKey,apiSecret can not be null");
        }
        if (messages == null || messages.isEmpty()) {
            throw new BusinessException("messages is null");
        }
        if (StringUtils.isNullOrEmpty(domain)) {
            throw new BusinessException("domain value must between [query,para]");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public static final class Builder {

        private List<RoleContent> messages;
        private String domain;
        private String userId;
        private String appId;
        private String apiKey;
        private String apiSecret;

        private Builder() {
        }

        public EmbeddingParam build() {
            return new EmbeddingParam(this);
        }

        public Builder messages(List<RoleContent> messages) {
            this.messages = messages;
            return this;
        }

        public Builder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder apiSecret(String apiSecret) {
            this.apiSecret = apiSecret;
            return this;
        }
    }
}
