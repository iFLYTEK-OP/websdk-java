package cn.xfyun.base.webscoket;

import cn.xfyun.base.Client;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import java.io.Closeable;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author <ydwang16@iflytek.com>
 * @description websocket能力的客户端
 * @date 2021/4/7
 */
public abstract class WebSocketClient extends Client {

        protected Request request;

        protected OkHttpClient okHttpClient;

        protected WebSocket webSocket;

        protected Integer waitMillis;

        protected byte[] bytes;

        protected InputStream inputStream;

        protected Closeable closeable;

        protected Integer frameSize;

    public WebSocketClient(WebSocketBuilder builder) {
        this.hostUrl = builder.hostUrl;
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.apiSecret = builder.apiSecret;
        this.okHttpClient = new OkHttpClient
                .Builder()
                .callTimeout(builder.callTimeout, TimeUnit.SECONDS)
                .connectTimeout(builder.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(builder.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(builder.writeTimeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(builder.retryOnConnectionFailure)
                .pingInterval(builder.pingInterval, TimeUnit.SECONDS)
                .build();
        this.waitMillis = builder.waitMillis;
        this.bytes = builder.bytes;
        this.inputStream = builder.inputStream;
        this.closeable = builder.closeable;
        this.frameSize = builder.frameSize;
    }

    protected void createWebSocketConnect(WebSocketListener webSocketListener) {
        this.request = new Request.Builder().url(getSignature()).build();
        this.webSocket = okHttpClient.newWebSocket(request, webSocketListener);
    }

    public void closeWebsocket() {
        this.webSocket.close(1000, null);
        okHttpClient.connectionPool().evictAll();
    }

    public abstract String getSignature();

    public boolean isRetryOnConnectionFailure() {
        return okHttpClient.retryOnConnectionFailure();
    }

    public int getCallTimeout() {
        return okHttpClient.callTimeoutMillis() / 1000;
    }

    public int getConnectTimeout() {
        return okHttpClient.connectTimeoutMillis() / 1000;
    }

    public int getReadTimeout() {
        return okHttpClient.readTimeoutMillis() / 1000;
    }

    public int getWriteTimeout() {
        return okHttpClient.writeTimeoutMillis() / 1000;
    }

    public int getPingInterval() {
        return okHttpClient.pingIntervalMillis() / 1000;
    }

}
