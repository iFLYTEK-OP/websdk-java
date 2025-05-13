package cn.xfyun.config;

/**
 * 年龄段枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum AgeGroupEnum {

    CHILD(1, "儿童"),
    YOUTH(2, "青年"),
    MIDDLE_AGED(3, "中年"),
    SENIOR(4, "中老年");

    private final int value;
    private final String description;

    AgeGroupEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static AgeGroupEnum fromValue(int value) {
        for (AgeGroupEnum group : values()) {
            if (group.value == value) {
                return group;
            }
        }
        return CHILD; // 默认值为儿童
    }
}
