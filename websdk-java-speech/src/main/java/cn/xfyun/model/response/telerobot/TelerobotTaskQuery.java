package cn.xfyun.model.response.telerobot;

import cn.xfyun.model.response.telerobot.vo.Task;

import java.util.List;

/**
 * 外呼任务查询
 *
 * @author : jun
 * @date : 2021年06月21日
 */
public class TelerobotTaskQuery {
    /**
     * 总行数
     */
    private Integer total_rows;
    /**
     * 结果列表
     */
    private List<Task> rows;

    public Integer getTotal_rows() {
        return total_rows;
    }

    public void setTotal_rows(Integer total_rows) {
        this.total_rows = total_rows;
    }

    public List<Task> getRows() {
        return rows;
    }

    public void setRows(List<Task> rows) {
        this.rows = rows;
    }
}
