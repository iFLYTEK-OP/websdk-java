package cn.xfyun.api;

import cn.xfyun.base.webscoket.WebSocketClient;
import cn.xfyun.model.sign.AbstractSignature;
import cn.xfyun.model.sign.RtasrSignature;
import cn.xfyun.service.rta.AbstractRtasrWebSocketListener;
import cn.xfyun.service.rta.RtasrSendTask;
import okhttp3.*;
import okhttp3.internal.Util;

import java.io.*;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *   实时语音专业client类
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/3/17 10:24
 */
public class RtasrClient extends WebSocketClient {

    private static final String RTASR_URL = "wss://rtasr.xfyun.cn/v1/ws";

    public static final String SEND_END = "{\"end\": true}";

    /** 标点过滤控制，默认返回标点，punc=0会过滤结果中的标点 */
    private String punc;

    /** 垂直领域个性化参数, 设置示例：pd="edu" 参数pd为非必须设置，不设置参数默认为通用 */
    private String pd;

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();


    /**
     *   创建 client对象
     *
     * @param builder
     */
    public RtasrClient(Builder builder) {
        this.okHttpClient = new OkHttpClient().newBuilder().build();
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.request = builder.request;
        this.webSocket = builder.webSocket;
        this.pd = builder.pd;
        this.punc = builder.punc;
        if (Objects.isNull(builder.hostUrl)) {
            this.originHostUrl = RTASR_URL;
            this.hostUrl = RTASR_URL;
        } else {
            this.originHostUrl = builder.hostUrl;
            this.hostUrl = builder.hostUrl;
        }
        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.callTimeout = builder.callTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
    }

    /**
     * 生成鉴权对象，并建立websocket连接
     *
     * @return
     * @throws MalformedURLException
     * @throws SignatureException
     */
    @Override
    protected void createWebSocketConnect(WebSocketListener webSocketListener) throws SignatureException {
        this.signature = new RtasrSignature(appId, apiKey);
        String url = RTASR_URL + signature.getSigna();
        this.request = new Request.Builder().url(url).build();
        // 创建websocket连接
        this.webSocket = okHttpClient.newWebSocket(request, webSocketListener);
    }

    /**
     *   发送pcm流
     *
     * @param inputStream
     * @throws InterruptedException
     */
    public void send(InputStream inputStream, AbstractRtasrWebSocketListener webSocketListener) throws SignatureException {
        // 创建webSocket连接
        createWebSocketConnect(webSocketListener);

        if (inputStream == null) {
            webSocket.close(1000, null);
            return;
        }
        // 实时语音转写数据发送任务
        RtasrSendTask rtasrSendTask = new RtasrSendTask();
        new RtasrSendTask.Builder()
                .inputStream(inputStream)
                .webSocket(this.webSocket)
                .build(rtasrSendTask);

        executorService.submit(rtasrSendTask);
    }

    /**
     *   发送pcm字节
     *
     * @param bytes
     * @throws InterruptedException
     */
    public void send(byte[] bytes,Closeable closeable, AbstractRtasrWebSocketListener webSocketListener) throws SignatureException {
        if (bytes == null || bytes.length == 0) {
            return;
        }
        // 创建webSocket连接
        createWebSocketConnect(webSocketListener);

        // 实时语音转写数据发送任务
        RtasrSendTask iatSendTask = new RtasrSendTask();
        new RtasrSendTask.Builder()
                .bytes(bytes)
                .webSocket(webSocket)
                .closeable(closeable)
                .build(iatSendTask);
        executorService.submit(iatSendTask);
    }

    /**
     *    创建websocket对象
     *
     * @param webSocketListener
     * @return
     * @throws SignatureException
     */
    public WebSocket newWebSocket(AbstractRtasrWebSocketListener webSocketListener) {
        String url = RTASR_URL + new RtasrSignature(appId, apiKey).getSigna();
        this.request = new Request.Builder().url(url).build();
        // 创建websocket连接
        this.webSocket = okHttpClient.newWebSocket(request, webSocketListener);
        return webSocket;
    }

    public void sendEnd() {
        webSocket.send(SEND_END);
        closeWebsocket();
    }

    @Override
    public WebSocket getWebSocket() {
        return webSocket;
    }

    public String getPunc() {
        return punc;
    }

    public String getPd() {
        return pd;
    }

    public String getHostUrl() {
        return originHostUrl;
    }

    public String getOriginHostUrl() {
        return originHostUrl;
    }

    public String getAppId() {
        return appId;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getApiKey() {
        return apiKey;
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

    public Request getRequest() {
        return request;
    }

    public AbstractSignature getSignature() {
        return signature;
    }

    /**
     *   rtasrClient构建对象
     */
    public static final class Builder {
        // websocket相关
        boolean retryOnConnectionFailure = true;

        int callTimeout = 0;

        int connectTimeout = 10000;

        int readTimeout = 10000;

        int writeTimeout = 10000;

        int pingInterval = 0;

        private String hostUrl;

        private String appId;

        private String apiKey;

        private Request request;

        private WebSocket webSocket;

        private String punc;

        private String pd;


        public RtasrClient.Builder request(Request request) {
            this.request = request;
            return this;
        }

        public RtasrClient.Builder webSocket(WebSocket webSocket) {
            this.webSocket = webSocket;
            return this;
        }

        public RtasrClient.Builder addPunc() {
            this.punc = "0";
            return this;
        }

        public RtasrClient.Builder hostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        public RtasrClient.Builder addPd(String pd) {
            this.pd = pd;
            return this;
        }

        public RtasrClient.Builder callTimeout(long timeout, TimeUnit unit) {
            this.callTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public RtasrClient.Builder connectTimeout(long timeout, TimeUnit unit) {
            this.connectTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public RtasrClient.Builder readTimeout(long timeout, TimeUnit unit) {
            this.readTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public RtasrClient.Builder writeTimeout(long timeout, TimeUnit unit) {
            this.writeTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public RtasrClient.Builder pingInterval(long interval, TimeUnit unit) {
            this.pingInterval = Util.checkDuration("interval", interval, unit);
            return this;
        }

        public RtasrClient.Builder retryOnConnectionFailure(boolean retryOnConnectionFailure) {
            this.retryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }

        public RtasrClient.Builder signature(String appId, String apiKey) {
            this.appId = appId;
            this.apiKey = apiKey;
            return this;
        }

        public RtasrClient build() {
            RtasrClient rtasrClient = new RtasrClient(this);
            return rtasrClient;
        }

    }
}
