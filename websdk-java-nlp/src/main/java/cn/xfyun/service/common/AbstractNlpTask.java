package cn.xfyun.service.common;

import cn.xfyun.base.websocket.AbstractClient;
import cn.xfyun.util.IOCloseUtil;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * websocket间断发送Task
 *
 * @author <zyding6@ifytek.com>
 */
public abstract class AbstractNlpTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractNlpTask.class);

    protected AbstractClient client;

    protected WebSocket webSocket;

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

    public AbstractNlpTask build(Builder builder) {
        this.waitMillis = builder.waitMillis;
        this.bytes = builder.bytes;
        this.closeable = builder.closeable;
        this.inputStream = builder.inputStream;
        this.frameSize = builder.frameSize;
        this.client = builder.client;
        this.webSocket = builder.webSocket;
        return this;
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[frameSize];

            // 流操作
            if (inputStream != null) {
                // 文件块编号
                int seq = 0;
                do {
                    seq++;
                    int readLength = inputStream.read(buffer);
                    if (readLength == -1) {
                        isDataEnd = true;
                    }

                    String data = businessDataProcess(isDataEnd ? null : Arrays.copyOf(buffer, readLength), isDataEnd, seq);
                    webSocket.send(data);
                    // 间隔发送
                    TimeUnit.MILLISECONDS.sleep(waitMillis);
                } while (!isDataEnd);
            } else {
                // 针对于byte数组的操作
                if (bytes != null && bytes.length > 0) {
                    int byteLen = bytes.length;
                    // 文件块编号
                    int seq = 0;
                    for (int startIndex = 0; ; startIndex += frameSize) {
                        seq++;
                        if (startIndex + frameSize < byteLen) {
                            byte[] contents = Arrays.copyOfRange(bytes, startIndex, startIndex + frameSize);
                            String data = businessDataProcess(contents, false, seq);
                            webSocket.send(data);
                        } else {
                            byte[] contents = Arrays.copyOfRange(bytes, startIndex, byteLen);
                            String data = businessDataProcess(contents, true, seq);
                            webSocket.send(data);
                            logger.info("数据发送完毕!");
                            break;
                        }
                        // 间隔发送
                        TimeUnit.MILLISECONDS.sleep(waitMillis);
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
     * @param contents 文本内容
     * @param isEnd    是否结束
     * @param seq      数据块
     * @return 请求入参
     * @throws InterruptedException 线程异常
     */
    public abstract String businessDataProcess(byte[] contents, boolean isEnd, Integer seq) throws InterruptedException;

    public static final class Builder {

        private Integer waitMillis = 40;
        private AbstractClient client;
        private WebSocket webSocket;
        private byte[] bytes;
        private Closeable closeable;
        private InputStream inputStream;
        private Integer frameSize = 1280;

        public Builder waitMillis(Integer waitMills) {
            this.waitMillis = waitMills;
            return this;
        }

        public Builder client(AbstractClient client) {
            this.client = client;
            return this;
        }

        public Builder webSocket(WebSocket webSocket) {
            this.webSocket = webSocket;
            return this;
        }

        public Builder bytes(byte[] bytes) {
            this.bytes = bytes;
            return this;
        }

        public Builder inputStream(InputStream inputStream) {
            this.inputStream = inputStream;
            return this;
        }

        public Builder closeable(Closeable closeable) {
            this.closeable = closeable;
            return this;
        }

        public Builder frameSize(Integer frameSize) {
            this.frameSize = frameSize;
            return this;
        }

        public void build(AbstractNlpTask task) {
            task.build(this);
        }
    }
}
