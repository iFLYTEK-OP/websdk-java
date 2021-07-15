package cn.xfyun.service.rta;

import cn.xfyun.service.common.AbstractTaskBuilder;
import cn.xfyun.service.common.AbstractTimedTask;
import okhttp3.WebSocket;


/**
 * @author mqgao
 * @version 1.0
 * @date 2021/4/8 14:27
 */
public class RtasrSendTask extends AbstractTimedTask {

	public RtasrSendTask(Builder builder) {
		super(builder);
	}

	public static final class Builder extends AbstractTaskBuilder<Builder> {

		public Builder(WebSocket webSocket) {
			super(webSocket);
		}

		@Override
		public RtasrSendTask build() {
			return new RtasrSendTask(this);
		}
	}
}
