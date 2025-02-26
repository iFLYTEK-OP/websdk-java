package cn.xfyun.function;

import okhttp3.WebSocket;

/**
 * @author: rblu2
 * @desc:
 * @create: 2025-02-20 15:47
 **/
@FunctionalInterface
public interface WebsocketListener {
    void listen(WebSocket webSocket, int code, String reason);
}
