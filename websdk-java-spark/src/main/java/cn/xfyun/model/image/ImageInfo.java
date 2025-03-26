package cn.xfyun.model.image;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @program: websdk-java
 * @description: 图片生成详情实体类
 * @author: zyding6
 * @create: 2025/3/19 15:18
 **/
public class ImageInfo {


    /**
     * image : ["image"]
     * prompt : 大闹天空
     * aspect_ratio : 1:1
     * negative_prompt :
     * img_count : 1
     * resolution : 2k
     */

    private String prompt;
    @SerializedName("aspect_ratio")
    private String aspectRatio;
    @SerializedName("negative_prompt")
    private String negativePrompt;
    @SerializedName("img_count")
    private int imgCount;
    private String resolution;
    private List<String> image;

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

    public int getImgCount() {
        return imgCount;
    }

    public void setImgCount(int imgCount) {
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
}
