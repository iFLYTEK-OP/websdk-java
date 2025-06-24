package cn.xfyun.config;

public enum DocumentEnum {

    EXCEL("0", "excel"),
    DOC("1", "doc"),
    PPT("2", "ppt");

    private final String code;
    private final String desc;

    DocumentEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static DocumentEnum getEnumByCode(String code) {
        for (DocumentEnum e : DocumentEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static String getDescByCode(String code) {
        for (DocumentEnum documentEnum : DocumentEnum.values()) {
            if (documentEnum.getCode().equals(code)) {
                return documentEnum.getDesc();
            }
        }
        return null;
    }
} 