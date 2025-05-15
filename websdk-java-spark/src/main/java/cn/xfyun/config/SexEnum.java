package cn.xfyun.config;

/**
 * 性别枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum SexEnum {

    MALE(1, "男"),
    FEMALE(2, "女");

    private final int value;
    private final String description;

    SexEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static SexEnum fromValue(int value) {
        for (SexEnum sex : values()) {
            if (sex.value == value) {
                return sex;
            }
        }
        return MALE; // 默认值为男
    }
}
