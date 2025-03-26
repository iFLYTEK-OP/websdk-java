package cn.xfyun.model.voiceclone.response;

/**
 * @author zyding
 * 一句话合成返回参数
 */
public class VoiceCloneResponse {


    /**
     * header : {"code":0,"message":"success","sid":"ase000704fa@dx16ade44e4d87a1c802","status":0}
     * payload : {"audio":{"encoding":"speex-wb","sample_rate":16000,"channels":1,"bit_depth":16,"status":0,"seq":0,"audio":"","frame_size":0},"pybuf":{"encoding":"utf8","compress":"raw","format":"plain","status":0,"seq":0,"text":""}}
     */

    private HeaderBean header;
    private PayloadBean payload;

    public VoiceCloneResponse() {
    }

    public VoiceCloneResponse(Integer code, String message, PayloadBean payload, String sid) {
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
         * sid : ase000704fa@dx16ade44e4d87a1c802
         * status : 0
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
         * audio : {"encoding":"speex-wb","sample_rate":16000,"channels":1,"bit_depth":16,"status":0,"seq":0,"audio":"","frame_size":0}
         * pybuf : {"encoding":"utf8","compress":"raw","format":"plain","status":0,"seq":0,"text":""}
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
             * encoding : speex-wb
             * sample_rate : 16000
             * channels : 1
             * bit_depth : 16
             * status : 0
             * seq : 0
             * audio :
             * frame_size : 0
             */

            private String encoding;
            private int sample_rate;
            private int channels;
            private int bit_depth;
            private int status;
            private int seq;
            private String audio;
            private int frame_size;

            public String getEncoding() {
                return encoding;
            }

            public void setEncoding(String encoding) {
                this.encoding = encoding;
            }

            public int getSample_rate() {
                return sample_rate;
            }

            public void setSample_rate(int sample_rate) {
                this.sample_rate = sample_rate;
            }

            public int getChannels() {
                return channels;
            }

            public void setChannels(int channels) {
                this.channels = channels;
            }

            public int getBit_depth() {
                return bit_depth;
            }

            public void setBit_depth(int bit_depth) {
                this.bit_depth = bit_depth;
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

            public String getAudio() {
                return audio;
            }

            public void setAudio(String audio) {
                this.audio = audio;
            }

            public int getFrame_size() {
                return frame_size;
            }

            public void setFrame_size(int frame_size) {
                this.frame_size = frame_size;
            }
        }

        public static class PybufBean {
            /**
             * encoding : utf8
             * compress : raw
             * format : plain
             * status : 0
             * seq : 0
             * text :
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
