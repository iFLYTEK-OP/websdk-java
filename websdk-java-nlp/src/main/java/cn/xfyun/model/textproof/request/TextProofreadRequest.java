package cn.xfyun.model.textproof.request;

import cn.xfyun.api.TextProofreadClient;
import com.google.gson.annotations.SerializedName;

/**
 * 文本校对请求实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class TextProofreadRequest {


    /**
     * header : {"app_id":"your_appid","status":3}
     * parameter : {"midu_correct":{"output_result":{"encoding":"utf8","compress":"raw","format":"json"}}}
     * payload : {"text":{"encoding":"utf8","compress":"raw","format":"plain","status":3,"text":"56ys5LqM5Liq55m+5bm055uu5qCH"}}
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
         * status : 3
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
         * midu_correct : {"output_result":{"encoding":"utf8","compress":"raw","format":"json"}}
         */

        @SerializedName("midu_correct")
        private MiduCorrect miduCorrect;

        public Parameter() {
        }

        public Parameter(TextProofreadClient client) {
            this.miduCorrect = new MiduCorrect(client);
        }

        public MiduCorrect getMiduCorrect() {
            return miduCorrect;
        }

        public void setMiduCorrect(MiduCorrect miduCorrect) {
            this.miduCorrect = miduCorrect;
        }

        public static class MiduCorrect {
            /**
             * output_result : {"encoding":"utf8","compress":"raw","format":"json"}
             */

            @SerializedName("output_result")
            private OutputResult outputResult;

            public MiduCorrect() {
            }

            public MiduCorrect(TextProofreadClient client) {
                this.outputResult = new OutputResult(client);
            }

            public OutputResult getOutputResult() {
                return outputResult;
            }

            public void setOutputResult(OutputResult outputResult) {
                this.outputResult = outputResult;
            }

            public static class OutputResult {
                /**
                 * encoding : utf8
                 * compress : raw
                 * format : json
                 */

                private String encoding;
                private String compress;
                private String format;

                public OutputResult() {
                }

                public OutputResult(TextProofreadClient client) {
                    this.encoding = client.getEncoding();
                    this.compress = client.getCompress();
                    this.format = client.getFormat();
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
         * text : {"encoding":"utf8","compress":"raw","format":"plain","status":3,"text":"56ys5LqM5Liq55m+5bm055uu5qCH"}
         */

        private Text text;

        public Payload() {
        }

        public Payload(TextProofreadClient client) {
            this.text = new Text(client);
        }

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
             * status : 3
             * text : 56ys5LqM5Liq55m+5bm055uu5qCH
             */

            private String encoding;
            private String compress;
            private String format;
            private Integer status;
            private String text;

            public Text() {
            }

            public Text(TextProofreadClient client) {
                this.encoding = client.getEncoding();
                this.compress = client.getCompress();
                this.format = client.getFormat();
                this.encoding = client.getEncoding();
                this.status = client.getStatus();
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
        }
    }
}
