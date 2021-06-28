package cn.xfyun.config;

/**
 * 非实时转写任务状态码
 *
 * @author : jun
 * @date : 2021年03月27日
 */
public enum LfasrTaskStatusEnum {

    /**
     * 失败
     */
    STATUS_FAILED(-1, "失败"),
    /**
     * 任务创建成功
     */
    STATUS_0(0, "任务创建成功"),
    /**
     * 音频上传完成
     */
    STATUS_1(1, "音频上传完成"),
    /**
     * 音频合并完成
     */
    STATUS_2(2, "音频合并完成"),
    /**
     * 音频转写中
     */
    STATUS_3(3, "音频转写中"),
    /**
     * 转写结果处理中
     */
    STATUS_4(4, "转写结果处理中"),
    /**
     * 转写完成
     */
    STATUS_5(5, "转写完成"),
    /**
     * 转写结果上传完成
     */
    STATUS_9(9, "转写结果上传完成");

    private Integer key;
    private String value;

    LfasrTaskStatusEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }
}
