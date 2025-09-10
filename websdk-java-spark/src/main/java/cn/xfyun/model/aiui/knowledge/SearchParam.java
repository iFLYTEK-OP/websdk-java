package cn.xfyun.model.aiui.knowledge;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.StringUtils;

/**
 * 个性化知识库查询请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class SearchParam {

    private Long uid;
    private String appId;
    private String sceneName;
    private String sid;
    private String channel;

    public SearchParam(Builder builder) {
        this.uid = builder.uid;
        this.sid = builder.sid;
        this.appId = builder.appId;
        this.sceneName = builder.sceneName;
        this.channel = builder.channel;
    }

    public SearchParam() {
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void searchCheck() {
        if (null == uid) {
            throw new BusinessException("uid不能为空");
        } else if (StringUtils.isNullOrEmpty(sceneName)) {
            throw new BusinessException("sceneName不能为空");
        } else if (StringUtils.isNullOrEmpty(appId)) {
            throw new BusinessException("appId不能为空");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public static final class Builder {

        private Long uid;
        private String appId;
        private String sceneName;
        private String sid;
        private String channel;

        private Builder() {
        }

        public SearchParam build() {
            return new SearchParam(this);
        }

        public Builder uid(long uid) {
            this.uid = uid;
            return this;
        }

        public Builder sid(String sid) {
            this.sid = sid;
            return this;
        }

        public Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public Builder sceneName(String sceneName) {
            this.sceneName = sceneName;
            return this;
        }

        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }
    }
}
