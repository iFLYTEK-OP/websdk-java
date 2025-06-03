package cn.xfyun.api;

import cn.xfyun.base.websocket.AbstractClient;
import cn.xfyun.config.SparkIatModelEnum;
import cn.xfyun.service.sparkiat.SparkIatSendTask;
import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 大模型语音听写 Client
 * 中英大模型文档地址: <a href="https://www.xfyun.cn/doc/spark/spark_zh_iat.html">...</a>
 * 方言大模型文档地址: <a href="https://www.xfyun.cn/doc/spark/spark_slm_iat.html">...</a>
 * 多语种大模型文档地址: <a href="https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class SparkIatClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(SparkIatClient.class);

    /**
     * 大模型中文语音识别能力，将中文短音频(≤60秒)精准识别成文字，实时返回文字结果，真实还原语音内容
     */
    private static final String CN_LANGUAGE_API = "https://iat.xf-yun.com/v1";

    /**
     * 方言大模型，支持普通话，简单英语和202种方言全免切，无需显示指定语种
     * 或
     * 模型多语种语音识别能力，将多语种短音频(≤60秒)精准识别成文字，实时返回文字结果，真实还原语音内容
     */
    private static final String MULTI_LANGUAGE_API = "https://iat.cn-huabei-1.xf-yun.com/v1";

    /**
     * 线程池管理
     * 默认单例线程池
     */
    private final ExecutorService executor;

    /**
     * 语种类型
     * SparkIatModelEnum
     */
    private final Integer langType;

    /**
     * 语种
     * zh_cn：中文或者方言
     * mul_cn：多语种
     */
    private final String language;
    /**
     * 应用领域
     * 固定slm
     */
    private final String domain;
    /**
     * 方言，当language为zh_cn中文时，支持普通话mandarin和方言mulacc选择。
     * mandarin：中文普通话、其他语种
     * mulacc：中文方言
     * 其他方言：可到控制台-语音听写（流式版）-方言/语种处添加试用或购买，添加后会显示该方言参数值；方言若未授权无法使用会报错11200。
     */
    private final String accent;
    /**
     * 用于设置端点检测的静默时间，单位是毫秒[600,60000]。
     * 即静默多长时间后引擎认为音频结束。
     * 方言大模型 默认1800
     * 不设置该参数默认为未开启VAD
     */
    private final int eos;
    /**
     * 返回子句结果对应的起始和结束的端点帧偏移值。端点帧偏移值表示从音频开头起已过去的帧长度。
     * 0：关闭（默认值）
     * 1：开启
     * 开启后返回的结果中会增加data.result.vad字段，详见下方返回结果。
     * 注：若开通并使用了动态修正功能，则该功能无法使用。
     */
    private final int vinfo;
    /**
     * （仅中文普通话支持）动态修正
     * wpgs：开启流式结果返回功能
     * 注：该扩展功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处免费开通；若未授权状态下设置该参数并不会报错，但不会生效。
     */
    private final String dwa;
    /**
     * 取值范围[1,5]，通过设置此参数，获取在发音相似时的句子多侯选结果。设置多候选会影响性能，响应时间延迟200ms左右。
     * 注：该扩展功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处免费开通；若未授权状态下设置该参数并不会报错，但不会生效。
     */
    private final Integer nbest;
    /**
     * 取值范围[1,5]，通过设置此参数，获取在发音相似时的词语多侯选结果。设置多候选会影响性能，响应时间延迟200ms左右。
     * 注：该扩展功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处免费开通；若未授权状态下设置该参数并不会报错，但不会生效。
     */
    private final Integer wbest;
    /**
     * （仅中文支持）标点预测：在语音识别结果中增加标点符号
     * 1：开启（默认值）
     * 0：关闭
     */
    private final Integer ptt;
    /**
     * （仅中文支持）顺滑功能：将语音识别结果中的顺滑词（语气词、叠词）进行标记，业务侧通过标记过滤语气词最终展现识别结果
     * 1：开启
     * 0：关闭（默认值）
     */
    private final Integer smth;
    /**
     * （仅中文支持）数字规整：将语音识别结果中的原始文字串转为相应的阿拉伯数字或者符号
     * 1：开启（默认值）
     * 0：关闭
     */
    private final Integer nunum;
    /**
     * 0:json格式输出，不带属性,
     * 1:文本格式输出，不带属性,
     * 2:json格式输出，带文字属性"wp":"n"和标点符号属性"wp":"p"
     */
    private final Integer opt;
    /**
     * 会话热词，支持utf-8和gb2312；
     * 取值样例：“dhw=db2312;你好|大家”（对应gb2312编码）；
     * “dhw=utf-8;你好|大家”（对应utf-8编码）
     * [0,1024]
     */
    private final String dhw;
    /**
     * （仅中文支持）字体
     * zh-cn :简体中文（默认值）
     * zh-hk :繁体香港
     * zh-mo :新加坡
     * zh-tw :台湾
     * 注：该繁体功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处免费开通；若未授权状态下设置为繁体并不会报错，但不会生效。
     */
    private final String rlang;
    /**
     * （仅中文支持）是否进行中英文筛选
     * 1:不进行筛选, 2:只出中文, 3:只出英文
     */
    private final Integer ltc;
    /**
     * 音频数据格式
     * 中文大模型   raw或lame
     * 方言大模型   lame, speex, opus, opus-wb, speex-wb
     * 多语种大模型  raw或lame
     * raw：原生音频（支持单声道的pcm）
     * speex：speex压缩后的音频（8k）
     * speex-wb：speex压缩后的音频（16k）
     * 请注意压缩前也必须是采样率16k或8k单声道的pcm。
     * lame：mp3格式（仅中文普通话和英文支持，方言及小语种暂不支持）
     */
    private final String encoding;
    /**
     * 音频的采样率支持16k和8k
     * 16k音频：audio/L16;rate=16000
     * 8k音频：audio/L16;rate=8000
     */
    private final Integer sampleRate;
    /**
     * 声道数1,2
     */
    private final Integer channels;
    /**
     * 位深 16,8
     */
    private final Integer bitDepth;
    /**
     * 请注意不同音频格式一帧大小的字节数不同，我们建议：
     * <p>
     * 1.未压缩的PCM格式，每次发送音频间隔40ms，每次发送音频字节数1280B；
     * 2.讯飞定制speex格式，每次发送音频间隔40ms，假如16k的压缩等级为7，则每次发送61B的整数倍；
     * 3.标准开源speex格式，每次发送音频间隔40ms，假如16k的压缩等级为7，则每次发送60B的整数倍；
     */
    private final Integer frameSize;
    /**
     * 文本编码
     * utf8 （默认值）
     * gb2312
     */
    private final String textEncoding;
    /**
     * 文本编码
     * raw（默认值）
     * gzip
     */
    private final String textCompress;
    /**
     * 文本编码
     * plain
     * json（默认值）
     * xml
     */
    private final String textFormat;
    /**
     * 语种参数：
     * 支持两种模式，
     * 指定语种，如识别英文ln=en，可参考语种列表；
     * 免切模式，不需要指定语种参数或传参ln=none
     */
    private final String ln;

    public SparkIatClient(Builder builder) {
        this.okHttpClient = new OkHttpClient
                .Builder()
                .callTimeout(builder.callTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(builder.callTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(builder.readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(builder.writeTimeout, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(builder.retryOnConnectionFailure)
                .build();
        this.originHostUrl = builder.hostUrl;
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.apiSecret = builder.apiSecret;
        this.executor = (null == builder.executor) ? Executors.newSingleThreadExecutor() : builder.executor;

        this.language = builder.language;
        this.domain = builder.domain;
        this.accent = builder.accent;
        this.eos = builder.eos;
        this.encoding = builder.encoding;
        this.dwa = builder.dwa;
        this.smth = builder.smth;
        this.ptt = builder.ptt;
        this.rlang = builder.rlang;
        this.vinfo = builder.vinfo;
        this.nunum = builder.nunum;
        this.dhw = builder.dhw;
        this.opt = builder.opt;
        this.ltc = builder.ltc;
        this.sampleRate = builder.sampleRate;
        this.channels = builder.channels;
        this.bitDepth = builder.bitDepth;
        this.nbest = builder.nbest;
        this.wbest = builder.wbest;
        this.frameSize = builder.frameSize;
        this.textEncoding = builder.textEncoding;
        this.textCompress = builder.textCompress;
        this.textFormat = builder.textFormat;
        this.langType = builder.langType;
        this.ln = builder.ln;

        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.callTimeout = builder.callTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
    }

    public String getLanguage() {
        return language;
    }

    public String getDomain() {
        return domain;
    }

    public String getAccent() {
        return accent;
    }

    public int getEos() {
        return eos;
    }

    public int getVinfo() {
        return vinfo;
    }

    public String getDwa() {
        return dwa;
    }

    public Integer getNbest() {
        return nbest;
    }

    public Integer getWbest() {
        return wbest;
    }

    public Integer getPtt() {
        return ptt;
    }

    public Integer getSmth() {
        return smth;
    }

    public Integer getNunum() {
        return nunum;
    }

    public Integer getOpt() {
        return opt;
    }

    public String getDhw() {
        return dhw;
    }

    public String getRlang() {
        return rlang;
    }

    public Integer getLtc() {
        return ltc;
    }

    public String getEncoding() {
        return encoding;
    }

    public Integer getSampleRate() {
        return sampleRate;
    }

    public Integer getChannels() {
        return channels;
    }

    public Integer getBitDepth() {
        return bitDepth;
    }

    public Integer getFrameSize() {
        return frameSize;
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

    public Integer getLangType() {
        return langType;
    }

    public String getLn() {
        return ln;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    /**
     * 发送文件给语音听写服务端
     *
     * @param file              发送的文件
     * @param webSocketListener ws监听类 (AbstractSparkIatWebSocketListener)
     */
    public void send(File file, WebSocketListener webSocketListener) throws FileNotFoundException, MalformedURLException, SignatureException {
        FileInputStream fileInputStream = new FileInputStream(file);
        send(fileInputStream, webSocketListener);
    }

    /**
     * 发送文件流给服务端
     *
     * @param inputStream       需要发送的流
     * @param webSocketListener ws监听类 (AbstractSparkIatWebSocketListener)
     */
    public void send(InputStream inputStream, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        if (inputStream == null) {
            return;
        }

        // 创建webSocket连接
        WebSocket socket = newWebSocket(webSocketListener);
        logger.debug("语音听写请求URL：{}", this.originHostUrl);

        // 大模型语音听写数据发送任务
        SparkIatSendTask sparkIatSendTask = new SparkIatSendTask();
        new SparkIatSendTask.Builder()
                .inputStream(inputStream)
                .webSocket(socket)
                .client(this)
                .build(sparkIatSendTask);

        executor.submit(sparkIatSendTask);
    }

    /**
     * @param bytes             字节数据
     * @param closeable         需要关闭的流，可为空
     * @param webSocketListener ws监听类 (AbstractSparkIatWebSocketListener)
     */
    public void send(byte[] bytes, Closeable closeable, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        if (bytes == null || bytes.length == 0) {
            return;
        }

        // 创建webSocket连接
        WebSocket socket = newWebSocket(webSocketListener);
        logger.debug("语音听写请求URL：{}", this.originHostUrl);

        SparkIatSendTask sparkIatSendTask = new SparkIatSendTask();
        new SparkIatSendTask.Builder()
                .bytes(bytes)
                .webSocket(socket)
                .client(this)
                .closeable(closeable)
                .build(sparkIatSendTask);

        executor.submit(sparkIatSendTask);
    }

    public static class Builder {

        // websocket相关
        private boolean retryOnConnectionFailure = true;
        private int callTimeout = 0;
        private int connectTimeout = 10000;
        private int readTimeout = 30000;
        private int writeTimeout = 30000;
        private int pingInterval = 0;
        private String hostUrl = CN_LANGUAGE_API;
        private String appId;
        private String apiKey;
        private String apiSecret;
        private Integer langType = 1;
        private String language = "zh_cn";
        private final String domain = "slm";
        private String accent = "mandarin";
        private String encoding = "raw";
        private String dwa;
        private Integer smth;
        private Integer eos = 6000;
        private Integer ptt;
        private String rlang;
        private Integer nunum;
        private Integer nbest = 0;
        private Integer wbest = 0;
        private int vinfo = 1;
        private String dhw;
        private Integer opt;
        private Integer ltc;
        private Integer sampleRate = 16000;
        private Integer channels = 1;
        private Integer bitDepth = 16;
        private Integer frameSize = 1280;
        private String textEncoding = "utf8";
        private String textCompress = "raw";
        private String textFormat = "json";
        private String ln = "none";
        private ExecutorService executor;

        public SparkIatClient build() {
            return new SparkIatClient(this);
        }

        /**
         * 1-中文大模型   2-方言大模型    3-多语种大模型
         *
         * @param langType 语言类型
         */
        public Builder signature(String appId, String apiKey, String apiSecret, Integer langType) {
            this.appId = appId;
            this.apiKey = apiKey;
            this.apiSecret = apiSecret;
            if (SparkIatModelEnum.ZH_CN_MULACC.codeEquals(langType)) {
                this.hostUrl = MULTI_LANGUAGE_API;
                this.accent = "mulacc";
                this.langType = langType;
            } else if (SparkIatModelEnum.MUL_CN_MANDARIN.codeEquals(langType)) {
                this.hostUrl = MULTI_LANGUAGE_API;
                this.language = "mul_cn";
                this.langType = langType;
            }
            return this;
        }

        public Builder hostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        public Builder eos(int eos) {
            this.eos = eos;
            return this;
        }

        public Builder vinfo(int vinfo) {
            this.vinfo = vinfo;
            return this;
        }

        public Builder dwa(String dwa) {
            this.dwa = dwa;
            return this;
        }

        public Builder nbest(Integer nbest) {
            this.nbest = nbest;
            return this;
        }

        public Builder wbest(Integer wbest) {
            this.wbest = wbest;
            return this;
        }

        public Builder ptt(Integer ptt) {
            this.ptt = ptt;
            return this;
        }

        public Builder smth(Integer smth) {
            this.smth = smth;
            return this;
        }

        public Builder nunum(Integer nunum) {
            this.nunum = nunum;
            return this;
        }

        public Builder opt(Integer opt) {
            this.opt = opt;
            return this;
        }

        public Builder dhw(String dhw) {
            this.dhw = dhw;
            return this;
        }

        public Builder rlang(String rlang) {
            this.rlang = rlang;
            return this;
        }

        public Builder ltc(Integer ltc) {
            this.ltc = ltc;
            return this;
        }

        public Builder encoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public Builder sampleRate(Integer sampleRate) {
            this.sampleRate = sampleRate;
            return this;
        }

        public Builder channels(Integer channels) {
            this.channels = channels;
            return this;
        }

        public Builder bitDepth(Integer bitDepth) {
            this.bitDepth = bitDepth;
            return this;
        }

        public Builder frameSize(Integer frameSize) {
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

        public Builder ln(String ln) {
            this.ln = ln;
            return this;
        }

        public Builder executor(ExecutorService executor) {
            this.executor = executor;
            return this;
        }
    }
}
