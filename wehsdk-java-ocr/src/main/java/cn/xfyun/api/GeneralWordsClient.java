package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.config.HttpRequestEnum;
import cn.xfyun.config.LanguageEnum;
import cn.xfyun.config.LocationEnum;
import cn.xfyun.config.OcrWordsEnum;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;

/**
 *    印刷文字识别 & 手写文字识别
 *
 *    通用文字识别(General words Recognition)基于深度神经网络模型的端到端文字识别系统，
 *    将图片（来源如扫描仪或数码相机）中的文字转化为计算机可编码的文字，支持中英文。
 *
 *    错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/6 18:00
 */
public class GeneralWordsClient extends HttpClient {

    private OcrWordsEnum ocrTypeEnum;

    /**
     *   文本语言   默认false
     */
    private LanguageEnum language;

    /**
     *   是否返回文本位置信息  默认false
     */
    private LocationEnum location;

    public LanguageEnum getLanguage() {
        return language;
    }

    public LocationEnum getLocation() {
        return location;
    }

    public GeneralWordsClient(GeneralWordsClient.Builder builder) {
        super(builder);
        this.ocrTypeEnum = builder.ocrTypeEnum;
        this.language = builder.language;
        this.location = builder.location;
    }

    public String generalWords(String imageBase64) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("language", language.getValue());
        jsonObject.addProperty("location", location.getValue());
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, jsonObject.toString(), HttpRequestEnum.FORM.getValue());
        return sendPost(hostUrl + ocrTypeEnum.getValue(), FORM, header, "image=" + imageBase64);
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://webapi.xfyun.cn/v1/service/v1/ocr/";

        private OcrWordsEnum ocrTypeEnum;

        private LanguageEnum language = LanguageEnum.CN;

        private LocationEnum location = LocationEnum.OFF;


        public Builder language(LanguageEnum language) {
            this.language = language;
            return this;
        }

        public Builder location(LocationEnum location) {
            this.location = location;
            return this;
        }

        public Builder(String appId, String apiKey, OcrWordsEnum ocrTypeEnum) {
            super(HOST_URL, appId, apiKey, null);
            this.ocrTypeEnum = ocrTypeEnum;
        }

        @Override
        public GeneralWordsClient build() {
            return new GeneralWordsClient(this);
        }
    }
}
