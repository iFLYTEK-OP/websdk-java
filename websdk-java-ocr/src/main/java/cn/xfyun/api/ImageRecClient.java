package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.config.ImageRecEnum;
import cn.xfyun.config.IntsigRecgEnum;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;

/**
 *
 *   场景识别 & 物体识别
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/27 9:48
 */
public class ImageRecClient extends HttpClient {

    public ImageRecClient(HttpBuilder builder) {
        super(builder);
    }


    public String send(String imageName, byte[] body) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("image_name", imageName);
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, jsonObject.toString());
        return sendPost(hostUrl, FORM, header, body);
    }


    public static final class Builder extends HttpBuilder<Builder> {

        public Builder(String appId, String apiKey, ImageRecEnum recgEnum) {
            super(recgEnum.getValue(), appId, apiKey, null);
        }

        @Override
        public ImageRecClient build() {
            return new ImageRecClient(this);
        }
    }
}
