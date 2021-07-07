package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 *
 *        火车票识别
 *
 *   火车票识别，基于深度神经网络模型的端到端文字识别系统，可以自动地从图片中定位火车票区域，
 *   识别出其中包含的信息。可以省去用户手动录入的过程，自动完成火车票信息的结构化和图像数据的采集
 *
 *   错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 15:40
 */
public class TrainTicketsRecgClient  extends PlatformHttpClient {

    public TrainTicketsRecgClient(Builder builder) {
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

        service.addProperty("template_list", "train_ticket");

        parameter.add(serviceId, service);
        jso.add("parameter", parameter);

        /** payload **/
        JsonObject payload = new JsonObject();
        JsonObject inputImage1 = new JsonObject();
        inputImage1.addProperty("encoding", imageBase64);
        inputImage1.addProperty("image", encoding);
        inputImage1.addProperty("status",status);
        payload.add("s19cfe728_data_1", inputImage1);
        jso.add("payload", payload);
        return jso.toString();
    }

    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/s19cfe728";

        private static final String SERVER_ID = "s19cfe728";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVER_ID, appId, apiKey, apiSecret);
        }

        @Override
        public TrainTicketsRecgClient build() {
            return new TrainTicketsRecgClient(this);
        }
    }
}
