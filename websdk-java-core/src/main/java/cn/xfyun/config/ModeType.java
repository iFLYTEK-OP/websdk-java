package cn.xfyun.config;

/**
 * 请求类型模式分类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum ModeType {

    //代表本地图片base64的模式
    BASE64("base64"),

    //代表url外链的图片地址模式
    LINK("link");

    private final String value;

    ModeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
