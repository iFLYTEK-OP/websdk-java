package cn.xfyun.model.header;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yingpeng
 * 构建http请求头
 */
public class BuildHttpHeader {

    /**
     * 构建自然语言处理能力请求头
     * @param param 待处理的数据
     * @param apiKey apiKey
     * @param appId appId
     * @return 请求头
     * @throws UnsupportedEncodingException 异常
     */
    public static Map<String, String> buildHttpHeader(String param, String apiKey, String appId) throws UnsupportedEncodingException {
        String curTime = System.currentTimeMillis() / 1000L + "";

        String paramBase64 = new String(Base64.encodeBase64(param.getBytes("UTF-8")));
        String checkSum = DigestUtils.md5Hex(apiKey + curTime + paramBase64);
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        header.put("X-Param", paramBase64);
        header.put("X-CurTime", curTime);
        header.put("X-CheckSum", checkSum);
        header.put("X-Appid", appId);
        return header;
    }
}
