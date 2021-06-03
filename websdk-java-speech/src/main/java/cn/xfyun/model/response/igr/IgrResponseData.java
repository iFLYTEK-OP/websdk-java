package cn.xfyun.model.response.igr;

import com.google.gson.JsonObject;

/**
 * @author: <flhong2@iflytek.com>
 * @description: 接口返回消息体
 * @version: v1.0
 * @create: 2021-06-02 15:20
 **/
public class IgrResponseData {
    private int code;
    private String message;
    private String sid;
    private JsonObject data;
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public String getSid() {
        return sid;
    }
    public JsonObject getData() {
        return data;
    }
}
