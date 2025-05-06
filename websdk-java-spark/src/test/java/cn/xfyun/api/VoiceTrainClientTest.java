package cn.xfyun.api;

import cn.xfyun.model.voiceclone.request.AudioAddParam;
import cn.xfyun.model.voiceclone.request.CreateTaskParam;
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
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VoiceTrainClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class VoiceTrainClientTest {
    private static final Logger logger = LoggerFactory.getLogger(VoiceTrainClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getVoiceCloneAPPKey();

    @Test
    public void testSuccess() {
        try {
            VoiceTrainClient client = new VoiceTrainClient.Builder(appId, apiKey).build();
            logger.info("token:{}, 到期时间: {}", client.getToken(), client.getTokenExpiryTime());
            client.setToken(null, 1L);

            // 获取到训练文本
            String trainTextTree = client.trainText(5001L);
            logger.info("获取到训练文本列表：{}", trainTextTree);

            // 创建任务
            CreateTaskParam createTaskParam = CreateTaskParam.builder()
                    .taskName("2025-03-11测试")
                    .sex(2)
                    .ageGroup(2)
                    .thirdUser("百度翻译")
                    .language("en")
                    .resourceName("百度翻译女发音人")
                    .build();
            String taskResp = client.createTask(createTaskParam);
            JsonObject taskObj = StringUtils.gson.fromJson(taskResp, JsonObject.class);
            String taskId = taskObj.get("data").getAsString();
            logger.info("创建任务：{}，返回taskId：{}", taskResp, taskId);

            // 添加链接音频
            AudioAddParam audioAddParam1 = AudioAddParam.builder()
                    .audioUrl("https开头,wav|mp3|m4a|pcm文件结尾的URL地址")
                    .taskId(taskId)
                    .textId(5001L)
                    .textSegId(1L)
                    .build();
            String audioResp = client.audioAdd(audioAddParam1);
            logger.info("添加链接音频：{}", audioResp);

            // 提交任务
            String submit = client.submit(taskId);
            logger.info("提交任务：{}", submit);

            // 任务结果
            String result = client.result(taskId);
            logger.info("任务结果：{}", result);

            // 提交文件任务
            AudioAddParam audioAddParam2 = AudioAddParam.builder()
                    .file(new File("wav/mp3/m4a/pcm文件地址"))
                    .taskId(taskId)
                    .textId(5001L)
                    .textSegId(1L)
                    .build();
            String submitWithAudio = client.submitWithAudio(audioAddParam2);
            logger.info("提交任务：{}", submitWithAudio);
        } catch (Exception e) {
            logger.error("请求失败", e);
        }
    }

    @Test
    public void getToken() {
        VoiceTrainClient client = new VoiceTrainClient.Builder(appId, apiKey).build();

        String token = client.refreshToken();
        logger.info("获取token接口返回结果: {}", token);
        String accessToken = client.getToken();
        logger.info("获取accessToken: {}", accessToken);
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
                .build();

        Assert.assertEquals(client.getCallTimeout(), 0);
        Assert.assertEquals(client.getWriteTimeout(), 10000);
        Assert.assertEquals(client.getReadTimeout(), 10000);
        Assert.assertEquals(client.getConnectTimeout(), 10000);
        Assert.assertTrue(client.getRetryOnConnectionFailure());
        Assert.assertEquals(client.getHostUrl(), "test.url");
    }
}
