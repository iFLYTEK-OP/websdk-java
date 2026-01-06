package cn.xfyun.model.rag;

import cn.xfyun.util.StringUtils;

/**
 * 知识库QA对应用请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class QaApply {

    /**
     * QA对id
     */
    private String id;

    /**
     * 知识库id
     */
    private String repoId;

    /**
     * 文件Id
     */
    private String fileId;

    /**
     * 问题
     */
    private String question;

    /**
     * 答案
     */
    private String answer;

    /**
     * 向量类型，Q-仅仅问题做向量、QA-问题和内容一起做向量
     */
    private String embType;

    public QaApply(Builder builder) {
        this.id = builder.id;
        this.fileId = builder.fileId;
        this.repoId = builder.repoId;
        this.question = builder.question;
        this.answer = builder.answer;
        this.embType = builder.embType;
    }

    public QaApply() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepoId() {
        return repoId;
    }

    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getEmbType() {
        return embType;
    }

    public void setEmbType(String embType) {
        this.embType = embType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public static final class Builder {

        private String id;
        private String repoId;
        private String fileId;
        private String question;
        private String answer;
        private String embType;

        private Builder() {
        }

        public QaApply build() {
            return new QaApply(this);
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder repoId(String repoId) {
            this.repoId = repoId;
            return this;
        }

        public Builder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder question(String question) {
            this.question = question;
            return this;
        }

        public Builder answer(String answer) {
            this.answer = answer;
            return this;
        }

        public Builder embType(String embType) {
            this.embType = embType;
            return this;
        }
    }
}
