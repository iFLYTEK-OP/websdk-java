package cn.xfyun.model.sign;

import cn.xfyun.util.CryptTools;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zyding
 * @version 1.0
 * @date 2025/3/11 9:56
 */
public class VoiceCloneSignature {

    /**
     * 一句话复刻获取token头
     *
     * @param apiKey
     * @param timestamp
     * @param body
     * @return header
     */
    public static Map<String, String> tokenSign(String apiKey, String timestamp, String body) {
        try {
            Map<String, String> header = new HashMap<>(6);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(CryptTools.md5Encrypt(apiKey + timestamp));
            stringBuilder.append(body);
            String authorization = CryptTools.md5Encrypt(stringBuilder.toString());
            header.put("Authorization", authorization);
            header.put("Content-Type", "application/json");
            return header;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 一句话复刻通用加密
     *
     * @param appId
     * @param apiKey
     * @param body
     * @return header
     */
    public static Map<String, String> commonSign(String appId, String apiKey, String body, String token) {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(apiKey);
            stringBuilder.append(timestamp);
            stringBuilder.append(CryptTools.md5Encrypt(body));
            String authorization = CryptTools.md5Encrypt(stringBuilder.toString());

            Map<String, String> header = new HashMap<>(6);
            header.put("X-Sign", authorization);
            header.put("X-Token", token);
            header.put("X-AppId", appId);
            header.put("X-Time", timestamp);
            return header;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
