package cn.xfyun.model.llmocr;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.StringUtils;

/**
 * 大模型通用文档识别请求参数
 *
 * @author <zyding6@iflytek.com>
 **/
public class LLMOcrParam {

    /**
     * 请求用户服务返回的uid，用户及设备级别个性化功能依赖此参数
     * 最大长度50
     */
    private String uid;
    /**
     * 请求方确保唯一的设备标志，设备级别个性化功能依赖此参数
     * 最大长度50
     */
    private String did;
    /**
     * 客户端请求的会话唯一标识
     * 最大长度64
     */
    private String requestId;
    /**
     * 图片base64信息
     */
    private String imageBase64;
    /**
     * 图片格式
     */
    private String format;

    public LLMOcrParam() {
    }

    public LLMOcrParam(Builder builder) {
        this.uid = builder.uid;
        this.did = builder.did;
        this.requestId = builder.requestId;
        this.imageBase64 = builder.imageBase64;
        this.format = builder.format;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void selfCheck() {
        if (StringUtils.isNullOrEmpty(imageBase64)) {
            throw new BusinessException("图片信息不能为空");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String uid;
        private String did;
        private String requestId;
        private String imageBase64;
        private String format;

        private Builder() {
        }

        public LLMOcrParam build() {
            return new LLMOcrParam(this);
        }

        public Builder uid(String uid) {
            this.uid = uid;
            return this;
        }

        public Builder did(String did) {
            this.did = did;
            return this;
        }

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder imageBase64(String imageBase64) {
            this.imageBase64 = imageBase64;
            return this;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }
    }
}
