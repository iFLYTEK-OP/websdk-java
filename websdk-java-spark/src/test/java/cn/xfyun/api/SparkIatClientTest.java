package cn.xfyun.api;

import cn.xfyun.config.SparkIatModelEnum;
import cn.xfyun.model.sparkiat.response.SparkIatResponse;
import cn.xfyun.service.sparkiat.AbstractSparkIatWebSocketListener;
import cn.xfyun.util.StringUtils;
import config.PropertiesConfig;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 大模型语音听写 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SparkIatClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class SparkIatClientTest {

    private static final Logger logger = LoggerFactory.getLogger(SparkIatClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getSparkIatAPPKey();
    private static final String apiSecret = PropertiesConfig.getSparkIatAPPSecret();

    private final String filePath = "audio/16k_10.pcm";

    private final String resourcePath = Objects.requireNonNull(this.getClass().getResource("/")).getPath();


    @Test
    public void defaultParamTest() {
        SparkIatClient sparkIatClient = new SparkIatClient.Builder()
                .signature(appId, apiKey, apiSecret, SparkIatModelEnum.ZH_CN_MANDARIN.getCode())
                .callTimeout(0, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .pingInterval(0, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .executor(null)
                .hostUrl("test.url")
                .encoding("raw")
                .dwa("123")
                .smth(1)
                .eos(7000)
                .ptt(1)
                .rlang("123")
                .nunum(1)
                .nbest(1)
                .wbest(1)
                .vinfo(0)
                .dhw("dhw")
                .opt(1)
                .ltc(1)
                .sampleRate(24000)
                .channels(2)
                .bitDepth(24)
                .frameSize(120)
                .textCompress("plain")
                .textEncoding("utf8")
                .textFormat("123")
                .ln("none")
                .build();

        Assert.assertEquals(sparkIatClient.getAppId(), appId);
        Assert.assertEquals(sparkIatClient.getApiKey(), apiKey);
        Assert.assertEquals(sparkIatClient.getApiSecret(), apiSecret);

        Assert.assertEquals(sparkIatClient.getCallTimeout(), 0);
        Assert.assertEquals(sparkIatClient.getConnectTimeout(), 10000);
        Assert.assertEquals(sparkIatClient.getWriteTimeout(), 10000);
        Assert.assertEquals(sparkIatClient.getReadTimeout(), 10000);
        Assert.assertEquals(sparkIatClient.getPingInterval(), 0);
        Assert.assertTrue(sparkIatClient.isRetryOnConnectionFailure());

        Assert.assertTrue(sparkIatClient.getOriginHostUrl().contains("test"));
        Assert.assertEquals(sparkIatClient.getLangType().intValue(), 1);
        Assert.assertEquals(sparkIatClient.getLanguage(), "zh_cn");
        Assert.assertEquals(sparkIatClient.getDomain(), "slm");
        Assert.assertEquals(sparkIatClient.getAccent(), "mandarin");
        Assert.assertEquals(sparkIatClient.getEncoding(), "raw");
        Assert.assertEquals(sparkIatClient.getDwa(), "123");
        Assert.assertEquals(sparkIatClient.getSmth().intValue(), 1);
        Assert.assertEquals(sparkIatClient.getEos(), 7000);
        Assert.assertEquals(sparkIatClient.getPtt().intValue(), 1);
        Assert.assertEquals(sparkIatClient.getRlang(), "123");
        Assert.assertEquals(sparkIatClient.getNunum().intValue(), 1);
        Assert.assertEquals(sparkIatClient.getNbest().intValue(), 1);
        Assert.assertEquals(sparkIatClient.getWbest().intValue(), 1);
        Assert.assertEquals(sparkIatClient.getVinfo(), 0);
        Assert.assertEquals(sparkIatClient.getDhw(), "dhw");
        Assert.assertEquals(sparkIatClient.getOpt().intValue(), 1);
        Assert.assertEquals(sparkIatClient.getLtc().intValue(), 1);

        Assert.assertEquals(sparkIatClient.getSampleRate().intValue(), 24000);
        Assert.assertEquals(sparkIatClient.getChannels().intValue(), 2);
        Assert.assertEquals(sparkIatClient.getBitDepth().intValue(), 24);
        Assert.assertEquals(sparkIatClient.getFrameSize().intValue(), 120);
        Assert.assertEquals(sparkIatClient.getTextCompress(), "plain");
        Assert.assertEquals(sparkIatClient.getTextEncoding(), "utf8");
        Assert.assertEquals(sparkIatClient.getTextFormat(), "123");
        Assert.assertEquals(sparkIatClient.getLn(), "none");

        Assert.assertNotNull(sparkIatClient.getOkHttpClient());
        Assert.assertNull(sparkIatClient.getExecutor());
    }

    @Test
    public void testErrorSignature() throws MalformedURLException, SignatureException, FileNotFoundException {
        SparkIatClient sparkIatClient = new SparkIatClient.Builder()
                .signature("123456", apiKey, apiSecret, SparkIatModelEnum.ZH_CN_MULACC.getCode())
                .build();
        File file = new File(resourcePath + filePath);
        sparkIatClient.send(file, new AbstractSparkIatWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, SparkIatResponse iatResponse) {
                Assert.assertNotNull(iatResponse);
                Assert.assertNotNull(iatResponse.getHeader().getMessage());

                Assert.assertNotEquals(iatResponse.getHeader().getCode(), 101);
                Assert.assertEquals(iatResponse.getHeader().getCode(), 10313);
                webSocket.close(1000, "");
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            }

            @Override
            public void onClose(WebSocket webSocket, int code, String reason) {

            }
        });
        Assert.assertNotNull(sparkIatClient.getRequest());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testSuccess() throws FileNotFoundException, InterruptedException, MalformedURLException, SignatureException {
        SparkIatClient sparkIatClient = new SparkIatClient.Builder()
                .signature(appId, apiKey, apiSecret, SparkIatModelEnum.ZH_CN_MANDARIN.getCode())
                // 流式实时返回撰写结果
                .dwa("wpgs")
                .build();

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        File file = new File(resourcePath + filePath);
        StringBuffer finalResult = new StringBuffer();

        // 存储流式返回结果的Map sn -> content
        Map<Integer, String> contentMap = new TreeMap<>();
        sparkIatClient.send(file, new AbstractSparkIatWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, SparkIatResponse resp) {
                // logger.debug("{}", StringUtils.gson.toJson(resp));
                if (resp.getHeader().getCode() != 0) {
                    logger.error("code=>{}，error=>{}，sid=>{}", resp.getHeader().getCode(), resp.getHeader().getMessage(), resp.getHeader().getSid());
                    logger.warn("错误码查询链接：https://www.xfyun.cn/document/error-code");
                    return;
                }

                if (resp.getPayload() != null) {
                    // 非流式实时返回结果处理方式
                    /*if (resp.getPayload().getResult() != null) {
                        String tansTxt = resp.getPayload().getResult().getText();
                        if (null != tansTxt) {
                            // 解码转写结果
                            byte[] decodedBytes = Base64.getDecoder().decode(resp.getPayload().getResult().getText());
                            String decodeRes = new String(decodedBytes, StandardCharsets.UTF_8);
                            SparkIatResponse.JsonParseText jsonParseText = StringUtils.gson.fromJson(decodeRes, SparkIatResponse.JsonParseText.class);

                            StringBuilder text = new StringBuilder();
                            for (SparkIatResponse.Ws ws : jsonParseText.getWs()) {
                                List<SparkIatResponse.Cw> cwList = ws.getCw();
                                for (SparkIatResponse.Cw cw : cwList) {
                                    text.append(cw.getW());
                                }
                            }

                            finalResult.append(text);
                            logger.info("中间识别结果 ==>{}", text);
                        }
                    }*/

                    // 流式实时返回结果处理方式
                    if (null != resp.getPayload().getResult().getText()) {
                        byte[] decodedBytes = Base64.getDecoder().decode(resp.getPayload().getResult().getText());
                        String decodeRes = new String(decodedBytes, StandardCharsets.UTF_8);
                        // logger.info("中间识别结果 ==>{}", decodeRes);
                        SparkIatResponse.JsonParseText jsonParseText = StringUtils.gson.fromJson(decodeRes, SparkIatResponse.JsonParseText.class);
                        // 拼接单句ws的内容
                        StringBuilder reqResult = getWsContent(jsonParseText);
                        // 根据pgs参数判断是拼接还是替换
                        if (jsonParseText.getPgs().equals("apd")) {
                            // 直接添加
                            contentMap.put(jsonParseText.getSn(), reqResult.toString());
                            logger.info("中间识别结果 【{}】 拼接后结果==> {}", reqResult, getLastResult(contentMap));
                        } else if (jsonParseText.getPgs().equals("rpl")) {
                            List<Integer> rg = jsonParseText.getRg();
                            int startIndex = rg.get(0);
                            int endIndex = rg.get(1);
                            // 替换 rg 范围内的内容
                            for (int i = startIndex; i <= endIndex; i++) {
                                contentMap.remove(i);
                            }
                            contentMap.put(jsonParseText.getSn(), reqResult.toString());
                            logger.info("中间识别结果 【{}】 替换后结果==> {}", reqResult, getLastResult(contentMap));
                        }
                    }

                    if (resp.getPayload().getResult().getStatus() == 2) {
                        // resp.data.status ==2 说明数据全部返回完毕，可以关闭连接，释放资源
                        logger.info("session end");
                        Date dateEnd = new Date();
                        logger.info("{}开始", sdf.format(dateBegin));
                        logger.info("{}结束", sdf.format(dateEnd));
                        logger.info("耗时：{}ms", dateEnd.getTime() - dateBegin.getTime());
                        if (!contentMap.isEmpty()) {
                            // 获取最终拼接结果
                            logger.info("最终识别结果 ==>{}", getLastResult(contentMap));
                        } else {
                            logger.info("最终识别结果 ==>{}", finalResult);
                        }
                        logger.info("本次识别sid ==>{}", resp.getHeader().getSid());
                        webSocket.close(1000, "");
                    }
                }
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                logger.error("异常信息: {}", t.getMessage(), t);
            }

            @Override
            public void onClose(WebSocket webSocket, int code, String reason) {
                logger.info("关闭连接,code是{},reason:{}", code, reason);
            }
        });

        TimeUnit.MILLISECONDS.sleep(30000);
    }

    @Test
    public void testSendNull() throws MalformedURLException, SignatureException, InterruptedException {
        SparkIatClient sparkIatClient = new SparkIatClient.Builder()
                .signature(appId, apiKey, apiSecret, SparkIatModelEnum.ZH_CN_MULACC.getCode())
                .build();

        InputStream inputStream = null;
        sparkIatClient.send(inputStream, new AbstractSparkIatWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, SparkIatResponse iatResponse) {
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            }

            @Override
            public void onClose(WebSocket webSocket, int code, String reason) {

            }
        });

        byte[] bytes = null;
        sparkIatClient.send(bytes, null, new AbstractSparkIatWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, SparkIatResponse iatResponse) {
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            }

            @Override
            public void onClose(WebSocket webSocket, int code, String reason) {

            }
        });
        Thread.sleep(5000);
    }

    @Test
    public void testSendBytes() throws IOException, SignatureException {
        SparkIatClient sparkIatClient = new SparkIatClient.Builder()
                .signature(appId, apiKey, apiSecret, SparkIatModelEnum.ZH_CN_MULACC.getCode())
                .build();

        File file = new File(resourcePath + filePath);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024000];
        int len = inputStream.read(buffer);
        AbstractSparkIatWebSocketListener iatWebSocketListener = new AbstractSparkIatWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, SparkIatResponse resp) {
                Assert.assertNotNull(resp);
                Assert.assertNotNull(resp.getHeader().getMessage());

                byte[] decodedBytes = Base64.getDecoder().decode(resp.getPayload().getResult().getText());
                String decodeRes = new String(decodedBytes, StandardCharsets.UTF_8);

                SparkIatResponse.JsonParseText jsonParseText = StringUtils.gson.fromJson(decodeRes, SparkIatResponse.JsonParseText.class);
                List<SparkIatResponse.Ws> wss = jsonParseText.getWs();
                for (SparkIatResponse.Ws ws : wss) {
                    List<SparkIatResponse.Cw> cws = ws.getCw();
                    for (SparkIatResponse.Cw cw : cws) {
                        System.out.print(cw.getW());
                    }
                }

                if (resp.getHeader().getCode() != 0) {
                    System.out.println("code=>" + resp.getHeader().getCode() + " error=>" + resp.getHeader().getMessage() + " sid=" + resp.getHeader().getSid());
                    System.out.println("错误码查询链接：https://www.xfyun.cn/document/error-code");
                    return;
                }

                if (resp.getPayload() != null) {
                    if (resp.getPayload().getResult().getStatus() == 2) {
                        // resp.data.status ==2 说明数据全部返回完毕，可以关闭连接，释放资源
                        System.out.println("session end ");
                        System.out.println("本次识别sid ==》" + resp.getHeader().getSid());

                        webSocket.close(1000, "");
                    } else {
                        // 根据返回的数据处理
                        System.out.println(StringUtils.gson.toJson(resp));
                    }
                }

            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                webSocket.close(1000, "");
            }

            @Override
            public void onClose(WebSocket webSocket, int code, String reason) {
            }
        };
        sparkIatClient.send(buffer, inputStream, iatWebSocketListener);
    }

    private static String getLastResult(Map<Integer, String> contentMap) {
        StringBuilder result = new StringBuilder();
        for (String part : contentMap.values()) {
            result.append(part);
        }
        return result.toString();
    }

    private static StringBuilder getWsContent(SparkIatResponse.JsonParseText jsonParseText) {
        StringBuilder reqResult = new StringBuilder();
        List<SparkIatResponse.Ws> wsList = jsonParseText.getWs();
        for (SparkIatResponse.Ws ws : wsList) {
            List<SparkIatResponse.Cw> cwList = ws.getCw();
            for (SparkIatResponse.Cw cw : cwList) {
                // logger.info(cw.getW());
                reqResult.append(cw.getW());
            }
        }
        return reqResult;
    }
}
