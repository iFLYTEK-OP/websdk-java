package cn.xfyun.model.sign;

import cn.xfyun.util.CryptTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 一句话复刻鉴权工具类
 *
 * @author zyding
 */
public class VoiceCloneSignature {

    private static final Logger logger = LoggerFactory.getLogger(VoiceCloneSignature.class);

    /**
     * 一句话复刻获取token头
     *
     * @param apiKey    应用的key
     * @param timestamp 时间戳
     * @param body      请求体
     * @return header 请求头
     */
    public static Map<String, String> tokenSign(String apiKey, String timestamp, String body) {
        try {
            Map<String, String> header = new HashMap<>(6);
            String builder = CryptTools.md5Encrypt(apiKey + timestamp) + body;
            String authorization = CryptTools.md5Encrypt(builder);
            header.put("Authorization", authorization);
            header.put("Content-Type", "application/json");
            return header;
        } catch (Exception e) {
            logger.error("生成token头失败", e);
        }
        return null;
    }

    /**
     * 一句话复刻通用加密
     *
     * @param appId  应用ID
     * @param apiKey 应用的key
     * @param body   请求体
     * @return header 请求头
     */
    public static Map<String, String> commonSign(String appId, String apiKey, String body, String token) {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String builder = apiKey + timestamp + CryptTools.md5Encrypt(body);
            String authorization = CryptTools.md5Encrypt(builder);

            Map<String, String> header = new HashMap<>(6);
            header.put("X-Sign", authorization);
            header.put("X-Token", token);
            header.put("X-AppId", appId);
            header.put("X-Time", timestamp);
            return header;
        } catch (Exception e) {
            logger.error("生成通用加密头失败", e);
        }
        return null;
    }
}
