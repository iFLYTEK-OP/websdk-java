package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 *     人脸比对sensetime
 *
 *     错误码链接：https://www.xfyun.cn/document/error-code
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/1 14:24
 */
public class FaceVerificationClient extends HttpClient {

    /**
     *  是否对图片进行自动旋转，true旋转，false不旋转，默认false
     */
    private boolean autoRotate;

    public boolean isAutoRotate() {
        return autoRotate;
    }

    public FaceVerificationClient(FaceVerificationClient.Builder builder) {
        super(builder);
        this.autoRotate = builder.autoRotate;
    }

    public String compareFace(String imageBase641, String imageBase642) throws IOException {
        JsonObject jso = new JsonObject();
        jso.addProperty("get_image", true);
        jso.addProperty("auto_rotate", autoRotate);
        String params = jso.toString();
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, params);
        return sendPost(hostUrl, FORM, header, "first_image=" + URLEncoder.encode(imageBase641, "UTF-8") + "&" + "second_image="+ URLEncoder.encode(imageBase642, "UTF-8"));
    }


    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://api.xfyun.cn/v1/service/v1/image_identify/face_verification";

        private boolean autoRotate = false;

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        public FaceVerificationClient.Builder url(String url) {
            this.hostUrl(url);
            return this;
        }

        public FaceVerificationClient.Builder autoRotate(boolean autoRotate) {
            this.autoRotate = autoRotate;
            return this;
        }

        @Override
        public FaceVerificationClient build() {
            FaceVerificationClient client = new FaceVerificationClient(this);
            return client;
        }

    }






}
