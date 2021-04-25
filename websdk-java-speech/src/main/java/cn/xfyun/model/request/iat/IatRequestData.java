package cn.xfyun.model.request.iat;

import cn.xfyun.api.IatClient;

/**
 * @author <ydwang16@iflytek.com>
 * @description 语音听写请求
 * @date 2021/3/24
 */
public class IatRequestData {

    /**
     * 音频的状态
     * 0 :第一帧音频
     * 1 :中间的音频
     * 2 :最后一帧音频，最后一帧必须要发送
     */
    private Integer status;

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
     * 音频内容，采用base64编码
     */
    private String audio;

    public IatRequestData(IatClient iatClient) {
        this.encoding = iatClient.getEncoding();
        this.format = iatClient.getFormat();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
