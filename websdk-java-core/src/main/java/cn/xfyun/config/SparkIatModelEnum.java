package cn.xfyun.config;

/**
 * @program: websdk-java
 * @description:
 * @author: zyding6
 * @create: 2025/3/12 15:24
 **/
public enum SparkIatModelEnum {

    /**
     * 中文大模型
     */
    ZH_CN_MANDARIN(1, "中文大模型"),
    /**
     * 方言大模型
     */
    ZH_CN_MULACC(2, "方言大模型"),
    /**
     * 多语种大模型
     */
    MUL_CN_MANDARIN(3, "多语种大模型");

    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    SparkIatModelEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public boolean codeEquals(final Integer code) {
        return this.code.equals(code);
    }
}
