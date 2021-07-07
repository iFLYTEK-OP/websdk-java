package cn.xfyun.api;

import cn.xfyun.base.http.HttpRequestBuilder;
import cn.xfyun.base.http.HttpRequestClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 13:42
 */
public class InvoiceClient  extends HttpRequestClient {


    public InvoiceClient(Builder builder) {
        super(builder);
    }

    public String invoice(String imageBase64) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("engine_type", "invoice");
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, jsonObject.toString(), null);
        return sendPost(hostUrl, FORM, header, "image=" + imageBase64);
    }

    public static final class Builder extends HttpRequestBuilder<Builder> {

        private static final String HOST_URL = "https://webapi.xfyun.cn/v1/service/v1/ocr/invoice";

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        @Override
        public InvoiceClient build() {
            return new InvoiceClient(this);
        }
    }
}
