package cn.xfyun.api;

import cn.xfyun.config.HandWritingLanguageEnum;
import cn.xfyun.base.http.HttpRequestBuilder;
import cn.xfyun.base.http.HttpRequestClient;
import cn.xfyun.config.HandWritingLocationEnum;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 *   手写文字识别
 *
 *   手写文字识别(Handwriting words Recognition)基于深度神经网络模型的端到端文字识别系统，
 *   将图片（来源如扫描仪或数码相机）中的手写字体转化为计算机可编码的文字，支持中英文。
 *
 *   图片属性：jpg/png/bmp,最短边至少15px，最长边最大4096px,编码后大小不超过4M,识别文字语种：中英文
 *
 *   错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
 *
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/6 17:01
 */
public class HandWritingClient extends HttpRequestClient {


    /**
     *   文本语言
     */
    private HandWritingLanguageEnum language;


    /**
     *   是否返回文本位置信息  默认false
     */
    private HandWritingLocationEnum location;


    public HandWritingLanguageEnum getLanguage() {
        return language;
    }

    public HandWritingLocationEnum getLocation() {
        return location;
    }

    public HandWritingClient(Builder builder) {
        super(builder);
        this.language = builder.language;
        this.location = builder.location;
    }

    public String handWriting(String imageBase64) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("language", language.getValue());
        jsonObject.addProperty("location", location.getValue());
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, jsonObject.toString(), null);
        return sendPost(hostUrl, FORM, header, "image=" + imageBase64);
    }


    public static final class Builder extends HttpRequestBuilder<Builder> {
        /**
         * 服务请求地址
         */
        private static final String HOST_URL = "https://webapi.xfyun.cn/v1/service/v1/ocr/handwriting";

        private HandWritingLanguageEnum language = HandWritingLanguageEnum.CN;

        private HandWritingLocationEnum location = HandWritingLocationEnum.OFF;


        public Builder language(HandWritingLanguageEnum language) {
            this.language = language;
            return this;
        }

        public Builder location(HandWritingLocationEnum location) {
            this.location = location;
            return this;
        }

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        @Override
        public HandWritingClient build() {
            return new HandWritingClient(this);
        }
    }

}
