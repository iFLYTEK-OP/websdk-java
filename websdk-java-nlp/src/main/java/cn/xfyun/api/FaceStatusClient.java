package cn.xfyun.api;

import cn.xfyun.config.BaseBuilder;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.HttpConnector;
import com.google.gson.JsonObject;
import okhttp3.internal.Util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *    配合式活体检测
 *
 *    基于讯飞自研的人脸算法，可以通过用户提供的图片，对图片内人物眼睛是否睁开进行判断，并且返回得分。
 *    该能力是通过HTTP API的方式给开发者提供一个通用的接口。
 *    HTTP API适用于一次性交互数据传输的AI服务场景，块式传输。相较于SDK，API具有轻量、跨语言的特点。
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/1 15:46
 */
public class FaceStatusClient extends HttpRequestClient{

    /**
     * 服务引擎ID 默认 s67c9c78c
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



    public FaceStatusClient(FaceStatusClient.Builder builder) {
        super(builder);
        this.serviceId = builder.serviceId;
        this.status = builder.status;
        this.encoding = builder.encoding;
        this.compress = builder.compress;
        this.format = builder.format;
    }


    public String faceContrast(String imageBase641, String encoding) throws IOException {
        String signUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        return sendPost(signUrl, JSON, null, bodyParam(imageBase641, encoding));
    }

    public String bodyParam(String imageBase641, String encoding) {
        JsonObject jso = new JsonObject();

        /** header **/
        JsonObject header = new JsonObject();
        header.addProperty("app_id", appId);
        header.addProperty("status", status);

        jso.add("header", header);

        /** parameter **/
        JsonObject parameter = new JsonObject();
        JsonObject service = new JsonObject();
        service.addProperty("service_kind", "face_status");

        JsonObject faceCompareResult = new JsonObject();
        faceCompareResult.addProperty("encoding", encoding);
        faceCompareResult.addProperty("format", format);
        faceCompareResult.addProperty("compress", compress);
        service.add("face_status_result", faceCompareResult);
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

        public FaceStatusClient.Builder url(String url) {
            this.hostUrl(url);
            return this;
        }

        public FaceStatusClient.Builder serviceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public FaceStatusClient.Builder status(int status) {
            this.status = status;
            return this;
        }

        public FaceStatusClient.Builder encoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public FaceStatusClient.Builder compress(String compress) {
            this.compress = compress;
            return this;
        }

        public FaceStatusClient.Builder format(String format) {
            this.format = format;
            return this;
        }


        public FaceStatusClient build() {
            FaceStatusClient client = new FaceStatusClient(this);
            return client;
        }
    }

}
