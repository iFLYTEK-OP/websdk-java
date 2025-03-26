package cn.xfyun.service.sparkiat;

import cn.xfyun.model.sparkiat.response.SparkIatResponse;
import cn.xfyun.util.StringUtils;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * 大模型语音听写
 *
 * @author zyding
 * @date 2025/3/12 17:42
 */
public abstract class AbstractSparkIatWebSocketListener extends WebSocketListener {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSparkIatWebSocketListener.class);

    /**
     * construction method
     */
    public AbstractSparkIatWebSocketListener() {
    }

    /**
     * websocket返回成功时，需要用户重写的方法
     *
     * @param webSocket   websocket
     * @param iatResponse 返回结果
     */
    public abstract void onSuccess(WebSocket webSocket, SparkIatResponse iatResponse);

    /**
     * websocket返回失败时，需要用户重写的方法
     *
     * @param webSocket websocket
     * @param t         异常信息
     * @param response  返回结果
     */
    public abstract void onFail(WebSocket webSocket, Throwable t, Response response);

    /**
     * websocket关闭时返回，需要用户重写的方法
     *
     * @param webSocket websocket
     * @param code      关闭编码
     * @param reason    关闭原因
     */
    public abstract void onClose(WebSocket webSocket, int code, String reason);


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        logger.info("webSocket is open");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        try {
            if (text != null) {
                SparkIatResponse sparkIatResponse = StringUtils.gson.fromJson(text, SparkIatResponse.class);

                if (sparkIatResponse != null) {
                    onSuccess(webSocket, sparkIatResponse);
                } else {
                    onSuccess(webSocket, new SparkIatResponse(-1, "iat response error"));
                }
            }
        } catch (Exception e) {
            logger.error("parse text to object fail ,text is [{}]", text);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        logger.warn("webSocket is closing ,code is {} , reason is [{}]", code, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        logger.warn("webSocket is closed ,code is {} , reason is [{}]", code, reason);
        onClose(webSocket, code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        logger.error("webSocket connect failed .", t);
        onFail(webSocket, t, response);
    }

}
