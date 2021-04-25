package cn.xfyun.service.rta;

import cn.xfyun.model.response.rtasr.RtasrResponse;
import cn.xfyun.service.iat.AbstractIatWebSocketListener;
import com.google.gson.Gson;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.EOFException;
import java.util.Objects;

/**
 *
 *   实时语音转写回调
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/3/17 11:19
 */
public abstract class AbstractRtasrWebSocketListener extends WebSocketListener {

	private static final Logger logger = LoggerFactory.getLogger(AbstractIatWebSocketListener.class);

	private static final String STARTED = "started";
	private static final String RESULT = "result";
	private static final String ERROR = "error";

	public AbstractRtasrWebSocketListener() {
	}

	/**
	 *    服务端发送的消息回调
	 *
	 * @param webSocket
	 * @param text
	 */
	public abstract void onSuccess(WebSocket webSocket, String text);

	/**
	 *    连接异常
	 *
	 * @param webSocket
	 * @param t
	 * @param response
	 */
	public abstract void onFail(WebSocket webSocket, Throwable t, @Nullable Response response);

	/**
	 *    业务异常
	 *
	 * @param webSocket
	 * @param text
	 */
	public abstract void onBusinessFail(WebSocket webSocket, String text);

	/**
	 *  服务端关闭
	 */
	public abstract void onClosed();

	@Override
	public void onOpen(WebSocket webSocket, Response response) {
		super.onOpen(webSocket, response);
	}

	@Override
	public void onMessage(WebSocket webSocket, String text) {
		Gson gson = new Gson();
		RtasrResponse rtasrResponse = gson.fromJson(text, RtasrResponse.class);
		String action = rtasrResponse.getAction();
		if (Objects.equals(STARTED, action)) {
		   logger.debug("服务端握手成功 info : [{}]", text);
		} else if (Objects.equals(RESULT, action)) {
            // 服务端处理音频返回
			onSuccess(webSocket, text);

		} else if (Objects.equals(ERROR, action)) {
            // 服务端处理音频失败
			onBusinessFail(webSocket, text);
			logger.error("出现错误 error : [{}]", text);
		}
	}

	@Override
	public void onMessage(WebSocket webSocket, ByteString bytes) {
		super.onMessage(webSocket, bytes);
	}

	@Override
	public void onClosing(WebSocket webSocket, int code, String reason) {
		super.onClosing(webSocket, code, reason);
		logger.warn("webSocket is closed ,code is {} , reason is [{}]", code, reason);
	}

	@Override
	public void onClosed(WebSocket webSocket, int code, String reason) {
		super.onClosed(webSocket, code, reason);
		logger.warn("webSocket is closed ,code is {} , reason is [{}]", code, reason);
		onClosed();
	}

	@Override
	public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
		if (t instanceof EOFException) {
			onClosed();
		} else {
			t.printStackTrace();
			onFail(webSocket, t, response);
		}
	}


}
