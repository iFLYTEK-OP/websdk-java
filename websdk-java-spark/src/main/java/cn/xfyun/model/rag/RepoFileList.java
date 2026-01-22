package cn.xfyun.model.rag;

import cn.xfyun.util.StringUtils;

/**
 * 知识库文件列表查询请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class RepoFileList {

    /**
     * 知识库id
     */
    private String repoId;

    /**
     * 文件名称，模糊查询
     */
    private String fileName;

    /**
     * 文件后缀
     */
    private String extName;

    /**
     * 当前第几页
     */
    private Integer currentPage;

    /**
     * 每页几条
     */
    private Integer pageSize;

    public RepoFileList(Builder builder) {
        this.repoId = builder.repoId;
        this.extName = builder.extName;
        this.fileName = builder.fileName;
        this.currentPage = builder.currentPage;
        this.pageSize = builder.pageSize;
    }

    public RepoFileList() {
    }

    public String getRepoId() {
        return repoId;
    }

    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
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

        private String repoId;
        private String fileName;
        private String extName;
        private Integer currentPage;
        private Integer pageSize;

        private Builder() {
        }

        public RepoFileList build() {
            return new RepoFileList(this);
        }

        public Builder repoId(String repoId) {
            this.repoId = repoId;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder extName(String extName) {
            this.extName = extName;
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
