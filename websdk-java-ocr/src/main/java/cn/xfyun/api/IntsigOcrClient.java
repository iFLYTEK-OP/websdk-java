package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.config.IdcardEnum;
import cn.xfyun.config.IntsigRecgEnum;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * 身份证识别 营业执照识别 增值税发票识别  通用文字识别
 * <p>
 * 错误码链接：<a href="https://www.xfyun.cn/document/error-code">...</a> (code返回错误码时必看)
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 11:28
 */
public class IntsigOcrClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(IntsigOcrClient.class);

    private final IntsigRecgEnum intsigRecgEnum;

    /**
     * 是否返回头像图片：默认head_portrait=0，
     * 即不返回头像图片，head_portrait=1，则返回身份证头像照片（Base64编码）
     */
    private final IdcardEnum headPortrait;

    /**
     * 是否返回切片图，默认crop_image=0，1表示返回身份证切片照片（Base64编码）
     */
    private final IdcardEnum cropImage;

    /**
     * 是否返回身份证号码区域截图，默认id_number_image=0，即不返回身份号码区域截图，1表示返回证件号区域截图（Base64编码）
     */
    private final IdcardEnum idNumberImage;

    /**
     * 是否先对图片进行切片后再识别，默认recognize_mode=0，即直接对图片进行识别，1表示采用先切片后识别的模式
     */
    private final IdcardEnum recognizeMode;

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

    /**
     * @param imageBase64 图片base64
     * @return 返回结果
     * @throws IOException 请求异常
     */
    public String intsigRecg(String imageBase64) throws IOException {
        // 发送请求
        return intsigRecg(imageBase64, null);
    }

    /**
     * @param imageBase64 入参
     * @param format      入参
     * @return 返回结果
     * @throws IOException 异常信息
     */
    public String intsigRecg(String imageBase64, String format) throws IOException {
        // 参数校验
        paramCheck(imageBase64);

        // 参数初始化
        String body = buildParam(imageBase64, format);

        if (intsigRecgEnum.equals(IntsigRecgEnum.COMMON_WORD)) {
            // 获取鉴权URL
            String signUrl = Signature.signHostDateAuthorization(
                    Builder.COMMON_WORD_URL + intsigRecgEnum.getValue(), "POST", apiKey, apiSecret);
            return sendPost(signUrl, JSON, body);
        } else {
            // 获取鉴权头
            Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, body);
            return sendPost(hostUrl + intsigRecgEnum.getValue(), FORM, header, "image=" + imageBase64);
        }
    }

    /**
     * 参数校验
     */
    private void paramCheck(String imageBase64) {
        if (StringUtils.isNullOrEmpty(imageBase64)) {
            throw new BusinessException("需要识别的图片信息不能为空");
        } else if (null == this.intsigRecgEnum) {
            throw new BusinessException("识别类型不能为空");
        }
    }

    /**
     * 构建参数
     */
    private String buildParam(String imageBase64, String format) {
        JsonObject jsonObject = new JsonObject();
        if (intsigRecgEnum.equals(IntsigRecgEnum.IDCARD)) {
            jsonObject.addProperty("engine_type", intsigRecgEnum.getValue());
            jsonObject.addProperty("head_portrait", headPortrait.getValue());
            jsonObject.addProperty("crop_image", cropImage.getValue());
            jsonObject.addProperty("id_number_image", idNumberImage.getValue());
            jsonObject.addProperty("recognize_mode", recognizeMode.getValue());
        } else if (intsigRecgEnum.equals(IntsigRecgEnum.COMMON_WORD)) {
            // header
            JsonObject header = new JsonObject();
            header.addProperty("app_id", appId);
            header.addProperty("status", 3);
            jsonObject.add("header", header);

            // parameter
            JsonObject parameter = new JsonObject();
            JsonObject service = new JsonObject();
            JsonObject param = new JsonObject();
            param.addProperty("encoding", "utf8");
            param.addProperty("format", "json");
            param.addProperty("compress", "raw");
            service.add("recognizeDocumentRes", param);
            parameter.add(intsigRecgEnum.getValue(), service);
            jsonObject.add("parameter", parameter);

            // payload
            JsonObject payload = new JsonObject();
            JsonObject inputImage1 = new JsonObject();
            inputImage1.addProperty("encoding", format);
            inputImage1.addProperty("image", imageBase64);
            inputImage1.addProperty("status", 3);
            payload.add("image", inputImage1);
            jsonObject.add("payload", payload);
        } else {
            jsonObject.addProperty("engine_type", intsigRecgEnum.getValue());
        }

        logger.debug("intsig{} 请求参数：{}", intsigRecgEnum.getName(), jsonObject);
        return jsonObject.toString();
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://webapi.xfyun.cn/v1/service/v1/ocr/";

        private static final String COMMON_WORD_URL = "https://api.xf-yun.com/v1/private/";

        private final IntsigRecgEnum intsigRecgEnum;

        private IdcardEnum headPortrait = IdcardEnum.OFF;

        private IdcardEnum cropImage = IdcardEnum.OFF;

        private IdcardEnum idNumberImage = IdcardEnum.OFF;

        private IdcardEnum recognizeMode = IdcardEnum.OFF;

        public Builder(String appId, String apiKey, IntsigRecgEnum intsigRecgEnum) {
            super(HOST_URL, appId, apiKey, null);
            this.intsigRecgEnum = intsigRecgEnum;
        }

        public Builder(String appId, String apiKey, String apiSecret, IntsigRecgEnum intsigRecgEnum) {
            super(HOST_URL, appId, apiKey, apiSecret);
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
