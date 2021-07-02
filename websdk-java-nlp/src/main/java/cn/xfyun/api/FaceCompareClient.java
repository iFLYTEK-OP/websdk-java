package cn.xfyun.api;

import cn.xfyun.config.BaseBuilder;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;
import okhttp3.internal.Util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *     基于讯飞自研的人脸算法，可实现对比两张照片中的人脸信息，判断是否是同一个人并返回相似度得分
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/1 13:53
 */
public class FaceCompareClient extends HttpRequestClient {

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



    public FaceCompareClient(FaceCompareClient.Builder builder) {
        super(builder);
        this.serviceId = builder.serviceId;
        this.status = builder.status;
        this.encoding = builder.encoding;
        this.compress = builder.compress;
        this.format = builder.format;
    }


    public String faceContrast(String imageBase641, String encoding1, String imageBase642, String encoding2) throws IOException {
        String signUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        return sendPost(signUrl, JSON, null, bodyParam(imageBase641, encoding1, imageBase642, encoding2));
    }

    public String bodyParam(String imageBase641, String imageEncoding1, String imageBase642, String imageEncoding2) {
        JsonObject jso = new JsonObject();

        /** header **/
        JsonObject header = new JsonObject();
        header.addProperty("app_id", appId);
        header.addProperty("status", 3);

        jso.add("header", header);

        /** parameter **/
        JsonObject parameter = new JsonObject();
        JsonObject service = new JsonObject();
        service.addProperty("service_kind", "face_compare");
        JsonObject faceCompareResult = new JsonObject();
        faceCompareResult.addProperty("encoding", encoding);
        faceCompareResult.addProperty("format", format);
        faceCompareResult.addProperty("compress", compress);
        service.add("face_compare_result", faceCompareResult);
        parameter.add(serviceId, service);
        jso.add("parameter", parameter);

        /** payload **/
        JsonObject payload = new JsonObject();
        JsonObject inputImage1 = new JsonObject();
        inputImage1.addProperty("encoding", imageEncoding1);
        inputImage1.addProperty("image", imageBase641);
        payload.add("input1", inputImage1);

        JsonObject inputImage2 = new JsonObject();
        inputImage2.addProperty("encoding", imageEncoding2);
        inputImage2.addProperty("image", imageBase642);
        payload.add("input2", inputImage2);
        jso.add("payload", payload);
        return jso.toString();
    }

    public static final class Builder extends BaseBuilder {

        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/s67c9c78c";

        private String serviceId = "s67c9c78c";

        private int status = 3;

        private String encoding = "utf8";

        private String compress = "raw";

        private String format = "json";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
        }

        public FaceCompareClient.Builder url(String url) {
            this.hostUrl(url);
            return this;
        }

        public FaceCompareClient.Builder serviceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public FaceCompareClient.Builder status(int status) {
            this.status = status;
            return this;
        }

        public FaceCompareClient.Builder encoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public FaceCompareClient.Builder compress(String compress) {
            this.compress = compress;
            return this;
        }

        public FaceCompareClient.Builder format(String format) {
            this.format = format;
            return this;
        }

        public FaceCompareClient build() {
            FaceCompareClient client = new FaceCompareClient(this);
            return client;
        }
    }
}
