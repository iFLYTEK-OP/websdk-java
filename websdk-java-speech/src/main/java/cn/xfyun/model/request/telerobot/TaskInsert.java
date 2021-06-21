package cn.xfyun.model.request.telerobot;

/**
 * 提交任务数据
 *
 * @author : jun
 * @date : 2021年06月16日
 */
public class TaskInsert {

    /**
     * 任务id
     */
    private String task_id;
    /**
     * 数据列映射
     */
    private String[] call_column;
    /**
     * 数据行	单次上限50条
     */
    private String[] call_list;


    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String[] getCall_column() {
        return call_column;
    }

    public void setCall_column(String[] call_column) {
        this.call_column = call_column;
    }

    public String[] getCall_list() {
        return call_list;
    }

    public void setCall_list(String[] call_list) {
        this.call_list = call_list;
    }
}
