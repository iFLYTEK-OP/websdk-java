package cn.xfyun.model.response.telerobot.vo;

/**
 * 话术
 *
 * @author : jun
 * @date : 2021年06月21日
 */
public class Robot {
    /**
     * 话术编号
     */
    private String robot_id;
    /**
     * 话术名称
     */
    private String robot_name;
    /**
     * 外呼数据列模板	第一列必须是客户手机号，其他列是话术动态信息。
     */
    private String[] call_column;
    /**
     * 话术状态	当前已创建的话术库状态。 1：审核中，2：未通过，3：待发布，4：已发布。
     */
    private Integer status;
    /**
     * 话术类型	1：普通话术，2：动态话术。
     * 其中动态话术是指包含多个话术组，能够实现动态跳转，动态话术适用于业务模式比较大流程比较复杂的情况，
     * 划分成多个子流程后，一方面管理维护容易，另外一方面可以复用。详细请登录后台管理平台话术库中体验。
     */
    private Integer type;
    /**
     * 删除标记	0：未删除，1：已删除。
     */
    private Integer deleted;
    /**
     * 话术创建时间	毫秒时间戳
     */
    private Long time_create;
    /**
     * 话术更新时间	毫秒时间戳
     */
    private Long time_update;


    public void setRobot_id(String robot_id) {
        this.robot_id = robot_id;
    }

    public void setRobot_name(String robot_name) {
        this.robot_name = robot_name;
    }

    public void setCall_column(String[] call_column) {
        this.call_column = call_column;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public void setTime_create(Long time_create) {
        this.time_create = time_create;
    }

    public void setTime_update(Long time_update) {
        this.time_update = time_update;
    }
}
