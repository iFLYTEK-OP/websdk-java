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
    
    private final boolean logRequest; 

    public AIPPTClient(Builder builder) {
        super(builder);
        this.logRequest = builder.logRequest;
    }

    public boolean getLogRequest() {
        return logRequest;
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
        //参数
        String json = StringUtils.gson.toJson(createParam);
        if (this.logRequest) {
            logger.info("智能ppt生成请求参数: {}, signature: {}", json, signature);
        }
        return sendPost(hostUrl + "api/ppt/v2/create", JSON, header, json);
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
        if (this.logRequest) {
            logger.info("智能ppt生成进度查询请求sid: {}, signature: {}", sid, signature);
        }
        return sendGet(hostUrl + "api/aippt/progress?sid=" + sid, header);
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
        if (this.logRequest) {
            logger.info("智能ppt主题列表查询 signature: {}", signature);
        }
        return sendGet(hostUrl + "api/aippt/themeList", header);
    }

    public static final class Builder extends HttpBuilder<Builder> {
        private static final String HOST_URL = "https://zwapi.xfyun.cn/";
        
        private boolean logRequest = false;

        public Builder(String appId, String apiSecret) {
            super(HOST_URL, appId, null, apiSecret);
        }

        public Builder logRequest(boolean logRequest) {
            this.logRequest = logRequest;
            return this;
        }

        @Override
        public AIPPTClient build() {
            return new AIPPTClient(this);
        }
    }
}
