package api;

import cn.xfyun.api.SaClient;
import config.PropertiesConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/6/11 11:08
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SaClientTest.class})
@PowerMockIgnore("javax.net.ssl.*")
public class SaClientTest {

	private static final String appId = PropertiesConfig.getAppId();
	private static final String apiKey = PropertiesConfig.getSaClientApiKey();

	/**
	 * 测试参数设置
	 */
	@Test
	public void testParams() {
		SaClient saClient = new SaClient
				.Builder(appId, apiKey)
				.build();
		assertEquals(appId, saClient.getAppId());
		assertEquals(apiKey, saClient.getApiKey());
		assertEquals("https://ltpapi.xfyun.cn/v2/sa", Whitebox.getInternalState(saClient, "hostUrl"));
	}

	@Test
	public void testBuildParams() {
		SaClient saClient = new SaClient
				.Builder(appId, apiKey)
				.connectTimeout(1)
				.readTimeout(2)
				.callTimeout(3)
				.writeTimeout(4)
				.build();
		Assert.assertEquals(3, saClient.getCallTimeout());
		Assert.assertEquals(2, saClient.getReadTimeout());
		Assert.assertEquals(4, saClient.getWriteTimeout());
		Assert.assertEquals(1, saClient.getConnectTimeout());
	}

	/**
	 *    一句话调用服务
	 *
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		SaClient saClient = new SaClient.Builder(appId, apiKey)
				.build();
		System.out.println(saClient.send("你好，李焕英"));
	}


	@Test
	public void test1() throws Exception {
		SaClient saClient = new SaClient.Builder(appId, apiKey)
				.connectTimeout(3)
				.writeTimeout(2)
				.readTimeout(1)
				.build();
//		System.out.println(saClient.send("websdk java"));
	}


}
