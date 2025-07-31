package cn.xfyun.config;

/**
 * 工作流操作枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum WorkFlowEnum {

    COMPLETIONS("https://xingchen-api.xf-yun.com/workflow/v1/chat/completions", "执行工作流", "POST"),
    RESUME("https://xingchen-api.xf-yun.com/workflow/v1/resume", "恢复运行工作流", "POST"),
    UPLOAD_FILE("https://xingchen-api.xf-yun.com/workflow/v1/upload_file", "文件上传", "POST");

    private final String url;
    private final String desc;
    private final String method;

    WorkFlowEnum(String url, String desc, String method) {
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
