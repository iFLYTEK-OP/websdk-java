package api;

import cn.xfyun.api.LLMOcrClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.llmocr.LLMOcrParam;
import cn.xfyun.util.FileUtil;
import config.PropertiesConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

/**
 * 大模型通用文档识别 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LLMOcrClientTest.class})
@PowerMockIgnore({"javax.crypto.*", "javax.net.ssl.*"})
public class LLMOcrClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getjDOcrClientApiKey();
    private static final String apiSecret = PropertiesConfig.getjDOcrClientApiSecret();
    private String resourcePath = this.getClass().getResource("/").getPath();

    @Test
    public void testParams() {
        LLMOcrClient client = new LLMOcrClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        Assert.assertEquals(appId, client.getAppId());
        Assert.assertEquals(apiKey, client.getApiKey());
        Assert.assertEquals(apiSecret, client.getApiSecret());
        Assert.assertEquals("https://cbm01.cn-huabei-1.xf-yun.com/v1/private/se75ocrbm", client.getHostUrl());
    }

    @Test
    public void testBuildParams() {
        LLMOcrClient client = new LLMOcrClient
                .Builder(appId, apiKey, apiSecret)
                .hostUrl("test.url")
                .callTimeout(1)
                .readTimeout(2)
                .writeTimeout(3)
                .connectTimeout(4)
                .retryOnConnectionFailure(true)
                .imsi("imsi")
                .mac("mac")
                .alphaOption("alphaOption")
                .exifOption("exifOption")
                .netIsp("netIsp")
                .imei("imei")
                .jsonElementOption("jsonElementOption")
                .markdownElementOption("markdownElementOption")
                .netType("netType")
                .outputType("outputType")
                .resId("resId")
                .resultFormat("resultFormat")
                .resultOption("resultOption")
                .rotationMinAngle(0.0f)
                .sedElementOption("sedElementOption")
                .build();
        Assert.assertEquals("test.url", client.getHostUrl());
        Assert.assertEquals(1, client.getCallTimeout());
        Assert.assertEquals(2, client.getReadTimeout());
        Assert.assertEquals(3, client.getWriteTimeout());
        Assert.assertEquals(4, client.getConnectTimeout());
        Assert.assertEquals(true, client.getRetryOnConnectionFailure());
        Assert.assertEquals("alphaOption", client.getAlphaOption());
        Assert.assertEquals("exifOption", client.getExifOption());
        Assert.assertEquals("netIsp", client.getNetIsp());
        Assert.assertEquals("imei", client.getImei());
        Assert.assertEquals("jsonElementOption", client.getJsonElementOption());
        Assert.assertEquals("markdownElementOption", client.getMarkdownElementOption());
        Assert.assertEquals("netType", client.getNetType());
        Assert.assertEquals("outputType", client.getOutputType());
        Assert.assertEquals("resId", client.getResId());
        Assert.assertEquals("resultFormat", client.getResultFormat());
        Assert.assertEquals("resultOption", client.getResultOption());
        Assert.assertEquals(0.0f, client.getRotationMinAngle(), 0.01);
        Assert.assertEquals("sedElementOption", client.getSedElementOption());
        Assert.assertEquals("mac", client.getMac());
        Assert.assertEquals("imsi", client.getImsi());
    }

    @Test
    public void testError() throws IOException {
        LLMOcrClient client = new LLMOcrClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        try {
            client.send(null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("参数不能为空"));
        }
        try {
            LLMOcrParam param = LLMOcrParam.builder()
                    .build();
            client.send(param);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("图片信息不能为空"));
        }
    }

    @Test
    public void test() throws IOException {
        LLMOcrClient client = new LLMOcrClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        LLMOcrParam param = LLMOcrParam.builder()
                .imageBase64(FileUtil.fileToBase64(resourcePath + "/image/car.jpg"))
                .format("jpg")
                .build();
        client.send(param);
    }
}
