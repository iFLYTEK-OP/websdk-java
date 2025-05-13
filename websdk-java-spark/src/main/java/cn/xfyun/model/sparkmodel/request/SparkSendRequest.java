package cn.xfyun.model.sparkmodel.request;

import cn.xfyun.model.sparkmodel.RoleContent;
import cn.xfyun.model.sparkmodel.FunctionCall;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 大模型ws请求实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class SparkSendRequest {


    /**
     * header : {"app_id":"12345","uid":"12345"}
     * parameter : {"chat":{"domain":"generalv3.5","temperature":0.5,"max_tokens":1024}}
     * payload : {"message":{"text":[]}}
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
         * app_id : 12345
         * uid : 12345
         */

        @SerializedName("app_id")
        private String appId;
        private String uid;

        public Header(String appId, String uuid) {
            this.appId = appId;
            this.uid = uuid;
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
         * chat : {"domain":"generalv3.5","temperature":0.5,"max_tokens":1024}
         */

        private Chat chat;

        public Chat getChat() {
            return chat;
        }

        public void setChat(Chat chat) {
            this.chat = chat;
        }

        public static class Chat {
            /**
             * domain : generalv3.5
             * temperature : 0.5
             * max_tokens : 1024
             * top_k : 4
             */

            private String domain;
            private Float temperature;
            @SerializedName("max_tokens")
            private Integer maxTokens;
            @SerializedName("top_k")
            private Integer topK;
            private List<Object> tools;
            @SerializedName("chat_id")
            private String chatId;

            public String getChatId() {
                return chatId;
            }

            public void setChatId(String chatId) {
                this.chatId = chatId;
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

            public Integer getMaxTokens() {
                return maxTokens;
            }

            public void setMaxTokens(Integer maxTokens) {
                this.maxTokens = maxTokens;
            }

            public Integer getTopK() {
                return topK;
            }

            public void setTopK(Integer topK) {
                this.topK = topK;
            }

            public List<Object> getTools() {
                return tools;
            }

            public void setTools(List<Object> tools) {
                this.tools = tools;
            }
        }
    }

    public static class Payload {
        /**
         * message : {"text":[]}
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
            private List<RoleContent> text;

            public List<RoleContent> getText() {
                return text;
            }

            public void setText(List<RoleContent> text) {
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
