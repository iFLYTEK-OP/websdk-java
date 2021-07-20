package cn.xfyun.config;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/8 10:51
 */
public enum OcrWordsEnum {

    /**
     *   印刷文字识别
     */
    PRINT("general"),

    /**
     *   手写文字识别
     */
    HANDWRITING("handwriting");

    String value;

    OcrWordsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
