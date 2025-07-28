package cn.xfyun.model.agent;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sparkmodel.RoleContent;
import cn.xfyun.util.StringUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

/**
 * 智能体参数
 *
 * @author <zyding6@ifytek.com>
 **/
public class AgentChatParam {

    /**
     * 工作流 id
     */
    @SerializedName("flow_id")
    private String flowId;

    /**
     * 用户 id
     */
    private String uid;

    /**
     * 是否启用流式返回 流式：true 非流式：false
     */
    private Boolean stream;

    /**
     * 用于指定一些额外字段，比如一些插件隐藏字段
     */
    private Object ext;

    /**
     * 工作流开始节点的输入参数及取值，你可以在指定工作流的编排页面查看参数列表
     */
    private Object parameters;

    /**
     * 会话 id，用于区分不同的工作流会话，长度不超过 32 位
     */
    @SerializedName("chat_id")
    private String chatId;

    /**
     * 历史对话信息[history_message object]集合
     */
    private List<RoleContent> history;

    public AgentChatParam(Builder builder) {
        this.flowId = builder.flowId;
        this.uid = builder.uid;
        this.stream = builder.stream;
        this.ext = builder.ext;
        this.parameters = builder.parameters;
        this.chatId = builder.chatId;
        this.history = builder.history;
    }

    public AgentChatParam() {
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<RoleContent> getHistory() {
        return history;
    }

    public void setHistory(List<RoleContent> history) {
        this.history = history;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public void selfCheck() {
        if (StringUtils.isNullOrEmpty(flowId)) {
            throw new BusinessException("flowId不能为空");
        }

        if (Objects.isNull(parameters)) {
            throw new BusinessException("parameters参数不能为空");
        }
    }

    public static final class Builder {

        private String flowId;
        private String uid;
        private Boolean stream;
        private Object ext;
        private Object parameters;
        private String chatId;
        private List<RoleContent> history;

        private Builder() {
        }

        public AgentChatParam build() {
            return new AgentChatParam(this);
        }

        public Builder flowId(String flowId) {
            this.flowId = flowId;
            return this;
        }

        public Builder uid(String uid) {
            this.uid = uid;
            return this;
        }

        public Builder stream(boolean stream) {
            this.stream = stream;
            return this;
        }

        public Builder ext(Object ext) {
            this.ext = ext;
            return this;
        }

        public Builder parameters(Object parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder chatId(String chatId) {
            this.chatId = chatId;
            return this;
        }

        public Builder history(List<RoleContent> history) {
            this.history = history;
            return this;
        }
    }
}
