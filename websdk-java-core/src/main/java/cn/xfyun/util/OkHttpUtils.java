package cn.xfyun.util;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * okhttp工具类
 *
 * @author <zyding6@iflytek.com>
 */
public class OkHttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);

    private static volatile OkHttpClient client;

    static {
        getDefaultClient();
    }

    /**
     * 创建一个具有推荐默认配置的 OkHttpClient 实例。
     * 这些默认值旨在平衡性能和资源消耗，适用于大多数线上环境。
     */
    private static OkHttpClient createDefaultClient() {
        Dispatcher dispatcher = new Dispatcher();
        // 调整最大并发请求数为更保守的默认值
        dispatcher.setMaxRequests(100);
        // 单个主机最大并发数
        dispatcher.setMaxRequestsPerHost(10);

        // 连接池配置
        ConnectionPool connectionPool = new ConnectionPool(
                5, 5, TimeUnit.MINUTES);

        return new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .build();
    }

    /**
     * 获取默认的 OkHttpClient 实例。
     * 使用双重检查锁定实现懒加载和线程安全。
     * 建议用于一般场景。
     *
     * @return 默认的 OkHttpClient 实例
     */
    public static OkHttpClient getDefaultClient() {
        if (client == null) {
            synchronized (OkHttpUtils.class) {
                if (client == null) {
                    client = createDefaultClient();
                }
            }
        }
        return client;
    }

    /**
     * 允许外部提供自定义的 OkHttpClient 实例。
     * 这样 SDK 的使用者可以完全控制配置。
     *
     * @param defaultClient 自定义的 OkHttpClient 实例
     * @param closeOld      销毁老的 OkHttpClient 实例
     */
    public static void setDefaultClient(OkHttpClient defaultClient, boolean closeOld) {
        if (defaultClient == null) {
            throw new IllegalArgumentException("defaultClient must not be null");
        }

        synchronized (OkHttpUtils.class) {
            OkHttpClient oldClient = client;
            client = defaultClient;

            if (closeOld && oldClient != null && oldClient != defaultClient) {
                // 关闭连接池：清理所有连接
                oldClient.connectionPool().evictAll();

                // 关闭 Dispatcher 的线程池
                ExecutorService executorService = oldClient.dispatcher().executorService();
                executorService.shutdown();

                try {
                    // 尝试等待线程池优雅关闭（可选，避免阻塞太久）
                    if (!executorService.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                        executorService.shutdownNow(); // 强制关闭
                    }
                } catch (InterruptedException e) {
                    executorService.shutdownNow();
                    Thread.currentThread().interrupt();
                    logger.warn("Interrupted while closing old OkHttpClient dispatcher", e);
                }

                // 注意：OkHttpClient 没有像 close() 这样的方法，但以上操作已释放关键资源
                logger.debug("Old OkHttpClient instance has been closed and resources released.");
            }
        }
    }

    /**
     * 执行 GET 请求，可带请求头和查询参数。
     *
     * @param url     请求 URL
     * @param headers 请求头 (可为 null)
     * @param params  查询参数 (可为 null)
     * @return Response 对象
     * @throws IOException 网络异常
     */
    public static Response get(String url,
                               Map<String, String> headers,
                               Map<String, String> params) throws IOException {
        return request(url, "GET", null, headers, params);
    }

    /**
     * 执行同步 POST 请求
     * 注意：查询参数附加在 URL 上，requestBody 请求体。
     *
     * @param url     请求 URL
     * @param body    请求体
     * @param headers 请求头 (可为 null)
     * @param params  查询参数 (可为 null)
     * @return Response 对象
     * @throws IOException 网络异常
     */
    public static Response post(String url,
                                RequestBody body,
                                Map<String, String> headers,
                                Map<String, String> params) throws IOException {
        return request(url, "POST", body, headers, params);
    }

    /**
     * 获取请求 (通用方法)
     *
     * @param url     请求 URL
     * @param body    请求体 (对于 GET/HEAD 应为 null)
     * @param method  请求方式 get post delete put 等
     * @param headers 请求头 (可为 null)
     * @param params  查询参数 (可为 null)
     * @return Response 对象
     * @throws IOException              网络异常
     * @throws IllegalArgumentException 如果 URL 无效或 method 与 body 组合不当
     */
    public static Response request(String url,
                                   String method,
                                   RequestBody body,
                                   Map<String, String> headers,
                                   Map<String, String> params) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("URL must not be null");
        }

        HttpUrl parsedUrl = HttpUrl.parse(url);
        if (parsedUrl == null) {
            throw new IllegalArgumentException("Invalid URL format: " + url);
        }

        HttpUrl.Builder urlBuilder = parsedUrl.newBuilder();
        if (params != null && !params.isEmpty()) {
            params.forEach((k, v) -> {
                if (k != null && v != null) {
                    urlBuilder.addQueryParameter(k, v);
                }
            });
        }

        Request.Builder reqBuilder = new Request.Builder()
                .url(urlBuilder.build());

        switch (method.toUpperCase()) {
            case "GET":
                if (body != null) {
                    logger.warn("GET request with body is not recommended.");
                }
                reqBuilder.get();
                break;
            case "POST":
                reqBuilder.post(body != null ? body : RequestBody.create(null, new byte[0]));
                break;
            case "PUT":
                reqBuilder.put(body != null ? body : RequestBody.create(null, new byte[0]));
                break;
            case "DELETE":
                reqBuilder.delete(body);
                break;
            default:
                reqBuilder.method(method.toUpperCase(), body);
        }

        if (headers != null && !headers.isEmpty()) {
            headers.forEach((k, v) -> {
                if (k != null && v != null) {
                    reqBuilder.addHeader(k, v);
                }
            });
        }

        Request request = reqBuilder.build();
        logger.debug("Executing {} request: {}", method.toUpperCase(), request.url());
        return getDefaultClient().newCall(request).execute();
    }
}



