package cn.xfyun.api;

import cn.xfyun.base.websocket.AbstractClient;
import cn.xfyun.base.websocket.WebsocketBuilder;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.voiceclone.VoiceCloneParam;
import cn.xfyun.model.voiceclone.request.VoiceCloneRequest;
import cn.xfyun.util.StringUtils;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Base64;

/**
 * 一句话复刻合成 Client (标准版、美化版)
 * 文档地址: <a href="https://www.xfyun.cn/doc/spark/reproduction.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class VoiceCloneV2Client extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(VoiceCloneV2Client.class);

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
     * 数据状态
     * 固定值:2一次性传完
     */
    private final int status;

    /**
     * 	输出音素时长信息
     */
    private final int pybuffer;

    public VoiceCloneV2Client(Builder builder) {
        super(builder);
        this.originHostUrl = builder.hostUrl;

        this.textEncoding = builder.textEncoding;
        this.textCompress = builder.textCompress;
        this.textFormat = builder.textFormat;
        this.encoding = builder.encoding;
        this.sampleRate = builder.sampleRate;
        this.status = builder.status;
        this.pybuffer = builder.pybuffer;
    }

    public int getPybuffer() {
        return pybuffer;
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

    public int getStatus() {
        return status;
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
     * @param voiceCloneParam             请求入参
     * @param webSocketListener ws监听类 AbstractVoiceCloneWebSocketListener
     */
    public void send(VoiceCloneParam voiceCloneParam, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        // 参数校验
        paramCheck(voiceCloneParam);

        // 初始化链接client
        WebSocket socket = newWebSocket(webSocketListener);

        try {
            // 构建请求参数
            String param = buildParam(voiceCloneParam);
            logger.debug("一句话复刻合成入参：{}", param);

            // 发送合成文本
            socket.send(param);
        } catch (Exception e) {
            logger.error("一句话复刻合成请求出错", e);
        }
    }

    /**
     * 参数校验
     */
    private void paramCheck(VoiceCloneParam param) {
        if (null == param) {
            throw new BusinessException("参数不能为空");
        }
        param.selfCheck();
    }

    /**
     * 构建参数
     */
    private String buildParam(VoiceCloneParam param) {
        VoiceCloneRequest request = new VoiceCloneRequest();
        // 请求头
        VoiceCloneRequest.Header header = new VoiceCloneRequest.Header();
        header.setAppId(appId);
        header.setStatus(status);
        header.setResId(param.getResId());
        request.setHeader(header);
        // 请求参数
        VoiceCloneRequest.Parameter parameter = new VoiceCloneRequest.Parameter();
        VoiceCloneRequest.Parameter.Tts tts = new VoiceCloneRequest.Parameter.Tts();
        VoiceCloneRequest.Parameter.Tts.Audio audio = new VoiceCloneRequest.Parameter.Tts.Audio();
        tts.setSpeed(param.getSpeed());
        tts.setVolume(param.getVolume());
        tts.setPitch(param.getPitch());
        tts.setLanguageId(param.getLanguageId());
        tts.setBgs(param.getBgs());
        tts.setReg(param.getReg());
        tts.setRdn(param.getRdn());
        tts.setRhy(param.getRhy());
        tts.setVcn(param.getVcn());
        tts.setStyle(param.getStyle());
        tts.setPybuffer(pybuffer);
        audio.setEncoding(encoding);
        audio.setSampleRate(sampleRate);
        tts.setAudio(audio);
        parameter.setTts(tts);
        request.setParameter(parameter);
        // 请求体
        VoiceCloneRequest.Payload payload = new VoiceCloneRequest.Payload();
        VoiceCloneRequest.Payload.Text payloadText = new VoiceCloneRequest.Payload.Text();
        payloadText.setEncoding(textEncoding);
        payloadText.setCompress(textCompress);
        payloadText.setFormat(textFormat);
        payloadText.setStatus(status);
        payloadText.setSeq(0);
        payloadText.setText(Base64.getEncoder().encodeToString(param.getText().getBytes(StandardCharsets.UTF_8)));
        payload.setText(payloadText);
        request.setPayload(payload);
        return StringUtils.gson.toJson(request);
    }

    public static final class Builder extends WebsocketBuilder<Builder> {

        private String hostUrl = "http://cn-huabei-1.xf-yun.com/v1/private/voice_clone";
        private String textEncoding = "utf8";
        private String textCompress = "raw";
        private String textFormat = "plain";
        private String encoding = "lame";
        private int sampleRate = 24000;
        private int status = 2;
        private int pybuffer = 1;

        public VoiceCloneV2Client build() {
            return new VoiceCloneV2Client(this);
        }

        public Builder signature(String appId, String apiKey, String apiSecret) {
            super.signature(appId, apiKey, apiSecret);
            return this;
        }

        public Builder pybuffer(int pybuffer) {
            this.pybuffer = pybuffer;
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
    }
}
