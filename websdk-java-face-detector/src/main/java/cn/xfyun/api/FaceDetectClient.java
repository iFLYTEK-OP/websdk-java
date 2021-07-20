package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.config.FaceDetectEnum;
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
public class FaceDetectClient extends PlatformHttpClient {

    /**
     * 检测特征点开关
     * 0:只检测人脸，不检测特征点
     * 1:检测到人脸之后检测特征点
     */
    private FaceDetectEnum detectPoints;

    /**
     * 检测人脸属性开关
     * 0:不检测人脸属性
     * 1:检测人脸属性
     */
    private FaceDetectEnum detectProperty;


    public FaceDetectEnum getDetectPoints() {
        return detectPoints;
    }

    public FaceDetectEnum getDetectProperty() {
        return detectProperty;
    }

    public FaceDetectClient(FaceDetectClient.Builder builder) {
        super(builder);
        this.detectPoints = builder.detectPoints;
        this.detectProperty = builder.detectProperty;
    }

    public String faceContrast(String imageBase641, String encoding) throws IOException {
        String signUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        return sendPost(signUrl, JSON, null, bodyParam(imageBase641, encoding));
    }

    public String bodyParam(String imageBase641, String imageEncoding1) {
        JsonObject jso = new JsonObject();
        jso.add("header", buildHeader());
        /** parameter **/
        JsonObject parameter = new JsonObject();
        JsonObject service = new JsonObject();
        service.addProperty("service_kind", "face_detect");
        //检测特征点
        service.addProperty("detect_points", detectPoints.getValue());
        //检测人脸属性
        service.addProperty("detect_property", detectProperty.getValue());


        service.add("face_detect_result", buildResult());
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

    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/s67c9c78c";

        private static final String SERVICE_ID = "s67c9c78c";

        private FaceDetectEnum detectPoints = FaceDetectEnum.OFF;

        private FaceDetectEnum detectProperty = FaceDetectEnum.OFF;

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVICE_ID, appId, apiKey, apiSecret);
        }

        public FaceDetectClient.Builder detectPoints(FaceDetectEnum detectPoints) {
            this.detectPoints = detectPoints;
            return this;
        }

        public FaceDetectClient.Builder detectProperty(FaceDetectEnum detectProperty) {
            this.detectProperty = detectProperty;
            return this;
        }

        @Override
        public FaceDetectClient build() {
            FaceDetectClient client = new FaceDetectClient(this);
            return client;
        }
    }

}
