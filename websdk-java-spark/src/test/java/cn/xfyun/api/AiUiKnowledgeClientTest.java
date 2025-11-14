package cn.xfyun.api;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.aiui.knowledge.*;
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
import java.util.Collections;
import java.util.Objects;

/**
 * aiui个性化知识库 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AIPPTV2Client.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class AiUiKnowledgeClientTest {

    private static final Logger logger = LoggerFactory.getLogger(AiUiKnowledgeClientTest.class);
    private static final String APP_ID = PropertiesConfig.getAppId();
    private static final String API_PASSWORD = PropertiesConfig.getAiUiKnowledgePassword();
    private static final String SCENE_NAME = "场景名称";
    private static final long UID = 1111111111;
    private String filePath;
    private String resourcePath;

    @Before
    public void init() {
        try {
            // 图片基路径
            resourcePath = Objects.requireNonNull(this.getClass().getResource("/")).toURI().getPath();
            filePath = "document/aiuiknowledge.txt";
        } catch (URISyntaxException e) {
            logger.error("获取资源路径失败", e);
        }
    }

    @Test
    public void testError() throws IOException {
        AiUiKnowledgeClient client = new AiUiKnowledgeClient.Builder(API_PASSWORD).build();

        try {
            CreateParam createParam = CreateParam.builder()
                    .build();
            client.create(createParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("uid不能为空"));
        }

        try {
            CreateParam createParam = CreateParam.builder()
                    .uid(UID)
                    .build();
            client.create(createParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("知识库名称不能为空"));
        }

        try {
            SearchParam searchParam = SearchParam.builder()
                    .build();
            client.list(searchParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("uid不能为空"));
        }

        try {
            SearchParam searchParam = SearchParam.builder()
                    .uid(UID)
                    .build();
            client.list(searchParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("sceneName不能为空"));
        }

        try {
            SearchParam searchParam = SearchParam.builder()
                    .uid(UID)
                    .sceneName(SCENE_NAME)
                    .build();
            client.list(searchParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("appId不能为空"));
        }

        try {
            LinkParam linkParam = LinkParam.builder()
                    .build();
            client.link(linkParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("uid不能为空"));
        }

        try {
            LinkParam linkParam = LinkParam.builder()
                    .uid(UID)
                    .build();
            client.link(linkParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("appId不能为空"));
        }

        try {
            LinkParam linkParam = LinkParam.builder()
                    .uid(UID)
                    .appId("123")
                    .build();
            client.link(linkParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("sceneName不能为空"));
        }

        try {
            UploadParam uploadParam = UploadParam.builder()
                    .build();
            client.upload(uploadParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("uid不能为空"));
        }

        try {
            UploadParam uploadParam = UploadParam.builder()
                    .uid(UID)
                    .build();
            client.upload(uploadParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("groupId不能为空"));
        }

        try {
            UploadParam uploadParam = UploadParam.builder()
                    .uid(UID)
                    .groupId("您创建知识库返回的groupId")
                    .build();
            client.upload(uploadParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("files和fileList不能同时为空"));
        }

        try {
            DeleteParam deleteParam = DeleteParam.builder()
                    .build();
            client.delete(deleteParam);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("uid不能为空"));
        }
    }

    @Test
    public void testSuccess() throws IOException {
        AiUiKnowledgeClient client = new AiUiKnowledgeClient.Builder(API_PASSWORD).build();

        // 创建个性化知识库
        CreateParam createParam = CreateParam.builder()
                .uid(UID)
                .name("测试库-001")
                .build();
        String createResp = client.create(createParam);
        logger.info("创建结果：{}", createResp);

        // 查询个性化知识库，如果知识库没有文件，则查询结果为空
        SearchParam searchParam = SearchParam.builder()
                .uid(UID)
                .appId(APP_ID)
                .sceneName(SCENE_NAME)
                .build();
        String searchResp = client.list(searchParam);
        logger.info("查询结果：{}", searchResp);

        // 关联知识库，管理知识库传参为全量保存方式
        LinkParam.Repo repo = new LinkParam.Repo();
        repo.setGroupId("您创建知识库返回的groupId");
        LinkParam linkParam = LinkParam.builder()
                .appId(APP_ID)
                .sceneName(SCENE_NAME)
                .uid(UID)
                .repos(Collections.singletonList(repo))
                .build();
        String linkResp = client.link(linkParam);
        logger.info("关联结果：{}", linkResp);

        // 上传文件  支持本地文件和在线文件两种上传方式  冲突默认取本地上传文件
        UploadParam.FileInfo fileInfo = new UploadParam.FileInfo("aiuiknowledge.txt",
                "https://oss-beijing-m8.openstorage.cn/knowledge-origin-test/knowledge/file/123123213/7741/a838a943/20250910163419/aiuiknowledge.txt",
                43L);
        File file = new File(resourcePath + filePath);
        UploadParam uploadParam = UploadParam.builder()
                .uid(UID)
                .groupId("您创建知识库返回的groupId")
                // .fileList(Collections.singletonList(fileInfo))
                .files(Collections.singletonList(file))
                .build();
        String uploadResp = client.upload(uploadParam);
        logger.info("上传结果：{}", uploadResp);

        // 删除知识库或者知识库内文件
        DeleteParam deleteParam = DeleteParam.builder()
                .uid(UID)
                .groupId("您创建知识库返回的groupId")
                .docId("您上传文件返回的docId")
                .build();
        String deleteResp = client.delete(deleteParam);
        logger.info("删除结果：{}", deleteResp);
    }

    @Test
    public void testParam() {
        AiUiKnowledgeClient client = new AiUiKnowledgeClient.Builder(API_PASSWORD)
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
        Assert.assertEquals(API_PASSWORD, client.getApiPassword());
        Assert.assertTrue(client.getRetryOnConnectionFailure());
    }
}
