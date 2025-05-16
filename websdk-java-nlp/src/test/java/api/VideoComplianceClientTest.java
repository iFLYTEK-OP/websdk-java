package api;

import cn.xfyun.api.VideoComplianceClient;
import cn.xfyun.config.VideoFormat;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.Video;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import config.PropertiesConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 视频合规 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VideoComplianceClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class VideoComplianceClientTest {

    private static final Logger logger = LoggerFactory.getLogger(VideoComplianceClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getVideoComplianceClientApiKey();
    private static final String apiSecret = PropertiesConfig.getVideoComplianceClientApiSecret();
    private static List<String> videos;

    @Before
    public void init() {
        // 视频公网地址
        String videoUrl1 = "https://xfyun-doc.cn-bj.ufileos.com/static/16794773437998668/1.mp4";
        String videoUrl2 = "";
        String videoUrl3 = "";
        videos = Arrays.asList(videoUrl1, videoUrl2, videoUrl3);
    }

    @Test
    public void testParamBuild() {
        VideoComplianceClient client = new VideoComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .notifyUrl("test.url")
                .queryUrl("test.url")
                .build();
        Assert.assertNotNull(client.getQueryUrl());
        Assert.assertNotNull(client.getHostUrl());
        Assert.assertEquals(client.getNotifyUrl(), "test.url");
        Assert.assertEquals(client.getQueryUrl(), "test.url");
    }

    @Test
    public void testSuccess() throws Exception {
        VideoComplianceClient correctionClient = new VideoComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        // 创建视频信息列表
        List<Video> videoList = new ArrayList<>();
        for (String videoUrl : videos) {
            if (!StringUtils.isNullOrEmpty(videoUrl)) {
                Video video = new Video.Builder()
                        .videoType(VideoFormat.MP4.getFormat())
                        .fileUrl(videoUrl)
                        .name("1.mp4")
                        .build();
                videoList.add(video);
            }
        }

        // 发起音频合规任务请求
        String resp = correctionClient.send(videoList);
        logger.info("视频合规调用返回：{}", resp);
        JsonObject obj = StringUtils.gson.fromJson(resp, JsonObject.class);
        String requestId = obj.getAsJsonObject("data").get("request_id").getAsString();
        logger.info("视频合规任务请求Id：{}", requestId);

        // 拿到request_id后主动查询合规结果   如果有回调函数则在完成后自动调用回调接口
        while (true) {
            String query = correctionClient.query("T2025031417270301a5fdb78eca47000");
            JsonObject queryObj = StringUtils.gson.fromJson(query, JsonObject.class);
            int auditStatus = queryObj.getAsJsonObject("data").get("audit_status").getAsInt();
            if (auditStatus == 0) {
                logger.info("视频合规待审核...");
            }
            if (auditStatus == 1) {
                logger.info("视频合规审核中...");
            }
            if (auditStatus == 2) {
                logger.info("视频合规审核完成：");
                logger.info(query);
                break;
            }
            if (auditStatus == 4) {
                logger.info("视频合规审核异常：");
                logger.info(query);
                break;
            }
            TimeUnit.MILLISECONDS.sleep(3000);
        }
    }

    @Test
    public void testSendNull() throws Exception {
        VideoComplianceClient correctionClient = new VideoComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        try {
            String result = correctionClient.send(new ArrayList<>());
            logger.info("返回结果: {}", result);
        } catch (BusinessException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
