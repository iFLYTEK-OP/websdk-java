package api;

import cn.xfyun.api.FaceDetectClient;
import cn.xfyun.config.FaceDetectEnum;
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
import java.util.Base64;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/6 11:00
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FaceDetectClientTest.class})
@PowerMockIgnore({"javax.crypto.*", "javax.net.ssl.*"})
public class FaceDetectClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getApiKey();
    private static final String apiSecret = PropertiesConfig.getApiSecret();

    private String resourcePath = this.getClass().getResource("/").getPath();

    /**
     * 测试参数设置
     */
    @Test
    public void testParams() {
        FaceDetectClient client = new FaceDetectClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        Assert.assertEquals(appId, client.getAppId());
        Assert.assertEquals(apiKey, client.getApiKey());
        Assert.assertEquals(apiSecret, client.getApiSecret());
    }

    @Test
    public void testBuildParams() {
        FaceDetectClient client = new FaceDetectClient
                .Builder(appId, apiKey, apiSecret)
                .hostUrl("test.url")
                .detectPoints(FaceDetectEnum.ON)
                .detectProperty(FaceDetectEnum.ON)
                .compress("compress")
                .encoding("encoding")
                .format("format")
                .status(2)
                .callTimeout(1)
                .readTimeout(2)
                .writeTimeout(3)
                .connectTimeout(4)
                .retryOnConnectionFailure(true)
                .build();
        Assert.assertEquals("test.url", client.getHostUrl());
        Assert.assertEquals("12345", client.getServiceId());
        Assert.assertEquals("compress", client.getCompress());
        Assert.assertEquals("encoding", client.getEncoding());
        Assert.assertEquals("format", client.getFormat());
        Assert.assertEquals(1, client.getDetectPoints().getValue());
        Assert.assertEquals(1, client.getDetectProperty().getValue());
        Assert.assertEquals(2, client.getStatus());
        Assert.assertEquals(1, client.getCallTimeout());
        Assert.assertEquals(2, client.getReadTimeout());
        Assert.assertEquals(3, client.getWriteTimeout());
        Assert.assertEquals(4, client.getConnectTimeout());
        Assert.assertEquals(true, client.getRetryOnConnectionFailure());
    }


    @Test
    public void test() throws IOException {
        FaceDetectClient client = new FaceDetectClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/daiye2.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.faceContrast(imageBase64, "jpg"));
    }

    @Test
    public void test1() throws IOException {
        FaceDetectClient client = new FaceDetectClient
                .Builder(appId, apiKey, apiSecret)
                .detectPoints(FaceDetectEnum.ON)
                .detectProperty(FaceDetectEnum.ON)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/daiye2.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.faceContrast(imageBase64, "jpg"));
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
