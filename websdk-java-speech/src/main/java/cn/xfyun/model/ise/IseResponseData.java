package cn.xfyun.model.ise;

/**
 * @author: <flhong2@iflytek.com>
 * @description: 接口返回消息体
 * @version 1.0
 * @create: 2021-03-22 16:25
 **/
public class IseResponseData {
    private int code;
    private String message;
    private String sid;
    private IseResponse data;
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public String getSid() {
        return sid;
    }
    public IseResponse getData() {
        return data;
    }
}
