package cn.xfyun.api;

import cn.xfyun.exception.HttpException;
import cn.xfyun.util.HttpConnector;
import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import sun.misc.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: <flhong2@iflytek.com>
 * @description: 歌曲识别
 * @version: v1.0
 * @create: 2021-06-03 20:28
 **/
public class QbhClient {

    /**
     * 服务请求地址
     */
    private String hostUrl;

    /**
     * 应用ID,控制台获区
     */
    private String appId;

    /**
     * 应用Key,控制台获区
     */
    private String apiKey;

    /**
     * 相关参数JSON串
     */
    private JsonObject paramJson;

    /**
     * 引擎类型，可选值：afs（哼唱)
     */
    private String engineType;

    /**
     * 音频编码，可选值：raw（pcm、wav格式）、aac，默认raw
     */
    private String aue;

    /**
     * 采样率，可选值：8000、16000，默认16000，aue是aac，sample_rate必须是8000
     */
    private String sampleRate;

    /**
     * 哼唱音频存放地址url
     */
    private String audioUrl;

    private HttpConnector connector;

    public QbhClient(Builder builder) {
        this.hostUrl = builder.hostUrl;
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.paramJson = builder.paramJson;
        this.engineType = builder.engineType;
        this.aue = builder.aue;
        this.sampleRate = builder.sampleRate;
        this.audioUrl = builder.audioUrl;
        this.connector = builder.connector;
    }

    public String send(File file) throws IOException, HttpException {
        byte[] bytes = Files.readAllBytes(Paths.get(file.getPath()));
        return send(bytes);
    }

    public String send(String data) throws IOException, HttpException {
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        return send(inputStream);
    }

    public String send(InputStream inputStream) throws IOException, HttpException {
        byte[] bytes = IOUtils.readFully(inputStream, -1, true);
        return send(bytes);
    }

    /**
     * 发送数据到服务端
     *
     * @param bytes
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public String send(byte[] bytes) throws IOException, HttpException {

        String result = connector.postByBytes(this.hostUrl, buildHttpHeader(), bytes);
        return result;
    }

    public String send() throws IOException, HttpException {

        String result = connector.postByBytes(this.hostUrl, buildHttpHeader(), null);
        return result;
    }

    /**
     * 组装http请求头
     */
    private Map<String, String> buildHttpHeader() throws UnsupportedEncodingException {
        String curTime = "" + System.currentTimeMillis() / 1000L;

        String paramBase64 = new String(Base64.encodeBase64(paramJson.toString().getBytes(StandardCharsets.UTF_8)));
        String checkSum = DigestUtils.md5Hex(this.apiKey + curTime + paramBase64);
        Map<String, String> header = new HashMap<String, String>(5);
        header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        header.put("X-Param", paramBase64);
        header.put("X-CurTime", curTime);
        header.put("X-CheckSum", checkSum);
        header.put("X-Appid", this.appId);
        return header;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public String getAppId() {
        return appId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public JsonObject getParamJson() {
        return paramJson;
    }

    public String getEngineType() {
        return engineType;
    }

    public String getAue() {
        return aue;
    }

    public String getSampleRate() {
        return sampleRate;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public static class Builder {
        private String hostUrl = "https://webqbh.xfyun.cn/v1/service/v1/qbh";
        private String appId;
        private String apiKey;
        private JsonObject paramJson = new JsonObject();

        private String engineType = "afs";
        private String aue = "raw";
        private String sampleRate = "16000";
        private String audioUrl;

        private HttpConnector connector;
        private Integer maxConnections = 50;
        private Integer connTimeout = 10000;
        private Integer soTimeout = 30000;
        private Integer retryCount = 3;

        public QbhClient build() {

            if (Objects.nonNull(paramJson)) {
                paramJson.addProperty("engine_type", engineType);
                paramJson.addProperty("aue", aue);
                paramJson.addProperty("sample_rate", sampleRate);
            }

            this.connector = HttpConnector.build(maxConnections, connTimeout, soTimeout, retryCount);
            return new QbhClient(this);
        }

        public QbhClient.Builder hostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        public QbhClient.Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public QbhClient.Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public QbhClient.Builder paramJson(JsonObject paramJson) {
            this.paramJson = paramJson;
            return this;
        }

        public QbhClient.Builder engineType(String engineType) {
            this.engineType = engineType;
            this.paramJson.addProperty("engine_type", engineType);
            return this;
        }

        public QbhClient.Builder aue(String aue) {
            this.aue = aue;
            this.paramJson.addProperty("aue", aue);
            return this;
        }

        public QbhClient.Builder sampleRate(String sampleRate) {
            this.sampleRate = sampleRate;
            this.paramJson.addProperty("sample_rate", sampleRate);
            return this;
        }

        public QbhClient.Builder audioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
            this.paramJson.addProperty("audio_url", audioUrl);
            return this;
        }

        public QbhClient.Builder maxConnections(Integer maxConnections) {
            this.maxConnections = maxConnections;
            return this;
        }

        public QbhClient.Builder connTimeout(Integer connTimeout) {
            this.connTimeout = connTimeout;
            return this;
        }

        public QbhClient.Builder soTimeout(Integer soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public QbhClient.Builder retryCount(Integer retryCount) {
            this.retryCount = retryCount;
            return this;
        }
    }
}

