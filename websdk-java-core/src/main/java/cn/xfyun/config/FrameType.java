package cn.xfyun.config;

/**
 * 数据状态枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum FrameType {

    FIRST_FRAME(0, "首帧"),
    MIDDLE_FRAME(1, "中间帧"),
    LAST_FRAME(2, "末帧");

    private final int value;
    private final String description;

    FrameType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public boolean codeEqual(final int status) {
        return value == status;
    }
}
