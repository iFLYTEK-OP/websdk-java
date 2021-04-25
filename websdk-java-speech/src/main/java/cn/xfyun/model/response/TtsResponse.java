package cn.xfyun.model.response;

/**
 * @author yingpeng
 * 在线语音合成返回参数
 */
public class TtsResponse {

    /**
     * 返回码，0表示成功，其它表示异常，详情请参考错误码。
     */
    private Integer code;

    /**
     * 描述信息
     */
    private String message;

    private Data data;

    /**
     * 本次会话的id，只在第一帧请求时返回
     */
    private String sid;

    public TtsResponse() {
    }

    public TtsResponse(Integer code, String message, Data data, String sid) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.sid = sid;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "TtsResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", sid='" + sid + '\'' +
                '}';
    }

    public static class Data {

        /**
         * 合成后的音频片段，采用base64编码
         */
        private String audio;

        /**
         * 当前音频流状态，1表示合成中，2表示合成结束
         */
        private Integer status;

        /**
         * 合成进度，指当前合成文本的字节数
         * 注：请注意合成是以句为单位切割的，若文本只有一句话，则每次返回结果的ced是相同的。
         */
        private String ced;

        public Data() {
        }

        public Data(String audio, Integer status, String ced) {
            this.audio = audio;
            this.status = status;
            this.ced = ced;
        }

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getCed() {
            return ced;
        }

        public void setCed(String ced) {
            this.ced = ced;
        }
    }
}
