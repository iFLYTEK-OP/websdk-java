package cn.xfyun.model.sparkmodel.batch;


/**
 * 大模型批处理文件实体类
 *
 * @author <zyding6@iflytek.com>
 **/
public class FileInfo {

    /**
     * id : 文件唯一标识符（全局唯一）格式为：file_xxxxx
     * object : 上传类型(扩展字段，当前仅为 file)
     * bytes : 文件长度(单位：Byte)
     * created_at : 文件创建时间
     * filename : 文件名
     * purpose : 上传文件的意图(请求参数传入值)
     */

    private String id;
    private String object;
    private Integer bytes;
    private Integer created_at;
    private String filename;
    private String purpose;

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

    public Integer getBytes() {
        return bytes;
    }

    public void setBytes(Integer bytes) {
        this.bytes = bytes;
    }

    public Integer getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Integer createdAt) {
        this.created_at = createdAt;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
