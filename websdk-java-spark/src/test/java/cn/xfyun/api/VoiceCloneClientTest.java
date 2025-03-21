package cn.xfyun.api;

import cn.xfyun.model.voiceclone.response.VoiceCloneResponse;
import cn.xfyun.service.voiceclone.AbstractVoiceCloneWebSocketListener;
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
@PrepareForTest({VoiceCloneClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class VoiceCloneClientTest {
    private static final Logger logger = LoggerFactory.getLogger(VoiceCloneClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getVoiceCloneAPPKey();
    private static final String apiSecret = PropertiesConfig.getVoiceCloneAPPSecret();

    @Test
    public void defaultParamTest() throws MalformedURLException, SignatureException {
        VoiceCloneClient voiceCloneClient = new VoiceCloneClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        Assert.assertEquals(voiceCloneClient.getAppId(), appId);
        Assert.assertEquals(voiceCloneClient.getApiKey(), apiKey);
        Assert.assertEquals(voiceCloneClient.getApiSecret(), apiSecret);
        Assert.assertEquals(voiceCloneClient.getOriginHostUrl(), "http://cn-huabei-1.xf-yun.com/v1/private/voice_clone");
        Assert.assertEquals(voiceCloneClient.getTextEncoding(), "utf-8");
        Assert.assertEquals(voiceCloneClient.getTextCompress(), "raw");
        Assert.assertEquals(voiceCloneClient.getTextFormat(), "plain");
        Assert.assertEquals(voiceCloneClient.getReg(), 0);
        Assert.assertEquals(voiceCloneClient.getRdn(), 0);
        Assert.assertEquals(voiceCloneClient.getRhy(), 0);
        Assert.assertEquals(voiceCloneClient.getEncoding(), "speex-wb");
        Assert.assertEquals(voiceCloneClient.getSampleRate(), 16000);
        Assert.assertNotNull(voiceCloneClient.getResId());
    }

    @Test
    public void test() throws MalformedURLException, SignatureException, FileNotFoundException, UnsupportedEncodingException {
        String filePath = "src/test/resources/audio/voiceclone_" + UUID.randomUUID() + ".mp3";
        String text = "欢迎使用本语音合成测试文本，本测试旨在全面检验语音合成系统在准确性、流畅性以及自然度等多方面的性能表现。";
        // 正常流程默认参数
        VoiceCloneClient voiceCloneClient1 = new VoiceCloneClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .resId("e5b0686_ttsclone-807526e8-azzys").languageId(0)
                .build();
        // 正常流程全部参数
        VoiceCloneClient voiceCloneClient2 = new VoiceCloneClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .resId("123456")
                .languageId(0).speed(50).volume(50).pitch(50).bgs(0).rhy(0).reg(0).rdn(0)
                .encoding("speex-wb").sampleRate(16000)
                .textEncoding("utf-8").textCompress("raw").textFormat("plain")
                .build();
        // 测试不支持的语种以外的格式
        VoiceCloneClient voiceCloneClient3 = new VoiceCloneClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .resId("123456").languageId(10)
                .build();
        // 测试不存在的resId的情况
        VoiceCloneClient voiceCloneClient4 = new VoiceCloneClient.Builder()
                .signature(appId, "123456", apiSecret)
                .resId("123456").languageId(0)
                .build();
        // 测试鉴权失败的情况
        VoiceCloneClient voiceCloneClient5 = new VoiceCloneClient.Builder()
                .signature(appId, "123456", apiSecret)
                .build();

        send(text, voiceCloneClient1, filePath);
        send(text, voiceCloneClient2, filePath);
        send(text, voiceCloneClient3, filePath);
        send(text, voiceCloneClient4, filePath);
        send(text, voiceCloneClient5, filePath);
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void send(String text, VoiceCloneClient voiceCloneClient, String filePath) throws MalformedURLException, SignatureException, FileNotFoundException, UnsupportedEncodingException {
        // 存放音频的文件
        File f = new File(filePath);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        voiceCloneClient.send(text, new AbstractVoiceCloneWebSocketListener(f) {
            @Override
            public void onSuccess(byte[] bytes) {
                assertNotNull(bytes);
                assertNotNull(f);
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
        });
    }
}