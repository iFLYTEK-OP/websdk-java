package cn.xfyun.api;

import cn.xfyun.base.http.HttpRequestBuilder;
import cn.xfyun.base.http.HttpRequestClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 *     基于商汤的活体检测技术，将一段实地拍摄的人脸视频进行云端检测，
 *     判断是否为真人活体，该接口用于对一段短视频进行静默活体检测，判断视频中人脸是否为活体。
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/1 15:56
 */
public class SilentDetectionClient extends HttpRequestClient {

    public SilentDetectionClient(SilentDetectionClient.Builder builder) {
        super(builder);
    }


    public String silentDetection(String audioBase64) throws IOException {
        JsonObject jso = new JsonObject();
        jso.addProperty("get_image", true);
        String params = jso.toString();
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, params, null);
        return sendPost(hostUrl, FORM, header, "file=" + URLEncoder.encode(audioBase64, "UTF-8"));
    }

    public static final class Builder extends HttpRequestBuilder<Builder> {

        private static final String HOST_URL = "https://api.xfyun.cn/v1/service/v1/image_identify/silent_detection";

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        @Override
        public SilentDetectionClient build() {
            SilentDetectionClient client = new SilentDetectionClient(this);
            return client;
        }

    }
}
