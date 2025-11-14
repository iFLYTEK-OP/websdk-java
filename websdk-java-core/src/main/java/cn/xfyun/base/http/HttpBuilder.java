package cn.xfyun.base.http;


import okhttp3.OkHttpClient;
import java.net.Proxy;


/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/2 13:46
 */
public abstract class HttpBuilder<T> {

    private String hostUrl;

    private String appId;

    private String apiKey;

    private String apiSecret;

    private boolean retryOnConnectionFailure = false;

    /**
     *   整个流程耗费的超时时间
     */
    private int callTimeout = 0;

    /**
     *   三次握手 + SSL建立耗时
     */
    private int connectTimeout = 10;

    private int readTimeout = 10;

    /**
     *  sink写入耗时
     */
    private int writeTimeout = 10;

    /**
     * okHttpClient
     */
    private OkHttpClient httpClient;

    /**
     * proxy
     */
    private Proxy proxy;

    public HttpBuilder(String hostUrl, String appId, String apiKey, String apiSecret) {
        this.hostUrl = hostUrl;
        this.appId = appId;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public T httpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
        return (T)this;
    }

    public T retryOnConnectionFailure(boolean retryOnConnectionFailure) {
        this.retryOnConnectionFailure = retryOnConnectionFailure;
        return (T)this;
    }

    public T callTimeout(int callTimeout) {
        this.callTimeout = callTimeout;
        return (T)this;
    }

    public T connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return (T)this;
    }

    public T readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return (T)this;
    }

    public T writeTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return (T)this;
    }

    public T proxy(Proxy proxy) {
        this.proxy = proxy;
        return (T)this;
    }

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

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
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

    public abstract HttpClient build();

}
