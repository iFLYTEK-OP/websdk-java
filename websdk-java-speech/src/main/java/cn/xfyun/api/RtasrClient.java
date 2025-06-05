package cn.xfyun.api;

import cn.xfyun.base.websocket.WebSocketClient;
import cn.xfyun.model.request.ise.IseBusiness;
import cn.xfyun.model.request.ise.IseRequest;
import cn.xfyun.model.request.ise.IseRequestData;
import cn.xfyun.model.sign.AbstractSignature;
import cn.xfyun.model.sign.RtasrSignature;
import cn.xfyun.service.rta.AbstractRtasrWebSocketListener;
import cn.xfyun.service.rta.RtasrSendTask;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import okhttp3.*;
import okhttp3.internal.Util;
import okio.ByteString;

import java.io.*;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.Base64;
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

    /**
     * 实时语音转写语种，不传默认为中文
     * 语种类型：中文、中英混合识别：cn；
     * 英文：en；
     * 小语种及方言可到控制台-实时语音转写-方言/语种处添加，添加后会显示该方言/语种参数值。传参示例如："lang=en"
     * 若未授权无法使用会报错10110
     * */
    private String lang;

    /**
     * normal表示普通翻译，默认值normal；
     * 注意：需控制台开通翻译功能
     * */
    private String transType;

    /**
     * 策略1，转写的vad结果直接送去翻译；
     * 策略2，返回中间过程中的结果；
     * 策略3，按照结束性标点拆分转写结果请求翻译；
     * 建议使用策略2
     * 注意：需控制台开通翻译功能
     * */
    private Integer transStrategy;

    /**
     * 目标翻译语种：控制把源语言转换成什么类型的语言；
     * 请注意类似英文转成法语必须以中文为过渡语言，即英-中-法，暂不支持不含中文语种之间的直接转换；
     * 中文：cn
     * 英文：en
     * 日语：ja
     * 韩语：ko
     * 俄语：ru
     * 法语：fr
     * 西班牙语：es
     * 越南语：vi
     * 广东话：cn_cantonese
     * 如果使用中文实时翻译为英文传参示例如下：
     * "&lang=cn&transType=normal&transStrategy=2&targetLang=en"
     * 注意：需控制台开通翻译功能
     * */
    private String targetLang;

    /**
     * 远近场切换，不传此参数或传1代表远场，传2代表近场
     * */
    private Integer vadMdn;

    /**
     * 是否开角色分离，默认不开启，传2开启
     * (效果持续优化中)
     * */
    private Integer roleType;

    /**
     * 语言识别模式，默认为模式1中英文模式：
     * 1：自动中英文模式
     * 2：中文模式，可能包含少量英文
     * 4：纯中文模式，不包含英文
     * */
    private Integer engLangType;

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
        this.lang = builder.lang;
        this.transType = builder.transType;
        this.transStrategy = builder.transStrategy;
        this.targetLang = builder.targetLang;
        this.vadMdn = builder.vadMdn;
        this.roleType = builder.roleType;
        this.engLangType = builder.engLangType;
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
        String url = RTASR_URL + signature.getSigna() + getLinkParam();
        this.request = new Request.Builder().url(url).build();
        // 创建websocket连接
        this.webSocket = okHttpClient.newWebSocket(request, webSocketListener);
    }

    /**
     * 语音评测服务端启动
     */
    public void start(WebSocketListener webSocketListener) throws SignatureException {
        // 创建webSocket连接
        createWebSocketConnect(webSocketListener);
    }

    /**
     * 语音评测发送数据
     */
    public void sendMessage(byte[] bytes) {
        if (null == bytes) {
            webSocket.send("");
        } else {
            webSocket.send(ByteString.of(bytes));
        }
    }

    /**
     *   发送pcm流
     *
     * @param inputStream
     * @throws InterruptedException
     */
    public void send(InputStream inputStream, AbstractRtasrWebSocketListener webSocketListener) throws SignatureException {
        if (inputStream == null) {
            return;
        }
        // 创建webSocket连接
        createWebSocketConnect(webSocketListener);

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

    /**
     * 获取拼接参数
     */
    private String getLinkParam() {
        StringBuilder param = new StringBuilder();
        if (!StringUtils.isNullOrEmpty(punc)) {
            param.append("&punc=").append(punc);
        }
        if (!StringUtils.isNullOrEmpty(pd)) {
            param.append("&pd=").append(pd);
        }
        if (!StringUtils.isNullOrEmpty(lang)) {
            param.append("&lang=").append(lang);
        }
        if (!StringUtils.isNullOrEmpty(transType)) {
            param.append("&transType=").append(transType);
        }
        if (null != transStrategy) {
            param.append("&transStrategy=").append(transStrategy);
        }
        if (!StringUtils.isNullOrEmpty(targetLang)) {
            param.append("&targetLang=").append(targetLang);
        }
        if (null != vadMdn) {
            param.append("&vadMdn=").append(vadMdn);
        }
        if (null != roleType) {
            param.append("&roleType=").append(roleType);
        }
        if (null != engLangType) {
            param.append("&engLangType=").append(engLangType);
        }
        return param.toString();
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

    public String getLang() {
        return lang;
    }

    public String getTransType() {
        return transType;
    }

    public int getTransStrategy() {
        return transStrategy;
    }

    public String getTargetLang() {
        return targetLang;
    }

    public Integer getVadMdn() {
        return vadMdn;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public Integer getEngLangType() {
        return engLangType;
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

        private String lang;

        private String transType;

        private Integer transStrategy;

        private String targetLang;

        private Integer vadMdn;

        private Integer roleType;

        private Integer engLangType;

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

        public Builder lang(String lang) {
            this.lang = lang;
            return this;
        }

        public Builder transType(String transType) {
            this.transType = transType;
            return this;
        }

        public Builder transStrategy(int transStrategy) {
            this.transStrategy = transStrategy;
            return this;
        }

        public Builder targetLang(String targetLang) {
            this.targetLang = targetLang;
            return this;
        }

        public Builder vadMdn(int vadMdn) {
            this.vadMdn = vadMdn;
            return this;
        }

        public Builder roleType(int roleType) {
            this.roleType = roleType;
            return this;
        }

        public Builder engLangType(int engLangType) {
            this.engLangType = engLangType;
            return this;
        }
    }
}
