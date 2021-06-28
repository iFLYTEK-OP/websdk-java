package cn.xfyun.api;

import cn.xfyun.model.sign.TextCorrectionSignature;
import cn.xfyun.util.AuthUtil;
import cn.xfyun.util.HttpConnector;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Base64;

/**
 *
 *     文本纠错 API
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/6/8 14:32
 */
public class TextCorrectionClient {

	/**
	 *   请求地址
	 */
	private String url;

	/**
	 *   在平台申请的appid信息
	 */
	private String appId;

	/**
	 *   在平台申请的apiKey信息
	 */
	private String apiKey;

	/**
	 *   在平台申请的apiSecret信息
	 */
	private String apiSecret;

	/**
	 *   服务引擎ID 默认 s9a87e3ec
	 */
	private String serviceId;

	/**
	 *   请求状态，取值范围为：3（一次传完）
	 */
	private int status;

	/**
	 *    文本编码，可选值：utf8（默认值
	 */
	private String encoding;

	/**
	 *   文本压缩格式，可选值：raw（默认值）
	 */
	private String compress;

	/**
	 *    文本格式，可选值：json（默认值）
	 */
	private String format;

	/**
	 *   文本数据，base64编码，最大支持7000字节，请注意中文要控制在2000个字符
	 */
	private String text;

	/**
	 *   http最大连接数  默认20
	 */
	private int maxConnections;

	/**
	 *   http连接时间  默认3s
	 */
	private int connTimeout;

	/**
	 *    http socket超时时间 默认 5s
	 */
	private int socketTimeout;

	/**
	 *    http 重试次数 默认不重试 0
	 */
	private int retryCount;


	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getCompress() {
		return compress;
	}

	public void setCompress(String compress) {
		this.compress = compress;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public int getConnTimeout() {
		return connTimeout;
	}

	public void setConnTimeout(int connTimeout) {
		this.connTimeout = connTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public TextCorrectionClient(TextCorrectionClient.Builder builder) {
		this.url = builder.url;
		this.appId = builder.appId;
		this.apiKey = builder.apiKey;
		this.apiSecret = builder.apiSecret;
		this.serviceId = builder.serviceId;
		this.status = builder.status;
		this.encoding = builder.encoding;
		this.compress = builder.compress;
		this.format = builder.format;
		this.text = builder.text;
		this.url = builder.url;
		this.maxConnections = builder.maxConnections;
		this.connTimeout = builder.connTimeout;
		this.socketTimeout = builder.socketTimeout;
		this.retryCount = builder.retryCount;
	}


	public String send(String text) throws Exception {
		HttpConnector httpClient = HttpConnector.build(maxConnections, connTimeout, socketTimeout, retryCount);
		TextCorrectionSignature signature = new TextCorrectionSignature(apiKey, apiSecret, url);
		String realUrl = AuthUtil.generateRequestUrl(signature);
		return httpClient.post(realUrl, buildParam(text));
	}

	private String buildParam(String text) throws IOException {
		JsonObject req = new JsonObject();
		//平台参数
		JsonObject header = new JsonObject();
		header.addProperty("app_id", appId);
		header.addProperty("status", status);
		//功能参数
		JsonObject parameter = new JsonObject();
		JsonObject inputAcp = new JsonObject();
		JsonObject result = new JsonObject();
		//构建result段参数
		inputAcp.addProperty("encoding", encoding);
		//raw,gzip
		inputAcp.addProperty("compress", compress);
		//plain,json,xml
		inputAcp.addProperty("format", format);
		result.add("result", inputAcp);
		parameter.add(this.serviceId, result);
		//请求数据
		JsonObject payload = new JsonObject();
		JsonObject input = new JsonObject();
		//jpg:jpg格式,jpeg:jpeg格式,png:png格式,bmp:bmp格式
		input.addProperty("encoding",encoding);
		//raw,gzip
		input.addProperty("compress",compress);
		//plain,json,xml
		input.addProperty("format",format);
		//3:一次性传完
		input.addProperty("status",status);
		//文本数据，base64
		input.addProperty("text", Base64.getEncoder().encodeToString(text.getBytes("UTF-8")));
		payload.add("input",input);
		req.add("header",header);
		req.add("parameter",parameter);
		req.add("payload",payload);

		return req.toString();
	}


	public static final class Builder {

		private String url = "https://api.xf-yun.com/v1/private/s9a87e3ec";

		private String appId;

		private String apiKey;

		private String apiSecret;

		private String serviceId = "s9a87e3ec";

		private int status = 3;

		private String encoding = "utf8";

		private String compress = "raw";

		private String format = "json";

		private String text;

		private int maxConnections = 20;

		private int connTimeout = 3000;

		private int socketTimeout = 5000;

		private int retryCount = 0;

		public Builder(String appId, String apiSecret, String apiKey) {
			this.appId = appId;
			this.apiKey = apiKey;
			this.apiSecret = apiSecret;
		}

		public TextCorrectionClient.Builder url(String url) {
			this.url = url;
			return this;
		}

		public TextCorrectionClient.Builder serviceId(String serviceId) {
			this.serviceId = serviceId;
			return this;
		}

		public TextCorrectionClient.Builder status(int status) {
			this.status = status;
			return this;
		}

		public TextCorrectionClient.Builder encoding(String encoding) {
			this.encoding = encoding;
			return this;
		}

		public TextCorrectionClient.Builder compress(String compress) {
			this.compress = compress;
			return this;
		}

		public TextCorrectionClient.Builder format(String format) {
			this.format = format;
			return this;
		}

		public TextCorrectionClient.Builder maxConnections(int maxConnections) {
			this.maxConnections = maxConnections;
			return this;
		}

		public TextCorrectionClient.Builder connTimeout(int connTimeout) {
			this.connTimeout = connTimeout;
			return this;
		}

		public TextCorrectionClient.Builder socketTimeout(int socketTimeout) {
			this.socketTimeout = socketTimeout;
			return this;
		}

		public TextCorrectionClient.Builder retryCount(int retryCount) {
			this.retryCount = retryCount;
			return this;
		}

		public TextCorrectionClient build() {
			TextCorrectionClient client = new TextCorrectionClient(this);
			return client;
		}

	}


}


