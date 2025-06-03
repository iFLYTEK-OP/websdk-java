package cn.xfyun.model.simult.response;

import com.google.gson.annotations.SerializedName;

/**
 * 同声传译返回参数
 *
 * @author <zyding6@ifytek.com>
 */
public class SimInterpResponse {

    private Header header;
    private Payload payload;

    public SimInterpResponse(Integer code, String message) {
        this.header = new Header();
        this.header.code = code;
        this.header.message = message;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public static class Header {

        private Integer code;
        private String message;
        private String sid;
        private Integer status;

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

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }

    public static class Payload {

        @SerializedName("recognition_results")
        private RecognitionResults recognitionResults;

        @SerializedName("streamtrans_results")
        private StreamtransResults streamtransResults;

        @SerializedName("tts_results")
        private TtsResults ttsResults;

        public RecognitionResults getRecognitionResults() {
            return recognitionResults;
        }

        public void setRecognitionResults(RecognitionResults recognitionResults) {
            this.recognitionResults = recognitionResults;
        }

        public StreamtransResults getStreamtransResults() {
            return streamtransResults;
        }

        public void setStreamtransResults(StreamtransResults streamtransResults) {
            this.streamtransResults = streamtransResults;
        }

        public TtsResults getTtsResults() {
            return ttsResults;
        }

        public void setTtsResults(TtsResults ttsResults) {
            this.ttsResults = ttsResults;
        }

        public static class RecognitionResults {

            private String format;
            private Integer status;
            private String text;
            private String encoding;

            public String getFormat() {
                return format;
            }

            public void setFormat(String format) {
                this.format = format;
            }

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getEncoding() {
                return encoding;
            }

            public void setEncoding(String encoding) {
                this.encoding = encoding;
            }
        }

        public static class StreamtransResults {

            private String text;
            private String seq;
            private Integer status;
            private String encoding;
            private String format;
            private String compress;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getSeq() {
                return seq;
            }

            public void setSeq(String seq) {
                this.seq = seq;
            }

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
            }

            public String getEncoding() {
                return encoding;
            }

            public void setEncoding(String encoding) {
                this.encoding = encoding;
            }

            public String getFormat() {
                return format;
            }

            public void setFormat(String format) {
                this.format = format;
            }

            public String getCompress() {
                return compress;
            }

            public void setCompress(String compress) {
                this.compress = compress;
            }
        }

        public static class TtsResults {

            private String encoding;
            private Integer channels;
            private String id;
            private Integer seq;
            private String audio;
            @SerializedName("sample_rate")
            private Integer sampleRate;
            private Integer status;
            @SerializedName("bit_depth")
            private String bitDepth;
            private String type;
            private String ced;

            public String getEncoding() {
                return encoding;
            }

            public void setEncoding(String encoding) {
                this.encoding = encoding;
            }

            public Integer getChannels() {
                return channels;
            }

            public void setChannels(Integer channels) {
                this.channels = channels;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Integer getSeq() {
                return seq;
            }

            public void setSeq(Integer seq) {
                this.seq = seq;
            }

            public String getAudio() {
                return audio;
            }

            public void setAudio(String audio) {
                this.audio = audio;
            }

            public Integer getSampleRate() {
                return sampleRate;
            }

            public void setSampleRate(Integer sampleRate) {
                this.sampleRate = sampleRate;
            }

            public String getBitDepth() {
                return bitDepth;
            }

            public void setBitDepth(String bitDepth) {
                this.bitDepth = bitDepth;
            }

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getCed() {
                return ced;
            }

            public void setCed(String ced) {
                this.ced = ced;
            }
        }
    }
}
