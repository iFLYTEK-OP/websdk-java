package cn.xfyun.config;

/**
 * @author: rblu2
 * @desc: 模型版本
 * @create: 2025-02-18 18:12
 **/
public enum SparkModelEum {
    LITE("lite", "https://spark-api-open.xf-yun.com/v1/chat/completions", "wss://spark-api.xf-yun.com/v1.1/chat"),
    GENERAL_V3("generalv3", "https://spark-api-open.xf-yun.com/v1/chat/completions", "wss://spark-api.xf-yun.com/v3.1/chat"),
    PRO_128K("pro-128k", "https://spark-api-open.xf-yun.com/v1/chat/completions", " wss://spark-api.xf-yun.com/chat/pro-128k"),
    GENERAL_V35("generalv3.5", "https://spark-api-open.xf-yun.com/v1/chat/completions", "wss://spark-api.xf-yun.com/v3.5/chat"),
    MAX_32K("max-32k", "https://spark-api-open.xf-yun.com/v1/chat/completions", "wss://spark-api.xf-yun.com/chat/max-32k"),
    V4_ULTRA("4.0Ultra", "https://spark-api-open.xf-yun.com/v1/chat/completions", "wss://spark-api.xf-yun.com/v4.0/chat");
    private final String code;

    private final String httpUrl;

    private final String wsUrl;

    SparkModelEum(String code, String httpUrl, String wsUrl) {
        this.code = code;
        this.httpUrl = httpUrl;
        this.wsUrl = wsUrl;
    }

    public String getCode() {
        return code;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public String getWsUrl() {
        return wsUrl;
    }
}
