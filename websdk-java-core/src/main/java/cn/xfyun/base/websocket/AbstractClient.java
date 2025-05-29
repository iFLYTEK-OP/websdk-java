package cn.xfyun.base.websocket;

import cn.xfyun.model.sign.AbstractSignature;
import cn.xfyun.model.sign.Hmac256Signature;
import cn.xfyun.util.AuthUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import java.net.MalformedURLException;
import java.security.SignatureException;

/**
 * websocket能力的客户端
 *
 * @author <zyding6@iflytek.com>
 */
public abstract class AbstractClient {

    /**
     * 用户鉴权相关信息
     */
    protected String appId;

    protected String apiKey;

    protected String apiSecret;

    /**
     * 原始url地址
     */
    protected String originHostUrl;

    /**
     * OKHttpClient对象
     */
    protected OkHttpClient okHttpClient;

    /**
     * webSocket超时时间相关
     */
    protected boolean retryOnConnectionFailure;
    protected int callTimeout;
    protected int connectTimeout;
    protected int readTimeout;
    protected int writeTimeout;
    protected int pingInterval;

    public String getAppId() {
        return appId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getOriginHostUrl() {
        return originHostUrl;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public boolean isRetryOnConnectionFailure() {
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

    public int getPingInterval() {
        return pingInterval;
    }

    /**
     * 获取鉴权对象
     */
    public AbstractSignature getSignature() {
        return new Hmac256Signature(apiKey, apiSecret, originHostUrl);
    }

    /**
     * 获取请求地址
     */
    public String getHostUrl() throws MalformedURLException, SignatureException {
        String url = AuthUtil.generateRequestUrl(getSignature());
        return url.replace("http://", "ws://").replace("https://", "wss://");
    }

    /**
     * 获取鉴权请求
     */
    public Request getRequest() throws MalformedURLException, SignatureException {
        return new Request.Builder().url(getHostUrl()).build();
    }

    /**
     * 新建webSocket
     *
     * @param listener WebSocketListener对象
     * @return websocket对象
     */
    protected WebSocket newWebSocket(WebSocketListener listener) throws MalformedURLException, SignatureException {
        // 创建websocket连接
        return okHttpClient.newWebSocket(getRequest(), listener);
    }
}
