package api;

import cn.xfyun.api.TupApiClient;
import cn.xfyun.config.TupApiEnum;
import config.PropertiesConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/6 11:57
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TupApiClientTest.class})
@PowerMockIgnore({"javax.crypto.*", "javax.net.ssl.*"})
public class TupApiClientTest {
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getTupApiClientApiKey();

    private String resourcePath = this.getClass().getResource("/").getPath();

    /**
     * 测试参数设置
     */
    @Test
    public void testParams() {
        TupApiClient client = new TupApiClient
                .Builder(appId, apiKey, TupApiEnum.AGE)
                .build();
        Assert.assertEquals(appId, client.getAppId());
        Assert.assertEquals(apiKey, client.getApiKey());
        Assert.assertEquals("http://tupapi.xfyun.cn/v1/", client.getHostUrl());
        Assert.assertEquals(TupApiEnum.AGE, client.getFunc());
    }

    @Test
    public void testBuildParams() {
        TupApiClient client = new TupApiClient
                .Builder(appId, apiKey, TupApiEnum.AGE)
                .func(TupApiEnum.SEX)
                .hostUrl("test.url")
                .callTimeout(1)
                .readTimeout(2)
                .writeTimeout(3)
                .connectTimeout(4)
                .retryOnConnectionFailure(true)
                .build();
        Assert.assertEquals(TupApiEnum.SEX, client.getFunc());
        Assert.assertEquals("test.url", client.getHostUrl());
        Assert.assertEquals(1, client.getCallTimeout());
        Assert.assertEquals(2, client.getReadTimeout());
        Assert.assertEquals(3, client.getWriteTimeout());
        Assert.assertEquals(4, client.getConnectTimeout());
        Assert.assertTrue(client.getRetryOnConnectionFailure());
    }

    @Test
    public void testAge() throws IOException {
        TupApiClient client = new TupApiClient
                .Builder(appId, apiKey, TupApiEnum.AGE)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/12.jpg");
        try {
            Assert.assertNotNull(client.recognition("12.jpg", imageByteArray));
        } catch (SocketTimeoutException e) {
            System.out.println("SocketTimeoutException!");
        }
    }

    @Test
    public void testSex() throws IOException {
        TupApiClient client = new TupApiClient
                .Builder(appId, apiKey, TupApiEnum.SEX)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/12.jpg");
        try {
            Assert.assertNotNull(client.recognition("12.jpg", imageByteArray));
        } catch (SocketTimeoutException e) {
            System.out.println("SocketTimeoutException!");
        }
    }

    @Test
    public void testFaceScore() throws IOException {
        TupApiClient client = new TupApiClient
                .Builder(appId, apiKey, TupApiEnum.FACE_SCORE)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/12.jpg");
        try {
            Assert.assertNotNull(client.recognition("12.jpg", imageByteArray));
        } catch (SocketTimeoutException e) {
            System.out.println("SocketTimeoutException!");
        }
    }

    @Test
    public void testExpression() throws IOException {
        TupApiClient client = new TupApiClient
                .Builder(appId, apiKey, TupApiEnum.EXPRESSION)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/12.jpg");
        try {
            Assert.assertNotNull(client.recognition("12.jpg", imageByteArray));
        } catch (SocketTimeoutException e) {
            System.out.println("SocketTimeoutException!");
        }
    }

    /**
     * 流转二进制数组
     *
     * @param in
     * @return
     * @throws IOException
     */
    private static byte[] inputStream2ByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    private static byte[] read(String filePath) throws IOException {
        InputStream in = new FileInputStream(filePath);
        byte[] data = inputStream2ByteArray(in);
        in.close();
        return data;
    }
}
