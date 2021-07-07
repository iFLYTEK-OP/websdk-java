package cn.xfyun.api;

import cn.xfyun.base.http.HttpRequestBuilder;
import cn.xfyun.base.http.HttpRequestClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;

/**
 *       名片识别
 *
 *    名片识别，通过OCR（光学字符识别 Optical Character Recognition）技术，对纸质名片进行识别，
 *    返回名片上的姓名、手机、电话、公司、部门、职位、传真、邮箱、网站、地址等关键信息，可以省去用户手动录入的过程，
 *    自动完成名片信息的结构化和数据的采集，可以很方便对接客户的后台数据系统，给用户带来极大的便利。
 *    该名片识别接口支持中文（简体和繁体）名片、英文、以及 16种小语种 名片，接口可以 自动识别名片语种
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 10:35
 */
public class BusinessCardClient extends HttpRequestClient {

    public BusinessCardClient(Builder builder) {
        super(builder);
    }


    public String businessCardRecognition(String imageBase64) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("engine_type", "bankcard");
        jsonObject.addProperty("card_number_image", "0");
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, jsonObject.toString(), null);
        return sendPost(hostUrl, FORM, header, "image=" + imageBase64);
    }



    public static final class Builder extends HttpRequestBuilder<Builder> {

        private static final String HOST_URL = "https://webapi.xfyun.cn/v1/service/v1/ocr/business_card";

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        @Override
        public BusinessCardClient build() {
            return new BusinessCardClient(this);
        }
    }
}
