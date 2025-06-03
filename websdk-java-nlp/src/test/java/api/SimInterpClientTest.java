package api;

import cn.xfyun.api.SimInterpClient;
import cn.xfyun.model.simult.response.Recognition;
import cn.xfyun.model.simult.response.SimInterpResponse;
import cn.xfyun.model.simult.response.Streamtrans;
import cn.xfyun.service.simult.SimInterpWebSocketListener;
import cn.xfyun.util.StringUtils;
import config.PropertiesConfig;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 同声传译 Client单元测试
 *
 * @author <zyding6@ifytek.com>
 */
public class SimInterpClientTest {

    private static final Logger logger = LoggerFactory.getLogger(SimInterpClientTest.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getSimInterpClientApiKey();
    private static final String apiSecret = PropertiesConfig.getSimInterpClientApiSecret();
    private String resourcePath;
    /**
     * 传译后文件保存位置
     */
    private String saveFilePath;
    /**
     * 需要传译的音频文件位置
     */
    private String inputFilePath;

    @Before
    public void init() {
        resourcePath = Objects.requireNonNull(this.getClass().getResource("/")).getPath();
        saveFilePath = "audio/" + UUID.randomUUID() + ".pcm";
        inputFilePath = "audio/original.pcm";
    }

    @Test
    public void testParamBuild() {
        SimInterpClient client = new SimInterpClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .vcn("vcn")
                .executor(Executors.newFixedThreadPool(1))
                .language("123")
                .languageType(10)
                .domain("domain")
                .accent("accent")
                .eos(-1)
                .vto(-1)
                .nunum(-1)
                .from("from")
                .to("to")
                .encoding("encoding")
                .sampleRate(-1)
                .channels(-1)
                .bitDepth(-1)
                .callTimeout(0, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .pingInterval(0, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        Assert.assertEquals(client.getVcn(), "vcn");
        Assert.assertEquals(client.getLanguage(), "123");
        Assert.assertEquals(client.getLanguageType(), 10);
        Assert.assertEquals(client.getDomain(), "domain");
        Assert.assertEquals(client.getAccent(), "accent");
        Assert.assertEquals(client.getFrom(), "from");
        Assert.assertEquals(client.getTo(), "to");
        Assert.assertEquals(client.getEncoding(), "encoding");
        Assert.assertEquals(client.getSampleRate(), -1);
        Assert.assertEquals(client.getChannels(), -1);
        Assert.assertEquals(client.getBitDepth(), -1);
        Assert.assertTrue(client.getEos() == -1);
        Assert.assertTrue(client.getVto() == -1);
        Assert.assertTrue(client.getNunum() == -1);
        Assert.assertNotNull(client.getExecutor());
        Assert.assertEquals(client.getCallTimeout(), 0);
        Assert.assertEquals(client.getConnectTimeout(), 10000);
        Assert.assertEquals(client.getWriteTimeout(), 10000);
        Assert.assertEquals(client.getReadTimeout(), 10000);
        Assert.assertEquals(client.getPingInterval(), 0);
        Assert.assertTrue(client.isRetryOnConnectionFailure());
    }

    @Test
    public void testStart() {
        SimInterpClient client = new SimInterpClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .vcn("x2_catherine")
                .build();

        WebSocket start = null;
        try {
            start = client.start(new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    super.onOpen(webSocket, response);
                }
            });
            client.sendMessage(start, new byte[]{1, 2, 3}, 0);
        } catch (Exception e) {
            logger.error("请求失败", e);
        } finally {
            if (start != null) {
                try {
                    start.close(1000, "");
                } catch (Exception e) {
                    logger.error("ws关闭失败", e);
                }
            }
        }
    }


    @Test
    public void testSuccess() {
        SimInterpClient client = new SimInterpClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .vcn("x2_catherine")
                .build();

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        // 默认输入音频的pcm文件
        File inputFile = new File(resourcePath + inputFilePath);
        // 合成后音频存储路径
        File saveFile = new File(resourcePath + saveFilePath);

        try {
            // 返回的音频结果
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            FileOutputStream os = new FileOutputStream(saveFile);

            // 转写结果
            StringBuffer finalResult = new StringBuffer();
            // 存储流式返回结果的Map sn -> content
            Map<Integer, String> contentMap = new TreeMap<>();

            // 翻译结果
            StringBuffer translateSrcResult = new StringBuffer();
            StringBuffer translateDstResult = new StringBuffer();

            client.send(inputFile, new SimInterpWebSocketListener() {
                @Override
                public void onSuccess(WebSocket webSocket, SimInterpResponse resp) {
                    if (resp.getHeader().getCode() != 0) {
                        logger.error("code=>{}，error=>{}，sid=>{}", resp.getHeader().getCode(), resp.getHeader().getMessage(), resp.getHeader().getSid());
                        logger.warn("错误码查询链接：https://www.xfyun.cn/document/error-code");
                        return;
                    }

                    if (resp.getPayload() != null) {
                        // 处理转写结果
                        if (null != resp.getPayload().getRecognitionResults()) {
                            byte[] decodedBytes = Base64.getDecoder().decode(resp.getPayload().getRecognitionResults().getText());
                            String decodeRes = new String(decodedBytes, StandardCharsets.UTF_8);
                            if (StringUtils.isNullOrEmpty(decodeRes)) {
                                return;
                            }
                            // logger.info("中间识别结果 ==>{}", decodeRes);
                            Recognition recognition = StringUtils.gson.fromJson(decodeRes, Recognition.class);
                            // 拼接单句ws的内容
                            StringBuilder reqResult = getWsContent(recognition);
                            // 根据pgs参数判断是拼接还是替换
                            if (recognition.getPgs().equals("apd")) {
                                // 直接添加
                                contentMap.put(recognition.getSn(), reqResult.toString());
                                logger.info("中间识别结果 【{}】 拼接后结果==> {}", reqResult, getLastResult(contentMap));
                            } else if (recognition.getPgs().equals("rpl")) {
                                List<Integer> rg = recognition.getRg();
                                int startIndex = rg.get(0);
                                int endIndex = rg.get(1);
                                // 替换 rg 范围内的内容
                                for (int i = startIndex; i <= endIndex; i++) {
                                    contentMap.remove(i);
                                }
                                contentMap.put(recognition.getSn(), reqResult.toString());
                                logger.info("中间识别结果 【{}】 替换后结果==> {}", reqResult, getLastResult(contentMap));
                            }

                            if (resp.getPayload().getRecognitionResults().getStatus() == 2) {
                                // 转写流程结束
                                logger.info("转写流程结束==========================>");
                            }
                        }

                        // 处理翻译结果
                        if (null != resp.getPayload().getStreamtransResults()) {
                            String text = resp.getPayload().getStreamtransResults().getText();
                            byte[] decodedBytes = Base64.getDecoder().decode(text);
                            String decodeRes = new String(decodedBytes, StandardCharsets.UTF_8);
                            if (StringUtils.isNullOrEmpty(decodeRes)) {
                                return;
                            }
                            Streamtrans streamtrans = StringUtils.gson.fromJson(decodeRes, Streamtrans.class);
                            if (1 == streamtrans.getIs_final()) {
                                // 翻译流程结果
                                translateSrcResult.append(streamtrans.getSrc());
                                translateDstResult.append(streamtrans.getDst());
                                logger.info("翻译流程结束==========================>");
                            } else if (0 == streamtrans.getIs_final()) {
                                // 中间结果
                                logger.info("翻译中间结果 ==> 原文：{}， 译文：{}", streamtrans.getSrc(), streamtrans.getDst());
                            }
                        }

                        // 处理
                        if (null != resp.getPayload().getTtsResults()) {
                            String result = resp.getPayload().getTtsResults().getAudio();
                            if (result != null) {
                                byte[] audio = Base64.getDecoder().decode(result);
                                try {
                                    byteArrayOutputStream.write(audio);
                                    // audioPlayer.play(audio);
                                } catch (IOException e) {
                                    logger.error("返回音频合成异常", e);
                                }
                            }

                            if (Objects.equals(resp.getPayload().getTtsResults().getStatus(), 2)) {
                                try {
                                    os.write(byteArrayOutputStream.toByteArray());
                                    os.flush();
                                    logger.info("音频合成流程结束==========================>");
                                } catch (IOException e) {
                                    logger.error("音频解析异常", e);
                                } finally {
                                    try {
                                        os.close();
                                    } catch (IOException e) {
                                        logger.warn("流关闭异常：{}", e.getMessage(), e);
                                    }
                                }
                            }
                        }
                    }
                    // 整体流程结束
                    if (resp.getHeader().getStatus() == 2) {
                        logger.info("session end");
                        Date dateEnd = new Date();
                        logger.info("{}开始", sdf.format(dateBegin));
                        logger.info("{}结束", sdf.format(dateEnd));
                        logger.info("耗时：{}ms", dateEnd.getTime() - dateBegin.getTime());
                        if (!contentMap.isEmpty()) {
                            // 获取最终拼接结果
                            logger.info("最终转写识别结果 ==>{}", getLastResult(contentMap));
                        } else {
                            logger.info("最终转写识别结果 ==>{}", finalResult);
                        }
                        logger.info("翻译最终结果 ==> 原文：{}， 译文：{}", translateSrcResult, translateDstResult);
                        logger.info("合成的音频文件保存在：" + saveFile.getPath());
                        logger.info("本次识别sid ==>{}", resp.getHeader().getSid());
                        try {
                            byteArrayOutputStream.close();
                        } catch (IOException e) {
                            logger.error(e.getMessage(), e);
                        }
                        try {
                            os.close();
                        } catch (IOException e) {
                            logger.error(e.getMessage(), e);
                        }
                        webSocket.close(1000, "");
                    }
                }

                @Override
                public void onClose(WebSocket webSocket, int code, String reason) {
                    logger.info("关闭连接,code是{},reason:{}", code, reason);
                    System.exit(0);
                }

                @Override
                public void onConnect(WebSocket webSocket, Response response) {
                }

                @Override
                public void onFail(WebSocket webSocket, Throwable throwable, Response response) {
                    logger.error(throwable.getMessage());
                    System.exit(0);
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.error("错误码查询链接：https://www.xfyun.cn/document/error-code");
        }
    }

    private static String getLastResult(Map<Integer, String> contentMap) {
        StringBuilder result = new StringBuilder();
        for (String part : contentMap.values()) {
            result.append(part);
        }
        return result.toString();
    }

    private static StringBuilder getWsContent(Recognition recognition) {
        StringBuilder reqResult = new StringBuilder();
        List<Recognition.Ws> wsList = recognition.getWs();
        for (Recognition.Ws ws : wsList) {
            List<Recognition.Ws.Cw> cwList = ws.getCw();
            for (Recognition.Ws.Cw cw : cwList) {
                reqResult.append(cw.getW());
            }
        }
        return reqResult;
    }
}
