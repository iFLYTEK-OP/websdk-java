package cn.xfyun.model.sparkiat.request;

import cn.xfyun.api.SparkIatClient;
import cn.xfyun.config.SparkIatModelEnum;
import com.google.gson.annotations.SerializedName;

/**
 * @program: websdk-java
 * @description:
 * @author: zyding6
 * @create: 2025/3/20 17:14
 **/
public class SparkIatRequest {


    /**
     * header : {"app_id":"your_appid","status":0}
     * parameter : {"iat":{"domain":"slm","language":"zh_cn","accent":"mandarin","eos":6000,"vinfo":1,"dwa":"wpgs","result":{"encoding":"utf8","compress":"raw","format":"json"}}}
     * payload : {"audio":{"encoding":"raw","sample_rate":16000,"channels":1,"bit_depth":16,"seq":1,"status":0,"audio":"AAAAAP..."}}
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
         * app_id : your_appid
         * status : 0
         */

        @SerializedName("app_id")
        private String appId;
        private int status;

        public Header(String appId, int status) {
            this.appId = appId;
            this.status = status;
        }

        public Header() {
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class Parameter {
        /**
         * iat : {"domain":"slm","language":"zh_cn","accent":"mandarin","eos":6000,"vinfo":1,"dwa":"wpgs","result":{"encoding":"utf8","compress":"raw","format":"json"}}
         */

        private Iat iat;

        public Parameter(SparkIatClient client) {
            this.iat = new Iat(client);
        }

        public Parameter() {
        }

        public Iat getIat() {
            return iat;
        }

        public void setIat(Iat iat) {
            this.iat = iat;
        }

        public static class Iat {
            /**
             * domain : slm
             * language : zh_cn
             * accent : mandarin
             * eos : 6000
             * vinfo : 1
             * dwa : wpgs
             * result : {"encoding":"utf8","compress":"raw","format":"json"}
             */

            private String domain;
            private String language;
            private String accent;
            private Integer eos;
            private Integer vinfo;
            private String dwa;
            private Integer nbest;
            private Integer wbest;
            private Integer ptt;
            private Integer smth;
            private Integer nunum;
            private Integer opt;
            private String dhw;
            private String rlang;
            private Integer ltc;
            private String ln;
            private Result result;

            public Iat(SparkIatClient client) {
                this.domain = client.getDomain();
                this.language = client.getLanguage();
                this.accent = client.getAccent();
                this.eos = client.getEos();
                this.vinfo = client.getVinfo();
                this.dwa = client.getDwa();
                this.result = new Result(client);
                if (SparkIatModelEnum.ZH_CN_MULACC.codeEquals(client.getLangType())) {
                    this.nbest = client.getNbest();
                    this.wbest = client.getWbest();
                    this.ptt = client.getPtt();
                    this.smth = client.getSmth();
                    this.nunum = client.getNunum();
                    this.opt = client.getOpt();
                    this.dhw = client.getDhw();
                    this.rlang = client.getRlang();
                    this.ltc = client.getLtc();
                } else if (SparkIatModelEnum.MUL_CN_MANDARIN.codeEquals(client.getLangType())) {
                    this.ln = client.getLn();
                }
            }

            public Iat() {
            }

            public String getLn() {
                return ln;
            }

            public void setLn(String ln) {
                this.ln = ln;
            }

            public void setEos(Integer eos) {
                this.eos = eos;
            }

            public void setVinfo(Integer vinfo) {
                this.vinfo = vinfo;
            }

            public Integer getNbest() {
                return nbest;
            }

            public void setNbest(Integer nbest) {
                this.nbest = nbest;
            }

            public Integer getWbest() {
                return wbest;
            }

            public void setWbest(Integer wbest) {
                this.wbest = wbest;
            }

            public Integer getPtt() {
                return ptt;
            }

            public void setPtt(Integer ptt) {
                this.ptt = ptt;
            }

            public Integer getSmth() {
                return smth;
            }

            public void setSmth(Integer smth) {
                this.smth = smth;
            }

            public Integer getNunum() {
                return nunum;
            }

            public void setNunum(Integer nunum) {
                this.nunum = nunum;
            }

            public Integer getOpt() {
                return opt;
            }

            public void setOpt(Integer opt) {
                this.opt = opt;
            }

            public Integer getLtc() {
                return ltc;
            }

            public void setLtc(Integer ltc) {
                this.ltc = ltc;
            }

            public String getDomain() {
                return domain;
            }


            public String getDhw() {
                return dhw;
            }

            public void setDhw(String dhw) {
                this.dhw = dhw;
            }

            public String getRlang() {
                return rlang;
            }

            public void setRlang(String rlang) {
                this.rlang = rlang;
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

            public String getAccent() {
                return accent;
            }

            public void setAccent(String accent) {
                this.accent = accent;
            }

            public int getEos() {
                return eos;
            }

            public void setEos(int eos) {
                this.eos = eos;
            }

            public int getVinfo() {
                return vinfo;
            }

            public void setVinfo(int vinfo) {
                this.vinfo = vinfo;
            }

            public String getDwa() {
                return dwa;
            }

            public void setDwa(String dwa) {
                this.dwa = dwa;
            }

            public Result getResult() {
                return result;
            }

            public void setResult(Result result) {
                this.result = result;
            }

            public static class Result {
                /**
                 * encoding : utf8
                 * compress : raw
                 * format : json
                 */

                private String encoding;
                private String compress;
                private String format;

                public Result(SparkIatClient client) {
                    this.encoding = client.getTextEncoding();
                    this.compress = client.getTextCompress();
                    this.format = client.getTextFormat();
                }

                public Result() {
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
    }

    public static class Payload {
        /**
         * audio : {"encoding":"raw","sample_rate":16000,"channels":1,"bit_depth":16,"seq":1,"status":0,"audio":"AAAAAP..."}
         */

        private Audio audio;

        public Payload(SparkIatClient client) {
            this.audio = new Audio(client);
        }

        public Payload() {
        }

        public Audio getAudio() {
            return audio;
        }

        public void setAudio(Audio audio) {
            this.audio = audio;
        }

        public static class Audio {
            /**
             * encoding : raw
             * sample_rate : 16000
             * channels : 1
             * bit_depth : 16
             * seq : 1
             * status : 0
             * audio : AAAAAP...
             */

            private String encoding;
            @SerializedName("sample_rate")
            private int sampleRate;
            private int channels;
            @SerializedName("bit_depth")
            private int bitDepth;
            private int seq;
            private int status;
            private String audio;

            public Audio(SparkIatClient client) {
                this.encoding = client.getEncoding();
                this.sampleRate = client.getSampleRate();
                this.channels = client.getChannels();
                this.bitDepth = client.getBitDepth();
            }

            public Audio() {
            }

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

            public int getSeq() {
                return seq;
            }

            public void setSeq(int seq) {
                this.seq = seq;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getAudio() {
                return audio;
            }

            public void setAudio(String audio) {
                this.audio = audio;
            }
        }
    }
}
