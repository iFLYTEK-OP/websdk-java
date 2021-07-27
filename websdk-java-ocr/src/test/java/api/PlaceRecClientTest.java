package api;

import cn.xfyun.api.ImageRecClient;
import cn.xfyun.api.PlaceRecClient;
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
import java.util.Base64;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/27 10:23
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PlaceRecClientTest.class})
@PowerMockIgnore({"javax.crypto.*", "javax.net.ssl.*"})
public class PlaceRecClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getApiKey();
    private static final String apiSecret = PropertiesConfig.getApiSecret();

    private String resourcePath = this.getClass().getResource("/").getPath();


    @Test
    public void testParams() {
        PlaceRecClient client = new PlaceRecClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        Assert.assertEquals(appId, client.getAppId());
        Assert.assertEquals(apiKey, client.getApiKey());
        Assert.assertEquals("https://api.xf-yun.com/v1/private/s5833e7f6", client.getHostUrl());
    }

    @Test
    public void testBuildParams() {
        PlaceRecClient client = new PlaceRecClient
                .Builder(appId, apiKey, apiSecret)
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
    public void testPlace() throws IOException {
        PlaceRecClient client = new PlaceRecClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/car.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.send( imageBase64, "jpg"));
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
