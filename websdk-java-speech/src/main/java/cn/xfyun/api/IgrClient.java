package cn.xfyun.api;

import cn.xfyun.base.websocket.WebSocketClient;
import cn.xfyun.common.IgrConstant;
import cn.xfyun.model.request.iat.IatBusiness;
import cn.xfyun.model.request.iat.IatRequest;
import cn.xfyun.model.request.iat.IatRequestData;
import cn.xfyun.model.request.igr.IgrRequest;
import cn.xfyun.model.sign.AbstractSignature;
import cn.xfyun.model.sign.Hmac256Signature;
import cn.xfyun.service.igr.IgrSendTask;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author: <flhong2@iflytek.com>
 * @description: 性别年龄识别
 * @version: v1.0
 * @create: 2021-06-01 16:56
 **/
public class IgrClient extends WebSocketClient {

    private static final Logger logger = LoggerFactory.getLogger(IatClient.class);

    /**
     * 公共参数，仅在握手成功后首帧请求时上传
     */
    private JsonObject common;
    /**
     * 业务参数，在握手成功后首帧请求与后续数据发送时上传
     */
    private JsonObject business;
    /**
     * 业务数据流参数，在握手成功后的所有请求中都需要上传
     */
    private JsonObject data;
    /**
     * 在平台申请的APPID信息
     */
    private String appId;
    /**
     * 引擎类型，目前仅支持igr
     */
    private String ent;
    /**
     * 音频格式
     * raw：原生音频数据pcm格式
     * speex：speex格式（rate需设置为8000）
     * speex-wb：宽频speex格式（rate需设置为16000）
     * amr：amr格式（rate需设置为8000）
     * amr-wb：宽频amr格式（rate需设置为16000）
     */
    private String aue;
    /**
     * 音频采样率 16000/8000,必填
     */
    private int rate;
    private Integer frameSize = IgrConstant.IGR_SIZE_FRAME;
    private ExecutorService executorService;

    public IgrClient(Builder builder) {
        this.appId = builder.appId;
        this.ent = builder.ent;
        this.aue = builder.aue;
        this.rate = builder.rate;

        this.common = builder.common;
        this.business = builder.business;

        this.apiKey = builder.apiKey;
        this.apiSecret = builder.apiSecret;
        this.originHostUrl = builder.hostUrl;

        this.request = builder.request;
        this.frameSize = builder.frameSize;
        this.okHttpClient = new OkHttpClient().newBuilder().build();
        this.signature = builder.signature;
        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.callTimeout = builder.callTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
        this.executorService = (null == builder.executorService) ? Executors.newSingleThreadExecutor() : builder.executorService;
    }

    /**
     * 听写服务端启动
     */
    public void start(WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        // 创建webSocket连接
        createWebSocketConnect(webSocketListener);
    }

    /**
     * 听写发送数据
     */
    public void sendMessage(byte[] bytes, Integer status) {
        // 发送数据,请求数据均为json字符串
        IgrRequest frame = new IgrRequest();

        // 第一帧才需要的参数
        if (0 == status) {
            // 填充common,只有第一帧需要
            JsonObject common = new JsonObject();
            common.addProperty("app_id", appId);

            // 填充business,第一帧必须发送
            JsonObject business = new JsonObject();
            business.addProperty("ent", ent);
            business.addProperty("aue", aue);
            business.addProperty("rate", rate);

            // 填充frame
            frame.setCommon(common);
            frame.setBusiness(business);
        }

        // 填充data，每一帧都要发送
        JsonObject data = new JsonObject();
        data.addProperty("status", status);
        if (bytes == null || bytes.length == 0) {
            data.addProperty("audio", "");
        } else {
            data.addProperty("audio", Base64.getEncoder().encodeToString(bytes));
        }
        frame.setData(data);

        String json = StringUtils.gson.toJson(frame);
        logger.debug("性别年龄识别请求入参：{}", json);
        webSocket.send(json);
    }

    /**
     * 发送音频文件给性别年龄识别服务端
     *
     * @param file 发送的文件
     * @throws FileNotFoundException
     */
    public void send(File file, WebSocketListener webSocketListener) throws FileNotFoundException, MalformedURLException, SignatureException {
        // createWebSocketConnect(webSocketListener);
        FileInputStream fileInputStream = new FileInputStream(file);
        send(fileInputStream, webSocketListener);
    }

    /**
     * 将base64编码后的数据发给性别年龄识别服务端
     *
     * @param data 发送的文件
     * @throws FileNotFoundException
     */
    public void send(String data, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        // createWebSocketConnect(webSocketListener);
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        send(inputStream, webSocketListener);
    }

    /**
     * 发送文件流给服务端
     *
     * @param inputStream 需要发送的流
     */
    public void send(InputStream inputStream, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        // 创建webSocket连接
        createWebSocketConnect(webSocketListener);
        if (inputStream == null) {
            webSocket.close(1000, null);
            return;
        }

        // 数据发送任务
        IgrSendTask igrSendTask = new IgrSendTask();
        new IgrSendTask.Builder()
                .inputStream(inputStream)
                .webSocketClient(this)
                .build(igrSendTask);

        executorService.submit(igrSendTask);
    }

    /**
     * @param bytes
     * @param closeable 需要关闭的流，可为空
     */
    public void send(byte[] bytes, Closeable closeable, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        // 创建webSocket连接
        createWebSocketConnect(webSocketListener);
        if (bytes == null || bytes.length == 0) {
            webSocket.close(1000, null);
            return;
        }

        IgrSendTask igrSendTask = new IgrSendTask();
        new IgrSendTask.Builder()
                .bytes(bytes)
                .webSocketClient(this)
                .closeable(closeable)
                .build(igrSendTask);

        executorService.submit(igrSendTask);
    }

    public String getAppId() {
        return this.appId;
    }

    public String getEnt() {
        return this.ent;
    }

    public String getAue() {
        return this.aue;
    }

    public int getRate() {
        return this.rate;
    }

    public String getHostUrl() {
        return this.originHostUrl;
    }

    public String getOriginHostUrl() {
        return this.originHostUrl;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public String getApiSecret() {
        return this.apiSecret;
    }

    public Request getRequest() {
        return this.request;
    }

    public OkHttpClient getClient() {
        return this.okHttpClient;
    }

    @Override
    public WebSocket getWebSocket() {
        return this.webSocket;
    }

    public boolean isRetryOnConnectionFailure() {
        return this.retryOnConnectionFailure;
    }

    public int getCallTimeout() {
        return this.callTimeout;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public int getWriteTimeout() {
        return this.writeTimeout;
    }

    public int getPingInterval() {
        return this.pingInterval;
    }

    public AbstractSignature getSignature() {
        return this.signature;
    }

    public Integer getFrameSize() {
        return this.frameSize;
    }

    public static class Builder {

        private JsonObject common = new JsonObject();
        private JsonObject business = new JsonObject();

        private String appId;
        private String ent = "igr";
        private String aue;
        // 设置个兜底值8000
        private int rate = 8000;

        private String hostUrl = IgrConstant.HOST_URL;
        private String apiKey;
        private String apiSecret;
        private Hmac256Signature signature;
        private Integer frameSize = IgrConstant.IGR_SIZE_FRAME;

        private Request request;
        private OkHttpClient client;
        private ExecutorService executorService;

        // websocket相关
        boolean retryOnConnectionFailure = true;
        int callTimeout = 0;
        int connectTimeout = 10000;
        int readTimeout = 10000;
        int writeTimeout = 10000;
        int pingInterval = 0;

        public IgrClient build() {
            return new IgrClient(this);
        }

        public IgrClient.Builder signature(String appId, String apiKey, String apiSecret) {
            this.appId = appId;
            this.apiKey = apiKey;
            this.apiSecret = apiSecret;
            Hmac256Signature signature = new Hmac256Signature(apiKey, apiSecret, hostUrl);
            this.signature = signature;
            return this;
        }

        public IgrClient.Builder appId(String appId) {
            this.appId = appId;
            this.common.addProperty("app_id", appId);
            return this;
        }

        public IgrClient.Builder ent(String ent) {
            this.ent = ent;
            this.business.addProperty("ent", ent);
            return this;
        }

        public IgrClient.Builder aue(String aue) {
            this.aue = aue;
            this.business.addProperty("aue", aue);
            return this;
        }

        public IgrClient.Builder rate(int rate) {
            this.rate = rate;
            this.business.addProperty("aue", aue);
            return this;
        }

        public IgrClient.Builder hostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        public IgrClient.Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public IgrClient.Builder apiSecret(String apiSecret) {
            this.apiSecret = apiSecret;
            return this;
        }

        public IgrClient.Builder common(JsonObject common) {
            this.common = common;
            return this;
        }

        public IgrClient.Builder business(JsonObject business) {
            this.business = business;
            return this;
        }

        public IgrClient.Builder frameSize(Integer frameSize) {
            this.frameSize = frameSize;
            return this;
        }

        public IgrClient.Builder addRequest(Request request) {
            this.request = request;
            return this;
        }


        public IgrClient.Builder addClient(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public IgrClient.Builder callTimeout(long timeout, TimeUnit unit) {
            this.callTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public IgrClient.Builder connectTimeout(long timeout, TimeUnit unit) {
            this.connectTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public IgrClient.Builder readTimeout(long timeout, TimeUnit unit) {
            this.readTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public IgrClient.Builder writeTimeout(long timeout, TimeUnit unit) {
            this.writeTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public IgrClient.Builder pingInterval(long interval, TimeUnit unit) {
            this.pingInterval = Util.checkDuration("interval", interval, unit);
            return this;
        }

        public IgrClient.Builder retryOnConnectionFailure(boolean retryOnConnectionFailure) {
            this.retryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }

        public IgrClient.Builder executorService(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }
    }
}
