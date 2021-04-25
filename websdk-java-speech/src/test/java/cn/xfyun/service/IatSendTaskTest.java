package cn.xfyun.service;

import cn.xfyun.service.iat.IatSendTask;
import cn.xfyun.api.IatClient;
import org.junit.Test;

import java.net.MalformedURLException;
import java.security.SignatureException;

/**
 * @author <ydwang16@iflytek.com>
 * @description
 * @date 2021/3/31
 */
public class IatSendTaskTest {
    private String appId = "";
    private String apiKey = "";
    private String secretKey = "";

    @Test
    public void testSendTask() throws MalformedURLException, SignatureException {
        IatClient iatClient = new IatClient.Builder()
                .signature(appId, apiKey, secretKey)
                .build();
        IatSendTask iatSendTask = new IatSendTask();
        new IatSendTask.Builder()
                .webSocketClient(iatClient)
                .build(iatSendTask);

        String content = "test";
        byte[] bytes = content.getBytes();

        // 第一帧
        String result = iatSendTask.businessDataProcess(bytes, false);
        System.out.println(result);

        // 中间帧
        result = iatSendTask.businessDataProcess(bytes, false);
        System.out.println(result);

        //最后一帧
        result = iatSendTask.businessDataProcess(bytes, true);
        System.out.println(result);

    }
}
