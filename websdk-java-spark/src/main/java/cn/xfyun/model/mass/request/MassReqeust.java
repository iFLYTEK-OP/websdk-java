package cn.xfyun.model.mass.request;

import cn.xfyun.api.MassClient;
import cn.xfyun.model.sparkmodel.RoleContent;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 精调文本大模型请求实体类
 *
 * @author zyding6
 **/
public class MassReqeust {


    /**
     * header : {"app_id":"123456","uid":"39769795890","patch_id":["xxx"]}
     * parameter : {"chat":{"domain":"patch","temperature":0.5,"top_k":4,"max_tokens":2048,"auditing":"default","chat_id":"xxx"}}
     * payload : {"message":{"text":[{"role":"system","content":"你是星火认知大模型"},{"role":"user","content":"今天的天气"}]}}
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
         * uid : 39769795890
         * patch_id : ["xxx"]
         */

        @SerializedName("app_id")
        private String appId;
        private String uid;
        @SerializedName("patch_id")
        private List<String> patchId;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public List<String> getPatchId() {
            return patchId;
        }

        public void setPatchId(List<String> patchId) {
            this.patchId = patchId;
        }
    }

    public static class Parameter {
        /**
         * chat : {"domain":"patch","temperature":0.5,"top_k":4,"max_tokens":2048,"auditing":"default","chat_id":"xxx"}
         */

        private Chat chat;

        public Parameter(MassClient chat) {
            this.chat = new Chat();
            this.chat.domain = chat.getDomain();
            this.chat.temperature = chat.getTemperature();
            this.chat.topK = chat.getTopK();
            this.chat.maxTokens = chat.getMaxTokens();
            this.chat.auditing = chat.getAuditing();
        }

        public Chat getChat() {
            return chat;
        }

        public void setChat(Chat chat) {
            this.chat = chat;
        }

        public static class Chat {
            /**
             * domain : patch
             * temperature : 0.5
             * top_k : 4
             * max_tokens : 2048
             * auditing : default
             * chat_id : xxx
             */

            private String domain;
            private double temperature;
            @SerializedName("top_k")
            private int topK;
            @SerializedName("max_tokens")
            private int maxTokens;
            private String auditing;
            @SerializedName("chat_id")
            private String chatId;
            @SerializedName("search_disable")
            private Boolean searchDisable;
            @SerializedName("show_ref_label")
            private Boolean showRefLabel;

            public String getDomain() {
                return domain;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public double getTemperature() {
                return temperature;
            }

            public void setTemperature(double temperature) {
                this.temperature = temperature;
            }

            public int getTopK() {
                return topK;
            }

            public void setTopK(int topK) {
                this.topK = topK;
            }

            public int getMaxTokens() {
                return maxTokens;
            }

            public void setMaxTokens(int maxTokens) {
                this.maxTokens = maxTokens;
            }

            public String getAuditing() {
                return auditing;
            }

            public void setAuditing(String auditing) {
                this.auditing = auditing;
            }

            public String getChatId() {
                return chatId;
            }

            public void setChatId(String chatId) {
                this.chatId = chatId;
            }

            public Boolean getSearchDisable() {
                return searchDisable;
            }

            public void setSearchDisable(Boolean searchDisable) {
                this.searchDisable = searchDisable;
            }

            public Boolean getShowRefLabel() {
                return showRefLabel;
            }

            public void setShowRefLabel(Boolean showRefLabel) {
                this.showRefLabel = showRefLabel;
            }
        }
    }

    public static class Payload {
        /**
         * message : {"text":[{"role":"system","content":"你是星火认知大模型"},{"role":"user","content":"今天的天气"}]}
         */

        private Message message;

        public Payload() {
            this.message = new Message();
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
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
    }
}
