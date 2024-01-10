package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.model.CreateParam;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 讯飞智文client
 *
 * @author junzhang27
 */
public class AIPPTClient extends HttpClient {

    public AIPPTClient(AIPPTClient.Builder builder) {
        super(builder);
    }

    /**
     * PPT生成
     */
    public String create(CreateParam createParam) throws IOException {
        Map<String, String> header = new HashMap<>(6);
        Long timestamp = System.currentTimeMillis() / 1000;
        header.put("signature", Signature.generateSignature(appId, timestamp, apiSecret));
        header.put("appId", appId);
        header.put("timestamp", String.valueOf(timestamp));
        return sendPost(hostUrl + "api/aippt/create", JSON, header, StringUtils.gson.toJson(createParam));
    }

    /**
     * PPT进度查询
     *
     * @param sid 请求唯一ID
     * @return
     * @throws IOException
     */
    public String progress(String sid) throws IOException {
        Map<String, String> header = new HashMap<>(6);
        Long timestamp = System.currentTimeMillis() / 1000;
        header.put("signature", Signature.generateSignature(appId, timestamp, apiSecret));
        header.put("appId", appId);
        header.put("timestamp", String.valueOf(timestamp));
        return sendGet(hostUrl + "api/aippt/progress?sid=" + sid, header);
    }

    /**
     * PPT主题列表查询
     */
    public String themeList() throws IOException {
        Map<String, String> header = new HashMap<>(6);
        Long timestamp = System.currentTimeMillis() / 1000;
        header.put("signature", Signature.generateSignature(appId, timestamp, apiSecret));
        header.put("appId", appId);
        header.put("timestamp", String.valueOf(timestamp));
        return sendGet(hostUrl + "api/aippt/themeList", header);
    }

    public static final class Builder extends HttpBuilder<AIPPTClient.Builder> {
        private static final String HOST_URL = "https://zwapi.xfyun.cn/";

        public Builder(String appId, String apiSecret) {
            super(HOST_URL, appId, null, apiSecret);
        }

        @Override
        public AIPPTClient build() {
            return new AIPPTClient(this);
        }
    }
}
