package cn.xfyun.model.rag;

import cn.xfyun.model.sparkmodel.RoleContent;
import java.util.List;
import java.util.Map;

/**
 * 知识库文件问答请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class FileChat {

    /**
     * 知识库id，单个知识库最多包括100个文档 (repoId、repoIds、fileIds必传其一)
     */
    private String repoId;

    /**
     * 知识库id列表，最大100 (repoId、repoIds、fileIds必传其一)
     */
    private List<String> repoIds;

    /**
     * 文件id列表，最大200 (repoId、repoIds、fileIds必传其一)
     */
    private List<String> fileIds;

    /**
     * 底层大模型版本,可选值generalv3.5、vdeepseekv3。
     * generalv3.5：星火大模型 Spark Max，vdeepseekv3：DeepSeek V3
     */
    private String llmVersion;

    /**
     * 向量库文本段查询数量
     */
    private Integer topN;

    /**
     * 问答内容列表，按时间正序，最后一条为最新提问
     */
    private List<RoleContent> messages;

    /**
     * 大模型对话自定义扩展字段
     */
    private Map<String, Object> chatExtends;

    public FileChat(Builder builder) {
        this.repoId = builder.repoId;
        this.repoIds = builder.repoIds;
        this.fileIds = builder.fileIds;
        this.llmVersion = builder.llmVersion;
        this.topN = builder.topN;
        this.messages = builder.messages;
        this.chatExtends = builder.chatExtends;
    }

    public FileChat() {
    }

    public String getRepoId() {
        return repoId;
    }

    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }

    public List<String> getRepoIds() {
        return repoIds;
    }

    public void setRepoIds(List<String> repoIds) {
        this.repoIds = repoIds;
    }

    public List<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<String> fileIds) {
        this.fileIds = fileIds;
    }

    public String getLlmVersion() {
        return llmVersion;
    }

    public void setLlmVersion(String llmVersion) {
        this.llmVersion = llmVersion;
    }

    public Integer getTopN() {
        return topN;
    }

    public void setTopN(Integer topN) {
        this.topN = topN;
    }

    public List<RoleContent> getMessages() {
        return messages;
    }

    public void setMessages(List<RoleContent> messages) {
        this.messages = messages;
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

    public static final class Builder {

        private String repoId;
        private List<String> repoIds;
        private List<String> fileIds;
        private String llmVersion;
        private Integer topN;
        private List<RoleContent> messages;
        private Map<String, Object> chatExtends;

        private Builder() {
        }

        public FileChat build() {
            return new FileChat(this);
        }

        public Builder repoId(String repoId) {
            this.repoId = repoId;
            return this;
        }

        public Builder repoIds(List<String> repoIds) {
            this.repoIds = repoIds;
            return this;
        }

        public Builder fileIds(List<String> fileIds) {
            this.fileIds = fileIds;
            return this;
        }

        public Builder llmVersion(String llmVersion) {
            this.llmVersion = llmVersion;
            return this;
        }

        public Builder topN(Integer topN) {
            this.topN = topN;
            return this;
        }

        public Builder messages(List<RoleContent> messages) {
            this.messages = messages;
            return this;
        }

        public Builder chatExtends(Map<String, Object> chatExtends) {
            this.chatExtends = chatExtends;
            return this;
        }
    }
}
