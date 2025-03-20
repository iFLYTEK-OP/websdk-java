package cn.xfyun.base.http;

import cn.xfyun.base.Client;
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
public abstract class HttpClient extends Client {

    protected static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    protected static final MediaType FORM = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");
    protected static final MediaType BINARY = MediaType.get("binary/octet-stream");

    protected Request request;

    protected OkHttpClient okHttpClient;

    public Request getRequest() {
        return request;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    protected String sendPost(String url, MediaType mediaType, Map<String, String> header, String body) throws IOException {
        return sendPost(url, header, RequestBody.create(mediaType, body));
    }

    protected String sendPost(String url, MediaType mediaType, String body) throws IOException {
        return sendPost(url, null, RequestBody.create(mediaType, body));
    }

    protected String sendPost(String url, MediaType mediaType, Map<String, String> header, byte[] body) throws IOException {
        return sendPost(url, header, RequestBody.create(mediaType, body));
    }

    protected String sendPost(String url, Map<String, String> header, Map<String, String> body) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (Objects.nonNull(body)) {
            for (Map.Entry<String, String> entry : body.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        return sendPost(url, header, formBuilder.build());
    }

    protected String sendBodyPost(String url, Map<String, String> header, RequestBody requestBody) throws IOException {
        return sendPost(url, header, requestBody);
    }

    protected String sendPost(String url, MediaType mediaType, Map<String, String> header, String body, Map<String, String> parameter) throws IOException {
        // 构建完整的URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (Objects.nonNull(parameter)) {
            for (Map.Entry<String, String> entry : parameter.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        Request.Builder builder = new Request
                .Builder()
                .url(urlBuilder.build().toString())
                .post(RequestBody.create(mediaType, body));
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

    private String sendPost(String url, Map<String, String> header, RequestBody requestBody) throws IOException {
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


    public String sendGet(String url, Map<String, String> header) throws IOException {
        Request.Builder builder = new Request
                .Builder()
                .url(url)
                .get();
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

    public HttpClient(HttpBuilder builder) {
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
                .retryOnConnectionFailure(builder.getRetryOnConnectionFailure())
                .build();
    }

    public boolean getRetryOnConnectionFailure() {
        return okHttpClient.retryOnConnectionFailure();
    }

    public int getCallTimeout() {
        return okHttpClient.callTimeoutMillis() / 1000;
    }

    public int getConnectTimeout() {
        return okHttpClient.connectTimeoutMillis() / 1000;
    }

    public int getReadTimeout() {
        return okHttpClient.readTimeoutMillis() / 1000;
    }

    public int getWriteTimeout() {
        return okHttpClient.writeTimeoutMillis() / 1000;
    }

}
