package cn.xfyun.config;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 11:38
 */
public enum IdcardEnum {

    /**
     *   返回文本位置信息
     */
    ON("1"),

    /**
     *   不返回文本位置信息
     */
    OFF("0");

    private String value;

    IdcardEnum(String value){
        this.value = value;
    }


    public String getValue() {
        return value;
    }
}
