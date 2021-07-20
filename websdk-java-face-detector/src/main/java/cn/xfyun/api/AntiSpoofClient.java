package cn.xfyun.api;



import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 *     静默活体检测
 *
 *    基于讯飞自研的活体检测算法，针对打印照、屏幕二次翻拍等作弊场景，
 *    基于图片中人像破绽及成像畸形，可有效识别目标是否为活体，并给出置信度参考。
 *
 *    错误码链接：https://www.xfyun.cn/document/error-code （code返回错误码时必看）
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/1 15:19
 */
public class AntiSpoofClient extends PlatformHttpClient {

    public AntiSpoofClient(AntiSpoofClient.Builder builder) {
        super(builder);
    }

    public String faceContrast(String imageBase64, String encoding) throws IOException {
        String signUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        return sendPost(signUrl, JSON, null, bodyParam(imageBase64, encoding));
    }

    private String bodyParam(String imageBase64, String encoding) {
        JsonObject jso = new JsonObject();
        jso.add("header", buildHeader());
        JsonObject service = new JsonObject();
        service.addProperty("service_kind", "anti_spoof");
        service.add("anti_spoof_result", buildResult());

        JsonObject parameter = new JsonObject();
        parameter.add(serviceId, service);
        jso.add("parameter", parameter);

        /** payload **/
        JsonObject payload = new JsonObject();
        JsonObject inputImage1 = new JsonObject();
        inputImage1.addProperty("encoding", encoding);
        inputImage1.addProperty("image", imageBase64);
        payload.add("input1", inputImage1);

        jso.add("payload", payload);
        return jso.toString();
    }


    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/s67c9c78c";

        private static final String SERVICE_ID = "s67c9c78c";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVICE_ID, appId, apiKey, apiSecret);
        }

        @Override
        public AntiSpoofClient build() {
            AntiSpoofClient client = new AntiSpoofClient(this);
            return client;
        }
    }
}
