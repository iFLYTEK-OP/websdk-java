package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 *
 *     营业执照识别
 *
 *  营业执照识别，对营业执照图片进行识别，返回营业执照图片上的注册号、名称、类型、住所、法定代表人、注册资本、成立日期、
 *  营业期限和经营范围等信息，可以省去用户手动录入的过程，自动完成营业执照信息的结构化和图像数据的采集
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 15:21
 */
public class BusinessLicenseRecgClient extends PlatformHttpClient {


    public BusinessLicenseRecgClient(Builder builder) {
        super(builder);
    }

    public String invoice(String imageBase64, String encoding) throws IOException {
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

        service.addProperty("template_list", "bus_license");

        parameter.add(serviceId, service);
        jso.add("parameter", parameter);

        /** payload **/
        JsonObject payload = new JsonObject();
        JsonObject inputImage1 = new JsonObject();
        inputImage1.addProperty("encoding", imageBase64);
        inputImage1.addProperty("image", encoding);
        inputImage1.addProperty("status",status);
        payload.add("sff4ea3cf_data_1", inputImage1);
        jso.add("payload", payload);
        return jso.toString();
    }

    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/sff4ea3cf";

        private static final String SERVER_ID = "sff4ea3cf";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVER_ID, appId, apiKey, apiSecret);
        }

        @Override
        public BusinessLicenseRecgClient build() {
            return new BusinessLicenseRecgClient(this);
        }
    }
}
