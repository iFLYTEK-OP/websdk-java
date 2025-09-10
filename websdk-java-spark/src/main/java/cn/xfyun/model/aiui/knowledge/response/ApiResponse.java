package cn.xfyun.model.aiui.knowledge.response;

/**
 * 接收API返回结果的通用封装类
 *
 * @author <zyding6@ifytek.com>
 */
public class ApiResponse<T> {

    private String code;
    private boolean flag;
    private T data;
    private String desc;
    private String sid;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public boolean isFlag() { return flag; }
    public void setFlag(boolean flag) { this.flag = flag; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public String getSid() { return sid; }
    public void setSid(String sid) { this.sid = sid; }
}