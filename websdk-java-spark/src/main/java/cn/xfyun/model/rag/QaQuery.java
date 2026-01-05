package cn.xfyun.model.rag;

import cn.xfyun.util.StringUtils;

/**
 * 知识库QA对应用查询请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class QaQuery {

    /**
     * 文件Id
     */
    private String fileId;

    /**
     * 知识库id
     */
    private String repoId;

    /**
     * 当前第几页
     */
    private Integer currentPage;

    /**
     * 每页几条
     */
    private Integer pageSize;

    public QaQuery(Builder builder) {
        this.fileId = builder.fileId;
        this.repoId = builder.repoId;
        this.currentPage = builder.currentPage;
        this.pageSize = builder.pageSize;
    }

    public QaQuery() {
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getRepoId() {
        return repoId;
    }

    public void setRepoId(String repoId) {
        this.repoId = repoId;
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

        private String fileId;
        private String repoId;
        private Integer currentPage;
        private Integer pageSize;

        private Builder() {
        }

        public QaQuery build() {
            return new QaQuery(this);
        }

        public Builder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder repoId(String repoId) {
            this.repoId = repoId;
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
