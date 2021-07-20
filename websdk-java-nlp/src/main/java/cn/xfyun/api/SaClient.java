package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.model.sign.Signature;

import java.io.IOException;

/**
 *   情感分析
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/6/11 10:43
 */
public class SaClient extends HttpClient {

	private static final String TYPE = "{\"type\":\"dependent\"}";

	public SaClient(SaClient.Builder builder) {
		super(builder);
	}


	public String send(String text) throws IOException {
		return sendPost(hostUrl, FORM, Signature.signHttpHeaderCheckSum(appId, apiKey,TYPE), text);
	}


	public static final class Builder extends HttpBuilder<Builder> {
		/**
		 * 服务请求地址
		 */
		private static final String HOST_URL = "https://ltpapi.xfyun.cn/v2/sa";;

		public Builder(String appId, String apiKey) {
			super(HOST_URL, appId, apiKey, null);
		}

		@Override
		public SaClient build() {
			return new SaClient(this);
		}
	}
}
