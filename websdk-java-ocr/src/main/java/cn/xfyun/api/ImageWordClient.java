package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.config.ImageWordEnum;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 营业执照识别  出租车发票识别  火车票识别  增值税发票识别  身份证识别  印刷文字识别  通用文字识别  通用文字识别（intsig）
 * <p>
 * 错误码链接：<a href="https://www.xfyun.cn/document/error-code">...</a> (code返回错误码时必看)
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 15:21
 */
public class ImageWordClient extends PlatformHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(ImageWordClient.class);

    private final ImageWordEnum imageWordEnum;

    public ImageWordEnum getImageWordEnum() {
        return imageWordEnum;
    }

    public ImageWordClient(Builder builder) {
        super(builder);
        this.imageWordEnum = builder.imageWordEnum;
    }

    /**
     * @param imageBase64   图片base64信息
     * @param format      编码
     * @return 返回结果
     * @throws IOException 异常信息
     */
    public String imageWord(String imageBase64, String format) throws IOException {
        // 获取鉴权后的URL
        String signUrl = Signature.signHostDateAuthorization(hostUrl + serviceId, "POST", apiKey, apiSecret);

        // 请求体
        String body = bodyParam(imageBase64, encoding);

        // 发送请求
        return sendPost(signUrl, JSON, body);
    }

    /**
     * 构建参数
     */
    private String bodyParam(String imageBase64, String encoding) {
        if (null == imageWordEnum) {
            throw new BusinessException("识别类型不能为空");
        }

        JsonObject jso = new JsonObject();
        jso.add("header", buildHeader());

        // parameter
        JsonObject parameter = new JsonObject();
        JsonObject service = new JsonObject();
        service.add("result", buildResult());

        if (imageWordEnum.equals(ImageWordEnum.COMMON_WORD)) {

            // 通用文字识别
            service.addProperty("category", "ch_en_public_cloud");

        } else if (imageWordEnum.equals(ImageWordEnum.PRINTED_WORD)) {

            // 印刷文字多语种
            service.addProperty("category", "mix0");

        } else {

            // others
            service.addProperty("template_list", imageWordEnum.getTemplateList());

        }

        parameter.add(serviceId, service);
        jso.add("parameter", parameter);

        // payload
        JsonObject payload = new JsonObject();
        JsonObject inputImage1 = new JsonObject();
        inputImage1.addProperty("encoding", encoding);
        inputImage1.addProperty("image", imageBase64);
        inputImage1.addProperty("status", status);
        payload.add(imageWordEnum.getPayload(), inputImage1);
        jso.add("payload", payload);

        logger.debug("{} 请求入参: {}", imageWordEnum.getName(), jso);
        return jso.toString();
    }

    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/";

        private final ImageWordEnum imageWordEnum;

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
