package cn.xfyun.api;

import cn.xfyun.base.http.HttpRequestBuilder;
import cn.xfyun.base.http.HttpRequestClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;

/**
 *      银行卡识别
 *
 *   银行卡识别，通过 OCR（光学字符识别 Optical Character Recognition）技术，自动对银行卡进行识别，
 *   返回银行卡原件上的银行卡卡号、有效日期、发卡行、卡片类型（借记卡&信用卡）、持卡人姓名（限信用卡）等信息，
 *   可以省去用户手动录入的过程，自动完成银行卡信息的结构化和图像数据的采集，可以很方便对接客户的后台数据系统，
 *   给用户带来极大的便利。采特有的图像处理技术，在识别银行卡图片过程中，还可以对银行卡图片上的卡号图像，方便用户保存。
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 13:36
 */
public class BankcardClient extends HttpRequestClient {


    public BankcardClient(Builder builder) {
        super(builder);
    }

    public String bankcard(String imageBase64) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("engine_type", "business_card");
        jsonObject.addProperty("pic_required", "1");
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, jsonObject.toString(), null);
        return sendPost(hostUrl, FORM, header, "image=" + imageBase64);
    }

    public static final class Builder extends HttpRequestBuilder<Builder> {

        private static final String HOST_URL = "https://webapi.xfyun.cn/v1/service/v1/ocr/bankcard";

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        @Override
        public BankcardClient build() {
            return new BankcardClient(this);
        }
    }
}
