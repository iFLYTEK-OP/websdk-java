package cn.xfyun.api;

import cn.xfyun.config.BaseBuilder;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 *    人脸检测和属性分析 API
 *
 *    基于讯飞自研的人脸检测算法，对图片中的人脸进行精准定位并标记，分析人脸性别、表情、口罩等属性信息。
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/1 16:07
 */
public class FaceDetectClient extends HttpRequestClient{

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


    /**
     * 检测特征点开关
     * 0:只检测人脸，不检测特征点
     * 1:检测到人脸之后检测特征点
     */
    private int detectPoints;

    /**
     * 检测人脸属性开关
     * 0:不检测人脸属性
     * 1:检测人脸属性
     */
    private int detectProperty;



    public FaceDetectClient(FaceDetectClient.Builder builder) {
        super(builder);
        this.serviceId = builder.serviceId;
        this.status = builder.status;
        this.encoding = builder.encoding;
        this.compress = builder.compress;
        this.format = builder.format;
        this.detectPoints = builder.detectPoints;
        this.detectProperty = builder.detectProperty;
    }

    public String faceContrast(String imageBase641, String encoding) throws IOException {
        String signUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        return sendPost(signUrl, JSON, null, bodyParam(imageBase641, encoding));
    }

    public String bodyParam(String imageBase641, String imageEncoding1) {
        JsonObject jso = new JsonObject();

        /** header **/
        JsonObject header = new JsonObject();
        header.addProperty("app_id", appId);
        header.addProperty("status", status);

        jso.add("header", header);

        /** parameter **/
        JsonObject parameter = new JsonObject();
        JsonObject service = new JsonObject();
        service.addProperty("service_kind", "face_detect");
        //检测特征点
        service.addProperty("detect_points", detectPoints);
        //检测人脸属性
        service.addProperty("detect_property", detectProperty);

        JsonObject faceCompareResult = new JsonObject();
        faceCompareResult.addProperty("encoding", encoding);
        faceCompareResult.addProperty("format", format);
        faceCompareResult.addProperty("compress", compress);
        service.add("face_detect_result", faceCompareResult);
        parameter.add(serviceId, service);
        jso.add("parameter", parameter);

        /** payload **/
        JsonObject payload = new JsonObject();
        JsonObject inputImage1 = new JsonObject();
        inputImage1.addProperty("encoding", imageEncoding1);
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

        private int detectPoints = 0;

        private int detectProperty = 0;

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
        }

        public FaceDetectClient.Builder url(String url) {
            this.url(url);
            return this;
        }

        public FaceDetectClient.Builder serviceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public FaceDetectClient.Builder status(int status) {
            this.status = status;
            return this;
        }

        public FaceDetectClient.Builder encoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public FaceDetectClient.Builder compress(String compress) {
            this.compress = compress;
            return this;
        }

        public FaceDetectClient.Builder format(String format) {
            this.format = format;
            return this;
        }

        public FaceDetectClient.Builder detectPoints(int detectPoints) {
            this.detectPoints = detectPoints;
            return this;
        }

        public FaceDetectClient.Builder detectProperty(int detectProperty) {
            this.detectProperty = detectProperty;
            return this;
        }

        public FaceDetectClient build() {
            FaceDetectClient client = new FaceDetectClient(this);
            return client;
        }
    }

}
