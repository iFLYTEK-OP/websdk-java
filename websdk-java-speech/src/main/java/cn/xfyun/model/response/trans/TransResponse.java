package cn.xfyun.model.response.trans;

/**
 * @author <ydwang16@iflytek.com>
 * @description 翻译结果
 * @date 2021/6/15
 */
public class TransResponse {

    /**
     * 返回码，0表示成功，其它表示异常，详情请参考
     * 小牛： https://www.xfyun.cn/doc/nlp/niutrans/API.html#%E9%94%99%E8%AF%AF%E7%A0%81
     * 自研： https://www.xfyun.cn/doc/nlp/xftrans/API.html#%E9%94%99%E8%AF%AF%E7%A0%81
     */
    private Integer code;

    /**
     * 本次会话id
     */
    private String sid;

    /**
     * 描述信息
     */
    private String message;

    /**
     * 翻译结果
     * 若接口报错（code不为0），则无该字段
     */
    private TransData data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TransData getData() {
        return data;
    }

    public void setData(TransData data) {
        this.data = data;
    }
}
