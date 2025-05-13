package cn.xfyun.model.sparkmodel.batch;

import java.util.List;

/**
 * 大模型批处理批次查询响应体
 *
 * @author <zyding6@iflytek.com>
 **/
public class BatchListResponse {

    /**
     * object : 返回类型
     * data : 批次集合
     * first_id : 第一个id
     * last_id : 最后一个id
     * has_more : 是否全部返回(取反)
     */

    private String object;
    private List<BatchInfo> data;
    private String first_id;
    private String last_id;
    private Boolean has_more;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<BatchInfo> getData() {
        return data;
    }

    public void setData(List<BatchInfo> data) {
        this.data = data;
    }

    public String getFirstId() {
        return first_id;
    }

    public void setFirstId(String firstId) {
        this.first_id = firstId;
    }

    public String getLastId() {
        return last_id;
    }

    public void setLastId(String lastId) {
        this.last_id = lastId;
    }

    public Boolean getHasMore() {
        return has_more;
    }

    public void setHasMore(Boolean hasMore) {
        this.has_more = hasMore;
    }
}
