package cn.xfyun.api;

import cn.xfyun.config.BaseBuilder;
import cn.xfyun.config.Client;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/2 9:52
 */
public class HttpRequestClient extends Client {


    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static final MediaType FORM = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");

    protected Request request;

    protected OkHttpClient okHttpClient;


    public String sendPost(String url, MediaType mediaType, Map<String, String> header, String body) throws IOException {
        RequestBody requestBody = RequestBody.create(mediaType, body);
        Request.Builder builder = new Request
                .Builder()
                .url(url)
                .post(requestBody);
        if (Objects.nonNull(header)) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        request = builder.build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public HttpRequestClient(BaseBuilder builder) {
        this.hostUrl = builder.getHostUrl();
        this.appId = builder.getAppId();
        this.apiKey = builder.getApiKey();
        this.apiSecret = builder.getApiSecret();
        this.okHttpClient = new OkHttpClient
                .Builder()
                .callTimeout(builder.getCallTimeout(), TimeUnit.SECONDS)
                .connectTimeout(builder.getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(builder.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(builder.getWriteTimeout(), TimeUnit.SECONDS)
                .build();
    }

}
