package cn.xfyun.model.rag;

import cn.xfyun.util.StringUtils;

/**
 * 知识库文档列表请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class FileList {

    /**
     * 文件名称，模糊查询
     */
    private String fileName;

    /**
     * 文件后缀
     */
    private String extName;

    /**
     * 文件类型
     */
    private String fileStatus;

    /**
     * 当前第几页
     */
    private Integer currentPage;

    /**
     * 每页几条
     */
    private Integer pageSize;

    public FileList(Builder builder) {
        this.fileName = builder.fileName;
        this.extName = builder.extName;
        this.fileStatus = builder.fileStatus;
        this.currentPage = builder.currentPage;
        this.pageSize = builder.pageSize;
    }

    public FileList() {
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

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
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

        private String fileName;
        private String extName;
        private String fileStatus;
        private Integer currentPage;
        private Integer pageSize;

        private Builder() {
        }

        public FileList build() {
            return new FileList(this);
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder extName(String extName) {
            this.extName = extName;
            return this;
        }

        public Builder fileStatus(String fileStatus) {
            this.fileStatus = fileStatus;
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
