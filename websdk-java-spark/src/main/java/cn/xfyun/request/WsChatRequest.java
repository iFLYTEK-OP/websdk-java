package cn.xfyun.request;


import cn.xfyun.model.RoleMessage;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: rblu2
 * @create: 2025-02-19 17:01
 **/

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WsChatRequest {
    private Header header;
    private Parameter parameter;
    private Payload payload;


    public static WsChatRequest create(String appId, String domain) {
        WsChatRequest request = new WsChatRequest();
        Header header = new Header().app_id(appId);
        Parameter parameter = new Parameter().chat(new Parameter.Chat().domain(domain));
        Payload payload = new Payload().message(new Payload.Message());
        request.header(header).parameter(parameter).payload(payload);
        return request;
    }

    public Header getHeader() {
        return header;
    }

    public WsChatRequest header(Header header) {
        this.header = header;
        return this;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public WsChatRequest parameter(Parameter parameter) {
        this.parameter = parameter;
        return this;
    }

    public Payload getPayload() {
        return payload;
    }

    public WsChatRequest payload(Payload payload) {
        this.payload = payload;
        return this;
    }

    public static class Header {
        private String app_id;
        private String uid;

        public String getApp_id() {
            return app_id;
        }

        public Header app_id(String app_id) {
            this.app_id = app_id;
            return this;
        }

        public String getUid() {
            return uid;
        }

        public Header uid(String uid) {
            this.uid = uid;
            return this;
        }
    }

    public static class Parameter {
        private Chat chat;

        public Chat getChat() {
            return chat;
        }

        public Parameter chat(Chat chat) {
            this.chat = chat;
            return this;
        }

        public static class Chat {
            private String domain;
            private Float temperature = 0.5f;
            private Integer max_tokens = 4096;

            private Integer top_k = 4;

            private Boolean show_ref_label = false;

            public String getDomain() {
                return domain;
            }

            public Chat domain(String domain) {
                this.domain = domain;
                return this;
            }

            public Float getTemperature() {
                return temperature;
            }

            public Chat temperature(Float temperature) {
                this.temperature = temperature;
                return this;
            }

            public Integer getMax_tokens() {
                return max_tokens;
            }

            public Chat max_tokens(Integer max_tokens) {
                this.max_tokens = max_tokens;
                return this;
            }

            public Integer getTop_k() {
                return top_k;
            }

            public Chat top_k(Integer top_k) {
                this.top_k = top_k;
                return this;
            }

            public Boolean getShow_ref_label() {
                return show_ref_label;
            }

            public Chat show_ref_label(Boolean show_ref_label) {
                this.show_ref_label = show_ref_label;
                return this;
            }
        }

    }

    public static class Payload {
        private Message message;

        public Message getMessage() {
            return message;
        }

        public Payload message(Message message) {
            this.message = message;
            return this;
        }

        public static class Message {
            private List<RoleMessage> text = new ArrayList<>();

            public List<RoleMessage> getText() {
                return text;
            }

            public Message text(List<RoleMessage> text) {
                this.text = text;
                return this;
            }
        }
    }
}
