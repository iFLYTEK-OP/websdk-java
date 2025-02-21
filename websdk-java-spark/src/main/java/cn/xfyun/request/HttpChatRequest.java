package cn.xfyun.request;


import cn.xfyun.model.RoleMessage;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: rblu2
 * @desc:
 * @create: 2025-02-20 15:25
 **/

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpChatRequest {

    private String model;
    private List<RoleMessage> messages;

    private Float temperature;

    private Integer top_k;

    private boolean stream;

    private Integer max_tokens;

    private List<Tool> tools;

    public HttpChatRequest(String model) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.temperature = 1.0f;
        this.top_k = 4;
        this.max_tokens = 4096;
    }

    public String getModel() {
        return model;
    }

    public HttpChatRequest model(String model) {
        this.model = model;
        return this;
    }

    public List<RoleMessage> getMessages() {
        return messages;
    }

    public HttpChatRequest messages(List<RoleMessage> roleMessages) {
        this.messages = roleMessages;
        return this;
    }

    public float getTemperature() {
        return temperature;
    }

    public HttpChatRequest temperature(Float temperature) {
        this.temperature = temperature;
        return this;
    }

    public Integer getTop_k() {
        return top_k;
    }

    public HttpChatRequest top_k(Integer top_k) {
        this.top_k = top_k;
        return this;
    }

    public boolean isStream() {
        return stream;
    }

    public HttpChatRequest stream(boolean stream) {
        this.stream = stream;
        return this;
    }

    public Integer getMax_tokens() {
        return max_tokens;
    }

    public HttpChatRequest max_tokens(Integer max_tokens) {
        this.max_tokens = max_tokens;
        return this;
    }

    public List<Tool> getTools() {
        return tools;
    }

    public HttpChatRequest tools(List<Tool> tools) {
        this.tools = tools;
        return this;
    }
    
    public HttpChatRequest webSearch() {
        this.tools = new ArrayList<>();
        this.tools.add(new Tool("web_search", new Tool.WebSearch(true, true)));
        return this;
    }

    public static class Tool {
        private String type;
        private WebSearch web_search;
        
        public Tool(String type, WebSearch web_search) {
            this.type = type;
            this.web_search = web_search;
        }

        public String getType() {
            return type;
        }

        public Tool type(String type) {
            this.type = type;
            return this;
        }

        public WebSearch getWeb_search() {
            return web_search;
        }

        public Tool web_search(WebSearch web_search) {
            this.web_search = web_search;
            return this;
        }

        public static class WebSearch {
            private boolean enable;

            private boolean show_ref_label;
            public WebSearch(boolean enable, boolean show_ref_label) {
                this.enable = enable;
                this.show_ref_label = show_ref_label;
            }

            public boolean isEnable() {
                return enable;
            }

            public WebSearch enable(boolean enable) {
                this.enable = enable;
                return this;
            }

            public boolean isShow_ref_label() {
                return show_ref_label;
            }

            public WebSearch show_ref_label(boolean show_ref_label) {
                this.show_ref_label = show_ref_label;
                return this;
            }
        }
    }
}
