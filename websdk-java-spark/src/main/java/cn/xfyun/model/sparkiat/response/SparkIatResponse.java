package cn.xfyun.model.sparkiat.response;

import java.util.List;

/**
 * @author zyding
 * <p>
 * 大模型语音听写返回参数
 */
public class SparkIatResponse {

    private Header header;
    private Payload payload;

    public SparkIatResponse() {
    }

    public SparkIatResponse(int code, String message) {
        this.header = new Header();
        this.header.code = code;
        this.header.message = message;
    }

    public Header getHeader() {
        return header;
    }

    public Payload getPayload() {
        return payload;
    }

    public static class Header {
        private int code;
        private String message;
        private String sid;
        private int status;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class Payload {
        private Result result;

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }
    }

    public static class Result {
        private String text;
        private int status;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class JsonParseText {
        private Integer sn;
        private List<Ws> ws;
        private String pgs;
        private String rst;
        private List<Integer> rg;

        public String getRst() {
            return rst;
        }

        public void setRst(String rst) {
            this.rst = rst;
        }

        public Integer getSn() {
            return sn;
        }

        public void setSn(Integer sn) {
            this.sn = sn;
        }

        public List<Ws> getWs() {
            return ws;
        }

        public void setWs(List<Ws> ws) {
            this.ws = ws;
        }

        public String getPgs() {
            return pgs;
        }

        public void setPgs(String pgs) {
            this.pgs = pgs;
        }

        public List<Integer> getRg() {
            return rg;
        }

        public void setRg(List<Integer> rg) {
            this.rg = rg;
        }
    }

    public static class Ws {
        private List<Cw> cw;

        public List<Cw> getCw() {
            return cw;
        }

        public void setCw(List<Cw> cw) {
            this.cw = cw;
        }
    }

    public static class Cw {
        private String w;

        public String getW() {
            return w;
        }

        public void setW(String w) {
            this.w = w;
        }
    }
}
