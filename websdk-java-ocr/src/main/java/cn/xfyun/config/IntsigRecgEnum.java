package cn.xfyun.config;

/**
 *    合合能力
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/8 11:05
 */
public enum IntsigRecgEnum {
    /**
     *   身份证识别
     */
    IDCARD("idcard"),

    /**
     *   营业执照识别
     */
    BUSINESS_LICENSE("business_license"),

    /**
     *   增值税发票识别
     */
    INVOICE("invoice"),

    /**
     *   印刷文字识别（多语种）
     */
    RECOGNIZE_DOCUMENT("recognize_document");

    String value;

    IntsigRecgEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
