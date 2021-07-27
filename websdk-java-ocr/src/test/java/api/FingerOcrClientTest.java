package api;

import cn.xfyun.api.FingerOcrClient;
import cn.xfyun.api.IntsigOcrClient;
import cn.xfyun.config.IdcardEnum;
import cn.xfyun.config.IntsigRecgEnum;
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
 * @date 2021/7/8 16:43
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({IntsigOcrClientTest.class})
@PowerMockIgnore({"javax.crypto.*", "javax.net.ssl.*"})
public class FingerOcrClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getFingerOcrClientApiKey();
    private static final String apiSecret = PropertiesConfig.getFingerOcrClientApiSecret();

    private String resourcePath = this.getClass().getResource("/").getPath();

    @Test
    public void testParams() {
        FingerOcrClient client = new FingerOcrClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        Assert.assertEquals(appId, client.getAppId());
        Assert.assertEquals(apiKey, client.getApiKey());
        Assert.assertEquals(apiSecret, client.getApiSecret());
        Assert.assertEquals("https://tyocr.xfyun.cn/v2/ocr", client.getHostUrl());
    }

    @Test
    public void testBuildParams() {
        FingerOcrClient client = new FingerOcrClient
                .Builder(appId, apiKey, apiSecret)
                .hostUrl("test.url")
                .cutHScale(1.0f)
                .cutWScale(1.0f)
                .cutShift(1.0f)
                .resizeH(1)
                .resizeW(1)
                .callTimeout(1)
                .readTimeout(2)
                .writeTimeout(3)
                .connectTimeout(4)
                .retryOnConnectionFailure(true)
                .build();
        Assert.assertEquals("test.url", client.getHostUrl());
        Assert.assertEquals(1.0, client.getCutHScale(), 1.0f);
        Assert.assertEquals(1.0, client.getCutWScale(), 1.0f);
        Assert.assertEquals(1.0, client.getCutShift(), 1.0f);
        Assert.assertEquals(1, client.getResizeH());
        Assert.assertEquals(1, client.getResizeW());
        Assert.assertEquals(1, client.getCallTimeout());
        Assert.assertEquals(2, client.getReadTimeout());
        Assert.assertEquals(3, client.getWriteTimeout());
        Assert.assertEquals(4, client.getConnectTimeout());
        Assert.assertEquals(true, client.getRetryOnConnectionFailure());
    }

    @Test
    public void test() throws Exception {
        FingerOcrClient client = new FingerOcrClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/finger.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.fingerOcr(imageBase64));
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
