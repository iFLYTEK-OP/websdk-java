package cn.xfyun.config;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/8 10:04
 */
public enum HttpRequestEnum {

    FORM("application/x-www-form-urlencoded; charset=utf-8"),

    JSON("application/json; charset=utf-8"),

    BINARY("binary/octet-stream");


    private String value;

    HttpRequestEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
