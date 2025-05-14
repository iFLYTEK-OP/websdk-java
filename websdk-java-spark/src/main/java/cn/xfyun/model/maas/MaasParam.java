package cn.xfyun.model.maas;

import cn.xfyun.model.sparkmodel.RoleContent;

import java.util.List;
import java.util.Map;

/**
 * maas精调大模型会话请求实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class MaasParam {

    /**
     * 对话记录和当前问题列表集合
     */
    private List<RoleContent> messages;

    /**
     * 拓展的会话Id , 保障用户会话的唯一性
     * 仅多语种大模型联动返回
     */
    private String chatId;

    /**
     * 用户的唯一id，表示一个用户，user_123456
     */
    private String userId;

    /**
     * 额外的请求头信息
     * 默认值为{"lora_id": "0"} : 通过传递 lora_id 加载特定的LoRA模型
     */
    private Map<String, Object> extraHeaders;

    /**
     * 额外的请求体信息
     * 仅DeepSeek-R1和DeepSeek-V3支持该功能。
     */
    private Map<String, Object> extraBody;

    public MaasParam(Builder builder) {
        this.messages = builder.messages;
        this.chatId = builder.chatId;
        this.userId = builder.userId;
        this.extraHeaders = builder.extraHeaders;
        this.extraBody = builder.extraBody;
    }

    public MaasParam() {
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<RoleContent> getMessages() {
        return messages;
    }

    public void setMessages(List<RoleContent> messages) {
        this.messages = messages;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<String, Object> getExtraHeaders() {
        return extraHeaders;
    }

    public void setExtraHeaders(Map<String, Object> extraHeaders) {
        this.extraHeaders = extraHeaders;
    }

    public Map<String, Object> getExtraBody() {
        return extraBody;
    }

    public void setExtraBody(Map<String, Object> extraBody) {
        this.extraBody = extraBody;
    }

    public static final class Builder {

        private List<RoleContent> messages;
        private String chatId;
        private String userId;
        private Map<String, Object> extraHeaders;
        private Map<String, Object> extraBody;

        private Builder() {
        }

        public MaasParam build() {
            return new MaasParam(this);
        }

        public Builder messages(List<RoleContent> messages) {
            this.messages = messages;
            return this;
        }

        public Builder chatId(String chatId) {
            this.chatId = chatId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder extraHeaders(Map<String, Object> extraHeaders) {
            this.extraHeaders = extraHeaders;
            return this;
        }

        public Builder extraBody(Map<String, Object> extraBody) {
            this.extraBody = extraBody;
            return this;
        }
    }
}
