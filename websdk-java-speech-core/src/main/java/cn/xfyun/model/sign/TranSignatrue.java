package cn.xfyun.model.sign;

import cn.xfyun.util.CryptTools;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.SignatureException;

/**
 * @author <ydwang16@iflytek.com>
 * @description
 * @date 2021/6/15
 */
public class TranSignatrue extends Hmac256Signature {

    private String httpBody;

    public TranSignatrue(String apiKey, String secretKey, String hostUrl, boolean isPost) {
        super(apiKey, secretKey, hostUrl, isPost);
    }

    /**
     * 生成待加密原始字符
     * 规则如下：
     * host: iat-api.xfyun.cn
     * date: Wed, 10 Jul 2019 07:35:43 GMT
     * GET /v2/iat HTTP/1.1
     *
     * @throws SignatureException
     */
    @Override
    public String generateOriginSign() throws SignatureException {
        try {
            URL url = new URL(this.getUrl());

            String digestBase64 = "SHA-256=" + CryptTools.base64Encode(DigestUtils.sha256Hex(httpBody));
            StringBuilder builder = new StringBuilder("host: ").append(url.getHost()).append("\n")
                    .append("date: ").append(this.getTs()).append("\n")
                    .append(requestMethod).append(" ").append(url.getPath()).append(" HTTP/1.1").append("\n")
                    .append("digest: ").append(digestBase64);

            return builder.toString();
        } catch (MalformedURLException e) {
            throw new SignatureException("MalformedURLException:" + e.getMessage());
        }
    }

    public TranSignatrue setHttpBody(String httpBody) {
        this.httpBody = httpBody;
        return this;
    }
}
