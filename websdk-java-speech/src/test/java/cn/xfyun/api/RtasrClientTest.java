package cn.xfyun.api;

import config.PropertiesConfig;
import cn.xfyun.service.rta.AbstractRtasrWebSocketListener;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.annotation.Nullable;
import java.io.*;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;


/**
 *  实时语音转写测试
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/3/23 15:48
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RtasrClientTest.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class RtasrClientTest {

	private static final String appId = PropertiesConfig.getAppId();
	private static final String apiKey = PropertiesConfig.getApiKey();

	private String filePath = "audio/rtasr.pcm";
	private String resourcePath = this.getClass().getResource("/").getPath();

	@Test
	public void defaultParamTest() {
		RtasrClient rtasrClient = new RtasrClient
				.Builder(appId, apiKey)
				.build();
		Assert.assertEquals(rtasrClient.getAppId(), appId);
		Assert.assertEquals(rtasrClient.getApiKey(), apiKey);
		Assert.assertTrue(rtasrClient.getHostUrl().contains("rtasr.xfyun.cn/v1/ws"));
	}

	@Test
	public void testParamBuild() {
		RtasrClient rtasrClient = new RtasrClient.Builder(appId, apiKey)
				.hostUrl("http://www.iflytek.com")
				.punc()
				.pd("edu")
				.callTimeout(1)
				.connectTimeout(2)
				.readTimeout(3)
				.writeTimeout(4)
				.pingInterval(5)
				.retryOnConnectionFailure(false)
				.build();
		Assert.assertTrue(rtasrClient.getHostUrl().contains("http://www.iflytek.com"));
		Assert.assertEquals(rtasrClient.getPd(), "edu");
		Assert.assertEquals(rtasrClient.getPunc(), "0");
		Assert.assertEquals(rtasrClient.getCallTimeout(), 1);
		Assert.assertEquals(rtasrClient.getConnectTimeout(), 2);
		Assert.assertEquals(rtasrClient.getReadTimeout(), 3);
		Assert.assertEquals(rtasrClient.getWriteTimeout(), 4);
		Assert.assertEquals(rtasrClient.getPingInterval(), 5);
		Assert.assertFalse(rtasrClient.isRetryOnConnectionFailure());
	}


	@Test
	public void testSuccess() throws FileNotFoundException, InterruptedException {
		RtasrClient rtasrClient = new RtasrClient
				.Builder(appId, apiKey)
				.build();
		String AUDIO_FILE_PATH = resourcePath + "/audio/rtasr.pcm";
		FileInputStream inputStream = new FileInputStream(AUDIO_FILE_PATH);
		CountDownLatch latch = new CountDownLatch(1);
		rtasrClient.send(inputStream, new AbstractRtasrWebSocketListener() {
			@Override
			public void onSuccess(WebSocket webSocket, String text) {
				System.out.println(text);
			}

			@Override
			public void onFail(WebSocket webSocket, Throwable t, @Nullable Response response) {
				System.out.println(response.code());
				latch.countDown();
			}

			@Override
			public void onBusinessFail(WebSocket webSocket, String text) {
				System.out.println(text);
				latch.countDown();
			}

			@Override
			public void onClosed() {
				latch.countDown();
			}
		});

		latch.await();

	}



	@Test
	public void testSendByte() throws SignatureException, IOException, InterruptedException {
		RtasrClient rtasrClient = new RtasrClient.Builder(appId, apiKey).build();
		File file = new File(resourcePath + filePath);
		FileInputStream inputStream = new FileInputStream(file);
		byte[] buffer = new byte[1024000];
		CountDownLatch latch = new CountDownLatch(1);
		inputStream.read(buffer);
		rtasrClient.send(buffer, new AbstractRtasrWebSocketListener() {
			@Override
			public void onSuccess(WebSocket webSocket, String text) {
				System.out.println(text);
			}

			@Override
			public void onFail(WebSocket webSocket, Throwable t, @Nullable Response response) {
				System.out.println(response.code());
				latch.countDown();
			}

			@Override
			public void onBusinessFail(WebSocket webSocket, String text) {
				System.out.println(text);
				latch.countDown();
			}

			@Override
			public void onClosed() {
				latch.countDown();
			}

		});
		latch.await();
	}

	@Test
	public void testSendCustom() throws InterruptedException {
		RtasrClient rtasrClient = new RtasrClient
				.Builder(appId, apiKey)
				.build();
		CountDownLatch latch = new CountDownLatch(1);

		WebSocket webSocket = rtasrClient.newWebSocket(new AbstractRtasrWebSocketListener() {
			@Override
			public void onSuccess(WebSocket webSocket, String text) {
				System.out.println(text);
			}

			@Override
			public void onFail(WebSocket webSocket, Throwable t, @Nullable Response response) {
				System.out.println("onFail");
				latch.countDown();
			}

			@Override
			public void onBusinessFail(WebSocket webSocket, String text) {
				System.out.println(text);
				latch.countDown();
			}

			@Override
			public void onClosed() {
				System.out.println("onClosed");
				latch.countDown();
			}
		});

		try {
			byte[] bytes = new byte[1280];
			String AUDIO_FILE_PATH = resourcePath + "/audio/rtasr.pcm";
			RandomAccessFile raf = new RandomAccessFile(AUDIO_FILE_PATH, "r");
			int len = -1;
			long lastTs = 0;
			while ((len = raf.read(bytes)) != -1) {
				if (len < 1280) {
					bytes = Arrays.copyOfRange(bytes, 0, len);
					webSocket.send(ByteString.of(bytes));
					break;
				}

				long curTs = System.currentTimeMillis();
				if (lastTs == 0) {
					lastTs = System.currentTimeMillis();
				} else {
					long s = curTs - lastTs;
					if (s < 40) {
						System.out.println("error time interval: " + s + " ms");
					}
				}
				webSocket.send(ByteString.of(bytes));
				// 每隔40毫秒发送一次数据
				Thread.sleep(40);
			}
			// 发送结束标识
			rtasrClient.sendEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}
		latch.await();
	}

}
