package cn.xfyun.model.aiui.knowledge;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.StringUtils;

import java.util.List;

/**
 * 个性化知识库关联请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class LinkParam {

    private Long uid;
    private String appId;
    private String sceneName;
    private String sid;
    private List<Repo> repos;

    public LinkParam(Builder builder) {
        this.uid = builder.uid;
        this.sid = builder.sid;
        this.appId = builder.appId;
        this.sceneName = builder.sceneName;
        this.repos = builder.repos;
    }

    public LinkParam() {
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

    public List<Repo> getRepos() {
        return repos;
    }

    public void setRepos(List<Repo> repos) {
        this.repos = repos;
    }

    public void linkCheck() {
        if (null == uid) {
            throw new BusinessException("uid不能为空");
        } else if (StringUtils.isNullOrEmpty(appId)) {
            throw new BusinessException("appId不能为空");
        } else if (StringUtils.isNullOrEmpty(sceneName)) {
            throw new BusinessException("sceneName不能为空");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public static class Repo {

        private String groupId;
        private String repoName;
        private String threshold;

        public Repo() {
        }

        public Repo(String groupId, String repoName, String threshold) {
            this.groupId = groupId;
            this.repoName = repoName;
            this.threshold = threshold;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getRepoName() {
            return repoName;
        }

        public void setRepoName(String repoName) {
            this.repoName = repoName;
        }

        public String getThreshold() {
            return threshold;
        }

        public void setThreshold(String threshold) {
            this.threshold = threshold;
        }
    }

    public static final class Builder {

        private Long uid;
        private String appId;
        private String sceneName;
        private String sid;
        private List<Repo> repos;

        private Builder() {
        }

        public LinkParam build() {
            return new LinkParam(this);
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

        public Builder repos(List<Repo> repos) {
            this.repos = repos;
            return this;
        }
    }
}
