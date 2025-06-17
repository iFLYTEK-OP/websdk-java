package cn.xfyun.config;

/**
 * 合合能力
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/8 11:05
 */
public enum IntsigRecgEnum {

    /**
     * 身份证识别
     */
    IDCARD("身份证识别", "idcard"),

    /**
     * 营业执照识别
     */
    BUSINESS_LICENSE("营业执照识别","business_license"),

    /**
     * 增值税发票识别
     */
    INVOICE("增值税发票识别","invoice"),

    /**
     * 印刷文字识别（多语种）
     */
    RECOGNIZE_DOCUMENT("印刷文字识别（多语种）","recognize_document"),

    /**
     * 通用文本识别（多语种）
     */
    COMMON_WORD("通用文本识别（多语种）","hh_ocr_recognize_doc");

    private final String value;

    private final String name;

    IntsigRecgEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
