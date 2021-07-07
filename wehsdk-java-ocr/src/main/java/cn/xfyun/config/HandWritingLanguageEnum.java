package cn.xfyun.config;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/6 17:08
 */
public enum HandWritingLanguageEnum {

    /**
     *   英文
     */
    EN("en"),

    /**
     *   中英文混合
     */
    CN("cn|en");


    private String value;

    HandWritingLanguageEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
