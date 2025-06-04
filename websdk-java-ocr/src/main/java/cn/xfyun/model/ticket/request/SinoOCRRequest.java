package cn.xfyun.model.ticket.request;

import cn.xfyun.api.SinoOCRClient;
import com.google.gson.annotations.SerializedName;

/**
 * 国内通用票证识别(sinosecu)请求实体类
 *
 * @author zyding6
 **/
public class SinoOCRRequest {


    /**
     * header : {"app_id":"your_appid","status":3}
     * parameter : {"image_recognize":{"output_text_result":{"encoding":"utf8","compress":"raw","format":"plain"}}}
     * payload : {"image":{"encoding":"jpg","image":"iVBORw0KGgo......","status":3}}
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
         * image_recognize : {"output_text_result":{"encoding":"utf8","compress":"raw","format":"plain"}}
         */

        @SerializedName("image_recognize")
        private ImageRecognize imageRecognize;

        public Parameter() {
        }

        public Parameter(SinoOCRClient client) {
            this.imageRecognize = new ImageRecognize(client);
        }

        public ImageRecognize getImageRecognize() {
            return imageRecognize;
        }

        public void setImageRecognize(ImageRecognize imageRecognize) {
            this.imageRecognize = imageRecognize;
        }

        public static class ImageRecognize {
            /**
             * output_text_result : {"encoding":"utf8","compress":"raw","format":"plain"}
             */

            @SerializedName("output_text_result")
            private OutputTextResult outputTextResult;

            public ImageRecognize() {
            }

            public ImageRecognize(SinoOCRClient client) {
                this.outputTextResult = new OutputTextResult(client);
            }

            public OutputTextResult getOutputTextResult() {
                return outputTextResult;
            }

            public void setOutputTextResult(OutputTextResult outputTextResult) {
                this.outputTextResult = outputTextResult;
            }

            public static class OutputTextResult {
                /**
                 * encoding : utf8
                 * compress : raw
                 * format : plain
                 */

                private String encoding;
                private String compress;
                private String format;

                public OutputTextResult() {
                }

                public OutputTextResult(SinoOCRClient client) {
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
         * image : {"encoding":"jpg","image":"iVBORw0KGgo......","status":3}
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
             * image : iVBORw0KGgo......
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
