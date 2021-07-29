package api;

import cn.xfyun.api.BankcardClient;
import cn.xfyun.api.JDOcrClient;
import cn.xfyun.config.JDRecgEnum;
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
import java.util.Base64;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/8 15:19
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JDOcrClientTest.class})
@PowerMockIgnore({"javax.crypto.*", "javax.net.ssl.*"})
public class JDOcrClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getjDOcrClientApiKey();
    private static final String apiSecret = PropertiesConfig.getjDOcrClientApiSecret();

    private String resourcePath = this.getClass().getResource("/").getPath();

    @Test
    public void testParams() {
        JDOcrClient client = new JDOcrClient
                .Builder(appId, apiKey, apiSecret, JDRecgEnum.JD_OCR_CAR)
                .build();
        Assert.assertEquals(appId, client.getAppId());
        Assert.assertEquals(apiKey, client.getApiKey());
        Assert.assertEquals("https://api.xf-yun.com/v1/private/", client.getHostUrl());
    }

    @Test
    public void testBuildParams() {
        JDOcrClient client = new JDOcrClient
                .Builder(appId, apiKey, apiSecret, JDRecgEnum.JD_OCR_CAR)
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
    public void test() throws IOException {
        JDOcrClient client = new JDOcrClient
                .Builder(appId, apiKey, apiSecret, JDRecgEnum.JD_OCR_CAR)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/car.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        try {
            System.out.println(client.handle(imageBase64, "jpg"));
        } catch (SocketTimeoutException e){
            System.out.println("SocketTimeoutException!");
        }
    }

    @Test
    public void test1() throws IOException {
        JDOcrClient client = new JDOcrClient
                .Builder(appId, apiKey, apiSecret, JDRecgEnum.JD_OCR_DRIVER)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/car.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        try {
            System.out.println(client.handle(imageBase64, "jpg"));
        } catch (SocketTimeoutException e){
            System.out.println("SocketTimeoutException!");
        }
    }

    @Test
    public void test2() throws IOException {
        JDOcrClient client = new JDOcrClient
                .Builder(appId, apiKey, apiSecret, JDRecgEnum.JD_OCR_VEHICLE)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/car.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        try {
            System.out.println(client.handle(imageBase64, "jpg"));
        } catch (SocketTimeoutException e){
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
