package cn.xfyun.api;

import config.PropertiesConfig;
import cn.xfyun.common.IgrConstant;
import cn.xfyun.model.response.igr.IgrResponseData;
import cn.xfyun.model.sign.AbstractSignature;
import cn.xfyun.service.igr.AbstractIgrWebSocketListener;
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
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * @author: <flhong2@iflytek.com>
 * @description: 性别年龄识别测试
 * @version: v1.0
 * @create: 2021-06-02 15:57
 **/
@RunWith(PowerMockRunner.class)
@PrepareForTest({IseClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class IgrClientTest {

    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getApiKey();
    private static final String apiSecret = PropertiesConfig.getApiSecret();
    String filePath = "src/test/resources/audio/cn/read_sentence_cn.pcm";

    @Test
    public void defaultParamTest() {
        IgrClient igrClient = new IgrClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        Assert.assertEquals(igrClient.getEnt(), "igr");
        Assert.assertEquals(igrClient.getApiKey(), apiKey);
        Assert.assertEquals(igrClient.getAppId(), appId);
        Assert.assertEquals(igrClient.getApiSecret(), apiSecret);

        Assert.assertEquals(igrClient.getCallTimeout(), 0);
        Assert.assertEquals(igrClient.getConnectTimeout(), 10000);
        Assert.assertEquals(igrClient.getReadTimeout(), 10000);
        Assert.assertEquals(igrClient.getWriteTimeout(), 10000);
        Assert.assertEquals(igrClient.getPingInterval(), 0);
        Assert.assertTrue(igrClient.isRetryOnConnectionFailure());

        Assert.assertNotNull(igrClient.getClient());
    }

    @Test
    public void testParamBuild() {
        IgrClient igrClient = new IgrClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .ent("igr").aue("raw").rate(8000)
                .frameSize(40)
                .callTimeout(2000, TimeUnit.MILLISECONDS)
                .connectTimeout(2000, TimeUnit.MILLISECONDS)
                .readTimeout(2000, TimeUnit.MILLISECONDS)
                .writeTimeout(2000, TimeUnit.MILLISECONDS)
                .pingInterval(10, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(false)
                .build();

        Assert.assertEquals(igrClient.getApiKey(), apiKey);
        Assert.assertEquals(igrClient.getAppId(), appId);
        Assert.assertEquals(igrClient.getApiSecret(), apiSecret);
        Assert.assertEquals(igrClient.getEnt(), "igr");
        Assert.assertEquals(igrClient.getAue(), "raw");
        Assert.assertEquals(igrClient.getRate(), 8000);

        Assert.assertTrue(igrClient.getFrameSize() == 40);
        Assert.assertEquals(igrClient.getCallTimeout(), 2000);
        Assert.assertEquals(igrClient.getConnectTimeout(), 2000);
        Assert.assertEquals(igrClient.getReadTimeout(), 2000);
        Assert.assertEquals(igrClient.getWriteTimeout(), 2000);
        Assert.assertEquals(igrClient.getPingInterval(), 10);
        Assert.assertFalse(igrClient.isRetryOnConnectionFailure());

    }

    @Test
    public void testSignature() throws MalformedURLException, SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        IgrClient iseClient = new IgrClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        AbstractSignature signature = iseClient.getSignature();
        Assert.assertNotNull(signature);
        Assert.assertEquals(signature.getId(), apiKey);
        Assert.assertEquals(signature.getKey(), apiSecret);

        URL url = new URL(IgrConstant.HOST_URL);
        String ts = signature.getTs();
        StringBuilder builder = new StringBuilder("host: ").append(url.getHost()).append("\n").//
                append("date: ").append(ts).append("\n").//
                append("GET ").append(url.getPath()).append(" HTTP/1.1");
        Charset charset = StandardCharsets.UTF_8;
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
        System.out.println("auth" + auth);
        System.out.println("authorization" + authorization);
        Assert.assertEquals(authorization, auth);
    }

    @Test
    public void testErrorSignature() throws FileNotFoundException, SignatureException, MalformedURLException, InterruptedException {
        IgrClient igrClient = new IgrClient.Builder()
                .signature("123456", apiKey, apiSecret)
                .build();
        File file = new File(filePath);
        igrClient.send(file, new AbstractIgrWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IgrResponseData igrResponseData) {

            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                System.out.println("授权失败了!");
            }
        });
        Thread.sleep(50000);
    }

    @Test
    public void testSuccessByFile() throws FileNotFoundException, SignatureException, MalformedURLException, InterruptedException {
        IgrClient igrClient = new IgrClient.Builder()
                .signature(appId, apiKey, apiSecret).ent("igr").aue("raw").rate(8000)
                .build();
        File file = new File(filePath);
        igrClient.send(file, new AbstractIgrWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IgrResponseData igrResponseData) {
                System.out.println("sid:" + igrResponseData.getSid());
                System.out.println("识别结果为: " + igrResponseData.getData());
                webSocket.close(1000, "");
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {

            }
        });
        Thread.sleep(50000);
    }

    @Test
    public void testSuccessByStream() throws FileNotFoundException, SignatureException, MalformedURLException, InterruptedException {
        IgrClient igrClient = new IgrClient.Builder()
                .signature(appId, apiKey, apiSecret).ent("igr").aue("raw").rate(8000)
                .build();
        File file = new File(filePath);
        igrClient.send(new FileInputStream(file), new AbstractIgrWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IgrResponseData igrResponseData) {
                System.out.println("sid:" + igrResponseData.getSid());
                System.out.println("识别结果为: " + igrResponseData.getData());
                webSocket.close(1000, "");
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {

            }
        });
        Thread.sleep(10000);
    }

    @Test
    public void testSuccessByString() throws FileNotFoundException, SignatureException, MalformedURLException, InterruptedException {
        IgrClient igrClient = new IgrClient.Builder()
                .signature(appId, apiKey, apiSecret).ent("igr").aue("raw").rate(8000)
                .build();
        InputStream inputStream = new FileInputStream(filePath);
        String datas = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        try {
            while ((i = inputStream.read()) != -1) {
                baos.write(i);
            }
            datas = baos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        igrClient.send(datas, new AbstractIgrWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IgrResponseData igrResponseData) {
                System.out.println("sid:" + igrResponseData.getSid());
                System.out.println("识别结果为: " + igrResponseData.getData());
                webSocket.close(1000, "");
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {

            }
        });
        Thread.sleep(10000);
    }

    @Test
    public void testSuccessByBytes() throws IOException, InterruptedException, SignatureException {
        IgrClient igrClient = new IgrClient.Builder()
                .signature(appId, apiKey, apiSecret).ent("igr").aue("raw").rate(8000)
                .build();
        InputStream inputStream = new FileInputStream(filePath);
        byte[] buffer = new byte[102400];
        int len = inputStream.read(buffer);
        System.out.println("len:" + len);

        igrClient.send(buffer, null, new AbstractIgrWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IgrResponseData igrResponseData) {
                System.out.println("sid:" + igrResponseData.getSid());
                System.out.println("识别结果为: " + igrResponseData.getData());
                webSocket.close(1000, "");
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {

            }
        });
        Thread.sleep(10000);
    }

    @Test
    public void testSendNull() throws MalformedURLException, SignatureException, InterruptedException {
        IgrClient igrClient = new IgrClient.Builder()
                .signature(appId, apiKey, apiSecret).ent("igr").aue("raw").rate(8000)
                .build();
        FileInputStream stream = null;
        igrClient.send(stream, new AbstractIgrWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IgrResponseData igrResponseData) {
                System.out.println("sid:" + igrResponseData.getSid());
                System.out.println("识别结果为: " + igrResponseData.getData());
                webSocket.close(1000, "");
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {

            }
        });
        Thread.sleep(10000);
    }

}
