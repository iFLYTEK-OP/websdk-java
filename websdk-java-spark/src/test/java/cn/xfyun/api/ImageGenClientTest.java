package cn.xfyun.api;

import cn.xfyun.model.RoleContent;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import config.PropertiesConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * @author <zyding6@iflytek.com>
 * @description 图片生成
 * @date 2025/3/12
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ImageGenClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class ImageGenClientTest {
    private static final Logger logger = LoggerFactory.getLogger(ImageGenClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getImageGenAPPKey();
    private static final String apiSecret = PropertiesConfig.getImageGenAPPSecret();
    private String imagePath;
    private String resourcePath;

    @Before
    public void init() {
        try {
            // 图片基路径
            resourcePath = this.getClass().getResource("/").toURI().getPath();
            imagePath = "image/gen_" + UUID.randomUUID() + ".png";
        } catch (URISyntaxException e) {
            logger.error("获取资源路径失败", e);
        }
    }

    @Test
    public void testSuccess() throws Exception {
        ImageGenClient client = new ImageGenClient
                .Builder(appId, apiKey, apiSecret)
                .connectTimeout(60000)
                .readTimeout(100000)
                .build();

        List<RoleContent> messages = new ArrayList<>();
        RoleContent roleContent = new RoleContent();
        roleContent.setRole("user");
        roleContent.setContent("帮我画一个小鸟");
        messages.add(roleContent);

        logger.info("请求地址：{}", client.getHostUrl());
        String resp = client.send(messages);
        logger.info("请求返回结果：{}", resp);

        // 结果获取text后解码
        JsonObject obj = StringUtils.gson.fromJson(resp, JsonObject.class);
        String base64 = obj.getAsJsonObject("payload")
                .getAsJsonObject("choices")
                .getAsJsonArray("text")
                .get(0)
                .getAsJsonObject()
                .get("content")
                .getAsString();
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        // base64解码后的图片字节数组写入文件
        try (FileOutputStream imageOutFile = new FileOutputStream(resourcePath + imagePath)) {
            // 将字节数组写入文件
            imageOutFile.write(decodedBytes);
            logger.info("图片已成功保存到: {}", resourcePath + imagePath);
        } catch (IOException e) {
            logger.error("保存图片时出错: {}", e.getMessage(), e);
        }
    }
}
