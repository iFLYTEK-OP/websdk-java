package cn.xfyun.config;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/27 9:59
 */
public enum ImageRecEnum {


    /**
     * 场景识别
     */
    SCENE("http://tupapi.xfyun.cn/v1/scene"),


    /**
     * 物体识别
     */
    CURRENCY("http://tupapi.xfyun.cn/v1/currency");

    private String value;

    ImageRecEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
