package cn.xfyun.chat;

import cn.xfyun.basic.ConvertOperation;
import cn.xfyun.basic.EasyOperation;
import cn.xfyun.basic.RestOperation;
import cn.xfyun.basic.TimeOperation;
import cn.xfyun.function.WebsocketListener;
import cn.xfyun.model.RoleMessage;
import cn.xfyun.request.WsChatDocRequest;
import cn.xfyun.util.SignatureHelper;
import okhttp3.*;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author: rblu2
 * @desc: 上传星火知识库文档
 * @create: 2025-02-24 10:34
 **/
public class SparkDocChat {
    private static final OkHttpClient client = new OkHttpClient.Builder().pingInterval(30, TimeUnit.SECONDS).build();
    private String url = "wss://chatdoc.xfyun.cn/openapi/chat";
    private String appId;
    private String apiSecret;
    private WsChatDocRequest chatRequest;
    private Runnable onOpen;
    private Consumer<String> onMessage;
    private Runnable onMessageEnding;
    private WebsocketListener onClosed;
    private Consumer<Response> onFailure;
    private long startTime;

    public static SparkDocChat prepare(String appId, String apiSecret) {
        SparkDocChat sparkChat = new SparkDocChat();
        sparkChat.appId = appId;
        sparkChat.apiSecret = apiSecret;
        sparkChat.chatRequest = new WsChatDocRequest();
        return sparkChat.prefect();
    }

    public SparkDocChat prefect() {
        long timestamp = TimeOperation.time() / 1000;
        String signature = SignatureHelper.getSignature(appId, apiSecret, timestamp);
        this.url += RestOperation.buildParams(EasyOperation.map().put("appId", appId).put("timestamp", timestamp).put("signature", signature).get());
        return this;
    }

    public SparkDocChat addFileId(String fileId) {
        this.chatRequest.getFileIds().add(fileId);
        return this;
    }

    public SparkDocChat sparkFlag(boolean spark) {
        this.chatRequest.getChatExtends().spark(spark);
        return this;
    }

    public SparkDocChat temperature(float temperature) {
        if(temperature <= 0f || temperature > 1f) {
            throw new RuntimeException("temperature must be in range (0,1]");
        }
        this.chatRequest.getChatExtends().temperature(temperature);
        return this;
    }

    public SparkDocChat append(RoleMessage roleMessage) {
        this.chatRequest.getMessages().add(roleMessage);
        return this;
    }

    public SparkDocChat onOpen(Runnable onOpen) {
        this.onOpen = onOpen;
        return this;
    }

    public SparkDocChat onMessage(Consumer<String> onMessage) {
        this.onMessage = onMessage;
        return this;
    }

    public SparkDocChat onMessageEnding(Runnable onMessageEnding) {
        this.onMessageEnding = onMessageEnding;
        return this;
    }

    public SparkDocChat onClosed(WebsocketListener onClosed) {
        this.onClosed = onClosed;
        return this;
    }

    public SparkDocChat onFailure(Consumer<Response> onFailure) {
        this.onFailure = onFailure;
        return this;
    }

    public void execute() {
        System.out.printf(
                "请求详情: " +
                        "\n" + "URI          : %s " +
                        "\n" + "Param        : %s " +
                        "\n"
                , url, ConvertOperation.toJson(chatRequest));
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
            public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
                System.out.println("接收到二进制消息: " + bytes.hex());
            }

            @Override
            public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                System.out.println("WebSocket 正在关闭: " + code + " " + reason);
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
                t.printStackTrace();
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
        return ConvertOperation.parseObject(text, ResponseData.class).getStatus() == 2;
    }

    public static class ResponseData {
        private String code;
        private Integer status;
        private String sid;

        public String getCode() {
            return code;
        }

        public Integer getStatus() {
            return status;
        }

        public String getSid() {
            return sid;
        }
    }
}
