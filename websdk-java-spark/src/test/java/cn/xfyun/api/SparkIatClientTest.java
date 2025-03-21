package cn.xfyun.api;

import cn.xfyun.config.SparkIatModelEnum;
import cn.xfyun.model.sign.AbstractSignature;
import cn.xfyun.model.sparkiat.response.SparkIatResponse;
import cn.xfyun.service.sparkiat.AbstractSparkIatWebSocketListener;
import cn.xfyun.util.AuthUtil;
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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author <zyding6@iflytek.com>
 * @description 大模型语音听写
 * @date 2025/3/12
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SparkIatClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class SparkIatClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getSparkIatAPPKey();
    private static final String apiSecret = PropertiesConfig.getSparkIatAPPSecret();

    private String filePath = "audio/16k_10.pcm";
    //    private String filePath = "audio/20210329145025829.mp3";
    private String resourcePath = this.getClass().getResource("/").getPath();


    @Test
    public void defaultParamTest() {
        SparkIatClient sparkIatClient = new SparkIatClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .mulLanguage(SparkIatModelEnum.ZH_CN_MANDARIN.getCode())
                .build();

        Assert.assertEquals(sparkIatClient.getAppId(), appId);
        Assert.assertEquals(sparkIatClient.getApiKey(), apiKey);
        Assert.assertEquals(sparkIatClient.getApiSecret(), apiSecret);

        Assert.assertTrue(sparkIatClient.getOriginHostUrl().contains("iat"));
        Assert.assertTrue(sparkIatClient.getHostUrl().contains("iat"));
        Assert.assertEquals(sparkIatClient.getLanguage(), "zh_cn");
        Assert.assertEquals(sparkIatClient.getDomain(), "slm");
        Assert.assertEquals(sparkIatClient.getAccent(), "mandarin");
        Assert.assertEquals(sparkIatClient.getEos(), 6000);
        Assert.assertEquals(sparkIatClient.getEncoding(), "raw");
        Assert.assertTrue(sparkIatClient.getFrameSize() == 1280);
        Assert.assertEquals(sparkIatClient.getVinfo(), 0);

        Assert.assertEquals(sparkIatClient.getCallTimeout(), 0);
        Assert.assertEquals(sparkIatClient.getConnectTimeout(), 10000);
        Assert.assertEquals(sparkIatClient.getReadTimeout(), 10000);
        Assert.assertEquals(sparkIatClient.getWriteTimeout(), 10000);
        Assert.assertEquals(sparkIatClient.getPingInterval(), 0);
        Assert.assertTrue(sparkIatClient.isRetryOnConnectionFailure());

        Assert.assertNotNull(sparkIatClient.getOkHttpClient());
    }


    @Test
    public void testParamBuild() {
        SparkIatClient sparkIatClient = new SparkIatClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .mulLanguage(SparkIatModelEnum.ZH_CN_MULACC.getCode())
                .hostUrl("http://www.iflytek.com")
                .eos(3000)
                .dwa("wpgs1")
                .ptt(0)
                .rlang("zh-hk")
                .encoding("speex")
                .vinfo(1)
                .nunum(0)
                .nbest(3)
                .wbest(5)
                .frameSize(120)
                .callTimeout(2000, TimeUnit.MILLISECONDS)
                .connectTimeout(2000, TimeUnit.MILLISECONDS)
                .readTimeout(2000, TimeUnit.MILLISECONDS)
                .writeTimeout(2000, TimeUnit.MILLISECONDS)
                .pingInterval(10, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(false)
                .build();

        Assert.assertEquals(sparkIatClient.getAppId(), appId);
        Assert.assertEquals(sparkIatClient.getApiKey(), apiKey);
        Assert.assertEquals(sparkIatClient.getApiSecret(), apiSecret);

        Assert.assertTrue(sparkIatClient.getOriginHostUrl().contains("iat"));
        Assert.assertTrue(sparkIatClient.getHostUrl().contains("iat"));
        Assert.assertEquals(sparkIatClient.getLanguage(), "zh_cn");
        Assert.assertEquals(sparkIatClient.getDomain(), "slm");
        Assert.assertEquals(sparkIatClient.getAccent(), "mandarin");
        Assert.assertEquals(sparkIatClient.getEos(), 6000);
        Assert.assertEquals(sparkIatClient.getEncoding(), "raw");
        Assert.assertTrue(sparkIatClient.getFrameSize() == 1280);
        Assert.assertEquals(sparkIatClient.getVinfo(), 0);

        Assert.assertEquals(sparkIatClient.getCallTimeout(), 0);
        Assert.assertEquals(sparkIatClient.getConnectTimeout(), 10000);
        Assert.assertEquals(sparkIatClient.getReadTimeout(), 10000);
        Assert.assertEquals(sparkIatClient.getWriteTimeout(), 10000);
        Assert.assertEquals(sparkIatClient.getPingInterval(), 0);
        Assert.assertTrue(sparkIatClient.isRetryOnConnectionFailure());

        Assert.assertNotNull(sparkIatClient.getOkHttpClient());

    }

    @Test
    public void testSignature() throws MalformedURLException, SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        SparkIatClient sparkIatClient = new SparkIatClient.Builder()
                .mulLanguage(SparkIatModelEnum.ZH_CN_MULACC.getCode())
                .signature(appId, apiKey, apiSecret)
                .build();
        sparkIatClient.send(null, null, new AbstractSparkIatWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, SparkIatResponse sparkIatResponse) {
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            }
        });

        Assert.assertTrue(sparkIatClient.getRequest().url().toString().contains("iat-api.xfyun.cn/v2/iat"));

        AbstractSignature signature = sparkIatClient.getSignature();
        Assert.assertNotNull(signature);
        Assert.assertEquals(signature.getId(), apiKey);
        Assert.assertEquals(signature.getKey(), apiSecret);

        URL url = new URL("https://iat-api.xfyun.cn/v2/iat");
        String ts = signature.getTs();
        StringBuilder builder = new StringBuilder("host: ").append(url.getHost()).append("\n").//
                append("date: ").append(ts).append("\n").//
                append("GET ").append(url.getPath()).append(" HTTP/1.1");
        Charset charset = Charset.forName("UTF-8");
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
        String sha = Base64.getEncoder().encodeToString(hexDigits);

        Assert.assertEquals(sha, sparkIatClient.getSignature().getSigna());

        // 手动生成
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 工具生成
        String auth = AuthUtil.generateAuthorization(signature, "hmac-sha256");

        Assert.assertEquals(authorization, auth);
    }

    @Test
    public void testErrorSignature() throws MalformedURLException, SignatureException, FileNotFoundException {
        SparkIatClient sparkIatClient = new SparkIatClient.Builder()
                .mulLanguage(SparkIatModelEnum.ZH_CN_MULACC.getCode())
                .signature("123456", apiKey, apiSecret)
                .build();
        File file = new File(resourcePath + filePath);
        sparkIatClient.send(file, new AbstractSparkIatWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, SparkIatResponse iatResponse) {
                Assert.assertNotNull(iatResponse);
                Assert.assertNotNull(iatResponse.getHeader().getMessage());

                Assert.assertNotEquals(iatResponse.getHeader().getCode(), 101);
                Assert.assertEquals(iatResponse.getHeader().getCode(), 10313);
                sparkIatClient.closeWebsocket();
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            }
        });
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testSuccess() throws FileNotFoundException, InterruptedException, MalformedURLException, SignatureException {
        SparkIatClient sparkIatClient = new SparkIatClient.Builder()
                .mulLanguage(SparkIatModelEnum.ZH_CN_MANDARIN.getCode())
                .dwa("wpgs")
                .signature(appId, apiKey, apiSecret)
                .build();

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        File file = new File(resourcePath + filePath);
        sparkIatClient.send(file, new AbstractSparkIatWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, SparkIatResponse resp) {
                Assert.assertNotNull(resp);
                Assert.assertNotNull(resp.getHeader().getMessage());
//                System.out.println(StringUtils.gson.toJson(resp));
                if (resp.getHeader().getCode() != 0) {
                    System.out.println("code=>" + resp.getHeader().getCode() + " error=>" + resp.getHeader().getMessage() + " sid=" + resp.getHeader().getSid());
                    System.out.println("错误码查询链接：https://www.xfyun.cn/document/error-code");
                    return;
                }
                if (null != resp.getPayload()) {
                    if (null != resp.getPayload().getResult().getText()) {
                        byte[] decodedBytes = Base64.getDecoder().decode(resp.getPayload().getResult().getText());
                        String decodeRes = new String(decodedBytes, StandardCharsets.UTF_8);
//                        System.out.println("中间识别结果 ==》" + decodeRes);
                        SparkIatResponse.JsonParseText jsonParseText = StringUtils.gson.fromJson(decodeRes, SparkIatResponse.JsonParseText.class);

                        String mark = "";
                        if (jsonParseText.getPgs().equals("apd")) {
                            mark = "结果追加到上面结果";
                        } else if (jsonParseText.getPgs().equals("rpl")) {
                            mark = "结果替换前面" + jsonParseText.getRg().get(0) + "到" + jsonParseText.getRg().get(1);
                        }
                        System.out.print("中间识别结果 【" + mark + "】==》");

                        List<SparkIatResponse.Ws> wsList = jsonParseText.getWs();
                        for (SparkIatResponse.Ws ws : wsList) {
                            List<SparkIatResponse.Cw> cwList = ws.getCw();
                            for (SparkIatResponse.Cw cw : cwList) {
                                System.out.print(cw.getW());
                            }
                        }
                    }
                    if (resp.getPayload().getResult().getStatus() == 2) { // 最终结果  说明数据全部返回完毕，可以关闭连接，释放资源
                        System.out.println("session end ");
                        Date dateEnd = new Date();
                        System.out.println(sdf.format(dateBegin) + "开始");
                        System.out.println(sdf.format(dateEnd) + "结束");
                        System.out.println("耗时:" + (dateEnd.getTime() - dateBegin.getTime()) + "ms");
//                         System.out.println("最终识别结果 ==》" + decodeRes);  // 按照规则替换与追加出最终识别结果
                        System.out.println();
                        System.out.println("本次识别sid ==》" + resp.getHeader().getSid());
                        webSocket.close(1000, "");
                    }
                }
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            }
        });
        Thread.sleep(60000);
    }

    @Test
    public void testSendNull() throws MalformedURLException, SignatureException, InterruptedException {
        SparkIatClient sparkIatClient = new SparkIatClient.Builder()
                .mulLanguage(SparkIatModelEnum.ZH_CN_MULACC.getCode())
                .signature(appId, apiKey, apiSecret)
                .build();

        InputStream inputStream = null;
        sparkIatClient.send(inputStream, new AbstractSparkIatWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, SparkIatResponse iatResponse) {
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
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
        });
        Thread.sleep(5000);
    }

    @Test
    public void testSendBytes() throws IOException, InterruptedException, SignatureException {
        SparkIatClient sparkIatClient = new SparkIatClient.Builder()
                .mulLanguage(SparkIatModelEnum.ZH_CN_MULACC.getCode())
                .signature(appId, apiKey, apiSecret)
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

                        sparkIatClient.closeWebsocket();
                    } else {
                        // 根据返回的数据处理
                        System.out.println(StringUtils.gson.toJson(resp));
                    }
                }

            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            }
        };
        sparkIatClient.send(buffer, inputStream, iatWebSocketListener);
        Thread.sleep(60000);
    }
}
