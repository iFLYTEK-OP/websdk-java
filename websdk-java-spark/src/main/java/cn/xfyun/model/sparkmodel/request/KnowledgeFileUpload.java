package cn.xfyun.model.sparkmodel.request;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.StringUtils;

import java.io.File;

/**
 * 知识库文件上传请求实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class KnowledgeFileUpload {

    /**
     * 文件对象
     */
    private File file;

    /**
     * 知识库名称
     */
    private String knowledgeName;

    /**
     * 该文件使用功能，
     * 目前取值：文件提取（file-extract）
     */
    private String purpose;

    public KnowledgeFileUpload(Builder builder) {
        this.file = builder.file;
        this.knowledgeName = builder.knowledgeName;
        this.purpose = builder.purpose;
    }

    public KnowledgeFileUpload() {
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getKnowledgeName() {
        return knowledgeName;
    }

    public void setKnowledgeName(String knowledgeName) {
        this.knowledgeName = knowledgeName;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void selfCheck() {
        if (StringUtils.isNullOrEmpty(knowledgeName)) {
            throw new BusinessException("知识库名称不能为空");
        } else if (null == purpose) {
            throw new BusinessException("purpose不能为空");
        } else if (null == file) {
            throw new BusinessException("媒体文件不能为空");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private File file;
        private String knowledgeName;
        private String purpose = "file-extract";

        private Builder() {
        }

        public KnowledgeFileUpload build() {
            return new KnowledgeFileUpload(this);
        }

        public Builder file(File file) {
            this.file = file;
            return this;
        }

        public Builder knowledgeName(String knowledgeName) {
            this.knowledgeName = knowledgeName;
            return this;
        }

        public Builder purpose(String purpose) {
            this.purpose = purpose;
            return this;
        }
    }
}
