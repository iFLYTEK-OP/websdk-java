package api;

import cn.xfyun.api.SilentDetectionClient;
import cn.xfyun.api.WatermarkVerificationClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;
import config.PropertiesConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/6 11:53
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({WatermarkVerificationClientTest.class})
@PowerMockIgnore({"javax.crypto.*", "javax.net.ssl.*"})
public class WatermarkVerificationClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getApiKey();

    private String resourcePath = this.getClass().getResource("/").getPath();

    /**
     * 测试参数设置
     */
    @Test
    public void testParams() {
        WatermarkVerificationClient client = new WatermarkVerificationClient
                .Builder(appId, apiKey)
                .build();
        Assert.assertEquals(appId, client.getAppId());
        Assert.assertEquals(apiKey, client.getApiKey());
        Assert.assertEquals("https://api.xfyun.cn/v1/service/v1/image_identify/face_verification", client.getHostUrl());
    }

    @Test
    public void testBuildParams() {
        WatermarkVerificationClient client = new WatermarkVerificationClient
                .Builder(appId, apiKey)
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
        WatermarkVerificationClient client = new WatermarkVerificationClient
                .Builder(appId, apiKey)
                .build();
        byte[] imageByteArray1 = read(resourcePath + "/image/1.jpg");
        String imageBase641 = Base64.getEncoder().encodeToString(imageByteArray1);
        byte[] imageByteArray2 = read(resourcePath + "/image/2.png");
        String imageBase642 = Base64.getEncoder().encodeToString(imageByteArray2);
        System.out.println(client.compare(imageBase641, imageBase642));

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
