package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 *
 *        出租车发票识别
 *
 *    出租车发票识别，对出租车发票图片进行识别，返回 车牌号、日期、上下车时间、单价、里程、金额、号码等信息
 *
 *    错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 15:40
 */
public class TaxiInvoiceRecgClient extends PlatformHttpClient {

    public TaxiInvoiceRecgClient(Builder builder) {
        super(builder);
    }

    public String trainTicket(String imageBase64, String encoding) throws IOException {
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

        service.addProperty("template_list", "taxi_ticket");

        parameter.add(serviceId, service);
        jso.add("parameter", parameter);

        /** payload **/
        JsonObject payload = new JsonObject();
        JsonObject inputImage1 = new JsonObject();
        inputImage1.addProperty("encoding", imageBase64);
        inputImage1.addProperty("image", encoding);
        inputImage1.addProperty("status",status);
        payload.add("sb6db0171_data_1", inputImage1);
        jso.add("payload", payload);
        return jso.toString();
    }

    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/sb6db0171";

        private static final String SERVER_ID = "sb6db0171";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVER_ID, appId, apiKey, apiSecret);
        }

        @Override
        public TaxiInvoiceRecgClient build() {
            return new TaxiInvoiceRecgClient(this);
        }
    }
}
