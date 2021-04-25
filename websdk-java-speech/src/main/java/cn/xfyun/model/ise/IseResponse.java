package cn.xfyun.model.ise;

/**
 * @version 1.0
 * @author: <flhong2@iflytek.com>
 * @description: 语音评测返回体
 * @create: 2021-03-17 20:58
 **/
public class IseResponse {
    private int status;
    private String data;

    public int getStatus() {
        return status;
    }
    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "IseResponse{" +
                "status=" + status +
                ", data='" + data + '\'' +
                '}';
    }
}
