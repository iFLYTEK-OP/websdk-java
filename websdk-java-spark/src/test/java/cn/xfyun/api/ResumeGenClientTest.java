package cn.xfyun.api;

import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import config.PropertiesConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author <zyding6@iflytek.com>
 * @description 简历生成
 * @date 2025/3/12
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ResumeGenClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class ResumeGenClientTest {
    private static final Logger logger = LoggerFactory.getLogger(ResumeGenClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getResumeGenClientApiKey();
    private static final String apiSecret = PropertiesConfig.getResumeGenClientApiSecret();

    @Test
    public void testSuccess() throws Exception {
        ResumeGenClient client = new ResumeGenClient
                .Builder(appId, apiKey, apiSecret)
                .connectTimeout(70000)
                .readTimeout(10000)
                .build();

        logger.info("请求地址：{}", client.getHostUrl());
        String resp = client.send("我是一名从业5年的java开发程序员, 今年25岁, 邮箱是xxx@qq.com , 电话13000000000, 性别男 , 就业地址合肥, 期望薪资20k , 主要从事AI大模型相关的项目经历");
        logger.info("请求返回结果：{}", resp);

        // 结果获取text后解码
        JsonObject obj = StringUtils.gson.fromJson(resp, JsonObject.class);
        String text = obj.getAsJsonObject("payload").getAsJsonObject("resData").get("text").getAsString();
        byte[] decodedBytes = Base64.getDecoder().decode(text);
        String decodeRes = new String(decodedBytes, StandardCharsets.UTF_8);
        logger.info("文本解码后的结果：{}", decodeRes);
    }
}
