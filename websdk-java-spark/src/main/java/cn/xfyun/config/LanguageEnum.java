package cn.xfyun.config;

/**
 * 常用语种枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum LanguageEnum {

    CHINESE("中文普通话", 0),
    ENGLISH("英语", 1),
    JAPANESE("日语", 2),
    KOREAN("韩语", 3),
    RUSSIAN("俄语", 4),
    FRENCH("法语", 5),
    ARABIC("阿拉伯语", 6),
    SPANISH("西班牙语", 7),
    CANTONESE("粤语", 8);

    private final String description;
    private final int code;

    LanguageEnum(String description, int code) {
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + " -> " + description;
    }

    /**
     * 根据编号获取语言枚举值
     */
    public static LanguageEnum getByCode(int code) {
        for (LanguageEnum language : values()) {
            if (language.getCode() == code) {
                return language;
            }
        }
        return null;
    }

    /**
     * 根据描述获取语言枚举值
     */
    public static LanguageEnum getByDescription(String description) {
        for (LanguageEnum language : values()) {
            if (language.getDescription().equals(description)) {
                return language;
            }
        }
        return null;
    }
}
