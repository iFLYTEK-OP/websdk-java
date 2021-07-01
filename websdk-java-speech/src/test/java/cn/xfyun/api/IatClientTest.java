package cn.xfyun.api;

import config.PropertiesConfig;
import cn.xfyun.model.response.iat.IatResponse;
import cn.xfyun.model.response.iat.IatResult;
import cn.xfyun.service.iat.AbstractIatWebSocketListener;
import cn.xfyun.model.sign.AbstractSignature;
import cn.xfyun.util.AuthUtil;
import cn.xfyun.util.StringUtils;
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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author <ydwang16@iflytek.com>
 * @description 语音听写
 * @date 2021/3/29
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({IatClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class IatClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getApiKey();
    private static final String apiSecret = PropertiesConfig.getApiSecret();

    private String filePath = "audio/20210330141636536.pcm";
    private String resourcePath = this.getClass().getResource("/").getPath();


    @Test
    public void defaultParamTest() {
        IatClient iatClient = new IatClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        Assert.assertEquals(iatClient.getAppId(), appId);
        Assert.assertEquals(iatClient.getApiKey(), apiKey);
        Assert.assertEquals(iatClient.getAppId(), appId);
        Assert.assertEquals(iatClient.getApiSecret(), apiSecret);

        Assert.assertTrue(iatClient.getOriginHostUrl().contains("iat-api.xfyun.cn/v2/iat"));
        Assert.assertTrue(iatClient.getHostUrl().contains("iat-api.xfyun.cn/v2/iat"));
        Assert.assertEquals(iatClient.getLanguage(), "zh_cn");
        Assert.assertEquals(iatClient.getDomain(), "iat");
        Assert.assertEquals(iatClient.getAccent(), "mandarin");
        Assert.assertEquals(iatClient.getRlang(), "zh-cn");
        Assert.assertEquals(iatClient.getVad_eos(), 2000);
        Assert.assertEquals(iatClient.getPtt(), 1);
        Assert.assertTrue(iatClient.getFrameSize() == 1280);
        Assert.assertEquals(iatClient.getVinfo(), 0);
        Assert.assertEquals(iatClient.getNunum(), 1);
        Assert.assertEquals(iatClient.getFormat(), "audio/L16;rate=16000");
        Assert.assertEquals(iatClient.getEncoding(), "raw");
        Assert.assertTrue(iatClient.getSpeex_size() == 2);

        Assert.assertEquals(iatClient.getCallTimeout(), 0);
        Assert.assertEquals(iatClient.getConnectTimeout(), 10000);
        Assert.assertEquals(iatClient.getReadTimeout(), 10000);
        Assert.assertEquals(iatClient.getWriteTimeout(), 10000);
        Assert.assertEquals(iatClient.getPingInterval(), 0);
        Assert.assertTrue(iatClient.isRetryOnConnectionFailure());

        Assert.assertNotNull(iatClient.getOkHttpClient());
    }


    @Test
    public void testParamBuild() {
        IatClient iatClient = new IatClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .hostUrl("http://www.iflytek.com")
                .smallLanguage(false)
                .smallLanguage(true)
                .language("zh_hk")
                .domain("medical")
                .accent("mandrin1")
                .vad_eos(3000)
                .dwa("wpgs1")
                .pd("game")
                .ptt(0)
                .rlang("zh-hk")
                .format("audio/L16;rate=8000")
                .encoding("speex")
                .vinfo(1)
                .nunum(0)
                .speex_size(70)
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

        Assert.assertTrue(iatClient.getHostUrl().contains("iat-niche-api.xfyun.cn/v2/iat"));
        Assert.assertEquals(iatClient.getDomain(), "medical");
        Assert.assertEquals(iatClient.getLanguage(), "zh_hk");
        Assert.assertEquals(iatClient.getAccent(), "mandrin1");
        Assert.assertEquals(iatClient.getVad_eos(), 3000);
        Assert.assertEquals(iatClient.getDwa(), "wpgs1");
        Assert.assertEquals(iatClient.getPd(), "game");
        Assert.assertEquals(iatClient.getPtt(), 0);
        Assert.assertEquals(iatClient.getRlang(), "zh-hk");
        Assert.assertEquals(iatClient.getFormat(), "audio/L16;rate=8000");
        Assert.assertEquals(iatClient.getEncoding(), "speex");
        Assert.assertEquals(iatClient.getVinfo(), 1);
        Assert.assertEquals(iatClient.getNunum(), 0);
        Assert.assertTrue(iatClient.getSpeex_size() == 70);
        Assert.assertTrue(iatClient.getNbest() == 3);
        Assert.assertTrue(iatClient.getWbest() == 5);
        Assert.assertTrue(iatClient.getFrameSize() == 120);
        Assert.assertEquals(iatClient.getCallTimeout(), 2000);
        Assert.assertEquals(iatClient.getReadTimeout(), 2000);
        Assert.assertEquals(iatClient.getWriteTimeout(), 2000);
        Assert.assertEquals(iatClient.getPingInterval(), 10);
        Assert.assertFalse(iatClient.isRetryOnConnectionFailure());

    }

    @Test
    public void testSignature() throws MalformedURLException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchMethodException {
        IatClient iatClient = new IatClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();
        iatClient.send(null, null, new AbstractIatWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IatResponse iatResponse) {
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            }
        });

        Assert.assertTrue(iatClient.getRequest().url().toString().contains("iat-api.xfyun.cn/v2/iat"));

        AbstractSignature signature = iatClient.getSignature();
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

        Assert.assertEquals(sha, iatClient.getSignature().getSigna());

        // 手动生成
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 工具生成
        String auth = AuthUtil.generateAuthorization(signature, "hmac-sha256");

        Assert.assertEquals(authorization, auth);
    }

    @Test
    public void testErrorSignature() throws MalformedURLException, SignatureException, FileNotFoundException {
        IatClient iatClient = new IatClient.Builder()
                .signature("123456", apiKey, apiSecret)
                .build();
        File file = new File(resourcePath + filePath);
        iatClient.send(file, new AbstractIatWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IatResponse iatResponse) {
                Assert.assertNotNull(iatResponse);
                Assert.assertNotNull(iatResponse.getMessage());

                Assert.assertNotEquals(iatResponse.getCode(), 101);
                Assert.assertEquals(iatResponse.getCode(), 10313);
                iatClient.closeWebsocket();
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
        IatClient iatClient = new IatClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        File file = new File(resourcePath + filePath);
        iatClient.send(file, new AbstractIatWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IatResponse iatResponse) {
                Assert.assertNotNull(iatResponse);
                Assert.assertNotNull(iatResponse.getMessage());

                IatResult.Ws[] wss = iatResponse.getData().getResult().getWs();
                for (IatResult.Ws ws : wss) {
                    IatResult.Cw[] cws = ws.getCw();

                    for (IatResult.Cw cw : cws) {
                        System.out.println(cw.getW());
                    }
                }
                if (iatResponse.getCode() != 0) {
                    System.out.println("code=>" + iatResponse.getCode() + " error=>" + iatResponse.getMessage() + " sid=" + iatResponse.getSid());
                    System.out.println("错误码查询链接：https://www.xfyun.cn/document/error-code");
                    return;
                }

                if (iatResponse.getData() != null) {
                    if (iatResponse.getData().getStatus() == 2) {
                        // resp.data.status ==2 说明数据全部返回完毕，可以关闭连接，释放资源
                        System.out.println("session end ");
                        Date dateEnd = new Date();
                        System.out.println(sdf.format(dateBegin) + "开始");
                        System.out.println(sdf.format(dateEnd) + "结束");
                        System.out.println("耗时:" + (dateEnd.getTime() - dateBegin.getTime()) + "ms");
                        System.out.println("本次识别sid ==》" + iatResponse.getSid());
                        iatClient.closeWebsocket();
                    } else {
                        // 根据返回的数据处理
                        System.out.println(StringUtils.gson.toJson(iatResponse));
                    }
                }
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            }
        });
        Thread.sleep(15000);
    }

    @Test
    public void testSendNull() throws MalformedURLException, SignatureException, InterruptedException {
        IatClient iatClient = new IatClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        InputStream inputStream = null;
        iatClient.send(inputStream, new AbstractIatWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IatResponse iatResponse) {
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            }
        });

        byte[] bytes = null;
        iatClient.send(bytes, null, new AbstractIatWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IatResponse iatResponse) {
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            }
        });
//        Thread.sleep(5000);
    }

    @Test
    public void testSendBytes() throws IOException, InterruptedException, SignatureException {
        IatClient iatClient = new IatClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        File file = new File(resourcePath + filePath);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024000];
        int len = inputStream.read(buffer);
        AbstractIatWebSocketListener iatWebSocketListener = new AbstractIatWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IatResponse iatResponse) {
                Assert.assertNotNull(iatResponse);
                Assert.assertNotNull(iatResponse.getMessage());

                IatResult.Ws[] wss = iatResponse.getData().getResult().getWs();
                for (IatResult.Ws ws : wss) {
                    IatResult.Cw[] cws = ws.getCw();

                    for (IatResult.Cw cw : cws) {
                        System.out.println(cw.getW());
                    }
                }
                if (iatResponse.getCode() != 0) {
                    System.out.println("code=>" + iatResponse.getCode() + " error=>" + iatResponse.getMessage() + " sid=" + iatResponse.getSid());
                    System.out.println("错误码查询链接：https://www.xfyun.cn/document/error-code");
                    return;
                }

                if (iatResponse.getData() != null) {
                    if (iatResponse.getData().getStatus() == 2) {
                        // resp.data.status ==2 说明数据全部返回完毕，可以关闭连接，释放资源
                        System.out.println("session end ");
                        System.out.println("本次识别sid ==》" + iatResponse.getSid());

                        iatClient.closeWebsocket();
                    } else {
                        // 根据返回的数据处理
                        System.out.println(StringUtils.gson.toJson(iatResponse));
                    }
                }

            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            }
        };
        iatClient.send(buffer, inputStream, iatWebSocketListener);
        Thread.sleep(15000);
    }
}
