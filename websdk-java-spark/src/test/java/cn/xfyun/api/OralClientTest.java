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

import static org.junit.Assert.assertNotNull;

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
        OralClient oralClient = new OralClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        Assert.assertEquals(oralClient.getAppId(), appId);
        Assert.assertEquals(oralClient.getApiKey(), apiKey);
        Assert.assertEquals(oralClient.getApiSecret(), apiSecret);
        Assert.assertEquals(oralClient.getOriginHostUrl(), "https://cbm01.cn-huabei-1.xf-yun.com/v1/private/mcd9m97e6");
        Assert.assertEquals(oralClient.getOralLevel(), "mid");
        Assert.assertEquals(oralClient.getSparkAssist(), 1);
        Assert.assertEquals(oralClient.getBgs(), 0);
        Assert.assertEquals(oralClient.getReg(), 0);
        Assert.assertEquals(oralClient.getRdn(), 0);
        Assert.assertEquals(oralClient.getRhy(), 0);
        Assert.assertEquals(oralClient.getEncoding(), "lame");
        Assert.assertEquals(oralClient.getSampleRate(), 24000);
        Assert.assertEquals(oralClient.getChannels(), 1);
        Assert.assertEquals(oralClient.getBitDepth(), 16);
        Assert.assertEquals(oralClient.getFrameSize(), 0);
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
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void send(String text, OralClient oralClient, String filePath) throws MalformedURLException, SignatureException, FileNotFoundException, UnsupportedEncodingException {
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

            @Override
            public void onPlay(byte[] bytes) {

            }
        });
    }
}