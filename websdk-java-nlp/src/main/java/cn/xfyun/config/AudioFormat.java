package cn.xfyun.config;

/**
 * 音频类型枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum AudioFormat {

    MP3("mp3"),
    ALAW("alaw"),
    ULAW("ulaw"),
    PCM("pcm"),
    AAC("aac"),
    WAV("wav");

    private final String format;

    AudioFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    @Override
    public String toString() {
        return this.format;
    }
}
