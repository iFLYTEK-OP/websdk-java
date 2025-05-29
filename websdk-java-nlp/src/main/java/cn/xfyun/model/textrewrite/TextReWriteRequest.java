package cn.xfyun.model.textrewrite;

import cn.xfyun.api.TextRewriteClient;
import com.google.gson.annotations.SerializedName;

/**
 * 文本改写请求实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class TextReWriteRequest {

    /**
     * header : {"app_id":"your_app_id","status":3}
     * parameter : {"se3acbe7f":{"level":"<L1>","result":{"encoding":"utf8","compress":"raw","format":"json"}}}
     * payload : {"input1":{"encoding":"utf8","compress":"raw","format":"plain","status":3,"text":"5aSq6Ziz5b2T......"}}
     */

    private Header header;
    private Payload payload;

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
        /**
         * app_id : your_app_id
         * status : 3
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

    public static class Payload {
        /**
         * input1 : {"encoding":"utf8","compress":"raw","format":"plain","status":3,"text":"5aSq6Ziz5b2T......"}
         */

        @SerializedName("input1")
        private Input input;

        public Payload(TextRewriteClient client) {
            this.input = new Input(client);
        }

        public Payload() {
        }

        public Input getInput() {
            return input;
        }

        public void setInput(Input input) {
            this.input = input;
        }

        public static class Input {
            /**
             * encoding : utf8
             * compress : raw
             * format : plain
             * status : 3
             * text : 5aSq6Ziz5b2T......
             */

            private String encoding;
            private String compress;
            private String format;
            private int status;
            private String text;

            public Input(TextRewriteClient client) {
                this.encoding = client.getEncoding();
                this.compress = client.getCompress();
                this.format = client.getFormat();
                this.status = client.getStatus();
            }

            public Input() {
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

    public static class ServiceModel {
        /**
         * level : <L1>
         * result : {"encoding":"utf8","compress":"raw","format":"json"}
         */

        private String level;
        private Result result;

        public ServiceModel() {
        }

        public ServiceModel(TextRewriteClient client) {
            this.result = new Result(client);
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
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

            public Result(TextRewriteClient client) {
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
