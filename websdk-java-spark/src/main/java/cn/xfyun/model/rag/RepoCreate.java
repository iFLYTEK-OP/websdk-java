package cn.xfyun.model.rag;


import cn.xfyun.util.StringUtils;

/**
 * 知识库创建请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class RepoCreate {

    /**
     * 知识库名称，唯一
     */
    private String repoName;

    /**
     * 知识库简介
     */
    private String repoDesc;

    /**
     * 知识库标签
     */
    private String repoTags;

    public RepoCreate(Builder builder) {
        this.repoName = builder.repoName;
        this.repoDesc = builder.repoDesc;
        this.repoTags = builder.repoTags;
    }

    public RepoCreate() {
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getRepoDesc() {
        return repoDesc;
    }

    public void setRepoDesc(String repoDesc) {
        this.repoDesc = repoDesc;
    }

    public String getRepoTags() {
        return repoTags;
    }

    public void setRepoTags(String repoTags) {
        this.repoTags = repoTags;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public static final class Builder {

        private String repoName;
        private String repoDesc;
        private String repoTags;

        private Builder() {
        }

        public RepoCreate build() {
            return new RepoCreate(this);
        }

        public Builder repoName(String repoName) {
            this.repoName = repoName;
            return this;
        }

        public Builder repoDesc(String repoDesc) {
            this.repoDesc = repoDesc;
            return this;
        }

        public Builder repoTags(String repoTags) {
            this.repoTags = repoTags;
            return this;
        }
    }
}
