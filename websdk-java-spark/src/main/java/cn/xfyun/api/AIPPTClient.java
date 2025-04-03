package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.model.CreateParam;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 讯飞智文client
 *
 * @author junzhang27
 */
public class AIPPTClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(AIPPTClient.class);

    /**
     * 创建PPT接口
     */
    private static final String CREATE_URL = "api/ppt/v2/create";

    /**
     * 查询接口
     */
    private static final String PROGRESS_URL = "api/aippt/progress?sid=%s";

    /**
     * PPT主题列表查询接口
     */
    private static final String THEME_URL = "api/aippt/themeList";

    public AIPPTClient(Builder builder) {
        super(builder);
    }

    /**
     * PPT生成
     */
    public String create(CreateParam createParam) throws IOException {
        Map<String, String> header = new HashMap<>(6);
        Long timestamp = System.currentTimeMillis() / 1000;
        String signature = Signature.generateSignature(appId, timestamp, apiSecret);
        header.put("signature", signature);
        header.put("appId", appId);
        header.put("timestamp", String.valueOf(timestamp));
        // 参数
        String json = StringUtils.gson.toJson(createParam);
        logger.debug("智能ppt生成请求参数: {}, signature: {}", json, signature);

        return sendPost(hostUrl + CREATE_URL, JSON, header, json);
    }

    /**
     * PPT进度查询
     *
     * @param sid 请求唯一ID
     */
    public String progress(String sid) throws IOException {
        Map<String, String> header = new HashMap<>(6);
        Long timestamp = System.currentTimeMillis() / 1000;
        String signature = Signature.generateSignature(appId, timestamp, apiSecret);
        header.put("signature", signature);
        header.put("appId", appId);
        header.put("timestamp", String.valueOf(timestamp));
        logger.debug("智能ppt生成进度查询请求sid: {}, signature: {}", sid, signature);

        return sendGet(hostUrl + String.format(PROGRESS_URL, sid), header);
    }

    /**
     * PPT主题列表查询
     */
    public String themeList() throws IOException {
        Map<String, String> header = new HashMap<>(6);
        Long timestamp = System.currentTimeMillis() / 1000;
        String signature = Signature.generateSignature(appId, timestamp, apiSecret);
        header.put("signature", signature);
        header.put("appId", appId);
        header.put("timestamp", String.valueOf(timestamp));
        logger.debug("智能ppt主题列表查询 signature: {}", signature);

        return sendGet(hostUrl + THEME_URL, header);
    }

    public static final class Builder extends HttpBuilder<Builder> {
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
