package cn.xfyun.model.oral.request;

import cn.xfyun.api.OralClient;
import com.google.gson.annotations.SerializedName;

/**
 * @program: websdk-java
 * @description:
 * @author: zyding6
 * @create: 2025/3/18 13:42
 **/
public class OralRequest {


    /**
     * header : {"app_id":"123456","status":2}
     * parameter : {"oral":{"oral_level":"mid"},"tts":{"vcn":"x4_lingfeiyi_oral","speed":50,"volume":50,"pitch":50,"bgs":0,"reg":0,"rdn":0,"rhy":0,"audio":{"encoding":"lame","sample_rate":24000,"channels":1,"bit_depth":16,"frame_size":0}}}
     * payload : {"text":{"encoding":"utf8","compress":"raw","format":"plain","status":2,"seq":0,"text":"xxxxxxx"}}
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
         * app_id : 123456
         * status : 2
         */

        @SerializedName("app_id")
        private String appId;
        private Integer status;

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
         * oral : {"oral_level":"mid"}
         * tts : {"vcn":"x4_lingfeiyi_oral","speed":50,"volume":50,"pitch":50,"bgs":0,"reg":0,"rdn":0,"rhy":0,"audio":{"encoding":"lame","sample_rate":24000,"channels":1,"bit_depth":16,"frame_size":0}}
         */

        private Oral oral;
        private Tts tts;

        public Parameter(OralClient client) {
            this.oral = new Oral(client);
            this.tts = new Tts(client);
        }

        public Parameter() {
        }

        public Oral getOral() {
            return oral;
        }

        public void setOral(Oral oral) {
            this.oral = oral;
        }

        public Tts getTts() {
            return tts;
        }

        public void setTts(Tts tts) {
            this.tts = tts;
        }

        public static class Oral {
            /**
             * oral_level : mid
             */

            @SerializedName("spark_assist")
            private Integer sparkAssist;

            @SerializedName("oral_level")
            private String oralLevel;

            @SerializedName("stop_split")
            private Integer stopSplit;

            private Integer remain;

            public Oral(OralClient client) {
                this.sparkAssist = client.getSparkAssist();
                this.oralLevel = client.getOralLevel();
                this.stopSplit = client.getStopSplit();
                this.remain = client.getRemain();
            }

            public Oral() {
            }

            public Integer getSparkAssist() {
                return sparkAssist;
            }

            public void setSparkAssist(Integer sparkAssist) {
                this.sparkAssist = sparkAssist;
            }

            public String getOralLevel() {
                return oralLevel;
            }

            public void setOralLevel(String oralLevel) {
                this.oralLevel = oralLevel;
            }

            public Integer getStopSplit() {
                return stopSplit;
            }

            public void setStopSplit(Integer stopSplit) {
                this.stopSplit = stopSplit;
            }

            public Integer getRemain() {
                return remain;
            }

            public void setRemain(Integer remain) {
                this.remain = remain;
            }
        }

        public static class Tts {
            /**
             * vcn : x4_lingfeiyi_oral
             * speed : 50
             * volume : 50
             * pitch : 50
             * bgs : 0
             * reg : 0
             * rdn : 0
             * rhy : 0
             * audio : {"encoding":"lame","sample_rate":24000,"channels":1,"bit_depth":16,"frame_size":0}
             */

            private String vcn;
            private Integer speed;
            private Integer volume;
            private Integer pitch;
            private Integer bgs;
            private Integer reg;
            private Integer rdn;
            private Integer rhy;
            private Audio audio;

            public Tts(OralClient client) {
                this.vcn = client.getVcn();
                this.speed = client.getSpeed();
                this.volume = client.getVolume();
                this.pitch = client.getPitch();
                this.bgs = client.getBgs();
                this.reg = client.getReg();
                this.rdn = client.getRdn();
                this.rhy = client.getRhy();
                this.audio = new Audio(client);
            }

            public Tts() {
            }

            public String getVcn() {
                return vcn;
            }

            public void setVcn(String vcn) {
                this.vcn = vcn;
            }

            public Integer getSpeed() {
                return speed;
            }

            public void setSpeed(Integer speed) {
                this.speed = speed;
            }

            public Integer getVolume() {
                return volume;
            }

            public void setVolume(Integer volume) {
                this.volume = volume;
            }

            public Integer getPitch() {
                return pitch;
            }

            public void setPitch(Integer pitch) {
                this.pitch = pitch;
            }

            public Integer getBgs() {
                return bgs;
            }

            public void setBgs(Integer bgs) {
                this.bgs = bgs;
            }

            public Integer getReg() {
                return reg;
            }

            public void setReg(Integer reg) {
                this.reg = reg;
            }

            public Integer getRdn() {
                return rdn;
            }

            public void setRdn(Integer rdn) {
                this.rdn = rdn;
            }

            public Integer getRhy() {
                return rhy;
            }

            public void setRhy(Integer rhy) {
                this.rhy = rhy;
            }

            public Audio getAudio() {
                return audio;
            }

            public void setAudio(Audio audio) {
                this.audio = audio;
            }

            public static class Audio {
                /**
                 * encoding : lame
                 * sample_rate : 24000
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

                public Audio(OralClient client) {
                    this.encoding = client.getEncoding();
                    this.sampleRate = client.getSampleRate();
                    this.channels = client.getChannels();
                    this.bitDepth = client.getBitDepth();
                    this.frameSize = client.getFrameSize();
                }

                public Audio() {
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
         * text : {"encoding":"utf8","compress":"raw","format":"plain","status":2,"seq":0,"text":"xxxxxxx"}
         */

        private Text text;

        public Text getText() {
            return text;
        }

        public void setText(Text text) {
            this.text = text;
        }

        public static class Text {
            /**
             * encoding : utf8
             * compress : raw
             * format : plain
             * status : 2
             * seq : 0
             * text : xxxxxxx
             */

            private String encoding;
            private String compress;
            private String format;
            private Integer status;
            private Integer seq;
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

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
            }

            public Integer getSeq() {
                return seq;
            }

            public void setSeq(Integer seq) {
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
