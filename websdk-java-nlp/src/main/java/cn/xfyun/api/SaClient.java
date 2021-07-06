package cn.xfyun.api;

import cn.xfyun.base.http.HttpRequestBuilder;
import cn.xfyun.base.http.HttpRequestClient;
import cn.xfyun.model.sign.Signature;

import java.io.IOException;
import java.util.Map;

/**
 *   情感分析
 *
 *   以哈工大社会计算与信息检索研究中心研发的 “语言技术平台（LTP）” 为基础，
 *   为用户提供针对 中文（简体） 文本的情感分析服务
 *
 *   错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
 *
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/6/11 10:43
 */
public class SaClient extends HttpRequestClient {

	private static final String TYPE = "{\"type\":\"dependent\"}";


	public SaClient(SaClient.Builder builder) {
		super(builder);
	}


	public String send(String text) throws IOException {
		Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, TYPE, null);
        return sendPost(hostUrl, FORM, header, "text=" + text);
	}


	public static final class Builder extends HttpRequestBuilder<Builder> {
		/**
		 * 服务请求地址
		 */
		private static final String HOST_URL = "https://ltpapi.xfyun.cn/v2/sa";


		public Builder(String appId, String apiKey) {
			super(HOST_URL, appId, apiKey, null);
		}

		@Override
		public SaClient build() {
			return new SaClient(this);
		}
	}

}
