package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.model.textproof.request.TextProofreadRequest;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 文本校对 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/nlp/textCorrectionOfficial/API.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class TextProofreadClient extends PlatformHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(TextProofreadClient.class);

    public TextProofreadClient(Builder builder) {
        super(builder);
    }

    /**
     * @param text 校对文本
     * @return 返回结果
     * @throws IOException 请求失败
     */
    public String send(String text) throws IOException {
        // 参数校验
        if (StringUtils.isNullOrEmpty(text)) {
            throw new BusinessException("text不能为空");
        }

        // 获取鉴权请求地址
        String realUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);

        // 发送请求
        return sendPost(realUrl, JSON, null, buildParam(text));
    }

    /**
     * 构建请求参数
     */
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
        payload.getText().setText(Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8)));
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
            return new TextProofreadClient(this);
        }
    }
}
