package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 *     基于讯飞自研的人脸算法，可实现对比两张照片中的人脸信息，判断是否是同一个人并返回相似度得分
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/1 13:53
 */
public class FaceCompareClient extends PlatformHttpClient {

    public FaceCompareClient(FaceCompareClient.Builder builder) {
        super(builder);
    }


    public String faceCompare(String imageBase641, String encoding1, String imageBase642, String encoding2) throws IOException {
        String signUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        return sendPost(signUrl, JSON, null, bodyParam(imageBase641, encoding1, imageBase642, encoding2));
    }

    public String bodyParam(String imageBase641, String imageEncoding1, String imageBase642, String imageEncoding2) {
        JsonObject jso = new JsonObject();

        jso.add("header", buildHeader());

        /** parameter **/
        JsonObject parameter = new JsonObject();
        JsonObject service = new JsonObject();
        service.addProperty("service_kind", "face_compare");

        service.add("face_compare_result", buildResult());
        parameter.add(serviceId, service);
        jso.add("parameter", parameter);

        /** payload **/
        JsonObject payload = new JsonObject();
        JsonObject inputImage1 = new JsonObject();
        inputImage1.addProperty("encoding", imageEncoding1);
        inputImage1.addProperty("image", imageBase641);
        payload.add("input1", inputImage1);

        JsonObject inputImage2 = new JsonObject();
        inputImage2.addProperty("encoding", imageEncoding2);
        inputImage2.addProperty("image", imageBase642);
        payload.add("input2", inputImage2);
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
        public FaceCompareClient build() {
            FaceCompareClient client = new FaceCompareClient(this);
            return client;
        }
    }
}
