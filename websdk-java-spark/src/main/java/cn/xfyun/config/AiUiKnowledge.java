package cn.xfyun.config;

/**
 * 个性化知识库枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum AiUiKnowledge {

    CREATE("https://sparkcons-rag.cn-huabei-1.xf-yun.com/aiuiKnowledge/rag/api/repo/create", "用户知识库创建", "POST"),
    UPLOAD("https://sparkcons-rag.cn-huabei-1.xf-yun.com/aiuiKnowledge/rag/api/doc/saveRepoDoc", "知识库追加文件上传", "POST"),
    DELETE("https://sparkcons-rag.cn-huabei-1.xf-yun.com/aiuiKnowledge/rag/api/repo/file/delete", "删除用户知识库或某个文件", "DELETE"),
    LIST("https://sparkcons-rag.cn-huabei-1.xf-yun.com/aiuiKnowledge/rag/api/app/getRepoConfig", "查询应用已绑定知识库列表及全量知识库列表", "GET"),
    LINK("https://sparkcons-rag.cn-huabei-1.xf-yun.com/aiuiKnowledge/rag/api/app/saveRepoConfig", "用户应用关联绑定知识库", "POST");

    private final String url;
    private final String desc;
    private final String method;

    AiUiKnowledge(String url, String desc, String method) {
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
