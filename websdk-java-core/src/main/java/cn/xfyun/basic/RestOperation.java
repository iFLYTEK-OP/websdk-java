package cn.xfyun.basic;


import okhttp3.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created: Renbing.Lu
 * Time: 2025/2/24 16:52
 */

public class RestOperation {
    private static final OkHttpClient httpClient = new OkHttpClient();

    public static RestResult form(String url, Map<String, String> header, Map<String, String> param, File file) {
        try {
            MultipartBody.Builder bodyBuilder = body(param);
            if(Objects.nonNull(file)) {
                bodyBuilder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }
            Request request = request(url, header, bodyBuilder.build()).build();
            RestResult data = RestResult.from(httpClient.newCall(request).execute());
            System.out.printf("\nURI          : %s " +
                    "\nMethod       : %s " +
                    "\nHeaders      : %s " +
                    "\nResponseStatus   : %s " +
                    "\nResponseBody     : %s " +
                    "\nResponseMessage   : %s " +
                    "\n", url, request.method(), request.headers(), data.getCode(), data.getBody(), data.getMessage());
            return data;
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


    public static <REQUEST> RestResult post(String url, Map<String, String> header, REQUEST req) {

        try {
            String json = ConvertOperation.toJson(req);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            Request request = request(url, header, requestBody).build();
            Response response = httpClient.newCall(request).execute();
            RestResult data = RestResult.from(response);
            System.out.printf("\nURI          : %s " +
                    "\nMethod       : %s " +
                    "\nHeaders      : %s " +
                    "\nRequestBody   : %s " +
                    "\nResponseStatus   : %s " +
                    "\nResponseBody     : %s " +
                    "\nResponseMessage   : %s " +
                    "\n", url, request.method(), request.headers(), json, data.getCode(), data.getBody(), data.getMessage());
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
