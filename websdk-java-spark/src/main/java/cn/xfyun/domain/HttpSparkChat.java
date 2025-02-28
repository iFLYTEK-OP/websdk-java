package cn.xfyun.domain;

import cn.xfyun.config.SparkModelEum;
import cn.xfyun.model.RoleMessage;
import cn.xfyun.request.HttpChatRequest;
import cn.xfyun.util.EasyOperation;
import cn.xfyun.util.RestOperation;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author: rblu2
 * @desc: http星火模型对话
 * @create: 2025-02-18 18:06
 **/
public class HttpSparkChat {
    private SparkModelEum modelEum;
    private String url;
    private String apiPassword;
    private HttpChatRequest chatRequest;
    public static HttpSparkChat prepare(SparkModelEum modelEum, String apiPassword) {
        HttpSparkChat sparkChat = new HttpSparkChat();
        sparkChat.modelEum = modelEum;
        sparkChat.url = modelEum.getHttpUrl();
        sparkChat.apiPassword = apiPassword;
        sparkChat.chatRequest = new HttpChatRequest(modelEum.getCode());
        return sparkChat;
    }

    public HttpSparkChat temperature(float temperature){
        if(temperature < 0f || temperature > 2f) {
            throw new IllegalArgumentException("temperature must be in range [0,2]");
        }
        this.chatRequest.temperature(temperature);
        return this;
    }
    
    public HttpSparkChat top_k(int top_k){
        if(top_k < 1 || top_k > 6) {
            throw new IllegalArgumentException("top_k must be in range [1,6]");
        }
        this.chatRequest.top_k(top_k);
        return this;
    }
    
    public HttpSparkChat max_tokens(int max_tokens){
        if(modelEum == SparkModelEum.LITE || modelEum == SparkModelEum.PRO_128K) {
            if(max_tokens < 1 || max_tokens > 4096) {
                throw new IllegalArgumentException("max_tokens must be in range [1,4096]");
            }
        }

        if(modelEum == SparkModelEum.MAX_32K || modelEum == SparkModelEum.V4_ULTRA) {
            if(max_tokens < 1 || max_tokens > 8192) {
                throw new IllegalArgumentException("max_tokens must be in range [1,8192]");
            }
        }

        this.chatRequest.max_tokens(max_tokens);
        return this;
    }
    
    public HttpSparkChat append(RoleMessage roleMessage) {
        this.chatRequest.getMessages().add(roleMessage);
        return this;
    }

    public HttpSparkChat append(List<RoleMessage> roleMessages) {
        this.chatRequest.getMessages().addAll(roleMessages);
        return this;
    }

    public HttpSparkChat webSearch() {
        if(modelEum != SparkModelEum.MAX_32K && modelEum != SparkModelEum.V4_ULTRA) {
            throw new IllegalArgumentException("At present,webSearch support by spark as " + SparkModelEum.MAX_32K.getCode() + ", " + SparkModelEum.V4_ULTRA.getCode());
        }
        this.chatRequest.webSearch();
        return this;
    }

    public String execute() {
        if(chatRequest.isStream()) {
            throw new RuntimeException("param stream is true, please check...");
        }
        Map<String, String> header = EasyOperation.map(String.class)
                .put("Authorization", "Bearer " + apiPassword).
                put("Content-Type", "application/json").get();
        return RestOperation.post(url, header, chatRequest);
    }

    public void execute(Consumer<String> consumer) {
        this.chatRequest.stream(true);
        Map<String, String> header = header();
        RestOperation.stream(url, header, chatRequest, consumer);
    }

    private Map<String, String> header() {
        return EasyOperation.map(String.class)
                .put("Authorization", "Bearer " + apiPassword).
                put("Content-Type", "application/json").get();
    }


}
