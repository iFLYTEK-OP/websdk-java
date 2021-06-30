package api;

import cn.xfyun.api.SaClinet;
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
@PowerMockIgnore("cn.xfyun.util.HttpConnector")
public class SaClientTest {

	private static final String appId = PropertiesConfig.getAppId();
	private static final String apiKey = PropertiesConfig.getApiKey();

	/**
	 * 测试参数设置
	 */
	@Test
	public void testParams() {
		SaClinet saClinet = new SaClinet.Builder(appId, apiKey)
				.hostUrl("https://ltpapi.xfyun.cn/v2/sa")
				.connTimeout(1)
				.maxConnections(2)
				.retryCount(3)
				.socketTimeout(4)
				.build();
		assertEquals(appId, Whitebox.getInternalState(saClinet, "appId"));
		assertEquals(apiKey, Whitebox.getInternalState(saClinet, "apiKey"));
		assertEquals("https://ltpapi.xfyun.cn/v2/sa", Whitebox.getInternalState(saClinet, "hostUrl"));
	}

	@Test
	public void testParams1() {
		SaClinet saClinet = new SaClinet.Builder(appId, apiKey)
				.hostUrl("https://ltpapi.xfyun.cn/v2/sa")
				.connTimeout(1)
				.maxConnections(2)
				.retryCount(3)
				.socketTimeout(4)
				.build();
		Assert.assertEquals(2, saClinet.getConnTimeout());
		Assert.assertEquals(3, saClinet.getRetryCount());
		Assert.assertEquals("https://ltpapi.xfyun.cn/v2/sa", saClinet.getHostUrl());
	}

	/**
	 *    一句话调用服务
	 *
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		SaClinet saClinet = new SaClinet.Builder(appId, apiKey)
				.build();
		System.out.println(saClinet.send("你好，李焕英"));
	}


	@Test
	public void test1() throws Exception {
		SaClinet saClinet = new SaClinet.Builder(appId, apiKey)
				.maxConnections(2)
				.retryCount(3)
				.build();
		System.out.println(saClinet.send("websdk java"));
	}


}
