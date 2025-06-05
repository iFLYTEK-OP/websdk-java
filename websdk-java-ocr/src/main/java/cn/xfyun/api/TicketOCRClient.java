package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.config.DocumentType;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.model.ticket.TicketOCRParam;
import cn.xfyun.model.ticket.request.TicketOCRRequest;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 票据卡证识别 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/words/TicketIdentification/API.html">...</a>
 *
 * @author zyding6
 */
public class TicketOCRClient extends PlatformHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(TicketOCRClient.class);

    public TicketOCRClient(Builder builder) {
        super(builder);
    }

    /**
     * @param param 票证识别请求入参
     * @return 返回结果
     * @throws IOException 请求异常信息
     */
    public String send(TicketOCRParam param) throws IOException {
        // 参数校验
        paramCheck(param);

        // 构建签名URL
        String signUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);

        // 发送请求
        return sendPost(signUrl, JSON, bodyParam(param));
    }

    /**
     * 参数校验
     */
    private void paramCheck(TicketOCRParam param) {
        if (null == param) {
            throw new BusinessException("请求参数不能为空");
        }
        param.selfCheck();
    }

    /**
     * 构建请求参数
     */
    private String bodyParam(TicketOCRParam param) {
        DocumentType documentType = param.getDocumentType();
        // 创建请求实体类
        TicketOCRRequest request = new TicketOCRRequest();

        // 请求头
        TicketOCRRequest.Header header = new TicketOCRRequest.Header();
        header.setAppId(appId);
        header.setUid(param.getUid());
        header.setStatus(status);
        request.setHeader(header);

        // 请求参数
        TicketOCRRequest.Parameter parameter = new TicketOCRRequest.Parameter(this);
        parameter.getOcr().setType(documentType.getType());
        parameter.getOcr().setLevel(param.getLevel());
        request.setParameter(parameter);

        // 请求体
        TicketOCRRequest.Payload payload = new TicketOCRRequest.Payload();
        TicketOCRRequest.Payload.Image image = new TicketOCRRequest.Payload.Image();
        image.setImage(param.getImageBase64());
        image.setStatus(status);
        image.setEncoding(param.getImageFormat());
        payload.setImage(image);
        request.setPayload(payload);

        String json = StringUtils.gson.toJson(request);
        logger.debug("{} 识别请求入参: {}", documentType.getDesc(), json);
        logger.info("{} 识别开始...", documentType.getDesc());
        return json;
    }

    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://cn-huadong-1.xf-yun.com/v1/inv";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, null, appId, apiKey, apiSecret);
            this.readTimeout(30);
        }

        @Override
        public TicketOCRClient build() {
            return new TicketOCRClient(this);
        }
    }
}
