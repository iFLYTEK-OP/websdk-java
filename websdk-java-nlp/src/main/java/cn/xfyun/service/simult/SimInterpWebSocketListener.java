package cn.xfyun.service.simult;

import cn.xfyun.model.simult.response.SimInterpResponse;
import cn.xfyun.util.StringUtils;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * 同声传译ws监听类
 *
 * @author <zyding6@ifytek.com>
 */
public abstract class SimInterpWebSocketListener extends WebSocketListener {

    private static final Logger logger = LoggerFactory.getLogger(SimInterpWebSocketListener.class);

    /**
     * construction method
     */
    public SimInterpWebSocketListener() {
    }

    /**
     * websocket返回成功时，需要用户重写的方法
     *
     * @param webSocket   websocket
     * @param iatResponse 返回结果
     */
    public abstract void onSuccess(WebSocket webSocket, SimInterpResponse iatResponse);

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

    /**
     * websocket关闭时返回，需要用户重写的方法
     *
     * @param webSocket websocket
     * @param response  response
     */
    public abstract void onConnect(WebSocket webSocket, Response response);


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        logger.info("webSocket is open");
        onConnect(webSocket, response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        try {
            if (text != null) {
                SimInterpResponse sparkIatResponse = StringUtils.gson.fromJson(text, SimInterpResponse.class);

                if (sparkIatResponse != null) {
                    onSuccess(webSocket, sparkIatResponse);
                } else {
                    onSuccess(webSocket, new SimInterpResponse(-1, "SimInterp response error"));
                }
            }
        } catch (Exception e) {
            logger.error("parse text to object fail ,text is [{}]", text, e);
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
