package cn.xfyun.api;

import cn.xfyun.config.Client;
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
 * @author <ydwang16@iflytek.com>
 * @description websocket能力的客户端
 * @date 2021/4/7
 */
public class WebSocketClient extends Client {

    protected String originHostUrl;

    /**
     * websocket相关配置
     */
    protected Request request;

    protected OkHttpClient okHttpClient;

    protected WebSocket webSocket;

    protected AbstractSignature signature;


    /**
     * webSocket超时时间相关
     */
    protected boolean retryOnConnectionFailure;
    protected int callTimeout;
    protected int connectTimeout;
    protected int readTimeout;
    protected int writeTimeout;
    protected int pingInterval;

    public WebSocket getWebSocket() {
        return webSocket;
    }

    /**
     * 生成鉴权对象，并建立websocket连接
     *
     * @return
     * @throws MalformedURLException
     * @throws SignatureException
     */
    protected void createWebSocketConnect(WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        this.signature = new Hmac256Signature(apiKey, apiSecret, originHostUrl);
        String url = AuthUtil.generateRequestUrl(signature);
        this.hostUrl = url.replace("http://", "ws://").replace("https://", "wss://");
        this.request = new Request.Builder().url(this.hostUrl).build();

        // 创建websocket连接
        newWebSocket(webSocketListener);
    }

    /**
     * 为语音听写client新建webSocket
     *
     * @param listener
     * @return
     */
    protected void newWebSocket(WebSocketListener listener) {
        this.webSocket = okHttpClient.newWebSocket(request, listener);
    }

    /**
     * 关闭websocket连接
     */
    public void closeWebsocket() {
        this.webSocket.close(1000, null);
        okHttpClient.connectionPool().evictAll();
    }
}
