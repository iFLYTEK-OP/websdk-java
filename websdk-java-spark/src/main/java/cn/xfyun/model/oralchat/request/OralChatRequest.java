package cn.xfyun.model.oralchat.request;

import cn.xfyun.api.OralChatClient;
import cn.xfyun.model.oralchat.Avatar;
import com.google.gson.annotations.SerializedName;

/**
 * 超拟人聊天开启请求参数
 *
 * @author <zyding6@iflytek.com>
 **/
public class OralChatRequest {


    /**
     * header : {"app_id":"","uid":"","status":0,"stmid":"1","scene":"sos_app","interact_mode":"continuous"}
     * parameter : {"iat":{"iat":{"encoding":"utf8","compress":"raw","format":"json"},"vgap":50},"nlp":{"nlp":{"encoding":"utf8","compress":"raw","format":"json"},"new_session":"true","personal":"人设 id","prompt":"prompt 信息， 例如：你是小明，一个小学学生，热爱画画"},"tts":{"vcn":"x5_lingfeiyi_flow","res_id":"xxxx","res_gender":"","speed":50,"volume":50,"pitch":50,"tts":{"encoding":"raw","sample_rate":16000,"channels":1,"bit_depth":16,"frame_size":0}},"avatar":{"avatar_id":"","image":"","encoding":"","width":512,"height":512}}
     * payload : {"audio":{"status":0,"audio":"base64的音频数据","encoding":"raw","sample_rate":16000,"channels":1,"bit_depth":16,"frame_size":0}}
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
         * app_id :
         * uid :
         * status : 0
         * stmid : 1
         * scene : sos_app
         * interact_mode : continuous
         */

        @SerializedName("app_id")
        private String appId;
        private String uid;
        private Integer status;
        private String stmid;
        private String scene;
        @SerializedName("interact_mode")
        private String interactMode;
        @SerializedName("os_sys")
        private String osSys;
        @SerializedName("pers_param")
        private String persParam;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getStmid() {
            return stmid;
        }

        public void setStmid(String stmid) {
            this.stmid = stmid;
        }

        public String getScene() {
            return scene;
        }

        public void setScene(String scene) {
            this.scene = scene;
        }

        public String getInteractMode() {
            return interactMode;
        }

        public void setInteractMode(String interactMode) {
            this.interactMode = interactMode;
        }

        public String getOsSys() {
            return osSys;
        }

        public void setOsSys(String osSys) {
            this.osSys = osSys;
        }

        public String getPersParam() {
            return persParam;
        }

        public void setPersParam(String persParam) {
            this.persParam = persParam;
        }
    }

    public static class Parameter {
        /**
         * iat : {"iat":{"encoding":"utf8","compress":"raw","format":"json"},"vgap":50}
         * nlp : {"nlp":{"encoding":"utf8","compress":"raw","format":"json"},"new_session":"true","personal":"人设 id","prompt":"prompt 信息， 例如：你是小明，一个小学学生，热爱画画"}
         * tts : {"vcn":"x5_lingfeiyi_flow","res_id":"xxxx","res_gender":"","speed":50,"volume":50,"pitch":50,"tts":{"encoding":"raw","sample_rate":16000,"channels":1,"bit_depth":16,"frame_size":0}}
         * avatar : {"avatar_id":"","image":"","encoding":"","width":512,"height":512}
         */

        private Iat iat;
        private Nlp nlp;
        private Tts tts;
        private Avatar avatar;

        public Parameter() {
        }

        public Parameter(OralChatClient client) {
            this.iat = new Iat(client);
            this.nlp = new Nlp(client);
            this.tts = new Tts(client);
        }

        public Iat getIat() {
            return iat;
        }

        public void setIat(Iat iat) {
            this.iat = iat;
        }

        public Nlp getNlp() {
            return nlp;
        }

        public void setNlp(Nlp nlp) {
            this.nlp = nlp;
        }

        public Tts getTts() {
            return tts;
        }

        public void setTts(Tts tts) {
            this.tts = tts;
        }

        public Avatar getAvatar() {
            return avatar;
        }

        public void setAvatar(Avatar avatar) {
            this.avatar = avatar;
        }

        public static class Iat {
            /**
             * iat : {"encoding":"utf8","compress":"raw","format":"json"}
             * vgap : 50
             */

            private InnerIat iat;
            private Integer vgap;
            private String dwa;
            private String eos;
            private String domain;

            public Iat() {
            }

            public Iat(OralChatClient client) {
                this.iat = new InnerIat(client);
                this.vgap = client.getVgap();
                this.dwa = client.getDwa();
                this.eos = client.getEos();
                this.domain = client.getDomain();
            }

            public InnerIat getIat() {
                return iat;
            }

            public void setIat(InnerIat iat) {
                this.iat = iat;
            }

            public Integer getVgap() {
                return vgap;
            }

            public void setVgap(Integer vgap) {
                this.vgap = vgap;
            }

            public String getDwa() {
                return dwa;
            }

            public void setDwa(String dwa) {
                this.dwa = dwa;
            }

            public String getEos() {
                return eos;
            }

            public void setEos(String eos) {
                this.eos = eos;
            }

            public String getDomain() {
                return domain;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public static class InnerIat {
                /**
                 * encoding : utf8
                 * compress : raw
                 * format : json
                 */

                private String encoding;
                private String compress;
                private String format;

                public InnerIat() {
                }

                public InnerIat(OralChatClient client) {
                    this.encoding = client.getTextEncoding();
                    this.compress = client.getTextCompress();
                    this.format = client.getTextFormat();
                }

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
            }
        }

        public static class Nlp {
            /**
             * nlp : {"encoding":"utf8","compress":"raw","format":"json"}
             * new_session : true
             * personal : 人设 id
             * prompt : prompt 信息， 例如：你是小明，一个小学学生，热爱画画
             */

            private InnerNlp nlp;
            @SerializedName("new_session")
            private String newSession;
            private String personal;
            private String prompt;

            public Nlp() {
            }

            public Nlp(OralChatClient client) {
                this.nlp = new InnerNlp(client);
            }

            public InnerNlp getNlp() {
                return nlp;
            }

            public void setNlp(InnerNlp nlp) {
                this.nlp = nlp;
            }

            public String getNewSession() {
                return newSession;
            }

            public void setNewSession(String newSession) {
                this.newSession = newSession;
            }

            public String getPersonal() {
                return personal;
            }

            public void setPersonal(String personal) {
                this.personal = personal;
            }

            public String getPrompt() {
                return prompt;
            }

            public void setPrompt(String prompt) {
                this.prompt = prompt;
            }

            public static class InnerNlp {
                /**
                 * encoding : utf8
                 * compress : raw
                 * format : json
                 */

                private String encoding;
                private String compress;
                private String format;

                public InnerNlp() {
                }

                public InnerNlp(OralChatClient client) {
                    this.encoding = client.getTextEncoding();
                    this.compress = client.getTextCompress();
                    this.format = client.getTextFormat();
                }

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
            }
        }

        public static class Tts {
            /**
             * vcn : x5_lingfeiyi_flow
             * res_id : xxxx
             * res_gender :
             * speed : 50
             * volume : 50
             * pitch : 50
             * tts : {"encoding":"raw","sample_rate":16000,"channels":1,"bit_depth":16,"frame_size":0}
             */

            private String vcn;
            @SerializedName("res_id")
            private String resId;
            @SerializedName("res_gender")
            private String resGender;
            private Integer speed;
            private Integer volume;
            private Integer pitch;
            private InnerTts tts;

            public Tts() {
            }

            public Tts(OralChatClient client) {
                this.tts = new InnerTts(client);
            }

            public String getVcn() {
                return vcn;
            }

            public void setVcn(String vcn) {
                this.vcn = vcn;
            }

            public String getResId() {
                return resId;
            }

            public void setResId(String resId) {
                this.resId = resId;
            }

            public String getResGender() {
                return resGender;
            }

            public void setResGender(String resGender) {
                this.resGender = resGender;
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

            public InnerTts getTts() {
                return tts;
            }

            public void setTts(InnerTts tts) {
                this.tts = tts;
            }

            public static class InnerTts {
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

                public InnerTts() {
                }

                public InnerTts(OralChatClient client) {
                    this.encoding = client.getEncodingOut();
                    this.sampleRate = client.getSampleRateOut();
                    this.channels = client.getChannelsOut();
                    this.bitDepth = client.getBitDepthOut();
                    this.frameSize = client.getFrameSize();
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


            }
        }
    }

    public static class Payload {
        /**
         * audio : {"status":0,"audio":"base64的音频数据","encoding":"raw","sample_rate":16000,"channels":1,"bit_depth":16,"frame_size":0}
         */

        private Audio audio;

        public Payload() {
        }

        public Payload(OralChatClient client) {
            this.audio = new Audio(client);
        }

        public Audio getAudio() {
            return audio;
        }

        public void setAudio(Audio audio) {
            this.audio = audio;
        }

        public static class Audio {
            /**
             * status : 0
             * audio : base64的音频数据
             * encoding : raw
             * sample_rate : 16000
             * channels : 1
             * bit_depth : 16
             * frame_size : 0
             */

            private Integer status;
            private String audio;
            private String encoding;
            @SerializedName("sample_rate")
            private Integer sampleRate;
            private Integer channels;
            @SerializedName("bit_depth")
            private Integer bitDepth;
            @SerializedName("frame_size")
            private Integer frameSize;

            public Audio() {
            }

            public Audio(OralChatClient client) {
                this.encoding = client.getEncodingIn();
                this.sampleRate = client.getSampleRateIn();
                this.channels = client.getChannelsIn();
                this.bitDepth = client.getBitDepthIn();
                this.frameSize = client.getFrameSize();
            }

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
            }

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
