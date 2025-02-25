package cn.xfyun.request;

import cn.xfyun.model.RoleMessage;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: rblu2
 * @desc:
 * @create: 2025-02-24 14:04
 **/

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WsChatDocRequest {
    private String repoId;
    private List<String> fileIds;
    private List<RoleMessage> messages;

    private ChatExtends chatExtends;

    public WsChatDocRequest() {
        this.fileIds = new ArrayList<>();
        this.messages  = new ArrayList<>();
        this.chatExtends = new ChatExtends();
    }

    public String getRepoId() {
        return repoId;
    }

    public WsChatDocRequest repoId(String repoId) {
        this.repoId = repoId;
        return this;
    }

    public List<String> getFileIds() {
        return fileIds;
    }

    public WsChatDocRequest fileIds(List<String> fileIds) {
        this.fileIds = fileIds;
        return this;
    }

    public List<RoleMessage> getMessages() {
        return messages;
    }

    public WsChatDocRequest messages(List<RoleMessage> messages) {
        this.messages = messages;
        return this;
    }

    public ChatExtends getChatExtends() {
        return chatExtends;
    }

    public WsChatDocRequest chatExtends(ChatExtends chatExtends) {
        this.chatExtends = chatExtends;
        return this;
    }

    public static class ChatExtends {
        private Boolean spark;
        private Float temperature;

        public ChatExtends() {
            this.spark = true;
            this.temperature = 0.5f;
        }

        public Boolean getSpark() {
            return spark;
        }

        public ChatExtends spark(Boolean spark) {
            this.spark = spark;
            return this;
        }

        public Float getTemperature() {
            return temperature;
        }

        public ChatExtends temperature(Float temperature) {
            this.temperature = temperature;
            return this;
        }
    }
}
