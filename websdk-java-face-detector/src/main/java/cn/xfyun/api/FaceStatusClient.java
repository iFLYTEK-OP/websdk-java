package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 *    配合式活体检测
 *
 *    基于讯飞自研的人脸算法，可以通过用户提供的图片，对图片内人物眼睛是否睁开进行判断，并且返回得分。
 *    该能力是通过HTTP API的方式给开发者提供一个通用的接口。
 *    HTTP API适用于一次性交互数据传输的AI服务场景，块式传输。相较于SDK，API具有轻量、跨语言的特点。
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/1 15:46
 */
public class FaceStatusClient extends PlatformHttpClient {


    public FaceStatusClient(FaceStatusClient.Builder builder) {
        super(builder);
    }


    public String faceContrast(String imageBase641, String encoding) throws IOException {
        String signUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        return sendPost(signUrl, JSON, null, bodyParam(imageBase641, encoding));
    }

    private String bodyParam(String imageBase64, String encoding) {
        JsonObject jso = new JsonObject();
        jso.add("header", buildHeader());
        JsonObject service = new JsonObject();
        service.addProperty("service_kind", "face_status");
        service.add("face_status_result", buildResult());

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
        public FaceStatusClient build() {
            FaceStatusClient client = new FaceStatusClient(this);
            return client;
        }
    }

}
