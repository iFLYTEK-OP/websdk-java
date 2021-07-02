package cn.xfyun.api;

import cn.xfyun.config.BaseBuilder;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 *    基于讯飞自研的活体检测算法，针对打印照、屏幕二次翻拍等作弊场景，基于图片中人像破绽及成像畸形，
 *    可有效识别目标是否为活体，并给出置信度参考。
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/1 15:19
 */
public class AntiSpoofClient extends HttpRequestClient{

    /**
     * 服务引擎ID 默认 s9a87e3ec
     */
    private String serviceId;

    /**
     *   请求状态，取值范围为：3（一次传完）
     */
    private int status;

    /**
     *    文本编码，可选值：utf8（默认值
     */
    private String encoding;

    /**
     *   文本压缩格式，可选值：raw（默认值）
     */
    private String compress;

    /**
     *    文本格式，可选值：json（默认值）
     */
    private String format;




    public AntiSpoofClient(AntiSpoofClient.Builder builder) {
        super(builder);
        this.serviceId = builder.serviceId;
        this.status = builder.status;
        this.encoding = builder.encoding;
        this.compress = builder.compress;
        this.format = builder.format;
    }

    public String faceContrast(String imageBase641) throws IOException {
        String signUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        return sendPost(signUrl, JSON, null, bodyParam(imageBase641));
    }

    public String bodyParam(String imageBase641) {
        JsonObject jso = new JsonObject();

        /** header **/
        JsonObject header = new JsonObject();
        header.addProperty("app_id", appId);
        header.addProperty("status", 3);

        jso.add("header", header);

        /** parameter **/
        JsonObject parameter = new JsonObject();
        JsonObject service = new JsonObject();
        service.addProperty("service_kind", "anti_spoof");

        JsonObject faceCompareResult = new JsonObject();
        faceCompareResult.addProperty("encoding", encoding);
        faceCompareResult.addProperty("format", format);
        faceCompareResult.addProperty("compress", compress);
        service.add("anti_spoof_result", faceCompareResult);
        parameter.add(serviceId, service);
        jso.add("parameter", parameter);

        /** payload **/
        JsonObject payload = new JsonObject();
        JsonObject inputImage1 = new JsonObject();
        inputImage1.addProperty("encoding", encoding);
        inputImage1.addProperty("image", imageBase641);
        payload.add("input1", inputImage1);

        jso.add("payload", payload);
        return jso.toString();
    }


    public class Builder extends BaseBuilder {

        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/s67c9c78c";

        private String serviceId = "s67c9c78c";

        private int status = 3;

        private String encoding = "utf8";

        private String compress = "raw";

        private String format = "json";


        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
        }

        public AntiSpoofClient.Builder serviceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public AntiSpoofClient.Builder status(int status) {
            this.status = status;
            return this;
        }

        public AntiSpoofClient.Builder encoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public AntiSpoofClient.Builder compress(String compress) {
            this.compress = compress;
            return this;
        }

        public AntiSpoofClient.Builder format(String format) {
            this.format = format;
            return this;
        }

        public AntiSpoofClient build() {
            AntiSpoofClient client = new AntiSpoofClient(this);
            return client;
        }
    }
}
