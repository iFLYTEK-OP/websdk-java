package api;

import cn.xfyun.api.WordLibClient;
import cn.xfyun.config.CategoryEnum;
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

import java.util.Arrays;
import java.util.List;

/**
 * 词库 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({WordLibClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class WordLibClientTest {

    private static final Logger logger = LoggerFactory.getLogger(WordLibClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    /**
     * （图片 文本）合规的key
     */
    private static final String apiKey = PropertiesConfig.getTextComplianceClientApiKey();
    /**
     * （图片 文本）合规的secret
     */
    private static final String apiSecret = PropertiesConfig.getTextComplianceClientApiSecret();
    private WordLibClient client = null;

    @Before
    public void init() {
        client = new WordLibClient
                .Builder(appId, apiKey, apiSecret)
                .build();
    }

    @Test
    public void defaultParamTest() {
        Assert.assertEquals(client.getAppId(), appId);
        Assert.assertEquals(client.getApiKey(), apiKey);
        Assert.assertEquals(client.getApiSecret(), apiSecret);
        Assert.assertEquals(3, client.getReadTimeout());
        Assert.assertEquals(3, client.getWriteTimeout());
        Assert.assertEquals(3, client.getConnectTimeout());
    }

    @Test
    public void listLib() throws Exception {
        // 根据appid查询账户下所有词库
        String listLib = client.listLib();
        logger.info("{} 下所有词库结果：{}", appId, listLib);
    }

    @Test
    public void whiteLib() throws Exception {
        // 创建白名单
        String whiteLib = client.createWhiteLib("白名单词库1");
        logger.info("创建白名单词库返回结果：{}", whiteLib);
    }

    @Test
    public void blackLib() throws Exception {
        // 创建黑名单
        String blackLib = client.createBlackLib("黑名单词库1", CategoryEnum.CONTRABAND.getCode(), "block");
        logger.info("创建黑名单词库返回结果：{}", blackLib);
    }

    @Test
    public void info() throws Exception {
        // 查询词条明细
        String info = client.info("04eb999dfc024b7fa61b45d057cbca37");
        logger.info("查询词条明细结果：{}", info);
    }

    @Test
    public void addWord() throws Exception {
        // 添加词条
        List<String> search = Arrays.asList("傻缺", "蠢才");
        String addWord = client.addWord("04eb999dfc024b7fa61b45d057cbca37", search);
        logger.info("添加词条结果：{}", addWord);
    }

    @Test
    public void delList() throws Exception {
        // 删除词条
        List<String> delList = Arrays.asList("蠢才");
        String delWord = client.delWord("04eb999dfc024b7fa61b45d057cbca37", delList);
        logger.info("删除词条结果：{}", delWord);
    }

    @Test
    public void deleteLib() throws Exception {
        // 根据lib_id删除词库
        String deleteLib = client.deleteLib("04eb999dfc024b7fa61b45d057cbca37");
        logger.info("删除词库结果：{}", deleteLib);
    }
}
