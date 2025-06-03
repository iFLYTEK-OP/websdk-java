package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.resume.ResumeRequest;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 简历生成 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/spark/resume.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class ResumeGenClient extends PlatformHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(ResumeGenClient.class);

    public ResumeGenClient(Builder builder) {
        super(builder);
    }

    public String send(String text) throws IOException {
        // 参数校验
        paramCheck(text);

        // 获取签名
        String realUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);

        // 构建请求体
        String body = buildParam(text, realUrl);

        // 发送请求
        return sendPost(realUrl, JSON, null, body);
    }

    /**
     * 参数校验
     */
    private void paramCheck(String text) {
        if (StringUtils.isNullOrEmpty(text)) {
            throw new BusinessException("text不能为空");
        }
    }

    /**
     * 构建参数
     */
    private String buildParam(String text, String realUrl) {
        // 发送数据,请求数据均为json字符串
        ResumeRequest request = new ResumeRequest();
        // 请求头
        ResumeRequest.HeaderBean header = new ResumeRequest.HeaderBean();
        header.setAppId(appId);
        header.setStatus(status);
        request.setHeader(header);

        // 请求参数
        ResumeRequest.ParameterBean parameter = new ResumeRequest.ParameterBean(this);
        request.setParameter(parameter);

        // 请求体
        ResumeRequest.PayloadBean payload = new ResumeRequest.PayloadBean(this);
        payload.getReqData().setText(Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8)));
        payload.getReqData().setStatus(status);
        request.setPayload(payload);

        String body = StringUtils.gson.toJson(request);
        logger.debug("请求地址：{}，请求体：{}", realUrl, body);
        return body;
    }

    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://cn-huadong-1.xf-yun.com/v1/private/s73f4add9";
        private static final String SERVICE_ID = "s73f4add9";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVICE_ID, appId, apiKey, apiSecret);
            this.readTimeout(120);
        }

        @Override
        public ResumeGenClient build() {
            return new ResumeGenClient(this);
        }
    }
}
