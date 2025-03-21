package cn.xfyun.model.voiceclone.request;

import cn.xfyun.api.VoiceCloneClient;
import com.google.gson.annotations.SerializedName;

/**
 * @program: websdk-java
 * @description:
 * @author: zyding6
 * @create: 2025/3/18 13:42
 **/
public class VoiceCloneRequest {

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

        @SerializedName("app_id")
        private String appId;
        private Integer status;
        @SerializedName("res_id")
        private String resId;

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

        public String getResId() {
            return resId;
        }

        public void setResId(String resId) {
            this.resId = resId;
        }
    }

    public static class Parameter {
        private Tts tts;

        public Parameter(VoiceCloneClient client) {
            this.tts = new Tts(client);
        }

        public Parameter() {
        }

        public Tts getTts() {
            return tts;
        }

        public void setTts(Tts tts) {
            this.tts = tts;
        }

        public static class Tts {
            private String vcn;
            @SerializedName("LanguageID")
            private String languageId;
            private Integer speed;
            private Integer volume;
            private Integer pitch;
            private Integer bgs;
            private Integer reg;
            private Integer rdn;
            private Integer rhy;
            private Audio audio;

            public Tts(VoiceCloneClient client) {
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

            public String getLanguageId() {
                return languageId;
            }

            public void setLanguageId(String languageId) {
                this.languageId = languageId;
            }

            public Audio getAudio() {
                return audio;
            }

            public void setAudio(Audio audio) {
                this.audio = audio;
            }

            public static class Audio {

                private String encoding;
                @SerializedName("sample_rate")
                private Integer sampleRate;

                public Audio(VoiceCloneClient client) {
                    this.encoding = client.getEncoding();
                    this.sampleRate = client.getSampleRate();
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
            }
        }
    }

    public static class Payload {
        private Text text;

        public Text getText() {
            return text;
        }

        public void setText(Text text) {
            this.text = text;
        }

        public static class Text {
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
