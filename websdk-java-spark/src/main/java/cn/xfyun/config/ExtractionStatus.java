package cn.xfyun.config;

/**
 * 知识库文件萃取状态枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum ExtractionStatus {

    /**
     * 未开始
     */
    UNSTARTED("UNSTARTED", "未开始"),

    /**
     * 萃取中
     */
    EXTRACTING("EXTRACTING", "萃取中"),

    /**
     * 萃取完成
     */
    EXTRACTED("EXTRACTED", "萃取完成"),

    /**
     * 萃取失败
     */
    FAILED("FAILED", "萃取失败");

    private final String code;
    private final String description;

    ExtractionStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据 code 获取枚举（忽略大小写）
     */
    public static ExtractionStatus fromCode(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        for (ExtractionStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown extraction status code: " + code);
    }

    /**
     * 判断是否为终态（已完成或失败）
     */
    public boolean isTerminal() {
        return this == EXTRACTED || this == FAILED;
    }

    /**
     * 判断是否正在处理中
     */
    public boolean isInProgress() {
        return this == EXTRACTING;
    }
}
