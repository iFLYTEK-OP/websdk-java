package cn.xfyun.domain;


import cn.xfyun.basic.ConvertOperation;
import cn.xfyun.eum.SparkModelEum;
import cn.xfyun.function.WebsocketListener;
import cn.xfyun.model.RoleMessage;
import cn.xfyun.model.SparkChatResponse;
import cn.xfyun.request.WsChatRequest;
import cn.xfyun.util.SignatureHelper;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author: rblu2
 * @desc: websocket 星火对话
 * @create: 2025-02-20 13:59
 **/
public class WsSparkChat {

    private static final OkHttpClient client = new OkHttpClient.Builder().pingInterval(30, TimeUnit.SECONDS).build();
    private SparkModelEum modelEum;
    private String url;
    private String apiKey;
    private String apiSecret;
    private WsChatRequest chatRequest;
    private Runnable onOpen;
    private Consumer<String> onMessage;
    private Runnable onMessageEnding;
    private WebsocketListener onClosed;
    private Consumer<Response> onFailure;

    private long startTime;

    
    public static WsSparkChat prepare(SparkModelEum modelEum, String appId, String apiKey, String apiSecret) {
        WsSparkChat sparkChat = new WsSparkChat();
        sparkChat.modelEum = modelEum;
        sparkChat.apiKey = apiKey;
        sparkChat.apiSecret = apiSecret;
        sparkChat.chatRequest = WsChatRequest.create(appId, modelEum.getCode());
        return sparkChat.prefect();
    }
    
    public WsSparkChat prefect() {
        String httpUrl = modelEum.getWsUrl().replace("ws://", "http://").replace("wss://", "https://");
        this.url = SignatureHelper.getAuthUrl(httpUrl, apiKey, apiSecret);
        return this;
    }

    public WsSparkChat append(RoleMessage roleMessage) {
        this.chatRequest.getPayload().getMessage().getText().add(roleMessage);
        return this;
    }

    public WsSparkChat onOpen(Runnable onOpen) {
        this.onOpen = onOpen;
        return this;
    }
    
    public WsSparkChat temperature(float temperature) {
        this.chatRequest.getParameter().getChat().temperature(temperature);
        return this;
    }

    public WsSparkChat max_tokens(int max_tokens){
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

        this.chatRequest.getParameter().getChat().max_tokens(max_tokens);
        return this;
    }

    public WsSparkChat top_k(int top_k){
        if(top_k < 1 || top_k > 6) {
            throw new IllegalArgumentException("top_k must be in range [1,6]");
        }
        this.chatRequest.getParameter().getChat().top_k(top_k);
        return this;
    }

    public WsSparkChat webSearch() {
        if(modelEum != SparkModelEum.MAX_32K && modelEum != SparkModelEum.GENERAL_V35 && modelEum != SparkModelEum.V4_ULTRA) {
            throw new IllegalArgumentException("At present,webSearch support by spark as " + SparkModelEum.MAX_32K.getCode() + ", " + SparkModelEum.GENERAL_V35.getCode() + ", " + SparkModelEum.V4_ULTRA.getCode());
        }
        this.chatRequest.getParameter().getChat().show_ref_label(true);
        return this;
    }


    public WsSparkChat onMessage(Consumer<String> onMessage) {
        this.onMessage = onMessage;
        return this;
    }

    public WsSparkChat onMessageEnding(Runnable onMessageEnding) {
        this.onMessageEnding = onMessageEnding;
        return this;
    }

    public WsSparkChat onClosed(WebsocketListener onClosed) {
        this.onClosed = onClosed;
        return this;
    }

    public WsSparkChat onFailure(Consumer<Response> onFailure) {
        this.onFailure = onFailure;
        return this;
    }
    
    public void execute() {
        System.out.printf(
                "请求详情: " +
                        "\n" + "URI          : %s " +
                        "\n" + "Param        : %s " +
                        "\n", url, ConvertOperation.toJson(chatRequest));
        Request request = new Request.Builder().url(url).build();
        startTime = System.currentTimeMillis();
        client.newWebSocket(request, socketListener());
    }

    private WebSocketListener socketListener() {
        // 创建 WebSocket 监听器
        return new WebSocketListener() {
            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                if(Objects.isNull(onOpen)) {
                    onOpen = defaultOnOpen();
                }
                onOpen.run();
                webSocket.send(ConvertOperation.toJson(chatRequest));
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                if(Objects.isNull(onMessage)) {
                    onMessage = defaultOnMessage();
                }
                onMessage.accept(text);
                //结束标志
                if(endFlag(text)) {
                    onEnd();
                    webSocket.close(1000, "正常关闭");
                }
            }

            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                if(Objects.isNull(onClosed)) {
                    onClosed = defaultOnClosed();
                }
                onClosed.listen(webSocket, code, reason);
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
                System.err.println("WebSocket 连接失败: " + t.getMessage());
                if(response != null) {
                    if(onFailure == null) {
                        onFailure = defaultOnFailure();
                    }
                    onFailure.accept(response);
                }
            }
        };
    }

    private void onEnd() {
        if(Objects.isNull(onMessageEnding)) {
            onMessageEnding = defaultOnMessageEnding();
        }
        onMessageEnding.run();
    }

    private Runnable defaultOnOpen() {
        return () -> System.out.println("WebSocket 连接已打开, 即将发送信息...");
    }

    private Consumer<String> defaultOnMessage() {
        return message -> System.out.println("接收到消息: " + message);
    }

    private Runnable defaultOnMessageEnding() {
        return () -> System.out.println("所有消息接收完毕... cost " + (System.currentTimeMillis() - startTime) + "ms");
    }
    
    private WebsocketListener defaultOnClosed() {
        return (webSocket, code, reason) -> System.out.println("WebSocket 连接已关闭 code: " + code + " reason: " + reason);
    }
    
    private Consumer<Response> defaultOnFailure() {
        return response -> {
            int responseCode = response.code();
            String responseMessage = response.message();
            String body = "";
            try {
                ResponseBody responseBody = response.body();
                if(responseBody != null) {
                    body = responseBody.string();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println("ResponseStatus :" + responseCode + ",ResponseMessage : " + responseMessage + ",ResponseBody : " + body);
        };
    }

    private boolean endFlag(String text) {
        return ConvertOperation.parseObject(text, SparkChatResponse.class).getHeader().getStatus() == 2;
    }
}
