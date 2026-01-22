package cn.xfyun.model.rag;

import cn.xfyun.util.StringUtils;

import java.util.List;

/**
 * 知识库文件切分请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class FileSplit {

    /**
     * 文件id列表，最大200 (repoId、repoIds、fileIds必传其一)
     */
    private List<String> fileIds;

    /**
     * 是否用默认切分策略，如果需要自定义切分符，该值需要设置为false
     */
    private Boolean isSplitDefault;

    /**
     * 按什么类型拆分，传固定 "wiki"
     */
    private String splitType;

    /**
     * 大模型对话自定义扩展字段
     */
    private String wikiSplitExtends;

    public FileSplit(Builder builder) {
        this.fileIds = builder.fileIds;
        this.isSplitDefault = builder.isSplitDefault;
        this.splitType = builder.splitType;
        this.wikiSplitExtends = builder.wikiSplitExtends;
    }

    public FileSplit() {
    }

    public List<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<String> fileIds) {
        this.fileIds = fileIds;
    }

    public Boolean getSplitDefault() {
        return isSplitDefault;
    }

    public void setSplitDefault(Boolean splitDefault) {
        isSplitDefault = splitDefault;
    }

    public String getSplitType() {
        return splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }

    public String getWikiSplitExtends() {
        return wikiSplitExtends;
    }

    public void setWikiSplitExtends(String wikiSplitExtends) {
        this.wikiSplitExtends = wikiSplitExtends;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public static final class Builder {

        private List<String> fileIds;
        private Boolean isSplitDefault;
        private String splitType = "wiki";
        private String wikiSplitExtends;

        private Builder() {
        }

        public FileSplit build() {
            return new FileSplit(this);
        }

        public Builder fileIds(List<String> fileIds) {
            this.fileIds = fileIds;
            return this;
        }

        public Builder isSplitDefault(Boolean isSplitDefault) {
            this.isSplitDefault = isSplitDefault;
            return this;
        }

        public Builder splitType(String splitType) {
            this.splitType = splitType;
            return this;
        }

        public Builder wikiSplitExtends(String wikiSplitExtends) {
            this.wikiSplitExtends = wikiSplitExtends;
            return this;
        }
    }
}
