package cn.xfyun.api;

import cn.xfyun.base.http.HttpRequestBuilder;
import cn.xfyun.base.http.HttpRequestClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *     人脸水印照比对
 *
 *     人脸水印照比对，对通过接口上传的人脸照片和一个人脸水印照片进行比对，来判断是否为同一个人。
 *     若上传的照片中包含 exif 方向信息，我们会按此信息旋转、翻转后再做后续处理。
 *     同时，我们还提供自动旋转功能，当照片方向混乱且 exif 方向信息不存在或不正确的情况下，
 *     服务会根据照片中人脸方向来检查可能正确的方向，并按照正确的方向提供人脸检测结果。
 *
 *     错误码链接：https://www.xfyun.cn/document/error-code （code返回错误码时必看）
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/1 15:00
 */
public class WatermarkVerificationClient extends HttpRequestClient {

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
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, params, null);
        Map<String, String> body = new HashMap<>(2);
        body.put("face_image", faceImageBase64);
        body.put("watermark_image", watermarkImageBase64);
        return sendPost(hostUrl, header, body);
    }


    public static final class Builder extends HttpRequestBuilder<Builder> {

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