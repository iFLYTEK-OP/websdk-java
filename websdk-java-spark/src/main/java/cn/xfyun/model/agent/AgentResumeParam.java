package cn.xfyun.model.agent;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.StringUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

/**
 * 智能体恢复参数
 *
 * @author <zyding6@ifytek.com>
 **/
public class AgentResumeParam {

    /**
     * 事件 id，中断类事件发生时由 chat 和 resume 接口返回，用于标识同一工作流中一次请求产生的多个事件，值保持一致。
     */
    @SerializedName("event_id")
    private String eventId;

    /**
     * 用于处理事件，默认走恢复。 resume: 恢复 ignore: 忽略 abort: 结束
     */
    @SerializedName("event_type")
    private String eventType;

    /**
     * 回答内容, 如果是选项回答，只需传选项信息 A-Z
     */
    private String content;

    public AgentResumeParam(Builder builder) {
        this.eventId = builder.eventId;
        this.eventType = builder.eventType;
        this.content = builder.content;
    }

    public AgentResumeParam() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public void selfCheck() {
        if (StringUtils.isNullOrEmpty(eventId)) {
            throw new BusinessException("eventId不能为空");
        }

        if (null == content) {
            throw new BusinessException("content参数不能为空");
        }
    }

    public static final class Builder {

        private String eventId;
        private String eventType;
        private String content;

        private Builder() {
        }

        public AgentResumeParam build() {
            return new AgentResumeParam(this);
        }

        public Builder eventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }
    }
}
