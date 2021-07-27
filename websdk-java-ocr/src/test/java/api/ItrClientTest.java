package api;

import cn.xfyun.api.IntsigOcrClient;
import cn.xfyun.api.ItrClient;
import cn.xfyun.config.IntsigRecgEnum;
import cn.xfyun.config.ItrEntEnum;
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
 * @date 2021/7/8 16:35
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({IntsigOcrClientTest.class})
@PowerMockIgnore({"javax.crypto.*", "javax.net.ssl.*"})
public class ItrClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getItrClientApiKey();
    private static final String apiSecret = PropertiesConfig.getItrClientApiSecret();

    private String resourcePath = this.getClass().getResource("/").getPath();

    @Test
    public void testParams() {
        ItrClient client = new ItrClient
                .Builder(appId, apiKey, apiSecret, ItrEntEnum.MATH_ARITH)
                .build();
        Assert.assertEquals(appId, client.getAppId());
        Assert.assertEquals(apiKey, client.getApiKey());
        Assert.assertEquals("https://rest-api.xfyun.cn/v2/itr", client.getHostUrl());
    }

    @Test
    public void testBuildParams() {
        ItrClient client = new ItrClient
                .Builder(appId, apiKey, apiSecret, ItrEntEnum.MATH_ARITH)
                .hostUrl("test.url")
                .callTimeout(1)
                .readTimeout(2)
                .writeTimeout(3)
                .connectTimeout(4)
                .retryOnConnectionFailure(true)
                .build();
        Assert.assertEquals(appId, client.getAppId());
        Assert.assertEquals(apiKey, client.getApiKey());
        Assert.assertEquals("test.url", client.getHostUrl());
        Assert.assertEquals(1, client.getCallTimeout());
        Assert.assertEquals(2, client.getReadTimeout());
        Assert.assertEquals(3, client.getWriteTimeout());
        Assert.assertEquals(4, client.getConnectTimeout());
        Assert.assertEquals(true, client.getRetryOnConnectionFailure());
    }

    @Test
    public void test() throws Exception {
        ItrClient client = new ItrClient
                .Builder(appId, apiKey, apiSecret, ItrEntEnum.MATH_ARITH)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/itr.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.itr(imageBase64));
    }

    @Test
    public void test1() throws Exception {
        ItrClient client = new ItrClient
                .Builder(appId, apiKey, apiSecret, ItrEntEnum.TEACH_PHOTO_PRINT)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/itr.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.itr(imageBase64));
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
