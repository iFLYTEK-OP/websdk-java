package cn.xfyun.api;

import cn.xfyun.base.http.HttpRequestBuilder;
import cn.xfyun.base.http.HttpRequestClient;
import cn.xfyun.config.IdcardEnum;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;

/**
 *      身份证识别
 *
 *    身份证识别，通过OCR（光学字符识别 Optical Character Recognition）技术，对身份证正反面图片进行识别，
 *    返回身份证图片上的姓名、民族、住址、身份证号、签发机关和有效期等信息，可以省去用户手动录入的过程，
 *    自动完成身份证信息的结构化和图像数据的采集，可以很方便对接客户的后台数据系统，给用户带来极大的便利。
 *    采用特有的图像处理技术，在识别身份证图片过程中，还可以对身份证图片进行切边矫正，去除背景图片，并
 *    可以获取身份证图片上的头像，方便用户保存。不过请注意， 不支持同时识别身份证正反面，正反面需分开在不同的图片进行识别
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 11:28
 */
public class IdcardClient extends HttpRequestClient {

    /**
     *   是否返回头像图片：默认head_portrait=0，
     *   即不返回头像图片，head_portrait=1，则返回身份证头像照片（Base64编码）
     */
    private IdcardEnum headPortrait;

    /**
     *   是否返回切片图，默认crop_image=0，1表示返回身份证切片照片（Base64编码）
     */
    private IdcardEnum cropImage;

    /**
     *   是否返回身份证号码区域截图，默认id_number_image=0，即不返回身份号码区域截图，1表示返回证件号区域截图（Base64编码）
     */
    private IdcardEnum idNumberImage;

    /**
     *   是否先对图片进行切片后再识别，默认recognize_mode=0，即直接对图片进行识别，1表示采用先切片后识别的模式
     */
    private IdcardEnum recognizeMode;


    public IdcardEnum getHeadPortrait() {
        return headPortrait;
    }

    public IdcardEnum getCropImage() {
        return cropImage;
    }

    public IdcardEnum getIdNumberImage() {
        return idNumberImage;
    }

    public IdcardEnum getRecognizeMode() {
        return recognizeMode;
    }

    public IdcardClient(Builder builder) {
        super(builder);
        this.headPortrait = builder.headPortrait;
        this.cropImage = builder.cropImage;
        this.idNumberImage = builder.idNumberImage;
        this.recognizeMode = builder.recognizeMode;
    }


    public String idcardRecognition(String imageBase64) throws IOException {
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, buildParam(), null);
        return sendPost(hostUrl, FORM, header, "image=" + imageBase64);
    }

    private String buildParam() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("engine_type", "idcard");
        jsonObject.addProperty("head_portrait", headPortrait.getValue());
        jsonObject.addProperty("crop_image", cropImage.getValue());
        jsonObject.addProperty("id_number_image", idNumberImage.getValue());
        jsonObject.addProperty("recognize_mode", recognizeMode.getValue());
        return jsonObject.toString();
    }

    public static final class Builder extends HttpRequestBuilder<Builder> {

        private static final String HOST_URL = "https://webapi.xfyun.cn/v1/service/v1/ocr/idcard";

        private IdcardEnum headPortrait = IdcardEnum.OFF;

        private IdcardEnum cropImage = IdcardEnum.OFF;

        private IdcardEnum idNumberImage = IdcardEnum.OFF;

        private IdcardEnum recognizeMode = IdcardEnum.OFF;

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        public Builder headPortrait(IdcardEnum idcardEnum) {
            this.headPortrait = idcardEnum;
            return this;
        }

        public Builder cropImage(IdcardEnum idcardEnum) {
            this.cropImage = idcardEnum;
            return this;
        }

        public Builder idNumberImage(IdcardEnum idcardEnum) {
            this.idNumberImage = idcardEnum;
            return this;
        }

        public Builder recognizeMode(IdcardEnum idcardEnum) {
            this.recognizeMode = idcardEnum;
            return this;
        }

        @Override
        public IdcardClient build() {
            return new IdcardClient(this);
        }
    }
}
