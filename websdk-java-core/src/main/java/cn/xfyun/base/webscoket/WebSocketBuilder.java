package cn.xfyun.base.webscoket;


import okhttp3.WebSocket;

import java.io.Closeable;
import java.io.InputStream;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/9 10:32
 */
public abstract class WebSocketBuilder<T> {

    protected String hostUrl;

    protected String appId;

    protected String apiKey;

    protected String apiSecret;

    protected boolean retryOnConnectionFailure;

    protected int callTimeout = 0;

    protected int connectTimeout = 10;

    protected int readTimeout = 10;

    protected int writeTimeout = 10;

    protected int pingInterval = 0;

    protected Integer waitMillis = 40;

    protected byte[] bytes;

    protected Closeable closeable;

    protected InputStream inputStream;

    protected Integer frameSize = 1280;

    public WebSocketBuilder(String hostUrl, String appId, String apiKey, String apiSecret) {
        this.hostUrl = hostUrl;
        this.appId = appId;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public abstract WebSocketClient build();

    public String getHostUrl() {
        return hostUrl;
    }

    public T hostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
        return (T)this;
    }

    public String getAppId() {
        return appId;
    }

    public T appId(String appId) {
        this.appId = appId;
        return (T)this;
    }

    public String getApiKey() {
        return apiKey;
    }

    public T apiKey(String apiKey) {
        this.apiKey = apiKey;
        return (T)this;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public T apiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
        return (T)this;
    }

    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }

    public T retryOnConnectionFailure(boolean retryOnConnectionFailure) {
        this.retryOnConnectionFailure = retryOnConnectionFailure;
        return (T)this;
    }

    public int getCallTimeout() {
        return callTimeout;
    }

    public T callTimeout(int timeout) {
        this.callTimeout = timeout;
        return (T)this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public T connectTimeout(int timeout) {
        this.connectTimeout = timeout;
        return (T)this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public T readTimeout(int timeout) {
        this.readTimeout = timeout;
        return (T)this;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public T writeTimeout(int timeout) {
        this.writeTimeout = timeout;
        return (T)this;
    }

    public int getPingInterval() {
        return pingInterval;
    }

    public T pingInterval(int interval) {
        this.pingInterval = interval;
        return (T)this;
    }

    public T waitMillis(Integer waitMills) {
        this.waitMillis = waitMills;
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
}
