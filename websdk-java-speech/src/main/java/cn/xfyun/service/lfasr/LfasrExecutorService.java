package cn.xfyun.service.lfasr;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import cn.xfyun.model.response.lfasr.LfasrMessage;
import cn.xfyun.service.lfasr.task.Task;
import cn.xfyun.util.HttpConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;


/**
 * 语音转写业务服务层
 *
 * @author : iflytek
 * @date : 2021年03月15日
 */
public class LfasrExecutorService {
    private static final Logger logger = LoggerFactory.getLogger(LfasrExecutorService.class);

    private ExecutorService executor;

    private HttpConnector connector;

    private int thresholdTimeout = 60000;

    private LfasrExecutorService() {
    }

    public static LfasrExecutorService build(int coreThreads, int maxThreads, int maxConnections, int connTimeout, int soTimeout) {
        LfasrExecutorService processor = ProcessorBuilder.PROCESSOR;

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("Lfasr-pool-%d").build();
        processor.executor = new ThreadPoolExecutor(coreThreads, maxThreads, 5L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        processor.connector = HttpConnector.build(maxConnections, connTimeout, soTimeout, 3);

        processor.thresholdTimeout = connTimeout + soTimeout + 10000;
        return processor;
    }

    public LfasrMessage exec(Task task) {
        task.setConnector(this.connector);
        Future<LfasrMessage> future = this.executor.submit(task);
        return getFuture(future, task);
    }

    private LfasrMessage getFuture(Future<LfasrMessage> future, Task task) {
        LfasrMessage message;
        try {
            message = future.get(this.thresholdTimeout, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            logger.warn(task.getIntro() + ", " + e.getMessage());
            message = new LfasrMessage("服务调用异常");
        } catch (TimeoutException e) {
            logger.warn(task.getIntro() + ", " + e.getMessage());
            message = new LfasrMessage("连接超时! 请检查您的网络");
        } catch (InterruptedException e) {
            logger.warn(task.getIntro() + ", " + e.getMessage());
            message = new LfasrMessage("服务调用异常");
            Thread.currentThread().interrupt();
        }
        return message;
    }

    private static class ProcessorBuilder {
        private static final LfasrExecutorService PROCESSOR = new LfasrExecutorService();
    }
}
