package cn.xfyun.api;

import cn.xfyun.base.websocket.AbstractClient;
import cn.xfyun.config.VoiceCloneLangEnum;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.voiceclone.request.VoiceCloneRequest;
import cn.xfyun.util.StringUtils;
import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * 一句话复刻合成Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/spark/reproduction.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class VoiceCloneClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(VoiceCloneClient.class);

    /**
     * 文本编码 utf8, gb2312, gbk
     * 默认 utf8
     */
    private final String textEncoding;

    /**
     * 文本压缩格式 raw, gzip
     * 默认 raw
     */
    private final String textCompress;

    /**
     * 文本格式 plain, json, xml
     * 默认 plain
     */
    private final String textFormat;

    /**
     * 训练得到的音色id
     */
    private final String resId;

    /**
     * 合成的语种
     * 注意：需要和训练时指定的语种保持一致
     * 中：0 英：1 日：2 韩：3 俄：4
     * 默认 0
     */
    private final int languageId;

    /**
     * 语速[0,100]
     * 语速：0对应默认语速的1/2，100对应默认语速的2倍
     */
    private final int speed;

    /**
     * 音量[0,100]
     * 音量：0对应默认音量的1/2，100对应默认音量的2倍
     */
    private final int volume;

    /**
     * 语调[0,100]
     * 语调：0对应默认语速的1/2，100对应默认语速的2倍
     */
    private final int pitch;

    /**
     * 背景音   默认0
     */
    private final int bgs;

    /**
     * 英文发音方式，
     * 0:自动判断处理，如果不确定将按照英文词语拼写处理（缺省）,
     * 1:所有英文按字母发音
     * 2:自动判断处理，如果不确定将按照字母朗读
     */
    private final int reg;

    /**
     * 合成音频数字发音方式，
     * 0:自动判断（缺省）,
     * 1:完全数值,
     * 2:完全字符串,
     * 3:字符串优先
     */
    private final int rdn;

    /**
     * 是否返回拼音标注，
     * 0:不返回拼音,
     * 1:返回拼音（纯文本格式，utf8编码）,
     * 3:支持文本中的标点符号输出（纯文本格式，utf8编码）
     * 默认 0
     */
    private final int rhy;

    /**
     * 音频编码
     * lame, speex, opus, opus-wb, speex-wb
     * 默认 speex-wb
     */
    private final String encoding;

    /**
     * 音频采样率
     * 16000, 8000, 24000（缺省）
     */
    private final int sampleRate;

    /**
     * 发言人名称
     * 固定值x5_clone
     */
    private final String vcn;

    /**
     * 数据状态
     * 固定值:2一次性传完
     */
    private final int status;

    public VoiceCloneClient(Builder builder) {
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

        this.textEncoding = builder.textEncoding;
        this.textCompress = builder.textCompress;
        this.textFormat = builder.textFormat;
        this.resId = builder.resId;
        this.languageId = builder.languageId;
        this.speed = builder.speed;
        this.volume = builder.volume;
        this.pitch = builder.pitch;
        this.bgs = builder.bgs;
        this.reg = builder.reg;
        this.rdn = builder.rdn;
        this.rhy = builder.rhy;
        this.encoding = builder.encoding;
        this.sampleRate = builder.sampleRate;
        this.vcn = builder.vcn;
        this.status = builder.status;

        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.callTimeout = builder.callTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
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

    public String getVcn() {
        return vcn;
    }

    public int getStatus() {
        return status;
    }

    public String getResId() {
        return resId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public int getSpeed() {
        return speed;
    }

    public int getVolume() {
        return volume;
    }

    public int getPitch() {
        return pitch;
    }

    public int getBgs() {
        return bgs;
    }

    public int getReg() {
        return reg;
    }

    public int getRdn() {
        return rdn;
    }

    public int getRhy() {
        return rhy;
    }

    public String getEncoding() {
        return encoding;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    /**
     * 超拟人语音合成处理方法
     *
     * @param text 合成文本
     *             文本数据[1,8000]
     *             文本内容，base64编码后不超过8000字节，约2000个字符
     * @param webSocketListener ws监听类 AbstractVoiceCloneWebSocketListener
     */
    public void send(String text, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        // 参数校验
        paramCheck(text);

        // 初始化链接client
        WebSocket socket = newWebSocket(webSocketListener);

        try {
            // 构建请求参数
            String param = buildParam(text);
            logger.debug("一句话复刻合成入参：{}", param);

            // 发送合成文本
            socket.send(param);
        } catch (Exception e) {
            logger.error("一句话复刻合成请求出错：{}", e.getMessage(), e);
        }
    }

    /**
     * 参数校验
     */
    private void paramCheck(String text) {
        if (StringUtils.isNullOrEmpty(text)) {
            throw new BusinessException("合成文本不能为空");
        } else {
            byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
            if (bytes.length > 8000) {
                throw new BusinessException("合成文本长度不能超过8000字节");
            }
        }
    }

    /**
     * 构建参数
     */
    private String buildParam(String text) {
        VoiceCloneRequest request = new VoiceCloneRequest();
        // 请求头
        VoiceCloneRequest.Header header = new VoiceCloneRequest.Header();
        header.setAppId(appId);
        header.setStatus(status);
        header.setResId(resId);
        request.setHeader(header);
        // 请求参数
        VoiceCloneRequest.Parameter parameter = new VoiceCloneRequest.Parameter(this);
        parameter.getTts().setVcn(vcn);
        parameter.getTts().setPybuffer(1);
        request.setParameter(parameter);
        // 请求体
        VoiceCloneRequest.Payload payload = new VoiceCloneRequest.Payload();
        VoiceCloneRequest.Payload.Text payloadText = new VoiceCloneRequest.Payload.Text();
        payloadText.setEncoding(textEncoding);
        payloadText.setCompress(textCompress);
        payloadText.setFormat(textFormat);
        payloadText.setStatus(status);
        payloadText.setSeq(0);
        payloadText.setText(Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8)));
        payload.setText(payloadText);
        request.setPayload(payload);
        return StringUtils.gson.toJson(request);
    }

    public static final class Builder {

        // websocket相关
        boolean retryOnConnectionFailure = true;
        int callTimeout = 0;
        int connectTimeout = 30000;
        int readTimeout = 30000;
        int writeTimeout = 30000;
        int pingInterval = 0;
        private String hostUrl = "http://cn-huabei-1.xf-yun.com/v1/private/voice_clone";
        private String appId;
        private String apiKey;
        private String apiSecret;
        private String textEncoding = "utf8";
        private String textCompress = "raw";
        private String textFormat = "plain";
        private String resId;
        private int languageId = 0;
        private int speed = 50;
        private int volume = 50;
        private int pitch = 50;
        private int bgs = 0;
        private int reg = 0;
        private int rdn = 0;
        private int rhy = 0;
        private String encoding = "speex-wb";
        private int sampleRate = 16000;
        private int status = 2;
        private String vcn = "x5_clone";

        public VoiceCloneClient build() {
            return new VoiceCloneClient(this);
        }

        public Builder signature(String resId, VoiceCloneLangEnum langEnum, String appId, String apiKey, String apiSecret) {
            this.appId = appId;
            this.apiKey = apiKey;
            this.apiSecret = apiSecret;
            this.resId = resId;
            this.languageId = langEnum.code();
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

        public Builder speed(int speed) {
            this.speed = speed;
            return this;
        }

        public Builder volume(int volume) {
            this.volume = volume;
            return this;
        }

        public Builder pitch(int pitch) {
            this.pitch = pitch;
            return this;
        }

        public Builder bgs(int bgs) {
            this.bgs = bgs;
            return this;
        }

        public Builder reg(int reg) {
            this.reg = reg;
            return this;
        }

        public Builder rdn(int rdn) {
            this.rdn = rdn;
            return this;
        }

        public Builder rhy(int rhy) {
            this.rhy = rhy;
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

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder vcn(String vcn) {
            this.vcn = vcn;
            return this;
        }
    }
}
