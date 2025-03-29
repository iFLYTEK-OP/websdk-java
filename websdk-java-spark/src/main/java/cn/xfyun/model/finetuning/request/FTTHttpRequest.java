package cn.xfyun.model.finetuning.request;

import cn.xfyun.model.RoleContent;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 精调文本大模型http请求实体类
 *
 * @author zyding6
 **/
public class FTTHttpRequest {
    private String model;
    private List<RoleContent> messages;
    private Boolean stream;
    private Float temperature;
    @SerializedName("max_tokens")
    private Integer maxTokens;
    @SerializedName("extra_headers")
    private Object extraHeaders;
    @SerializedName("stream_options")
    private Object streamOptions;

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

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public Object getExtraHeaders() {
        return extraHeaders;
    }

    public void setExtraHeaders(Object extraHeaders) {
        this.extraHeaders = extraHeaders;
    }

    public Object getStreamOptions() {
        return streamOptions;
    }

    public void setStreamOptions(Object streamOptions) {
        this.streamOptions = streamOptions;
    }
}
