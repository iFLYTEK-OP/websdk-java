package cn.xfyun.api;

import cn.xfyun.base.webscoket.WebSocketBuilder;
import cn.xfyun.base.webscoket.WebSocketClient;
import cn.xfyun.service.iat.IatSendTask;
import okhttp3.WebSocketListener;

import java.io.*;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author <ydwang16@iflytek.com>
 * @description 语音听写
 * @date 2021/3/24
 */
public class IatClient extends WebSocketClient {

    /**
     * 小语种
     */
    private static final String SMALL_LANGUAGE = "http://iat-niche-api.xfyun.cn/v2/iat";

    /**
     * 中英文
     */
    private static final String CH_EN_LANGUAGE = "http://iat-api.xfyun.cn/v2/iat";
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    /**
     * 语种
     * zh_cn：中文（支持简单的英文识别）
     * en_us：英文
     * 其他小语种：可到控制台-语音听写（流式版）-方言/语种处添加试用或购买，添加后会显示该小语种参数值，若未授权无法使用会报错11200。
     */
    private String language;
    /**
     * 应用领域
     * iat：日常用语
     * medical：医疗
     * gov-seat-assistant：政务坐席助手
     * seat-assistant：金融坐席助手
     * gov-ansys：政务语音分析
     * gov-nav：政务语音导航
     * fin-nav：金融语音导航
     * fin-ansys：金融语音分析
     * 注：除日常用语领域外其他领域若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处添加试用或购买；若未授权无法使用会报错11200。
     * 坐席助手、语音导航、语音分析相关垂直领域仅适用于8k采样率的音频数据，另外三者的区别详见下方。
     */
    private String domain;
    /**
     * 方言，当前仅在language为中文时，支持方言选择。
     * mandarin：中文普通话、其他语种
     * 其他方言：可到控制台-语音听写（流式版）-方言/语种处添加试用或购买，添加后会显示该方言参数值；方言若未授权无法使用会报错11200。
     */
    private String accent;
    /**
     * 音频的采样率支持16k和8k
     * 16k音频：audio/L16;rate=16000
     * 8k音频：audio/L16;rate=8000
     */
    private String format;
    /**
     * 音频数据格式
     * raw：原生音频（支持单声道的pcm）
     * speex：speex压缩后的音频（8k）
     * speex-wb：speex压缩后的音频（16k）
     * 请注意压缩前也必须是采样率16k或8k单声道的pcm。
     * lame：mp3格式（仅中文普通话和英文支持，方言及小语种暂不支持）
     */
    private String encoding;
    /**
     * 用于设置端点检测的静默时间，单位是毫秒。
     * 即静默多长时间后引擎认为音频结束。
     * 默认2000（小语种除外，小语种不设置该参数默认为未开启VAD）。
     */
    private int vadEos;
    /**
     * （仅中文普通话支持）动态修正
     * wpgs：开启流式结果返回功能
     * 注：该扩展功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处免费开通；若未授权状态下设置该参数并不会报错，但不会生效。
     */
    private String dwa;
    /**
     * （仅中文支持）领域个性化参数
     * game：游戏
     * health：健康
     * shopping：购物
     * trip：旅行
     * 注：该扩展功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处添加试用或购买；若未授权状态下设置该参数并不会报错，但不会生效。
     */
    private String pd;
    /**
     * （仅中文支持）是否开启标点符号添加
     * 1：开启（默认值）
     * 0：关闭
     */
    private int ptt;
    /**
     * （仅中文支持）字体
     * zh-cn :简体中文（默认值）
     * zh-hk :繁体香港
     * 注：该繁体功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处免费开通；若未授权状态下设置为繁体并不会报错，但不会生效。
     */
    private String rlang;
    /**
     * 返回子句结果对应的起始和结束的端点帧偏移值。端点帧偏移值表示从音频开头起已过去的帧长度。
     * 0：关闭（默认值）
     * 1：开启
     * 开启后返回的结果中会增加data.result.vad字段，详见下方返回结果。
     * 注：若开通并使用了动态修正功能，则该功能无法使用。
     */
    private int vinfo;
    /**
     * （中文普通话和日语支持）将返回结果的数字格式规则为阿拉伯数字格式，默认开启
     * 0：关闭
     * 1：开启
     */
    private int nunum;
    /**
     * speex音频帧长，仅在speex音频时使用
     * 1 当speex编码为标准开源speex编码时必须指定
     * 2 当speex编码为讯飞定制speex编码时不要设置
     */
    private Integer speexSize;
    /**
     * 取值范围[1,5]，通过设置此参数，获取在发音相似时的句子多侯选结果。设置多候选会影响性能，响应时间延迟200ms左右。
     * 注：该扩展功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处免费开通；若未授权状态下设置该参数并不会报错，但不会生效。
     */
    private Integer nbest;
    /**
     * 取值范围[1,5]，通过设置此参数，获取在发音相似时的词语多侯选结果。设置多候选会影响性能，响应时间延迟200ms左右。
     * 注：该扩展功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处免费开通；若未授权状态下设置该参数并不会报错，但不会生效。
     */
    private Integer wbest;
    /**
     * 请注意不同音频格式一帧大小的字节数不同，我们建议：
     * <p>
     * 1.未压缩的PCM格式，每次发送音频间隔40ms，每次发送音频字节数1280B；
     * 2.讯飞定制speex格式，每次发送音频间隔40ms，假如16k的压缩等级为7，则每次发送61B的整数倍；
     * 3.标准开源speex格式，每次发送音频间隔40ms，假如16k的压缩等级为7，则每次发送60B的整数倍；
     */
    private Integer frameSize;

    public IatClient(Builder builder) {
        super(builder);
        this.language = builder.language;
        this.domain = builder.domain;
        this.accent = builder.accent;
        this.format = builder.format;
        this.encoding = builder.encoding;
        this.vadEos = builder.vad_eos;
        this.dwa = builder.dwa;
        this.pd = builder.pd;
        this.ptt = builder.ptt;
        this.rlang = builder.rlang;
        this.vinfo = builder.vinfo;
        this.nunum = builder.nunum;
        this.speexSize = builder.speex_size;
        this.nbest = builder.nbest;
        this.wbest = builder.wbest;
        this.frameSize = builder.frameSize;
    }

    /**
     * 发送文件给语音听写服务端
     *
     * @param file 发送的文件
     * @throws FileNotFoundException
     */
    public void send(File file, WebSocketListener webSocketListener) throws FileNotFoundException, MalformedURLException, SignatureException {
        createWebSocketConnect(webSocketListener);
        FileInputStream fileInputStream = new FileInputStream(file);
        send(fileInputStream, webSocketListener);
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

        // 语音听写数据发送任务
        IatSendTask iatSendTask = new IatSendTask();
        new IatSendTask.Builder()
                .inputStream(inputStream)
                .webSocketClient(this)
                .build(iatSendTask);


        executorService.submit(iatSendTask);
    }

    /**
     * @param bytes
     * @param closeable 需要关闭的流，可为空
     */
    public void send(byte[] bytes, Closeable closeable, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        createWebSocketConnect(webSocketListener);
        if (bytes == null || bytes.length == 0) {
            webSocket.close(1000, null);
            return;
        }

        IatSendTask iatSendTask = new IatSendTask();
        new IatSendTask.Builder()
                .bytes(bytes)
                .webSocketClient(this)
                .closeable(closeable)
                .build(iatSendTask);

        executorService.submit(iatSendTask);
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

    public String getFormat() {
        return format;
    }

    public String getEncoding() {
        return encoding;
    }

    public int getVad_eos() {
        return vadEos;
    }

    public String getDwa() {
        return dwa;
    }

    public String getPd() {
        return pd;
    }

    public int getPtt() {
        return ptt;
    }

    public String getRlang() {
        return rlang;
    }

    public int getVinfo() {
        return vinfo;
    }

    public int getNunum() {
        return nunum;
    }

    public Integer getSpeex_size() {
        return speexSize;
    }

    public Integer getNbest() {
        return nbest;
    }

    public Integer getWbest() {
        return wbest;
    }

    public Integer getFrameSize() {
        return frameSize;
    }

    @Override
    public String getSignature() {
        return null;
    }


    public static final class Builder extends WebSocketBuilder<Builder> {

        private static final String HOST_URL = "http://iat-api.xfyun.cn/v2/iat";

        private String language = "zh_cn";

        private String domain = "iat";

        private String accent = "mandarin";

        private String format = "audio/L16;rate=16000";

        private String encoding = "raw";

        private int vad_eos = 2000;

        private String dwa;

        private String pd;

        private int ptt = 1;

        private String rlang = "zh-cn";

        private int vinfo = 0;

        private int nunum = 1;

        private Integer speex_size = 2;

        private Integer nbest;

        private Integer wbest;

        private Integer frameSize = 1280;

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
        }

        @Override
        public IatClient build() {
            return new IatClient(this);
        }

        public IatClient.Builder language(String language) {
            this.language = language;
            return this;
        }

        public IatClient.Builder smallLanguage(Boolean isSmallLanguage) {
            if (isSmallLanguage) {
                this.hostUrl = IatClient.SMALL_LANGUAGE;
            } else {
                this.hostUrl = IatClient.CH_EN_LANGUAGE;
            }
            return this;
        }

        public IatClient.Builder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public IatClient.Builder accent(String accent) {
            this.accent = accent;
            return this;
        }

        public IatClient.Builder format(String format) {
            this.format = format;
            return this;
        }

        public IatClient.Builder encoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public IatClient.Builder vad_eos(int vad_eos) {
            this.vad_eos = vad_eos;
            return this;
        }

        public IatClient.Builder dwa(String dwa) {
            this.dwa = dwa;
            return this;
        }

        public IatClient.Builder pd(String pd) {
            this.pd = pd;
            return this;
        }

        public IatClient.Builder ptt(int ptt) {
            this.ptt = ptt;
            return this;
        }

        public IatClient.Builder rlang(String rlang) {
            this.rlang = rlang;
            return this;
        }

        public IatClient.Builder vinfo(int vinfo) {
            this.vinfo = vinfo;
            return this;
        }

        public IatClient.Builder nunum(int nunum) {
            this.nunum = nunum;
            return this;
        }

        public IatClient.Builder speex_size(Integer speex_size) {
            this.speex_size = speex_size;
            return this;
        }

        public IatClient.Builder nbest(Integer nbest) {
            this.nbest = nbest;
            return this;
        }

        public IatClient.Builder wbest(Integer wbest) {
            this.wbest = wbest;
            return this;
        }

        public IatClient.Builder frameSize(Integer frameSize) {
            this.frameSize = frameSize;
            return this;
        }
    }
}
