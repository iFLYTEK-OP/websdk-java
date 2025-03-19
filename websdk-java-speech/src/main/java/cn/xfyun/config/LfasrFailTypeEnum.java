package cn.xfyun.config;

/**
 * 语音转写任务失败类型枚举
 */
public enum LfasrFailTypeEnum {
    
    /**
     * 音频正常执行
     */
    NORMAL_EXECUTION(0, "音频正常执行"),
    
    /**
     * 音频上传失败
     */
    UPLOAD_FAILED(1, "音频上传失败"),
    
    /**
     * 音频转码失败
     */
    TRANSCODING_FAILED(2, "音频转码失败"),
    
    /**
     * 音频识别失败
     */
    RECOGNITION_FAILED(3, "音频识别失败"),
    
    /**
     * 音频时长超限（最大音频时长为5小时）
     */
    DURATION_EXCEEDED(4, "音频时长超限（最大音频时长为5小时）"),
    
    /**
     * 音频校验失败（duration对应的值与真实音频时长不符合要求）
     */
    VALIDATION_FAILED(5, "音频校验失败（duration对应的值与真实音频时长不符合要求）"),
    
    /**
     * 静音文件
     */
    SILENT_FILE(6, "静音文件"),
    
    /**
     * 翻译失败
     */
    TRANSLATION_FAILED(7, "翻译失败"),
    
    /**
     * 账号无翻译权限
     */
    NO_TRANSLATION_PERMISSION(8, "账号无翻译权限"),
    
    /**
     * 转写质检失败
     */
    QUALITY_INSPECTION_FAILED(9, "转写质检失败"),
    
    /**
     * 转写质检未匹配出关键词
     */
    NO_KEYWORDS_MATCHED(10, "转写质检未匹配出关键词"),
    
    /**
     * 未开启质检或者翻译能力
     */
    CAPABILITY_NOT_ENABLED(11, "未开启质检或者翻译能力"),
    
    /**
     * 其他错误
     */
    OTHER_ERROR(99, "其他错误");
    
    private final int key;
    private final String value;
    
    LfasrFailTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public int getKey() {
        return key;
    }
    
    public String getValue() {
        return value;
    }
    
    public static LfasrFailTypeEnum getEnum(int key) {
        for (LfasrFailTypeEnum typeEnum : LfasrFailTypeEnum.values()) {
            if (typeEnum.getKey() == key) {
                return typeEnum;
            }
        }
        return OTHER_ERROR;
    }
}