package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.config.ImageWordEnum;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 *
 *     营业执照识别  出租车发票识别  火车票识别  增值税发票识别  身份证识别  印刷文字识别
 *
 *   错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 15:21
 */
public class ImageWordClient extends PlatformHttpClient {

    private ImageWordEnum imageWordEnum;

    public ImageWordEnum getImageWordEnum() {
        return imageWordEnum;
    }

    public ImageWordClient(Builder builder) {
        super(builder);
        this.imageWordEnum = builder.imageWordEnum;
    }

    public String imageWord(String imageBase64, String encoding) throws IOException {
        String signUrl = Signature.signHostDateAuthorization(hostUrl + serviceId, "POST", apiKey, apiSecret);
        return sendPost(signUrl, JSON, bodyParam(imageBase64, encoding));
    }

    private String bodyParam(String imageBase64, String encoding) {
        JsonObject jso = new JsonObject();
        jso.add("header", buildHeader());

        /** parameter **/
        JsonObject parameter = new JsonObject();
        JsonObject service = new JsonObject();
        service.add("result", buildResult());

        // 印刷文字多语种
        if (imageWordEnum.equals(ImageWordEnum.PRINTED_WORD)) {
            service.addProperty("category", "mix0");

            // 不是身份证识别 也不是印刷文字多语种识别
        } else if (!imageWordEnum.equals(ImageWordEnum.IDCARD)) {
            service.addProperty("template_list", imageWordEnum.getTemplateList());
        }
        parameter.add(serviceId, service);
        jso.add("parameter", parameter);

        /** payload **/
        JsonObject payload = new JsonObject();
        JsonObject inputImage1 = new JsonObject();
        inputImage1.addProperty("encoding", encoding);
        inputImage1.addProperty("image", imageBase64);
        inputImage1.addProperty("status",status);
        payload.add(imageWordEnum.getPayload(), inputImage1);
        jso.add("payload", payload);
        return jso.toString();
    }

    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/";

        private ImageWordEnum imageWordEnum;

        public Builder(String appId, String apiKey, String apiSecret, ImageWordEnum imageWordEnum) {
            super(HOST_URL, imageWordEnum.getServiceId(), appId, apiKey, apiSecret);
            this.imageWordEnum = imageWordEnum;
        }

        @Override
        public ImageWordClient build() {
            return new ImageWordClient(this);
        }
    }
}
