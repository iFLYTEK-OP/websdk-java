package cn.xfyun.config;


/**
 * 大模型批处理枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum SparkBatchEnum {

    UPLOAD_FILE("https://spark-api-open.xf-yun.com/v1/files", "上传文件", "POST"),
    GET_FILES("https://spark-api-open.xf-yun.com/v1/files?page=%s&size=%s", "查询文件列表", "GET"),
    GET_FILE("https://spark-api-open.xf-yun.com/v1/files/%s", "查询单个文件", "GET"),
    DELETE_FILE("https://spark-api-open.xf-yun.com/v1/files/%s", "删除文件", "DELETE"),
    DOWNLOAD_FILE("https://spark-api-open.xf-yun.com/v1/files/%s/content", "下载文件", "GET"),
    CREATE("https://spark-api-open.xf-yun.com/v1/batches", "创建Batch任务", "POST"),
    GET_BATCH("https://spark-api-open.xf-yun.com/v1/batches/%s", "查询Batch任务", "GET"),
    CANCEL("https://spark-api-open.xf-yun.com/v1/batches/%s/cancel", "取消Batch任务", "POST"),
    GET_BATHES("https://spark-api-open.xf-yun.com/v1/batches?limit=%s&after=%s", "查询Batch列表", "GET");

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
