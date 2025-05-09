package cn.xfyun.api;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.aippt.request.Outline;
import cn.xfyun.model.aippt.request.PPTCreate;
import cn.xfyun.model.aippt.request.PPTSearch;
import cn.xfyun.util.StringUtils;
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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * 智能ppt(新) Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AIPPTV2Client.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class AIPPTV2ClientTest {

    private static final Logger logger = LoggerFactory.getLogger(AIPPTV2ClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiSecret = PropertiesConfig.getAIPPTClientApiSecret();
    private String filePath;
    private String resourcePath;

    @Before
    public void init() {
        try {
            // 图片基路径
            resourcePath = Objects.requireNonNull(this.getClass().getResource("/")).toURI().getPath();
            filePath = "document/aipptv2.pdf";
        } catch (URISyntaxException e) {
            logger.error("获取资源路径失败", e);
        }
    }

    @Test
    public void testError() throws IOException {
        AIPPTV2Client client = new AIPPTV2Client.Builder(appId, apiSecret).build();

        try {
            PPTCreate createParam = PPTCreate.builder()
                    .build();
            client.create(createParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("query、file、fileUrl参数必填其一"));
        }

        try {
            PPTCreate createParam = PPTCreate.builder()
                    .fileUrl("123", null)
                    .build();
            client.create(createParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("文件名称不能为空"));
        }

        try {
            PPTCreate createParam = PPTCreate.builder()
                    .build();
            client.createOutline(createParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("query参数不合法"));
        }

        try {
            PPTCreate createParam = PPTCreate.builder()
                    .build();
            client.createOutlineByDoc(createParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("fileName不能为空"));
        }

        try {
            PPTCreate createParam = PPTCreate.builder()
                    .query("123")
                    .build();
            client.createPptByOutline(createParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("大纲内容不能为空"));
        }
    }

    @Test
    public void testSuccess() throws IOException {
        AIPPTV2Client client = new AIPPTV2Client.Builder(appId, apiSecret).build();

        PPTSearch searchParam = PPTSearch.builder()
                .pageNum(1)
                .pageSize(2)
                .build();
        String themeList = client.list(searchParam);
        logger.info("ppt主题列表：{}", themeList);

        PPTCreate createParam = PPTCreate.builder()
                // .query("生成一个介绍科大讯飞的ppt")
                .file(new File(resourcePath + filePath), "aipptv2.pdf")
                .build();
        String create = client.create(createParam);
        logger.info("ppt生成返回结果：{}", create);

        PPTCreate createOutlineParam = PPTCreate.builder()
                .query("生成一个介绍科大讯飞的大纲")
                .build();
        String createOutline = client.createOutline(createOutlineParam);
        logger.info("ppt大纲生成返回结果：{}", createOutline);

        PPTCreate docParam = PPTCreate.builder()
                .query("模仿这个文件生成一个随机的大纲")
                .file(new File(resourcePath + filePath), "aipptv2.pdf")
                .build();
        String docResult = client.createOutlineByDoc(docParam);
        logger.info("自定义大纲生成返回结果：{}", docResult);

        PPTCreate outLine = PPTCreate.builder()
                .query("生成一个介绍科大讯飞的ppt")
                .outline(getMockOutLine())
                .build();
        String ppt = client.createPptByOutline(outLine);
        logger.info("通过大纲生成PPT生成返回结果：{}", ppt);

        String progress = client.progress("ea1c356473e94b84879f7a0a351cc8d2");
        logger.info("查询PPT进度返回结果：{}", progress);
    }

    @Test
    public void testParam() {
        AIPPTV2Client client = new AIPPTV2Client.Builder(appId, apiSecret)
                .readTimeout(10000)
                .hostUrl("test.url")
                .writeTimeout(10000)
                .apiKey("123")
                .apiSecret("456")
                .appId("wewqe123")
                .connectTimeout(10000)
                .retryOnConnectionFailure(true)
                .build();
        Assert.assertEquals(10000, client.getReadTimeout());
        Assert.assertEquals("test.url", client.getHostUrl());
        Assert.assertEquals(10000, client.getWriteTimeout());
        Assert.assertEquals("123", client.getApiKey());
        Assert.assertEquals("456", client.getApiSecret());
        Assert.assertEquals("wewqe123", client.getAppId());
        Assert.assertEquals(10000, client.getConnectTimeout());
        Assert.assertTrue(client.getRetryOnConnectionFailure());
    }

    private Outline getMockOutLine() {
        String outLineStr = "{\"title\":\"科大讯飞技术与创新概览\",\"subTitle\":\"探索语音识别与人工智能的前沿发展\",\"chapters\":[{\"chapterTitle\":\"科大讯飞简介\",\"chapterContents\":[{\"chapterTitle\":\"公司历史\"},{\"chapterTitle\":\"主要业务\"}]},{\"chapterTitle\":\"技术与创新\",\"chapterContents\":[{\"chapterTitle\":\"语音识别技术\"},{\"chapterTitle\":\"人工智能应用\"}]},{\"chapterTitle\":\"产品与服务\",\"chapterContents\":[{\"chapterTitle\":\"智能语音产品\"},{\"chapterTitle\":\"教育技术服务\"}]},{\"chapterTitle\":\"市场地位\",\"chapterContents\":[{\"chapterTitle\":\"行业领导者\"},{\"chapterTitle\":\"国际影响力\"}]},{\"chapterTitle\":\"未来展望\",\"chapterContents\":[{\"chapterTitle\":\"发展战略\"},{\"chapterTitle\":\"持续创新计划\"}]}]}";
        return StringUtils.gson.fromJson(outLineStr, Outline.class);
    }
}
