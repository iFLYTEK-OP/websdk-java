package cn.xfyun.model.response.telerobot.vo;

/**
 * 线路
 *
 * @author : jun
 * @date : 2021年06月21日
 */
public class Line {
    /**
     * 号码
     */
    private String line_num;

    /**
     * 并发数	此线路的最大并发数
     */
    private Integer concurrents;
    /**
     * 工作时段	电话机器人工作时段
     */
    private String[] time_work;
    /**
     * 状态	0：空闲，即该线路上没有正在执行的任务 1：任务占用中，即该线路上当前有任务正在执行。
     */
    private Integer status;
    /**
     * 申请时间	线路的对接时间。数值：毫秒时间戳
     */
    private Long time_apply;
    /**
     * 有效期	线路的有效期。单位：秒。-1：永久有效。
     */
    private Long time_expire;


    public String getLine_num() {
        return line_num;
    }

    public void setLine_num(String line_num) {
        this.line_num = line_num;
    }

    public Integer getConcurrents() {
        return concurrents;
    }

    public void setConcurrents(Integer concurrents) {
        this.concurrents = concurrents;
    }

    public String[] getTime_work() {
        return time_work;
    }

    public void setTime_work(String[] time_work) {
        this.time_work = time_work;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTime_apply() {
        return time_apply;
    }

    public void setTime_apply(Long time_apply) {
        this.time_apply = time_apply;
    }

    public Long getTime_expire() {
        return time_expire;
    }

    public void setTime_expire(Long time_expire) {
        this.time_expire = time_expire;
    }
}
