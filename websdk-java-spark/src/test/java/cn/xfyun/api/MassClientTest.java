package cn.xfyun.api;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.RoleContent;
import cn.xfyun.model.finetuning.response.MassResponse;
import cn.xfyun.service.finetuning.AbstractMassWebSocketListener;
import cn.xfyun.util.StringUtils;
import config.PropertiesConfig;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 星辰Mass Client单元测试
 *
 * @author zyding6
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MassClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class MassClientTest {
    private static final Logger logger = LoggerFactory.getLogger(MassClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getMassAPPKey();
    private static final String apiSecret = PropertiesConfig.getMassAPPSecret();
    private static final String postKey = PropertiesConfig.getMassAPIKey();

    @Test
    public void buildParamTest() throws MalformedURLException, SignatureException {
        MassClient massClient = new MassClient.Builder()
                .signatureWs("0", "1", appId, apiKey, apiSecret)
                .wsUrl("test.wsUrl")
                .callTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .chatId("666")
                .domain("1")
                .logRequest(Boolean.TRUE)
                .maxTokens(8960)
                .pingInterval(0, TimeUnit.SECONDS)
                .patchId(new ArrayList<>())
                .topK(1)
                .temperature(0.5F)
                .stream(true)
                .build();

        Assert.assertEquals(massClient.getAppId(), appId);
        Assert.assertEquals(massClient.getApiKey(), apiKey);
        Assert.assertEquals(massClient.getApiSecret(), apiSecret);
        Assert.assertEquals(massClient.getOriginHostUrl(), "test.wsUrl");
        Assert.assertNotNull(massClient.getRequestUrl());
        Assert.assertEquals(massClient.getCallTimeout(), 10000);
        Assert.assertEquals(massClient.getConnectTimeout(), 10000);
        Assert.assertEquals(massClient.getWriteTimeout(), 10000);
        Assert.assertEquals(massClient.getReadTimeout(), 10000);
        Assert.assertEquals(massClient.getPingInterval(), 0);
        Assert.assertTrue(massClient.isRetryOnConnectionFailure());
        Assert.assertTrue(massClient.getLogRequest());
        Assert.assertTrue(massClient.getStream());
        Assert.assertTrue(massClient.getPatchId().isEmpty());
        Assert.assertEquals(massClient.getChatId(), "666");
        Assert.assertEquals(massClient.getDomain(), "1");
        Assert.assertEquals(massClient.getMaxTokens().intValue(), 8960);
        Assert.assertEquals(massClient.getTopK().intValue(), 1);
        Assert.assertNotNull(massClient.getTemperature());
    }

    @Test
    public void testBusinessError() throws IOException, SignatureException {
        MassClient client = new MassClient.Builder()
                .signatureWs("0", "xdeepseekv3", appId, apiKey, apiSecret)
                // .signatureWs("0", "xdeepseekr1", appId, apiKey, apiSecret)
                .wsUrl("wss://maas-api.cn-huabei-1.xf-yun.com/v1.1/chat")
                .build();
        try {
            client.send(null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("文本内容不能为空"));
        }
        try {
            client.send(null, null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("文本内容不能为空"));
        }
    }

    @Test
    public void testWs() throws MalformedURLException, SignatureException, UnsupportedEncodingException {
        MassClient client = new MassClient.Builder()
                .signatureWs("0", "xdeepseekv3", appId, apiKey, apiSecret)
                .wsUrl("wss://maas-api.cn-huabei-1.xf-yun.com/v1.1/chat")
                .logRequest(Boolean.TRUE)
                .build();

        List<RoleContent> messages = new ArrayList<>();
        RoleContent roleContent = new RoleContent();
        roleContent.setRole("user");
        roleContent.setContent("你好");
        RoleContent roleContent1 = new RoleContent();
        roleContent1.setRole("assistant");
        roleContent1.setContent("你好！");
        RoleContent roleContent2 = new RoleContent();
        roleContent2.setRole("user");
        roleContent2.setContent("你是谁");
        RoleContent roleContent3 = new RoleContent();
        roleContent3.setRole("assistant");
        roleContent3.setContent("我是Spark API。");
        RoleContent roleContent4 = new RoleContent();
        roleContent4.setRole("user");
        roleContent4.setContent("帮我讲一个笑话");

        messages.add(roleContent);
        messages.add(roleContent1);
        messages.add(roleContent2);
        messages.add(roleContent3);
        messages.add(roleContent4);

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        StringBuffer finalResult = new StringBuffer();
        StringBuffer thingkingResult = new StringBuffer();
        client.send(messages, new AbstractMassWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, MassResponse resp) {
                logger.debug("中间返回json结果 ==>{}", StringUtils.gson.toJson(resp));
                if (resp.getHeader().getCode() != 0) {
                    logger.error("code=>{}，error=>{}，sid=>{}", resp.getHeader().getCode(), resp.getHeader().getMessage(), resp.getHeader().getSid());
                    logger.warn("错误码查询链接：https://www.xfyun.cn/doc/spark/%E7%B2%BE%E8%B0%83%E6%9C%8D%E5%8A%A1API-websocket.html");
                    return;
                }

                if (null != resp.getPayload() && null != resp.getPayload().getChoices()) {
                    List<MassResponse.Payload.Choices.Text> text = resp.getPayload().getChoices().getText();
                    if (null != text && !text.isEmpty()) {
                        String content = resp.getPayload().getChoices().getText().get(0).getContent();
                        String reasonContent = resp.getPayload().getChoices().getText().get(0).getReasoning_content();
                        if (!StringUtils.isNullOrEmpty(reasonContent)) {
                            thingkingResult.append(reasonContent);
                            logger.info("思维链结果... ==> {}", reasonContent);
                        } else if (!StringUtils.isNullOrEmpty(content)) {
                            finalResult.append(content);
                            logger.info("中间结果 ==> {}", content);
                        }
                    }

                    if (resp.getPayload().getChoices().getStatus() == 2) {
                        // 说明数据全部返回完毕，可以关闭连接，释放资源
                        logger.info("session end");
                        Date dateEnd = new Date();
                        logger.info("{}开始", sdf.format(dateBegin));
                        logger.info("{}结束", sdf.format(dateEnd));
                        logger.info("耗时：{}ms", dateEnd.getTime() - dateBegin.getTime());
                        logger.info("完整思维链结果 ==> {}", thingkingResult);
                        logger.info("最终识别结果 ==> {}", finalResult);
                        logger.info("本次识别sid ==> {}", resp.getHeader().getSid());
                        client.closeWebsocket();
                    }
                }
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                client.closeWebsocket();
            }

            @Override
            public void onClose(WebSocket webSocket, int code, String reason) {
            }
        });
    }

    @Test
    public void testPost() throws IOException, SignatureException {
        MassClient client = new MassClient.Builder()
                .signatureHttp("0", "xdeepseekv3", postKey)
                .requestUrl("https://maas-api.cn-huabei-1.xf-yun.com/v1")
                .logRequest(Boolean.TRUE)
                .build();

        List<RoleContent> messages = new ArrayList<>();
        RoleContent roleContent = new RoleContent();
        roleContent.setRole("user");
        roleContent.setContent("你好");
        RoleContent roleContent1 = new RoleContent();
        roleContent1.setRole("assistant");
        roleContent1.setContent("你好！");
        RoleContent roleContent2 = new RoleContent();
        roleContent2.setRole("user");
        roleContent2.setContent("你是谁");
        RoleContent roleContent3 = new RoleContent();
        roleContent3.setRole("assistant");
        roleContent3.setContent("我是Spark API。");
        RoleContent roleContent4 = new RoleContent();
        roleContent4.setRole("user");
        roleContent4.setContent("帮我讲一个笑话");

        messages.add(roleContent);
        messages.add(roleContent1);
        messages.add(roleContent2);
        messages.add(roleContent3);
        messages.add(roleContent4);

        // post方式
        String result = client.send(messages);
        logger.info("{} 模型返回结果 ==>{}", client.getDomain(), result);
        // JsonObject obj = StringUtils.gson.fromJson(result, JsonObject.class);
        // String content = obj.getAsJsonArray("choices").get(0).getAsJsonObject().getAsJsonObject("message").get("content").getAsString();
        // logger.info("{} 大模型回复内容 ==>{}", client.getDomain(), content);
    }
}