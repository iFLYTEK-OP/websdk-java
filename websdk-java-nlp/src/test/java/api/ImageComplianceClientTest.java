package api;

import cn.xfyun.api.ImageComplianceClient;
import cn.xfyun.config.ModeType;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.FileUtils;
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
 * @author zyding
 * @version 1.0
 * @date 2025/3/13 10:14
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ImageComplianceClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class ImageComplianceClientTest {

    private static final Logger logger = LoggerFactory.getLogger(ImageComplianceClientTest.class);

    private static final String appId = PropertiesConfig.getAppId();

    private static final String apiKey = PropertiesConfig.getPicComplianceClientApiKey();

    private static final String apiSecret = PropertiesConfig.getPicComplianceClientApiSecret();

    private String resourcePath = this.getClass().getResource("/").getPath();

    private String imagePath = "image/1.png";// 待检测图片路径

    private String imageUrl = "http://baidu.com/1.jpg";// 待检测图片地址

    @Test
    public void testParamBuild() {
        ImageComplianceClient client = new ImageComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        Assert.assertNull(client.getBizType());
    }


    @Test
    public void testSuccess() throws Exception {
        ImageComplianceClient correctionClient = new ImageComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        String pathResp = correctionClient.send(FileUtils.fileToBase64(resourcePath + imagePath), ModeType.BASE64.getValue());
        logger.info("图片地址返回结果: {}", pathResp);

		// String urlResp = correctionClient.send(imageUrl, ModeType.LINK.getValue());
        // logger.info("图片链接返回结果: {}", pathResp);
    }

    @Test
    public void testSendNull() throws Exception {
        ImageComplianceClient correctionClient = new ImageComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        try {
            String result = correctionClient.send("", "");
            logger.info("返回结果: {}", result);
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
        }

    }
}
