package cn.xfyun.config;


/**
 * 大模型批处理枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum SparkBatchEnum {

    UPLOAD_FILE("/files", "上传文件", "POST"),
    GET_FILES("/files?page=%s&size=%s", "查询文件列表", "GET"),
    GET_FILE("/files/%s", "查询单个文件", "GET"),
    DELETE_FILE("/files/%s", "删除文件", "DELETE"),
    DOWNLOAD_FILE("/files/%s/content", "下载文件", "GET"),
    CREATE("/batches", "创建Batch任务", "POST"),
    GET_BATCH("/batches/%s", "查询Batch任务", "GET"),
    CANCEL("/batches/%s/cancel", "取消Batch任务", "POST"),
    GET_BATCHES("/batches?limit=%s&after=%s", "查询Batch列表", "GET");

    private final String url;
    private final String desc;
    private final String method;

    SparkBatchEnum(String url, String desc, String method) {
        this.url = url;
        this.desc = desc;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public String getDesc() {
        return desc;
    }

    public String getMethod() {
        return method;
    }
}
