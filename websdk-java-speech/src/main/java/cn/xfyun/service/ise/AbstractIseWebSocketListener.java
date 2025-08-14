package cn.xfyun.service.ise;

import cn.xfyun.model.response.ise.IseResponseData;
import com.google.gson.Gson;
import cn.xfyun.common.IseConstant;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * @version 1.0
 * @author: <flhong2@iflytek.com>
 * @description: 语音评测服务层
 * @create: 2021-03-17 19:46
 **/
public abstract class AbstractIseWebSocketListener extends WebSocketListener {

    public static final Gson JSON = new Gson();
    private static final Logger logger = LoggerFactory.getLogger(AbstractIseWebSocketListener.class);

    public AbstractIseWebSocketListener() {

    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        try {
            logger.debug("Handshake success, code={}, headers={}", response.code(), response.headers());
        } finally {
            // 防止握手失败资源泄漏
            try {
                response.close();
            } catch (Exception closeError) {
                logger.warn("response close failed", closeError);
            }
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        IseResponseData resp = JSON.fromJson(text, IseResponseData.class);
        if (resp != null) {
            if (resp.getCode() != 0) {
                logger.error("errorCode:{},  errorMessage:{}, sid:{}, 错误码查询链接：https://www.xfyun.cn/document/error-code", resp.getCode(), resp.getMessage(), resp.getSid());
                return;
            }

            if (resp.getData() != null && resp.getData().getStatus() == IseConstant.CODE_STATUS_SUCCESS) {
                onSuccess(webSocket, resp);
                webSocket.close(1000, "");
            }

        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        try {
            onFail(webSocket, t, response);
        } finally {
            // 手动关闭,防止连接泄漏
            if (response != null) {
                try {
                    response.close();
                } catch (Exception closeError) {
                    logger.warn("response close failed", closeError);
                }
            }
        }
    }

    /**
     * 处理成功
     *
     * @param webSocket
     * @param iseResponseData
     */
    public abstract void onSuccess(WebSocket webSocket, IseResponseData iseResponseData);

    /**
     * fail
     *
     * @param webSocket
     * @param t
     * @param response
     */
    public abstract void onFail(WebSocket webSocket, Throwable t, Response response);

}
