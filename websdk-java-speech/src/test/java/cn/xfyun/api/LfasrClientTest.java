package cn.xfyun.api;

import cn.xfyun.exception.LfasrException;
import cn.xfyun.model.response.lfasr.LfasrMessage;
import cn.xfyun.service.lfasr.LfasrExecutorService;
import cn.xfyun.service.lfasr.task.MergeTask;
import cn.xfyun.service.lfasr.task.PrepareTask;
import cn.xfyun.service.lfasr.task.UploadTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.File;
import java.io.FileInputStream;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 语音转写client单元测试
 *
 * @author : jun
 * @date : 2021年03月29日
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LfasrClient.class, LfasrExecutorService.class, PrepareTask.class})
@PowerMockIgnore("cn.xfyun.util.HttpConnector")
public class LfasrClientTest {
    private String appId = "appid_1234";
    private String secretKey = "secretKey_11343";

    @Test
    public void lfasrClientTest() {
        LfasrClient.Builder builder = new LfasrClient.Builder(appId, secretKey)
                .language("cn")
                .coreThreads(10)
                .maxThreads(20)
                .role_type("a")
                .connTimeout(1000)
                .file_name("fileName")
                .has_participle("has_participle")
                .has_seperate("has_seperate")
                .lfasr_type("lfasr_type")
                .max_alternatives("max_alternatives")
                .maxConnections(100)
                .pd("pd")
                .slice(30)
                .soTimeout(40).speaker_number("3");
        LfasrClient lfasrClient = new LfasrClient(builder);

        assertEquals(appId, Whitebox.getInternalState(lfasrClient, "appId"));
        assertEquals(secretKey, Whitebox.getInternalState(lfasrClient, "secretKey"));
        assertEquals("cn", Whitebox.getInternalState(lfasrClient, "language"));
        assertEquals("a", Whitebox.getInternalState(lfasrClient, "role_type"));
        assertEquals("fileName", Whitebox.getInternalState(lfasrClient, "file_name"));
        assertEquals("has_participle", Whitebox.getInternalState(lfasrClient, "has_participle"));
        assertEquals("has_seperate", Whitebox.getInternalState(lfasrClient, "has_seperate"));
        assertEquals("lfasr_type", Whitebox.getInternalState(lfasrClient, "lfasr_type"));
        assertEquals("max_alternatives", Whitebox.getInternalState(lfasrClient, "max_alternatives"));
        assertEquals("pd", Whitebox.getInternalState(lfasrClient, "pd"));
        assertEquals(new Integer(30), Whitebox.getInternalState(lfasrClient, "slice_size"));
        assertEquals("3", Whitebox.getInternalState(lfasrClient, "speaker_number"));
    }

    @Test
    public void paramHandlerTest() throws Exception {
        File audio = PowerMockito.mock(File.class);
        PowerMockito.when(audio.getName()).thenReturn("test.mp3");
        LfasrClient lfasrClient = new LfasrClient.Builder(appId, secretKey).language("cn").coreThreads(10)
                .maxThreads(20).role_type("a").connTimeout(1000).file_name("file_name").has_participle("has_participle")
                .has_seperate("has_seperate").lfasr_type("lfasr_type").max_alternatives("max_alternatives")
                .maxConnections(100).pd("pd").slice(30).soTimeout(40).speaker_number("3").build();

        Map<String, String> param = new HashMap<>();
        Whitebox.invokeMethod(lfasrClient, "paramHandler", param, 1234L, audio);

        assertEquals("file_name", param.get("file_name"));
    }

    @Test
    public void uploadTest_26001() throws SignatureException {
        LfasrClient lfasrClient = new LfasrClient.Builder(appId, secretKey).build();
        try {
            lfasrClient.upload(null);
        } catch (LfasrException e) {
            assertTrue(e.getMessage().contains("is null"));
        }
        File audio = PowerMockito.mock(File.class);
        PowerMockito.when(audio.exists()).thenReturn(false);
        try {
            lfasrClient.upload("/file/path/a.mp3");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("not exists"));
        }
    }

    @Test
    public void uploadTest_max_length() throws Exception {
        File audio = PowerMockito.mock(File.class);
        PowerMockito.whenNew(File.class).withArguments("/file/path/a.mp3").thenReturn(audio);
        PowerMockito.when(audio.exists()).thenReturn(true);
        PowerMockito.when(audio.length()).thenReturn(524288001L);
        LfasrClient lfasrClient = new LfasrClient.Builder(appId, secretKey).build();
        try {
            lfasrClient.upload("/file/path/a.mp3");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("too large"));
        }

    }

    @Test
    public void uploadTest() throws Exception {
        LfasrClient lfasrClient = new LfasrClient.Builder(appId, secretKey).build();

        File audio = PowerMockito.mock(File.class);
        PowerMockito.whenNew(File.class).withArguments("/file/path/a.mp3").thenReturn(audio);
        PowerMockito.when(audio.exists()).thenReturn(true);
        PowerMockito.when(audio.length()).thenReturn(52428800L);

        PrepareTask prepareTask = PowerMockito.mock(PrepareTask.class);
        PowerMockito.whenNew(PrepareTask.class).withAnyArguments().thenReturn(prepareTask);

        UploadTask uploadTask = PowerMockito.mock(UploadTask.class);
        PowerMockito.whenNew(UploadTask.class).withAnyArguments().thenReturn(uploadTask);

        MergeTask mergeTask = PowerMockito.mock(MergeTask.class);
        PowerMockito.whenNew(MergeTask.class).withAnyArguments().thenReturn(mergeTask);


        LfasrMessage message = new LfasrMessage();
        String taskId = "taskId_1132412341234";
        message.setData(taskId);
        message.setOk(1);
        LfasrExecutorService lfasrExecutorService = Whitebox.getInternalState(lfasrClient, "lfasrExecutorService");
        PowerMockito.when(lfasrExecutorService.exec(prepareTask)).thenReturn(message);

        FileInputStream fileInputStream = PowerMockito.mock(FileInputStream.class);
        PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStream);
        PowerMockito.when(fileInputStream.read(Mockito.any())).thenReturn(1).thenReturn(0);

        LfasrMessage returnMsg = lfasrClient.upload("/file/path/a.mp3");

        assertEquals(1, returnMsg.getOk());
    }
}
