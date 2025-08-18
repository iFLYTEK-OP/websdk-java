package cn.xfyun.service.tts;

import cn.xfyun.model.response.TtsResponse;
import com.google.gson.Gson;
import cn.xfyun.util.StringUtils;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @author yingpeng
 * 在线语音合成监听类
 */
public abstract class AbstractTtsWebSocketListener extends WebSocketListener {

    public static final Gson JSON = new Gson();
    private static final Logger logger = LoggerFactory.getLogger(AbstractTtsWebSocketListener.class);
    /**
     * 传输结束标识
     */
    private static final int END = 2;

    private byte[] bytes = new byte[0];

    private File f;

    private FileOutputStream os;

    public AbstractTtsWebSocketListener() {
    }

    public AbstractTtsWebSocketListener(File f) throws FileNotFoundException {
        this.f = f;
        this.os = new FileOutputStream(f);
    }

    /**
     * 接口调用成功
     *
     * @param bytes 返回的音频流字节数组
     */
    public abstract void onSuccess(byte[] bytes);

    /**
     * websocket返回失败时，需要用户重写的方法
     *
     * @param webSocket
     * @param t
     * @param response
     */
    public abstract void onFail(WebSocket webSocket, Throwable t, Response response);

    /**
     * 发生业务失败的情况，需要用户重写的方法
     * @param webSocket
     * @param response
     */
    public abstract void onBusinessFail(WebSocket webSocket, TtsResponse response);

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
        logger.debug("receive=>" + text);
        TtsResponse resp = JSON.fromJson(text, TtsResponse.class);
        if (resp != null) {
            if (resp.getCode() != 0) {
                logger.error("error=>" + resp.getMessage() + " sid=" + resp.getSid());
                onBusinessFail(webSocket, resp);
            }
            if (resp.getData() != null) {
                String result = resp.getData().getAudio();
                if (result != null) {
                    byte[] audio = Base64.getDecoder().decode(result);
                    bytes = StringUtils.unitByteArray(bytes, audio);
                }
                if (resp.getData().getStatus() == END) {
                    onSuccess(bytes);
                    if (f != null) {
                        try {
                            os.write(bytes);
                            os.flush();
                        } catch (IOException e) {
                            logger.error("文件写入失败", e);
                        }
                        logger.info("session end ");
                        logger.info("合成的音频文件保存在：" + f.getPath());
                    }
                    webSocket.close(1000, "");
                    try {
                        if (os != null) {
                            os.close();
                        }
                    } catch (IOException e) {
                        logger.error("流关闭失败", e);
                        onBusinessFail(webSocket, new TtsResponse(-1, "IO Exception", null, null));
                    }
                }
            }
        }
    }


    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        logger.info("socket closing");
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        logger.info("socket closed");
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
}
