package cn.xfyun.model.sparkmodel.request;

import cn.xfyun.api.SparkChatClient;
import cn.xfyun.model.sparkmodel.RoleContent;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 大模型ws请求实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class SparkChatPostRequest {

    /**
     * model : generalv3.5
     * user : 用户唯一id
     * messages : [{"role":"system","content":"你是知识渊博的助理"},{"role":"user","content":"你好，讯飞星火"}]
     * temperature : 0.5
     * top_k : 4
     * stream : false
     * max_tokens : 1024
     * presence_penalty : 1
     * frequency_penalty : 1
     * response_format : {"type":"json_object"}
     * suppress_plugin : ["knowledge"]
     */

    private String model;
    private String user;
    private List<RoleContent> messages;
    private Float temperature;
    @SerializedName("top_k")
    private Integer topK;
    @SerializedName("top_p")
    private Integer topP;
    private Boolean stream;
    @SerializedName("max_tokens")
    private Integer maxTokens;
    @SerializedName("response_format")
    private ResponseFormat responseFormat;
    @SerializedName("presence_penalty")
    private Float presencePenalty;
    @SerializedName("frequency_penalty")
    private Float frequencyPenalty;
    private List<Object> tools;
    @SerializedName("tool_calls_switch")
    private Boolean toolCallsSwitch;
    @SerializedName("tool_choice")
    private Object toolChoice;
    @SerializedName("suppress_plugin")
    private List<String> suppressPlugin;
    @SerializedName("keep_alive")
    private Boolean keepAlive;
    private SparkChatRequest.Parameter.Chat.Thinking thinking;

    public SparkChatPostRequest() {
    }

    public SparkChatPostRequest(SparkChatClient client) {
        this.temperature = client.getTemperature();
        this.topK = client.getTopK();
        this.topP = client.getTopP();
        this.maxTokens = client.getMaxTokens();
        this.presencePenalty = client.getPresencePenalty();
        this.frequencyPenalty = client.getFrequencyPenalty();
        this.toolChoice = client.getToolChoice();
        this.suppressPlugin = client.getSuppressPlugin();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<RoleContent> getMessages() {
        return messages;
    }

    public void setMessages(List<RoleContent> messages) {
        this.messages = messages;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Integer getTopK() {
        return topK;
    }

    public void setTopK(Integer topK) {
        this.topK = topK;
    }

    public Integer getTopP() {
        return topP;
    }

    public void setTopP(Integer topP) {
        this.topP = topP;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public ResponseFormat getResponseFormat() {
        return responseFormat;
    }

    public void setResponseFormat(ResponseFormat responseFormat) {
        this.responseFormat = responseFormat;
    }

    public Float getPresencePenalty() {
        return presencePenalty;
    }

    public void setPresencePenalty(Float presencePenalty) {
        this.presencePenalty = presencePenalty;
    }

    public Float getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public void setFrequencyPenalty(Float frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
    }

    public List<Object> getTools() {
        return tools;
    }

    public void setTools(List<Object> tools) {
        this.tools = tools;
    }

    public Boolean getToolCallsSwitch() {
        return toolCallsSwitch;
    }

    public void setToolCallsSwitch(Boolean toolCallsSwitch) {
        this.toolCallsSwitch = toolCallsSwitch;
    }

    public Object getToolChoice() {
        return toolChoice;
    }

    public void setToolChoice(Object toolChoice) {
        this.toolChoice = toolChoice;
    }

    public List<String> getSuppressPlugin() {
        return suppressPlugin;
    }

    public void setSuppressPlugin(List<String> suppressPlugin) {
        this.suppressPlugin = suppressPlugin;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public SparkChatRequest.Parameter.Chat.Thinking getThinking() {
        return thinking;
    }

    public void setThinking(SparkChatRequest.Parameter.Chat.Thinking thinking) {
        this.thinking = thinking;
    }

    public static class ResponseFormat {

        /**
         * type : json_object
         */

        private String type;

        public ResponseFormat() {
        }

        public ResponseFormat(String responseType) {
            this.type = responseType;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
