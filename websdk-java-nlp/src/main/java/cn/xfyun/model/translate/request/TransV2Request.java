package cn.xfyun.model.translate.request;

import com.google.gson.annotations.SerializedName;

/**
 * 新版本翻译请求实体类
 *
 * @author <zyding6@ifytek.com>
 */
public class TransV2Request {

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
        private int status;
        @SerializedName("res_id")
        private String resId;

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

        public String getResId() {
            return resId;
        }

        public void setResId(String resId) {
            this.resId = resId;
        }
    }

    public static class Parameter {

        private Its its;

        public Its getIts() {
            return its;
        }

        public void setIts(Its its) {
            this.its = its;
        }

        public static class Its {

            private String from;
            private String to;
            private Object result;

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public String getTo() {
                return to;
            }

            public void setTo(String to) {
                this.to = to;
            }

            public Object getResult() {
                return result;
            }

            public void setResult(Object result) {
                this.result = result;
            }
        }
    }

    public static class Payload {

        @SerializedName("input_data")
        private InputData inputData;

        public InputData getInputData() {
            return inputData;
        }

        public void setInputData(InputData inputData) {
            this.inputData = inputData;
        }

        public static class InputData {

            private String encoding;
            private Integer status;
            private String text;

            public String getEncoding() {
                return encoding;
            }

            public void setEncoding(String encoding) {
                this.encoding = encoding;
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
