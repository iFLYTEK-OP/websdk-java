package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 *    场所识别
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/27 10:03
 */
public class PlaceRecClient extends PlatformHttpClient {

    public PlaceRecClient(Builder builder) {
        super(builder);
    }

    public String send(String imageBase64, String type) throws IOException {
        String realUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        return sendPost(realUrl, JSON, buildParam(imageBase64, type));
    }


    private String buildParam(String imageBase64, String type) {
        JsonObject req = new JsonObject();
        //平台参数
        req.add("header", buildHeader());
        //功能参数
        JsonObject parameter = new JsonObject();

        JsonObject result = new JsonObject();

        result.add("result", buildResult());
        result.addProperty("func", "image/place");

        parameter.add(this.serviceId, result);
        //请求数据
        JsonObject payload = new JsonObject();
        JsonObject input = new JsonObject();
        input.addProperty("encoding", type);
        input.addProperty("image", imageBase64);
        input.addProperty("status", status);
        payload.add("data1", input);

        req.add("parameter", parameter);
        req.add("payload", payload);
        return req.toString();
    }


    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/s5833e7f6";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, "s5833e7f6", appId, apiKey, apiSecret);
        }

        @Override
        public PlaceRecClient build() {
            return new PlaceRecClient(this);
        }
    }
}
