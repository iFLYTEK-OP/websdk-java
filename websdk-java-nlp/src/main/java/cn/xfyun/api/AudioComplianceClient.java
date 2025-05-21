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

import java.io.IOException;
import java.security.SignatureException;
import java.util.List;
import java.util.Map;

/**
 * 音频合规 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/nlp/AudioModeration/API.html">...</a>
 *
 * @author <zyding6@ifytek.com>
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

    /**
     * 支持mp3、alaw、ulaw、pcm、aac、wav格式，通过URL外链的音频时长建议限制在1小时内
     *
     * @param audioList 音频信息
     */
    public String send(List<Audio> audioList) throws IOException, SignatureException {
        // 发送请求
        return send(audioList, null);
    }

    /**
     * 支持mp3、alaw、ulaw、pcm、aac、wav格式，通过URL外链的音频时长建议限制在1小时内
     *
     * @param audioList 音频信息
     * @param notifyUrl 回调地址
     */
    public String send(List<Audio> audioList, String notifyUrl) throws SignatureException, IOException {
        // 参数校验
        paramCheck(audioList);

        // 构建签名参数
        Map<String, String> parameters = Signature.getAuth(appId, apiKey, apiSecret);

        // 发送请求
        return sendPost(hostUrl, JSON, null, buildParam(audioList, notifyUrl), parameters);
    }

    /**
     * 查询合规任务结果
     *
     * @param requestId 请求ID
     */
    public String query(String requestId) throws IOException, SignatureException {
        // 参数校验
        queryCheck(requestId);

        // 构建签名参数
        Map<String, String> parameters = Signature.getAuth(appId, apiKey, apiSecret);

        // 发送请求
        return sendPost(queryUrl, JSON, null, buildQuery(requestId), parameters);
    }

    /**
     * 音频合规进度参数校验
     */
    private void queryCheck(String requestId) {
        if (StringUtils.isNullOrEmpty(requestId)) {
            throw new BusinessException("requestId不能为空");
        }
    }

    /**
     * 音频信息校验
     */
    private void paramCheck(List<Audio> audioList) {
        if (null == audioList || audioList.isEmpty()) {
            throw new BusinessException("音频信息不能为空");
        }
    }

    /**
     * 封装参数
     */
    private String buildParam(List<Audio> audioList, String callBackUrl) {
        // 发送数据,求数据均为json字符串
        JsonObject param = new JsonObject();
        param.add("audio_list", StringUtils.gson.toJsonTree(audioList));
        param.addProperty("notify_url", StringUtils.isNullOrEmpty(callBackUrl) ? notifyUrl : callBackUrl);
        logger.debug("音频合规 sendAudio: {}", param);
        return param.toString();
    }

    /**
     * 封装查询参数
     */
    private String buildQuery(String requestId) {
        // 发送数据,求数据均为json字符串
        JsonObject param = new JsonObject();
        param.addProperty("request_id", requestId);
        logger.debug("音频合规 sendQuery: {}", param);
        return param.toString();
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://audit.iflyaisol.com/audit/v2/audio";
        private String queryUrl = "https://audit.iflyaisol.com/audit/v2/query";
        private String notifyUrl;

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
