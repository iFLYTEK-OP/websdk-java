package cn.xfyun.config;

import java.util.Objects;

/**
 * 星火大模型类型枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum StreamMode {

    /**
     * 单工模式（continuous_vad）：必须递增
     */
    CONTINUOUS_VAD("continuous_vad", "单工模式"),

    /**
     * 双工模式（continuous）：固定不变
     */
    CONTINUOUS("continuous", "双工模式");

    private final String value;
    private final String description;

    StreamMode(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public boolean modeEquals(final String mode) {
        return value.equals(mode);
    }

    public static StreamMode fromValue(String value) {
        for (StreamMode sex : values()) {
            if (Objects.equals(sex.value, value)) {
                return sex;
            }
        }
        return null;
    }
}
