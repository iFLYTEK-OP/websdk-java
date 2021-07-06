package api;

import cn.xfyun.api.TextCheckClient;
import config.PropertiesConfig;
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
@PrepareForTest({TextCheckClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class TextCheckClientTest {

	private static final String appId = PropertiesConfig.getAppId();
	private static final String apiKey = PropertiesConfig.getApiKey();
	private static final String apiSecret = PropertiesConfig.getApiSecret();

	@Test
	public void defaultParamTest() {
		TextCheckClient client = new TextCheckClient
				.Builder(appId, apiKey, apiSecret).build();
		Assert.assertEquals(client.getAppId(), appId);
		Assert.assertEquals(client.getApiKey(), apiKey);
		Assert.assertEquals(client.getApiSecret(), apiSecret);
		Assert.assertEquals(3, client.getReadTimeout());
		Assert.assertEquals(3, client.getWriteTimeout());
		Assert.assertEquals(3, client.getConnectTimeout());
	}

	@Test
	public void testParamBuild() {
		TextCheckClient client = new TextCheckClient
				.Builder(appId, apiKey, apiSecret)
				.serviceId("12323")
				.compress("gzip")
				.format("plain")
				.encoding("base64")
				.status(2)
				.build();
		Assert.assertEquals(client.getServiceId(), "12323");
		Assert.assertEquals(client.getCompress(), "gzip");
		Assert.assertEquals(client.getFormat(), "plain");
		Assert.assertEquals(client.getEncoding(), "base64");
		Assert.assertEquals(client.getStatus(), 2);
	}


	@Test
	public void testSuccess() throws Exception {
		TextCheckClient correctionClient = new TextCheckClient
				.Builder(appId, apiKey, apiSecret)
				.build();
		String result = correctionClient.send("画蛇天足");
		System.out.println("返回结果: " + result);
	}

	@Test
	public void testSendNull() throws Exception {
		TextCheckClient correctionClient = new TextCheckClient
				.Builder(appId, apiKey, apiSecret)
				.build();

		String result = correctionClient.send("");
		System.out.println("返回结果: " + result);
	}

}
