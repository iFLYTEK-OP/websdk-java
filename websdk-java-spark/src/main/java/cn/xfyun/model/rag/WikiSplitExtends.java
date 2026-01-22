package cn.xfyun.model.rag;

import java.util.List;

/**
 * 知识库上传请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class WikiSplitExtends {

    /**
     * 分段分隔符，支持多分隔符，base64编码
     */
    private List<String> chunkSeparators;

    /**
     * 分分段最大长度，超过强行切分，默认2000
     */
    private Integer chunkSize;

    /**
     * 分段最小长度，小于这个长度的会聚合，默认256
     */
    private Integer minChunkSize;

    public WikiSplitExtends() {
    }

    public WikiSplitExtends(Builder builder) {
        this.chunkSeparators = builder.chunkSeparators;
        this.chunkSize = builder.chunkSize;
        this.minChunkSize = builder.minChunkSize;
    }

    public List<String> getChunkSeparators() {
        return chunkSeparators;
    }

    public void setChunkSeparators(List<String> chunkSeparators) {
        this.chunkSeparators = chunkSeparators;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Integer getMinChunkSize() {
        return minChunkSize;
    }

    public void setMinChunkSize(Integer minChunkSize) {
        this.minChunkSize = minChunkSize;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private List<String> chunkSeparators;
        private Integer chunkSize;
        private Integer minChunkSize;

        private Builder() {
        }

        public WikiSplitExtends build() {
            return new WikiSplitExtends(this);
        }

        public Builder chunkSeparators(List<String> chunkSeparators) {
            this.chunkSeparators = chunkSeparators;
            return this;
        }

        public Builder chunkSize(Integer chunkSize) {
            this.chunkSize = chunkSize;
            return this;
        }

        public Builder minChunkSize(Integer minChunkSize) {
            this.minChunkSize = minChunkSize;
            return this;
        }
    }
}
