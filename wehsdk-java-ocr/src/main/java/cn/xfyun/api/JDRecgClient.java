package cn.xfyun.api;

import cn.xfyun.base.http.HttpRequestClient;
import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.config.JDRecgEnum;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 *
 *      行驶证识别
 *      驾驶证识别
 *      车牌识别
 *
 *   基于深度学习技术，识别机动车行驶证正页的关键字段，包括号牌号码、车辆类型、所有人、住址、使用性质、品牌型号、车辆识别代号、发动机号码、注册日期、发证日期等字段，准确可靠。
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 15:00
 */
public class JDRecgClient extends PlatformHttpClient {

    private JDRecgEnum jdRecgEnum;

    public JDRecgEnum getJdRecgEnum() {
        return jdRecgEnum;
    }

    public JDRecgClient(Builder builder) {
        super(builder);
    }

    public String handle(String imageBase64, String encoding) throws IOException {
        String signUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        return sendPost(signUrl + jdRecgEnum.getValue(), JSON, null, bodyParam(imageBase64, encoding));
    }

    private String bodyParam(String imageBase64, String encoding) {
        JsonObject jso = new JsonObject();
        jso.add("header", buildHeader());

        /** parameter **/
        JsonObject parameter = new JsonObject();
        JsonObject service = new JsonObject();

        service.add(jdRecgEnum.getService(), buildResult());
        parameter.add(jdRecgEnum.getParameter(), service);
        jso.add("parameter", parameter);

        /** payload **/
        JsonObject payload = new JsonObject();
        JsonObject inputImage1 = new JsonObject();
        inputImage1.addProperty("encoding", imageBase64);
        inputImage1.addProperty("image", encoding);
        inputImage1.addProperty("status",status);
        payload.add(jdRecgEnum.getPayload(), inputImage1);
        jso.add("payload", payload);
        return jso.toString();
    }


    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/";

        private JDRecgEnum jdRecgEnum;

        public Builder(String appId, String apiKey, String apiSecret, JDRecgEnum jdRecgEnum) {
            super(HOST_URL, jdRecgEnum.getValue(), appId, apiKey, apiSecret);
            this.jdRecgEnum = jdRecgEnum;
        }

        public Builder jdRecg(JDRecgEnum jdRecgEnum) {
            this.serviceId(jdRecgEnum.getValue());
            this.jdRecgEnum = jdRecgEnum;
            return this;
        }

        @Override
        public HttpRequestClient build() {
            return null;
        }
    }
}
