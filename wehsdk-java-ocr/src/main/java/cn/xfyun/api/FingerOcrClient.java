package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 *     指尖文字识别
 *
 *  指尖文字识别，可检测图片中指尖位置，将指尖处文字转化为计算机可编码的文字。
 *
 *  错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
 *
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 14:50
 */
public class FingerOcrClient extends HttpClient {

    public FingerOcrClient(Builder builder) {
        super(builder);
    }

    public String fingerOcr(String imageBase64) throws Exception {
        String body = buildHttpBody(imageBase64);
        Map<String, String> header = Signature.signHttpHeaderDigest(hostUrl, "POST", apiKey, apiSecret, body);
        return sendPost(hostUrl, JSON, header, body);
    }

    private String buildHttpBody(String imageBase64) {
        JsonObject body = new JsonObject();
        JsonObject business = new JsonObject();
        JsonObject common = new JsonObject();
        JsonObject data = new JsonObject();
        //填充common
        common.addProperty("app_id", appId);
        //填充business
        business.addProperty("ent", "fingerocr");
        business.addProperty("mode", "finger+ocr");
        business.addProperty("method", "dynamic");
        //business.addProperty("cut_w_scale", 5.0);
        //business.addProperty("cut_h_scale", 2);
        //business.addProperty("cut_shift", 1);
        business.addProperty("resize_w", 1088);
        business.addProperty("resize_h", 1632);
        //填充data
        data.addProperty("image", imageBase64);

        //填充body
        body.add("common", common);
        body.add("business", business);
        body.add("data", data);

        return body.toString();
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://tyocr.xfyun.cn/v2/ocr";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
        }

        @Override
        public FingerOcrClient build() {
            return new FingerOcrClient(this);
        }
    }
}
