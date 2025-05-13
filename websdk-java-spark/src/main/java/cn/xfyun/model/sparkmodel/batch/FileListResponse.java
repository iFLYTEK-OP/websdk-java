package cn.xfyun.model.sparkmodel.batch;

import java.util.List;

/**
 * 大模型批处理查询响应体
 *
 * @author <zyding6@iflytek.com>
 **/
public class FileListResponse {

    /**
     * object : 返回类型
     * data : 文件集合
     */

    private String object;
    private List<FileInfo> data;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<FileInfo> getData() {
        return data;
    }

    public void setData(List<FileInfo> data) {
        this.data = data;
    }
}
