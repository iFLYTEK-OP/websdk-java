package cn.xfyun.model.resume;

import cn.xfyun.api.ResumeGenClient;
import com.google.gson.annotations.SerializedName;

/**
 * @description: 简历解析请求参数
 * @author: zyding6
 * @create: 2025/3/17 11:17
 **/
public class ResumeRequest {

    /**
     * header : {"app_id":"appid","status":3}
     * parameter : {"ai_resume":{"resData":{"encoding":"utf8","compress":"raw","format":"json"}}}
     * payload : {"reqData":{"encoding":"utf8","compress":"raw","format":"plain","status":3,"text":"123"}}
     */

    private HeaderBean header;
    private ParameterBean parameter;
    private PayloadBean payload;

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public ParameterBean getParameter() {
        return parameter;
    }

    public void setParameter(ParameterBean parameter) {
        this.parameter = parameter;
    }

    public PayloadBean getPayload() {
        return payload;
    }

    public void setPayload(PayloadBean payload) {
        this.payload = payload;
    }

    public static class HeaderBean {
        /**
         * app_id : appid
         * status : 3
         */

        @SerializedName("app_id")
        private String appId;
        private int status;

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

    public static class ParameterBean {
        /**
         * ai_resume : {"resData":{"encoding":"utf8","compress":"raw","format":"json"}}
         */

        @SerializedName("ai_resume")
        private AiResumeBean aiResume;

        public ParameterBean(ResumeGenClient client) {
            this.aiResume = new AiResumeBean();
            this.aiResume.resData = new AiResumeBean.ResDataBean();
            this.aiResume.resData.compress = client.getCompress();
            this.aiResume.resData.format = client.getFormat();
            this.aiResume.resData.encoding = client.getEncoding();
        }

        public AiResumeBean getAiResume() {
            return aiResume;
        }

        public void setAiResume(AiResumeBean aiResume) {
            this.aiResume = aiResume;
        }

        public static class AiResumeBean {
            /**
             * resData : {"encoding":"utf8","compress":"raw","format":"json"}
             */

            private ResDataBean resData;

            public ResDataBean getResData() {
                return resData;
            }

            public void setResData(ResDataBean resData) {
                this.resData = resData;
            }

            public static class ResDataBean {
                /**
                 * encoding : utf8
                 * compress : raw
                 * format : json
                 */

                private String encoding;
                private String compress;
                private String format;

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

    public static class PayloadBean {
        /**
         * reqData : {"encoding":"utf8","compress":"raw","format":"plain","status":3,"text":"123"}
         */

        private ReqDataBean reqData;

        public PayloadBean(ResumeGenClient client) {
            this.reqData = new ReqDataBean();
            this.reqData.encoding = client.getEncoding();
            this.reqData.compress = client.getCompress();
            this.reqData.format = client.getFormat();
        }

        public ReqDataBean getReqData() {
            return reqData;
        }

        public void setReqData(ReqDataBean reqData) {
            this.reqData = reqData;
        }

        public static class ReqDataBean {
            /**
             * encoding : utf8
             * compress : raw
             * format : plain
             * status : 3
             * text : 123
             */

            private String encoding;
            private String compress;
            private String format;
            private int status;
            private String text;

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
