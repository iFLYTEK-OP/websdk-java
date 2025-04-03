package cn.xfyun.api;

import cn.xfyun.model.oral.response.OralResponse;
import cn.xfyun.service.oral.AbstractOralWebSocketListener;
import config.PropertiesConfig;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

/**
 * 超拟人合成 Client单元测试
 *
 * @author zyding6
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({OralClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class OralClientTest {
    private static final Logger logger = LoggerFactory.getLogger(OralClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getOralAPPKey();
    private static final String apiSecret = PropertiesConfig.getOralAPPSecret();

    @Test
    public void defaultParamTest() throws MalformedURLException, SignatureException {
        OralClient client = new OralClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .callTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .pingInterval(0, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .hostUrl("test.url")
                .oralLevel("mid")
                .sparkAssist(0)
                .stopSplit(1)
                .remain(1)
                .vcn("x4_lingfeihong_oral")
                .speed(40)
                .volume(40)
                .pitch(40)
                .bgs(1)
                .reg(1)
                .rdn(1)
                .rhy(1)
                .encoding("raw")
                .sampleRate(16000)
                .channels(2)
                .bitDepth(24)
                .frameSize(1280)
                .textFormat("json")
                .build();

        Assert.assertEquals(client.getAppId(), appId);
        Assert.assertEquals(client.getApiKey(), apiKey);
        Assert.assertEquals(client.getApiSecret(), apiSecret);
        Assert.assertEquals(client.getCallTimeout(), 10000);
        Assert.assertEquals(client.getConnectTimeout(), 10000);
        Assert.assertEquals(client.getWriteTimeout(), 10000);
        Assert.assertEquals(client.getReadTimeout(), 10000);
        Assert.assertEquals(client.getPingInterval(), 0);
        Assert.assertTrue(client.isRetryOnConnectionFailure());
        Assert.assertEquals(client.getOriginHostUrl(), "test.url");
        Assert.assertEquals(client.getOralLevel(), "mid");
        Assert.assertEquals(client.getSparkAssist(), 0);
        Assert.assertEquals(client.getStopSplit(), 1);
        Assert.assertEquals(client.getRemain(), 1);
        Assert.assertEquals(client.getVcn(), "x4_lingfeihong_oral");
        Assert.assertEquals(client.getSpeed(), 40);
        Assert.assertEquals(client.getVolume(), 40);
        Assert.assertEquals(client.getPitch(), 40);
        Assert.assertEquals(client.getBgs(), 1);
        Assert.assertEquals(client.getReg(), 1);
        Assert.assertEquals(client.getRdn(), 1);
        Assert.assertEquals(client.getRhy(), 1);
        Assert.assertEquals(client.getEncoding(), "raw");
        Assert.assertEquals(client.getSampleRate(), 16000);
        Assert.assertEquals(client.getChannels(), 2);
        Assert.assertEquals(client.getBitDepth(), 24);
        Assert.assertEquals(client.getFrameSize(), 1280);
        Assert.assertEquals(client.getTextFormat(), "json");
    }

    @Test
    public void test() throws MalformedURLException, SignatureException, FileNotFoundException, UnsupportedEncodingException {
        String filePath = "src/test/resources/audio/oral_" + UUID.randomUUID() + ".mp3";
        String text = "检查签名的各个参数是否有缺失是否正确，特别确认下复制的api_key是否正确";
        // 正常流程
        OralClient oralClient1 = new OralClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .vcn("x4_lingfeizhe_oral").speed(50).volume(50).pitch(50).bgs(0).rhy(0).reg(0).rdn(0)
                .encoding("lame").sampleRate(24000).channels(1).bitDepth(16).frameSize(0)
                .build();
        // 测试除了mp3以外的格式
        OralClient oralClient2 = new OralClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .encoding("raw")
                .build();
        // 测试保存到相应文件的监听器构造方法
        OralClient oralClient3 = new OralClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();
        // 测试请求参数非法的情况
        OralClient oralClient4 = new OralClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .sampleRate(100000)
                .build();
        // 测试鉴权失败的情况
        OralClient oralClient5 = new OralClient.Builder()
                .signature(appId, "123", apiSecret)
                .build();


        send(text, oralClient1, filePath);
        send(text, oralClient2, filePath);
        send(text, oralClient3, filePath);
        send(text, oralClient4, filePath);
        send(text, oralClient5, filePath);
    }

    private void send(String text, OralClient oralClient, String filePath) throws MalformedURLException, SignatureException, FileNotFoundException, UnsupportedEncodingException {
        // 存放音频的文件
        File f = new File(filePath);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        oralClient.send(text, new AbstractOralWebSocketListener(f) {
            @Override
            public void onSuccess(byte[] bytes) {
                assertNotNull(bytes);
                assertNotNull(f);
            }

            @Override
            public void onClose(WebSocket webSocket, int code, String reason) {

            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {

            }

            @Override
            public void onBusinessFail(WebSocket webSocket, OralResponse response) {

            }
        });
    }
}