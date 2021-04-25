package cn.xfyun.service.rta;

import cn.xfyun.api.RtasrClient;
import cn.xfyun.util.IOCloseUtil;
import okhttp3.WebSocket;
import okio.ByteString;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/4/8 14:27
 */
public class RtasrSendTask implements Runnable {

	protected WebSocket webSocket;
	/**
	 * 间隔时间
	 */
	private Integer waitMillis = 40;
	private byte[] bytes;

	private InputStream inputStream;

	private Closeable closeable;

	private Integer frameSize = 1280;

	public RtasrSendTask build(RtasrSendTask.Builder builder) {
		this.waitMillis = builder.waitMillis;
		this.bytes = builder.bytes;
		this.closeable = builder.closeable;
		this.inputStream = builder.inputStream;
		this.frameSize = builder.frameSize;
		this.webSocket = builder.webSocket;
		return this;
	}

	@Override
	public void run() {
		try {
			if (inputStream != null) {
				// 流操作
				sendByInputStream(inputStream);
			} else {
				// 针对于byte数组的操作
				sendByBytes(bytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOCloseUtil.close(closeable);
			IOCloseUtil.close(inputStream);
			webSocket.send(RtasrClient.SEND_END);
		}
	}

	private void sendByBytes(byte[] bytes) throws InterruptedException {
		// 针对于byte数组的操作
		if (bytes != null && bytes.length > 0) {
			for (int i = 0; i < bytes.length; i += frameSize) {
				int len = i + frameSize < bytes.length ? i + frameSize : bytes.length;
				byte[] cur = Arrays.copyOfRange(bytes, i, len);
				webSocket.send(ByteString.of(cur));
				Thread.sleep(waitMillis);
			}
		}
	}

	private void sendByInputStream(InputStream inputStream) throws IOException, InterruptedException {
		byte[] buffer = new byte[frameSize];
		if (inputStream != null) {

			int len = 0;
			while ((len = inputStream.read(buffer)) != -1) {
				if (len < frameSize) {
					webSocket.send(ByteString.of(Arrays.copyOfRange(buffer, 0, len)));
					break;
				}
				webSocket.send(ByteString.of(buffer));
				Thread.sleep(waitMillis);
			}
		}
	}


	public static final class Builder {
		private Integer waitMillis = 40;
		private WebSocket webSocket;
		private byte[] bytes;
		private Closeable closeable;
		private InputStream inputStream;
		private Integer frameSize = 1280;

		public RtasrSendTask.Builder waitMillis(Integer waitMills) {
			this.waitMillis = waitMills;
			return this;
		}

		public RtasrSendTask.Builder webSocket(WebSocket webSocket) {
			this.webSocket = webSocket;
			return this;
		}

		public RtasrSendTask.Builder bytes(byte[] bytes) {
			this.bytes = bytes;
			return this;
		}

		public RtasrSendTask.Builder inputStream(InputStream inputStream) {
			this.inputStream = inputStream;
			return this;
		}

		public RtasrSendTask.Builder closeable(Closeable closeable) {
			this.closeable = closeable;
			return this;
		}

		public RtasrSendTask.Builder frameSize(Integer frameSize) {
			this.frameSize = frameSize;
			return this;
		}

		public void build(RtasrSendTask task) {
			task.build(this);
		}
	}
}
