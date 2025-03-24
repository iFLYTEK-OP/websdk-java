package cn.xfyun.model;

import com.google.gson.annotations.SerializedName;

/**
 * @program: websdk-java
 * @description: 音频信息实体类
 * @author: zyding6
 * @create: 2025/3/14 14:28
 **/
public class Audio {

    /**
     * 音频类型，如果不传，取 url 后缀名作为格式
     * 支持类型：mp3、alaw、ulaw、pcm、aac、wav
     */
    private String audioType;

    /**
     * 音频地址，长度限制 500字符以内
     * 通过URL外链的音频时长建议限制在1小时内
     */
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

    @SerializedName("audio_type")
    public String getAudioType() {
        return audioType;
    }

    @SerializedName("file_url")
    public String getFileUrl() {
        return fileUrl;
    }

    public String getName() {
        return name;
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
