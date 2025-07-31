package cn.xfyun.api;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sparkmodel.RoleContent;
import cn.xfyun.model.sparkmodel.SparkChatParam;
import cn.xfyun.model.sparkmodel.response.SparkChatResponse;
import cn.xfyun.service.sparkmodel.AbstractSparkModelWebSocketListener;
import cn.xfyun.util.StringUtils;
import config.PropertiesConfig;
import okhttp3.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 星火助手 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SparkChatClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class SparkAssistantClientTest {

    private static final Logger logger = LoggerFactory.getLogger(SparkAssistantClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getSparkAPPKey();
    private static final String apiSecret = PropertiesConfig.getSparkAPPSecret();

    @Test
    public void buildParamTest() {
        SparkAssistantClient client = new SparkAssistantClient.Builder()
                .signature(appId, apiKey, apiSecret, "123")
                .callTimeout(0, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .pingInterval(0, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .maxTokens(4680)
                .temperature(0.5F)
                .topK(4)
                .hostUrl("test.url")
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
        Assert.assertEquals(client.getOriginHostUrl(), "test.url");
        Assert.assertEquals(client.getAssistantId(), "123");
    }

    @Test
    public void testBusinessError() throws SignatureException, MalformedURLException {
        SparkAssistantClient client = new SparkAssistantClient.Builder()
                .signature(appId, apiKey, apiSecret, "123")
                .build();
        try {
            client.send(new SparkChatParam(), null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("文本内容不能为空"));
        }
    }

    @Test
    public void testWs() throws MalformedURLException, SignatureException {
        SparkAssistantClient client = new SparkAssistantClient.Builder()
                .signature(appId, apiKey, apiSecret, "123")
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