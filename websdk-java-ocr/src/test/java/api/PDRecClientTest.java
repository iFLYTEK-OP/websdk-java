package api;

import cn.xfyun.api.PDRecClient;
import cn.xfyun.config.DocumentEnum;
import cn.xfyun.model.document.PDRecParam;
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
import java.security.SignatureException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * 图片还原文档 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PDRecClientTest.class})
@PowerMockIgnore({"javax.crypto.*", "javax.net.ssl.*"})
public class PDRecClientTest {

    private static final Logger logger = LoggerFactory.getLogger(PDRecClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getPdRecClientApiKey();
    private static final String apiSecret = PropertiesConfig.getPdRecClientApiSecret();
    private String resourcePath = this.getClass().getResource("/").getPath();


    @Test
    public void testParams() {
        PDRecClient client = new PDRecClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();
        Assert.assertEquals(appId, client.getAppId());
        Assert.assertEquals(apiKey, client.getApiKey());
        Assert.assertEquals(apiSecret, client.getApiSecret());
        Assert.assertEquals("https://ws-api.xf-yun.com/v1/private/ma008db16", client.getOriginHostUrl());
    }

    @Test
    public void testBuildParams() {
        PDRecClient client = new PDRecClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .hostUrl("test.url")
                .format("format")
                .encoding("encoding")
                .compress("compress")
                .category("category")
                .callTimeout(1, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .connectTimeout(4, TimeUnit.SECONDS)
                .pingInterval(5, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        Assert.assertEquals("test.url", client.getOriginHostUrl());
        Assert.assertEquals("format", client.getFormat());
        Assert.assertEquals("encoding", client.getEncoding());
        Assert.assertEquals("compress", client.getCompress());
        Assert.assertEquals("category", client.getCategory());
        Assert.assertEquals(1000, client.getCallTimeout());
        Assert.assertEquals(2000, client.getReadTimeout());
        Assert.assertEquals(3000, client.getWriteTimeout());
        Assert.assertEquals(4000, client.getConnectTimeout());
        Assert.assertEquals(5000, client.getPingInterval());
        Assert.assertEquals(true, client.isRetryOnConnectionFailure());
    }

    @Test
    public void testPlace() throws IOException {
        PDRecClient client = new PDRecClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/doc.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        PDRecParam param = PDRecParam.builder()
                .imgFormat("jpg")
                .imgBase64(imageBase64)
                .resultType(DocumentEnum.DOC.getCode())
                .build();
        try {
            client.send(param);
        } catch (SignatureException e) {
            logger.error("鉴权失败", e);
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
