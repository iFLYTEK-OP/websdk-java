package cn.xfyun.config;

/**
 * 词库请求枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum WordLibEnum {

    CREATE_BLACK("/wordLib/createBlack", "新增黑名单词库", "POST"),
    CREATE_WHITE("/wordLib/createWhite", "新增白名单词库", "POST"),
    ADD_WORD("/wordLib/addWord", "根据lib_id添加黑名单词条", "POST"),
    INFO("/wordLib/info", "根据lib_id查询词条明细", "POST"),
    DEL_WORD("/wordLib/delWord", "根据lib_id删除词条", "POST"),
    LIST("/wordLib/list", "根据appid查询账户下所有词库", "POST"),
    DEL_LIB("/wordLib/delete", "根据lib_id删除词库", "POST");

    private final String url;
    private final String desc;
    private final String method;

    WordLibEnum(String url, String desc, String method) {
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
