package cn.xfyun;

import cn.xfyun.api.TtsClient;
import cn.xfyun.model.response.TtsResponse;
import cn.xfyun.service.tts.AbstractTtsWebSocketListener;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TtsClient.class})
@PowerMockIgnore({"cn.xfyun.util.HttpConnector", "javax.crypto.*", "javax.net.ssl.*"})
public class TtsClientTest {

    private static final String appId;
    private static final String apiKey;
    private static final String apiSecret;

    /**
     * 从配置文件中获得所需属性
     */
    static {
        Properties properties = new Properties();
        try {
            //替换成自己的配置文件
            properties.load(new FileInputStream("src/test/resources/app-test.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        appId = properties.getProperty("appId");
        apiKey = properties.getProperty("apiKey");
        apiSecret = properties.getProperty("apiSecret");
    }

    Properties prop = new Properties();

    @Test
    public void defaultParamTest() throws MalformedURLException, SignatureException {
        TtsClient ttsClient = new TtsClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        Assert.assertEquals(ttsClient.getAppId(), appId);
        Assert.assertEquals(ttsClient.getApiKey(), apiKey);
        Assert.assertEquals(ttsClient.getApiSecret(), apiSecret);
        Assert.assertEquals(ttsClient.getOriginHostUrl(), "https://tts-api.xfyun.cn/v2/tts");
        Assert.assertEquals(ttsClient.getAue(), "lame");
        Assert.assertEquals(ttsClient.getSfl().longValue(), 1);
        Assert.assertEquals(ttsClient.getAuf(), "audio/L16;rate=16000");
        Assert.assertEquals(ttsClient.getVcn(), "xiaoyan");
        Assert.assertEquals(ttsClient.getSpeed().longValue(), 50);
        Assert.assertEquals(ttsClient.getVolume().longValue(), 50);
        Assert.assertEquals(ttsClient.getPitch().longValue(), 50);
        Assert.assertEquals(ttsClient.getBgs().longValue(), 0);
        Assert.assertEquals(ttsClient.getTte(), "UTF8");
        Assert.assertEquals(ttsClient.getReg(), "0");
        Assert.assertEquals(ttsClient.getRdn(), "0");
        Assert.assertNotNull(ttsClient.getClient());
    }


    @Test
    public void test() throws MalformedURLException, SignatureException, FileNotFoundException, UnsupportedEncodingException {
        //正常流程
        TtsClient ttsClient1 = new TtsClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .aue("lame").sfl(1).auf("audio/L16;rate=16000").vcn("xiaoyan").speed(50).volume(50).pitch(50).bgs(0)
                .tte("UTF8").reg("0").rdn("0")
                .build();
        //测试小语种
        TtsClient ttsClient2 = new TtsClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .hostUrl("https://tts-api.xfyun.cn/v2/tts")
                .vcn("x2_christiane").tte("UNICODE")
                .build();
        //测试除了mp3以外的格式
        TtsClient ttsClient3 = new TtsClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .aue("raw").sfl(1)
                .build();
        //测试保存到相应文件的监听器构造方法
        TtsClient ttsClient4 = new TtsClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();
        //测试请求参数非法的情况
        TtsClient ttsClient5 = new TtsClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .aue("123")
                .build();
        // 存放音频的文件
        File f = new File("src/test/resources/audio/20210329145025829.mp3");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //测试鉴权失败的情况
        TtsClient ttsClient6 = new TtsClient.Builder()
                .signature(appId, "123", apiSecret)
                .build();
        ttsClient1.send("检查签名的各个参数是否有缺失是否正确，特别确认下复制的api_key是否正确", new AbstractTtsWebSocketListener() {
            @Override
            public void onSuccess(byte[] bytes) {
                assertNotNull(bytes);
            }

            /**
             * websocket返回失败时，需要用户重写的方法
             *
             * @param webSocket
             * @param t
             * @param response
             */
            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                assertNull(t);
            }

            /**
             * 发生业务失败的情况，需要用户重写的方法
             *
             * @param webSocket
             * @param response
             */
            @Override
            public void onBusinessFail(WebSocket webSocket, TtsResponse response) {
                assertNull(response);
            }
        });
        ttsClient2.send("Überprüfen Sie, ob jeder Parameter der Signatur fehlt und korrekt ist, insbesondere um zu bestätigen, ob der kopierte api_key korrekt ist", new AbstractTtsWebSocketListener() {
            @Override
            public void onSuccess(byte[] bytes) {
                assertNotNull(bytes);
            }

            /**
             * websocket返回失败时，需要用户重写的方法
             *
             * @param webSocket
             * @param t
             * @param response
             */
            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                assertNull(t);
            }

            /**
             * 发生业务失败的情况，需要用户重写的方法
             *
             * @param webSocket
             * @param response
             */
            @Override
            public void onBusinessFail(WebSocket webSocket, TtsResponse response) {
                assertNull(response);
            }
        });
        ttsClient3.send("检查签名的各个参数是否有缺失是否正确，特别确认下复制的api_key是否正确", new AbstractTtsWebSocketListener() {
            @Override
            public void onSuccess(byte[] bytes) {
                assertNotNull(bytes);
            }

            /**
             * websocket返回失败时，需要用户重写的方法
             *
             * @param webSocket
             * @param t
             * @param response
             */
            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                assertNull(t);
            }

            /**
             * 发生业务失败的情况，需要用户重写的方法
             *
             * @param webSocket
             * @param response
             */
            @Override
            public void onBusinessFail(WebSocket webSocket, TtsResponse response) {
                assertNull(response);
            }
        });
        ttsClient4.send("检查签名的各个参数是否有缺失是否正确，特别确认下复制的api_key是否正确", new AbstractTtsWebSocketListener(f) {
            @Override
            public void onSuccess(byte[] bytes) {
                assertNotNull(bytes);
                assertNotNull(f);
            }

            /**
             * websocket返回失败时，需要用户重写的方法
             *
             * @param webSocket
             * @param t
             * @param response
             */
            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                assertNull(t);
            }

            /**
             * 发生业务失败的情况，需要用户重写的方法
             *
             * @param webSocket
             * @param response
             */
            @Override
            public void onBusinessFail(WebSocket webSocket, TtsResponse response) {
                assertNull(response);
            }
        });
        ttsClient5.send("检查签名的各个参数是否有缺失是否正确，特别确认下复制的api_key是否正确", new AbstractTtsWebSocketListener() {
            @Override
            public void onSuccess(byte[] bytes) {
                assertNotNull(bytes);
            }

            /**
             * websocket返回失败时，需要用户重写的方法
             *
             * @param webSocket
             * @param t
             * @param response
             */
            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                assertNull(t);
            }

            /**
             * 发生业务失败的情况，需要用户重写的方法
             *
             * @param webSocket
             * @param response
             */
            @Override
            public void onBusinessFail(WebSocket webSocket, TtsResponse response) {
                assertNotNull(response);
            }
        });
        ttsClient6.send("检查签名的各个参数是否有缺失是否正确，特别确认下复制的api_key是否正确", new AbstractTtsWebSocketListener() {
            @Override
            public void onSuccess(byte[] bytes) {
                assertNotNull(bytes);
            }

            /**
             * websocket返回失败时，需要用户重写的方法
             *
             * @param webSocket
             * @param t
             * @param response
             */
            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                assertNotNull(t);
            }

            /**
             * 发生业务失败的情况，需要用户重写的方法
             *
             * @param webSocket
             * @param response
             */
            @Override
            public void onBusinessFail(WebSocket webSocket, TtsResponse response) {
                assertNull(response);
            }
        });
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}