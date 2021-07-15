package cn.xfyun.config;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/9 14:27
 */
public enum IseLanguageEnum {

    /**
     *  英语
     */
    EN_US("en_us"),

    /**
     *  汉语
     */
    ZH_CN("zh_cn");

    private String value;

    IseLanguageEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
