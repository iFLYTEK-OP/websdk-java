package cn.xfyun.model.sign;

import cn.xfyun.util.CryptTools;
import cn.xfyun.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 签名实体
 *
 * @author : iflytek
 * @date : 2021年03月15日
 * 适用于语音听写、在线语音合成和语音评测的鉴权方案
 */
public class Hmac256Signature extends AbstractSignature {

    /**
     * 构造函数
     *
     * @param apiKey
     * @param secretKey
     * @param hostUrl
     */
    public Hmac256Signature(String apiKey, String secretKey, String hostUrl) {
        super(apiKey, secretKey, hostUrl);
    }

    @Override
    public String getSigna() throws SignatureException {
        if (StringUtils.isNullOrEmpty(this.signa)) {
            String originSign = generateOriginSign();
            setOriginSign(originSign);
            signa = generateSignature();
        }

        return signa;
    }

    @Override
    public String generateTs() {

        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());

        return date;
    }

    /**
     * 生成最终的签名
     *
     * @throws SignatureException
     */
    public String generateSignature() throws SignatureException {

        return CryptTools.hmacEncrypt(CryptTools.HMAC_SHA256, this.getOriginSign(), this.getKey());
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
    public String generateOriginSign() throws SignatureException {
        try {
            URL url = new URL(this.getUrl());

            return "host: " + url.getHost() + "\n" +
                    "date: " + this.getTs() + "\n" +
                    "GET " + url.getPath() + " HTTP/1.1";
        } catch (MalformedURLException e) {
            throw new SignatureException("MalformedURLException:" + e.getMessage());
        }
    }
}
