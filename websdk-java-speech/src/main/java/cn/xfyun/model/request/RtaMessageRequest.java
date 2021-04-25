package cn.xfyun.model.request;

import okio.ByteString;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/3/18 20:22
 */
public class RtaMessageRequest implements Delayed {

    private ByteString bytes;
    private long currentTimeMillis;

    public RtaMessageRequest(ByteString bytes) {
        this.bytes = bytes;
        this.currentTimeMillis = System.currentTimeMillis() + 40;
    }

    public ByteString getBytes() {
        return bytes;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return currentTimeMillis - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        RtaMessageRequest request = (RtaMessageRequest) o;
        return request.currentTimeMillis > this.currentTimeMillis ? -1 : 1;
    }
}
