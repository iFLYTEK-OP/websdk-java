package cn.xfyun.model.rag;

import cn.xfyun.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 知识库文件问答请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class FileVector {

    /**
     * 文件id列表，最大200 (repoId、repoIds、fileIds必传其一)
     */
    private List<String> fileIds;

    /**
     * 向量库文本段查询数量
     */
    private Integer topN;

    /**
     * 用户的问题
     */
    private String content;

    /**
     * 大模型对话自定义扩展字段
     */
    private Map<String, Object> chatExtends;

    public FileVector(Builder builder) {
        this.fileIds = builder.fileIds;
        this.topN = builder.topN;
        this.content = builder.content;
        this.chatExtends = builder.chatExtends;
    }

    public FileVector() {
    }

    public List<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<String> fileIds) {
        this.fileIds = fileIds;
    }

    public Integer getTopN() {
        return topN;
    }

    public void setTopN(Integer topN) {
        this.topN = topN;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getChatExtends() {
        return chatExtends;
    }

    public void setChatExtends(Map<String, Object> chatExtends) {
        this.chatExtends = chatExtends;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public static final class Builder {

        private List<String> fileIds;
        private Integer topN;
        private String content;
        private Map<String, Object> chatExtends;

        private Builder() {
        }

        public FileVector build() {
            return new FileVector(this);
        }

        public Builder fileIds(List<String> fileIds) {
            this.fileIds = fileIds;
            return this;
        }

        public Builder topN(Integer topN) {
            this.topN = topN;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder chatExtends(Map<String, Object> chatExtends) {
            this.chatExtends = chatExtends;
            return this;
        }
    }
}
