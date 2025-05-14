package cn.xfyun.api;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.maas.MaasParam;
import cn.xfyun.model.sparkmodel.RoleContent;
import cn.xfyun.model.maas.response.MaasResponse;
import cn.xfyun.service.maas.AbstractMaasWebSocketListener;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import config.PropertiesConfig;
import okhttp3.*;
import okio.BufferedSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 星辰Maas Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MaasClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class MaasClientTest {

    private static final Logger logger = LoggerFactory.getLogger(MaasClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getMaasAPPKey();
    private static final String apiSecret = PropertiesConfig.getMaasAPPSecret();
    private static final String postKey = PropertiesConfig.getMaasAPIKey();

    @Test
    public void buildParamTest() {
        MaasClient maasClient = new MaasClient.Builder()
                .signatureWs("0", "1", appId, apiKey, apiSecret)
                .wsUrl("test.wsUrl")
                .callTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .streamOptions(null)
                .retryOnConnectionFailure(true)
                .domain("1")
                .maxTokens(8960)
                .pingInterval(0, TimeUnit.SECONDS)
                .patchId(new ArrayList<>())
                .topK(1)
                .searchDisable(true)
                .showRefLabel(true)
                .temperature(0.5F)
                .build();

        Assert.assertEquals(maasClient.getAppId(), appId);
        Assert.assertEquals(maasClient.getApiKey(), apiKey);
        Assert.assertEquals(maasClient.getApiSecret(), apiSecret);
        Assert.assertEquals(maasClient.getOriginHostUrl(), "test.wsUrl");
        Assert.assertNotNull(maasClient.getRequestUrl());
        Assert.assertEquals(maasClient.getCallTimeout(), 10000);
        Assert.assertEquals(maasClient.getConnectTimeout(), 10000);
        Assert.assertEquals(maasClient.getWriteTimeout(), 10000);
        Assert.assertEquals(maasClient.getReadTimeout(), 10000);
        Assert.assertEquals(maasClient.getPingInterval(), 0);
        Assert.assertTrue(maasClient.isRetryOnConnectionFailure());
        Assert.assertTrue(maasClient.getPatchId().isEmpty());
        Assert.assertTrue(maasClient.isSearchDisable());
        Assert.assertTrue(maasClient.isShowRefLabel());
        Assert.assertEquals(maasClient.getDomain(), "1");
        Assert.assertEquals(maasClient.getMaxTokens().intValue(), 8960);
        Assert.assertEquals(maasClient.getTopK().intValue(), 1);
        Assert.assertNotNull(maasClient.getTemperature());
        Assert.assertNull(maasClient.getStreamOptions());
    }

    @Test
    public void testBusinessError() throws IOException, SignatureException {
        MaasClient client = new MaasClient.Builder()
                .signatureWs("0", "xdeepseekv3", appId, apiKey, apiSecret)
                // .signatureWs("0", "xdeepseekr1", appId, apiKey, apiSecret)
                .wsUrl("wss://maas-api.cn-huabei-1.xf-yun.com/v1.1/chat")
                .build();
        MaasParam param = MaasParam.builder()
                .messages(new ArrayList<>())
                .build();
        try {
            client.send(null, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    super.onOpen(webSocket, response);
                }
            });
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("参数不能为空"));
        }
        try {
            client.send(null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("参数不能为空"));
        }
        try {
            client.send(param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) {

                }
            });
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("文本内容不能为空"));
        }
    }

    @Test
    public void testWs() throws MalformedURLException, SignatureException {
        MaasClient client = new MaasClient.Builder()
                .signatureWs("0", "xdeepseekv3", appId, apiKey, apiSecret)
                .wsUrl("wss://maas-api.cn-huabei-1.xf-yun.com/v1.1/chat")
                .build();

        List<RoleContent> messages = new ArrayList<>();
        RoleContent roleContent = new RoleContent();
        roleContent.setRole("user");
        roleContent.setContent("帮我讲一个笑话");
        messages.add(roleContent);

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        StringBuffer finalResult = new StringBuffer();
        StringBuffer thingkingResult = new StringBuffer();

        MaasParam param = MaasParam.builder()
                .messages(messages)
                .build();
        client.send(param, new AbstractMaasWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, MaasResponse resp) {
                logger.debug("中间返回json结果 ==>{}", StringUtils.gson.toJson(resp));
                if (resp.getHeader().getCode() != 0) {
                    logger.error("code=>{}，error=>{}，sid=>{}", resp.getHeader().getCode(), resp.getHeader().getMessage(), resp.getHeader().getSid());
                    logger.warn("错误码查询链接：https://www.xfyun.cn/doc/spark/%E7%B2%BE%E8%B0%83%E6%9C%8D%E5%8A%A1API-websocket.html");
                    return;
                }

                if (null != resp.getPayload() && null != resp.getPayload().getChoices()) {
                    List<MaasResponse.Payload.Choices.Text> text = resp.getPayload().getChoices().getText();
                    if (null != text && !text.isEmpty()) {
                        String content = resp.getPayload().getChoices().getText().get(0).getContent();
                        String reasonContent = resp.getPayload().getChoices().getText().get(0).getReasoningContent();
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
                        webSocket.close(1000, "正常关闭");
                    }
                }
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                webSocket.close(1000, t.getMessage());
            }

            @Override
            public void onClose(WebSocket webSocket, int code, String reason) {
            }
        });
    }

    @Test
    public void testPost() throws IOException {
        MaasClient client = new MaasClient.Builder()
                .signatureHttp("0", "xdeepseekv3", postKey)
                .requestUrl("https://maas-api.cn-huabei-1.xf-yun.com/v1/chat/completions")
                .build();

        List<RoleContent> messages = new ArrayList<>();
        RoleContent roleContent = new RoleContent();
        roleContent.setRole("user");
        roleContent.setContent("讲一个笑话");
        messages.add(roleContent);

        MaasParam param = MaasParam.builder()
                .messages(messages)
                .build();

        // post方式
        String result = client.send(param);
        logger.info("{} 模型返回结果 ==>{}", client.getDomain(), result);
        JsonObject obj = StringUtils.gson.fromJson(result, JsonObject.class);
        String content = obj.getAsJsonArray("choices").get(0).getAsJsonObject().getAsJsonObject("message").get("content").getAsString();
        logger.info("{} 大模型回复内容 ==>{}", client.getDomain(), content);
    }

    @Test
    public void testStream() {
        MaasClient client = new MaasClient.Builder()
                .signatureHttp("0", "xdeepseekv3", postKey)
                .requestUrl("https://maas-api.cn-huabei-1.xf-yun.com/v1/chat/completions")
                .build();

        List<RoleContent> messages = new ArrayList<>();
        RoleContent roleContent = new RoleContent();
        roleContent.setRole("user");
        roleContent.setContent("帮我讲一个笑话");
        messages.add(roleContent);

        MaasParam param = MaasParam.builder()
                .messages(messages)
                .build();

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        StringBuilder finalResult = new StringBuilder();
        StringBuilder thingkingResult = new StringBuilder();

        // sse方式
        client.send(param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logger.error("sse连接失败：{}", e.getMessage());
                System.exit(0);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    logger.error("请求失败，状态码：{}，原因：{}", response.code(), response.message());
                    System.exit(0);
                    return;
                }
                ResponseBody body = response.body();
                if (body != null) {
                    BufferedSource source = body.source();
                    try {
                        while (true) {
                            String line = source.readUtf8Line();
                            if (line == null) {
                                break;
                            }
                            if (line.startsWith("data:")) {
                                // 去掉前缀 "data: "
                                String data = line.substring(5).trim();
                                if (extractContent(data, finalResult, thingkingResult)) {
                                    // 说明数据全部返回完毕，可以关闭连接，释放资源
                                    logger.info("session end");
                                    Date dateEnd = new Date();
                                    logger.info("{}开始", sdf.format(dateBegin));
                                    logger.info("{}结束", sdf.format(dateEnd));
                                    logger.info("耗时：{}ms", dateEnd.getTime() - dateBegin.getTime());
                                    logger.info("完整思维链结果 ==> {}", thingkingResult);
                                    logger.info("最终识别结果 ==> {}", finalResult);
                                    System.exit(0);
                                }
                            }
                        }
                    } catch (IOException e) {
                        logger.error("读取sse返回内容发生异常", e);
                    }
                }
            }
        });
    }

    /**
     * @param data            sse返回的数据
     * @param finalResult     实时回复内容
     * @param thingkingResult 实时思维链结果
     * @return 是否结束
     */
    private static boolean extractContent(String data, StringBuilder finalResult, StringBuilder thingkingResult) {
        logger.debug("sse返回数据 ==> {}", data);
        try {
            JsonObject obj = StringUtils.gson.fromJson(data, JsonObject.class);
            JsonObject choice0 = obj.getAsJsonArray("choices").get(0).getAsJsonObject();
            JsonObject delta = choice0.getAsJsonObject("delta");
            // 结束原因
            String finishReason = choice0.get("finish_reason").getAsString();
            if (!StringUtils.isNullOrEmpty(finishReason)) {
                if (finishReason.equals("stop")) {
                    logger.info("本次识别sid ==> {}", obj.get("id").getAsString());
                    return true;
                }
                throw new BusinessException("异常结束: " + finishReason);
            }
            // 回复
            JsonElement content = delta.get("content");
            if (null != content && StringUtils.isNullOrEmpty(content.getAsString())) {
                logger.info("中间结果 ==> {}", content.getAsString());
                finalResult.append(content.getAsString());
            }
            // 思维链
            JsonElement reasonContent = delta.get("reasoning_content");
            if (null != reasonContent && StringUtils.isNullOrEmpty(reasonContent.getAsString())) {
                logger.info("思维链结果... ==> {}", reasonContent.getAsString());
                thingkingResult.append(reasonContent.getAsString());
            }
            // 插件
            JsonElement pluginContent = delta.get("plugins_content");
            if (null != pluginContent && StringUtils.isNullOrEmpty(pluginContent.getAsString())) {
                logger.info("插件信息 ==> {}", pluginContent.getAsString());
            }
        } catch (BusinessException bx) {
            throw bx;
        } catch (Exception e) {
            logger.error("解析sse返回内容发生异常", e);
            logger.info("异常数据 ==> {}", data);
        }
        return false;
    }
}