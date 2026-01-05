package cn.xfyun.model.rag;

import cn.xfyun.util.StringUtils;

/**
 * 知识库查询请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class RepoQuery {

    /**
     * 知识库名称，模糊查询
     */
    private String repoName;

    /**
     * 当前第几页
     */
    private Integer currentPage;

    /**
     * 每页几条
     */
    private Integer pageSize;

    public RepoQuery(Builder builder) {
        this.repoName = builder.repoName;
        this.currentPage = builder.currentPage;
        this.pageSize = builder.pageSize;
    }

    public RepoQuery() {
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public static final class Builder {

        private String repoName;
        private Integer currentPage;
        private Integer pageSize;

        private Builder() {
        }

        public RepoQuery build() {
            return new RepoQuery(this);
        }

        public Builder repoName(String repoName) {
            this.repoName = repoName;
            return this;
        }

        public Builder currentPage(Integer currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public Builder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }
    }
}
