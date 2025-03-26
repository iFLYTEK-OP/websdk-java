package cn.xfyun.api;

import cn.xfyun.config.VoiceCloneLangEnum;
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
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

/**
 * 一句话复刻Client单元测试
 *
 * @author zyding6
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VoiceCloneClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class VoiceCloneClientTest {
    private static final Logger logger = LoggerFactory.getLogger(VoiceCloneClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getVoiceCloneAPPKey();
    private static final String apiSecret = PropertiesConfig.getVoiceCloneAPPSecret();

    @Test
    public void defaultParamTest() {
        VoiceCloneClient voiceCloneClient = new VoiceCloneClient.Builder()
                .signature("e5b0686_ttsclone-807526e8-azzys", VoiceCloneLangEnum.CN, appId, apiKey, apiSecret)
                .callTimeout(0, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .pingInterval(0, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .encoding("enc")
                .textEncoding("enc")
                .textFormat("format")
                .logRequest(Boolean.TRUE)
                .textCompress("compress")
                .hostUrl("test.url")
                .bgs(1)
                .rhy(1)
                .rdn(1)
                .reg(1)
                .sampleRate(1)
                .speed(1)
                .volume(1)
                .pitch(1)
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
        Assert.assertTrue(voiceCloneClient.getLogRequest());

        Assert.assertEquals(voiceCloneClient.getOriginHostUrl(), "test.url");
        Assert.assertEquals(voiceCloneClient.getTextEncoding(), "enc");
        Assert.assertEquals(voiceCloneClient.getEncoding(), "enc");
        Assert.assertEquals(voiceCloneClient.getTextCompress(), "compress");
        Assert.assertEquals(voiceCloneClient.getTextFormat(), "format");
        Assert.assertEquals(voiceCloneClient.getReg(), 1);
        Assert.assertEquals(voiceCloneClient.getBgs(), 1);
        Assert.assertEquals(voiceCloneClient.getRdn(), 1);
        Assert.assertEquals(voiceCloneClient.getRhy(), 1);
        Assert.assertEquals(voiceCloneClient.getSpeed(), 1);
        Assert.assertEquals(voiceCloneClient.getVolume(), 1);
        Assert.assertEquals(voiceCloneClient.getPitch(), 1);
        Assert.assertNotNull(voiceCloneClient.getResId());
        Assert.assertNotNull(voiceCloneClient.getLanguageId());

        Assert.assertNotNull(voiceCloneClient.getOkHttpClient());
    }

    @Test
    public void test() throws MalformedURLException, SignatureException, FileNotFoundException, UnsupportedEncodingException {
        String filePath = "src/test/resources/audio/voiceclone_" + UUID.randomUUID() + ".mp3";
        String text = "欢迎使用本语音合成测试文本，本测试旨在全面检验语音合成系统在准确性、流畅性以及自然度等多方面的性能表现。";
        // 正常流程默认参数
        VoiceCloneClient voiceCloneClient1 = new VoiceCloneClient.Builder()
                .signature("e5b0686_ttsclone-807526e8-azzys", VoiceCloneLangEnum.CN, appId, apiKey, apiSecret)
                .logRequest(Boolean.TRUE)
                .build();
        // 正常流程全部参数
        VoiceCloneClient voiceCloneClient2 = new VoiceCloneClient.Builder()
                .signature("e5b0686_ttsclone-807526e8-azzys", VoiceCloneLangEnum.CN, appId, apiKey, apiSecret)
                .logRequest(Boolean.TRUE)
                .speed(50).volume(50).pitch(50).bgs(0).rhy(0).reg(0).rdn(0)
                .encoding("speex-wb").sampleRate(16000)
                .textEncoding("utf-8").textCompress("raw").textFormat("plain")
                .build();
        // 测试不支持的语种以外的格式
        VoiceCloneClient voiceCloneClient3 = new VoiceCloneClient.Builder()
                .signature("e5b0686_ttsclone-807526e8-azzys", VoiceCloneLangEnum.CN, appId, apiKey, apiSecret)
                .logRequest(Boolean.TRUE)
                .build();
        // 测试不存在的resId的情况
        VoiceCloneClient voiceCloneClient4 = new VoiceCloneClient.Builder()
                .signature("e5b0686_ttsclone-807526e8-azzys", VoiceCloneLangEnum.CN, appId, apiKey, apiSecret)
                .logRequest(Boolean.TRUE)
                .build();
        // 测试鉴权失败的情况
        VoiceCloneClient voiceCloneClient5 = new VoiceCloneClient.Builder()
                .signature("e5b0686_ttsclone-807526e8-azzys", VoiceCloneLangEnum.CN, appId, apiKey, apiSecret)
                .logRequest(Boolean.TRUE)
                .build();

        send(text, voiceCloneClient1, filePath);
        send(text, voiceCloneClient2, filePath);
        send(text, voiceCloneClient3, filePath);
        send(text, voiceCloneClient4, filePath);
        send(text, voiceCloneClient5, filePath);
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

            @Override
            public void onPlay(byte[] bytes) {

            }
        });
    }
}