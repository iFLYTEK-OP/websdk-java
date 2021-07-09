package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.config.HttpRequestEnum;
import cn.xfyun.config.IdcardEnum;
import cn.xfyun.config.IntsigRecgEnum;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;

/**
 *      身份证识别 营业执照识别 增值税发票识别
 *
 *      错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 11:28
 */
public class IntsigOcrClient extends HttpClient {

    private IntsigRecgEnum intsigRecgEnum;

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

    public IntsigOcrClient(Builder builder) {
        super(builder);
        this.intsigRecgEnum = builder.intsigRecgEnum;
        this.headPortrait = builder.headPortrait;
        this.cropImage = builder.cropImage;
        this.idNumberImage = builder.idNumberImage;
        this.recognizeMode = builder.recognizeMode;
    }


    public String intsigRecg(String imageBase64) throws IOException {
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, buildParam(), HttpRequestEnum.FORM.getValue());
        return sendPost(hostUrl + intsigRecgEnum.getValue(), FORM, header, "image=" + imageBase64);
    }

    private String buildParam() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("engine_type", intsigRecgEnum.getValue());
        if (intsigRecgEnum.equals(IntsigRecgEnum.IDCARD)) {
            jsonObject.addProperty("head_portrait", headPortrait.getValue());
            jsonObject.addProperty("crop_image", cropImage.getValue());
            jsonObject.addProperty("id_number_image", idNumberImage.getValue());
            jsonObject.addProperty("recognize_mode", recognizeMode.getValue());
        }
        return jsonObject.toString();
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://webapi.xfyun.cn/v1/service/v1/ocr/";

        private IntsigRecgEnum intsigRecgEnum;

        private IdcardEnum headPortrait = IdcardEnum.OFF;

        private IdcardEnum cropImage = IdcardEnum.OFF;

        private IdcardEnum idNumberImage = IdcardEnum.OFF;

        private IdcardEnum recognizeMode = IdcardEnum.OFF;

        public Builder(String appId, String apiKey, IntsigRecgEnum intsigRecgEnum) {
            super(HOST_URL, appId, apiKey, null);
            this.intsigRecgEnum = intsigRecgEnum;
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
        public IntsigOcrClient build() {
            return new IntsigOcrClient(this);
        }
    }
}
