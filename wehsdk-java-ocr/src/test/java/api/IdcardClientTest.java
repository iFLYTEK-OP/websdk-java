package api;

import cn.xfyun.api.IdcardClient;
import cn.xfyun.config.IdcardEnum;
import config.PropertiesConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 11:49
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({BusinessCardClientTest.class})
@PowerMockIgnore({"javax.crypto.*", "javax.net.ssl.*"})
public class IdcardClientTest {


    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getApiKey();

    private String resourcePath = this.getClass().getResource("/").getPath();


    @Test
    public void testParams() {
        IdcardClient client = new IdcardClient
                .Builder(appId, apiKey)
                .build();
        Assert.assertEquals(appId, client.getAppId());
        Assert.assertEquals(apiKey, client.getApiKey());
        Assert.assertEquals(IdcardEnum.OFF, client.getHeadPortrait());
        Assert.assertEquals(IdcardEnum.OFF, client.getCropImage());
        Assert.assertEquals(IdcardEnum.OFF, client.getIdNumberImage());
        Assert.assertEquals(IdcardEnum.OFF, client.getRecognizeMode());
        Assert.assertEquals("https://webapi.xfyun.cn/v1/service/v1/ocr/idcard", client.getHostUrl());
    }

}
