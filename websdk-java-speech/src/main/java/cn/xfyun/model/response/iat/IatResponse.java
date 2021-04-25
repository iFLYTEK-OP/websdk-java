package cn.xfyun.model.response.iat;

/**
 * @author <ydwang16@iflytek.com>
 * @description 语音听写响应
 * @date 2021/3/24
 */
public class IatResponse {
    private int code;
    private String message;
    private String sid;
    private IatData data;

    public IatResponse() {
    }

    public IatResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public IatData getData() {
        return data;
    }

    public void setData(IatData data) {
        this.data = data;
    }
}
