package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.resume.ResumeRequest;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

/**
 * 简历生成 Client
 *
 * @author zyding6
 */
public class ResumeGenClient extends PlatformHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(ResumeGenClient.class);

    /**
     * 是否打印日志
     */
    private final boolean logRequest;

    public ResumeGenClient(Builder builder) {
        super(builder);
        this.logRequest = builder.logRequest;
    }

    public boolean getLogRequest() {
        return logRequest;
    }

    public String send(String text) throws Exception {
        if (StringUtils.isNullOrEmpty(text)) {
            throw new BusinessException("text不能为空");
        }
        // 获取签名
        String realUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        // 构建请求体
        String body = buildParam(text);
        if (this.logRequest) {
            logger.info("请求地址：{}，请求体：{}", realUrl, body);
        }
        return sendPost(realUrl, JSON, null, body);
    }

    private String buildParam(String text) {
        // 发送数据,求数据均为json字符串
        ResumeRequest request = new ResumeRequest();
        // 请求头
        ResumeRequest.HeaderBean header = new ResumeRequest.HeaderBean();
        header.setAppId(appId);
        header.setStatus(3);
        request.setHeader(header);

        // 请求参数
        ResumeRequest.ParameterBean parameter = new ResumeRequest.ParameterBean(this);
        request.setParameter(parameter);

        // 请求体
        ResumeRequest.PayloadBean payload = new ResumeRequest.PayloadBean(this);
        payload.getReqData().setText(Base64.getEncoder().encodeToString(text.getBytes()));
        payload.getReqData().setStatus(3);
        request.setPayload(payload);
        return StringUtils.gson.toJson(request);
    }


    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://cn-huadong-1.xf-yun.com/v1/private/s73f4add9";

        private static final String SERVICE_ID = "s73f4add9";

        private boolean logRequest = false;

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVICE_ID, appId, apiKey, apiSecret);
        }

        public Builder logRequest(boolean logRequest) {
            this.logRequest = logRequest;
            return this;
        }

        @Override
        public ResumeGenClient build() {
            return new ResumeGenClient(this);
        }

    }
}
