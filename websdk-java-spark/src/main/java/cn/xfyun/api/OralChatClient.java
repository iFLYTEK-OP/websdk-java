package cn.xfyun.api;

import cn.xfyun.base.websocket.AbstractClient;
import cn.xfyun.config.FrameType;
import cn.xfyun.config.StreamMode;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.oralchat.OralChatParam;
import cn.xfyun.model.oralchat.request.OralChatRequest;
import cn.xfyun.util.OkHttpUtils;
import cn.xfyun.util.StringUtils;
import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.Base64;
import java.util.concurrent.*;

/**
 * 超拟人交互 Client
 *
 * @author <zyding6@ifytek.com>
 **/
public class OralChatClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(OralChatClient.class);

    /**
     * WebSocket参数缓存，用于存储WebSocket实例和对应的OralChatParam
     */
    private final ConcurrentHashMap<String, OralChatParam> paramCache = new ConcurrentHashMap<>();

    /**
     * 静音断句阈值
     * 默认80(800ms)
     * 范围40-1000(400-10000ms)
     */
    private final int vgap;

    /**
     * 用于设置端点检测的静默时间，单位是毫秒。
     */
    private final String eos;

    /**
     * wpgs：开启流式结果返回功能
     */
    private final String dwa;

    /**
     * 领域
     */
    private final String domain;

    /**
     * iat/nlp 结果编码
     * 仅支持utf8
     */
    private final String textEncoding;

    /**
     * iat/nlp 压缩类型
     * 仅支持raw
     */
    private final String textCompress;

    /**
     * iat/nlp 结果格式
     * 仅支持json
     */
    private final String textFormat;

    /**
     * 输入编码格式
     * raw/opus
     * 默认raw
     */
    private final String encodingIn;

    /**
     * 输出音频编码
     * raw/lame/opus-wb/opus-swb
     * 默认raw:pcm音频
     * lame:mp3格式音频
     */
    private final String encodingOut;

    /**
     * 音频采样率 (输入)
     * 8000/16000
     * 默认16000Hz
     */
    private final int sampleRateIn;

    /**
     * 音频采样率 (输出)
     * 16000/24000
     * 默认16000Hz
     */
    private final int sampleRateOut;

    /**
     * 声道数   1（缺省）, 2
     */
    private final int channelsIn;

    /**
     * 默认1(单声道)
     */
    private final int channelsOut;

    /**
     * 8/16
     * 默认16bit
     */
    private final int bitDepthIn;

    /**
     * 默认16bit
     */
    private final int bitDepthOut;

    /**
     * 压缩帧大小
     * 默认0
     */
    private final int frameSize;

    public OralChatClient(Builder builder) {
        if (builder.okHttpClient != null) {
            // 使用用户提供的okHttpClient
            this.okHttpClient = builder.okHttpClient;
        } else {
            // 复用全局的okHttpClient
            this.okHttpClient = OkHttpUtils.client.newBuilder()
                    .connectTimeout(builder.connectTimeout, TimeUnit.MILLISECONDS)
                    .readTimeout(builder.readTimeout, TimeUnit.MILLISECONDS)
                    .writeTimeout(builder.writeTimeout, TimeUnit.MILLISECONDS)
                    .callTimeout(builder.callTimeout, TimeUnit.MILLISECONDS)
                    .pingInterval(builder.pingInterval, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(builder.retryOnConnectionFailure)
                    .build();
        }
        this.originHostUrl = builder.hostUrl;
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.apiSecret = builder.apiSecret;

        this.vgap = builder.vgap;
        this.encodingIn = builder.encodingIn;
        this.encodingOut = builder.encodingOut;
        this.sampleRateIn = builder.sampleRateIn;
        this.sampleRateOut = builder.sampleRateOut;
        this.channelsIn = builder.channelsIn;
        this.channelsOut = builder.channelsOut;
        this.bitDepthIn = builder.bitDepthIn;
        this.bitDepthOut = builder.bitDepthOut;
        this.frameSize = builder.frameSize;
        this.textEncoding = builder.textEncoding;
        this.textCompress = builder.textCompress;
        this.textFormat = builder.textFormat;
        this.dwa = builder.dwa;
        this.eos = builder.eos;
        this.domain = builder.domain;

        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.callTimeout = builder.callTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
    }

    public int getVgap() {
        return vgap;
    }

    public String getTextEncoding() {
        return textEncoding;
    }

    public String getTextCompress() {
        return textCompress;
    }

    public String getTextFormat() {
        return textFormat;
    }

    public String getEncodingIn() {
        return encodingIn;
    }

    public String getEncodingOut() {
        return encodingOut;
    }

    public int getSampleRateIn() {
        return sampleRateIn;
    }

    public int getSampleRateOut() {
        return sampleRateOut;
    }

    public int getChannelsIn() {
        return channelsIn;
    }

    public int getChannelsOut() {
        return channelsOut;
    }

    public int getBitDepthIn() {
        return bitDepthIn;
    }

    public int getBitDepthOut() {
        return bitDepthOut;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public String getDwa() {
        return dwa;
    }

    public String getEos() {
        return eos;
    }

    public String getDomain() {
        return domain;
    }

    /**
     * 超拟人交互服务端启动
     *
     * @param param             超拟人交互启动参数
     * @param webSocketListener ws监听器
     * @return 队列
     * @throws MalformedURLException URL地址处理异常
     * @throws SignatureException    签名异常
     */
    public WebSocket start(OralChatParam param, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        // 参数校验
        paramCheck(param);

        // 返回socket
        WebSocket socket = newWebSocket(webSocketListener);

        // 缓存启动参数
        cacheStartParam(param, socket);

        // 返回socket
        return socket;
    }

    /**
     * 超拟人交互服务端停止
     * @param socket websocket信息
     */
    public void stop(WebSocket socket) {
        // 使用 identityHashCode 生成唯一ID
        String uniqueId = String.valueOf(System.identityHashCode(socket));

        // 根据Id删除队列
        OralChatParam param = paramCache.remove(uniqueId);

        // 调用停止方法
        stop(socket, param);
    }

    /**
     * 超拟人交互服务端停止
     * @param socket websocket信息
     */
    private void stop(WebSocket socket, OralChatParam param) {
        try {
            if (null != param) {
                String msg = buildParam(null, 2, param, true);
                socket.send(msg);
            }
        } catch (Exception e) {
            logger.error("超拟人交互服务端停止返回异常", e);
        } finally {
            if (socket != null) {
                socket.close(1000, "");
            }
        }
    }

    /**
     * 超拟人交互服务发送数据
     * 单工模式时 , status重复传 0 时 , 开启新一轮对话
     *
     * @param socket  websocket
     * @param bytes  音频数据
     * @param status 当前音频的数据状态 0:首帧，1:中间帧，2:末帧
     */
    public void sendMsg(WebSocket socket, byte[] bytes, int status) {
        OralChatParam param = getOralChatParam(socket);
        sendMsg(socket, bytes, status, param);
    }

    /**
     * 超拟人交互服务发送数据
     * 单工模式时 , status重复传 0 时 , 开启新一轮对话
     *
     * @param socket  websocket
     * @param bytes  音频数据
     * @param status 当前音频的数据状态 0:首帧，1:中间帧，2:末帧
     */
    private void sendMsg(WebSocket socket, byte[] bytes, int status, OralChatParam param) {
        if (param == null) {
            logger.error("超拟人交互会话参数缺失");
            throw new BusinessException("超拟人交互会话参数缺失");
        }

        // 构建参数
        String body = buildParam(bytes, status, param, false);

        boolean send = socket.send(body);
        if (!send) {
            logger.error("发送消息到队列失败!");
            throw new BusinessException("发送消息到队列失败!");
        }
    }

    /**
     * 获取启动参数
     * @param socket WebSocket
     * @return 启动参数
     */
    private OralChatParam getOralChatParam(WebSocket socket) {
        // 使用 identityHashCode 生成唯一ID
        String queueId = String.valueOf(System.identityHashCode(socket));

        // 根据Id删除队列
        return paramCache.get(queueId);
    }

    /**
     * 缓存启动参数
     * @param param 启动参数
     * @param socket WebSocket
     */
    private void cacheStartParam(OralChatParam param, WebSocket socket) {
        // 使用 identityHashCode 生成唯一ID
        String queueId = String.valueOf(System.identityHashCode(socket));

        // 缓存队列到全局map中
        paramCache.put(queueId, param);
    }

    /**
     * 参数校验
     */
    private void paramCheck(OralChatParam param) {
        if (null == param) {
            throw new BusinessException("参数不能为空");
        }
        param.selfCheck();
    }

    /**
     * 构建参数
     */
    private String buildParam(byte[] bytes, int status, OralChatParam param, boolean isEnd) {
        // 是否新一轮会话
        boolean newChat = FrameType.FIRST_FRAME.codeEqual(status);
        // 是否首帧
        boolean firstFrame = newChat && (-1 == param.getStmid().get());
        // 流模式
        String mode = param.getInteractMode();

        // 构建请求参数
        OralChatRequest request = new OralChatRequest();

        // 设置请求头
        OralChatRequest.Header header = buildHeader(param, isEnd, firstFrame, mode, newChat);
        request.setHeader(header);

        if (firstFrame || (newChat && StreamMode.CONTINUOUS_VAD.modeEquals(mode))) {
            // 设置参数
            OralChatRequest.Parameter parameter = buildParameter(param);
            request.setParameter(parameter);
        }

        // 设置请求体
        int payloadStatus = StreamMode.CONTINUOUS_VAD.modeEquals(mode) ? status : header.getStatus();
        OralChatRequest.Payload payload = buildPayload(bytes, payloadStatus);
        request.setPayload(payload);

        String body = StringUtils.gson.toJson(request);
        logger.debug("超拟人交互请求入参：{}", body);
        return body;
    }

    private OralChatRequest.Payload buildPayload(byte[] bytes, int status) {
        OralChatRequest.Payload payload = new OralChatRequest.Payload(this);
        payload.getAudio().setStatus(status);
        payload.getAudio().setAudio(
                (bytes == null || bytes.length == 0) ? "" : Base64.getEncoder().encodeToString(bytes)
        );
        return payload;
    }

    private OralChatRequest.Parameter buildParameter(OralChatParam param) {
        OralChatRequest.Parameter parameter = new OralChatRequest.Parameter(this);
        parameter.getNlp().setNewSession(param.getNewSession());
        parameter.getNlp().setPersonal(param.getPersonal());
        parameter.getNlp().setPrompt(param.getPrompt());

        parameter.getTts().setPitch(param.getPitch());
        parameter.getTts().setVolume(param.getVolume());
        parameter.getTts().setSpeed(param.getSpeed());
        parameter.getTts().setResGender(param.getResGender());
        parameter.getTts().setResId(param.getResId());
        parameter.getTts().setVcn(param.getVcn());

        parameter.setAvatar(param.getAvatar());
        return parameter;
    }

    private OralChatRequest.Header buildHeader(OralChatParam param, boolean isEnd, boolean firstFrame, String mode, boolean newChat) {
        OralChatRequest.Header header = new OralChatRequest.Header();
        header.setAppId(appId);
        header.setScene(param.getScene());
        header.setUid(param.getUid());
        header.setStmid(String.valueOf(FrameType.FIRST_FRAME.getValue()));
        header.setStatus((isEnd ?
                FrameType.LAST_FRAME : firstFrame ?
                FrameType.FIRST_FRAME : FrameType.MIDDLE_FRAME).getValue());

        boolean isContinuousVad = StreamMode.CONTINUOUS_VAD.modeEquals(mode);
        if ((newChat && isContinuousVad) || firstFrame) {
            // 第一帧或 CONTINUOUS_VAD 新会话，设置额外参数
            int next = param.getStmid().incrementAndGet();
            header.setInteractMode(param.getInteractMode());
            header.setOsSys(param.getOsSys());
            header.setPersParam(param.getPersParam());
            header.setStmid(String.valueOf(next));
        } else if (isContinuousVad){
            // CONTINUOUS_VAD 且不是新会话
            header.setStmid(param.getStmid().toString());
        }
        return header;
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
        private String hostUrl = "https://sparkos.xfyun.cn/v1/openapi/chat";
        private String appId;
        private String apiKey;
        private String apiSecret;
        private int vgap = 80;
        private String encodingIn = "raw";
        private String encodingOut = "raw";
        private int sampleRateIn = 16000;
        private int sampleRateOut = 16000;
        private int channelsIn = 1;
        private int channelsOut = 1;
        private int bitDepthIn = 16;
        private int bitDepthOut = 16;
        private String textEncoding = "utf8";
        private String textCompress = "raw";
        private String textFormat = "json";
        private String dwa;
        private String eos;
        private String domain;
        private int frameSize = 0;
        private OkHttpClient okHttpClient;

        public OralChatClient build() {
            return new OralChatClient(this);
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

        public Builder vgap(int vgap) {
            this.vgap = vgap;
            return this;
        }

        public Builder encodingIn(String encodingIn) {
            this.encodingIn = encodingIn;
            return this;
        }

        public Builder encodingOut(String encodingOut) {
            this.encodingOut = encodingOut;
            return this;
        }

        public Builder sampleRateIn(int sampleRateIn) {
            this.sampleRateIn = sampleRateIn;
            return this;
        }

        public Builder sampleRateOut(int sampleRateOut) {
            this.sampleRateOut = sampleRateOut;
            return this;
        }

        public Builder channelsIn(int channelsIn) {
            this.channelsIn = channelsIn;
            return this;
        }

        public Builder channelsOut(int channelsOut) {
            this.channelsOut = channelsOut;
            return this;
        }

        public Builder bitDepthIn(int bitDepthIn) {
            this.bitDepthIn = bitDepthIn;
            return this;
        }

        public Builder bitDepthOut(int bitDepthOut) {
            this.bitDepthOut = bitDepthOut;
            return this;
        }

        public Builder frameSize(int frameSize) {
            this.frameSize = frameSize;
            return this;
        }

        public Builder textEncoding(String textEncoding) {
            this.textEncoding = textEncoding;
            return this;
        }

        public Builder textCompress(String textCompress) {
            this.textCompress = textCompress;
            return this;
        }

        public Builder textFormat(String textFormat) {
            this.textFormat = textFormat;
            return this;
        }

        public Builder dwa(String dwa) {
            this.dwa = dwa;
            return this;
        }

        public Builder eos(String eos) {
            this.eos = eos;
            return this;
        }

        public Builder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public Builder okHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }
    }
}
