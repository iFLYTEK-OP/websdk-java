package cn.xfyun.config;

/**
 * 词库请求枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum WordLibEnum {

    CREATE_BLACK("https://audit.iflyaisol.com/audit_res/v1/wordLib/createBlack", "新增黑名单词库", "POST"),
    CREATE_WHITE("https://audit.iflyaisol.com/audit_res/v1/wordLib/createWhite", "新增白名单词库", "POST"),
    ADD_WORD("https://audit.iflyaisol.com/audit_res/v1/wordLib/addWord", "根据lib_id添加黑名单词条", "POST"),
    INFO("https://audit.iflyaisol.com/audit_res/v1/wordLib/info", "根据lib_id查询词条明细", "POST"),
    DEL_WORD("https://audit.iflyaisol.com/audit_res/v1/wordLib/delWord", "根据lib_id删除词条", "POST"),
    LIST("https://audit.iflyaisol.com/audit_res/v1/wordLib/list", "根据appid查询账户下所有词库", "POST"),
    DEL_LIB("https://audit.iflyaisol.com/audit_res/v1/wordLib/delete", "根据lib_id删除词库", "POST");

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
