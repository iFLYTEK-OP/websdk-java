package api;

import cn.xfyun.api.ImageRecClient;
import cn.xfyun.config.ImageRecEnum;
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

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/27 10:15
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ImageRecClientTest.class})
@PowerMockIgnore({"javax.crypto.*", "javax.net.ssl.*"})
public class ImageRecClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getImageRecClientApiKey();

    private String resourcePath = this.getClass().getResource("/").getPath();

    @Test
    public void testParams() {
        ImageRecClient client = new ImageRecClient
                .Builder(appId, apiKey, ImageRecEnum.SCENE)
                .build();
        Assert.assertEquals(appId, client.getAppId());
        Assert.assertEquals(apiKey, client.getApiKey());
        Assert.assertEquals("http://tupapi.xfyun.cn/v1/scene", client.getHostUrl());
    }

    @Test
    public void testBuildParams() {
        ImageRecClient client = new ImageRecClient
                .Builder(appId, apiKey, ImageRecEnum.CURRENCY)
                .hostUrl("test.url")
                .callTimeout(1)
                .readTimeout(2)
                .writeTimeout(3)
                .connectTimeout(4)
                .retryOnConnectionFailure(true)
                .build();
        Assert.assertEquals("test.url", client.getHostUrl());
        Assert.assertEquals(1, client.getCallTimeout());
        Assert.assertEquals(2, client.getReadTimeout());
        Assert.assertEquals(3, client.getWriteTimeout());
        Assert.assertEquals(4, client.getConnectTimeout());
        Assert.assertEquals(true, client.getRetryOnConnectionFailure());
    }

    @Test
    public void testScene() throws IOException {
        ImageRecClient client = new ImageRecClient
                .Builder(appId, apiKey, ImageRecEnum.SCENE)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/car.jpg");
        System.out.println(client.send( "测试", imageByteArray));
    }

    @Test
    public void testCurrency() throws IOException {
        ImageRecClient client = new ImageRecClient
                .Builder(appId, apiKey, ImageRecEnum.CURRENCY)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/car.jpg");
        System.out.println(client.send( "测试", imageByteArray));
    }

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
