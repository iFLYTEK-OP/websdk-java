package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;

/**
 *    名片识别
 *
 *   错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 13:36
 */
public class BusinessCardClient extends HttpClient {

    private static final String BUSINESS_CARD = "business_card";

    private String picRequired;

    public BusinessCardClient(Builder builder) {
        super(builder);
        this.picRequired = builder.picRequired;
    }

    public String businessCard(String imageBase64) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("engine_type", BUSINESS_CARD);
        jsonObject.addProperty("pic_required", picRequired);
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, jsonObject.toString());
        return sendPost(hostUrl, FORM, header, "image=" + imageBase64);
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://webapi.xfyun.cn/v1/service/v1/ocr/business_card";

        private String picRequired = "0";

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        public Builder picRequired() {
            picRequired = "1";
            return this;
        }

        @Override
        public BusinessCardClient build() {
            return new BusinessCardClient(this);
        }
    }
}
