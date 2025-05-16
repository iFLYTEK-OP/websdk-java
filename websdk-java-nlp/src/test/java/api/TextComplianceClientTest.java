package api;

import cn.xfyun.api.TextComplianceClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.textcomp.TextCompParam;
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
 * 文本合规 Client单元测试
 *
 * @author <zyding6@ifytek.com>
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
    public void testParamBuild() {
        TextComplianceClient client = new TextComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .isMatchAll(1)
                .libIds(new ArrayList<>())
                .categories(new ArrayList<>())
                .build();
        Assert.assertEquals(1, client.getIsMatchAll());
        Assert.assertTrue(client.getLibIds().isEmpty());
        Assert.assertTrue(client.getCategories().isEmpty());
    }


    @Test
    public void testSuccess() throws Exception {
        TextComplianceClient correctionClient = new TextComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        TextCompParam param = TextCompParam.builder()
                .content("塔利班组织联合东突组织欲图。")
                .isMatchAll(0)
                .libIds(new ArrayList<>())
                .categories(new ArrayList<>())
                .build();
        String result = correctionClient.send(param);
        logger.info("返回结果: {}", result);
    }

    @Test
    public void testError() throws Exception {
        TextComplianceClient correctionClient = new TextComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        try {
            TextCompParam param = TextCompParam.builder()
                    .build();
            String result = correctionClient.send(param);
            logger.info("返回结果: {}", result);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("content参数不合法"));
        }
    }
}
