package cn.xfyun.model.sparkmodel;

import java.util.List;
import java.util.Map;

/**
 * 大模型会话请求实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class SparkChatParam {

    /**
     * 当前工具列表中，仅支持联网搜索工具 可选: web_search
     * web_search 仅Pro、Max、Ultra系列模型支持
     */
    private WebSearch webSearch;

    /**
     * 支持大模型在交互过程中识别出需要调度的外部接口
     * 触发了function_call的情况下，只会返回一帧结果，其中status 为2
     * 仅Spark Max/4.0 Ultra 支持了该功能
     */
    private List<FunctionCall> functions;

    /**
     * 对话记录和当前问题列表集合
     * 所有content的累计tokens长度，不同版本限制不同：
     * Lite、Pro、Max、4.0 Ultra版本: 不超过8192;
     * Max-32K版本: 不超过32* 1024;
     * Pro-128K版本:不超过 128*1024;
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
     * 用于控制深度思考模式
     * 支持以下3种模式切换：
     * enabled：强制开启深度思考能力
     * disabled：强制关闭深度思考能力
     * auto：模型自行判断是否进行深度思考
     */
    private String thinkingType;

    /**
     * 额外参数
     */
    private Map<String, Object> extraBody;

    public SparkChatParam(Builder builder) {
        this.webSearch = builder.webSearch;
        this.functions = builder.functions;
        this.messages = builder.messages;
        this.chatId = builder.chatId;
        this.userId = builder.userId;
        this.thinkingType = builder.thinkingType;
        this.extraBody = builder.extraBody;
    }

    public SparkChatParam() {
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public WebSearch getWebSearch() {
        return webSearch;
    }

    public void setWebSearch(WebSearch webSearch) {
        this.webSearch = webSearch;
    }

    public List<FunctionCall> getFunctions() {
        return functions;
    }

    public void setFunctions(List<FunctionCall> functions) {
        this.functions = functions;
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

    public String getThinkingType() {
        return thinkingType;
    }

    public void setThinkingType(String thinkingType) {
        this.thinkingType = thinkingType;
    }

    public Map<String, Object> getExtraBody() {
        return extraBody;
    }

    public void setExtraBody(Map<String, Object> extraBody) {
        this.extraBody = extraBody;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private WebSearch webSearch;
        private List<FunctionCall> functions;
        private List<RoleContent> messages;
        private String chatId;
        private String userId;
        private String thinkingType;
        private Map<String, Object> extraBody;

        private Builder() {
        }

        public SparkChatParam build() {
            return new SparkChatParam(this);
        }

        public Builder webSearch(WebSearch webSearch) {
            this.webSearch = webSearch;
            return this;
        }

        public Builder functions(List<FunctionCall> functions) {
            this.functions = functions;
            return this;
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

        public Builder thinkingType(String thinkingType) {
            this.thinkingType = thinkingType;
            return this;
        }

        public Builder extraBody(Map<String, Object> extraBody) {
            this.extraBody = extraBody;
            return this;
        }
    }
}
