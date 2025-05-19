package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.Video;
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
 * 视频合规 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/nlp/VideoModeration/API.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class VideoComplianceClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(VideoComplianceClient.class);

    /**
     * 回调地址，未传不进行回调
     */
    private final String notifyUrl;

    /**
     * 结果查询地址
     */
    private final String queryUrl;

    public VideoComplianceClient(Builder builder) {
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

    public String send(List<Video> videoList) throws Exception {
        return send(videoList, null);
    }

    /**
     * @param videoList 视频信息
     * @param notifyUrl 回调地址
     */
    public String send(List<Video> videoList, String notifyUrl) throws IOException, SignatureException {
        // 参数校验
        paramCheck(videoList);

        // 获取鉴权头
        Map<String, String> parameters = Signature.getAuth(appId, apiKey, apiSecret);

        // 发送请求
        return sendPost(hostUrl, JSON, null, buildParam(videoList, notifyUrl), parameters);
    }

    /**
     * 查询合规任务结果
     *
     * @param requestId 请求ID
     */
    public String query(String requestId) throws IOException, SignatureException {
        // 参数校验
        queryCheck(requestId);

        // 获取鉴权参数
        Map<String, String> parameters = Signature.getAuth(appId, apiKey, apiSecret);

        // 发送请求
        return sendPost(queryUrl, JSON, null, buildQuery(requestId), parameters);
    }

    /**
     * 构建查询入参
     */
    private String buildQuery(String requestId) {
        JsonObject param = new JsonObject();
        param.addProperty("request_id", requestId);
        return param.toString();
    }

    /**
     * 参数校验
     */
    private void queryCheck(String requestId) {
        if (StringUtils.isNullOrEmpty(requestId)) {
            throw new BusinessException("requestId不能为空");
        }
    }

    /**
     * 参数校验
     */
    private void paramCheck(List<Video> videoList) {
        if (null == videoList || videoList.isEmpty()) {
            throw new BusinessException("视频信息不能为空");
        }
    }

    /**
     * 构建参数
     */
    private String buildParam(List<Video> videoList, String callBackUrl) {
        // 发送数据,求数据均为json字符串
        JsonObject json = new JsonObject();
        json.add("video_list", StringUtils.gson.toJsonTree(videoList));
        json.addProperty("notify_url", StringUtils.isNullOrEmpty(callBackUrl) ? notifyUrl : callBackUrl);
        logger.debug("视频合规请求参数: {}", json);
        return json.toString();
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://audit.iflyaisol.com/audit/v2/video";
        private String notifyUrl;
        private String queryUrl = "https://audit.iflyaisol.com/audit/v2/query";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
        }

        @Override
        public VideoComplianceClient build() {
            return new VideoComplianceClient(this);
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
