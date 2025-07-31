package cn.xfyun.api;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.agent.AgentChatParam;
import cn.xfyun.model.agent.AgentResumeParam;
import cn.xfyun.service.agent.AgentCallback;
import config.PropertiesConfig;
import okhttp3.Call;
import okhttp3.Response;
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
import java.net.URISyntaxException;
import java.util.*;

/**
 * 智能体 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ImageUnderstandClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class AgentClientTest {

    private static final Logger logger = LoggerFactory.getLogger(AgentClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getImgUnderstandAPIKey();
    private static final String apiSecret = PropertiesConfig.getImgUnderstandAPPSecret();
    private String filePath;
    private String resourcePath;

    @Before
    public void init() {
        try {
            // 图片基路径
            resourcePath = Objects.requireNonNull(this.getClass().getResource("/")).toURI().getPath();
            filePath = "image/hidream_1.jpg";
        } catch (URISyntaxException e) {
            logger.error("获取资源路径失败", e);
        }
    }

    @Test
    public void buildParamTest() {
        AgentClient client = new AgentClient.Builder(apiKey, apiSecret)
                .callTimeout(0)
                .connectTimeout(10)
                .writeTimeout(10)
                .readTimeout(10)
                .retryOnConnectionFailure(true)
                .hostUrl("test.url")
                .build();

        Assert.assertEquals(client.getAppId(), appId);
        Assert.assertEquals(client.getApiKey(), apiKey);
        Assert.assertEquals(client.getApiSecret(), apiSecret);
        Assert.assertEquals(client.getCallTimeout(), 0);
        Assert.assertEquals(client.getConnectTimeout(), 10);
        Assert.assertEquals(client.getWriteTimeout(), 10);
        Assert.assertEquals(client.getReadTimeout(), 10);
        Assert.assertTrue(client.getRetryOnConnectionFailure());
        Assert.assertEquals(client.getHostUrl(), "test.url");
    }

    @Test
    public void testBusinessError() throws IOException {
        AgentClient client = new AgentClient.Builder(apiKey, apiSecret)
                .build();
        AgentChatParam agentChatParam = AgentChatParam.builder().build();
        AgentResumeParam resumeParam = AgentResumeParam.builder().build();
        try {
            client.completion(null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("参数不能为空"));
        }
        try {
            client.completion(agentChatParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("flowId不能为空"));
        }
        try {
            agentChatParam.setFlowId("null");
            client.completion(agentChatParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("parameters参数不能为空"));
        }

        try {
            client.resume(resumeParam, null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("eventId不能为空"));
        }
        try {
            resumeParam.setEventId("eventId");
            client.resume(resumeParam, null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("content参数不能为空"));
        }

        try {
            client.uploadFile(new File("123"));
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("文件不存在!"));
        }
    }

    @Test
    public void testWs() throws IOException {
        AgentClient client = new AgentClient.Builder(apiKey, apiSecret)
                .build();

        AgentChatParam agentChatParam = AgentChatParam.builder()
                .flowId("7351431612989308928")
                .parameters(new Object())
                .build();
        String result = client.completion(agentChatParam);
        logger.info("工作流返回结果：{}", result);

        client.completion(agentChatParam, getCallback());

        AgentResumeParam resumeParam = AgentResumeParam.builder()
                .eventId("eventId")
                .eventType("resume")
                .content("A")
                .build();

        client.resume(resumeParam, getCallback());

        String uploadFile = client.uploadFile(new File(resourcePath + filePath));
        logger.info("上传文件返回结果：{}", uploadFile);
    }

    private static AgentCallback getCallback() {
        return new AgentCallback() {
            @Override
            public void onEvent(Call call, String id, String type, String data) {
                logger.info("sse事件: id: {}, type: {}, data: {}", id, type, data);
            }

            @Override
            public void onFail(Call call, Throwable t) {
                logger.error("sse通信出错", t);
            }

            @Override
            public void onClosed(Call call) {
                logger.info("sse断开链接");
                call.cancel();
            }

            @Override
            public void onOpen(Call call, Response response) {
                logger.info("sse建立链接");
            }
        };
    }
}