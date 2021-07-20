package cn.xfyun.config;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/9 14:34
 */
public enum IseResultLevelEnum {

    ENTIRETY("entirety"),

    SIMPLE("simple");

    private String value;

    IseResultLevelEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
