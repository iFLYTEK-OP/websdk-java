package cn.xfyun.config;

/**
 * @program: websdk-java
 * @description: 一句话复刻语种枚举类
 * @author: zyding6
 * @create: 2025/3/20 16:47
 **/
public enum VoiceCloneLangEnum {

    CN("中", 0),
    EN("英", 1),
    JA("日", 2),
    KO("韩", 3),
    ES("俄", 4);

    private final Integer code;

    private final String desc;

    VoiceCloneLangEnum(String desc, Integer code) {
        this.code = code;
        this.desc = desc;
    }

    public Integer code() {
        return code;
    }

    public String desc() {
        return desc;
    }
}
