package cn.xfyun.model.document.response;

import cn.xfyun.model.common.response.ResponseHeader;

/**
 * @program: websdk-java
 * @description: 图片转文档返回响应类
 * @author: zyding6
 * @create: 2025/3/25 9:42
 **/
public class PDRecResponse {

    private ResponseHeader header;
    private Payload payload;

    public PDRecResponse(int i, String error) {
        ResponseHeader header = new ResponseHeader();
        header.setCode(i);
        header.setMessage(error);
        this.header = header;
    }

    public ResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public static class Payload {

        private Result result;

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }

        public static class Result {

            private String format;
            private String encoding;
            private String text;
            private String compress;

            public String getFormat() {
                return format;
            }

            public void setFormat(String format) {
                this.format = format;
            }

            public String getEncoding() {
                return encoding;
            }

            public void setEncoding(String encoding) {
                this.encoding = encoding;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getCompress() {
                return compress;
            }

            public void setCompress(String compress) {
                this.compress = compress;
            }
        }
    }
}
