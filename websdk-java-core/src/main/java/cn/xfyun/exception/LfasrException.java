package cn.xfyun.exception;

/**
 * 语音转写业务自定义异常
 *
 * @author : iflytek
 * @date : 2021年03月15日
 */
public class LfasrException extends RuntimeException {
    public LfasrException(String error) {
        super(error);
    }
}
