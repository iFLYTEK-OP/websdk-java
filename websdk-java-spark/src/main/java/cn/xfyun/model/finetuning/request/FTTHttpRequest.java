package cn.xfyun.model.finetuning.request;

import cn.xfyun.model.RoleContent;

import java.util.List;

/**
 * @program: websdk-java
 * @description: 精调文本大模型http请求实体类
 * @author: zyding6
 * @create: 2025/3/20 10:17
 **/
public class FTTHttpRequest {
    private String model;
    private List<RoleContent> messages;
    private Boolean stream;
    private Float temperature;
    private Integer max_tokens;
    private Object extra_headers;
    private Object stream_options;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<RoleContent> getMessages() {
        return messages;
    }

    public void setMessages(List<RoleContent> messages) {
        this.messages = messages;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Integer getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(Integer max_tokens) {
        this.max_tokens = max_tokens;
    }

    public Object getExtra_headers() {
        return extra_headers;
    }

    public void setExtra_headers(Object extra_headers) {
        this.extra_headers = extra_headers;
    }

    public Object getStream_options() {
        return stream_options;
    }

    public void setStream_options(Object stream_options) {
        this.stream_options = stream_options;
    }
}
