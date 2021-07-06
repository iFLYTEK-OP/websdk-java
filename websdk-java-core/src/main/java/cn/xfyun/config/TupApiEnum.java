package cn.xfyun.config;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/6 11:57
 */
public enum TupApiEnum {

    AGE("age"),
    SEX("sex"),
    EXPRESSION("expression"),
    FACE_SCORE("face_score");

    private String value;

    TupApiEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
