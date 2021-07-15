package cn.xfyun.service.common;

import okhttp3.WebSocket;

import java.io.Closeable;
import java.io.InputStream;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/12 14:34
 */
public abstract class AbstractTaskBuilder<T> {

    protected Integer waitMillis = 40;

    protected WebSocket webSocket;

    protected byte[] bytes;

    protected Closeable closeable;

    protected InputStream inputStream;

    protected Integer frameSize = 1280;

    public AbstractTaskBuilder(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public T waitMillis(Integer waitMills) {
        this.waitMillis = waitMills;
        return (T)this;
    }

    public T webSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
        return (T)this;
    }

    public T bytes(byte[] bytes) {
        this.bytes = bytes;
        return (T)this;
    }

    public T inputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        return (T)this;
    }

    public T closeable(Closeable closeable) {
        this.closeable = closeable;
        return (T)this;
    }

    public T frameSize(Integer frameSize) {
        this.frameSize = frameSize;
        return (T)this;
    }

    public abstract AbstractTimedTask build();
}
