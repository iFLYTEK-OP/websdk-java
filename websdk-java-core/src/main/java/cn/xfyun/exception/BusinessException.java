package cn.xfyun.exception;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/5 16:09
 */
public class BusinessException extends RuntimeException{

    public BusinessException(String msg) {
        super(msg);
    }
}
