package cn.xfyun.config;

/**
 * @program: websdk-java
 * @description:
 * @author: zyding6
 * @create: 2025/3/18 16:02
 **/
public enum VideoFormat {

    MP4("mp4"),
    THREE_GP("3gp"),
    ASF("asf"),
    AVI("avi"),
    RMVB("rmvb"),
    MPEG("mpeg"),
    WMV("wmv"),
    RM("rm"),
    MPEG4("mpeg4"),
    MPV("mpv"),
    MKV("mkv"),
    FLV("flv"),
    VOB("vob");

    private final String format;

    VideoFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    /**
     * 根据格式字符串获取对应的枚举
     */
    public static VideoFormat fromString(String format) {
        if (format != null) {
            for (VideoFormat vf : VideoFormat.values()) {
                if (vf.getFormat().equalsIgnoreCase(format)) {
                    return vf;
                }
            }
        }
        return null; // 或者抛出异常
    }
}
