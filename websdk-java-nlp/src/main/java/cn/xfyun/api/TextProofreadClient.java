package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.model.textproof.request.TextProofreadRequest;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

/**
 * 文本校对 API
 *
 * @author zyding
 * @version 1.0
 * @date 2025/3/13 14:32
 */
public class TextProofreadClient extends PlatformHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(TextProofreadClient.class);

    public TextProofreadClient(Builder builder) {
        super(builder);
    }

    public String send(String text) throws Exception {
        if (StringUtils.isNullOrEmpty(text)) {
            throw new BusinessException("text不能为空");
        }
        String realUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        return sendPost(realUrl, JSON, null, buildParam(text));
    }

    private String buildParam(String text) {
        // 发送数据,求数据均为json字符串
        TextProofreadRequest request = new TextProofreadRequest();
        // 请求头
        TextProofreadRequest.Header header = new TextProofreadRequest.Header();
        header.setAppId(appId);
        header.setStatus(status);
        request.setHeader(header);
        // 请求参数
        TextProofreadRequest.Parameter parameter = new TextProofreadRequest.Parameter(this);
        request.setParameter(parameter);
        // 请求体
        TextProofreadRequest.Payload payload = new TextProofreadRequest.Payload(this);
        payload.getText().setText(Base64.getEncoder().encodeToString(text.getBytes()));
        request.setPayload(payload);

        String requestStr = StringUtils.gson.toJson(request);
        logger.debug("文本校对 API 入参：{}", requestStr);
        return requestStr;
    }


    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://cn-huadong-1.xf-yun.com/v1/private/s37b42a45";

        private static final String SERVICE_ID = "s37b42a45";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVICE_ID, appId, apiKey, apiSecret);
        }

        @Override
        public TextProofreadClient build() {
            TextProofreadClient client = new TextProofreadClient(this);
            return client;
        }

    }
}
