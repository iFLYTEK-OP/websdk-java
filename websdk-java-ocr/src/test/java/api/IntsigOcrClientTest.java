package api;

import cn.xfyun.api.IntsigOcrClient;
import cn.xfyun.api.JDOcrClient;
import cn.xfyun.config.IdcardEnum;
import cn.xfyun.config.IntsigRecgEnum;
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
 * @date 2021/7/8 16:05
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({IntsigOcrClientTest.class})
@PowerMockIgnore({"javax.crypto.*", "javax.net.ssl.*"})
public class IntsigOcrClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getIntsigOcrClientApiKey();

    private String resourcePath = this.getClass().getResource("/").getPath();

    @Test
    public void testParams() {
        IntsigOcrClient client = new IntsigOcrClient
                .Builder(appId, apiKey, IntsigRecgEnum.IDCARD)
                .build();
        Assert.assertEquals(appId, client.getAppId());
        Assert.assertEquals(apiKey, client.getApiKey());
        Assert.assertEquals("https://webapi.xfyun.cn/v1/service/v1/ocr/", client.getHostUrl());
    }

    @Test
    public void testBuildParams() {
        IntsigOcrClient client = new IntsigOcrClient
                .Builder(appId, apiKey, IntsigRecgEnum.IDCARD)
                .hostUrl("test.url")
                .cropImage(IdcardEnum.ON)
                .headPortrait(IdcardEnum.ON)
                .idNumberImage(IdcardEnum.ON)
                .recognizeMode(IdcardEnum.ON)
                .callTimeout(1)
                .readTimeout(2)
                .writeTimeout(3)
                .connectTimeout(4)
                .retryOnConnectionFailure(true)
                .build();
        Assert.assertEquals("test.url", client.getHostUrl());
        Assert.assertEquals(IdcardEnum.ON, client.getCropImage());
        Assert.assertEquals(IdcardEnum.ON, client.getHeadPortrait());
        Assert.assertEquals(IdcardEnum.ON, client.getIdNumberImage());
        Assert.assertEquals(IdcardEnum.ON, client.getRecognizeMode());
        Assert.assertEquals(1, client.getCallTimeout());
        Assert.assertEquals(2, client.getReadTimeout());
        Assert.assertEquals(3, client.getWriteTimeout());
        Assert.assertEquals(4, client.getConnectTimeout());
        Assert.assertEquals(true, client.getRetryOnConnectionFailure());
    }

    @Test
    public void test() throws IOException {
        IntsigOcrClient client = new IntsigOcrClient
                .Builder(appId, apiKey, IntsigRecgEnum.IDCARD)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/car.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        try {
            System.out.println(client.intsigRecg(imageBase64));
        } catch (SocketTimeoutException e){
            System.out.println("SocketTimeoutException!");
        }
    }

    @Test
    public void test1() throws IOException {
        IntsigOcrClient client = new IntsigOcrClient
                .Builder(appId, apiKey, IntsigRecgEnum.BUSINESS_LICENSE)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/car.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        try {
            System.out.println(client.intsigRecg(imageBase64));
        } catch (SocketTimeoutException e){
            System.out.println("SocketTimeoutException!");
        }
    }

    @Test
    public void test2() throws IOException {
        IntsigOcrClient client = new IntsigOcrClient
                .Builder(appId, apiKey, IntsigRecgEnum.RECOGNIZE_DOCUMENT)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/car.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        try {
            System.out.println(client.intsigRecg(imageBase64));
        } catch (SocketTimeoutException e){
            System.out.println("SocketTimeoutException!");
        }
    }

    @Test
    public void test3() throws IOException {
        IntsigOcrClient client = new IntsigOcrClient
                .Builder(appId, apiKey, IntsigRecgEnum.INVOICE)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/car.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        try {
            System.out.println(client.intsigRecg(imageBase64));
        } catch (SocketTimeoutException e){
            System.out.println("SocketTimeoutException!");
        }
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
