package cn.xfyun.api;

import cn.xfyun.base.websocket.AbstractClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.oral.request.OralRequest;
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
 * 超拟人合成 Client
 *
 * @author <zyding6@ifytek.com>
 **/
public class OralClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(OralClient.class);

    /**
     * 传输数据状态
     * 2: 一次性传完
     */
    private static final int STATUS = 2;

    /**
     * 文本编码
     * 仅支持 utf8
     */
    private static final String TEXT_ENCODING = "utf8";

    /**
     * 文本压缩格式
     */
    private static final String TEXT_COMPRESS = "raw";

    /**
     * 口语化等级
     * 高:high, 中:mid, 低:low
     * 默认 mid
     */
    private final String oralLevel;

    /**
     * 是否通过大模型进行口语化
     * 开启:1, 关闭:0
     * 默认 1
     */
    private final int sparkAssist;

    /**
     * 关闭服务端拆句
     * 不关闭：0，关闭：1
     * 默认 0
     */
    private final int stopSplit;

    /**
     * 是否保留原书面语的样子
     * remain=1, 保留书面语，即移除所有新增填充语、重复语、语气词和话语符号，保留原书面语的样子。
     * remain=0, 不保留书面语，即包含所有新增填充语、重复语、语气词和话语符号，不保留原书面语的样子。
     * 默认 0
     */
    private final int remain;

    /**
     * 超拟人发音人
     * 可去开放平台查询可用超拟人信息
     */
    private final String vcn;

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
     * 1:返回拼音（纯文本格式，utf8编码）
     */
    private final int rhy;

    /**
     * 音频编码
     * raw,lame, speex, opus, opus-wb, opus-swb, speex-wb
     * lame: mp3格式音频
     * raw: pcm格式音频
     */
    private final String encoding;

    /**
     * 音频采样率
     * 16000, 8000, 24000（缺省）
     */
    private final int sampleRate;

    /**
     * 声道数   1（缺省）, 2
     */
    private final int channels;

    /**
     * 位深    16（缺省）, 8
     */
    private final int bitDepth;

    /**
     * 帧大小[0,1024]   默认0
     */
    private final int frameSize;

    /**
     * 返回结果格式
     * plain, json
     * 默认plain
     */
    private final String textFormat;

    public OralClient(Builder builder) {
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

        this.oralLevel = builder.oralLevel;
        this.sparkAssist = builder.sparkAssist;
        this.stopSplit = builder.stopSplit;
        this.remain = builder.remain;
        this.vcn = builder.vcn;
        this.speed = builder.speed;
        this.volume = builder.volume;
        this.pitch = builder.pitch;
        this.bgs = builder.bgs;
        this.reg = builder.reg;
        this.rdn = builder.rdn;
        this.rhy = builder.rhy;
        this.encoding = builder.encoding;
        this.sampleRate = builder.sampleRate;
        this.channels = builder.channels;
        this.bitDepth = builder.bitDepth;
        this.frameSize = builder.frameSize;
        this.textFormat = builder.textFormat;

        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.callTimeout = builder.callTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
    }

    public String getOralLevel() {
        return oralLevel;
    }

    public int getSparkAssist() {
        return sparkAssist;
    }

    public int getStopSplit() {
        return stopSplit;
    }

    public int getRemain() {
        return remain;
    }

    public String getVcn() {
        return vcn;
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

    public int getChannels() {
        return channels;
    }

    public int getBitDepth() {
        return bitDepth;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public String getTextFormat() {
        return textFormat;
    }

    /**
     * 超拟人语音合成处理方法(一次性合成)
     *
     * @param text              合成文本
     *                          文本数据[1,8000]
     *                          文本内容，base64编码后不超过8000字节，约2000个字符
     * @param webSocketListener ws监听类 (AbstractOralWebSocketListener)
     */
    public void send(String text, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        // 参数校验
        paramCheck(text);

        // 初始化websocket链接
        WebSocket socket = newWebSocket(webSocketListener);

        // 构建请求参数
        String jsonStr = buildParam(text);

        try {
            logger.debug("超拟人合成请求参数：{}", jsonStr);
            // 发送合成文本
            socket.send(jsonStr);
        } catch (Exception e) {
            logger.error("超拟人合成请求出错：{}", e.getMessage(), e);
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
        OralRequest request = new OralRequest();
        // 请求头
        OralRequest.Header header = new OralRequest.Header();
        header.setAppId(appId);
        header.setStatus(STATUS);
        request.setHeader(header);
        // 请求参数
        OralRequest.Parameter parameter = new OralRequest.Parameter(this);
        request.setParameter(parameter);
        // 请求体
        OralRequest.Payload payload = new OralRequest.Payload();
        OralRequest.Payload.Text payloadText = new OralRequest.Payload.Text();
        payloadText.setEncoding(TEXT_ENCODING);
        payloadText.setCompress(TEXT_COMPRESS);
        payloadText.setFormat(textFormat);
        payloadText.setStatus(STATUS);
        payloadText.setSeq(0);
        payloadText.setText(Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8)));
        payload.setText(payloadText);
        request.setPayload(payload);
        return StringUtils.gson.toJson(request);
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
        private String hostUrl = "https://cbm01.cn-huabei-1.xf-yun.com/v1/private/mcd9m97e6";
        private String appId;
        private String apiKey;
        private String apiSecret;
        private String oralLevel = "mid";
        private int sparkAssist = 1;
        private int stopSplit = 0;
        private int remain = 0;
        private String vcn = "x4_lingxiaoxuan_oral";
        private int speed = 50;
        private int volume = 50;
        private int pitch = 50;
        private int bgs = 0;
        private int reg = 0;
        private int rdn = 0;
        private int rhy = 0;
        private String encoding = "lame";
        private int sampleRate = 24000;
        private int channels = 1;
        private int bitDepth = 16;
        private int frameSize = 0;
        private String textFormat = "plain";

        public OralClient build() {
            return new OralClient(this);
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

        public Builder oralLevel(String oralLevel) {
            this.oralLevel = oralLevel;
            return this;
        }

        public Builder sparkAssist(int sparkAssist) {
            this.sparkAssist = sparkAssist;
            return this;
        }

        public Builder stopSplit(int stopSplit) {
            this.stopSplit = stopSplit;
            return this;
        }

        public Builder remain(int remain) {
            this.remain = remain;
            return this;
        }

        public Builder vcn(String vcn) {
            this.vcn = vcn;
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

        public Builder channels(int channels) {
            this.channels = channels;
            return this;
        }

        public Builder bitDepth(int bitDepth) {
            this.bitDepth = bitDepth;
            return this;
        }

        public Builder frameSize(int frameSize) {
            this.frameSize = frameSize;
            return this;
        }

        public Builder textFormat(String textFormat) {
            this.textFormat = textFormat;
            return this;
        }
    }
}
