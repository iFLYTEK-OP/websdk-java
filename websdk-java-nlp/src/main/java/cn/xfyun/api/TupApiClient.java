package cn.xfyun.api;

import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.HttpConnector;
import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 *    人脸特征分析年龄WebAPI接口
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/1 17:12
 */
public class TupApiClient {
    /**
     * 年龄识别url
     */
    private String ageUrl;

    /**
     * 颜值识别url
     */
    private String faceScoreUrl;

    /**
     * 性别识别url
     */
    private String sexUrl;

    /**
     * 表情识别url
     */
    private String expressionUrl;


    private String appId;


    private String apiKey;

    /**
     * http最大连接数  默认20
     */
    private int maxConnections;

    /**
     * http连接时间  默认3s
     */
    private int connTimeout;

    /**
     * http socket超时时间 默认 5s
     */
    private int socketTimeout;

    /**
     * http 重试次数 默认不重试 0
     */
    private int retryCount;


    public String getAgeUrl() {
        return ageUrl;
    }

    public void setAgeUrl(String ageUrl) {
        this.ageUrl = ageUrl;
    }

    public String getFaceScoreUrl() {
        return faceScoreUrl;
    }

    public void setFaceScoreUrl(String faceScoreUrl) {
        this.faceScoreUrl = faceScoreUrl;
    }

    public String getSexUrl() {
        return sexUrl;
    }

    public void setSexUrl(String sexUrl) {
        this.sexUrl = sexUrl;
    }

    public String getExpressionUrl() {
        return expressionUrl;
    }

    public void setExpressionUrl(String expressionUrl) {
        this.expressionUrl = expressionUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public TupApiClient(TupApiClient.Builder builder) {
        this.ageUrl = builder.ageUrl;
        this.faceScoreUrl = builder.faceScoreUrl;
        this.sexUrl = builder.sexUrl;
        this.expressionUrl = builder.expressionUrl;
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.maxConnections = builder.maxConnections;
        this.connTimeout = builder.connTimeout;
        this.socketTimeout = builder.socketTimeout;
        this.retryCount = builder.retryCount;
    }

    public String ageRecognition(String imageName, byte[] imageByteArray) throws IOException {
        Map<String, String> header = buildHeader(imageName);
        HttpConnector httpClient = HttpConnector.build(maxConnections, connTimeout, socketTimeout, retryCount);
        return httpClient.post(ageUrl, header, imageByteArray);
    }

    public String faceScore(String imageName, byte[] imageByteArray) throws IOException {
        Map<String, String> header = buildHeader(imageName);
        HttpConnector httpClient = HttpConnector.build(maxConnections, connTimeout, socketTimeout, retryCount);
        return httpClient.post(faceScoreUrl, header, imageByteArray);
    }

    public String sexRecognition(String imageName, byte[] imageByteArray) throws IOException {
        Map<String, String> header = buildHeader(imageName);
        HttpConnector httpClient = HttpConnector.build(maxConnections, connTimeout, socketTimeout, retryCount);
        return httpClient.post(sexUrl, header, imageByteArray);
    }

    public String expressionRecognition(String imageName, byte[] imageByteArray) throws IOException {
        Map<String, String> header = buildHeader(imageName);
        HttpConnector httpClient = HttpConnector.build(maxConnections, connTimeout, socketTimeout, retryCount);
        return httpClient.post(expressionUrl, header, imageByteArray);
    }



    private Map<String, String> buildHeader(String imageName) throws UnsupportedEncodingException {
        JsonObject jso = new JsonObject();
        jso.addProperty("image_name", imageName);
        String params = jso.getAsString();
        String paramBase64 = new String(Base64.encodeBase64(params.getBytes("UTF-8")));
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, paramBase64, null);
        return header;
    }

    public static final class Builder {

        private String ageUrl = "http://tupapi.xfyun.cn/v1/age";

        private String faceScoreUrl = "http://tupapi.xfyun.cn/v1/face_score";

        private String sexUrl = "http://tupapi.xfyun.cn/v1/sex";

        private String expressionUrl = "http://tupapi.xfyun.cn/v1/expression";

        private String appId;

        private String apiKey;

        private int maxConnections = 20;

        private int connTimeout = 3000;

        private int socketTimeout = 5000;

        private int retryCount = 0;

        public Builder(String appId, String apiKey) {
            this.appId = appId;
            this.apiKey = apiKey;
        }

        public TupApiClient.Builder ageUrl(String ageUrl) {
            this.ageUrl = ageUrl;
            return this;
        }

        public TupApiClient.Builder faceScoreUrl(String faceScoreUrl) {
            this.faceScoreUrl = faceScoreUrl;
            return this;
        }

        public TupApiClient.Builder sexUrl(String sexUrl) {
            this.sexUrl = sexUrl;
            return this;
        }

        public TupApiClient.Builder expressionUrl(String expressionUrl) {
            this.expressionUrl = expressionUrl;
            return this;
        }

        public TupApiClient.Builder maxConnections(int maxConnections) {
            this.maxConnections = maxConnections;
            return this;
        }

        public TupApiClient.Builder connTimeout(int connTimeout) {
            this.connTimeout = connTimeout;
            return this;
        }

        public TupApiClient.Builder socketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        public TupApiClient.Builder retryCount(int retryCount) {
            this.retryCount = retryCount;
            return this;
        }

        public TupApiClient build() {
            TupApiClient client = new TupApiClient(this);
            return client;
        }

    }







}
