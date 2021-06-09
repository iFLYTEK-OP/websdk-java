package cn.xfyun.api;

import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.exception.HttpException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;

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
    String filePath = "D:\\work\\workspace\\data\\voice\\output\\cut_0.wav";

    @Test
    public void defaultParamTest() throws IOException, HttpException {
        QbhClient qbhClient = new QbhClient.Builder()
                .appId(appId).apiKey(apiKey)
                .build();

        Assert.assertEquals(qbhClient.getAppId(), appId);
        Assert.assertEquals(qbhClient.getApiKey(), apiKey);
        Assert.assertEquals(qbhClient.getAue(), "raw");
        Assert.assertEquals(qbhClient.getEngineType(), "afs");
        Assert.assertEquals(qbhClient.getSampleRate(), "16000");

    }

    @Test
    public void testParamBuild() {
        QbhClient qbhClient = new QbhClient.Builder()
                .appId(appId).apiKey(apiKey)
                .aue("aac").engineType("afs").sampleRate("8000")
                .build();

        Assert.assertEquals(qbhClient.getAppId(), appId);
        Assert.assertEquals(qbhClient.getApiKey(), apiKey);
        Assert.assertEquals(qbhClient.getAue(), "aac");
        Assert.assertEquals(qbhClient.getEngineType(), "afs");
        Assert.assertEquals(qbhClient.getSampleRate(), "8000");
    }

    @Test
    public void testSuccessByFile() throws IOException, HttpException {
        QbhClient qbhClient = new QbhClient.Builder()
                .appId(appId).apiKey(apiKey)
                .aue("raw").engineType("afs").sampleRate("16000")
                .build();
        String result = qbhClient.send(new File(filePath));
        System.out.println("返回结果: " + result);
    }

    @Test
    public void testSuccessByAudioUrl() throws IOException, HttpException {
        QbhClient qbhClient = new QbhClient.Builder()
                .appId(appId).apiKey(apiKey)
                .aue("raw").engineType("afs").sampleRate("16000").audioUrl("http://iat-hotwords.cn-bj.ufileos.com/9.wav")
                .build();
        String result = qbhClient.send();
        System.out.println("返回结果: " + result);
    }

    @Test
    public void testSuccessByStream() throws IOException, HttpException {
        QbhClient qbhClient = new QbhClient.Builder()
                .appId(appId).apiKey(apiKey)
                .aue("raw").engineType("afs").sampleRate("16000")
                .build();
        InputStream inputStream = new FileInputStream(new File(filePath));
        String result = qbhClient.send(inputStream);
        System.out.println("返回结果: " + result);
    }

    @Test
    public void testSuccessByString() throws IOException, HttpException {
        QbhClient qbhClient = new QbhClient.Builder()
                .appId(appId).apiKey(apiKey)
                .aue("raw").engineType("afs").sampleRate("16000")
                .build();
        InputStream inputStream = new FileInputStream(new File(filePath));

        String datas = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        try {
            while ((i = inputStream.read()) != -1) {
                baos.write(i);
            }
            datas = baos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = qbhClient.send(datas);
        System.out.println("返回结果: " + result);
    }

    @Test
    public void testSuccessByBytes() throws IOException, HttpException {
        QbhClient qbhClient = new QbhClient.Builder()
                .appId(appId).apiKey(apiKey)
                .aue("raw").engineType("afs").sampleRate("16000")
                .build();

        InputStream inputStream = new FileInputStream(new File(filePath));
        byte[] buffer = new byte[102400];
        int len = inputStream.read(buffer);
        System.out.println("len:" + len);
        String result = qbhClient.send(buffer);
        System.out.println("返回结果: " + result);
    }

    @Test
    public void testSendNull() throws IOException, HttpException {
        QbhClient qbhClient = new QbhClient.Builder()
                .appId(appId).apiKey(apiKey)
                .aue("raw").engineType("afs").sampleRate("16000")
                .build();

        String result = qbhClient.send("");
        System.out.println("返回结果: " + result);
    }
}
