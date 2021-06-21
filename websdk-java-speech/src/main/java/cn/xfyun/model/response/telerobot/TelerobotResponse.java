package cn.xfyun.model.response.telerobot;

/**
 * AI客服平台能力中间件 返回值类型
 */
public class TelerobotResponse<T> {
    private int code;
    private String message;
    private T result;

    public TelerobotResponse() {
    }

    public TelerobotResponse(int code, String message) {
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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
