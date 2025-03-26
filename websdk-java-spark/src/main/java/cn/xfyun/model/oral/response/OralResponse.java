package cn.xfyun.model.oral.response;


import com.google.gson.annotations.SerializedName;

/**
 * @author zyding
 * 超拟人合成返回参数
 */
public class OralResponse {

    /**
     * header : {"code":0,"message":"success","sid":"aso000ede92@dx18caf514baab832882","status":1}
     * payload : {"audio":{"encoding":"lame","sample_rate":24000,"channels":1,"bit_depth":16,"status":0,"seq":0,"frame_size":0,"audio":"xxxxx"},"pybuf":{"encoding":"utf8","compress":"raw","format":"plain","status":0,"seq":0,"text":"xxxxx"}}
     */

    /**
     * 协议头部，用于描述平台特性的参数
     */
    private HeaderBean header;

    /**
     * 数据段，携带响应的数据。
     */
    private PayloadBean payload;

    public OralResponse() {
    }

    public OralResponse(Integer code, String message, PayloadBean payload, String sid) {
        this.header = new HeaderBean();
        this.header.code = code;
        this.header.message = message;
        this.header.sid = sid;
        this.payload = payload;
    }

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public PayloadBean getPayload() {
        return payload;
    }

    public void setPayload(PayloadBean payload) {
        this.payload = payload;
    }

    public static class HeaderBean {
        /**
         * code : 0
         * message : success
         * sid : aso000ede92@dx18caf514baab832882
         * status : 1
         */

        /**
         * 返回码，0表示成功，其它表示异常
         */
        private int code;

        /**
         * 返回信息，成功时为success，其它为失败原因
         */
        private String message;

        /**
         * 本次会话的id
         */
        private String sid;
        private int status;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class PayloadBean {
        /**
         * audio : {"encoding":"lame","sample_rate":24000,"channels":1,"bit_depth":16,"status":0,"seq":0,"frame_size":0,"audio":"xxxxx"}
         * pybuf : {"encoding":"utf8","compress":"raw","format":"plain","status":0,"seq":0,"text":"xxxxx"}
         */

        /**
         * 音频数据段，包含音频内容
         */
        private AudioBean audio;

        /**
         * pybuf, 当 rhy = 1 时返回
         */
        private PybufBean pybuf;

        public AudioBean getAudio() {
            return audio;
        }

        public void setAudio(AudioBean audio) {
            this.audio = audio;
        }

        public PybufBean getPybuf() {
            return pybuf;
        }

        public void setPybuf(PybufBean pybuf) {
            this.pybuf = pybuf;
        }

        public static class AudioBean {
            /**
             * encoding : lame
             * sample_rate : 24000
             * channels : 1
             * bit_depth : 16
             * status : 0
             * seq : 0
             * frame_size : 0
             * audio : xxxxx
             */

            /**
             * 音频编码  lame, raw
             */
            private String encoding;

            /**
             * 文采样率  16000, 8000, 24000  默认 24000
             */
            @SerializedName("sample_rate")
            private int sampleRate;

            /**
             * 声道数，单声道为1，双声道为2   默认 1
             */
            private int channels;

            /**
             * 位深    16, 8  默认16
             */
            @SerializedName("bit_depth")
            private int bitDepth;

            /**
             * 数据状态   0:开始, 1:中间, 2:结束
             */
            private int status;

            /**
             * 数据序号   最小值:0, 最大值:9999999   默认 0
             */
            private int seq;

            /**
             * 帧大小  最小值:0, 最大值:1024   默认 0
             */
            @SerializedName("frame_size")
            private int frameSize;

            /**
             * base64编码后的音频数据  最小尺寸:0B, 最大尺寸:10485760B(音频大小：0-10M)
             */
            private String audio;

            public String getEncoding() {
                return encoding;
            }

            public void setEncoding(String encoding) {
                this.encoding = encoding;
            }

            public int getSampleRate() {
                return sampleRate;
            }

            public void setSampleRate(int sampleRate) {
                this.sampleRate = sampleRate;
            }

            public int getChannels() {
                return channels;
            }

            public void setChannels(int channels) {
                this.channels = channels;
            }

            public int getBitDepth() {
                return bitDepth;
            }

            public void setBitDepth(int bitDepth) {
                this.bitDepth = bitDepth;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getSeq() {
                return seq;
            }

            public void setSeq(int seq) {
                this.seq = seq;
            }

            public int getFrameSize() {
                return frameSize;
            }

            public void setFrameSize(int frameSize) {
                this.frameSize = frameSize;
            }

            public String getAudio() {
                return audio;
            }

            public void setAudio(String audio) {
                this.audio = audio;
            }
        }

        public static class PybufBean {
            /**
             * 当 rhy = 1 时返回此词典。
             * encoding : utf8
             * compress : raw
             * format : plain
             * status : 0
             * seq : 0
             * text : xxxxx
             */

            /**
             * 文本编码   utf8   默认utf8
             */
            private String encoding;

            /**
             * 文本压缩格式  raw   默认  raw
             */
            private String compress;

            /**
             * 文本格式   plain, json    默认 plain
             */
            private String format;

            /**
             * 数据状态   0:开始, 1:中间, 2:结束(一次性合成直接传2)
             */
            private int status;

            /**
             * 数据序号   最小值:0, 最大值:9999999
             */
            private int seq;

            /**
             * 最小尺寸:0B, 最大尺寸:1048576B(文本大小：0-1M)
             */
            private String text;

            public String getEncoding() {
                return encoding;
            }

            public void setEncoding(String encoding) {
                this.encoding = encoding;
            }

            public String getCompress() {
                return compress;
            }

            public void setCompress(String compress) {
                this.compress = compress;
            }

            public String getFormat() {
                return format;
            }

            public void setFormat(String format) {
                this.format = format;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getSeq() {
                return seq;
            }

            public void setSeq(int seq) {
                this.seq = seq;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }
}
