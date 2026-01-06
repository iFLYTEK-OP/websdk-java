package cn.xfyun.model.rag;

import cn.xfyun.util.StringUtils;

/**
 * 知识库提交萃取结果查询请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class FileResult {

    /**
     * 任务Id，与fileId必须传一个
     */
    private String taskId;

    /**
     * 文件Id
     */
    private String fileId;

    public FileResult(Builder builder) {
        this.fileId = builder.fileId;
        this.taskId = builder.taskId;
    }

    public FileResult() {
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public static final class Builder {

        private String fileId;
        private String taskId;

        private Builder() {
        }

        public FileResult build() {
            return new FileResult(this);
        }

        public Builder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder taskId(String taskId) {
            this.taskId = taskId;
            return this;
        }
    }
}
