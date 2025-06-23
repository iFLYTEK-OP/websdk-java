package cn.xfyun.util;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * okhttp工具类
 *
 * @author <zyding6@iflytek.com>
 */
public class OkHttpUtils {

    public static final OkHttpClient client;

    static {
        Dispatcher dispatcher = new Dispatcher();
        // 最大并发请求数 默认 64
        dispatcher.setMaxRequests(100);
        // 单个主机最大并发数 默认 5
        dispatcher.setMaxRequestsPerHost(10);

        // 最大空闲连接数、空闲时间
        ConnectionPool connectionPool = new ConnectionPool(
                5, 5, TimeUnit.MINUTES);

        client = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .build();
    }
}
