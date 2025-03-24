package cn.xfyun.api;

import config.PropertiesConfig;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.SignatureException;
import java.util.Objects;

import org.junit.Test;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.xfyun.exception.LfasrException;
import cn.xfyun.model.response.lfasr.LfasrResponse;
import cn.xfyun.service.lfasr.LfasrService;
import cn.xfyun.util.HttpConnector;

import static org.mockito.Mockito.*;

/**
 * 语音转写Client单元测试
 *
 * @author kaili23
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LfasrClient.class, LfasrService.class, HttpConnector.class})
@PowerMockIgnore({"javax.crypto.*", "javax.net.ssl.*"})
public class LfasrClientTest {

    private static final Logger logger = LoggerFactory.getLogger(LfasrClientTest.class);

    private final String appId = PropertiesConfig.getAppId();
    private final String secretKey = PropertiesConfig.getSecretKey();

    private static String audioFilePath;

    private static final String AUDIO_URL = "https://openres.xfyun.cn/xfyundoc/2025-03-19/e7b6a79d-124f-44e0-b8aa-0e799410f453/1742353716311/lfasr.wav";

    static {
        try {
            audioFilePath = Objects.requireNonNull(LfasrClientTest.class.getResource("/")).toURI().getPath() + "/audio/lfasr.wav";
        } catch (Exception e) {
            logger.error("资源路径获取失败", e);
        }
    }
    
   /**
    * Builder方法异常参数测试
    */
   @Test(expected = IllegalArgumentException.class)
   public void testBuilderWithNullAppId() {
       new LfasrClient.Builder(null, "key");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testBuilderWithNullSecretKey() {
       new LfasrClient.Builder("id", null);
   }

    @Test
    public void lfaSrClientTest() throws SignatureException {
        LfasrClient.Builder builder = new LfasrClient.Builder(appId, secretKey)
                .maxConnections(50)
                .connTimeout(10000)
                .soTimeout(30000)
                .fileName("测试文件名.wav")
                .fileSize(1024L)
                .duration(60L)
                .language("cn")
                .callbackUrl("http://example.com/callback")
                .hotWord("测试热词|语音识别|转写服务")
                .candidate((short)1)
                .roleType((short)1)
                .roleNum((short)2)
                .pd("tech")
                .audioMode("fileStream")
                .audioUrl("http://example.com/audio.wav")
                .standardWav(1)
                .languageType(1)
                .trackMode((short)1)
                .transLanguage("en")
                .transMode((short)2)
                .engSegMax(300)
                .engSegMin(10)
                .engSegWeight(0.03f)
                .engSmoothproc(true)
                .engColloqproc(false)
                .engVadMdn(3)
                .engVadMargin(2)
                .engRlang(1);
        
        LfasrClient lfasrClient = builder.build();
        LfasrService lfasrService = Whitebox.getInternalState(lfasrClient, "lfasrService");
        // 获取connector对象
        HttpConnector connector = Whitebox.getInternalState(lfasrService, "connector");
        // 获取连接池对象
        PoolingHttpClientConnectionManager pool = Whitebox.getInternalState(connector, "pool");

        // 验证服务连接池参数（仅maxConnections，connTimeout和soTimeout设置层级过深无法直接获取）
        Assert.assertEquals(Integer.valueOf(50), Integer.valueOf(pool.getMaxTotal()));
        // 验证转写任务参数
        Assert.assertEquals(appId, Whitebox.getInternalState(lfasrClient, "appId"));
        Assert.assertEquals(secretKey, Whitebox.getInternalState(lfasrClient, "secretKey"));
        Assert.assertEquals("测试文件名.wav", Whitebox.getInternalState(lfasrClient, "fileName"));
        Assert.assertEquals(Long.valueOf(1024L), Whitebox.getInternalState(lfasrClient, "fileSize"));
        Assert.assertEquals(Long.valueOf(60L), Whitebox.getInternalState(lfasrClient, "duration"));
        Assert.assertEquals("cn", Whitebox.getInternalState(lfasrClient, "language"));
        Assert.assertEquals("http://example.com/callback", Whitebox.getInternalState(lfasrClient, "callbackUrl"));
        Assert.assertEquals("测试热词|语音识别|转写服务", Whitebox.getInternalState(lfasrClient, "hotWord"));
        Assert.assertEquals(Short.valueOf((short)1), Whitebox.getInternalState(lfasrClient, "candidate"));
        Assert.assertEquals(Short.valueOf((short)1), Whitebox.getInternalState(lfasrClient, "roleType"));
        Assert.assertEquals(Short.valueOf((short)2), Whitebox.getInternalState(lfasrClient, "roleNum"));
        Assert.assertEquals("tech", Whitebox.getInternalState(lfasrClient, "pd"));
        Assert.assertEquals("fileStream", Whitebox.getInternalState(lfasrClient, "audioMode"));
        Assert.assertEquals("http://example.com/audio.wav", Whitebox.getInternalState(lfasrClient, "audioUrl"));
        Assert.assertEquals(Integer.valueOf(1), Whitebox.getInternalState(lfasrClient, "standardWav"));
        Assert.assertEquals(Integer.valueOf(1), Whitebox.getInternalState(lfasrClient, "languageType"));
        Assert.assertEquals(Short.valueOf((short)1), Whitebox.getInternalState(lfasrClient, "trackMode"));
        Assert.assertEquals("en", Whitebox.getInternalState(lfasrClient, "transLanguage"));
        Assert.assertEquals(Short.valueOf((short)2), Whitebox.getInternalState(lfasrClient, "transMode"));
        Assert.assertEquals(Integer.valueOf(300), Whitebox.getInternalState(lfasrClient, "engSegMax"));
        Assert.assertEquals(Integer.valueOf(10), Whitebox.getInternalState(lfasrClient, "engSegMin"));
        Assert.assertEquals(Float.valueOf(0.03f), Whitebox.getInternalState(lfasrClient, "engSegWeight"));
        Assert.assertEquals(Boolean.TRUE, Whitebox.getInternalState(lfasrClient, "engSmoothproc"));
        Assert.assertEquals(Boolean.FALSE, Whitebox.getInternalState(lfasrClient, "engColloqproc"));
        Assert.assertEquals(Integer.valueOf(3), Whitebox.getInternalState(lfasrClient, "engVadMdn"));
        Assert.assertEquals(Integer.valueOf(2), Whitebox.getInternalState(lfasrClient, "engVadMargin"));
        Assert.assertEquals(Integer.valueOf(1), Whitebox.getInternalState(lfasrClient, "engRlang"));

        // 测试参数处理方法
        lfasrClient.uploadUrl(AUDIO_URL);
    }

    /**
     * 上传音频文件方法-异常参数测试
     */
    @Test
    public void uploadFileAbnormalTest() throws Exception {
        LfasrClient.Builder builder = new LfasrClient.Builder(appId, secretKey);
        LfasrClient lfasrClient = builder.build();

        // 空文件路径
        try {
            lfasrClient.uploadFile(null);
        } catch (LfasrException e) {
            Assert.assertTrue(e.getMessage().contains("地址为空"));
        }

        // 不存在的文件路径
        try {
            lfasrClient.uploadFile("notExist.wav");
        } catch (LfasrException e) {
            Assert.assertTrue(e.getMessage().contains("文件不存在"));
        }

        // 过大的文件
        File audio = PowerMockito.mock(File.class);
        PowerMockito.whenNew(File.class).withArguments("/file/path/a.mp3").thenReturn(audio);
        PowerMockito.when(audio.exists()).thenReturn(true);
        PowerMockito.when(audio.length()).thenReturn(524288001L);
        try {
            lfasrClient.uploadFile("/file/path/a.mp3");
        } catch (LfasrException e) {
            Assert.assertTrue(e.getMessage().contains("文件过大"));
        }
    }

    /**
     * 上传音频文件方法-成功测试
     */
    @Test
    public void uploadFileTest() throws SignatureException {
        LfasrClient.Builder builder = new LfasrClient.Builder(appId, secretKey);
        LfasrClient lfasrClient = builder.build();
        LfasrResponse response = lfasrClient.uploadFile(audioFilePath);
        Assert.assertEquals("000000", response.getCode());
    }

    /**
     * 上传音频Url方法-异常参数测试
     */ 
    @Test
    public void uploadUrlAbnormalTest() throws SignatureException {
        LfasrClient.Builder builder = new LfasrClient.Builder(appId, secretKey);
        LfasrClient lfasrClient = builder.build();
        try {
            lfasrClient.uploadUrl(null);
        } catch (LfasrException e) {
            Assert.assertTrue(e.getMessage().contains("链接为空"));
        }
    }

    /**
     * 上传音频Url方法-成功测试
     */ 
    @Test
    public void uploadUrlTest() throws SignatureException {
        LfasrClient.Builder builder = new LfasrClient.Builder(appId, secretKey);
        LfasrClient lfasrClient = builder.build();
        LfasrResponse response = lfasrClient.uploadUrl(AUDIO_URL);
        Assert.assertEquals("000000", response.getCode());
    }

    /**
     * 查询结果方法-异常参数测试
     */
    @Test
    public void getResultAbnormalTest() throws SignatureException {
        LfasrClient.Builder builder = new LfasrClient.Builder(appId, secretKey);
        LfasrClient lfasrClient = builder.build();
        try {
            lfasrClient.getResult(null);
        } catch (LfasrException e) {
            Assert.assertTrue(e.getMessage().contains("orderId为空"));
        }
        lfasrClient.getResult("notExistOrderId", "transfer");
    }

    /**
     * 查询结果方法-成功测试
     */
    @Test
    public void getResultTest() throws SignatureException {
        LfasrClient.Builder builder = new LfasrClient.Builder(appId, secretKey);
        LfasrClient lfasrClient = builder.build();
        LfasrResponse uploadResponse = lfasrClient.uploadUrl(AUDIO_URL);
        String orderId = uploadResponse.getContent().getOrderId();
        LfasrResponse resultResponse = lfasrClient.getResult(orderId);
        Assert.assertEquals("000000", resultResponse.getCode());
    }    

    /**
     * 测试LfasrService中的异常处理
     */
    @Test
    public void testLfasrServiceExceptions() throws Exception {
        LfasrClient.Builder builder = new LfasrClient.Builder(appId, secretKey);
        
        // 1.测试网络连接异常
        LfasrClient lfasrClient = builder.build();
        LfasrService lfasrService = Whitebox.getInternalState(lfasrClient, "lfasrService");
        HttpConnector mockConnector = PowerMockito.mock(HttpConnector.class);
        PowerMockito.when(mockConnector.postByBytes(anyString(), anyMap(), any(byte[].class)))
                .thenThrow(new IOException("模拟文件上传异常"));
        PowerMockito.when(mockConnector.post(anyString(), anyMap()))
                .thenThrow(new IOException("模拟URL上传异常"));
        // 替换原有connector
        Whitebox.setInternalState(lfasrService, "connector", mockConnector);
        // 调用会触发IOException的方法
        LfasrResponse uploadFileResponse = lfasrClient.uploadFile(audioFilePath);
        Assert.assertTrue(uploadFileResponse.getDescInfo().contains("文件上传失败"));
        LfasrResponse uploadUrlResponse = lfasrClient.uploadUrl(AUDIO_URL);
        Assert.assertTrue(uploadUrlResponse.getDescInfo().contains("URL上传失败"));
        LfasrResponse resultResponse = lfasrClient.getResult("testOrderId");
        Assert.assertTrue(resultResponse.getDescInfo().contains("获取结果失败"));
        
        // 2.测试URI构建异常
        // 模拟URIBuilder抛出异常
        URIBuilder mockUriBuilder = PowerMockito.mock(URIBuilder.class);
        PowerMockito.whenNew(URIBuilder.class).withAnyArguments().thenReturn(mockUriBuilder);
        PowerMockito.when(mockUriBuilder.addParameter(anyString(), anyString())).thenReturn(mockUriBuilder);
        PowerMockito.when(mockUriBuilder.build()).thenThrow(new URISyntaxException("测试URI", "模拟的URI语法异常"));
        // 调用会触发URISyntaxException的方法
        try {
            lfasrClient.uploadFile(audioFilePath);
        } catch (LfasrException e) {
            Assert.assertTrue(e.getMessage().contains("构建请求URL失败"));
        }
    }

}