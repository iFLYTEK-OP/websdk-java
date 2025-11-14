package cn.xfyun.config;

/**
 * 星火大模型类型枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum SparkModel {

    SPARK_X1("https://spark-api.xf-yun.com/v1/x1", "spark-x", "X1推理大模型", true, true, true),
    SPARK_4_0_ULTRA("https://spark-api.xf-yun.com/v4.0/chat", "4.0Ultra", "4.0Ultra大模型", true, true, true),
    SPARK_MAX_32K("https://spark-api.xf-yun.com/chat/max-32k", "max-32k", "max-32k大模型", true, false, true),
    SPARK_MAX("https://spark-api.xf-yun.com/v3.5/chat", "generalv3.5", "generalv3.5大模型", true, true, true),
    SPARK_PRO_128K("https://spark-api.xf-yun.com/chat/pro-128k", "pro-128k", "长文本大模型", true, false, true),
    SPARK_PRO("https://spark-api.xf-yun.com/v3.1/chat", "generalv3", "generalv3大模型", true, true, true),
    SPARK_LITE("https://spark-api.xf-yun.com/v1.1/chat", "lite", "lite模型", false, false, false),
    CHAT_KJWX("https://spark-openapi-n.cn-huabei-1.xf-yun.com/v1.1/chat_kjwx", "kjwx", "科研大模型", false, false, false),
    CHAT_MULTILANG("https://spark-api-n.xf-yun.com/v1.1/chat_multilang", "multilang", "多语种大模型", false, false, false);

    private final String url;
    private final String domain;
    private final String desc;
    private final boolean functionEnable;
    private final boolean webSearchEnable;
    private final boolean systemEnable;

    SparkModel(String url, String domain, String desc, boolean functionEnable, boolean webSearchEnable, boolean systemEnable) {
        this.url = url;
        this.domain = domain;
        this.desc = desc;
        this.functionEnable = functionEnable;
        this.webSearchEnable = webSearchEnable;
        this.systemEnable = systemEnable;
    }

    public String getUrl() {
        return url;
    }

    public String getDomain() {
        return domain;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isFunctionEnable() {
        return functionEnable;
    }

    public boolean isWebSearchEnable() {
        return webSearchEnable;
    }

    public boolean isSystemEnable() {
        return systemEnable;
    }
}
