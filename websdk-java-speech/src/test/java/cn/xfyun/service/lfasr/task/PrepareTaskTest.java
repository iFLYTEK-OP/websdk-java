package cn.xfyun.service.lfasr.task;

import com.google.gson.Gson;
import cn.xfyun.exception.HttpException;
import cn.xfyun.model.response.lfasr.LfasrMessage;
import cn.xfyun.model.sign.LfasrSignature;
import cn.xfyun.util.HttpConnector;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import java.io.IOException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * 文件上传准备单元测试
 *
 * @author : jun
 * @date : 2021年03月31日
 */
public class PrepareTaskTest {

    @Test
    public void callTest() throws SignatureException, IOException, HttpException {
        LfasrSignature signature = PowerMockito.mock(LfasrSignature.class);
        HttpConnector connector = PowerMockito.mock(HttpConnector.class);

        LfasrMessage message = new LfasrMessage();
        message.setOk(-1);
        Gson gson = new Gson();
        PowerMockito.when(connector.post(Mockito.eq("https://raasr.xfyun.cn/api/prepare"), Mockito.anyMap())).thenReturn(gson.toJson(message));

        String taskId = "2452435";
        Map<String, String> param = new HashMap<>();
        PrepareTask prepareTask = new PrepareTask(signature, param);
        Whitebox.setInternalState(prepareTask, "connector", connector);

        LfasrMessage returnMessage = prepareTask.call();
        assertEquals(1, returnMessage.getOk());
    }

    @Test
    public void callTest_HttpException() throws SignatureException, IOException, HttpException {
        LfasrSignature signature = PowerMockito.mock(LfasrSignature.class);
        HttpConnector connector = PowerMockito.mock(HttpConnector.class);

        LfasrMessage message = new LfasrMessage();
        message.setOk(1);
        PowerMockito.when(connector.post(Mockito.eq("https://raasr.xfyun.cn/api/prepare"), Mockito.anyMap())).thenThrow(HttpException.class);

        String taskId = "2452435";
        Map<String, String> param = new HashMap<>();
        PrepareTask prepareTask = new PrepareTask(signature, param);
        Whitebox.setInternalState(prepareTask, "connector", connector);

        LfasrMessage returnMessage = prepareTask.call();
        assertEquals(1, returnMessage.getOk());
    }

    @Test
    public void callTest_IOException() throws SignatureException, IOException, HttpException {
        LfasrSignature signature = PowerMockito.mock(LfasrSignature.class);
        HttpConnector connector = PowerMockito.mock(HttpConnector.class);

        LfasrMessage message = new LfasrMessage();
        message.setOk(1);
        PowerMockito.when(connector.post(Mockito.eq("https://raasr.xfyun.cn/api/prepare"), Mockito.anyMap())).thenThrow(IOException.class);

        String taskId = "2452435";
        Map<String, String> param = new HashMap<>();
        PrepareTask prepareTask = new PrepareTask(signature, param);
        Whitebox.setInternalState(prepareTask, "connector", connector);

        LfasrMessage returnMessage = prepareTask.call();
        assertEquals(1, returnMessage.getOk());
    }
}
