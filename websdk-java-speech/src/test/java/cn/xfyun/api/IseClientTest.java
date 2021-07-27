package cn.xfyun.api;

import config.PropertiesConfig;
import cn.xfyun.common.IseConstant;
import cn.xfyun.model.response.ise.*;
import cn.xfyun.service.ise.AbstractIseWebSocketListener;
import com.google.gson.JsonObject;
import cn.xfyun.model.sign.AbstractSignature;
import cn.xfyun.model.sign.Hmac256Signature;
import cn.xfyun.util.AuthUtil;
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
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * @author: flhong2
 * @description: 语音评测测试
 * @create: 2021-03-23 11:23
 **/
@RunWith(PowerMockRunner.class)
@PrepareForTest({IseClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class IseClientTest {

    final static Base64.Encoder encoder = Base64.getEncoder();//编码
    final static Base64.Decoder decoder = Base64.getDecoder();//解码
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getIseClientApiKey();
    private static final String apiSecret = PropertiesConfig.getIseClientApiSecret();
    String filePath = "src/test/resources/audio/cn/read_sentence_cn.mp3";

    @Test
    public void defaultParamTest() {
        IseClient iseClient = new IseClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        Assert.assertEquals(iseClient.getAppId(), appId);
        Assert.assertEquals(iseClient.getApiKey(), apiKey);
        Assert.assertEquals(iseClient.getAppId(), appId);
        Assert.assertEquals(iseClient.getApiSecret(), apiSecret);

        Assert.assertTrue(iseClient.getHostUrl().contains("ise-api.xfyun.cn/v2/open-ise"));
        Assert.assertEquals(iseClient.getAue(), "raw");
        Assert.assertEquals(iseClient.getAuf(), "audio/L16;rate=16000");
        Assert.assertEquals(iseClient.getRstcd(), "gbk");
        Assert.assertEquals(iseClient.getRst(), "entirety");
        Assert.assertEquals(iseClient.getIseUnite(), "0");
        Assert.assertEquals(iseClient.getPlev(), "0");

        Assert.assertEquals(iseClient.getCallTimeout(), 0);
        Assert.assertEquals(iseClient.getConnectTimeout(), 10000);
        Assert.assertEquals(iseClient.getReadTimeout(), 10000);
        Assert.assertEquals(iseClient.getWriteTimeout(), 10000);
        Assert.assertEquals(iseClient.getPingInterval(), 0);
        Assert.assertTrue(iseClient.isRetryOnConnectionFailure());

        Assert.assertNotNull(iseClient.getClient());
    }

    @Test
    public void testParamBuild() {
        IseClient iseClient = new IseClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .hostUrl("http://ise-api.xfyun.cn/v2/open-ise")
                .addSub("ise")
                .addEnt("cn_vip")
                .addCategory("read_sentence")
                .addAus(1)
                .addCmd("ssb")
                .addText('\uFEFF' + "今天天气怎么样")
                .addTte("utf-8")
                .addTtpSkip(true)
                .addExtraAbility("multi_dimension")
                .addAuf("audio/L16;rate=8000")
                .addAue("raw")
                .addRstcd("utf8")
                .addGroup("adult")
                .addCheckType("common")
                .addGrade("middle")
                .addRst("entirety")
                .addIseUnite("0")
                .addPlev("0")
                .addAppId(appId)
                .addAppKey(apiKey)
                .addAppSecret(apiSecret)
                .addBusiness(new JsonObject())
                .frameSize(40)
                .addSignature(new Hmac256Signature(apiKey, apiSecret, IseConstant.HOST_URL))
                .callTimeout(2000, TimeUnit.MILLISECONDS)
                .connectTimeout(2000, TimeUnit.MILLISECONDS)
                .readTimeout(2000, TimeUnit.MILLISECONDS)
                .writeTimeout(2000, TimeUnit.MILLISECONDS)
                .pingInterval(10, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(false)
                .build();

        Assert.assertTrue(iseClient.getHostUrl().contains("ise-api.xfyun.cn/v2/open-ise"));
        Assert.assertEquals(iseClient.getSub(), "ise");
        Assert.assertEquals(iseClient.getEnt(), "cn_vip");
        Assert.assertEquals(iseClient.getCategory(), "read_sentence");
        Assert.assertEquals(iseClient.getAus(), 1);
        Assert.assertEquals(iseClient.getCmd(), "ssb");
        Assert.assertEquals(iseClient.getText(), '\uFEFF' + "今天天气怎么样");
        Assert.assertEquals(iseClient.getTte(), "utf-8");
        Assert.assertTrue(iseClient.isTtpSkip() == true);
        Assert.assertEquals(iseClient.getExtraAbility(), "multi_dimension");
        Assert.assertEquals(iseClient.getAuf(), "audio/L16;rate=8000");
        Assert.assertEquals(iseClient.getAue(), "raw");
        Assert.assertEquals(iseClient.getRstcd(), "utf8");
        Assert.assertEquals(iseClient.getGroup(), "adult");
        Assert.assertEquals(iseClient.getCheckType(), "common");
        Assert.assertEquals(iseClient.getGrade(), "middle");
        Assert.assertEquals(iseClient.getRst(), "entirety");
        Assert.assertEquals(iseClient.getIseUnite(), "0");
        Assert.assertEquals(iseClient.getPlev(), "0");
        Assert.assertEquals(iseClient.getAppId(), appId);
        Assert.assertEquals(iseClient.getApiSecret(), apiSecret);
        Assert.assertEquals(iseClient.getApiKey(), apiKey);
        Assert.assertTrue(iseClient.getFrameSize() == 40);
        Assert.assertEquals(iseClient.getCallTimeout(), 2000);
        Assert.assertEquals(iseClient.getReadTimeout(), 2000);
        Assert.assertEquals(iseClient.getWriteTimeout(), 2000);
        Assert.assertEquals(iseClient.getPingInterval(), 10);
        Assert.assertFalse(iseClient.isRetryOnConnectionFailure());
    }

    @Test
    public void testSignature() throws MalformedURLException, SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        IseClient iseClient = new IseClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        AbstractSignature signature = iseClient.getSignature();
        Assert.assertNotNull(signature);
        Assert.assertEquals(signature.getId(), apiKey);
        Assert.assertEquals(signature.getKey(), apiSecret);

        URL url = new URL(IseConstant.HOST_URL);
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

        Assert.assertEquals(sha, iseClient.getSignature().getSigna());

        // 手动生成
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 工具生成
        String auth = AuthUtil.generateAuthorization(signature, "hmac-sha256");

        Assert.assertEquals(authorization, auth);
    }

    @Test
    public void testErrorSignature() throws FileNotFoundException, SignatureException, MalformedURLException, InterruptedException {
        IseClient iseClient = new IseClient.Builder()
                .signature("123456", apiKey, apiSecret)
                .build();
        File file = new File(filePath);
        iseClient.send(file, new AbstractIseWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IseResponseData iseResponseData) {
                Assert.assertNotNull(iseResponseData);
                Assert.assertNotNull(iseResponseData.getMessage());

                Assert.assertNotEquals(iseResponseData.getCode(), 200);
                iseClient.closeWebsocket();
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            }
        });
        Thread.sleep(10000);
    }

    @Test
    public void testSuccess() throws Exception {
        IseClient iseClient = new IseClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .addSub("ise")
                .addEnt("cn_vip")
                .addCategory("read_sentence")
                .addText('\uFEFF' + "今天天气怎么样")
                .addTte("utf-8")
                .addRstcd("utf8")
                .build();

        File file = new File(filePath);
        iseClient.send(file, new AbstractIseWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IseResponseData iseResponseData) {

                try {
                    System.out.println("sid:" + iseResponseData.getSid() + " 最终识别结果" + new String(decoder.decode(iseResponseData.getData().getData()), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                webSocket.close(1000, "");
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                System.out.println(response);
            }
        });
        Thread.sleep(10000);
    }

    @Test
    public void testSendNull() throws MalformedURLException, SignatureException, InterruptedException {
        IseClient iseClient = new IseClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .addSub("ise")
                .addEnt("cn_vip")
                .addCategory("read_sentence")
                .addText('\uFEFF' + "今天天气怎么样")
                .addTte("utf-8")
                .build();

        InputStream inputStream = null;
        iseClient.send(inputStream, new AbstractIseWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IseResponseData iseResponseData) {
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            }
        });

        byte[] bytes = null;
        iseClient.send(bytes, null, new AbstractIseWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IseResponseData iseResponseData) {
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            }
        });
//        Thread.sleep(30000);
    }

    @Test
    public void testSendBytes() throws IOException, InterruptedException, SignatureException {
        IseClient iseClient = new IseClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .addSub("ise")
                .addEnt("cn_vip")
                .addCategory("read_sentence")
                .addText('\uFEFF' + "今天天气怎么样")
                .addTte("utf-8")
                .build();

        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024000];
        int len = inputStream.read(buffer);
        System.out.println("len:" + len);
        AbstractIseWebSocketListener iseWebSocketListener = new AbstractIseWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IseResponseData iseResponseData) {
                Assert.assertNotNull(iseResponseData);
                Assert.assertNotNull(iseResponseData.getMessage());

                if (iseResponseData.getData() != null) {
                    if (iseResponseData.getData().getStatus() == 2) {
                        System.out.println("session end ");
                        System.out.println("本次识别sid ==》" + iseResponseData.getSid());

                        iseClient.closeWebsocket();
                    }
                }

            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            }
        };
        iseClient.send(buffer, inputStream, iseWebSocketListener);
        Thread.sleep(15000);
    }

}
