package cn.xfyun.config;

/**
 * 知识库操作枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum RagEnum {

    FILE_UPLOAD("/file/upload", "文档上传", "POST"),
    FILE_STATUS("/file/status", "文档状态查询", "POST"),
    EXTRACT_CREATE("/qa/extract", "提交萃取任务", "POST"),
    EXTRACT_STATUS("/qa/extract/status", "文件萃取状态查询", "GET"),
    EXTRACT_RESULT("/qa/extract/result", "获取萃取结果", "GET"),
    QA_APPLY("/qa/apply", "QA对应用", "POST"),
    QA_UPDATE("/qa/apply/update", "QA对更新", "POST"),
    QA_DELETE("/qa/apply/delete", "QA对删除", "POST"),
    QA_PAGE("/qa/apply/page", "QA对查询", "POST"),
    FILE_SUMMARY_CREATE("/file/summary/start", "发起文档总结", "POST"),
    FILE_SUMMARY_QUERY("/file/summary/query", "获取文档总结信息", "POST"),
    FILE_SPLIT("/file/split", "文档切分", "POST"),
    FILE_EMBEDDING("/file/embedding", "文档向量化", "POST"),
    FILE_COMPARE("/vector/search", "文档内容相似度检测", "POST"),
    FILE_CHUNKS("/file/chunks", "文档分块内容获取", "POST"),
    FILE_INFO("/file/info", "文档详情", "POST"),
    FILE_LIST("/file/list", "文档列表", "POST"),
    FILE_DEL("/file/del", "文档删除", "POST"),
    REPO_CREATE("/repo/create", "知识库创建", "POST"),
    REPO_ADD_FILE("/repo/file/add", "知识库添加文件", "POST"),
    REPO_REMOVE_FILE("/repo/file/remove", "知识库移除文件", "POST"),
    REPO_LIST("/repo/list", "知识库列表", "POST"),
    REPO_INFO("/repo/info", "知识库详情", "POST"),
    REPO_FILE_LIST("/repo/file/list", "知识库文件列表", "POST"),
    REPO_DEL("/repo/del", "知识库删除", "POST");

    private final String url;
    private final String desc;
    private final String method;

    RagEnum(String url, String desc, String method) {
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
