package api;

import cn.xfyun.api.TextComplianceClient;
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

import java.util.ArrayList;

/**
 * @author zyding
 * @version 1.0
 * @date 2025/3/13 10:14
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TextComplianceClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class TextComplianceClientTest {

    private static final Logger logger = LoggerFactory.getLogger(TextComplianceClientTest.class);

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getTextComplianceClientApiKey();
    private static final String apiSecret = PropertiesConfig.getTextComplianceClientApiSecret();

    @Test
    public void defaultParamTest() {
        TextComplianceClient client = new TextComplianceClient
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
        TextComplianceClient client = new TextComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .isMatchAll(true)
                .libIds(new ArrayList<>())
                .categories(new ArrayList<>())
                .build();
        Assert.assertTrue(client.isMatchAll());
    }


    @Test
    public void testSuccess() throws Exception {
        TextComplianceClient correctionClient = new TextComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        String result = correctionClient.send("塔利班组织联合东突组织欲图。");
        logger.info("返回结果: {}", result);
    }

    @Test
    public void testSendNull() throws Exception {
        TextComplianceClient correctionClient = new TextComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        try {
            String result = correctionClient.send("");
            logger.info("返回结果: {}", result);
        } catch (BusinessException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
