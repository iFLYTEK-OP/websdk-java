package cn.xfyun.util;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * okhttp工具类
 *
 * @author <zyding6@iflytek.com>
 */
public class OkHttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);

    public static final OkHttpClient client;

    static {
        Dispatcher dispatcher = new Dispatcher();
        // 调整最大并发请求数为更保守的默认值
        dispatcher.setMaxRequests(100);
        // 单个主机最大并发数
        dispatcher.setMaxRequestsPerHost(10);

        // 最大空闲连接数、空闲时间
        ConnectionPool connectionPool = new ConnectionPool(
                5, 5, TimeUnit.MINUTES);

        client = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .build();
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
        // 改进：更明确的 URL 验证
        if (url == null) {
            throw new IllegalArgumentException("URL must not be null");
        }
        HttpUrl parsedUrl = HttpUrl.parse(url);
        if (parsedUrl == null) {
            throw new IllegalArgumentException("Invalid URL format: " + url);
        }

        HttpUrl.Builder urlBuilder = parsedUrl.newBuilder();
        if (Objects.nonNull(params)) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                // HttpUrl.Builder 会处理 null 值和编码
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        Request.Builder builder = new Request.Builder()
                .url(urlBuilder.build().toString());

        if ("GET".equalsIgnoreCase(method)) {
            if (body != null) {
                // 记录警告或抛出异常
                logger.warn("Warning: GET request with body is not recommended.");
            }
            builder.get();
        } else if ("POST".equalsIgnoreCase(method)) {
            builder.post(body != null ? body : RequestBody.create(null, new byte[0]));
        } else if ("PUT".equalsIgnoreCase(method)) {
            builder.put(body != null ? body : RequestBody.create(null, new byte[0]));
        } else if ("DELETE".equalsIgnoreCase(method)) {
            builder.delete(body);
        } else {
            // 其他方法，如 HEAD, PATCH, OPTIONS 等
            builder.method(method, body);
        }

        // 设置请求头
        if (Objects.nonNull(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        return client.newCall(request).execute();
    }
}



