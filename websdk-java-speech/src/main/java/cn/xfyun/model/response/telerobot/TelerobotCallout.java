package cn.xfyun.model.response.telerobot;

/**
 * 外呼结果
 *
 * @author : jun
 * @date : 2021年06月21日
 */
public class TelerobotCallout {
    /**
     * 号码总数
     */
    private Integer total;
    /**
     * 外呼数据行对应的任务数据编号，用于结果推送数据关联。
     */
    private Long[] task_data_ids;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Long[] getTask_data_ids() {
        return task_data_ids;
    }

    public void setTask_data_ids(Long[] task_data_ids) {
        this.task_data_ids = task_data_ids;
    }
}
