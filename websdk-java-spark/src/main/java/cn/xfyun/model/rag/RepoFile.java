package cn.xfyun.model.rag;


import cn.xfyun.util.StringUtils;

import java.util.List;

/**
 * 知识库添加文件请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class RepoFile {

    /**
     * 知识库id
     */
    private String repoId;

    /**
     * 文件id列表，最大20
     */
    private List<String> fileIds;

    public RepoFile(Builder builder) {
        this.repoId = builder.repoId;
        this.fileIds = builder.fileIds;
    }

    public RepoFile() {
    }

    public String getRepoId() {
        return repoId;
    }

    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }

    public List<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<String> fileIds) {
        this.fileIds = fileIds;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public static final class Builder {

        private String repoId;
        private List<String> fileIds;

        private Builder() {
        }

        public RepoFile build() {
            return new RepoFile(this);
        }

        public Builder repoId(String repoId) {
            this.repoId = repoId;
            return this;
        }

        public Builder fileIds(List<String> fileIds) {
            this.fileIds = fileIds;
            return this;
        }
    }
}
