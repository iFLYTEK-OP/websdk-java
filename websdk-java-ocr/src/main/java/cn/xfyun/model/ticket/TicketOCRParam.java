package cn.xfyun.model.ticket;

import cn.xfyun.config.DocumentType;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.StringUtils;

/**
 * 通用票证请求参数
 *
 * @author zyding6
 **/
public class TicketOCRParam {

    /**
     * 票证类型
     */
    private DocumentType documentType;

    /**
     * 请求用户服务返回的uid
     */
    private String uid;

    /**
     * 图片的base64信息
     */
    private String imageBase64;

    /**
     * 图像编码，jpg、 jpeg、 png、bmp、webp、tiff
     */
    private String imageFormat;

    /**
     * 1:输出文本行识别结果及位置，不输出图像信息,
     * 3:仅输出文本行的top1结果，输出文本行及文本行位置,文本行base64到json中
     */
    private Integer level;

    public TicketOCRParam(Builder builder) {
        this.documentType = builder.documentType;
        this.uid = builder.uid;
        this.imageBase64 = builder.imageBase64;
        this.imageFormat = builder.imageFormat;
        this.level = builder.level;
    }

    public TicketOCRParam() {
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getImageFormat() {
        return imageFormat;
    }

    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat;
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public void selfCheck() {
        if (documentType == null) {
            throw new BusinessException("票证类型不能为空");
        }
        if (StringUtils.isNullOrEmpty(imageBase64)) {
            throw new BusinessException("图片数据不能为空");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private DocumentType documentType;
        private String uid;
        private String imageBase64;
        private String imageFormat;
        private Integer level;

        private Builder() {
        }

        public TicketOCRParam build() {
            return new TicketOCRParam(this);
        }

        public Builder documentType(DocumentType documentType) {
            this.documentType = documentType;
            return this;
        }

        public Builder uid(String uid) {
            this.uid = uid;
            return this;
        }

        public Builder imageBase64(String imageBase64) {
            this.imageBase64 = imageBase64;
            return this;
        }

        public Builder imageFormat(String imageFormat) {
            this.imageFormat = imageFormat;
            return this;
        }

        public Builder level(Integer level) {
            this.level = level;
            return this;
        }
    }
}
