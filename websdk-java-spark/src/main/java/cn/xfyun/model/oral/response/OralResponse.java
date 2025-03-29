package cn.xfyun.model.oral.response;


import com.google.gson.annotations.SerializedName;

/**
 * @author zyding
 * <p>
 * 超拟人合成返回参数
 */
public class OralResponse {

    /**
     * header : {"code":0,"message":"success","sid":"aso000ede92@dx18caf514baab832882","status":1}
     * payload : {"audio":{"encoding":"lame","sample_rate":24000,"channels":1,"bit_depth":16,"status":0,"seq":0,"frame_size":0,"audio":"xxxxx"},"pybuf":{"encoding":"utf8","compress":"raw","format":"plain","status":0,"seq":0,"text":"xxxxx"}}
     */

    private HeaderBean header;

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

        private int code;
        private String message;
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

        private AudioBean audio;
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

            private String encoding;
            @SerializedName("sample_rate")
            private int sampleRate;
            private int channels;
            @SerializedName("bit_depth")
            private int bitDepth;
            private int status;
            private int seq;
            @SerializedName("frame_size")
            private int frameSize;
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

            private String encoding;
            private String compress;
            private String format;
            private int status;
            private int seq;
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
