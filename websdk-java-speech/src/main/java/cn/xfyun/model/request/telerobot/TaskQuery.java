package cn.xfyun.model.request.telerobot;

/**
 * 查询任务数据
 *
 * @author : jun
 * @date : 2021年06月16日
 */
public class TaskQuery {
    /**
     * 任务id
     */
    private String task_id;

    /**
     * 开始时间
     */
    private Long time_begin;
    /**
     * 结束时间
     */
    private Long time_end;

    /**
     * 任务名称	模糊检索
     */
    private String task_name;
    /**
     * 页大小	最大值50，默认20
     */
    private Integer page_size;
    /**
     * 当前页码	从1开始
     */
    private Integer page_index;

    /**
     * 排序字段	ID：任务编号，NAME：任务名称，CREATETIME：任务创建时间，STARTTIME：任务开始时间，ENDTIME：任务结束时间
     */
    private String sort_name;
    /**
     * 排序字段方式	"ASC" 正序 "DESC" 倒序
     */
    private String sort_order;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
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

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public Integer getPage_size() {
        return page_size;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    public Integer getPage_index() {
        return page_index;
    }

    public void setPage_index(Integer page_index) {
        this.page_index = page_index;
    }

    public String getSort_name() {
        return sort_name;
    }

    public void setSort_name(String sort_name) {
        this.sort_name = sort_name;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }
}
