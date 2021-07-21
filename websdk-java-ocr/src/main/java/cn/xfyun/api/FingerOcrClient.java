package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 *     指尖文字识别
 *
 *  指尖文字识别，可检测图片中指尖位置，将指尖处文字转化为计算机可编码的文字。
 *
 *  错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
 *
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 14:50
 */
public class FingerOcrClient extends HttpClient {

    /**
     *   根据指尖位置选取ROI（感兴趣区域）的宽度倍数，
     *   即设置ROI的宽度是手指宽度的几倍（宽度= cut_w_scale * 手指宽度），默认3.0，取值范围：[0,65536]
     */
    private float cutWScale;

    /**
     *  根据指尖位置选取ROI（感兴趣区域）的高度倍数，
     *  即设置ROI的高度是手指宽度的几倍（高度= cut_h_scale * 手指宽度），默认2.0，取值范围：[0,65536]
     */
    private float cutHScale;

    /**
     *  根据指尖位置选取ROI（感兴趣区域）的往下平移的倍数，
     *  即设置ROI往下平移的距离是ROI宽度的几倍（平移量= cut_shift * 手指宽度），默认0.3，取值范围：[0,1]
     */
    private float cutShift;

    /**
     *  引擎内部处理模块输入图像宽度，取值范围：[1,65536]。
     *  若应用端上传图像宽为input_w，scale为缩放系数，则resize_w=input_w*scale。
     *  若不缩放直接按原图处理，引擎耗时会变长，建议根据实际情况测试以寻求最佳值
     */
    private int resizeW;

    /**
     *  引擎内部处理模块输入图像高度，取值范围：[1,65536]。
     *  若应用端上传图像高为input_h，scale为缩放系数，则resize_h=input_h*scale。
     *  若不缩放直接按原图处理，引擎耗时会变长，建议根据实际情况测试以寻求最佳值
     */
    private int resizeH;

    public float getCutWScale() {
        return cutWScale;
    }

    public float getCutHScale() {
        return cutHScale;
    }

    public float getCutShift() {
        return cutShift;
    }

    public int getResizeW() {
        return resizeW;
    }

    public int getResizeH() {
        return resizeH;
    }

    public FingerOcrClient(Builder builder) {
        super(builder);
        this.cutHScale = builder.cutHScale;
        this.cutWScale = builder.cutWScale;
        this.cutShift = builder.cutShift;
        this.resizeH = builder.resizeH;
        this.resizeW = builder.resizeW;
    }

    public String fingerOcr(String imageBase64) throws Exception {
        String body = buildHttpBody(imageBase64);
        Map<String, String> header = Signature.signHttpHeaderDigest(hostUrl, "POST", apiKey, apiSecret, body);
        return sendPost(hostUrl, JSON, header, body);
    }

    private String buildHttpBody(String imageBase64) {
        JsonObject body = new JsonObject();
        JsonObject business = new JsonObject();
        JsonObject common = new JsonObject();
        JsonObject data = new JsonObject();
        //填充common
        common.addProperty("app_id", appId);
        //填充business
        business.addProperty("ent", "fingerocr");
        business.addProperty("mode", "finger+ocr");
        business.addProperty("method", "dynamic");
        business.addProperty("cut_w_scale", cutWScale);
        business.addProperty("cut_h_scale", cutHScale);
        business.addProperty("cut_shift", cutShift);
        business.addProperty("resize_w", resizeW);
        business.addProperty("resize_h", resizeH);
        //填充data
        data.addProperty("image", imageBase64);

        //填充body
        body.add("common", common);
        body.add("business", business);
        body.add("data", data);

        return body.toString();
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://tyocr.xfyun.cn/v2/ocr";

        private float cutWScale = 3.0F;

        private float cutHScale = 2.0F;

        private float cutShift = 0.3F;

        private int resizeW = 1088;

        private int resizeH = 1632;


        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
        }

        public Builder cutWScale(float cutWScale) {
            this.cutWScale = cutWScale;
            return this;
        }

        public Builder cutHScale(float cutHScale) {
            this.cutHScale = cutHScale;
            return this;
        }

        public Builder cutShift(float cutShift) {
            this.cutShift = cutShift;
            return this;
        }

        public Builder resizeW(int resizeW) {
            this.resizeW = resizeW;
            return this;
        }

        public Builder resizeH(int resizeH) {
            this.resizeH = resizeH;
            return this;
        }

        @Override
        public FingerOcrClient build() {
            return new FingerOcrClient(this);
        }
    }
}
