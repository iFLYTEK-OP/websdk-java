package cn.xfyun.service.igr;

import cn.xfyun.common.IgrConstant;
import cn.xfyun.model.response.igr.IgrResponseData;
import com.google.gson.Gson;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * @author: <flhong2@iflytek.com>
 * @description: 性别年龄识别服务层
 * @version: v1.0
 * @create: 2021-06-02 15:14
 **/
public abstract class AbstractIgrWebSocketListener extends WebSocketListener {
    public static final Gson JSON = new Gson();
    private static final Logger logger = LoggerFactory.getLogger(AbstractIgrWebSocketListener.class);

    public AbstractIgrWebSocketListener() {

    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);

        IgrResponseData resp = JSON.fromJson(text, IgrResponseData.class);
        if (resp != null) {
            if (resp.getCode() != 0) {
                logger.error("errorCode:{},  errorMessage:{}, sid:{}, 错误码查询链接：https://www.xfyun.cn/document/error-code", resp.getCode(), resp.getMessage(), resp.getSid());
                return;
            }

            if (resp.getData() != null && resp.getData().get("status").getAsInt() == IgrConstant.CODE_STATUS_SUCCESS) {
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
        onFail(webSocket, t, response);
        // 必须手动关闭 response 否则连接泄漏
        if (response != null) {
            response.close();
        }
    }

    /**
     * 处理成功
     *
     * @param webSocket
     * @param iseResponseData
     */
    public abstract void onSuccess(WebSocket webSocket, IgrResponseData iseResponseData);

    /**
     * fail
     *
     * @param webSocket
     * @param t
     * @param response
     */
    public abstract void onFail(WebSocket webSocket, Throwable t, Response response);
}
