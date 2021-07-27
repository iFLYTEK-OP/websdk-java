package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *     人脸水印照比对
 *
 *     错误码链接：https://www.xfyun.cn/document/error-code （code返回错误码时必看）
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/1 15:00
 */
public class WatermarkVerificationClient extends HttpClient {

    /**
     * 是否对图片进行自动旋转，true旋转，false不旋转，默认false
     */
    private boolean autoRotate;


    public WatermarkVerificationClient(WatermarkVerificationClient.Builder builder) {
        super(builder);
        this.autoRotate = builder.autoRotate;
    }

    public String compare(String faceImageBase64, String watermarkImageBase64) throws IOException {
        JsonObject jso = new JsonObject();
        jso.addProperty("get_image", true);
        jso.addProperty("auto_rotate", autoRotate);
        String params = jso.toString();
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, params);
        Map<String, String> body = new HashMap<>(2);
        body.put("face_image", faceImageBase64);
        body.put("watermark_image", watermarkImageBase64);
        return sendPost(hostUrl, header, body);
    }


    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://api.xfyun.cn/v1/service/v1/image_identify/watermark_verification";

        private boolean autoRotate = false;

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        public Builder autoRotate(boolean autoRotate) {
            this.autoRotate = autoRotate;
            return this;
        }

        @Override
        public WatermarkVerificationClient build() {
            WatermarkVerificationClient client = new WatermarkVerificationClient(this);
            return client;
        }

    }

}