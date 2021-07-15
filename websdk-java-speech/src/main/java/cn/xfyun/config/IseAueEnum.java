package cn.xfyun.config;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/9 14:24
 */
public enum IseAueEnum {

    /**
     *  未压缩的 pcm 格式音频
     */
    RAW("raw"),

    /**
     *  标准开源speex
     */
    SPEEX("speex");

    private String value;

    IseAueEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
