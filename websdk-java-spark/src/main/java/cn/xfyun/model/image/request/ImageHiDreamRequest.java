package cn.xfyun.model.image.request;

import cn.xfyun.api.ImageGenHidreamClient;
import com.google.gson.annotations.SerializedName;

/**
 * @program: websdk-java
 * @description: 图片生成HiDream请求类
 * @author: zyding6
 * @create: 2025/3/19 15:08
 **/
public class ImageHiDreamRequest {


    /**
     * header : {"app_id":"xxxxxx","status":3,"channel":"default","callback_url":"default"}
     * parameter : {"oig":{"result":{"encoding":"utf8","compress":"raw","format":"json"}}}
     * payload : {"oig":{"encoding":"utf8","compress":"raw","format":"json","status":3,"text":"text"}}
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
         * app_id : xxxxxx
         * status : 3
         * channel : default
         * callback_url : default
         */

        @SerializedName("app_id")
        private String appId;
        private int status;
        private String channel;
        @SerializedName("callback_url")
        private String callbackUrl;

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

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getCallbackUrl() {
            return callbackUrl;
        }

        public void setCallbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
        }
    }

    public static class Parameter {
        /**
         * oig : {"result":{"encoding":"utf8","compress":"raw","format":"json"}}
         */

        private Oig oig;

        public Parameter(ImageGenHidreamClient client) {
            this.oig = new Oig(client);
        }

        public Parameter() {
        }

        public Oig getOig() {
            return oig;
        }

        public void setOig(Oig oig) {
            this.oig = oig;
        }

        public static class Oig {
            /**
             * result : {"encoding":"utf8","compress":"raw","format":"json"}
             */

            private Result result;

            public Oig(ImageGenHidreamClient client) {
                this.result = new Result(client);
            }

            public Oig() {
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

                public Result(ImageGenHidreamClient client) {
                    this.encoding = client.getEncoding();
                    this.compress = client.getCompress();
                    this.format = client.getFormat();
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
         * oig : {"encoding":"utf8","compress":"raw","format":"json","status":3,"text":"text"}
         */

        private Oig oig;

        public Payload(ImageGenHidreamClient client) {
            this.oig = new Oig(client);
        }

        public Payload() {
        }

        public Oig getOig() {
            return oig;
        }

        public void setOig(Oig oig) {
            this.oig = oig;
        }

        public static class Oig {
            /**
             * encoding : utf8
             * compress : raw
             * format : json
             * status : 3
             * text : text
             */

            private String encoding;
            private String compress;
            private String format;
            private int status;
            private String text;

            public Oig(ImageGenHidreamClient client) {
                this.encoding = client.getEncoding();
                this.compress = client.getCompress();
                this.format = client.getFormat();
                this.status = client.getStatus();
            }

            public Oig() {
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

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
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
