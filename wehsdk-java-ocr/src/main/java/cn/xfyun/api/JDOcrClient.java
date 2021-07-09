package cn.xfyun.api;

import cn.xfyun.base.http.HttpClient;
import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.config.JDRecgEnum;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 *
 *      行驶证识别  驾驶证识别  车牌识别
 *
 *
 *    错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 15:00
 */
public class JDOcrClient extends PlatformHttpClient {

    private JDRecgEnum jdRecgEnum;

    public JDRecgEnum getJdRecgEnum() {
        return jdRecgEnum;
    }

    public JDOcrClient(Builder builder) {
        super(builder);
        this.jdRecgEnum = builder.jdRecgEnum;
    }

    public String handle(String imageBase64, String encoding) throws IOException {
        String signUrl = Signature.signHostDateAuthorization(hostUrl + jdRecgEnum.getValue(), "POST", apiKey, apiSecret);
        return sendPost(signUrl, JSON, null, bodyParam(imageBase64, encoding));
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
        inputImage1.addProperty("encoding", encoding);
        inputImage1.addProperty("image", imageBase64);
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
        public JDOcrClient build() {
            return new JDOcrClient(this);
        }
    }
}
