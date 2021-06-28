package cn.xfyun.util;

import cn.xfyun.model.sign.AbstractSignature;
import okhttp3.HttpUrl;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.SignatureException;


/**
 * @author <ydwang16@iflytek.com>
 * @description 生成鉴权工具
 * @date 2021/3/24
 */
public class AuthUtil {

    private static String algorithm = "hmac-sha256";

    public static String generateAuthorization(AbstractSignature signature) throws SignatureException {
        return generateAuthorization(signature, algorithm);
    }

    /**
     * 生成鉴权内容
     *
     * @param signature 鉴权实例
     * @param algorithm 加密方式
     * @return
     * @throws SignatureException
     */
    public static String generateAuthorization(AbstractSignature signature, String algorithm) throws SignatureException {
        return String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"",
                signature.getId(),
                algorithm,
                "host date request-line",
                signature.getSigna());
    }

    /**
     * 生成翻译鉴权内容
     *
     * @param signature 鉴权实例
     * @param algorithm 加密方式
     * @return
     * @throws SignatureException
     */
    public static String generateTransAuthorization(AbstractSignature signature, String algorithm) throws SignatureException {
        return String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"",
                signature.getId(),
                algorithm,
                "host date request-line digest",
                signature.getSigna());
    }

    /**
     * 生成带鉴权的请求URL
     *
     * @param signature 鉴权实例
     * @return
     * @throws MalformedURLException
     * @throws SignatureException
     */
    public static String generateRequestUrl(AbstractSignature signature) throws MalformedURLException, SignatureException {
        URL url = new URL(signature.getUrl());
        String authorization = generateAuthorization(signature);

        HttpUrl httpUrl = HttpUrl.parse("https://" + url.getHost() + url.getPath()).newBuilder()
                .addQueryParameter("authorization", CryptTools.base64Encode(authorization))
                .addQueryParameter("date", signature.getTs())
                .addQueryParameter("host", url.getHost())
                .build();
        return httpUrl.toString();
    }
}
