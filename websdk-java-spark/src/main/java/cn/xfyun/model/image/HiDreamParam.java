package cn.xfyun.model.image;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * hidream图片生成请求实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class HiDreamParam {

    /**
     * 图片生成描述
     * 字符长度0 ~ 2000的字符串
     */
    private String prompt;

    /**
     * 图片比例
     * 例如 1:1
     */
    @SerializedName("aspect_ratio")
    private String aspectRatio;

    /**
     * 禁止生成的提示词
     * 字符长度0 ~ 2000的字符串
     */
    @SerializedName("negative_prompt")
    private String negativePrompt;

    /**
     * 一次生成的图片数量[1,4]
     */
    @SerializedName("img_count")
    private Integer imgCount;

    /**
     * 生成图片的分辨率 (目前仅支持2k)
     */
    private String resolution;

    /**
     * 图片数组, 支持url或base64
     */
    private List<String> image;

    public HiDreamParam(Builder builder) {
        this.prompt = builder.prompt;
        this.aspectRatio = builder.aspectRatio;
        this.negativePrompt = builder.negativePrompt;
        this.imgCount = builder.imgCount;
        this.resolution = builder.resolution;
        this.image = builder.image;
    }

    public HiDreamParam() {
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getNegativePrompt() {
        return negativePrompt;
    }

    public void setNegativePrompt(String negativePrompt) {
        this.negativePrompt = negativePrompt;
    }

    public Integer getImgCount() {
        return imgCount;
    }

    public void setImgCount(Integer imgCount) {
        this.imgCount = imgCount;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String prompt;
        private String aspectRatio = "1:1";
        private String negativePrompt;
        private int imgCount = 1;
        private String resolution = "2k";
        private List<String> image;

        private Builder() {
        }

        public HiDreamParam build() {
            return new HiDreamParam(this);
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder aspectRatio(String aspectRatio) {
            this.aspectRatio = aspectRatio;
            return this;
        }

        public Builder negativePrompt(String negativePrompt) {
            this.negativePrompt = negativePrompt;
            return this;
        }

        public Builder imgCount(int imgCount) {
            this.imgCount = imgCount;
            return this;
        }

        public Builder resolution(String resolution) {
            this.resolution = resolution;
            return this;
        }

        public Builder image(List<String> image) {
            this.image = image;
            return this;
        }
    }
}
