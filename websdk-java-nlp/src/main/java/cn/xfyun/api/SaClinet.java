package cn.xfyun.api;

import cn.xfyun.exception.HttpException;
import cn.xfyun.model.header.BuildHttpHeader;
import cn.xfyun.util.HttpConnector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *   情感分析
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/6/11 10:43
 */
public class SaClinet {

	private static final String TYPE = "{\"type\":\"dependent\"}";

	/**
	 * 服务请求地址
	 */
	private String hostUrl;

	/**
	 * 应用ID,控制台获取
	 */
	private String appId;

	/**
	 * 应用Key,控制台获取
	 */
	private String apiKey;


	private String text;


	private HttpConnector connector;

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



	public String getHostUrl() {
		return hostUrl;
	}

	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public HttpConnector getConnector() {
		return connector;
	}

	public void setConnector(HttpConnector connector) {
		this.connector = connector;
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

	public SaClinet(SaClinet.Builder builder) {
		this.hostUrl = builder.hostUrl;
		this.appId = builder.appId;
		this.apiKey = builder.apiKey;
		this.text = builder.text;
		this.maxConnections = builder.maxConnections;
		this.connTimeout = builder.connTimeout;
		this.socketTimeout = builder.socketTimeout;
		this.retryCount = builder.retryCount;
	}


	public String send(String text) throws IOException, HttpException {
        HttpConnector connector = HttpConnector.build(maxConnections, connTimeout, socketTimeout, retryCount);
        Map<String, String> body = new HashMap<>(1);
        body.put("text", text);
		return connector.post(hostUrl, BuildHttpHeader.buildHttpHeader(TYPE, apiKey, appId), body);
	}


	public static class Builder {
		/**
		 * 服务请求地址
		 */
		private String hostUrl = "https://ltpapi.xfyun.cn/v2/sa";;

		private String appId;

		private String apiKey;

		private String text;

		/**
		 * 最大连接数
		 */
		private Integer maxConnections = 20;
		/**
		 *  建立连接的超时时间
		 */
		private Integer connTimeout = 5000;
		/**
		 * 读数据包的超时时间
		 */
		private Integer socketTimeout = 3000;
		/**
		 *  重试次数
		 */
		private Integer retryCount = 0;

		public Builder(String appId, String apiKey) {
			this.appId = appId;
			this.apiKey = apiKey;
		}

		public SaClinet build() {
			return new SaClinet(this);
		}

		public SaClinet.Builder hostUrl(String hostUrl) {
			this.hostUrl = hostUrl;
			return this;
		}

		public SaClinet.Builder maxConnections(int maxConnections) {
			this.maxConnections = maxConnections;
			return this;
		}

		public SaClinet.Builder connTimeout(int connTimeout) {
			this.connTimeout = connTimeout;
			return this;
		}

		public SaClinet.Builder retryCount(int retryCount) {
			this.retryCount = retryCount;
			return this;
		}

		public SaClinet.Builder socketTimeout(int socketTimeout) {
			this.socketTimeout = socketTimeout;
			return this;
		}

	}
}
