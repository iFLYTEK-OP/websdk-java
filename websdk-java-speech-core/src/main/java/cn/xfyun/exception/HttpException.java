package cn.xfyun.exception;

/**
 * http自定义异常
 *
 * @author : iflytek
 * @date : 2021年03月15日
 */
public class HttpException extends Exception {
    private String errorCode;

    private String errorMsg;

    public HttpException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public HttpException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
        this.errorMsg = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
