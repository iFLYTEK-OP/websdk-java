package cn.xfyun.exception;

/**
 * @author yingpeng
 * 在线语音合成异常类
 */
public class TtsException extends RuntimeException {

    public TtsException(String error) {
        super(error);
    }
}
