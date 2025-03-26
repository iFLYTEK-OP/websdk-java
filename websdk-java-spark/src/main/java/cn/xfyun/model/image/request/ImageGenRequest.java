package cn.xfyun.model.image.request;

import cn.xfyun.model.RoleContent;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @description: 图片生成请求参数实体类
 * @author: zyding6
 * @create: 2025/3/19 13:53
 **/
public class ImageGenRequest {


    /**
     * header : {"app_id":"your_appid"}
     * parameter : {"chat":{"domain":"general","width":512,"height":512}}
     * payload : {"message":{"text":[{"role":"user","content":"帮我画一座山"}]}}
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
         * app_id : your_appid
         */

        @SerializedName("app_id")
        private String appId;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }
    }

    public static class Parameter {
        /**
         * chat : {"domain":"general","width":512,"height":512}
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
             * domain : general
             * width : 512
             * height : 512
             */

            private String domain;
            private int width;
            private int height;

            public String getDomain() {
                return domain;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }
    }

    public static class Payload {
        /**
         * message : {"text":[{"role":"user","content":"帮我画一座山"}]}
         */

        private Message message;

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
