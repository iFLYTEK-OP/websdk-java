package cn.xfyun.model.document;

import java.io.File;

/**
 * 文档还原请求参数实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class PDRecParam {

    /**
     * 图片的base64
     */
    private String imgBase64;

    /**
     * 需要还原的文档类型
     */
    private String resultType;

    /**
     * 图片格式
     */
    private String imgFormat;

    /**
     * 还原后文档保存位置 (不传则不保存)
     */
    private File dstFile;

    public PDRecParam(Builder builder) {
        this.imgBase64 = builder.imgBase64;
        this.resultType = builder.resultType;
        this.imgFormat = builder.imgFormat;
        this.dstFile = builder.dstFile;
    }

    public PDRecParam() {
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getImgFormat() {
        return imgFormat;
    }

    public void setImgFormat(String imgFormat) {
        this.imgFormat = imgFormat;
    }

    public File getDstFile() {
        return dstFile;
    }

    public void setDstFile(File dstFile) {
        this.dstFile = dstFile;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String imgBase64;

        private String resultType;

        private String imgFormat;

        private File dstFile;

        private Builder() {
        }

        public PDRecParam build() {
            return new PDRecParam(this);
        }

        public Builder imgBase64(String imgBase64) {
            this.imgBase64 = imgBase64;
            return this;
        }

        public Builder resultType(String resultType) {
            this.resultType = resultType;
            return this;
        }

        public Builder imgFormat(String imgFormat) {
            this.imgFormat = imgFormat;
            return this;
        }

        public Builder dstFile(File dstFile) {
            this.dstFile = dstFile;
            return this;
        }
    }
}
