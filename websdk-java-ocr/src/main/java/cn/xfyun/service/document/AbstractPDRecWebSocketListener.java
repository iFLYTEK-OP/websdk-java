package cn.xfyun.service.document;

import cn.xfyun.model.common.response.ResponseHeader;
import cn.xfyun.model.document.response.PDRecResponse;
import cn.xfyun.util.StringUtils;
import com.google.gson.Gson;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * 文档还原websocket监听抽象类
 *
 * @author zyding6
 * @version 1.0
 * @date 2025/3/25 11:19
 */
public abstract class AbstractPDRecWebSocketListener extends WebSocketListener {

    private static final Gson JSON = new Gson();
    private static final Logger logger = LoggerFactory.getLogger(AbstractPDRecWebSocketListener.class);
    private static final int END = 2;
    private byte[] bytes = new byte[0];
    private final File file;
    private FileOutputStream outputStream;

    public AbstractPDRecWebSocketListener() {
        this.file = null;
    }

    public AbstractPDRecWebSocketListener(File file) throws IOException {
        this.file = file;
        if (file != null) {
            ensureDirectoryExists(file.getParentFile());
            // Append mode
            this.outputStream = new FileOutputStream(file, true);
        }
    }

    private void ensureDirectoryExists(File directory) throws IOException {
        if (!directory.exists()) {
            Files.createDirectories(directory.toPath());
        }
    }

    public abstract void onSuccess(byte[] bytes);

    public abstract void onClose(WebSocket webSocket, int code, String reason);

    public abstract void onFail(WebSocket webSocket, Throwable t, Response response);

    public abstract void onBusinessFail(WebSocket webSocket, PDRecResponse response);

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        logger.debug("Received message: {}", text);
        try {
            PDRecResponse response = JSON.fromJson(text, PDRecResponse.class);
            if (response == null || response.getHeader() == null) {
                return;
            }

            ResponseHeader header = response.getHeader();
            if (header.getCode() != 0) {
                logger.error("Error: {} (CODE={}, SID={})", header.getMessage(), header.getCode(), header.getSid());
                onBusinessFail(webSocket, response);
                return;
            }

            if (header.getStatus() == 1) {
                PDRecResponse.Payload payload = response.getPayload();
                if (payload == null || payload.getResult() == null) {
                    return;
                }

                String textData = payload.getResult().getText();
                if (textData != null) {
                    byte[] audioBytes = Base64.getDecoder().decode(textData);
                    bytes = StringUtils.unitByteArray(bytes, audioBytes);
                }
            }

            if (header.getStatus() == END) {
                handleFinalAudio(webSocket);
            }
        } catch (Exception e) {
            logger.error("Failed to process message", e);
            onFail(webSocket, e, null);
        }
    }

    private void handleFinalAudio(WebSocket webSocket) {
        if (file != null && outputStream != null) {
            try (FileOutputStream fos = outputStream) {
                fos.write(bytes);
                fos.flush();
                logger.debug("File saved at: {}", file.getPath());
            } catch (IOException e) {
                logger.error("Error writing document file", e);
                onBusinessFail(webSocket, new PDRecResponse(-1, "File Write Error"));
            }
        }
        onSuccess(bytes);
        closeWebSocket(webSocket);
    }

    private void closeWebSocket(WebSocket webSocket) {
        try {
            webSocket.close(1000, "");
        } catch (Exception e) {
            logger.warn("Error closing WebSocket", e);
        }
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        // logger.warn("WebSocket closed with code: {}, reason: {}", code, reason);
        onClose(webSocket, code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        logger.error("WebSocket connection failed", t);
        onFail(webSocket, t, response);
    }
}