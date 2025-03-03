package cn.xfyun.domain;

import cn.xfyun.function.WebsocketListener;
import cn.xfyun.util.EasyOperation;
import cn.xfyun.util.RestOperation;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author: rblu2
 * @desc:  领域对象for websocket
 *
 * @create: 2025-02-28 15:03
 **/
public abstract class WebsocketTemplate<T extends WebsocketTemplate<T>> {

    private static final OkHttpClient client = new OkHttpClient.Builder().pingInterval(30, TimeUnit.SECONDS).build();

    public Runnable onOpen;
    public Consumer<String> onMessage;
    public WebsocketListener onClosed;
    public Consumer<Response> onFailure;
    public Runnable onMessageEnding;
    public long startTime = System.currentTimeMillis();

    public abstract EasyOperation.EasyLog<T> easyLog();

    protected abstract T self();

    public abstract String url();

    public void execute() {
        preExecute();
        client.newWebSocket(new Request.Builder().url(url()).build(), socketListener());
        afterExecute();
    }

    private WebSocketListener socketListener() {

        return new WebSocketListener() {
            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                selfOnOpen();
                pointOnOpen(webSocket, response);
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                selfOnMessage(text);
                boolean finished = pointOnMessage(webSocket, text);
                if(finished) {
                    endOnMessage(webSocket);
                }
            }

            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                selfOnClosed(webSocket, code, reason);
                pointOnClosed(webSocket, code, reason);
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
                selfOnFailure(t, response);
                pointOnFailure(webSocket, t, response);
            }
        };
    }

    private void selfOnFailure(@NotNull Throwable t, @Nullable Response response) {
        easyLog().logger().error("WebSocket 连接失败: ", t);
        if(Objects.nonNull(response)) {
            EasyOperation.getOrDefault(onFailure, this::defaultOnFailure).accept(response);
        }
    }

    private void selfOnClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        EasyOperation.getOrDefault(onClosed, this::defaultOnClosed).listen(webSocket, code, reason);
    }

    private void selfOnOpen() {
        EasyOperation.getOrDefault(onOpen, this::defaultOnOpen).run();
    }

    public void endOnMessage(@NotNull WebSocket webSocket) {
        EasyOperation.getOrDefault(onMessageEnding, this::defaultOnMessageEnding).run();
        webSocket.close(1000, "正常关闭");
    }

    private void selfOnMessage(@NotNull String text) {
        EasyOperation.getOrDefault(onMessage, this::defaultOnMessage).accept(text);
    }

    private Runnable defaultOnOpen() {
        return () -> easyLog().trace(logger -> logger.debug("WebSocket 连接已打开, 即将发送信息..."));
    }

    private Consumer<String> defaultOnMessage() {
        return message -> easyLog().trace(logger -> logger.debug("接收到消息: {}", message));
    }

    private WebsocketListener defaultOnClosed() {
        return (webSocket, code, reason) -> easyLog().trace(logger -> logger.debug("WebSocket 连接已关闭 code: {} reason {}", code, reason));
    }

    private Consumer<Response> defaultOnFailure() {
        return response -> {
            RestOperation.RestResult result = RestOperation.RestResult.from(response);
            easyLog().logger().warn("ResponseStatus :{} ResponseMessage {} ResponseBody {}", result.getCode(), result.getMessage() , result.bodyString());
        };
    }

    private Runnable defaultOnMessageEnding() {
        return () -> easyLog().logger().info("所有消息接收完毕... cost {}ms", System.currentTimeMillis() - startTime);
    }

    public void preExecute() {}

    public void afterExecute() {}
    public void pointOnOpen(WebSocket webSocket, Response response) {}
    public abstract boolean pointOnMessage(WebSocket webSocket, String text);
    public void pointOnClosed(WebSocket webSocket, int code, String reason) {}
    public void pointOnFailure( WebSocket webSocket, Throwable t, Response response) {}

    public T onOpen(Runnable onOpen) {
        this.onOpen = onOpen;
        return self();
    }

    public T onMessage(Consumer<String> onMessage) {
        this.onMessage = onMessage;
        return self();
    }

    public T onClosed(WebsocketListener onClosed) {
        this.onClosed = onClosed;
        return self();
    }

    public T onFailure(Consumer<Response> onFailure) {
        this.onFailure = onFailure;
        return self();
    }


}
