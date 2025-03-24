package api;

import cn.xfyun.api.AudioComplianceClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.Audio;
import cn.xfyun.util.FileUtils;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zyding
 * @version 1.0
 * @date 2025/3/13 10:14
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AudioComplianceClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class AudioComplianceClientTest {

    private static final Logger logger = LoggerFactory.getLogger(AudioComplianceClientTest.class);

    private static final String appId = PropertiesConfig.getAppId();

    private static final String apiKey = PropertiesConfig.getAudioComplianceClientApiKey();

    private static final String apiSecret = PropertiesConfig.getAudioComplianceClientApiSecret();

    //	private String audioUrl = "https://xfyun-doc.cn-bj.ufileos.com/static%2F16793792882352753%2F1.mp3";// 音频公网地址
    private static final String audioUrl1 = "https://chuanmei-m-test-integ-env.iflyrec.com/SpeechSynthesisService/5992f214-f43a-433f-a7ec-067ae189513a/133c3269-c823-4499-94ad-e4283167402f.wav";// 音频公网地址
    private static final String audioUrl2 = "https://chuanmei-m-test-integ-env.iflyrec.com/SpeechSynthesisService/5992f214-f43a-433f-a7ec-067ae189513a/133c3269-c823-4499-94ad-e4283167402f.wav";// 音频公网地址
    private static final String audioUrl3 = "https://chuanmei-m-test-integ-env.iflyrec.com/SpeechSynthesisService/5992f214-f43a-433f-a7ec-067ae189513a/133c3269-c823-4499-94ad-e4283167402f.wav";// 音频公网地址

    private static List<String> audios;

    static {
        audios = Arrays.asList(audioUrl1);
    }

    @Test
    public void testParamBuild() {
        AudioComplianceClient client = new AudioComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        Assert.assertNotNull(client.getQueryUrl());
        Assert.assertNotNull(client.getHostUrl());
        Assert.assertEquals(client.getNotifyUrl(), "");
    }


    @Test
    public void testSuccess() throws Exception {
        AudioComplianceClient correctionClient = new AudioComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        List<Audio> audioList = new ArrayList<>();
        for (String audioUrl : audios) {
            if (!StringUtils.isNullOrEmpty(audioUrl)) {
                Audio audio = new Audio.Builder()
                        .audioType(FileUtils.getLegalAudioSuffixByUrl(audioUrl))
                        .fileUrl(audioUrl)
                        .name(FileUtils.getLegalAudioNameByUrl(audioUrl))
                        .build();
                audioList.add(audio);
            }
        }

        // 发起音频合规任务请求
        String resp = correctionClient.send(audioList);
        logger.info("音频合规调用返回：{}", resp);
        JsonObject obj = StringUtils.gson.fromJson(resp, JsonObject.class);
        String requestId = obj.getAsJsonObject("data").get("request_id").getAsString();
        logger.info("音频合规任务请求Id：{}", requestId);

        // 拿到request_id后主动查询合规结果   如果有回调函数则在完成后自动调用回调接口
        while (true) {
            String query = correctionClient.query(requestId);
            JsonObject queryObj = StringUtils.gson.fromJson(query, JsonObject.class);
            int auditStatus = queryObj.getAsJsonObject("data").get("audit_status").getAsInt();
            if (auditStatus == 0) {
                logger.info("音频合规待审核...");
            }
            if (auditStatus == 1) {
                logger.info("音频合规审核中...");
            }
            if (auditStatus == 2) {
                logger.info("音频合规审核完成：");
                logger.info(query);
                break;
            }
            if (auditStatus == 4) {
                logger.info("音频合规审核异常：");
                logger.info(query);
            }
            TimeUnit.MILLISECONDS.sleep(3000);
        }
    }

    @Test
    public void testSendNull() throws Exception {
        AudioComplianceClient correctionClient = new AudioComplianceClient
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
