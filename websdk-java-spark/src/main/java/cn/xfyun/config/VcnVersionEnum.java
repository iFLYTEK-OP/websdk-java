package cn.xfyun.config;

/**
 * vcn版本枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum VcnVersionEnum {

    V6("x6_clone", "一句话复刻美化版本"),
    V5("x5_clone", "一句话复刻标准版本");

    private final String version;
    private final String desc;
    VcnVersionEnum(String version, String desc) {
        this.version = version;
        this.desc = desc;
    }

    public String getVersion() {
        return version;
    }

    public String getDesc() {
        return desc;
    }
}
