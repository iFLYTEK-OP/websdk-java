package cn.xfyun.api;

import cn.xfyun.config.LanguageEnum;
import cn.xfyun.config.VoiceStyleEnum;
import cn.xfyun.model.voiceclone.VoiceCloneParam;
import cn.xfyun.model.voiceclone.response.VoiceCloneResponse;
import cn.xfyun.service.voiceclone.AbstractVoiceCloneWebSocketListener;
import config.PropertiesConfig;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

/**
 * 一句话复刻（美化版、标准版）Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VoiceCloneV2Client.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class VoiceCloneV2ClientTest {

    private static final Logger logger = LoggerFactory.getLogger(VoiceCloneV2ClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getVoiceCloneAPPKey();
    private static final String apiSecret = PropertiesConfig.getVoiceCloneAPPSecret();

    @Test
    public void defaultParamTest() {
        VoiceCloneV2Client voiceCloneClient = new VoiceCloneV2Client.Builder()
                .signature(appId, apiKey, apiSecret)
                .callTimeout(0, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .pingInterval(0, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .encoding("enc")
                .textEncoding("enc")
                .textFormat("format")
                .textCompress("compress")
                .hostUrl("test.url")
                .status(2)
                .sampleRate(1)
                .build();

        Assert.assertEquals(voiceCloneClient.getAppId(), appId);
        Assert.assertEquals(voiceCloneClient.getApiKey(), apiKey);
        Assert.assertEquals(voiceCloneClient.getApiSecret(), apiSecret);

        Assert.assertEquals(voiceCloneClient.getCallTimeout(), 0);
        Assert.assertEquals(voiceCloneClient.getConnectTimeout(), 10000);
        Assert.assertEquals(voiceCloneClient.getWriteTimeout(), 10000);
        Assert.assertEquals(voiceCloneClient.getReadTimeout(), 10000);
        Assert.assertEquals(voiceCloneClient.getPingInterval(), 0);
        Assert.assertTrue(voiceCloneClient.isRetryOnConnectionFailure());

        Assert.assertEquals(voiceCloneClient.getOriginHostUrl(), "test.url");
        Assert.assertEquals(voiceCloneClient.getTextEncoding(), "enc");
        Assert.assertEquals(voiceCloneClient.getEncoding(), "enc");
        Assert.assertEquals(voiceCloneClient.getTextCompress(), "compress");
        Assert.assertEquals(voiceCloneClient.getTextFormat(), "format");
        Assert.assertEquals(voiceCloneClient.getStatus(), 2);

        Assert.assertNotNull(voiceCloneClient.getOkHttpClient());
    }

    @Test
    public void test() throws MalformedURLException, SignatureException, FileNotFoundException, UnsupportedEncodingException {
        String text = "欢迎使用本语音合成测试文本，本测试旨在全面检验语音合成系统在准确性、流畅性以及自然度等多方面的性能表现。";
        VoiceCloneV2Client client = new VoiceCloneV2Client.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        // 标准版
        VoiceCloneParam generalParam = VoiceCloneParam.builder()
                .text(text)
                .resId("a0b5e93_ttsclone-ab5552d9-ierkq")
                .vcn("x5_clone")
                .languageId(LanguageEnum.CHINESE.getCode())
                .build();
        client.send(generalParam, getListener());

        // 美化版
        VoiceCloneParam omniParam = VoiceCloneParam.builder()
                .text(text)
                .resId("d50f8d1_ttsclone-ab5552d9-urhek")
                .vcn("x6_clone")
                .style(VoiceStyleEnum.NEWS.getCode())
                .build();
        client.send(omniParam, getListener());
    }

    private WebSocketListener getListener() {
        return new AbstractVoiceCloneWebSocketListener() {
            @Override
            public void onSuccess(byte[] bytes) {
                assertNotNull(bytes);
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {

            }

            @Override
            public void onBusinessFail(WebSocket webSocket, VoiceCloneResponse response) {

            }

            @Override
            public void onClose(WebSocket webSocket, int code, String reason) {

            }
        };
    }
}