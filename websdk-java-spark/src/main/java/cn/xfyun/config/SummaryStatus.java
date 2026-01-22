package cn.xfyun.config;

/**
 * 知识库文件萃取状态枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum SummaryStatus {

    /**
     * 未总结
     */
    UNSUMMARY("unsummary", "未总结"),

    /**
     * 总结中
     */
    SUMMARYING("summarying", "总结中"),

    /**
     * 总结失败
     */
    FAILED("failed", "总结失败"),

    /**
     * 总结完成
     */
    DONE("done", "总结完成"),

    /**
     * 萃取失败
     */
    ILLEGAL("illegal", "内容敏感");

    private final String code;
    private final String description;

    SummaryStatus(String code, String description) {
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
    public static SummaryStatus fromCode(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        for (SummaryStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown summary status code: " + code);
    }
}
