package cn.xfyun.exception;

/**
 * @author: <flhong2@iflytek.com>
 * @description: 性别年龄识别自定义异常
 * @version: v1.0
 * @create: 2021-06-02 11:08
 **/
public class IgrException extends RuntimeException {
    public IgrException(String msg) {
        super(msg);
    }
}
