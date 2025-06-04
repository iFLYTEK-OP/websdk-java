package cn.xfyun.model.intsigocr;

import cn.xfyun.config.IntsigRecgEnum;

/**
 * intsig识别请求参数实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class IntsigParam {

    /**
     * 图片的base64编码
     */
    private String imageBase64;

    /**
     * 可选传参的识别类型
     */
    private IntsigRecgEnum intsigRecgEnum;

    /**
     * 图片类型
     */
    private String imageFormat;

    public IntsigParam(Builder builder) {
        this.imageBase64 = builder.imageBase64;
        this.intsigRecgEnum = builder.intsigRecgEnum;
        this.imageFormat = builder.imageFormat;
    }

    public IntsigParam() {
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public IntsigRecgEnum getIntsigRecgEnum() {
        return intsigRecgEnum;
    }

    public void setIntsigRecgEnum(IntsigRecgEnum intsigRecgEnum) {
        this.intsigRecgEnum = intsigRecgEnum;
    }

    public String getImageFormat() {
        return imageFormat;
    }

    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String imageBase64;

        private IntsigRecgEnum intsigRecgEnum;

        private String imageFormat;

        private Builder() {
        }

        public IntsigParam build() {
            return new IntsigParam(this);
        }

        public Builder imageBase64(String imageBase64) {
            this.imageBase64 = imageBase64;
            return this;
        }

        public Builder intsigRecgEnum(IntsigRecgEnum intsigRecgEnum) {
            this.intsigRecgEnum = intsigRecgEnum;
            return this;
        }

        public Builder imageFormat(String imageFormat) {
            this.imageFormat = imageFormat;
            return this;
        }
    }
}
