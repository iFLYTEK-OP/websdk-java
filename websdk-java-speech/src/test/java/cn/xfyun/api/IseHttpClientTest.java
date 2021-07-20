package cn.xfyun.api;

import cn.xfyun.config.IseAueEnum;
import cn.xfyun.config.IseCategoryEnum;
import cn.xfyun.config.IseLanguageEnum;
import cn.xfyun.config.IseResultLevelEnum;
import cn.xfyun.exception.HttpException;
import config.PropertiesConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import sun.misc.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * @author: <flhong2@iflytek.com>
 * @description: 语音评测(普通版)测试类
 * @version: v1.0
 * @create: 2021-06-10 15:18
 **/
@RunWith(PowerMockRunner.class)
@PrepareForTest({IseHttpClientTest.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class IseHttpClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getApiKey();
    String filePath = "src/test/resources/audio/cn/read_sentence_cn.pcm";

    @Test
    public void defaultParamTest() {
        IseHttpClient client = new IseHttpClient
                .Builder(appId, apiKey, IseAueEnum.RAW, IseLanguageEnum.ZH_CN, IseCategoryEnum.READ_SENTENCE)
                .build();
        Assert.assertEquals(client.getAppId(), appId);
        Assert.assertEquals(client.getApiKey(), apiKey);
        Assert.assertEquals(client.getResultLevel(), IseResultLevelEnum.ENTIRETY);
        Assert.assertEquals(client.getAue(), IseAueEnum.RAW);
        Assert.assertEquals(client.getLanguage(), IseLanguageEnum.ZH_CN);
        Assert.assertEquals(client.getCategory(), IseCategoryEnum.READ_SENTENCE);
    }

    @Test
    public void testParamBuild() {
        IseHttpClient client = new IseHttpClient
                .Builder(appId, apiKey, IseAueEnum.RAW, IseLanguageEnum.ZH_CN, IseCategoryEnum.READ_SENTENCE)
                .aue(IseAueEnum.SPEEX)
                .language(IseLanguageEnum.EN_US)
                .category(IseCategoryEnum.READ_CHAPTER)
                .resultLevel()
                .speexSize("100")
                .extraAbility()
                .callTimeout(1)
                .connectTimeout(2)
                .readTimeout(3)
                .writeTimeout(4)
                .retryOnConnectionFailure(true)
                .build();
        Assert.assertEquals(client.getAue(), IseAueEnum.SPEEX);
        Assert.assertEquals(client.getSpeexSize(), "100");
        Assert.assertEquals(client.getResultLevel(), IseResultLevelEnum.SIMPLE);
        Assert.assertEquals(client.getLanguage(), IseLanguageEnum.EN_US);
        Assert.assertEquals(client.getCategory(), IseCategoryEnum.READ_CHAPTER);
        Assert.assertEquals(client.getExtraAbility(), "multi_dimension");
        Assert.assertEquals(client.getCallTimeout(), 1);
        Assert.assertEquals(client.getConnectTimeout(), 2);
        Assert.assertEquals(client.getReadTimeout(), 3);
        Assert.assertEquals(client.getWriteTimeout(), 4);
        Assert.assertEquals(client.getRetryOnConnectionFailure(), true);
    }

    @Test
    public void testSuccessByString() throws IOException, HttpException {
        IseHttpClient client = new IseHttpClient
                .Builder(appId, apiKey, IseAueEnum.RAW, IseLanguageEnum.ZH_CN, IseCategoryEnum.READ_SENTENCE)
                .build();
        InputStream inputStream = new FileInputStream(new File(filePath));
        byte[] bytes = IOUtils.readFully(inputStream, -1, true);
        System.out.println(client.send(Base64.getEncoder().encodeToString(bytes), "今天天气怎么样"));
    }

}
