package cn.xfyun.api;

import cn.xfyun.base.websocket.AbstractClient;
import cn.xfyun.model.simult.request.SimInterpRequest;
import cn.xfyun.service.simult.SimInterpSendTask;
import cn.xfyun.util.StringUtils;
import okhttp3.OkHttpClient;
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
 * 同声传译 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/nlp/simultaneous-interpretation/API.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class SimInterpClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(SimInterpClient.class);

    /**
     * 线程池管理
     * 默认单例线程池
     */
    private final ExecutorService executor;

    /**
     * 语转写语种，可选值：zh_cn
     */
    private final String language;

    /**
     * 语言过滤筛选
     * 1：中英文模式，中文英文均可识别（默认）
     * 2：中文模式，可识别出简单英文
     * 3：英文模式，只识别出英文
     * 4：纯中文模式，只识别出中文
     * 注意：中文引擎支持该参数，其他语言不支持。
     */
    private final int languageType;

    /**
     * 应用领域
     * 可选值：ist_ed_open
     */
    private final String domain;

    /**
     * 口音取值范围
     * 目前固定为mandarin
     */
    private final String accent;

    /**
     * 用于设置端点检测的静默时间，单位是毫秒。
     * 即静默多长时间后引擎认为音频结束，取值范围0~99999999
     */
    private final Integer eos;

    /**
     * vad强切控制，单位毫秒，默认15000
     */
    private final Integer vto;

    /**
     * 将返回结果的数字格式规则为阿拉伯数字格式，默认开启
     * 0：关闭
     * 1：开启
     */
    private final Integer nunum;

    /**
     * 源语种
     */
    private final String from;

    /**
     * 目标语种
     */
    private final String to;

    /**
     * 对应同传发音人，有以下可选值：
     * 英文女性：x2_catherine
     * 英文男性：x2_john
     * 成年女性：x2_xiaoguo
     * 成年男性：x2_xiaozhong
     * 儿童女声：x2_xiaofang_cts
     * 童声开心：x2_mengmenghappy
     * 童声自然：x2_mengmengnetural
     */
    private final String vcn;

    /**
     * 音频编码，注意更改生成文件的后缀（如.pcm或.mp3），可选值：
     * raw：合成pcm音频
     * lame：合成mp3音频
     */
    private final String encoding;

    /**
     * 采样率，可选值：16000
     */
    private final int sampleRate;

    /**
     * 声道数，可选值：1
     */
    private final int channels;

    /**
     * 位深，可选值：16
     */
    private final int bitDepth;

    public SimInterpClient(Builder builder) {
        this.okHttpClient = new OkHttpClient
                .Builder()
                .callTimeout(builder.callTimeout, TimeUnit.SECONDS)
                .connectTimeout(builder.callTimeout, TimeUnit.SECONDS)
                .readTimeout(builder.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(builder.writeTimeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(builder.retryOnConnectionFailure)
                .build();
        this.originHostUrl = builder.hostUrl;
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.apiSecret = builder.apiSecret;

        this.language = builder.language;
        this.languageType = builder.languageType;
        this.domain = builder.domain;
        this.accent = builder.accent;
        this.eos = builder.eos;
        this.vto = builder.vto;
        this.nunum = builder.nunum;
        this.from = builder.from;
        this.to = builder.to;
        this.vcn = builder.vcn;
        this.encoding = builder.encoding;
        this.sampleRate = builder.sampleRate;
        this.channels = builder.channels;
        this.bitDepth = builder.bitDepth;

        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.callTimeout = builder.callTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
        this.executor = (null == builder.executor) ? Executors.newSingleThreadExecutor() : builder.executor;
    }

    public String getLanguage() {
        return language;
    }

    public int getLanguageType() {
        return languageType;
    }

    public String getDomain() {
        return domain;
    }

    public String getAccent() {
        return accent;
    }

    public Integer getEos() {
        return eos;
    }

    public Integer getVto() {
        return vto;
    }

    public Integer getNunum() {
        return nunum;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getVcn() {
        return vcn;
    }

    public String getEncoding() {
        return encoding;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getChannels() {
        return channels;
    }

    public int getBitDepth() {
        return bitDepth;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    /**
     * 同声传译服务端启动
     */
    public WebSocket start(WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        // 创建webSocket连接
        return newWebSocket(webSocketListener);
    }

    /**
     * 同声传译发送数据
     */
    public void sendMessage(WebSocket webSocket, byte[] bytes, Integer status) {
        // 发送数据,求数据均为json字符串
        SimInterpRequest request = new SimInterpRequest();
        // 请求头
        SimInterpRequest.Header header = new SimInterpRequest.Header(this.appId, status);
        request.setHeader(header);
        // 请求参数
        SimInterpRequest.Parameter parameter = new SimInterpRequest.Parameter(this);
        request.setParameter(parameter);
        // 请求体
        SimInterpRequest.Payload payload = new SimInterpRequest.Payload();
        SimInterpRequest.Payload.Data data = new SimInterpRequest.Payload.Data();
        data.setSampleRate(sampleRate);
        data.setEncoding(encoding);
        data.setSeq(0);
        data.setStatus(status);
        if (bytes == null || bytes.length == 0) {
            data.setAudio("");
        } else {
            data.setAudio(Base64.getEncoder().encodeToString(bytes));
        }
        payload.setData(data);
        request.setPayload(payload);
        // 发送消息
        String json = StringUtils.gson.toJson(request);
        logger.debug("同声传译请求入参：{}", json);
        webSocket.send(json);
    }

    /**
     * 发送文件给同声传译服务端
     *
     * @param file              发送的文件
     * @param webSocketListener ws监听类 (SimInterpWebSocketListener)
     */
    public void send(File file, WebSocketListener webSocketListener) throws FileNotFoundException, MalformedURLException, SignatureException {
        FileInputStream fileInputStream = new FileInputStream(file);
        send(fileInputStream, webSocketListener);
    }

    /**
     * 发送文件流给同声传译服务端
     *
     * @param inputStream       需要发送的流
     * @param webSocketListener ws监听类 (SimInterpWebSocketListener)
     */
    public void send(InputStream inputStream, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        if (inputStream == null) {
            return;
        }

        // 创建webSocket连接
        WebSocket socket = newWebSocket(webSocketListener);

        // 同声传译听写数据发送任务
        SimInterpSendTask sparkIatSendTask = new SimInterpSendTask();
        new SimInterpSendTask.Builder()
                .inputStream(inputStream)
                .client(this)
                .webSocket(socket)
                .build(sparkIatSendTask);

        executor.submit(sparkIatSendTask);
    }

    /**
     * 发送字节数组给同声传译服务端
     *
     * @param bytes             字节数据
     * @param closeable         需要关闭的流，可为空
     * @param webSocketListener ws监听类 (SimInterpWebSocketListener)
     */
    public void send(byte[] bytes, Closeable closeable, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        if (bytes == null || bytes.length == 0) {
            return;
        }

        // 创建webSocket连接
        WebSocket socket = newWebSocket(webSocketListener);

        // 同声传译听写数据发送任务
        SimInterpSendTask sparkIatSendTask = new SimInterpSendTask();
        new SimInterpSendTask.Builder()
                .bytes(bytes)
                .client(this)
                .webSocket(socket)
                .closeable(closeable)
                .build(sparkIatSendTask);

        executor.submit(sparkIatSendTask);
    }

    public static final class Builder {

        /**
         * websocket相关
         */
        boolean retryOnConnectionFailure = true;
        int callTimeout = 0;
        int connectTimeout = 30000;
        int readTimeout = 30000;
        int writeTimeout = 30000;
        int pingInterval = 0;
        private String hostUrl = "https://ws-api.xf-yun.com/v1/private/simult_interpretation";
        private String appId;
        private String apiKey;
        private String apiSecret;
        private String language = "zh_cn";
        private int languageType = 1;
        private String domain = "ist_ed_open";
        private String accent = "mandarin";
        private Integer eos = 600000;
        private Integer vto = 15000;
        private Integer nunum = 1;
        private String from = "cn";
        private String to = "en";
        private String vcn = "x2_xiaoguo";
        private String encoding = "raw";
        private int sampleRate = 16000;
        private int channels = 1;
        private int bitDepth = 16;
        private ExecutorService executor;

        public SimInterpClient build() {
            return new SimInterpClient(this);
        }

        public Builder signature(String appId, String apiKey, String apiSecret) {
            this.appId = appId;
            this.apiKey = apiKey;
            this.apiSecret = apiSecret;
            return this;
        }

        public Builder callTimeout(long timeout, TimeUnit unit) {
            this.callTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder connectTimeout(long timeout, TimeUnit unit) {
            this.connectTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder readTimeout(long timeout, TimeUnit unit) {
            this.readTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder writeTimeout(long timeout, TimeUnit unit) {
            this.writeTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder pingInterval(long interval, TimeUnit unit) {
            this.pingInterval = Util.checkDuration("interval", interval, unit);
            return this;
        }

        public Builder retryOnConnectionFailure(boolean retryOnConnectionFailure) {
            this.retryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }

        public Builder hostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder languageType(int languageType) {
            this.languageType = languageType;
            return this;
        }

        public Builder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public Builder accent(String accent) {
            this.accent = accent;
            return this;
        }

        public Builder eos(int eos) {
            this.eos = eos;
            return this;
        }

        public Builder vto(int vto) {
            this.vto = vto;
            return this;
        }

        public Builder nunum(int nunum) {
            this.nunum = nunum;
            return this;
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder to(String to) {
            this.to = to;
            return this;
        }

        public Builder vcn(String vcn) {
            this.vcn = vcn;
            return this;
        }

        public Builder encoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public Builder sampleRate(int sampleRate) {
            this.sampleRate = sampleRate;
            return this;
        }

        public Builder channels(int channels) {
            this.channels = channels;
            return this;
        }

        public Builder bitDepth(int bitDepth) {
            this.bitDepth = bitDepth;
            return this;
        }

        public Builder executor(ExecutorService executor) {
            this.executor = executor;
            return this;
        }
    }
}
