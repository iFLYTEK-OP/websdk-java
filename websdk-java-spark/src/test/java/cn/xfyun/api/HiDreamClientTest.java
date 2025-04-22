package cn.xfyun.api;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.image.HiDreamParam;
import cn.xfyun.util.FileUtil;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonArray;
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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 图片生成（hidream）Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HiDreamClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class HiDreamClientTest {

    private static final Logger logger = LoggerFactory.getLogger(HiDreamClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getHidreamAPPKey();
    private static final String apiSecret = PropertiesConfig.getHidreamAPPSecret();
    private static List<String> referenceImages;

    @Before
    public void init() {
        try {
            // 图片基路径
            String resourcePath = Objects.requireNonNull(this.getClass().getResource("/")).toURI().getPath();
            // 参考图片的路径
            String referenceImage1 = "image/hidream_1.jpg";
//            String referenceImage2 = "hidream_2.jpg";
            // 初始化参考图片列表   可以是url 或者 base64文件
            referenceImages = new ArrayList<>();
            referenceImages.add(FileUtil.fileToBase64(resourcePath + referenceImage1));
//            referenceImages.add(FileUtils.fileToBase64(referenceImage2));
        } catch (URISyntaxException e) {
            logger.error("获取资源路径失败", e);
        } catch (IOException ex) {
            logger.error("文件读取异常", ex);
        }
    }

    @Test
    public void testBusinessError() throws Exception {
        HiDreamClient client = new HiDreamClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        try {
            client.send(null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("参数不能为空"));
        }
        try {
            client.query(null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("taskId不能为空"));
        }
        HiDreamParam param = HiDreamParam.builder()
                .build();
        try {
            client.send(param);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("图片描述或参考图片不能同时为空"));
        }
    }

    @Test
    public void buildParam() {
        HiDreamClient client = new HiDreamClient
                .Builder(appId, apiKey, apiSecret)
                .connectTimeout(10000)
                .readTimeout(10000)
                .retryOnConnectionFailure(true)
                .writeTimeout(10000)
                .callTimeout(10000)
                .hostUrl("test.url")
                .encoding("utf8")
                .searchUrl("test.url")
                .apiKey("123")
                .apiSecret("456")
                .appId("qwer1234")
                .compress("raw")
                .format("json")
                .status(3)
                .build();
        Assert.assertEquals(10000, client.getConnectTimeout());
        Assert.assertEquals(10000, client.getReadTimeout());
        Assert.assertTrue(client.getRetryOnConnectionFailure());
        Assert.assertEquals(10000, client.getWriteTimeout());
        Assert.assertEquals(10000, client.getCallTimeout());
        Assert.assertEquals("utf8", client.getEncoding());
        Assert.assertEquals("test.url", client.getSearchUrl());
        Assert.assertEquals("123", client.getApiKey());
        Assert.assertEquals("456", client.getApiSecret());
        Assert.assertEquals("qwer1234", client.getAppId());
        Assert.assertEquals("raw", client.getCompress());
        Assert.assertEquals("json", client.getFormat());
        Assert.assertEquals(3, client.getStatus());
        Assert.assertEquals("test.url", client.getHostUrl());
    }

    @Test
    public void testSuccess() throws Exception {
        HiDreamClient client = new HiDreamClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        logger.info("请求地址：{}", client.getHostUrl());

        HiDreamParam param = HiDreamParam.builder()
                .image(referenceImages)
                .prompt("请将此图片改为孙悟空大闹天空")
                .aspectRatio("1:1")
                .imgCount(1)
                .build();
        String sendResult = client.send(param);
        logger.info("请求返回结果：{}", sendResult);

        // 结果获取taskId
        JsonObject obj = StringUtils.gson.fromJson(sendResult, JsonObject.class);
        String taskId = obj.getAsJsonObject("header").get("task_id").getAsString();
        logger.info("hidream任务id：{}", taskId);

        while (true) {
            // 根据taskId查询任务结果
            String searchResult = client.query(taskId);
            JsonObject queryObj = StringUtils.gson.fromJson(searchResult, JsonObject.class);
            String taskStatus = queryObj.getAsJsonObject("header").get("task_status").getAsString();
            if (Objects.equals(taskStatus, "1")) {
                logger.info("文生图任务待处理...");
            }
            if (Objects.equals(taskStatus, "2")) {
                logger.info("文生图任务处理中...");
            }
            if (Objects.equals(taskStatus, "3")) {
                logger.info("文生图任务处理完成：");
                logger.info(searchResult);
                String base64 = queryObj.getAsJsonObject("payload").getAsJsonObject("result").get("text").getAsString();
                byte[] decodedBytes = Base64.getDecoder().decode(base64);
                String decodedStr = new String(decodedBytes, StandardCharsets.UTF_8);
                logger.info("生成的图片解码后信息：{}", decodedStr);
                // 获取解码后的图片路径(默认只展示生成一张图片的情况)
                JsonArray imageInfo = StringUtils.gson.fromJson(decodedStr, JsonArray.class);
                String imageUrl = imageInfo.get(0).getAsJsonObject().get("image_wm").getAsString();
                logger.info("生成的图片Url：{}", imageUrl);
                // 下载图片
//                FileUtils.downloadFileWithFolder(imageUrl, resourcePath);
                break;
            }
            if (Objects.equals(taskStatus, "4")) {
                logger.info("文生图任务回调完成：");
                logger.info(searchResult);
                break;
            }
            TimeUnit.MILLISECONDS.sleep(3000);
        }
    }
}
