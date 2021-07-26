package cn.xfyun.api;

import config.PropertiesConfig;
import cn.xfyun.exception.HttpException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import sun.misc.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author: <flhong2@iflytek.com>
 * @description: 歌曲识别测试
 * @version: v1.0
 * @create: 2021-06-04 11:02
 **/
@RunWith(PowerMockRunner.class)
@PrepareForTest({IatClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class QbhClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getApiKey();
    String filePath = "src/test/resources/audio/audio_qbh.wav";

    @Test
    public void defaultParamTest() {
        QbhClient qbhClient = new QbhClient.Builder(appId, apiKey)
                .build();
        Assert.assertEquals(qbhClient.getAppId(), appId);
        Assert.assertEquals(qbhClient.getApiKey(), apiKey);
        Assert.assertEquals(qbhClient.getAue(), "raw");
        Assert.assertEquals(qbhClient.getEngineType(), "afs");
        Assert.assertEquals(qbhClient.getSampleRate(), "16000");

    }

    @Test
    public void testParamBuild() {
        QbhClient qbhClient = new QbhClient
                .Builder(appId, apiKey)
                .aue("aac")
                .engineType("afs")
                .sampleRate("8000")
                .build();

        Assert.assertEquals(qbhClient.getAppId(), appId);
        Assert.assertEquals(qbhClient.getApiKey(), apiKey);
        Assert.assertEquals(qbhClient.getAue(), "aac");
        Assert.assertEquals(qbhClient.getEngineType(), "afs");
        Assert.assertEquals(qbhClient.getSampleRate(), "8000");

    }

    @Test
    public void testSuccessByBytes() throws IOException {
        QbhClient qbhClient = new QbhClient
                .Builder(appId, apiKey)
                .build();
        File file = new File(filePath);
        byte[] buffer = Files.readAllBytes(Paths.get(file.getPath()));
        String result = qbhClient.send(buffer);
        System.out.println("返回结果: " + result);
    }

}
