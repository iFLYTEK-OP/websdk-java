package api;

import cn.xfyun.api.TextProofreadClient;
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
 * 文本校对 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TextProofreadClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class TextProofreadClientTest {

    private static final Logger logger = LoggerFactory.getLogger(TextProofreadClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getTextProofClientApiKey();
    private static final String apiSecret = PropertiesConfig.getTextProofClientApiSecret();

    @Test
    public void defaultParamTest() {
        TextProofreadClient client = new TextProofreadClient
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
        TextProofreadClient client = new TextProofreadClient
                .Builder(appId, apiKey, apiSecret)
                .compress("gzip")
                .format("plain")
                .encoding("base64")
                .status(2)
                .build();
        Assert.assertEquals(client.getServiceId(), "s9a87e3ec");
        Assert.assertEquals(client.getCompress(), "gzip");
        Assert.assertEquals(client.getFormat(), "plain");
        Assert.assertEquals(client.getEncoding(), "base64");
        Assert.assertEquals(client.getStatus(), 2);
    }


    @Test
    public void testSuccess() throws Exception {
        TextProofreadClient correctionClient = new TextProofreadClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        String result = correctionClient.send("第二个百年目标");
        logger.info("返回结果：{}", result);
    }

    @Test
    public void testSendNull() throws Exception {
        TextProofreadClient correctionClient = new TextProofreadClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        try {
            String result = correctionClient.send("");
            logger.info("返回结果：{}", result);
        } catch (BusinessException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
