package cn.xfyun.model;

import com.google.gson.annotations.SerializedName;

/**
 * 视频信息实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class Video {

    /**
     * 视频格式
     * 支持mp4、3gp、asf、avi、rmvb、mpeg、wmv、rm、mpeg4、mpv、mkv、flv、vob格式
     */
    @SerializedName("video_type")
    private String videoType;

    /**
     * 视频地址，长度限制 500字符以内
     * 通过URL外链的音频时长建议限制在2小时内
     */
    @SerializedName("file_url")
    private String fileUrl;

    /**
     * 视频名称
     */
    private String name;

    public Video(Builder builder) {
        this.videoType = builder.videoType;
        this.fileUrl = builder.fileUrl;
        this.name = builder.name;
    }

    public String getAudioType() {
        return videoType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getName() {
        return name;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setName(String name) {
        this.name = name;
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
