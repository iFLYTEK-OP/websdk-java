package cn.xfyun.model;

import com.google.gson.annotations.SerializedName;

/**
 * @program: websdk-java
 * @description: 视频信息实体类
 * @author: zyding6
 * @create: 2025/3/14 14:28
 **/
public class Video {

    /**
     * 视频格式
     * 支持mp4、3gp、asf、avi、rmvb、mpeg、wmv、rm、mpeg4、mpv、mkv、flv、vob格式
     */
    private String videoType;

    /**
     * 视频地址，长度限制 500字符以内
     * 通过URL外链的音频时长建议限制在2小时内
     */
    private String fileUrl;

    /**
     * 音频名称
     */
    private String name;

    public Video(Builder builder) {
        this.videoType = builder.videoType;
        this.fileUrl = builder.fileUrl;
        this.name = builder.name;
    }

    @SerializedName("video_type")
    public String getAudioType() {
        return videoType;
    }

    @SerializedName("file_url")
    public String getFileUrl() {
        return fileUrl;
    }

    public String getName() {
        return name;
    }

    public static final class Builder {

        private String videoType;
        private String fileUrl;
        private String name;

        public Video build() {
            return new Video(this);
        }

        public Builder videoType(String videoType) {
            this.videoType = videoType;
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
