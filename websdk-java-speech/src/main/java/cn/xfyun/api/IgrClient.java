package cn.xfyun.api;

import cn.xfyun.base.webscoket.WebSocketBuilder;
import cn.xfyun.base.webscoket.WebSocketClient;
import cn.xfyun.common.IgrConstant;
import cn.xfyun.model.sign.AbstractSignature;
import cn.xfyun.model.sign.Hmac256Signature;
import cn.xfyun.service.igr.IgrSendTask;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.Util;

import java.io.*;
import java.net.MalformedURLException;
import java.security.SignatureException;
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

    public IgrClient(Builder builder) {
        super(builder);
        this.ent = builder.ent;
        this.aue = builder.aue;
        this.rate = builder.rate;
        this.frameSize = builder.frameSize;
    }

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * 发送音频文件给性别年龄识别服务端
     *
     * @param file 发送的文件
     * @throws FileNotFoundException
     */
    public void send(File file, WebSocketListener webSocketListener) throws FileNotFoundException, MalformedURLException, SignatureException {
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

    @Override
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

    public Integer getFrameSize() {
        return this.frameSize;
    }

    @Override
    public String getSignature() {
        return null;
    }

    public static final class Builder extends WebSocketBuilder<Builder> {

        private JsonObject common = new JsonObject();
        private JsonObject business = new JsonObject();

        private String ent = "igr";

        private String aue;

        private int rate = 8000;

        private Hmac256Signature signature;

        private Integer frameSize = IgrConstant.IGR_SIZE_FRAME;

        public Builder(String appId, String apiKey, String apiSecret) {
            super(IgrConstant.HOST_URL, appId, apiKey, apiSecret);
            this.signature = new Hmac256Signature(apiKey, apiSecret, hostUrl);
        }

        @Override
        public IgrClient build() {
            return new IgrClient(this);
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

        public IgrClient.Builder frameSize(Integer frameSize) {
            this.frameSize = frameSize;
            return this;
        }

    }
}
