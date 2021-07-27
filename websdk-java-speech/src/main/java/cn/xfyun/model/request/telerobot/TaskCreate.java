package cn.xfyun.model.request.telerobot;

/**
 * 创建外呼任务参数
 *
 * @author : jun
 * @date : 2021年06月16日
 */
public class TaskCreate {
    /**
     * 任务名称
     */
    private String task_name;
    /**
     * 如果是多个，分号分隔
     */
    private String line_num;
    /**
     * 话术id
     */
    private String robot_id;
    /**
     * 重试外呼次数	最大3次，默认0
     */
    private Integer recall_count;
    /**
     * 重试等待时间	单位秒
     */
    private Long time_recall_wait;
    /**
     * 外呼时间段
     */
    private String[] time_range;
    /**
     * 任务开始时间	毫秒时间戳
     */
    private Long time_begin;
    /**
     * 任务结束时间	毫秒时间戳
     */
    private Long time_end;
    /**
     * 发音人编码
     */
    private Integer voice_code;


    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getLine_num() {
        return line_num;
    }

    public void setLine_num(String line_num) {
        this.line_num = line_num;
    }

    public String getRobot_id() {
        return robot_id;
    }

    public void setRobot_id(String robot_id) {
        this.robot_id = robot_id;
    }

    public Integer getRecall_count() {
        return recall_count;
    }

    public void setRecall_count(Integer recall_count) {
        this.recall_count = recall_count;
    }

    public Long getTime_recall_wait() {
        return time_recall_wait;
    }

    public void setTime_recall_wait(Long time_recall_wait) {
        this.time_recall_wait = time_recall_wait;
    }

    public String[] getTime_range() {
        return time_range;
    }

    public void setTime_range(String[] time_range) {
        this.time_range = time_range;
    }

    public Long getTime_begin() {
        return time_begin;
    }

    public void setTime_begin(Long time_begin) {
        this.time_begin = time_begin;
    }

    public Long getTime_end() {
        return time_end;
    }

    public void setTime_end(Long time_end) {
        this.time_end = time_end;
    }

    public Integer getVoice_code() {
        return voice_code;
    }

    public void setVoice_code(Integer voice_code) {
        this.voice_code = voice_code;
    }
}
