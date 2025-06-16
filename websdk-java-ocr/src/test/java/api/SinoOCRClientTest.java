package api;

import cn.xfyun.api.SinoOCRClient;
import config.PropertiesConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * sino国内通用票据识别 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SinoOCRClientTest.class})
@PowerMockIgnore({"javax.crypto.*", "javax.net.ssl.*"})
public class SinoOCRClientTest {

    private static final Logger logger = LoggerFactory.getLogger(SinoOCRClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getSinoOcrClientApiKey();
    private static final String apiSecret = PropertiesConfig.getSinoOcrClientApiSecret();
    private String resourcePath = this.getClass().getResource("/").getPath();


    @Test
    public void testParams() {
        SinoOCRClient client = new SinoOCRClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        Assert.assertEquals(appId, client.getAppId());
        Assert.assertEquals(apiKey, client.getApiKey());
        Assert.assertEquals(apiSecret, client.getApiSecret());
        Assert.assertEquals("https://api.xf-yun.com/v1/private/sc45f0684", client.getHostUrl());
    }

    @Test
    public void testBuildParams() {
        SinoOCRClient client = new SinoOCRClient
                .Builder(appId, apiKey, apiSecret)
                .hostUrl("test.url")
                .format("format")
                .encoding("encoding")
                .compress("compress")
                .callTimeout(1)
                .readTimeout(2)
                .writeTimeout(3)
                .connectTimeout(4)
                .retryOnConnectionFailure(true)
                .build();
        Assert.assertEquals("test.url", client.getHostUrl());
        Assert.assertEquals("format", client.getFormat());
        Assert.assertEquals("encoding", client.getEncoding());
        Assert.assertEquals("compress", client.getCompress());
        Assert.assertEquals(1000, client.getCallTimeout());
        Assert.assertEquals(2000, client.getReadTimeout());
        Assert.assertEquals(3000, client.getWriteTimeout());
        Assert.assertEquals(4000, client.getConnectTimeout());
        Assert.assertEquals(true, client.getRetryOnConnectionFailure());
    }

    @Test
    public void testPlace() throws IOException {
        SinoOCRClient client = new SinoOCRClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/invoice.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        client.send(imageBase64, "jpg");
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
