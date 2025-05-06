package cn.xfyun.model.sparkmodel;

import com.google.gson.annotations.SerializedName;

/**
 * 大模型会话message实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class RoleContent {

    private String role;
    private String content;
    @SerializedName("content_type")
    private String contentType;

    public RoleContent(Builder builder) {
        this.role = builder.role;
        this.content = builder.content;
        this.contentType = builder.contentType;
    }

    public RoleContent() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String role;
        private String content;
        private String contentType;

        private Builder() {
        }

        public RoleContent build() {
            return new RoleContent(this);
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }
    }
}
