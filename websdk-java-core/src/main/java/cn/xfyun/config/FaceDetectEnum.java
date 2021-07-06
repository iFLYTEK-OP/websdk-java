package cn.xfyun.config;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/6 11:05
 */
public enum FaceDetectEnum {

    ON("1"),
    OFF("0");

    private String value;

    FaceDetectEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
