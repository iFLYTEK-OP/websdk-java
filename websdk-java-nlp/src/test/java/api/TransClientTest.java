package api;

import cn.xfyun.api.TransClient;
import cn.xfyun.model.response.trans.TransData;
import cn.xfyun.model.response.trans.TransResponse;
import com.google.gson.Gson;
import config.PropertiesConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.IOException;
import java.security.SignatureException;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

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

        assertSame(niuResponse.getCode(),0);
        assertSame(itsResponse.getCode(),0);

        assertTrue(niuResponse.getData().getResult().getTrans_result().toString().contains("June 9th"));
        assertTrue(itsResponse.getData().getResult().getTrans_result().toString().contains("June 9th"));

        assertEquals(niuResponse.getData().getResult().getFrom(),"zh");
        assertEquals(niuResponse.getData().getResult().getTo(),"en");

        assertEquals(itsResponse.getData().getResult().getFrom(),"cn");
        assertEquals(itsResponse.getData().getResult().getTo(),"en");

        System.out.println(niuResponse.getSid());
        System.out.println(niuResponse.getCode());
        System.out.println(niuResponse.getMessage());

        TransData.Result result = niuResponse.getData().getResult();
        result.setFrom("cn");
        result.setTo("en");
        result.setTrans_result(new HashMap<>(16));

        niuResponse.setCode(1);
        niuResponse.setData(new TransData());
        niuResponse.setMessage("test");
        niuResponse.setSid("123456");
    }

    @Test
    public void testErrorSignature() throws SignatureException, IOException {
        TransClient client = new TransClient.Builder().signature("123", apiKey, apiSecret).build();
        TransResponse niuResponse = client.sendNiuTrans("今天天气很好。","en");
        assertTrue(niuResponse.getCode() == 10313);
    }
}
