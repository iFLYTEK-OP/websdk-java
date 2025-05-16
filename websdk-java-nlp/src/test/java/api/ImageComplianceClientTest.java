package api;

import cn.xfyun.api.ImageComplianceClient;
import cn.xfyun.config.ModeType;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.FileUtil;
import config.PropertiesConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 图片合规 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ImageComplianceClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class ImageComplianceClientTest {

    private static final Logger logger = LoggerFactory.getLogger(ImageComplianceClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getPicComplianceClientApiKey();
    private static final String apiSecret = PropertiesConfig.getPicComplianceClientApiSecret();
    private String resourcePath;
    private String imagePath;
    private String imageUrl;

    @Before
    public void init() {
        resourcePath = Objects.requireNonNull(this.getClass().getResource("/")).getPath();
        // 待检测图片路径
        imagePath = "image/1.png";
        // 待检测图片链接
        imageUrl = "http://baidu.com/1.jpg";
    }

    @Test
    public void testParamBuild() {
        ImageComplianceClient client = new ImageComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .bizType("pornDetection")
                .build();
        Assert.assertEquals(client.getBizType(), "pornDetection");
    }

    @Test
    public void testError() throws Exception {
        ImageComplianceClient client = new ImageComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        try {
            client.send(null, null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("content或modeType不能为空"));
        }
    }

    @Test
    public void testBase64() throws Exception {
        ImageComplianceClient correctionClient = new ImageComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        String pathResp = correctionClient.send(FileUtil.fileToBase64(resourcePath + imagePath), ModeType.BASE64);
        logger.info("图片地址返回结果: {}", pathResp);
    }

    @Test
    public void testLink() throws Exception {
        ImageComplianceClient correctionClient = new ImageComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        String urlResp = correctionClient.send(imageUrl, ModeType.LINK);
        logger.info("图片链接返回结果: {}", urlResp);
    }
}
