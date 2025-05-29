package api;

import cn.xfyun.api.TransClient;
import cn.xfyun.model.translate.TransParam;
import config.PropertiesConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import static org.junit.Assert.*;

/**
 * @author <ydwang16@iflytek.com>
 * @description 机器翻译测试用例
 * @date 2021/6/15
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TransClient.class})
@PowerMockIgnore({"javax.crypto.*", "javax.net.ssl.*"})
public class TransClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getTransClientApiKey();
    private static final String apiSecret = PropertiesConfig.getTransClientApiSecret();

    @Test
    public void testParams() {
        TransClient transClient = new TransClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        assertEquals(appId, transClient.getAppId());
        assertEquals(apiKey, transClient.getApiKey());
        assertEquals(apiSecret, transClient.getApiSecret());
        assertEquals("cn", transClient.getFrom());
        assertEquals("en", transClient.getTo());
    }

    @Test
    public void testBuildParams() {
        TransClient transClient = new TransClient
                .Builder(appId, apiKey, apiSecret)
                .hostUrl("test.url")
                .callTimeout(1)
                .connectTimeout(1)
                .readTimeout(1)
                .writeTimeout(1)
                .resId("666")
                .from("A")
                .to("B")
                .build();

        assertEquals(transClient.getHostUrl(), "test.url");
        assertEquals(transClient.getCallTimeout(), 1);
        assertEquals(transClient.getConnectTimeout(), 1);
        assertEquals(transClient.getReadTimeout(), 1);
        assertEquals(transClient.getWriteTimeout(), 1);
        assertEquals(transClient.getResId(), "666");
        assertEquals(transClient.getFrom(), "A");
        assertEquals(transClient.getTo(), "B");
    }

    @Test
    public void testSuccess() throws Exception {
        TransClient client = new TransClient.Builder(appId, apiKey, apiSecret).build();
        String niuResponse = client.sendNiuTrans("6月9日是科大讯飞司庆日。");
        String itsResponse = client.sendIst("6月9日是科大讯飞司庆日。");
        String itsV2Response = client.sendIstV2("6月9日是科大讯飞司庆日。");
        System.out.println(niuResponse);
        System.out.println(itsResponse);
        System.out.println(itsV2Response);

        assertNotNull(niuResponse);
        assertNotNull(itsResponse);
        assertNotNull(itsV2Response);

    }

    @Test
    public void testSuccessParam() throws Exception {
        TransClient client = new TransClient.Builder(appId, apiKey, apiSecret).build();
        TransParam param = TransParam.builder().text("6月9日是科大讯飞司庆日。").build();
        String niuResponse = client.sendNiuTrans(param);
        String itsResponse = client.sendIst(param);
        String itsV2Response = client.sendIstV2(param);
        System.out.println(niuResponse);
        System.out.println(itsResponse);
        System.out.println(itsV2Response);

        assertNotNull(niuResponse);
        assertNotNull(itsResponse);
        assertNotNull(itsV2Response);

    }

    @Test
    public void testErrorSignature() throws Exception {
        TransClient client = new TransClient.Builder("123", apiKey, apiSecret).build();
        String niuResponse = client.sendNiuTrans("今天天气很好。");
        System.out.println(niuResponse);
    }
}
