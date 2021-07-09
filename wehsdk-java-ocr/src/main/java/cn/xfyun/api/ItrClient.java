package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.config.ItrEntEnum;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 *     拍照速算识别 & 公式识别
 *
 *   拍照速算识别基于深度学习的端到端识别技术，自动识别图片中的速算题并智能批改，
 *   返回标准LaTeX公式及批改结果。覆盖K12教育范围内15种题型，支持口算、竖式、方程、脱式计算等，
 *   详细请参照 速算题型 。支持的场景有印刷体、手写体、拍照场景。
 *
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 13:50
 */
public class ItrClient extends HttpClient {

    private ItrEntEnum itrEntEnum;

    public ItrEntEnum getItrEntEnum() {
        return itrEntEnum;
    }

    public ItrClient(Builder builder) {
        super(builder);
        this.itrEntEnum = builder.itrEntEnum;
    }


    public String itr(String imageBase64) throws Exception {
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
        business.addProperty("ent", itrEntEnum.getValue());
        business.addProperty("aue", "raw");
        //填充data
        data.addProperty("image", imageBase64);
        //填充body
        body.add("common", common);
        body.add("business", business);
        body.add("data", data);
        return body.toString();
    }



    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://rest-api.xfyun.cn/v2/itr";

        private ItrEntEnum itrEntEnum;

        public Builder(String appId, String apiKey, String apiSecret, ItrEntEnum itrEntEnum) {
            super(HOST_URL, appId, apiKey, apiSecret);
            this.itrEntEnum = itrEntEnum;
        }

        @Override
        public ItrClient build() {
            return new ItrClient(this);
        }
    }
}
