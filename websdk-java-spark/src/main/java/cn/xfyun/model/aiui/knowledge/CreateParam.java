package cn.xfyun.model.aiui.knowledge;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.StringUtils;

/**
 * 个性化知识库创建请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class CreateParam {

    private Long uid;
    private String name;
    private String description;
    private String sid;
    private String channel;

    public CreateParam(Builder builder) {
        this.uid = builder.uid;
        this.name = builder.name;
        this.description = builder.description;
        this.sid = builder.sid;
        this.channel = builder.channel;
    }

    public CreateParam() {
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public void createCheck() {
        if (null == uid) {
            throw new BusinessException("uid不能为空");
        }
        if (StringUtils.isNullOrEmpty(name)) {
            throw new BusinessException("知识库名称不能为空");
        }
    }

    public static final class Builder {

        private Long uid;
        private String name;
        private String description;
        private String sid;
        private String channel;

        private Builder() {
        }

        public CreateParam build() {
            return new CreateParam(this);
        }

        public Builder uid(long uid) {
            this.uid = uid;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder sid(String sid) {
            this.sid = sid;
            return this;
        }

        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }
    }
}
