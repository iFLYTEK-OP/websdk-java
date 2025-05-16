package cn.xfyun.model;

import com.google.gson.annotations.SerializedName;

/**
 * 音频信息实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class Audio {

    /**
     * 音频类型，如果不传，取 url 后缀名作为格式
     * 支持类型：mp3、alaw、ulaw、pcm、aac、wav
     */
    @SerializedName("audio_type")
    private String audioType;

    /**
     * 音频地址，长度限制 500字符以内
     * 通过URL外链的音频时长建议限制在1小时内
     */
    @SerializedName("file_url")
    private String fileUrl;

    /**
     * 音频名称
     */
    private String name;

    public Audio(Builder builder) {
        this.audioType = builder.audioType;
        this.fileUrl = builder.fileUrl;
        this.name = builder.name;
    }

    public String getAudioType() {
        return audioType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getName() {
        return name;
    }

    public void setAudioType(String audioType) {
        this.audioType = audioType;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final class Builder {

        private String audioType;
        private String fileUrl;
        private String name;

        public Audio build() {
            return new Audio(this);
        }

        public Builder audioType(String audioType) {
            this.audioType = audioType;
            return this;
        }

        public Builder fileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }
    }
}
