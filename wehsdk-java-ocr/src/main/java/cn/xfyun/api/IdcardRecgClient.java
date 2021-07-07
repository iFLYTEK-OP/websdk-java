package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 *
 *     身份证识别
 *
 *  身份证识别，对身份证正反面图片进行识别，返回身份证图片上的姓名、民族、住址、身份证号、
 *  签发机关和有效期等信息，可以省去用户手动录入的过程
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 15:21
 */
public class IdcardRecgClient extends PlatformHttpClient {


    public IdcardRecgClient(Builder builder) {
        super(builder);
    }

    public String idcardRecg(String imageBase64, String encoding) throws IOException {
        String signUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        return sendPost(signUrl, JSON, null, bodyParam(imageBase64, encoding));
    }

    private String bodyParam(String imageBase64, String encoding) {
        JsonObject jso = new JsonObject();
        jso.add("header", buildHeader());

        /** parameter **/
        JsonObject parameter = new JsonObject();
        JsonObject service = new JsonObject();

        service.add("result", buildResult());
        parameter.add(serviceId, service);
        jso.add("parameter", parameter);

        /** payload **/
        JsonObject payload = new JsonObject();
        JsonObject inputImage1 = new JsonObject();
        inputImage1.addProperty("encoding", imageBase64);
        inputImage1.addProperty("image", encoding);
        inputImage1.addProperty("status",status);
        payload.add("s5ccecfce_data_1", inputImage1);
        jso.add("payload", payload);
        return jso.toString();
    }

    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/s5ccecfce";

        private static final String SERVER_ID = "s5ccecfce";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVER_ID, appId, apiKey, apiSecret);
        }

        @Override
        public IdcardRecgClient build() {
            return new IdcardRecgClient(this);
        }
    }
}
