package cn.xfyun.model.sparkmodel.batch;

/**
 * 大模型批处理删除响应体
 *
 * @author <zyding6@iflytek.com>
 **/
public class DeleteResponse {

    /**
     * id : 文件ID
     * object : 文件类型
     * deleted : 是否删除成功
     */

    private String id;
    private String object;
    private Boolean deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
