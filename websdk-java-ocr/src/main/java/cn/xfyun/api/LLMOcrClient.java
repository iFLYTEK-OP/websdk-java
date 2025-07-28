package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.llmocr.LLMOcrParam;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * （OCRforLLM）通用文档识别(大模型) Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/words/OCRforLLM/API.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class LLMOcrClient extends PlatformHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(LLMOcrClient.class);

    /**
     * 设备imei信息
     * 最大长度 50
     */
    private final String imei;
    /**
     * 设备imsi信息
     * 最大长度 50
     */
    private final String imsi;
    /**
     * 设备mac信息
     * 最大长度 50
     */
    private final String mac;
    /**
     * 网络类型
     * 可选值为wifi、2G、3G、4G、5G
     */
    private final String netType;
    /**
     * 运营商信息
     * 可选值为CMCC、CUCC、CTCC、other
     */
    private final String netIsp;
    /**
     * 个性化资源ID
     * 最大长度 1024
     */
    private final String resId;
    /**
     * 设备mac信息
     * 最大长度 50
     */
    private final String resultOption;
    /**
     * 设备mac信息
     * 最大长度 50
     */
    private final String resultFormat;
    /**
     * 设备mac信息
     * 最大长度 50
     */
    private final String outputType;
    /**
     * 设备mac信息
     * 最大长度 50
     */
    private final String exifOption;
    /**
     * (保留字段暂不支持)默认为空字符串，所有要素均输出；
     * 针对每个要素有特殊需求时可使用本参数进行设定 输入格式为: “element_name1=value1,element_name2=value2”
     * 最大长度1000
     */
    private final String jsonElementOption;
    /**
     * 默认为空字符串，所有要素均输出；
     * 针对每个要素有特殊需求时可使用本参数进行设定，
     * 输入格式为: “element_name1=value1,element_name2=value2”
     * 其中element_name可选值有：seal:印章，information_bar:信息栏，fingerprint:手印，qrcode:二维码，watermark:水印，barcode:条形码，page_header:页眉 ，page_footer:页脚，page_number:页码，layout:版面，title:标题，region:区域，paragraph:段落，textline:文本行，table:表格，graph:插图，list:列表，pseudocode:伪代码，code:代码，footnote:脚注，formula:公式；value值可选值有：0:不输出，1:输出，默认值；
     * 说明：当element_name为table时，1表示同时识别有线表和少线表，2表示只识别有线表。
     * 最大长度1000
     */
    private final String markdownElementOption;
    /**
     * 默认为空字符串，所有要素均输出；
     * 针对每个要素有特殊需求时可使用本参数进行设定，
     * 输入格式为: “element_name1=value1,element_name2=value2”
     * 其中element_name可选值有：seal:印章，information_bar:信息栏，fingerprint:手印，qrcode:二维码，watermark:水印，barcode:条形码，page_header:页眉 ，page_footer:页脚，page_number:页码，layout:版面，title:标题，region:区域，paragraph:段落，textline:文本行，table:表格，graph:插图，list:列表，pseudocode:伪代码，code:代码，footnote:脚注，formula:公式；value值可选值有：0:不输出，1:输出，默认值；
     * 说明：当element_name为table时，1表示同时识别有线表和少线表，2表示只识别有线表。
     * 最大长度1000
     */
    private final String sedElementOption;
    /**
     * 设备mac信息
     * 最大长度 50
     */
    private final String alphaOption;
    /**
     * 设备mac信息
     * 最大长度 50
     */
    private final Float rotationMinAngle;

    public String getImei() {
        return imei;
    }

    public String getImsi() {
        return imsi;
    }

    public String getMac() {
        return mac;
    }

    public String getNetType() {
        return netType;
    }

    public String getNetIsp() {
        return netIsp;
    }

    public String getResultOption() {
        return resultOption;
    }

    public String getResultFormat() {
        return resultFormat;
    }

    public String getOutputType() {
        return outputType;
    }

    public String getExifOption() {
        return exifOption;
    }

    public String getAlphaOption() {
        return alphaOption;
    }

    public Float getRotationMinAngle() {
        return rotationMinAngle;
    }

    public String getResId() {
        return resId;
    }

    public String getMarkdownElementOption() {
        return markdownElementOption;
    }

    public String getSedElementOption() {
        return sedElementOption;
    }

    public String getJsonElementOption() {
        return jsonElementOption;
    }

    public LLMOcrClient(Builder builder) {
        super(builder);
        this.imei = builder.imei;
        this.imsi = builder.imsi;
        this.mac = builder.mac;
        this.netType = builder.netType;
        this.netIsp = builder.netIsp;
        this.resultOption = builder.resultOption;
        this.resultFormat = builder.resultFormat;
        this.outputType = builder.outputType;
        this.exifOption = builder.exifOption;
        this.alphaOption = builder.alphaOption;
        this.rotationMinAngle = builder.rotationMinAngle;
        this.resId = builder.resId;
        this.markdownElementOption = builder.markdownElementOption;
        this.sedElementOption = builder.sedElementOption;
        this.jsonElementOption = builder.jsonElementOption;
    }

    /**
     * @param param   请求参数
     * @return 返回结果
     * @throws IOException 异常信息
     */
    public String send(LLMOcrParam param) throws IOException {
        // 请求体
        String body = bodyParam(param);

        // 获取鉴权后的URL
        String signUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);

        // 发送请求
        return sendPost(signUrl, JSON, body);
    }

    /**
     * 参数校验
     */
    private void paramCheck(LLMOcrParam param) {
        if (null == param) {
            throw new BusinessException("参数不能为空");
        }
        param.selfCheck();
    }

    /**
     * 构建参数
     */
    private String bodyParam(LLMOcrParam param) {
        // 参数校验
        paramCheck(param);

        // 构建请求对象
        JsonObject body = new JsonObject();

        // 请求头
        body.add("header", getHeader(param));

        // 功能参数
        body.add("parameter", getParameter());

        // 请求数据
        body.add("payload", getPayload(param));

        logger.debug("通用大模型文档还原请求入参: {}", body);
        return StringUtils.gson.toJson(body);
    }

    private JsonObject getHeader(LLMOcrParam param) {
        JsonObject header = buildHeader();
        header.addProperty("uid", param.getUid());
        header.addProperty("did", param.getDid());
        header.addProperty("imei", imei);
        header.addProperty("imsi", imsi);
        header.addProperty("mac", mac);
        header.addProperty("net_type", netType);
        header.addProperty("net_isp", netIsp);
        header.addProperty("request_id", param.getRequestId());
        header.addProperty("res_id", resId);
        return header;
    }

    private JsonObject getParameter() {
        JsonObject parameter = new JsonObject();
        JsonObject ocr = new JsonObject();
        ocr.addProperty("result_option", resultOption);
        ocr.addProperty("result_format", resultFormat);
        ocr.addProperty("output_type", outputType);
        ocr.addProperty("exif_option", exifOption);
        ocr.addProperty("json_element_option", jsonElementOption);
        ocr.addProperty("markdown_element_option", markdownElementOption);
        ocr.addProperty("sed_element_option", sedElementOption);
        ocr.addProperty("alpha_option", alphaOption);
        ocr.addProperty("rotation_min_angle", rotationMinAngle);
        ocr.add("result", buildResult());
        parameter.add("ocr", ocr);
        return parameter;
    }

    private JsonObject getPayload(LLMOcrParam param) {
        JsonObject payload = new JsonObject();
        JsonObject image = new JsonObject();
        image.addProperty("encoding", param.getFormat());
        image.addProperty("image", param.getImageBase64());
        image.addProperty("status", status);
        image.addProperty("seq", 0);
        payload.add("image", image);
        return payload;
    }

    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://cbm01.cn-huabei-1.xf-yun.com/v1/private/se75ocrbm";
        private static final String SERVICE_ID = "se75ocrbm";
        private String imei;
        private String imsi;
        private String mac;
        private String netType;
        private String netIsp;
        private String resId;
        private String resultOption;
        private String resultFormat;
        private String outputType;
        private String exifOption;
        private String jsonElementOption;
        private String markdownElementOption;
        private String sedElementOption;
        private String alphaOption;
        private Float rotationMinAngle;

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVICE_ID, appId, apiKey, apiSecret);
            this.status(2);
        }

        @Override
        public LLMOcrClient build() {
            return new LLMOcrClient(this);
        }

        public Builder imei(String imei) {
            this.imei = imei;
            return this;
        }

        public Builder imsi(String imsi) {
            this.imsi = imsi;
            return this;
        }

        public Builder mac(String mac) {
            this.mac = mac;
            return this;
        }

        public Builder netType(String netType) {
            this.netType = netType;
            return this;
        }

        public Builder netIsp(String netIsp) {
            this.netIsp = netIsp;
            return this;
        }

        public Builder resId(String resId) {
            this.resId = resId;
            return this;
        }

        public Builder resultOption(String resultOption) {
            this.resultOption = resultOption;
            return this;
        }

        public Builder resultFormat(String resultFormat) {
            this.resultFormat = resultFormat;
            return this;
        }

        public Builder outputType(String outputType) {
            this.outputType = outputType;
            return this;
        }

        public Builder exifOption(String exifOption) {
            this.exifOption = exifOption;
            return this;
        }

        public Builder jsonElementOption(String jsonElementOption) {
            this.jsonElementOption = jsonElementOption;
            return this;
        }

        public Builder markdownElementOption(String markdownElementOption) {
            this.markdownElementOption = markdownElementOption;
            return this;
        }

        public Builder sedElementOption(String sedElementOption) {
            this.sedElementOption = sedElementOption;
            return this;
        }

        public Builder alphaOption(String alphaOption) {
            this.alphaOption = alphaOption;
            return this;
        }

        public Builder rotationMinAngle(Float rotationMinAngle) {
            this.rotationMinAngle = rotationMinAngle;
            return this;
        }
    }
}
