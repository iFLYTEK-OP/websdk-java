package cn.xfyun.model.rag;

import cn.xfyun.util.StringUtils;

import java.util.List;

/**
 * 知识库提交萃取任务请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class FileExtract {

    /**
     * 文件id
     */
    private String fileId;

    /**
     * 分片长度，根据这个长度将文件内容分块，然后根据每个分块抽取问题答案
     */
    private Integer chunkSize;

    /**
     * 每个分片问题数（通过改该值限制抽取问题数，非100%准确）
     */
    private Integer numPerChunk;

    /**
     * 答案长度（通过改该值限制答案长度，非100%准确）
     */
    private Integer answerSize;

    /**
     * 主题偏好
     */
    private List<String> topicPreference;

    /**
     * 是否包含答案，false的话仅有问题
     */
    private Boolean includeAnswer;

    /**
     * 回调地址
     */
    private String notifyUrl;

    public FileExtract(Builder builder) {
        this.fileId = builder.fileId;
        this.chunkSize = builder.chunkSize;
        this.numPerChunk = builder.numPerChunk;
        this.answerSize = builder.answerSize;
        this.topicPreference = builder.topicPreference;
        this.includeAnswer = builder.includeAnswer;
        this.notifyUrl = builder.notifyUrl;
    }

    public FileExtract() {
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Integer getNumPerChunk() {
        return numPerChunk;
    }

    public void setNumPerChunk(Integer numPerChunk) {
        this.numPerChunk = numPerChunk;
    }

    public Integer getAnswerSize() {
        return answerSize;
    }

    public void setAnswerSize(Integer answerSize) {
        this.answerSize = answerSize;
    }

    public List<String> getTopicPreference() {
        return topicPreference;
    }

    public void setTopicPreference(List<String> topicPreference) {
        this.topicPreference = topicPreference;
    }

    public Boolean getIncludeAnswer() {
        return includeAnswer;
    }

    public void setIncludeAnswer(Boolean includeAnswer) {
        this.includeAnswer = includeAnswer;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public static final class Builder {

        private String fileId;
        private Integer chunkSize;
        private Integer numPerChunk;
        private Integer answerSize;
        private List<String> topicPreference;
        private Boolean includeAnswer;
        private String notifyUrl;

        private Builder() {
        }

        public FileExtract build() {
            return new FileExtract(this);
        }

        public Builder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder chunkSize(Integer chunkSize) {
            this.chunkSize = chunkSize;
            return this;
        }

        public Builder numPerChunk(Integer numPerChunk) {
            this.numPerChunk = numPerChunk;
            return this;
        }

        public Builder answerSize(Integer answerSize) {
            this.answerSize = answerSize;
            return this;
        }

        public Builder topicPreference(List<String> topicPreference) {
            this.topicPreference = topicPreference;
            return this;
        }

        public Builder includeAnswer(Boolean includeAnswer) {
            this.includeAnswer = includeAnswer;
            return this;
        }

        public Builder notifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
            return this;
        }
    }
}
