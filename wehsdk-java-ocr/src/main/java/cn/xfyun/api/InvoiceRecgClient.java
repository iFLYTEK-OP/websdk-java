package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 *
 *     增值税发票识别
 *
 *  增值税发票识别，对增值税发票图片进行识别，返回图片上的发票号码、开票日期、购买方信息、金额、单价等信息。
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 15:21
 */
public class InvoiceRecgClient extends PlatformHttpClient {


    public InvoiceRecgClient(Builder builder) {
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

        service.addProperty("template_list", "vat_invoice");

        parameter.add(serviceId, service);
        jso.add("parameter", parameter);

        /** payload **/
        JsonObject payload = new JsonObject();
        JsonObject inputImage1 = new JsonObject();
        inputImage1.addProperty("encoding", imageBase64);
        inputImage1.addProperty("image", encoding);
        inputImage1.addProperty("status",status);
        payload.add("s824758f1_data_1", inputImage1);
        jso.add("payload", payload);
        return jso.toString();
    }

    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/s824758f1";

        private static final String SERVER_ID = "s824758f1";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVER_ID, appId, apiKey, apiSecret);
        }

        @Override
        public InvoiceRecgClient build() {
            return new InvoiceRecgClient(this);
        }
    }
}
