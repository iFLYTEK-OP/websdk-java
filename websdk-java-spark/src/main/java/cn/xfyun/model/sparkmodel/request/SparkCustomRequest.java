package cn.xfyun.model.sparkmodel.request;

import cn.xfyun.api.SparkCustomClient;
import cn.xfyun.model.sparkmodel.FileContent;
import cn.xfyun.model.sparkmodel.FunctionCall;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 星火定制化大模型会话请求实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class SparkCustomRequest {


    /**
     * header : {"app_id":"这里填写应用appid，从开放平台控制台创建的应用中获取","uid":"123456"}
     * parameter : {"chat":{"domain":"max","temperature":0.5,"top_k":4,"max_tokens":8192}}
     * payload : {"message":{"text":[{"role":"user","content":[{"type":"file","file":["file_id1","file_id2"]},{"type":"text","text":"请总结两篇文章的不同点"}]},{"role":"assistant","content":"两篇文章有几个不同点......"},{"role":"user","content":[{"type":"file","file":["file_id3"]},{"type":"text","text":"总结下这篇文章"}]}]}}
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
         * app_id : 这里填写应用appid，从开放平台控制台创建的应用中获取
         * uid : 123456
         */

        @SerializedName("app_id")
        private String appId;
        private String uid;

        public Header() {
        }

        public Header(String appId, String uid) {
            this.appId = appId;
            this.uid = uid;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
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
         * chat : {"domain":"max","temperature":0.5,"top_k":4,"max_tokens":8192}
         */

        private Chat chat;

        public Parameter() {
        }

        public Parameter(SparkCustomClient client) {
            this.chat = new Chat(client);
        }

        public Chat getChat() {
            return chat;
        }

        public void setChat(Chat chat) {
            this.chat = chat;
        }

        public static class Chat {
            /**
             * domain : max
             * temperature : 0.5
             * top_k : 4
             * max_tokens : 8192
             */

            private String domain;
            private Float temperature;
            @SerializedName("top_k")
            private Integer topK;
            @SerializedName("max_tokens")
            private Integer maxTokens;

            public Chat() {
            }

            public Chat(SparkCustomClient client) {
                this.domain = client.getDomain();
                this.temperature = client.getTemperature();
                this.topK = client.getTopK();
                this.maxTokens = client.getMaxTokens();
            }

            public String getDomain() {
                return domain;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public Float getTemperature() {
                return temperature;
            }

            public void setTemperature(Float temperature) {
                this.temperature = temperature;
            }

            public Integer getTopK() {
                return topK;
            }

            public void setTopK(Integer topK) {
                this.topK = topK;
            }

            public Integer getMaxTokens() {
                return maxTokens;
            }

            public void setMaxTokens(Integer maxTokens) {
                this.maxTokens = maxTokens;
            }
        }
    }

    public static class Payload {
        /**
         * message : {"text":[{"role":"user","content":[{"type":"file","file":["file_id1","file_id2"],"text":"请总结两篇文章的不同点"},{"type":"text","text":"请总结两篇文章的不同点"}]},{"role":"assistant","content":"两篇文章有几个不同点......"},{"role":"user","content":[{"type":"file","file":["file_id3"]},{"type":"text","text":"总结下这篇文章"}]}]}
         */

        private Message message;
        private Function functions;

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        public Function getFunctions() {
            return functions;
        }

        public void setFunctions(Function functions) {
            this.functions = functions;
        }

        public static class Message {
            private List<FileContent> text;

            public List<FileContent> getText() {
                return text;
            }

            public void setText(List<FileContent> text) {
                this.text = text;
            }
        }

        public static class Function {
            private List<FunctionCall> text;

            public List<FunctionCall> getText() {
                return text;
            }

            public void setText(List<FunctionCall> text) {
                this.text = text;
            }
        }
    }
}
