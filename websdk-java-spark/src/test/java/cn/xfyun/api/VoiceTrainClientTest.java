package cn.xfyun.api;

import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import config.PropertiesConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 语音训练 Client单元测试
 *
 * @author zyding6
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VoiceTrainClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class VoiceTrainClientTest {
    private static final Logger logger = LoggerFactory.getLogger(VoiceTrainClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getVoiceCloneAPPKey();
    private static final String apiSecret = PropertiesConfig.getVoiceCloneAPPSecret();

    @Test
    public void testSuccess() {
        try {
            VoiceTrainClient client = new VoiceTrainClient.Builder(appId, apiKey).build();
            String tokenResp = client.token();
            JsonObject tokenObject = StringUtils.gson.fromJson(tokenResp, JsonObject.class);
            String token = tokenObject.get("accesstoken").getAsString();
            logger.info("获取到token：{}", token);

            String trainTextTree = client.trainText(token, 5001L);
            logger.info("获取到训练文本列表：{}", trainTextTree);

            String taskResp = client.createTask("2025-03-11测试", 2, 2, "百度翻译", "en", "百度翻译女发音人", null, token);
            JsonObject taskObj = StringUtils.gson.fromJson(tokenResp, JsonObject.class);
            String taskId = taskObj.get("data").getAsString();
            logger.info("创建任务：{}，返回taskId：{}", taskResp, taskId);

            String audioResp = client.audioAdd(taskId, "https开头,wav|mp3|m4a|pcm文件结尾的URL地址",
                    5001L, 1L, token);
            logger.info("添加链接音频：{}", audioResp);

            String submit = client.submit(taskId, token);
            logger.info("提交任务：{}", submit);

            String result = client.result(taskId, token);
            logger.info("任务结果：{}", result);

            String submitWithAudio = client.submitWithAudio(new File("wav/mp3/m4a/pcm文件地址"), taskId, "5001", "1", token);
            logger.info("提交任务：{}", submitWithAudio);
        } catch (Exception e) {
            logger.error("请求失败", e);
        }
    }

    @Test
    public void buildParam() {
        VoiceTrainClient client = new VoiceTrainClient.Builder(appId, apiKey)
                .callTimeout(0)
                .writeTimeout(10000)
                .readTimeout(10000)
                .connectTimeout(10000)
                .retryOnConnectionFailure(true)
                .hostUrl("test.url")
                .logRequest(Boolean.TRUE)
                .build();

        Assert.assertEquals(client.getCallTimeout(), 0);
        Assert.assertEquals(client.getWriteTimeout(), 10000);
        Assert.assertEquals(client.getReadTimeout(), 10000);
        Assert.assertEquals(client.getConnectTimeout(), 10000);
        Assert.assertTrue(client.getRetryOnConnectionFailure());
        Assert.assertTrue(client.getLogRequest());
        Assert.assertEquals(client.getHostUrl(), "test.url");
    }
}
