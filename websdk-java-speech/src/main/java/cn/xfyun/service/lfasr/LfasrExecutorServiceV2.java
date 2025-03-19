package cn.xfyun.service.lfasr;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import cn.xfyun.model.response.lfasr.LfasrMessage;
import cn.xfyun.model.response.lfasr.LfasrResponse;
import cn.xfyun.service.lfasr.task.Task;
import cn.xfyun.service.lfasr.task.TaskV2;
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
public class LfasrExecutorServiceV2 {
    private static final Logger logger = LoggerFactory.getLogger(LfasrExecutorServiceV2.class);

    private ExecutorService executor;

    private HttpConnector connector;

    private int thresholdTimeout = 60000;

    private LfasrExecutorServiceV2() {
    }

    public static LfasrExecutorServiceV2 build(int coreThreads, int maxThreads, int maxConnections, int connTimeout, int soTimeout) {
        LfasrExecutorServiceV2 processor = ProcessorBuilder.PROCESSOR;

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("Lfasr-pool-%d").build();
        processor.executor = new ThreadPoolExecutor(coreThreads, maxThreads, 5L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        processor.connector = HttpConnector.build(maxConnections, connTimeout, soTimeout, 3);

        processor.thresholdTimeout = connTimeout + soTimeout + 10000;
        return processor;
    }

    public LfasrResponse exec(TaskV2 task) {
        task.setConnector(this.connector);
        Future<LfasrResponse> future = this.executor.submit(task);
        return getFuture(future, task);
    }

    private LfasrResponse getFuture(Future<LfasrResponse> future, TaskV2 task) {
        LfasrResponse response;
        try {
            response = future.get(this.thresholdTimeout, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            logger.warn(task.getIntro() + ", " + e.getMessage());
            response = LfasrResponse.error("服务调用异常");
        } catch (TimeoutException e) {
            logger.warn(task.getIntro() + ", " + e.getMessage());
            response = LfasrResponse.error("连接超时! 请检查您的网络");
        } catch (InterruptedException e) {
            logger.warn(task.getIntro() + ", " + e.getMessage());
            response = LfasrResponse.error("服务调用异常");
            Thread.currentThread().interrupt();
        }
        return response;
    }

    private static class ProcessorBuilder {
        private static final LfasrExecutorServiceV2 PROCESSOR = new LfasrExecutorServiceV2();
    }
}
