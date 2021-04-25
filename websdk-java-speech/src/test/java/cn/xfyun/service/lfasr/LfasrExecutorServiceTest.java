package cn.xfyun.service.lfasr;

import cn.xfyun.model.response.lfasr.LfasrMessage;
import cn.xfyun.service.lfasr.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * TODO
 *
 * @author : jun
 * @date : 2021年03月31日
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LfasrExecutorService.class})
@PowerMockIgnore("cn.xfyun.util.HttpConnector")
public class LfasrExecutorServiceTest {

    @Test
    public void execTest() throws Exception {
        LfasrExecutorService lfasrExecutorService = LfasrExecutorService.build(10, 30, 1000, 300, 22);
        Task task = PowerMockito.mock(Task.class);
        LfasrMessage message = new LfasrMessage();
        message.setOk(1);

        Future<LfasrMessage> future = PowerMockito.mock(Future.class);
        PowerMockito.when(future.get(10322, TimeUnit.MILLISECONDS)).thenReturn(message);
        lfasrExecutorService.exec(task);
        assertEquals(1, message.getOk());
    }


    @Test
    public void getFutureTest() throws Exception {
        LfasrExecutorService lfasrExecutorService = LfasrExecutorService.build(10, 30, 1000, 300, 22);
        LfasrMessage message = new LfasrMessage();
        message.setOk(1);

        Future<LfasrMessage> future = PowerMockito.mock(Future.class);
        PowerMockito.when(future.get(10322, TimeUnit.MILLISECONDS)).thenReturn(message);

        message = Whitebox.invokeMethod(lfasrExecutorService, "getFuture", future, Mockito.mock(Task.class));
        assertEquals(1, message.getOk());
    }

    @Test
    public void getFutureTest_ExecutionException() throws Exception {
        LfasrExecutorService lfasrExecutorService = LfasrExecutorService.build(10, 30, 1000, 300, 22);

        Future<LfasrMessage> future = PowerMockito.mock(Future.class);
        PowerMockito.when(future.get(10322, TimeUnit.MILLISECONDS)).thenThrow(ExecutionException.class);

        LfasrMessage message = Whitebox.invokeMethod(lfasrExecutorService, "getFuture", future, Mockito.mock(Task.class));
        assertTrue(message.getFailed().contains("服务调用异常"));
    }

    @Test
    public void getFutureTest_TimeoutException() throws Exception {
        LfasrExecutorService lfasrExecutorService = LfasrExecutorService.build(10, 30, 1000, 300, 22);

        Future<LfasrMessage> future = PowerMockito.mock(Future.class);
        PowerMockito.when(future.get(10322, TimeUnit.MILLISECONDS)).thenThrow(TimeoutException.class);

        LfasrMessage message = Whitebox.invokeMethod(lfasrExecutorService, "getFuture", future, Mockito.mock(Task.class));
        assertTrue(message.getFailed().contains("连接超时"));
    }

    @Test
    public void getFutureTest_InterruptedException() throws Exception {
        LfasrExecutorService lfasrExecutorService = LfasrExecutorService.build(10, 30, 1000, 300, 22);

        Future<LfasrMessage> future = PowerMockito.mock(Future.class);
        PowerMockito.when(future.get(10322, TimeUnit.MILLISECONDS)).thenThrow(InterruptedException.class);

        LfasrMessage message = Whitebox.invokeMethod(lfasrExecutorService, "getFuture", future, Mockito.mock(Task.class));
        assertTrue(message.getFailed().contains("服务调用异常"));
    }
}
