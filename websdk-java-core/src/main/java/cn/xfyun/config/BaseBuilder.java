package cn.xfyun.config;


/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/2 13:46
 */
public abstract class BaseBuilder {

    private String hostUrl;

    private String appId;

    private String apiKey;

    private String apiSecret;

    private boolean retryOnConnectionFailure = false;

    private int callTimeout = 0;

    private int connectTimeout = 10000;

    private int readTimeout = 10000;

    private int writeTimeout = 10000;

    public BaseBuilder(String hostUrl, String appId, String apiKey, String apiSecret) {
        this.hostUrl = hostUrl;
        this.appId = appId;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }


    public void retryOnConnectionFailure(boolean retryOnConnectionFailure) {
        this.retryOnConnectionFailure = retryOnConnectionFailure;
    }

    public void callTimeout(int callTimeout) {
        this.callTimeout = callTimeout;
    }

    public void connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void writeTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void hostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
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

    public void appId(String appId) {
        this.appId = appId;
    }

    public void apiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void apiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
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

}
