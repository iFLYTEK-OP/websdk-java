package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.Audio;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 音频合规 API
 *
 * @author zyding
 * @version 1.0
 * @date 2025/3/13 15:32
 */
public class AudioComplianceClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(AudioComplianceClient.class);

    /**
     * 回调地址，未传不进行回调
     */
    private final String notifyUrl;

    /**
     * 结果查询地址
     */
    private final String queryUrl;

    public AudioComplianceClient(Builder builder) {
        super(builder);
        this.notifyUrl = builder.notifyUrl;
        this.queryUrl = builder.queryUrl;
    }

    public String getQueryUrl() {
        return queryUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String send(List<Audio> audioList) throws Exception {
        return send(audioList, null);
    }

    /**
     * @param audioList 音频信息
     * @param notifyUrl 回调地址
     * @return
     * @throws Exception
     */
    public String send(List<Audio> audioList, String notifyUrl) throws Exception {
        if (null == audioList || audioList.isEmpty()) {
            throw new BusinessException("音频信息不能为空");
        }
        // 参数
        Map<String, String> parameters = Signature.getAuth(appId, apiKey, apiSecret);
        return sendPost(hostUrl, JSON, null, buildParam(audioList, notifyUrl), parameters);
    }

    public String query(String requestId) throws Exception {
        if (StringUtils.isNullOrEmpty(requestId)) {
            throw new BusinessException("requestId不能为空");
        }
        // 参数
        JsonObject param = new JsonObject();
        param.addProperty("request_id", requestId);
        Map<String, String> parameters = Signature.getAuth(appId, apiKey, apiSecret);
        return sendPost(queryUrl, JSON, null, param.toString(), parameters);
    }

    private String buildParam(List<Audio> audioList, String callBackUrl) {
        // 发送数据,求数据均为json字符串
        HashMap<String, Object> request = new HashMap<>();
        request.put("audio_list", audioList);
        request.put("notify_url", StringUtils.isNullOrEmpty(callBackUrl) ? notifyUrl : callBackUrl);
        return StringUtils.toJson(request);
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://audit.iflyaisol.com/audit/v2/audio";
        private String notifyUrl;
        private String queryUrl = "https://audit.iflyaisol.com/audit/v2/query";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
        }

        @Override
        public AudioComplianceClient build() {
            return new AudioComplianceClient(this);
        }

        public Builder notifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
            return this;
        }

        public Builder queryUrl(String queryUrl) {
            this.queryUrl = queryUrl;
            return this;
        }

    }
}
