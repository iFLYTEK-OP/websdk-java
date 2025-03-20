package cn.xfyun.model.finetuning.request;

import cn.xfyun.api.FTTClient;
import cn.xfyun.model.RoleContent;

import java.util.List;

/**
 * @description: 精调文本大模型请求实体类
 * @author: zyding6
 * @create: 2025/3/17 15:56
 **/
public class FTTReqeust {


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

        private String app_id;
        private String uid;
        private List<String> patch_id;

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public List<String> getPatch_id() {
            return patch_id;
        }

        public void setPatch_id(List<String> patch_id) {
            this.patch_id = patch_id;
        }
    }

    public static class Parameter {
        /**
         * chat : {"domain":"patch","temperature":0.5,"top_k":4,"max_tokens":2048,"auditing":"default","chat_id":"xxx"}
         */

        private Chat chat;
        
        public Parameter(FTTClient chat) {
            this.chat = new Chat();
            this.chat.domain = chat.getDomain();
            this.chat.temperature = chat.getTemperature();
            this.chat.top_k = chat.getTopK();
            this.chat.max_tokens = chat.getMaxTokens();
            this.chat.auditing = chat.getAuditing();
            this.chat.chat_id = chat.getChatId();
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
            private int top_k;
            private int max_tokens;
            private String auditing;
            private String chat_id;

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

            public int getTop_k() {
                return top_k;
            }

            public void setTop_k(int top_k) {
                this.top_k = top_k;
            }

            public int getMax_tokens() {
                return max_tokens;
            }

            public void setMax_tokens(int max_tokens) {
                this.max_tokens = max_tokens;
            }

            public String getAuditing() {
                return auditing;
            }

            public void setAuditing(String auditing) {
                this.auditing = auditing;
            }

            public String getChat_id() {
                return chat_id;
            }

            public void setChat_id(String chat_id) {
                this.chat_id = chat_id;
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
