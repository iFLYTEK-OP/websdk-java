package cn.xfyun.base.websocket;

import okhttp3.OkHttpClient;
import okhttp3.internal.Util;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * websocket 构建器
 *
 * @author <zyding6@ifytek.com>
 */
public abstract class WebsocketBuilder<T> {

    /**
     * 用户鉴权相关信息
     */
    private String appId;
    private String apiKey;
    private String apiSecret;
    /**
     * websocket相关
     */
    private boolean retryOnConnectionFailure = true;
    private int callTimeout = 0;
    private int connectTimeout = 30000;
    private int readTimeout = 30000;
    private int writeTimeout = 30000;
    private int pingInterval = 0;
    /**
     * okHttpClient
     */
    private OkHttpClient httpClient;
    /**
     * 代理
     */
    private Proxy proxy;

    protected T signature(String appId, String apiKey, String apiSecret) {
        this.appId = appId;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        return (T)this;
    }

    public T httpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
        return (T)this;
    }

    public T callTimeout(long timeout, TimeUnit unit) {
        this.callTimeout = Util.checkDuration("timeout", timeout, unit);
        return (T)this;
    }

    public T connectTimeout(long timeout, TimeUnit unit) {
        this.connectTimeout = Util.checkDuration("timeout", timeout, unit);
        return (T)this;
    }

    public T readTimeout(long timeout, TimeUnit unit) {
        this.readTimeout = Util.checkDuration("timeout", timeout, unit);
        return (T)this;
    }

    public T writeTimeout(long timeout, TimeUnit unit) {
        this.writeTimeout = Util.checkDuration("timeout", timeout, unit);
        return (T)this;
    }

    public T pingInterval(long interval, TimeUnit unit) {
        this.pingInterval = Util.checkDuration("interval", interval, unit);
        return (T)this;
    }

    public T retryOnConnectionFailure(boolean retryOnConnectionFailure) {
        this.retryOnConnectionFailure = retryOnConnectionFailure;
        return (T)this;
    }

    public T proxy(Proxy proxy) {
        this.proxy = proxy;
        return (T)this;
    }

    public T appId(String appId) {
        this.appId = appId;
        return (T)this;
    }

    public T apiKey(String apiKey) {
        this.apiKey = apiKey;
        return (T)this;
    }

    public T apiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
        return (T)this;
    }

    public String getAppId() {
        return appId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public boolean getRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }

    public int getCallTimeout() {
        return callTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public int getPingInterval() {
        return pingInterval;
    }

    public abstract AbstractClient build();
}
