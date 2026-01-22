package cn.xfyun.api;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.websearch.WebSearchParam;
import config.PropertiesConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 聚合搜索Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({WebSearchClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class WebSearchClientTest {

    private static final Logger logger = LoggerFactory.getLogger(WebSearchClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiPassword = PropertiesConfig.getWebSearchPassword();

    @Test
    public void buildParam() {
        WebSearchClient client = new WebSearchClient
                .Builder(appId, apiPassword)
                .build();
        Assert.assertEquals(apiPassword, client.getApiPassword());
    }

    @Test
    public void testBusinessError() throws IOException {
        WebSearchClient client = new WebSearchClient
                .Builder(appId, apiPassword)
                .build();
        WebSearchParam param = WebSearchParam.builder()
                .build();
        try {
            client.send(null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("请求参数不能为空"));
        }
        try {
            client.send(param);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("搜索内容不能为空"));
        }
    }

    @Test
    public void testSuccess() throws IOException {
        WebSearchClient client = new WebSearchClient
                .Builder(appId, apiPassword)
                .build();

        WebSearchParam param = WebSearchParam.builder()
                .query("现有鸡还是先有蛋")
                .build();

        logger.info("请求地址：{}", client.getHostUrl());
        String resp = client.send(param);
        logger.info("请求返回结果：{}", resp);
    }
}
