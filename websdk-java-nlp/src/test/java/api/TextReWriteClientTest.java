package api;

import cn.xfyun.api.TextRewriteClient;
import cn.xfyun.exception.BusinessException;
import config.PropertiesConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文本改写 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TextRewriteClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class TextReWriteClientTest {

    private static final Logger logger = LoggerFactory.getLogger(TextReWriteClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getTextReWriteClientApiKey();
    private static final String apiSecret = PropertiesConfig.getTextReWriteClientApiSecret();

    @Test
    public void defaultParamTest() {
        TextRewriteClient client = new TextRewriteClient
                .Builder(appId, apiKey, apiSecret).build();
        Assert.assertEquals(client.getAppId(), appId);
        Assert.assertEquals(client.getApiKey(), apiKey);
        Assert.assertEquals(client.getApiSecret(), apiSecret);
        Assert.assertEquals(3, client.getReadTimeout());
        Assert.assertEquals(3, client.getWriteTimeout());
        Assert.assertEquals(3, client.getConnectTimeout());
    }

    @Test
    public void testParamBuild() {
        TextRewriteClient client = new TextRewriteClient
                .Builder(appId, apiKey, apiSecret)
                .level("L1")
                .build();
        Assert.assertEquals("L1", client.getLevel());
    }


    @Test
    public void testSuccess() throws Exception {
        TextRewriteClient correctionClient = new TextRewriteClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        String result = correctionClient.send("随着我国城市化脚步的不断加快，园林工程建设的数量也在不断上升，城市对于园林工程的质量要求也随之上升，" +
                "然而就当前我国园林工程管理的实践而言，就园林工程质量管理这一环节还存在许多不足之处，本文在探讨园林工程质量内涵的基础上，" +
                "深入进行质量管理策略探讨，目的是保障我国园林工程施工质量和提升整体发展效率。", "L6");
        logger.info("返回结果: {}", result);
    }

    @Test
    public void testSendNull() throws Exception {
        TextRewriteClient correctionClient = new TextRewriteClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        try {
            String result = correctionClient.send("");
            logger.info("返回结果: {}", result);
        } catch (BusinessException e) {
            logger.error("请求失败", e);
        }
    }
}
