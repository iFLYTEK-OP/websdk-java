package cn.xfyun.api;

import cn.xfyun.config.PropertiesConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


/**
 * @author mqgao
 * @version 1.0
 * @date 2021/6/11 10:14
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TextCorrectionClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class TextCorrectionTest {

	private static final String appId = PropertiesConfig.getAppId();
	private static final String apiKey = PropertiesConfig.getApiKey();
	private static final String apiSecret = PropertiesConfig.getSecretKey();

	@Test
	public void defaultParamTest() {
		TextCorrectionClient correctionClient = new TextCorrectionClient
				.Builder(appId, apiSecret, apiKey)
				.build();

		Assert.assertEquals(correctionClient.getAppId(), appId);
		Assert.assertEquals(correctionClient.getApiKey(), apiKey);
		Assert.assertEquals(correctionClient.getEncoding(), "utf8");
		Assert.assertEquals(correctionClient.getCompress(), "raw");
		Assert.assertEquals(correctionClient.getFormat(), "json");
		Assert.assertEquals(correctionClient.getStatus(), 3);

	}

	@Test
	public void testParamBuild() {
		TextCorrectionClient correctionClient = new TextCorrectionClient
				.Builder(appId, apiSecret, apiKey)
				.maxConnections(100)
				.connTimeout(800)
				.retryCount(3)
				.compress("gzip")
				.format("plain")
				.encoding("base64")
				.status(2)
				.build();

		Assert.assertEquals(correctionClient.getAppId(), appId);
		Assert.assertEquals(correctionClient.getApiKey(), apiKey);
		Assert.assertEquals(correctionClient.getApiSecret(), apiSecret);
		Assert.assertEquals(correctionClient.getMaxConnections(), 100);
		Assert.assertEquals(correctionClient.getConnTimeout(), 800);
		Assert.assertEquals(correctionClient.getRetryCount(), 3);
		Assert.assertEquals(correctionClient.getCompress(), "gzip");
		Assert.assertEquals(correctionClient.getFormat(), "plain");
		Assert.assertEquals(correctionClient.getEncoding(), "base64");
		Assert.assertEquals(correctionClient.getStatus(), 2);
	}


	@Test
	public void testSuccess() throws Exception {
		TextCorrectionClient correctionClient = new TextCorrectionClient
				.Builder(appId, apiSecret, apiKey)
				.build();
		String result = correctionClient.send("画蛇天足");
		System.out.println("返回结果: " + result);
	}

	@Test
	public void testSendNull() throws Exception {
		TextCorrectionClient correctionClient = new TextCorrectionClient
				.Builder(appId, apiSecret, apiKey)
				.build();

		String result = correctionClient.send("");
		System.out.println("返回结果: " + result);
	}

}
