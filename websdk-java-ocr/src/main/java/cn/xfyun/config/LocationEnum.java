package cn.xfyun.config;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/6 17:17
 */
public enum LocationEnum {
    /**
     *   返回文本位置信息
     */
    ON("true"),

    /**
     *   不返回文本位置信息
     */
    OFF("false");

    String value;

    LocationEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
