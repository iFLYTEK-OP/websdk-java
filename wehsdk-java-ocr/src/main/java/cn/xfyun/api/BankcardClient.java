package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.config.HttpRequestEnum;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;

/**
 *        银行卡识别
 *
 *    错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 10:35
 */
public class BankcardClient extends HttpClient {

    /**
     *  是否返回卡号区域截图，默认为0，如果设为 1，则返回base64编码的卡号区域截图。
     */
    private String cardNumberImage;

    public BankcardClient(Builder builder) {
        super(builder);
        this.cardNumberImage = builder.cardNumberImage;
    }


    public String bankcard(String imageBase64) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("engine_type", "bankcard");
        jsonObject.addProperty("card_number_image", "0");
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, jsonObject.toString(), HttpRequestEnum.FORM.getValue());
        return sendPost(hostUrl, FORM, header, "image=" + imageBase64);
    }



    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://webapi.xfyun.cn/v1/service/v1/ocr/bankcard";

        private String cardNumberImage = "0";

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        public Builder cardNumberImage() {
            this.cardNumberImage = "1";
            return this;
        }

        @Override
        public BankcardClient build() {
            return new BankcardClient(this);
        }
    }
}
