package cn.xfyun.api;

import cn.xfyun.exception.HttpException;
import config.PropertiesConfig;
import org.apache.commons.codec.binary.Base64;
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

/**
 * @author: <flhong2@iflytek.com>
 * @description: 语音评测(普通版)测试类
 * @version: v1.0
 * @create: 2021-06-10 15:18
 **/
@RunWith(PowerMockRunner.class)
@PrepareForTest({IatClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class IseCommonTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getApiKey();
    String filePath = "src/test/resources/audio/cn/read_sentence_cn.pcm";

    @Test
    public void defaultParamTest() {
        IseCommonClient iseCommonClient = new IseCommonClient.Builder()
                .appId(appId).apiKey(apiKey)
                .build();

        Assert.assertEquals(iseCommonClient.getAppId(), appId);
        Assert.assertEquals(iseCommonClient.getApiKey(), apiKey);
        Assert.assertEquals(iseCommonClient.getResultLevel(), "entirety");
    }

    @Test
    public void testParamBuild() {
        IseCommonClient iseCommonClient = new IseCommonClient.Builder()
                .appId(appId).apiKey(apiKey).aue("raw").speexSize("70")
                .resultLevel("simple").language("zh_cn").category("read_sentence")
                .extraAbility("multi_dimension").text("今天天气怎么样？")
                .build();

        Assert.assertEquals(iseCommonClient.getAppId(), appId);
        Assert.assertEquals(iseCommonClient.getApiKey(), apiKey);
        Assert.assertEquals(iseCommonClient.getAue(), "raw");
        Assert.assertEquals(iseCommonClient.getSpeexSize(), "70");
        Assert.assertEquals(iseCommonClient.getResultLevel(), "simple");
        Assert.assertEquals(iseCommonClient.getLanguage(), "zh_cn");
        Assert.assertEquals(iseCommonClient.getCategory(), "read_sentence");
        Assert.assertEquals(iseCommonClient.getExtraAbility(), "multi_dimension");
        Assert.assertEquals(iseCommonClient.getText(), "今天天气怎么样？");
    }

    @Test
    public void testSuccessByFile() throws IOException, HttpException {
        IseCommonClient iseCommonClient = new IseCommonClient.Builder()
                .appId(appId).apiKey(apiKey).aue("raw").speexSize("70")
                .resultLevel("simple").language("zh_cn").category("read_sentence")
                .extraAbility("multi_dimension").text("今天天气怎么样？")
                .build();

//        String result = iseCommonClient.send(new File(filePath));
//        System.out.println("返回结果: " + result);
    }

    @Test
    public void testSuccessByStream() throws IOException, HttpException {
        IseCommonClient iseCommonClient = new IseCommonClient.Builder()
                .appId(appId).apiKey(apiKey).aue("raw").speexSize("70")
                .resultLevel("simple").language("zh_cn").category("read_sentence")
                .extraAbility("multi_dimension").text("今天天气怎么样？")
                .build();
        InputStream inputStream = new FileInputStream(new File(filePath));
        String result = iseCommonClient.send(inputStream);
        System.out.println("返回结果: " + result);
    }

    @Test
    public void testSuccessByString() throws IOException, HttpException {
        IseCommonClient iseCommonClient = new IseCommonClient.Builder()
                .appId(appId).apiKey(apiKey).aue("raw").speexSize("70")
                .resultLevel("simple").language("zh_cn").category("read_sentence")
                .extraAbility("multi_dimension").text("今天天气怎么样？")
                .build();

        InputStream inputStream = new FileInputStream(new File(filePath));
        byte[] bytes = IOUtils.readFully(inputStream, -1, true);
        String result = iseCommonClient.send(new String(Base64.encodeBase64(bytes), "UTF-8"));
        System.out.println("返回结果: " + result);
    }

    @Test
    public void testSendNull() throws IOException, HttpException {
        IseCommonClient iseCommonClient = new IseCommonClient.Builder()
                .appId(appId).apiKey(apiKey).aue("raw").speexSize("70")
                .resultLevel("simple").language("zh_cn").category("read_sentence")
                .extraAbility("multi_dimension").text("今天天气怎么样？")
                .build();

        String result = iseCommonClient.send(" ");
        System.out.println("返回结果: " + result);
    }
}
