package cn.xfyun.basic;


import okhttp3.*;
import okio.BufferedSource;
import okio.Okio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created: Renbing.Lu
 * Time: 2025/2/24 16:52
 */

public class RestOperation {

    private static final Logger log = LoggerFactory.getLogger(RestOperation.class);
    private static final OkHttpClient httpClient = new OkHttpClient();

    public static String form(String url, Map<String, String> header, Map<String, String> param, File file) {
        try {
            MultipartBody.Builder bodyBuilder = body(param);
            if(Objects.nonNull(file)) {
                bodyBuilder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }
            Request request = request(url, header, bodyBuilder.build()).build();
            RestResult data = RestResult.from(httpClient.newCall(request).execute());
            if(log.isDebugEnabled()) {
                log.debug("\nURI          : {} " +
                        "\nMethod       : {} " +
                        "\nHeaders      : {} " +
                        "\nResponseStatus   : {} " +
                        "\nResponseBody     : {} " +
                        "\nResponseMessage   : {} " +
                        "\n", url, request.method(), request.headers(),
                        data.getCode(), data.bodyString(), data.getMessage());
            }
            return data.bodyString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <REQUEST> String post(String url, Map<String, String> header, REQUEST req) {
        try {
            long current = TimeOperation.time();
            String json = ConvertOperation.toJson(req);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            Request request = request(url, header, requestBody).build();
            Response response = httpClient.newCall(request).execute();
            RestResult data = RestResult.from(response);

            debug(() -> log.debug("\nURI          : {} " +
                            "\nMethod       : {} " +
                            "\nHeaders      : {} " +
                            "\nRequestBody   : {} " +
                            "\nResponseStatus   : {} " +
                            "\nResponseBody     : {} " +
                            "\nResponseMessage   : {} " +
                            "\nCost   : {}" +
                            "\n", url, request.method(), request.headers(), json, data.getCode(),
                    data.bodyString(), data.getMessage(), TimeOperation.time() - current + "ms"));


            return data.bodyString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <REQUEST> void stream(String url, Map<String, String> header, REQUEST req, Consumer<String> consumer) {

        try {
            long current = TimeOperation.time();
            String json = ConvertOperation.toJson(req);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            Request request = request(url, header, requestBody).build();
            debug(() -> log.debug("\n" + "URI          : {} \n" + "Method       : {} \n"
                            + "Headers      : {} \n" + "Param        : {} \n",
                        url, request.method(), request.headers(), json));

            Response response = httpClient.newCall(request).execute();
            RestResult data = RestResult.from(response);

            debug(() -> log.debug("\n" + "ResponseStatus   : {} \n" + "ResponseMessage   : {} \n", data.code, data.message));
            data.stream(consumer);

            debug(() ->  log.debug("Total cost: {}ms", TimeOperation.time() - current));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static MultipartBody.Builder body(Map<String, String> param) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(Objects.nonNull(param)) {
            param.forEach(builder::addFormDataPart);
        }
        return builder;
    }

    public static Request.Builder request(String url, Map<String, String> header, RequestBody body) {
        Request.Builder builder = new Request.Builder();
        builder.url(url).post(body);
        if(Objects.nonNull(header)) {
            header.forEach(builder::addHeader);
        }
        return builder;
    }

    public static String buildParams(Map<String, Object> params) {
        List<String> list = new ArrayList<>(10);
        params.forEach((k ,v) -> list.add(k + "=" + v));
        return "?" + EasyOperation.joiner(list, "&").get();
    }

    public static class RestResult {
        private Integer code;
        private String message;
        public ResponseBody responseBody;
        public String bodyString;

        public static RestResult from(Response response) {
            RestResult data = new RestResult();
            data.code = response.code();
            data.message = response.message();
            data.responseBody = response.body();
            return data;
        }

        public Integer getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public ResponseBody getResponseBody() {
            return responseBody;
        }

        public synchronized String bodyString() {
            if(EasyOperation.isEmpty(bodyString)) {
                try {
                    bodyString =  Objects.nonNull(responseBody) ? responseBody.string() : "";
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return bodyString;
        }

        // 使用 BufferedSource 逐行读取 SSE 事件流
        public void stream(Consumer<String> consumer) throws IOException {
            BufferedSource bufferedSource = Okio.buffer(responseBody.source());
            while (!bufferedSource.exhausted()) {
                String line = bufferedSource.readUtf8Line();
                if (Objects.nonNull(line) && !line.isEmpty()) {
                    consumer.accept(line);
                }
            }
            bufferedSource.close();
        }
    }

    private static void debug(Runnable runnable) {
        if(log.isDebugEnabled()) {
            runnable.run();
        }
    }

}
