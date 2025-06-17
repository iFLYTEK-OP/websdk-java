package cn.xfyun.service.voiceclone;

import cn.xfyun.model.voiceclone.response.VoiceCloneResponse;
import cn.xfyun.util.StringUtils;
import com.google.gson.Gson;
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
 * @author zyding
 * <p>
 * 一句话复刻合成监听类
 */
public abstract class AbstractVoiceCloneWebSocketListener extends WebSocketListener {

    public static final Gson JSON = new Gson();
    private static final Logger logger = LoggerFactory.getLogger(AbstractVoiceCloneWebSocketListener.class);
    /**
     * 传输结束标识
     */
    private static final int END = 2;

    private byte[] bytes = new byte[0];

    private File f;

    private FileOutputStream os;

    public AbstractVoiceCloneWebSocketListener() {
    }

    public AbstractVoiceCloneWebSocketListener(File f) throws FileNotFoundException {
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
     * @param webSocket websocket
     * @param t         异常信息
     * @param response  返回结果
     */
    public abstract void onFail(WebSocket webSocket, Throwable t, Response response);

    /**
     * 发生业务失败的情况，需要用户重写的方法
     *
     * @param webSocket websocket
     * @param response  返回结果
     */
    public abstract void onBusinessFail(WebSocket webSocket, VoiceCloneResponse response);

    /**
     * 发生业务失败的情况，需要用户重写的方法
     *
     * @param webSocket websocket
     * @param code      关闭编码
     * @param reason    关闭原因
     */
    public abstract void onClose(WebSocket webSocket, int code, String reason);

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        logger.debug("receive=>" + text);
        VoiceCloneResponse resp = JSON.fromJson(text, VoiceCloneResponse.class);
        if (resp != null) {
            VoiceCloneResponse.HeaderBean header = resp.getHeader();
            VoiceCloneResponse.PayloadBean payload = resp.getPayload();

            if (header.getCode() != 0) {
                logger.error("error=>" + header.getMessage() + " sid=" + header.getSid());
                onBusinessFail(webSocket, resp);
            }

            if (null != payload && null != payload.getAudio()) {
                String result = payload.getAudio().getAudio();
                if (result != null) {
                    byte[] audio = Base64.getDecoder().decode(result);
                    bytes = StringUtils.unitByteArray(bytes, audio);
                }

                if (payload.getAudio().getStatus() == END) {
                    onSuccess(bytes);
                    if (f != null) {
                        try {
                            os.write(bytes);
                            os.flush();
                        } catch (IOException e) {
                            logger.error("音频解析异常", e);
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
                        logger.warn("流关闭异常", e);
                        onBusinessFail(webSocket, new VoiceCloneResponse(-1, "IO Exception", null, null));
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
        onClose(webSocket, code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        logger.error("connection failed");
        onFail(webSocket, t, response);
        // 必须手动关闭 response 否则连接泄漏
        if (response != null) {
            response.close();
        }
    }
}
