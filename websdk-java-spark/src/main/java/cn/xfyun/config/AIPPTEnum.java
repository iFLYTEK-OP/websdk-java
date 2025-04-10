package cn.xfyun.config;

/**
 * 智能ppt枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum AIPPTEnum {

    LIST("https://zwapi.xfyun.cn/api/ppt/v2/template/list", "PPT主题列表查询", "POST"),
    CREATE("https://zwapi.xfyun.cn/api/ppt/v2/create", "PPT生成", "POST"),
    CREATE_OUTLINE("https://zwapi.xfyun.cn/api/ppt/v2/createOutline", "大纲生成", "POST"),
    CREATE_OUTLINE_BY_DOC("https://zwapi.xfyun.cn/api/ppt/v2/createOutlineByDoc", "自定义大纲生成", "POST"),
    CREATE_PPT_BY_OUTLINE("https://zwapi.xfyun.cn/api/ppt/v2/createPptByOutline", "通过大纲生成PPT", "POST"),
    PROGRESS("https://zwapi.xfyun.cn/api/ppt/v2/progress?sid=%s", "PPT进度查询", "GET");

    private final String url;
    private final String desc;
    private final String method;

    AIPPTEnum(String url, String desc, String method) {
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
