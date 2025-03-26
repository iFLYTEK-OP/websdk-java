package cn.xfyun.config;

/**
 * @program: websdk-java
 * @description:
 * @author: zyding6
 * @create: 2025/3/14 10:38
 **/
public enum ModeType {

    //代表本地图片base64的模式
    BASE64("base64"),

    //代表url外链的图片地址模式
    LINK("link");

    private String value;

    ModeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
