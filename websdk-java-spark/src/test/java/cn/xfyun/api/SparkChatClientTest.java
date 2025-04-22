package cn.xfyun.api;

import cn.xfyun.config.SparkModel;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.RoleContent;
import cn.xfyun.model.sparkmodel.SparkChatParam;
import cn.xfyun.model.sparkmodel.response.SparkChatResponse;
import cn.xfyun.service.sparkmodel.AbstractSparkModelWebSocketListener;
import cn.xfyun.util.StringUtils;
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
import java.util.stream.IntStream;

/**
 * 星火大模型 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SparkChatClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class SparkChatClientTest {

    private static final Logger logger = LoggerFactory.getLogger(SparkChatClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getSparkAPPKey();
    private static final String apiSecret = PropertiesConfig.getSparkAPPSecret();
    private static final String apiPassword = PropertiesConfig.getSparkAPIPassword();

    @Test
    public void buildParamTest() {
        SparkChatClient client = new SparkChatClient.Builder()
                .signatureWs(appId, apiKey, apiSecret, SparkModel.SPARK_4_0_ULTRA)
                .callTimeout(0, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .pingInterval(0, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .functions(new ArrayList<>())
                .maxTokens(4680)
                .temperature(0.5F)
                .keepAlive(true)
                .topK(4)
                .webSearch(null)
                .frequencyPenalty(1)
                .presencePenalty(1)
                .topP(1)
                .toolCallsSwitch(true)
                .responseType("json_text")
                .suppressPlugin(new ArrayList<>())
                .hostUrl("test.url")
                .toolChoice("none")
                .build();

        Assert.assertEquals(client.getAppId(), appId);
        Assert.assertEquals(client.getApiKey(), apiKey);
        Assert.assertEquals(client.getApiSecret(), apiSecret);
        Assert.assertNotNull(client.getOriginHostUrl());
        Assert.assertEquals(client.getCallTimeout(), 0);
        Assert.assertEquals(client.getConnectTimeout(), 10000);
        Assert.assertEquals(client.getWriteTimeout(), 10000);
        Assert.assertEquals(client.getReadTimeout(), 10000);
        Assert.assertEquals(client.getPingInterval(), 0);
        Assert.assertEquals(client.getMaxTokens(), 4680);
        Assert.assertTrue(client.getTemperature() == 0.5f);
        Assert.assertEquals(client.getTopK(), 4);
        Assert.assertTrue(client.isRetryOnConnectionFailure());
        Assert.assertTrue(client.getFunctions().isEmpty());
        Assert.assertNull(client.getWebSearch());
        Assert.assertTrue(client.getFrequencyPenalty() == 1);
        Assert.assertTrue(client.getPresencePenalty() == 1);
        Assert.assertNotNull(client.getTopP());
        Assert.assertTrue(client.getToolCallsSwitch());
        Assert.assertEquals(client.getResponseType(), "json_text");
        Assert.assertTrue(client.getSuppressPlugin().isEmpty());
        Assert.assertEquals(client.getOriginHostUrl(), "test.url");
        Assert.assertEquals(client.getToolChoice(), "none");
        Assert.assertTrue(client.getKeepAlive());

        Assert.assertNotNull(client.getSparkModel());
    }

    @Test
    public void testBusinessError() throws IOException {
        SparkChatClient client = new SparkChatClient.Builder()
                .signatureWs(appId, apiKey, apiSecret, SparkModel.SPARK_4_0_ULTRA)
                .build();
        try {
            client.send(new SparkChatParam());
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("文本内容不能为空"));
        }

        SparkChatClient client1 = new SparkChatClient.Builder()
                .signatureWs(appId, apiKey, apiSecret, SparkModel.CHAT_MULTILANG)
                .build();
        SparkChatParam param = SparkChatParam.builder()
                .messages(getMessages())
                .build();
        try {
            client1.send(param);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("暂不支持post请求"));
        }
    }

    @Test
    public void testWs() throws MalformedURLException, SignatureException {
        SparkChatClient client = new SparkChatClient.Builder()
                .signatureWs(appId, apiKey, apiSecret, SparkModel.SPARK_4_0_ULTRA)
                .build();

        SparkChatParam sendParam = SparkChatParam.builder()
                .messages(getMessages())
                .chatId("123213213213")
                .build();

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        StringBuffer finalResult = new StringBuffer();

        // 使用ws方式请求大模型
        client.send(sendParam, new AbstractSparkModelWebSocketListener() {

            @Override
            public void onSuccess(WebSocket webSocket, SparkChatResponse resp) {
                if (resp.getHeader().getCode() != 0) {
                    logger.error("code=>{}，error=>{}，sid=>{}", resp.getHeader().getCode(), resp.getHeader().getMessage(), resp.getHeader().getSid());
                    logger.warn("错误码查询链接：https://www.xfyun.cn/doc/spark/%E7%B2%BE%E8%B0%83%E6%9C%8D%E5%8A%A1API-websocket.html");
                    return;
                }

                if (null != resp.getPayload()) {
                    if (null != resp.getPayload().getPlugins()) {
                        List<SparkChatResponse.Payload.Plugin.Text> plugins = resp.getPayload().getPlugins().getText();
                        if (null != plugins && !plugins.isEmpty()) {
                            logger.info("本次会话使用了插件，数量：{}", plugins.size());
                            IntStream.range(0, plugins.size()).forEach(index -> {
                                SparkChatResponse.Payload.Plugin.Text plugin = plugins.get(index);
                                logger.info("插件{} ==> 类型：{}，插件内容：{}", index, plugin.getName(), plugin.getContent());
                            });
                        }
                    }
                    if (null != resp.getPayload().getChoices()) {
                        List<SparkChatResponse.Payload.Choices.Text> text = resp.getPayload().getChoices().getText();
                        if (null != text && !text.isEmpty()) {
                            IntStream.range(0, text.size()).forEach(index -> {
                                String content = resp.getPayload().getChoices().getText().get(index).getContent();
                                SparkChatResponse.Payload.Choices.Text.FunctionCall call = resp.getPayload().getChoices().getText().get(index).getFunctionCall();
                                if (null != call) {
                                    logger.info("函数{} ==> 名称：{}，函数调用内容：{}", index, call.getName(), call.getArguments());
                                }
                                if (StringUtils.isNullOrEmpty(content)) {
                                    finalResult.append(content);
                                    logger.info("中间结果 ==> {}", content);
                                }
                            });
                        }

                        if (resp.getPayload().getChoices().getStatus() == 2) {
                            // 说明数据全部返回完毕，可以关闭连接，释放资源
                            logger.info("session end");
                            Date dateEnd = new Date();
                            logger.info("{}开始", sdf.format(dateBegin));
                            logger.info("{}结束", sdf.format(dateEnd));
                            logger.info("耗时：{}ms", dateEnd.getTime() - dateBegin.getTime());
                            logger.info("最终识别结果 ==> {}", finalResult);
                            logger.info("本次识别sid ==> {}", resp.getHeader().getSid());
                            webSocket.close(1000, "正常关闭");
                        }
                    }
                }
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                logger.error(t.getMessage(), t);
                webSocket.close(1000, t.getMessage());
            }

            @Override
            public void onClose(WebSocket webSocket, int code, String reason) {
            }
        });
    }

    @Test
    public void testPost() throws IOException {
        SparkChatClient client = new SparkChatClient.Builder()
                .signatureHttp(apiPassword, SparkModel.SPARK_4_0_ULTRA)
                .build();

        // 使用post方式请求大模型
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        SparkChatParam sendParam = SparkChatParam.builder()
                .messages(getMessages())
                .userId("testUser_123")
                .build();
        String send = client.send(sendParam);
        logger.debug("请求结果 ==> {}", send);
        logger.info("session end");
        Date dateEnd = new Date();
        logger.info("{}开始", sdf.format(dateBegin));
        logger.info("{}结束", sdf.format(dateEnd));
        logger.info("耗时：{}ms", dateEnd.getTime() - dateBegin.getTime());
    }

    @Test
    public void testSse() {
        SparkChatClient client = new SparkChatClient.Builder()
                .signatureHttp("你的apiPassword", SparkModel.SPARK_4_0_ULTRA)
                .build();

        // 请求参数
        SparkChatParam sendParam = SparkChatParam.builder()
                .messages(getMessages())
                .userId("testUser_123")
                .build();

        // 使用sse方式请求大模型
        client.send(sendParam, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logger.error("sse连接失败：{}", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    logger.error("请求失败，状态码：{}，原因：{}", response.code(), response.message());
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
                                if (line.contains("[DONE]")) {
                                    // 说明数据全部返回完毕，可以关闭连接，释放资源
                                    call.cancel();
                                }
                                // 去掉前缀 "data: "
                                String data = line.substring(5).trim();
                                logger.info("sse返回内容 ==> {}", data);
                            }
                        }
                    } catch (IOException e) {
                        logger.error("读取sse返回内容发生异常", e);
                    }
                }
            }
        });
    }

    private List<RoleContent> getMessages() {
        // 多轮交互需要将之前的交互历史按照system->user->assistant->user->assistant规则进行拼接
        List<RoleContent> messages = new ArrayList<>();

        // 会话记录
        RoleContent roleContent1 = new RoleContent();
        roleContent1.setRole("system");
        roleContent1.setContent("你是一个聊天的人工智能助手，可以和人类进行对话。");
        RoleContent roleContent2 = new RoleContent();
        roleContent2.setRole("user");
        roleContent2.setContent("你好");
        RoleContent roleContent3 = new RoleContent();
        roleContent3.setRole("assistant");
        roleContent3.setContent("你好！");
        // 当前会话
        RoleContent roleContent4 = new RoleContent();
        roleContent4.setRole("user");
        // roleContent4.setContent("北京今天天气怎么样");
        roleContent4.setContent("吴艳妮最新消息");
        // roleContent4.setContent("Stell dich vor");
        // roleContent4.setContent("今日の天気はどうですか。");
        // roleContent4.setContent("오늘 날씨 어때요?");
        // roleContent4.setContent("Какая сегодня погода?");
        // roleContent4.setContent("ما هو الطقس اليوم ؟");
        // roleContent4.setContent("Quelle est la météo aujourd'hui");
        // roleContent4.setContent("¿¿ cómo está el clima hoy?");
        // roleContent4.setContent("Como está o tempo hoje?");

        // messages.add(roleContent1);
        // messages.add(roleContent2);
        // messages.add(roleContent3);
        messages.add(roleContent4);
        return messages;
    }
}