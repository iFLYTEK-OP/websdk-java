package cn.xfyun.chat;


import cn.xfyun.basic.ConvertOperation;
import cn.xfyun.basic.TimeOperation;
import cn.xfyun.eum.SparkModelEum;
import cn.xfyun.model.RoleMessage;
import cn.xfyun.request.HttpChatRequest;
import okhttp3.*;
import okio.BufferedSource;
import okio.Okio;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author: rblu2
 * @desc: http星火模型对话
 * @create: 2025-02-18 18:06
 **/
public class HttpSparkChat {
    private static final OkHttpClient client = new OkHttpClient();
    private SparkModelEum modelEum;
    private String url;
    private String apiPassword;
    private HttpChatRequest chatRequest;
    private Consumer<String> consumer;
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

    public HttpSparkChat webSearch() {
        if(modelEum != SparkModelEum.MAX_32K && modelEum != SparkModelEum.V4_ULTRA) {
            throw new IllegalArgumentException("At present,webSearch support by spark as " + SparkModelEum.MAX_32K.getCode() + ", " + SparkModelEum.V4_ULTRA.getCode());
        }
        this.chatRequest.webSearch();
        return this;
    }

    private Request request() {
        String data = ConvertOperation.toJson(chatRequest);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data);
        return new Request.Builder().url(url).post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiPassword)
                .build();
    }

    public String execute() {
        if(chatRequest.isStream()) {
            throw new RuntimeException("param stream is true, please check...");
        }
        return execute(request());
    }

    public void execute(Consumer<String> consumer) {
        this.chatRequest.stream(true);
        this.consumer = consumer;
        streamApply();
    }


    private void streamApply() {
        Request request = request();
        System.out.printf(
                "\n" +
                        "URI          : %s \n" +
                        "Method       : %s \n" +
                        "Headers      : %s \n" +
                        "Param        : %s \n",
                url, request.method(), request.headers(), ConvertOperation.toJson(chatRequest));
        long from = System.currentTimeMillis();
        // 发起请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            int responseCode = response.code();
            String responseMessage = response.message();
            ResponseBody responseBody = response.body();

            System.out.printf("\n" + "ResponseStatus   : %s \n" + "ResponseMessage   : %s \n", responseCode, responseMessage);

            assert responseBody != null;
            accept(responseBody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("cost: " + (System.currentTimeMillis() - from) + "ms");
    }


    // 使用 BufferedSource 逐行读取 SSE 事件流
    private void accept(ResponseBody responseBody) throws IOException {
        BufferedSource bufferedSource = Okio.buffer(responseBody.source());
        while (!bufferedSource.exhausted()) {
            String line = bufferedSource.readUtf8Line(); // 读取一行数据
            if (Objects.nonNull(line) && !line.isEmpty()) {
                consumer.accept(line);
            }
        }
        bufferedSource.close();
    }

    private String execute(Request request) {
        String body = "";
        long current = TimeOperation.time();
        try {
            Response response = client.newCall(request).execute();
            int responseCode = response.code();
            String responseMessage = response.message();
            ResponseBody responseBody = response.body();
            if(responseBody != null) {
                body = responseBody.string();
            }
            System.out.printf(
                    "\n" +
                            "URI          : %s \n" +
                            "Method       : %s \n" +
                            "Headers      : %s \n" +
                            "Param        : %s \n" +
                            "ResponseStatus   : %s \n" +
                            "ResponseBody     : %s \n" +
                            "ResponseMessage   : %s \n" +
                            "cost     : %s" + "ms\n",
                    url, request.method(), request.headers(), ConvertOperation.toJson(chatRequest),
                    responseCode, body, responseMessage, TimeOperation.time() - current);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return body;
    }


}
