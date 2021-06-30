package api;

import cn.xfyun.api.LtpClient;
import cn.xfyun.model.response.ltp.LtpResponse;
import config.PropertiesConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LtpClient.class})
@PowerMockIgnore("cn.xfyun.util.HttpConnector")
public class LtpClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getLtpKey();

    /**
     * 测试参数设置
     * @throws Exception
     */
    @Test
    public void testParams() throws Exception {
        LtpClient ltpClient = new LtpClient.Builder(appId, apiKey)
                .hostUrl("123")
                .type("test")
                .connTimeout(1)
                .maxConnections(2)
                .retryCount(3)
                .soTimeout(4)
                .func("yes")
                .build();
        assertEquals(appId, Whitebox.getInternalState(ltpClient, "appId"));
        assertEquals(apiKey, Whitebox.getInternalState(ltpClient, "apiKey"));
        assertEquals("123yes", Whitebox.getInternalState(ltpClient, "hostUrl"));
        assertEquals("test", Whitebox.getInternalState(ltpClient, "type"));
    }

    /**
     * 测试异常情况
     */
    @Test
    public void testException(){
        try{
            LtpClient ltpClient = new LtpClient.Builder(appId, apiKey)
                    .build();
        }catch (Exception e){
            assertEquals("func参数为空", e.getMessage());
        }
    }

    /**
     * 测试中文分词功能
     * @throws Exception
     */
    @Test
    public void testCws() throws Exception {
        LtpClient ltpClient = new LtpClient.Builder(appId, apiKey)
                .func("cws")
                .build();
        LtpResponse response = ltpClient.send("我来自北方");
        System.out.println(response.toString());
//        Assert.assertEquals(response.getDesc(), "success");
    }

    /**
     * 测试词性标注功能
     * @throws Exception
     */
    @Test
    public void testPos() throws Exception {
        LtpClient ltpClient = new LtpClient.Builder(appId, apiKey)
                .func("pos")
                .build();
        LtpResponse response = ltpClient.send("我来自北方");
        System.out.println(response.toString());
//        Assert.assertEquals(response.getDesc(), "success");
    }

    /**
     * 测试命名实体识别功能
     * @throws Exception
     */
    @Test
    public void testNer() throws Exception {
        LtpClient ltpClient = new LtpClient.Builder(appId, apiKey)
                .func("ner")
                .build();
        LtpResponse response = ltpClient.send("我来自北方");
        System.out.println(response.toString());
//        Assert.assertEquals(response.getDesc(), "success");
    }

    /**
     * 测试依存句法分析功能
     * @throws Exception
     */
    @Test
    public void testDp() throws Exception {
        LtpClient ltpClient = new LtpClient.Builder(appId, apiKey)
                .func("dp")
                .build();
        LtpResponse response = ltpClient.send("我来自北方");
        System.out.println(response.toString());
//        Assert.assertEquals(response.getDesc(), "success");
    }

    /**
     * 测试语义角色标注功能
     * @throws Exception
     */
    @Test
    public void testSrl() throws Exception {
        LtpClient ltpClient = new LtpClient.Builder(appId, apiKey)
                .func("srl")
                .build();
        LtpResponse response = ltpClient.send("我来自北方");
        System.out.println(response.toString());
//        Assert.assertEquals(response.getDesc(), "success");
    }

    /**
     * 测试语义依存 (依存树) 分析功能
     * @throws Exception
     */
    @Test
    public void testSdp() throws Exception {
        LtpClient ltpClient = new LtpClient.Builder(appId, apiKey)
                .func("sdp")
                .build();
        LtpResponse response = ltpClient.send("我来自北方");
        System.out.println(response.toString());
        Assert.assertEquals(response.getDesc(), "success");
    }

    /**
     * 测试语义依存  (依存图) 分析功能
     * @throws Exception
     */
    @Test
    public void testSdgp() throws Exception {
        LtpClient ltpClient = new LtpClient.Builder(appId, apiKey)
                .func("sdgp")
                .build();
        LtpResponse response = ltpClient.send("我来自北方");
        System.out.println(response.toString());
        Assert.assertEquals(response.getDesc(), "success");
    }

    /**
     * 自然语言处理-关键词提取
     * @throws Exception
     */
    @Test
    public void testKey() throws Exception {
        LtpClient ltpClient = new LtpClient.Builder(appId, apiKey)
                .func("ke")
                .build();
        LtpResponse response = ltpClient.send("自然语言处理是计算机科学领域与人工智能领域中的一个重要方向。它研究能实现人与计算机之间用自然语言进行有效通信的各种理论和方法。自然语言处理是一门融语言学、计算机科学、数学于一体的科学。因此，这一领域的研究将涉及自然语言，即人们日常使用的语言，所以它与语言学的研究有着密切的联系，但又有重要的区别。自然语言处理并不是一般地研究自然语言，而在于研制能有效地实现自然语言通信的计算机系统，特别是其中的软件系统。因而它是计算机科学的一部分。");
        System.out.println(response.toString());
        Assert.assertEquals(response.getDesc(), "success");
    }
}