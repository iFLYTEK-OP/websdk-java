package cn.xfyun.api;

import cn.xfyun.common.IgrConstant;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.model.response.igr.IgrResponseData;
import cn.xfyun.model.sign.AbstractSignature;
import cn.xfyun.service.igr.AbstractIgrWebSocketListener;
import cn.xfyun.util.AuthUtil;
import com.google.gson.JsonParser;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Base64;

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
        System.out.println("auth" + auth);
        System.out.println("authorization" + authorization);
        Assert.assertEquals(authorization, auth);
    }

    @Test
    public void testSuccess() throws FileNotFoundException, SignatureException, MalformedURLException, InterruptedException {
        IgrClient igrClient = new IgrClient.Builder()
                .signature(appId, apiKey, apiSecret).ent("igr").aue("raw").rate(8000)
                .build();
        File file = new File("D:\\work\\workspace\\project\\websdk-java-speech\\websdk-java-speech\\src\\test\\resources\\audio\\cn\\read_sentence_cn.pcm");
        igrClient.send(file, new AbstractIgrWebSocketListener(){
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

}
