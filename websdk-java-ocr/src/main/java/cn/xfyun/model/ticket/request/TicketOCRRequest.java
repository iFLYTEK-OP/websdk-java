package cn.xfyun.model.ticket.request;

import cn.xfyun.api.TicketOCRClient;
import com.google.gson.annotations.SerializedName;

/**
 * 通用票证识别请求实体类
 *
 * @author zyding6
 **/
public class TicketOCRRequest {


    /**
     * header : {"app_id":"123456","status":3}
     * parameter : {"ocr":{"type":"air_itinerary","level":1,"result":{"encoding":"utf8","compress":"raw","format":"json"}}}
     * payload : {"image":{"encoding":"jpg","image":"MFApAT6cDaK······","status":3}}
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
         * status : 3
         */

        @SerializedName("app_id")
        private String appId;
        private Integer status;
        private String uid;

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

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }

    public static class Parameter {
        /**
         * ocr : {"type":"air_itinerary","level":1,"result":{"encoding":"utf8","compress":"raw","format":"json"}}
         */

        private Ocr ocr;

        public Parameter() {
        }

        public Parameter(TicketOCRClient client) {
            this.ocr = new Ocr(client);
        }

        public Ocr getOcr() {
            return ocr;
        }

        public void setOcr(Ocr ocr) {
            this.ocr = ocr;
        }

        public static class Ocr {
            /**
             * type : air_itinerary
             * level : 1
             * result : {"encoding":"utf8","compress":"raw","format":"json"}
             */

            private String type;
            private Integer level;
            private Result result;

            public Ocr() {
            }

            public Ocr(TicketOCRClient client) {
                this.result = new Result(client);
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Integer getLevel() {
                return level;
            }

            public void setLevel(Integer level) {
                this.level = level;
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

                public Result() {
                }

                public Result(TicketOCRClient client) {
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
         * image : {"encoding":"jpg","image":"MFApAT6cDaK······","status":3}
         */

        private Image image;

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public static class Image {
            /**
             * encoding : jpg
             * image : MFApAT6cDaK······
             * status : 3
             */

            private String encoding;
            private String image;
            private Integer status;

            public String getEncoding() {
                return encoding;
            }

            public void setEncoding(String encoding) {
                this.encoding = encoding;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
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
