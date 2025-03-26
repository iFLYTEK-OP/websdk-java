package cn.xfyun.api;

import cn.xfyun.model.CreateParam;
import cn.xfyun.model.CreateResult;
import cn.xfyun.model.ThemeResponse;
import cn.xfyun.util.StringUtils;
import config.PropertiesConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 图片生成Client单元测试
 *
 * @author zyding6
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AIPPTClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class AIPPTClientTest {
    private static final Logger logger = LoggerFactory.getLogger(AIPPTClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiSecret = PropertiesConfig.getAIPPTClientApiSecret();

    @Test
    public void testSuccess() {
        AIPPTClient.Builder builder = new AIPPTClient.Builder(appId, apiSecret)
                .readTimeout(10000).logRequest(Boolean.TRUE);
        AIPPTClient client = builder.build();
        
        try {
            String themeList = client.themeList();
            ThemeResponse themeResponse = StringUtils.gson.fromJson(themeList, ThemeResponse.class);
            logger.info("ppt主题列表：{}", StringUtils.gson.toJson(themeResponse));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        try {
            CreateParam createParam = new CreateParam.Builder()
                    .query("介绍一下中国的春节习俗")
                    .businessId(UUID.randomUUID().toString())
                    .build();
            String response = client.create(createParam);
            CreateResult createResult = StringUtils.gson.fromJson(response, CreateResult.class);
            logger.info("ppt生成首图结果：{}", StringUtils.gson.toJson(createResult));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        try {
            String response1 = client.progress("sid");
            logger.info("ppt生成进度信息：{}", response1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testParam() {
        AIPPTClient client = new AIPPTClient.Builder(appId, apiSecret)
                .readTimeout(10000)
                .hostUrl("https://zwapi.xfyun.cn/")
                .writeTimeout(10000)
                .apiKey("123")
                .apiSecret("456")
                .appId("wewqe123")
                .connectTimeout(10000)
                .retryOnConnectionFailure(true)
                .logRequest(Boolean.TRUE)
                .build();
        Assert.assertEquals(10000, client.getReadTimeout());
        Assert.assertEquals("https://zwapi.xfyun.cn/", client.getHostUrl());
        Assert.assertEquals(10000, client.getWriteTimeout());
        Assert.assertEquals("123", client.getApiKey());
        Assert.assertEquals("456", client.getApiSecret());
        Assert.assertEquals("wewqe123", client.getAppId());
        Assert.assertEquals(10000, client.getConnectTimeout());
        Assert.assertTrue("wewqe123", client.getRetryOnConnectionFailure());
        Assert.assertTrue(client.getLogRequest());
    }
}
