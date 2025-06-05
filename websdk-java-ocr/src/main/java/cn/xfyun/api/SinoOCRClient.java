package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.model.ticket.request.SinoOCRRequest;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 国内通用票证识别sinosecu Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/words/invoiceIdentification/API.html">...</a>
 *
 * @author zyding6
 */
public class SinoOCRClient extends PlatformHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(SinoOCRClient.class);

    public SinoOCRClient(Builder builder) {
        super(builder);
    }

    /**
     * @param imgBase64 图片base64编码
     * @param imgFormat 图片格式
     * @return 返回结果
     * @throws IOException 请求异常信息
     */
    public String send(String imgBase64, String imgFormat) throws IOException {
        if (StringUtils.isNullOrEmpty(imgBase64)) {
            throw new BusinessException("图片内容不能为空");
        }

        // 构建签名URL
        String signUrl = Signature.signHostDateAuthorization(
                hostUrl + serviceId, "POST", apiKey, apiSecret);

        return sendPost(signUrl, JSON, bodyParam(imgBase64, imgFormat));
    }

    /**
     * 构建请求参数
     */
    private String bodyParam(String imgBase64, String imgFormat) {
        // 创建请求实体类
        SinoOCRRequest request = new SinoOCRRequest();

        // 请求头
        SinoOCRRequest.Header header = new SinoOCRRequest.Header();
        header.setAppId(appId);
        header.setStatus(status);
        request.setHeader(header);

        // 请求参数
        SinoOCRRequest.Parameter parameter = new SinoOCRRequest.Parameter(this);
        request.setParameter(parameter);

        // 请求体
        SinoOCRRequest.Payload payload = new SinoOCRRequest.Payload();
        SinoOCRRequest.Payload.Image image = new SinoOCRRequest.Payload.Image();
        image.setImage(imgBase64);
        image.setStatus(status);
        image.setEncoding(imgFormat);
        payload.setImage(image);
        request.setPayload(payload);

        String json = StringUtils.gson.toJson(request);
        logger.debug("通用票证sinosecu识别请求入参: {}", json);
        return json;
    }

    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/";

        private static final String SERVICE_ID = "sc45f0684";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVICE_ID, appId, apiKey, apiSecret);
            this.readTimeout(30);
        }

        @Override
        public SinoOCRClient build() {
            return new SinoOCRClient(this);
        }
    }
}
