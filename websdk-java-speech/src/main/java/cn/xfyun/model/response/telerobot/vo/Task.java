package cn.xfyun.model.response.telerobot.vo;

/**
 * 外呼任务
 *
 * @author : jun
 * @date : 2021年06月21日
 */
public class Task {
    /**
     * 外呼任务id
     */
    private String task_id;
    /**
     * 任务名称
     */
    private String task_name;
    /**
     * 任务状态	0：新建，1：启动，2：运行，3：暂停，4：完成
     */
    private Integer status;
    /**
     * 任务类型	0：普通任务,2：任务池
     */
    private Integer task_type;
    /**
     * 删除标志	1：删除，0：正常
     */
    private String deleted;
    /**
     * 运行开始时间	毫秒时间戳
     */
    private Long time_task_start;
    /**
     * (仅限普通任务)	运行结束时间	毫秒时间戳
     */
    private Long time_task_finish;
    /**
     * 已呼叫次数
     */
    private Integer process_count;
    /**
     * 已呼叫号码数
     */
    private Integer process_tel_count;
    /**
     * 已接通量
     */
    private Integer process_through_count;
    /**
     * 当前接通率
     */
    private Double process_through_rate;
    /**
     * 任务号码量
     */
    private Integer count_tel;
    /**
     * (仅限普通任务)	任务已重试次数
     */
    private Integer count_recalled;
    /**
     * (仅限普通任务)	预设开始时间
     */
    private Long time_task_estimate_begin;
    /**
     * (仅限普通任务)	预设结束时间
     */
    private Long time_task_estimate_end;
    /**
     * (仅限普通任务)	线路号码
     */
    private String line_num;
    /**
     * (仅限普通任务)	话术id
     */
    private String robot_id;
    /**
     * (仅限普通任务)	话术名称
     */
    private String robot_name;
    /**
     * (仅限普通任务)	发音人编码
     */
    private String voice_code;
    /**
     * (仅限普通任务)	发音人语速，默认为1
     */
    private String voice_speed;
    /**
     * (仅限普通任务)	预设任务重试次数
     */
    private String count_max_recall;
    /**
     * (仅限普通任务)	预设重试外呼等待时间	单位：秒。
     */
    private String time_recall_wait;
    /**
     * (仅限普通任务)	预设外呼时间段
     */
    private String time_range;
    /**
     * (仅限普通任务)	预设推送意向度门限
     */
    private String intention_push;
}
