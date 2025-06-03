package cn.xfyun.model.simult.request;

import cn.xfyun.api.SimInterpClient;
import com.google.gson.annotations.SerializedName;

/**
 * 同声传译请求实体类
 *
 * @author <zyding6@ifytek.com>
 */
public class SimInterpRequest {


    /**
     * header : {"app_id":"your_app_id","status":0}
     * parameter : {"ist":{"accent":"mandarin","domain":"ist_ed_open","language":"zh_cn","vto":15000,"eos":150000},"streamtrans":{"from":"cn","to":"en"},"tts":{"vcn":"x2_john","tts_results":{"encoding":"raw","sample_rate":16000,"channels":1,"bit_depth":16,"frame_size":0}}}
     * payload : {"data":{"audio":"JiuY3iK9AAB...","encoding":"raw","sample_rate":16000,"seq":1,"status":0}}
     */

    private Header header;
    private Parameter parameter;
    private Payload payload;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public static class Header {
        /**
         * app_id : your_app_id
         * status : 0
         */

        @SerializedName("app_id")
        private String appId;
        private Integer status;

        public Header() {
        }

        public Header(String appId, Integer status) {
            this.appId = appId;
            this.status = status;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }

    public static class Parameter {
        /**
         * ist : {"accent":"mandarin","domain":"ist_ed_open","language":"zh_cn","vto":15000,"eos":150000}
         * streamtrans : {"from":"cn","to":"en"}
         * tts : {"vcn":"x2_john","tts_results":{"encoding":"raw","sample_rate":16000,"channels":1,"bit_depth":16,"frame_size":0}}
         */

        private Ist ist;
        private Streamtrans streamtrans;
        private Tts tts;

        public Parameter() {
        }

        public Parameter(SimInterpClient client) {
            this.ist = new Ist(client);
            this.streamtrans = new Streamtrans(client);
            this.tts = new Tts(client);
        }

        public Ist getIst() {
            return ist;
        }

        public void setIst(Ist ist) {
            this.ist = ist;
        }

        public Streamtrans getStreamtrans() {
            return streamtrans;
        }

        public void setStreamtrans(Streamtrans streamtrans) {
            this.streamtrans = streamtrans;
        }

        public Tts getTts() {
            return tts;
        }

        public void setTts(Tts tts) {
            this.tts = tts;
        }

        public static class Ist {
            /**
             * accent : mandarin
             * domain : ist_ed_open
             * language : zh_cn
             * vto : 15000
             * eos : 150000
             */

            private String accent;
            private String domain;
            private String language;
            private Integer vto;
            private Integer eos;

            public Ist() {
            }

            public Ist(SimInterpClient client) {
                this.accent = client.getAccent();
                this.domain = client.getDomain();
                this.language = client.getLanguage();
                this.vto = client.getVto();
                this.eos = client.getEos();
            }

            public String getAccent() {
                return accent;
            }

            public void setAccent(String accent) {
                this.accent = accent;
            }

            public String getDomain() {
                return domain;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }

            public Integer getVto() {
                return vto;
            }

            public void setVto(Integer vto) {
                this.vto = vto;
            }

            public Integer getEos() {
                return eos;
            }

            public void setEos(Integer eos) {
                this.eos = eos;
            }
        }

        public static class Streamtrans {
            /**
             * from : cn
             * to : en
             */

            private String from;
            private String to;

            public Streamtrans() {
            }

            public Streamtrans(SimInterpClient client) {
                this.from = client.getFrom();
                this.to = client.getTo();
            }

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public String getTo() {
                return to;
            }

            public void setTo(String to) {
                this.to = to;
            }
        }

        public static class Tts {
            /**
             * vcn : x2_john
             * tts_results : {"encoding":"raw","sample_rate":16000,"channels":1,"bit_depth":16,"frame_size":0}
             */

            private String vcn;
            @SerializedName("tts_results")
            private TtsResults ttsResults;

            public Tts() {
            }

            public Tts(SimInterpClient client) {
                this.vcn = client.getVcn();
                this.ttsResults = new TtsResults(client);
            }

            public String getVcn() {
                return vcn;
            }

            public void setVcn(String vcn) {
                this.vcn = vcn;
            }

            public TtsResults getTtsResults() {
                return ttsResults;
            }

            public void setTtsResults(TtsResults ttsResults) {
                this.ttsResults = ttsResults;
            }

            public static class TtsResults {
                /**
                 * encoding : raw
                 * sample_rate : 16000
                 * channels : 1
                 * bit_depth : 16
                 * frame_size : 0
                 */

                private String encoding;
                @SerializedName("sample_rate")
                private Integer sampleRate;
                private Integer channels;
                @SerializedName("bit_depth")
                private Integer bitDepth;
                @SerializedName("frame_size")
                private Integer frameSize;

                public TtsResults() {
                }

                public TtsResults(SimInterpClient client) {
                    this.encoding = client.getEncoding();
                    this.sampleRate = client.getSampleRate();
                    this.channels = client.getChannels();
                    this.bitDepth = client.getBitDepth();
                }

                public String getEncoding() {
                    return encoding;
                }

                public void setEncoding(String encoding) {
                    this.encoding = encoding;
                }

                public Integer getSampleRate() {
                    return sampleRate;
                }

                public void setSampleRate(Integer sampleRate) {
                    this.sampleRate = sampleRate;
                }

                public Integer getChannels() {
                    return channels;
                }

                public void setChannels(Integer channels) {
                    this.channels = channels;
                }

                public Integer getBitDepth() {
                    return bitDepth;
                }

                public void setBitDepth(Integer bitDepth) {
                    this.bitDepth = bitDepth;
                }

                public Integer getFrameSize() {
                    return frameSize;
                }

                public void setFrameSize(Integer frameSize) {
                    this.frameSize = frameSize;
                }
            }
        }
    }

    public static class Payload {
        /**
         * data : {"audio":"JiuY3iK9AAB...","encoding":"raw","sample_rate":16000,"seq":1,"status":0}
         */

        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public static class Data {
            /**
             * audio : JiuY3iK9AAB...
             * encoding : raw
             * sample_rate : 16000
             * seq : 1
             * status : 0
             */

            private String audio;
            private String encoding;
            @SerializedName("sample_rate")
            private Integer sampleRate;
            private Integer seq;
            private Integer status;

            public String getAudio() {
                return audio;
            }

            public void setAudio(String audio) {
                this.audio = audio;
            }

            public String getEncoding() {
                return encoding;
            }

            public void setEncoding(String encoding) {
                this.encoding = encoding;
            }

            public Integer getSampleRate() {
                return sampleRate;
            }

            public void setSampleRate(Integer sampleRate) {
                this.sampleRate = sampleRate;
            }

            public Integer getSeq() {
                return seq;
            }

            public void setSeq(Integer seq) {
                this.seq = seq;
            }

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
            }
        }
    }
}
