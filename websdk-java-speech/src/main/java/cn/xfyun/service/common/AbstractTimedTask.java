package cn.xfyun.service.common;

import cn.xfyun.base.webscoket.WebSocketClient;
import cn.xfyun.util.IOCloseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author <ydwang16@iflytek.com>
 * @description websocket间断发送Task
 * @date 2021/3/27
 */
public abstract class AbstractTimedTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTimedTask.class);
    protected WebSocketClient webSocketClient;
    /**
     * 间隔时间
     */
    private Integer waitMillis = 40;
    private byte[] bytes;

    private InputStream inputStream;

    private Closeable closeable;

    private Integer frameSize = 1280;

    /**
     * 是否为数据的最后一块
     */
    private boolean isDataEnd = false;

    public AbstractTimedTask build(Builder builder) {
        this.waitMillis = builder.waitMillis;
        this.bytes = builder.bytes;
        this.closeable = builder.closeable;
        this.inputStream = builder.inputStream;
        this.frameSize = builder.frameSize;
        this.webSocketClient = builder.webSocketClient;

        return this;
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[frameSize];

            // 流操作
            if (inputStream != null) {
                do {
                    int readLength = inputStream.read(buffer);
                    if (readLength == -1) {
                        isDataEnd = true;
                    }

                    String data = businessDataProcess(isDataEnd ? null : Arrays.copyOf(buffer, readLength), isDataEnd);
                    webSocketClient.getWebSocket().send(data);

                    Thread.sleep(waitMillis);
                } while (!isDataEnd);
            } else {
                // 针对于byte数组的操作
                if (bytes != null && bytes.length > 0) {
                    int byteLen = bytes.length;

                    for (int startIndex = 0; ; startIndex += frameSize) {
                        if (startIndex + frameSize < byteLen) {
                            byte[] contents = Arrays.copyOfRange(bytes, startIndex, startIndex + frameSize);
                            String data = businessDataProcess(contents, false);
                            webSocketClient.getWebSocket().send(data);
                        } else {
                            byte[] contents = Arrays.copyOfRange(bytes, startIndex, byteLen);
                            String data = businessDataProcess(contents, true);
                            webSocketClient.getWebSocket().send(data);
                            logger.info("数据发送完毕!");
                            break;
                        }
                        Thread.sleep(waitMillis);
                    }

                }
            }
        } catch (Exception e) {
            logger.error("处理数据异常", e);
        } finally {
            IOCloseUtil.close(closeable);
            IOCloseUtil.close(inputStream);
        }
    }

    /**
     * 处理业务数据，这里需要每个需要的能力去重写
     *
     * @param contents
     * @param isEnd
     * @return
     * @throws InterruptedException
     */
    public abstract String businessDataProcess(byte[] contents, boolean isEnd) throws InterruptedException;

    public static final class Builder {
        private Integer waitMillis = 40;
        private WebSocketClient webSocketClient;
        private byte[] bytes;
        private Closeable closeable;
        private InputStream inputStream;
        private Integer frameSize = 1280;

        public AbstractTimedTask.Builder waitMillis(Integer waitMills) {
            this.waitMillis = waitMills;
            return this;
        }

        public AbstractTimedTask.Builder webSocketClient(WebSocketClient webSocketClient) {
            this.webSocketClient = webSocketClient;
            return this;
        }

        public AbstractTimedTask.Builder bytes(byte[] bytes) {
            this.bytes = bytes;
            return this;
        }

        public AbstractTimedTask.Builder inputStream(InputStream inputStream) {
            this.inputStream = inputStream;
            return this;
        }

        public AbstractTimedTask.Builder closeable(Closeable closeable) {
            this.closeable = closeable;
            return this;
        }

        public AbstractTimedTask.Builder frameSize(Integer frameSize) {
            this.frameSize = frameSize;
            return this;
        }

        public void build(AbstractTimedTask task) {
            task.build(this);
        }
    }
}
