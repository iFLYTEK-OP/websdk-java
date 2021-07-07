package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 *       印刷文字识别（多语种）
 *
 *       印刷文字识别（多语种），基于深度神经网络模型的端到端文字识别系统，
 *       将图片（来源如扫描仪或数码相机）中的字体转化为计算机可编码的文字，支持中文、英文、日语、韩语、俄语。
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 9:43
 */
public class MultilingualWordsClient extends PlatformHttpClient {


    public MultilingualWordsClient(Builder builder) {
        super(builder);
    }

    public String multilingualWordsRecognition(String imageBase64, String imageEncoding) throws IOException {
        String signUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        return sendPost(signUrl, JSON, null, bodyParam(imageBase64, imageEncoding));
    }

    private String bodyParam(String imageBase64, String imageEncoding) {
        JsonObject jso = new JsonObject();
        jso.add("header", buildHeader());
        JsonObject service = new JsonObject();
        service.addProperty("category", "mix0");
        service.add("result", buildResult());

        JsonObject parameter = new JsonObject();
        parameter.add(serviceId, service);
        jso.add("parameter", parameter);

        /** payload **/
        JsonObject payload = new JsonObject();
        JsonObject inputImage1 = new JsonObject();
        inputImage1.addProperty("encoding", imageEncoding);
        inputImage1.addProperty("image", imageBase64);
        inputImage1.addProperty("status", status);
        payload.add("s00b65163_data_1", inputImage1);

        jso.add("payload", payload);
        return jso.toString();
    }

    public static final class Builder extends PlatformBuilder<Builder> {


        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/s00b65163";

        private static final String SERVICE_ID = "s00b65163";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVICE_ID, appId, apiKey, apiSecret);
        }

        @Override
        public MultilingualWordsClient build() {
            return new MultilingualWordsClient(this);
        }
    }
}
