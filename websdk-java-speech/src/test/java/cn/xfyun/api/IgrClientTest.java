package cn.xfyun.api;

import cn.xfyun.model.response.igr.IgrResponseData;
import cn.xfyun.service.igr.AbstractIgrWebSocketListener;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.security.SignatureException;

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

    @Test
    public void testSuccess() throws FileNotFoundException, SignatureException, MalformedURLException {
        IgrClient igrClient = new IgrClient.Builder()
                .signature("586d6ce4", "8ceb8d92faf3277f9ab346d40e5b51c4", "NTE3NjQ3OWIwMDljM2ViZTNlZmI5YTM5").build();
        File file = new File("D:\\work\\workspace\\data\\voice\\test1.mp3");
        igrClient.send(file, new AbstractIgrWebSocketListener(){
            @Override
            public void onSuccess(WebSocket webSocket, IgrResponseData iseResponseData) {
                System.out.println("sid:" + iseResponseData.getSid());
                webSocket.close(1000, "");
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {

            }
        });
    }
}
