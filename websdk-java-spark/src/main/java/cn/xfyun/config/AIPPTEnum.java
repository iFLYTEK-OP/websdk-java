package cn.xfyun.config;

/**
 * 智能ppt枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum AIPPTEnum {

    LIST("/template/list", "PPT主题列表查询", "POST"),
    CREATE("/create", "PPT生成", "POST"),
    CREATE_OUTLINE("/createOutline", "大纲生成", "POST"),
    CREATE_OUTLINE_BY_DOC("/createOutlineByDoc", "自定义大纲生成", "POST"),
    CREATE_PPT_BY_OUTLINE("/createPptByOutline", "通过大纲生成PPT", "POST"),
    PROGRESS("/progress?sid=%s", "PPT进度查询", "GET");

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
