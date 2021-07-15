package cn.xfyun.model.sign;

import cn.xfyun.util.CryptTools;
import cn.xfyun.util.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/1 9:56
 */
public class Signature {

    /**
     * 请求签名方式
     * host              请求主机
     * date              当前时间戳，RFC1123格式
     * authorization     使用base64编码的签名相关信息(签名基于hamc-sha256计算)
     *
     * @param requestUrl
     * @param apiKey
     * @param apiSecret
     * @return
     */
    public static String signHostDateAuthorization(String requestUrl, String requestMethod, String apiKey, String apiSecret) {
        URL url = null;
        // 替换调schema前缀 ，原因是URL库不支持解析包含ws,wss schema的url
        String httpRequestUrl = requestUrl.replace("ws://", "http://").replace("wss://", "https://");
        try {
            url = new URL(httpRequestUrl);
            //获取当前日期并格式化
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            String date = format.format(new Date());
            String host = url.getHost();
            StringBuilder builder = new StringBuilder().
                    append("host: ").append(host).append("\n")
                    .append("date: ").append(date).append("\n")
                    .append(requestMethod).append(" ").append(url.getPath()).append(" HTTP/1.1");
            Charset charset = StandardCharsets.UTF_8;
            SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
            Mac mac = Mac.getInstance("hmacsha256");
            mac.init(spec);
            byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
            String sha = Base64.getEncoder().encodeToString(hexDigits);
            String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
            String authBase = Base64.getEncoder().encodeToString(authorization.getBytes(charset));
            return String.format("%s?authorization=%s&host=%s&date=%s", requestUrl, URLEncoder.encode(authBase), URLEncoder.encode(host), URLEncoder.encode(date));
        } catch (Exception e) {
            throw new RuntimeException("assemble requestUrl error:" + e.getMessage());
        }
    }

    /**
     *    Host               请求主机
     *    Date               当前时间戳，RFC1123格式
     *    Digest             加密请求body SHA-256=Base64(SHA256(请求body))
     *    Authorization      使用base64编码的签名相关信息(签名基于hamc-sha256计算)
     *
     *
     * @param requestUrl
     * @param apiKey
     * @param apiSecret
     * @param body
     * @return
     * @throws Exception
     */
    public static Map<String, String> signHttpHeaderDigest(String requestUrl, String requestMethod, String apiKey, String apiSecret, String body) throws Exception {
        Map<String, String> header = new HashMap<>(6);
        URL url = new URL(requestUrl);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        String digestBase64 = "SHA-256=" + signBody(body);
        StringBuilder builder = new StringBuilder("host: ").append(url.getHost()).append("\n").
                append("date: ").append(date).append("\n").
                append(requestMethod + " ").append(url.getPath()).append(" HTTP/1.1").append("\n").
                append("digest: ").append(digestBase64);
        String sha = hmacsign(builder.toString(), apiSecret);
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line digest", sha);
        header.put("Authorization", authorization);
        header.put("Content-Type", "application/json");
        header.put("Accept", "application/json,version=1.0");
        header.put("Host", url.getHost());
        header.put("Date", date);
        header.put("Digest", digestBase64);
        return header;
    }

    /**
     * 对body进行SHA-256加密
     */
    private static String signBody(String body) {
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(body.getBytes("UTF-8"));
            encodestr = Base64.getEncoder().encodeToString(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodestr;
    }

    /**
     * hmacsha256加密
     */
    private static String hmacsign(String signature, String apiSecret) throws Exception {
        Charset charset = Charset.forName("UTF-8");
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(signature.getBytes(charset));
        return Base64.getEncoder().encodeToString(hexDigits);
    }


    /**
     *    X-Appid           appid
     *    X-CurTime         当前UTC时间戳
     *    X-Param           相关参数JSON串经Base64编码后的字符串
     *    X-CheckSum        MD5(APIKey + X-CurTime + X-Param)，三个值拼接的字符串，进行MD5哈希计算（32位小写）
     *
     * @param appId
     * @param apiKey
     * @param param
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> signHttpHeaderCheckSum(String appId, String apiKey, String param) throws UnsupportedEncodingException {
        String curTime = String.valueOf(System.currentTimeMillis() / 1000L);
        String paramBase64 = Base64.getEncoder().encodeToString(param.getBytes("UTF-8"));
        String checkSum = DigestUtils.md5Hex(apiKey + curTime + paramBase64);
        Map<String, String> header = new HashMap<>(6);
        header.put("X-Param", paramBase64);
        header.put("X-CurTime", curTime);
        header.put("X-CheckSum", checkSum);
        header.put("X-Appid", appId);
        return header;
    }

    public static String rtasrSignature(String url, String appId, String key) {
        url = url.replace("ws://", "http://").replace("wss://", "https://");
        String ts = String.valueOf(System.currentTimeMillis() / 1000L);
        try {
            String signature = CryptTools.hmacEncrypt(CryptTools.HMAC_SHA1, CryptTools.md5Encrypt(appId + ts), key);
            return url + "?appid=" + appId + "&ts=" + ts + "&signa=" + URLEncoder.encode(signature, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
