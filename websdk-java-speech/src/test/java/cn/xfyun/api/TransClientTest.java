package cn.xfyun.api;

import cn.xfyun.api.TransClient;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.model.response.trans.TransResponse;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.IOException;
import java.security.SignatureException;

import static org.junit.Assert.*;

/**
 * @author <ydwang16@iflytek.com>
 * @description 机器翻译测试用例
 * @date 2021/6/15
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TransClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class TransClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getApiKey();
    private static final String apiSecret = PropertiesConfig.getApiSecret();

    @Test
    public void testParams() throws IOException, SignatureException {
        TransClient transClient = new TransClient.Builder().signature(appId, apiKey, apiSecret)
                .hostUrl("test.url")
                .connTimeout(1)
                .maxConnections(2)
                .retryCount(3)
                .soTimeout(4)
                .build();

        assertEquals(appId, Whitebox.getInternalState(transClient, "appId"));
        assertEquals(apiKey, Whitebox.getInternalState(transClient, "apiKey"));
        assertEquals("test.url", Whitebox.getInternalState(transClient, "hostUrl"));

        transClient = new TransClient.Builder().signature(appId, apiKey, apiSecret).build();
        TransResponse response = transClient.sendNiuTrans("测试默认小牛","en");
        assertEquals("https://ntrans.xfyun.cn/v2/ots",transClient.getHostUrl());

        response = transClient.sendIst("测试默认自研","en");
        assertEquals("https://itrans.xfyun.cn/v2/its",transClient.getHostUrl());
    }

    @Test
    public void testSuccess() throws SignatureException, IOException {
        TransClient client = new TransClient.Builder().signature(appId, apiKey, apiSecret).build();
        TransResponse niuResponse = client.sendNiuTrans("6月9日是科大讯飞司庆日。","en");
        TransResponse itsResponse = client.sendIst("6月9日是科大讯飞司庆日。","en");
        System.out.println(new Gson().toJson(niuResponse));
        System.out.println(new Gson().toJson(itsResponse));

        assertNotNull(niuResponse);
        assertNotNull(itsResponse);

        assertTrue(niuResponse.getCode() == 0);
        assertTrue(itsResponse.getCode() == 0);

        assertTrue(niuResponse.getData().getResult().getTrans_result().toString().contains("June 9th"));
        assertTrue(itsResponse.getData().getResult().getTrans_result().toString().contains("June 9th"));
    }

    @Test
    public void testErrorSignature() throws SignatureException, IOException {
        TransClient client = new TransClient.Builder().signature("123", apiKey, apiSecret).build();
        TransResponse niuResponse = client.sendNiuTrans("今天天气很好。","en");
        assertTrue(niuResponse.getCode() == 10313);
    }
}
