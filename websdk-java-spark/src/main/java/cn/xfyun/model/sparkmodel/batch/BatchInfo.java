package cn.xfyun.model.sparkmodel.batch;

/**
 * 大模型批处理批次信息实体类
 *
 * @author <zyding6@iflytek.com>
 **/
public class BatchInfo {

    /**
     * id: 批任务唯一标识（全局唯一）格式为：batch_xxxxx
     * object: 结构类型，当前仅为 batch
     * endpoint: 处理批任务的服务的 path 路径，当前仅为 /v1/chat/completions
     * errors: 错误列表
     * errors.object: 描述 data 类型 (list)
     * errors.data: [{code string, message string, param string or null, line int or null}]
     * input_file_id: 文件id（用户入参）
     * completion_window: 24h
     * status: 任务状态（枚举值）
     * output_file_id: 结果文件id
     * error_file_id: 错误文件id
     * created_at: 任务创建时间（unix时间戳，下面的时间也均为unix时间戳）
     * in_progress_at: 任务开始处理时间
     * expires_at: 任务预期超时时间
     * finalizing_at: 任务完成开始时间(写 out_file && error_file 开始)
     * completed_at: 任务完成结束时间(写 out_file && error_file 结束)
     * failed_at: 任务失败时间(所有任务失败时返回)
     * expired_at: 任务实际超时时间
     * cancelling_at: 任务取消开始时间 （收到取消请求时间）
     * cancelled_at: 任务取消结束时间 （任务实际取消时间）
     * request_counts: 批任务信息
     * request_counts.total: 批任务当前完成总数
     * request_counts.completed: 任务当前成功数
     * request_counts.failed: 任务当前失败数
     * metadata: 批任务附加信息（用户入参）
     */

    private String id;
    private String object;
    private String endpoint;
    private Object errors;
    private String input_file_id;
    private String completion_window;
    private String status;
    private String output_file_id;
    private String error_file_id;
    private Integer created_at;
    private Integer in_progress_at;
    private Integer expires_at;
    private Integer finalizing_at;
    private Integer completed_at;
    private Integer failed_at;
    private Integer expired_at;
    private Integer cancelling_at;
    private Integer cancelled_at;
    private RequestCounts request_counts;
    private Object metadata;

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

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public String getInputFileId() {
        return input_file_id;
    }

    public void setInputFileId(String inputFileId) {
        this.input_file_id = inputFileId;
    }

    public String getCompletionWindow() {
        return completion_window;
    }

    public void setCompletionWindow(String completionWindow) {
        this.completion_window = completionWindow;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutputFileId() {
        return output_file_id;
    }

    public void setOutputFileId(String outputFileId) {
        this.output_file_id = outputFileId;
    }

    public String getErrorFileId() {
        return error_file_id;
    }

    public void setErrorFileId(String errorFileId) {
        this.error_file_id = errorFileId;
    }

    public Integer getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Integer createdAt) {
        this.created_at = createdAt;
    }

    public Integer getInProgressAt() {
        return in_progress_at;
    }

    public void setInProgressAt(Integer inProgressAt) {
        this.in_progress_at = inProgressAt;
    }

    public Integer getExpiresAt() {
        return expires_at;
    }

    public void setExpiresAt(Integer expiresAt) {
        this.expires_at = expiresAt;
    }

    public Integer getFinalizingAt() {
        return finalizing_at;
    }

    public void setFinalizingAt(Integer finalizingAt) {
        this.finalizing_at = finalizingAt;
    }

    public Integer getCompletedAt() {
        return completed_at;
    }

    public void setCompletedAt(Integer completedAt) {
        this.completed_at = completedAt;
    }

    public Integer getFailedAt() {
        return failed_at;
    }

    public void setFailedAt(Integer failedAt) {
        this.failed_at = failedAt;
    }

    public Integer getExpiredAt() {
        return expired_at;
    }

    public void setExpiredAt(Integer expiredAt) {
        this.expired_at = expiredAt;
    }

    public Integer getCancellingAt() {
        return cancelling_at;
    }

    public void setCancellingAt(Integer cancellingAt) {
        this.cancelling_at = cancellingAt;
    }

    public Integer getCancelledAt() {
        return cancelled_at;
    }

    public void setCancelledAt(Integer cancelledAt) {
        this.cancelled_at = cancelledAt;
    }

    public RequestCounts getRequestCounts() {
        return request_counts;
    }

    public void setRequestCounts(RequestCounts requestCounts) {
        this.request_counts = requestCounts;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public static class RequestCounts {

        private Integer total;
        private Integer completed;
        private Integer failed;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getCompleted() {
            return completed;
        }

        public void setCompleted(Integer completed) {
            this.completed = completed;
        }

        public Integer getFailed() {
            return failed;
        }

        public void setFailed(Integer failed) {
            this.failed = failed;
        }
    }
}
