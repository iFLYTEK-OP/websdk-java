package cn.xfyun.api;

import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.model.response.ltp.LtpResponse;
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
	private static final String apiKey = PropertiesConfig.getLtpKey();

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

	/**
	 *    一句话调用服务
	 *
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		SaClinet saClinet = new SaClinet.Builder(appId, apiKey)
				.build();
		System.out.println(saClinet.send("我来自北方"));
	}

}
