package cn.xfyun.api;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sparkmodel.RoleContent;
import cn.xfyun.model.sparkmodel.SparkChatParam;
import cn.xfyun.model.sparkmodel.response.ImageUnderstandResponse;
import cn.xfyun.service.sparkmodel.AbstractImgUnderstandWebSocketListener;
import cn.xfyun.util.FileUtil;
import cn.xfyun.util.StringUtils;
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

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 图像理解 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ImageUnderstandClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class ImageUnderstandClientTest {

    private static final Logger logger = LoggerFactory.getLogger(ImageUnderstandClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getImgUnderstandAPIKey();
    private static final String apiSecret = PropertiesConfig.getImgUnderstandAPPSecret();
    private String imagePath;
    private String resourcePath;

    @Before
    public void init() {
        try {
            // 图片基路径
            resourcePath = Objects.requireNonNull(this.getClass().getResource("/")).toURI().getPath();
            imagePath = "image/car.jpg";
        } catch (URISyntaxException e) {
            logger.error("获取资源路径失败", e);
        }
    }

    @Test
    public void buildParamTest() {
        ImageUnderstandClient client = new ImageUnderstandClient.Builder()
                .signature(appId, apiKey, apiSecret)
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
    }

    @Test
    public void testBusinessError() throws IOException, SignatureException {
        ImageUnderstandClient client = new ImageUnderstandClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();
        try {
            client.send("", "");
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("文本内容不能为空"));
        }
    }

    @Test
    public void testWs() throws IOException, SignatureException {
        ImageUnderstandClient client = new ImageUnderstandClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        SparkChatParam param = SparkChatParam.builder()
                .messages(getMessages())
                .chatId(UUID.randomUUID().toString().substring(0,10))
                .userId("user_001")
                .build();

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        StringBuffer finalResult = new StringBuffer();
        client.send(param, new AbstractImgUnderstandWebSocketListener() {

            @Override
            public void onSuccess(WebSocket webSocket, ImageUnderstandResponse resp) {
                if (resp.getHeader().getCode() != 0) {
                    logger.error("code=>{}，error=>{}，sid=>{}", resp.getHeader().getCode(), resp.getHeader().getMessage(), resp.getHeader().getSid());
                    logger.warn("错误码查询链接：https://www.xfyun.cn/doc/spark/%E7%B2%BE%E8%B0%83%E6%9C%8D%E5%8A%A1API-websocket.html");
                    return;
                }

                if (null != resp.getPayload()) {
                    if (null != resp.getPayload().getChoices()) {
                        List<ImageUnderstandResponse.Payload.Choices.Text> text = resp.getPayload().getChoices().getText();
                        // 是否进行了函数调用
                        if (null != text && !text.isEmpty()) {
                            IntStream.range(0, text.size()).forEach(index -> {
                                String content = resp.getPayload().getChoices().getText().get(index).getContent();
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


    private List<RoleContent> getMessages() throws IOException {
        // 多轮交互需要将之前的交互历史按照user(image)->user->assistant->user->assistant规则进行拼接，保证最后一条是user的当前问题
        List<RoleContent> messages = new ArrayList<>();

        // 会话记录
        RoleContent roleContent1 = new RoleContent();
        roleContent1.setRole("user");
        roleContent1.setContent(FileUtil.fileToBase64(resourcePath + imagePath));
        roleContent1.setContentType("image");
        RoleContent roleContent2 = new RoleContent();
        roleContent2.setRole("user");
        roleContent2.setContent("描述一下这张图片");
        roleContent2.setContentType("text");

        messages.add(roleContent1);
        messages.add(roleContent2);
        return messages;
    }
}