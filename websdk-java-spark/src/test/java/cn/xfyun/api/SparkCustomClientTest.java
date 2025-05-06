package cn.xfyun.api;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sparkmodel.FileContent;
import cn.xfyun.model.sparkmodel.FunctionCall;
import cn.xfyun.model.sparkmodel.request.KnowledgeFileUpload;
import cn.xfyun.model.sparkmodel.response.SparkChatResponse;
import cn.xfyun.service.sparkmodel.AbstractSparkModelWebSocketListener;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import config.PropertiesConfig;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 星辰Mass Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SparkCustomClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class SparkCustomClientTest {

    private static final Logger logger = LoggerFactory.getLogger(SparkCustomClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getSparkAPPKey();
    private static final String apiSecret = PropertiesConfig.getSparkAPPSecret();

    private String filePath;
    private String resourcePath;

    @Before
    public void init() {
        try {
            // 图片基路径
            resourcePath = Objects.requireNonNull(this.getClass().getResource("/")).toURI().getPath();
            filePath = "document/private.md";
        } catch (URISyntaxException e) {
            logger.error("获取资源路径失败", e);
        }
    }

    @Test
    public void buildParamTest() {
        SparkCustomClient client = new SparkCustomClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .callTimeout(0, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .pingInterval(0, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .functions(new ArrayList<>())
                .maxTokens(4680)
                .temperature(0.5F)
                .createKnowledgeUrl("test.url")
                .uploadFileUrl("test.url")
                .topK(4)
                .hostUrl("test.url")
                .userId("001")
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
        Assert.assertTrue(client.getMaxTokens() == 4680);
        Assert.assertTrue(client.getTemperature() == 0.5f);
        Assert.assertTrue(client.getTopK() == 4);
        Assert.assertTrue(client.isRetryOnConnectionFailure());
        Assert.assertTrue(client.getFunctions().isEmpty());
        Assert.assertEquals(client.getOriginHostUrl(), "test.url");
        Assert.assertEquals(client.getCreateKnowledgeUrl(), "test.url");
        Assert.assertEquals(client.getUploadFileUrl(), "test.url");
        Assert.assertEquals(client.getUserId(), "001");
    }

    @Test
    public void testBusinessError() throws IOException, SignatureException {
        SparkCustomClient client = new SparkCustomClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();
        try {
            client.send(null, null, null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("文本内容不能为空"));
        }

        try {
            client.create(null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("知识库名称不能为空"));
        }

        try {
            client.create("这是一个超过32字符的字符串这是一个超过32字符的字符串这是一个超过32字符的字符串");
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("知识库名称长度不能超过32字符"));
        }

        try {
            client.upload(null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("上传文件参数为空"));
        }

        try {
            KnowledgeFileUpload build = KnowledgeFileUpload.builder().knowledgeName("").build();
            client.upload(build);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("知识库名称不能为空"));
        }

        try {
            KnowledgeFileUpload build = KnowledgeFileUpload.builder().knowledgeName("123").purpose(null).build();
            client.upload(build);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("purpose不能为空"));
        }

        try {
            KnowledgeFileUpload build = KnowledgeFileUpload.builder().knowledgeName("123")
                    .build();
            client.upload(build);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("媒体文件不能为空"));
        }
    }

    @Test
    public void sparkChatWs() throws MalformedURLException, SignatureException {
        SparkCustomClient client = new SparkCustomClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        StringBuffer finalResult = new StringBuffer();
        client.send(getMessages(), getFunctions(), new AbstractSparkModelWebSocketListener() {

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
                                logger.info("插件{} ==> 类型：{}，插件内容：{}", index + 1, plugin.getName(), plugin.getContent());
                            });
                        }
                    }
                    if (null != resp.getPayload().getChoices()) {
                        List<SparkChatResponse.Payload.Choices.Text> text = resp.getPayload().getChoices().getText();
                        // 是否进行了函数调用
                        if (null != text && !text.isEmpty()) {
                            IntStream.range(0, text.size()).forEach(index -> {
                                String content = resp.getPayload().getChoices().getText().get(index).getContent();
                                SparkChatResponse.Payload.Choices.Text.FunctionCall call = resp.getPayload().getChoices().getText().get(index).getFunctionCall();
                                if (null != call) {
                                    logger.info("函数{} ==> 名称：{}，函数调用内容：{}", index + 1, call.getName(), call.getArguments());
                                }
                                if (!StringUtils.isNullOrEmpty(content)) {
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
                            webSocket.close(1000, "");
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
    public void create() throws IOException {
        SparkCustomClient client = new SparkCustomClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        String result = client.create("test-01");
        logger.info("创建知识库接口返回结果 ==> {}", result);
    }

    @Test
    public void upload() throws IOException {
        SparkCustomClient client = new SparkCustomClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        KnowledgeFileUpload upload = KnowledgeFileUpload.builder()
                .file(new File(resourcePath + filePath))
                .knowledgeName("test-01")
                .build();
        String result = client.upload(upload);
        logger.info("上传文件到知识库返回结果 ==> {}", result);
    }

    private List<FunctionCall> getFunctions() {
        List<FunctionCall> functions = new ArrayList<>();
        FunctionCall function = new FunctionCall();
        function.setName("天气查询");
        function.setDescription("天气插件可以提供天气相关信息。你可以提供指定的地点信息、指定的时间点或者时间段信息，来精准检索到天气信息。");
        function.setParameters(getParameters());
        functions.add(function);
        return functions;
    }

    private List<FileContent> getMessages() {
        // 多轮交互需要将之前的交互历史按照system->user->assistant->user->assistant规则进行拼接
        List<FileContent> messages = new ArrayList<>();

        // 会话记录
        FileContent fileContent1 = new FileContent();
        fileContent1.setRole("system");
        fileContent1.setContent("你是一个聊天的人工智能助手，可以和人类进行对话。");

        FileContent fileContent2 = new FileContent();
        fileContent2.setRole("user");
        fileContent2.setContent("你好");

        FileContent fileContent3 = new FileContent();
        fileContent3.setRole("assistant");
        fileContent3.setContent("你好！");

        FileContent fileContent4 = new FileContent();
        fileContent4.setRole("user");

        List<FileContent.Content> content = new ArrayList<>();
        FileContent.Content content1 = new FileContent.Content();
        content1.setType("file");
        content1.setFile(Arrays.asList("您的知识库文件ID"));
        content.add(content1);

        FileContent.Content content2 = new FileContent.Content();
        content2.setType("text");
        content2.setText("帮我总结一下这一篇文章讲的什么");
        content.add(content2);

        fileContent4.setRole("user");
        fileContent4.setContent(content);

        // messages.add(roleContent1);
        // messages.add(roleContent2);
        // messages.add(roleContent3);
        messages.add(fileContent4);
        return messages;
    }

    private FunctionCall.Parameters getParameters() {
        FunctionCall.Parameters parameters = new FunctionCall.Parameters();
        parameters.setType("object");
        // 函数的字段
        JsonObject properties = new JsonObject();
        // 字段1 地点
        FunctionCall.Parameters.Field location = new FunctionCall.Parameters.Field();
        location.setType("string");
        location.setDescription("地点，比如北京。");
        // 字段2 日期
        FunctionCall.Parameters.Field date = new FunctionCall.Parameters.Field();
        date.setType("string");
        date.setDescription("日期。");
        // 放到properties转换成字符串存储
        properties.add("location", StringUtils.gson.toJsonTree(location));
        properties.add("date", StringUtils.gson.toJsonTree(date));
        parameters.setProperties(properties.toString());
        // 必须要返回的字段(示例: 返回地点)
        parameters.setRequired(Collections.singletonList("location"));
        return parameters;
    }
}