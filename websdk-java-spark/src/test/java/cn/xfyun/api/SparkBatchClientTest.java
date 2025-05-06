package cn.xfyun.api;

import cn.xfyun.exception.BusinessException;
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
 * 大模型批量任务 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SparkBatchClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class SparkBatchClientTest {

    private static final Logger logger = LoggerFactory.getLogger(SparkBatchClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiPassword = PropertiesConfig.getSparkBatchAPIPassword();
    private String imagePath;
    private String resourcePath;

    @Before
    public void init() {
        try {
            // 图片基路径
            resourcePath = Objects.requireNonNull(this.getClass().getResource("/")).toURI().getPath();
            imagePath = "image/car.jpg";
        } catch (URISyntaxException e) {
            logger.error("获取资源路径失败", e);
        }
    }

    @Test
    public void testSuccess() {
        try {
            SparkBatchClient client = new SparkBatchClient.Builder(appId, apiPassword).build();

            String upload = client.upload(new File("您的jsonl文件地址"));
            logger.info("文件上传返回结果 ==> {}", upload);

            String fileList = client.listFile(1, 20);
            logger.info("文件查询返回结果 ==> {}", fileList);

            String getFile = client.getFile("您上传的文件ID");
            logger.info("获取文件信息返回结果 ==> {}", getFile);

            String deleteFile = client.deleteFile("您需要删除的文件ID");
            logger.info("删除文件返回结果 ==> {}", deleteFile);

            String download = client.download("您需要下载的文件ID");
            logger.info("下载文件返回结果 ==> {}", download);

            String create = client.create("您需要创建任务的文件ID", null);
            logger.info("创建任务返回结果 ==> {}", create);

            String getBatch = client.getBatch("您创建任务后返回的任务ID");
            logger.info("获取任务信息返回结果 ==> {}", getBatch);

            String cancel = client.cancel("您创建任务后返回的任务ID");
            logger.info("取消任务返回结果 ==> {}", cancel);

            String listBatch = client.listBatch(10, "您创建任务后返回的任务ID");
            logger.info("查询Batch列表返回结果 ==> {}", listBatch);
        } catch (Exception e) {
            logger.error("请求失败", e);
        }
    }

    @Test
    public void testError() throws IOException {
        SparkBatchClient client = new SparkBatchClient.Builder(appId, apiPassword).build();

        try {
            client.upload(null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("无效的jsonl文件"));
        }
        try {
            client.upload(new File(resourcePath + imagePath));
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("暂不支持该格式的文件"));
        }
        try {
            client.getFile(null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("参数不能为空"));
        }
        try {
            client.deleteFile(null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("参数不能为空"));
        }
        try {
            client.download(null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("参数不能为空"));
        }
        try {
            client.create(null, null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("参数不能为空"));
        }
        try {
            client.getBatch(null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("参数不能为空"));
        }
        try {
            client.cancel(null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("参数不能为空"));
        }
        try {
            client.listBatch(10, null);
        } catch (BusinessException e) {
            Assert.assertTrue(e.getMessage().contains("参数不能为空"));
        }
    }

    @Test
    public void buildParam() {
        SparkBatchClient client = new SparkBatchClient.Builder(appId, apiPassword)
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
