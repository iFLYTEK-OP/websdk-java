package cn.xfyun.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.model.response.lfasr.LfasrMessage;
import org.junit.Test;

import java.security.SignatureException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 语音转写client单元测试
 * 调用真实HTTP接口
 *
 * @author : jun
 * @date : 2021年03月29日
 */
public class LfasrClientHttpTest {

    private static final String appId = PropertiesConfig.getLfasrAppId();
    private static final String secretKey = PropertiesConfig.getSecretKey();
    private static final String AUDIO_FILE_PATH = LfasrClientHttpTest.class.getResource("/").getPath() + "/audio/lfasr.wav";

    @Test
    public void clientHttpTest() throws SignatureException, InterruptedException {
        //1、创建客户端实例
        LfasrClient lfasrClient = new LfasrClient.Builder(appId, secretKey).slice(102400).build();

        //2、上传
        LfasrMessage task = lfasrClient.upload(AUDIO_FILE_PATH);
        String taskId = task.getData();
        System.out.println("转写任务 taskId：" + taskId);
        assertNotNull(taskId);

        //3、查看转写进度
        String status = "0";
        while (!"9".equals(status)) {
            LfasrMessage message = lfasrClient.getProgress(taskId);
            assertNotNull(message.getData());
            System.out.println(message.toString());
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(message.getData(), new TypeToken<Map<String, String>>() {
            }.getType());
            status = map.get("status");
            TimeUnit.SECONDS.sleep(2);
        }
        //4、获取结果
        LfasrMessage result = lfasrClient.getResult(taskId);
        System.out.println("转写结果: \n" + result.getData());
        assertEquals(result.getOk(), 0);
    }
}
