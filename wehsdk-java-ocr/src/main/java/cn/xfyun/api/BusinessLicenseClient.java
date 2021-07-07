package cn.xfyun.api;

import cn.xfyun.base.http.HttpRequestBuilder;
import cn.xfyun.base.http.HttpRequestClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;

/**
 *       营业执照识别
 *
 *   营业执照识别，通过 OCR（光学字符识别 Optical Character Recognition）技术，对营业执照图片进行识别，返回营业执照图片上的注册号、
 *   名称、类型、住所、法定代表人、注册资本、成立日期、营业期限和经营范围等信息，可以省去用户手动录入的过程，
 *   自动完成营业执照信息的结构化和图像数据的采集，可以很方便对接客户的后台数据系统，给用户带来极大的便利，方便用户保存。
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 13:39
 */
public class BusinessLicenseClient extends HttpRequestClient {

    public BusinessLicenseClient(Builder builder) {
        super(builder);
    }

    public String businessLicense(String imageBase64) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("engine_type", "business_license");
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, jsonObject.toString(), null);
        return sendPost(hostUrl, FORM, header, "image=" + imageBase64);
    }


    public static final class Builder extends HttpRequestBuilder<Builder> {

        private static final String HOST_URL = "https://webapi.xfyun.cn/v1/service/v1/ocr/business_license";

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        @Override
        public BusinessLicenseClient build() {
            return new BusinessLicenseClient(this);
        }
    }
}
