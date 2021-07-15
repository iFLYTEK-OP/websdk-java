package cn.xfyun.service.common;

import cn.xfyun.util.IOCloseUtil;
import okhttp3.WebSocket;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author <ydwang16@iflytek.com>
 * @description websocket间断发送Task
 * @date 2021/3/27
 */
public abstract class AbstractTimedTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTimedTask.class);

    protected WebSocket webSocket;

    protected Integer waitMillis;

    protected byte[] bytes;

    protected InputStream inputStream;

    protected Closeable closeable;

    protected Integer frameSize;

    public AbstractTimedTask(AbstractTaskBuilder builder) {
        this.waitMillis = builder.waitMillis;
        this.bytes = builder.bytes;
        this.closeable = builder.closeable;
        this.inputStream = builder.inputStream;
        this.frameSize = builder.frameSize;
        this.webSocket = builder.webSocket;
    }

    @Override
    public void run() {
        try {
            if (inputStream != null) {
                sendByInputStream(inputStream);
            } else {
                sendByBytes(bytes);
            }
        } catch (Exception e) {
            logger.error("处理数据异常", e);
        } finally {
            IOCloseUtil.close(closeable);
            IOCloseUtil.close(inputStream);
        }
    }

    private void sendByBytes(byte[] bytes) throws InterruptedException {
        // 针对于byte数组的操作
        if (bytes != null && bytes.length > 0) {
            for (int i = 0; i < bytes.length; i += frameSize) {
                int len = Math.min(i + frameSize, bytes.length);
                byte[] cur = Arrays.copyOfRange(bytes, i, len);
                webSocket.send(ByteString.of(cur));
                Thread.sleep(waitMillis);
            }
        }
    }

    private void sendByInputStream(InputStream inputStream) throws IOException, InterruptedException {
        byte[] buffer = new byte[frameSize];
        if (inputStream != null) {
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                if (len < frameSize) {
                    webSocket.send(ByteString.of(Arrays.copyOfRange(buffer, 0, len)));
                    break;
                }
                webSocket.send(ByteString.of(buffer));
                Thread.sleep(waitMillis);
            }
        }
    }
}
