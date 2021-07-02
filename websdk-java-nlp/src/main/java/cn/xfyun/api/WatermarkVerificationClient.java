package cn.xfyun.api;

import cn.xfyun.config.BaseBuilder;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/1 15:00
 */
public class WatermarkVerificationClient extends HttpRequestClient{

    /**
     * 是否对图片进行自动旋转，true旋转，false不旋转，默认false
     */
    private boolean autoRotate;


    public WatermarkVerificationClient(WatermarkVerificationClient.Builder builder) {
        super(builder);
        this.autoRotate = builder.autoRotate;
    }

    public String compareWaterMark(String imageBase641, String imageBase642) throws IOException {
        JsonObject jso = new JsonObject();
        jso.addProperty("get_image", true);
        jso.addProperty("auto_rotate", autoRotate);
        String params = jso.getAsString();
        String paramBase64 = new String(Base64.encodeBase64(params.getBytes("UTF-8")));
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, paramBase64, null);
        return sendPost(hostUrl, FORM, header, "face_image=" + URLEncoder.encode(imageBase641, "UTF-8") + "&" + "watermark_image="+ URLEncoder.encode(imageBase642, "UTF-8"));

    }


    public class Builder extends BaseBuilder {

        private static final String HOST_URL = "https://api.xfyun.cn/v1/service/v1/image_identify/face_verification";

        private boolean autoRotate = false;

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        public WatermarkVerificationClient.Builder autoRotate(boolean autoRotate) {
            this.autoRotate = autoRotate;
            return this;
        }

        public WatermarkVerificationClient build() {
            WatermarkVerificationClient client = new WatermarkVerificationClient(this);
            return client;
        }

    }

}