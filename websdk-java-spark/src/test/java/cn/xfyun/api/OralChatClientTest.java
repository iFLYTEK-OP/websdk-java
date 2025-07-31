package cn.xfyun.api;

import cn.xfyun.config.StreamMode;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.oralchat.OralChatParam;
import config.PropertiesConfig;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 超拟人交互 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SparkIatClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class OralChatClientTest {

    private static final Logger logger = LoggerFactory.getLogger(OralChatClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getSparkIatAPPKey();
    private static final String apiSecret = PropertiesConfig.getSparkIatAPPSecret();
    private final String filePath = "audio/16k_10.pcm";
    private final String resourcePath = Objects.requireNonNull(this.getClass().getResource("/")).getPath();

    @Test
    public void defaultParamTest() {
        OralChatClient client = new OralChatClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .callTimeout(0, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .pingInterval(0, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .hostUrl("test.url")
                .dwa("wpgs")
                .eos("7000")
                .frameSize(0)
                .textCompress("plain")
                .textEncoding("utf8")
                .textFormat("json")
                .bitDepthIn(16)
                .bitDepthOut(16)
                .channelsIn(1)
                .channelsOut(1)
                .encodingIn("raw")
                .encodingOut("raw")
                .sampleRateIn(24000)
                .sampleRateOut(24000)
                .vgap(80)
                .domain("domain")
                .okHttpClient(null)
                .build();

        Assert.assertEquals(client.getAppId(), appId);
        Assert.assertEquals(client.getApiKey(), apiKey);
        Assert.assertEquals(client.getApiSecret(), apiSecret);

        Assert.assertEquals(client.getCallTimeout(), 0);
        Assert.assertEquals(client.getConnectTimeout(), 10000);
        Assert.assertEquals(client.getWriteTimeout(), 10000);
        Assert.assertEquals(client.getReadTimeout(), 10000);
        Assert.assertEquals(client.getPingInterval(), 0);
        Assert.assertTrue(client.isRetryOnConnectionFailure());

        Assert.assertTrue(client.getOriginHostUrl().contains("test"));
        Assert.assertEquals(client.getDwa(), "wpgs");
        Assert.assertEquals(client.getEos(), "7000");
        Assert.assertEquals(client.getDomain(), "domain");
        Assert.assertEquals(client.getFrameSize(), 0);
        Assert.assertEquals(client.getTextCompress(), "plain");
        Assert.assertEquals(client.getTextEncoding(), "utf8");
        Assert.assertEquals(client.getTextFormat(), "json");
        Assert.assertEquals(client.getBitDepthIn(), 16);
        Assert.assertEquals(client.getBitDepthOut(), 16);
        Assert.assertEquals(client.getChannelsIn(), 1);
        Assert.assertEquals(client.getChannelsOut(), 1);
        Assert.assertEquals(client.getEncodingIn(), "raw");
        Assert.assertEquals(client.getEncodingOut(), "raw");
        Assert.assertEquals(client.getSampleRateIn(), 24000);
        Assert.assertEquals(client.getSampleRateOut(), 24000);
        Assert.assertEquals(client.getVgap(), 80);

        Assert.assertNotNull(client.getOkHttpClient());
    }

    @Test
    public void testErrorSignature() throws MalformedURLException, SignatureException {
        OralChatClient oralChatClient = new OralChatClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        OralChatParam param = new OralChatParam();
        try {
            oralChatClient.start(null, null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("参数不能为空"));
        }
        try {
            oralChatClient.start(param, null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("交互模式不能为空"));
        }
        try {
            param.setInteractMode(StreamMode.CONTINUOUS_VAD.getValue());
            oralChatClient.start(param, null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("uid不能为空"));
        }
    }

    @Test
    public void testContinuous() throws MalformedURLException, SignatureException {
        OralChatClient oralChatClient = new OralChatClient.Builder()
                .signature(appId, apiKey, apiSecret)
                // 流式实时返回转写结果 (仅中文支持)
                .dwa("wpgs")
                .build();

        OralChatParam param = OralChatParam.builder()
                .interactMode(StreamMode.CONTINUOUS.getValue())
                .uid("youtestuid")
                .build();

        // 建立链接
        WebSocket socket = oralChatClient.start(param, getListener());
        // 发送内容
        sendChat(oralChatClient, socket, param);
        // 发送结束帧
        oralChatClient.stop(socket);
    }

    @Test
    public void testContinuousVad() throws MalformedURLException, SignatureException {
        OralChatClient oralChatClient = new OralChatClient.Builder()
                .signature(appId, apiKey, apiSecret)
                // 流式实时返回转写结果 (仅中文支持)
                .dwa("wpgs")
                .build();

        OralChatParam param = OralChatParam.builder()
                .interactMode(StreamMode.CONTINUOUS_VAD.getValue())
                .uid("youtestuid")
                .build();

        // 建立链接
        WebSocket socket = oralChatClient.start(param, getListener());
        // 发送内容
        sendChat(oralChatClient, socket, param);
        // 发送结束帧
        oralChatClient.stop(socket);
    }

    /**
     * 发送本地文件语音内容
     */
    private void sendChat(OralChatClient oralChatClient, WebSocket socket, OralChatParam param) {
        try (RandomAccessFile raf = new RandomAccessFile(new File(resourcePath + filePath), "r")) {
            byte[] bytes = new byte[1280];
            int len;
            boolean first = true;
            while ((len = raf.read(bytes)) != -1) {
                if (len < 1280) {
                    bytes = Arrays.copyOfRange(bytes, 0, len);
                    if (first) {
                        oralChatClient.sendMsg(socket, bytes, 0);
                    } else {
                        oralChatClient.sendMsg(socket, bytes, 2);
                    }
                    break;
                }
                if (first) {
                    first = false;
                    oralChatClient.sendMsg(socket, bytes, 0);
                } else {
                    oralChatClient.sendMsg(socket, bytes, 1);
                }
                // 每隔40毫秒发送一次数据
                TimeUnit.MILLISECONDS.sleep(40);
            }
            // 发送结束标识
        } catch (Exception e) {
            logger.error("消息发送失败", e);
        }
    }

    private WebSocketListener getListener() {
        return new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                logger.info("websocket启动成功");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                logger.info("接收到消息: {}", text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                // 处理二进制消息
                logger.info("接收到消息: {}", bytes.string(StandardCharsets.UTF_8));
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                logger.info("websocket关闭");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
                logger.error("ws错误", t);
            }
        };
    }
}
