package cn.xfyun.api;

import cn.xfyun.exception.HttpException;
import cn.xfyun.model.header.BuildHttpHeader;
import cn.xfyun.util.HttpConnector;
import com.google.gson.JsonObject;
import sun.misc.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author: <flhong2@iflytek.com>
 * @description: 歌曲识别
 * @version: v1.0
 * @create: 2021-06-03 20:28
 **/
public class CommonIseClient {

    /**
     * 服务请求地址
     */
    private String hostUrl;

    /**
     * 应用ID,控制台获取
     */
    private String appId;

    /**
     * 应用Key,控制台获取
     */
    private String apiKey;

    /**
     * 相关参数JSON串
     */
    private JsonObject paramJson;

    /**
     * 音频编码
     * raw（未压缩的 pcm 格式音频）
     * speex（标准开源speex）
     */
    private String aue;

    /**
     * 标准speex解码帧的大小
     * 当aue=speex时，若传此参数，表明音频格式为标准speex
     */
    private String speexSize;

    /**
     * 评测结果等级
     * entirety（默认值）
     * simple
     */
    private String resultLevel;

    /**
     * 评测语种
     * en_us（英语）
     * zh_cn（汉语）
     */
    private String language;

    /**
     * 评测题型
     * read_syllable（单字朗读，汉语专有）
     * read_word（词语朗读）
     * read_sentence（句子朗读）
     * read_chapter(篇章朗读)
     */
    private String category;

    /**
     * 拓展能力
     * multi_dimension(全维度 )
     */
    private String extraAbility;

    /**
     * 音频数据
     * base64 编码后进行 urlencode
     * 要求 base64 编码和 urlencode 后大小不超过5M
     */
    private String audio;

    /**
     * 评测文本（使用 utf-8 编码）需urlencode
     * 要求长度中文不超过180字节、英文不超过300字节
     */
    private String text;

    private HttpConnector connector;

    public CommonIseClient(Builder builder) {
        this.hostUrl = builder.hostUrl;
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.paramJson = builder.paramJson;
        this.aue = builder.aue;
        this.speexSize = builder.speexSize;
        this.resultLevel = builder.resultLevel;
        this.language = builder.language;
        this.category = builder.category;
        this.extraAbility = builder.extraAbility;
        this.audio = builder.audio;
        this.text = builder.text;
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

        String result = connector.postByBytes(this.hostUrl, BuildHttpHeader.buildHttpHeader(paramJson.toString(), apiKey, appId), bytes);
        return result;
    }

    public String send() throws IOException, HttpException {

        String result = connector.postByBytes(this.hostUrl, BuildHttpHeader.buildHttpHeader(paramJson.toString(), apiKey, appId), null);
        return result;
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

    public String getAue() {
        return aue;
    }

    public String getSpeexSize() {
        return speexSize;
    }

    public String getResultLevel() {
        return resultLevel;
    }

    public String getLanguage() {
        return language;
    }

    public String getCategory() {
        return category;
    }

    public String getExtraAbility() {
        return extraAbility;
    }

    public String getAudio() {
        return audio;
    }

    public String getText() {
        return text;
    }

    public static class Builder {
        private String hostUrl = "https://api.xfyun.cn/v1/service/v1/ise";
        private String appId;
        private String apiKey;
        private JsonObject paramJson = new JsonObject();

        private String aue;
        private String speexSize;
        private String resultLevel = "entirety";
        private String language;
        private String category;
        private String extraAbility;

        private String audio;
        private String text;

        private HttpConnector connector;
        /**
         * 最大连接数
         */
        private Integer maxConnections = 50;
        /**
         * 建立连接的超时时间
         */
        private Integer connTimeout = 10000;
        /**
         * 读数据包的超时时间
         */
        private Integer soTimeout = 30000;
        /**
         * 重试次数
         */
        private Integer retryCount = 3;

        public CommonIseClient build() {

            if (Objects.nonNull(paramJson)) {
                paramJson.addProperty("aue", aue);
                paramJson.addProperty("speex_size", speexSize);
                paramJson.addProperty("result_level", resultLevel);
                paramJson.addProperty("language", language);
                paramJson.addProperty("category", category);
                paramJson.addProperty("extra_ability", extraAbility);
            }

            this.connector = HttpConnector.build(maxConnections, connTimeout, soTimeout, retryCount);
            return new CommonIseClient(this);
        }

        public CommonIseClient.Builder hostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        public CommonIseClient.Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public CommonIseClient.Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public CommonIseClient.Builder paramJson(JsonObject paramJson) {
            this.paramJson = paramJson;
            return this;
        }

        public CommonIseClient.Builder aue(String aue) {
            this.aue = aue;
            this.paramJson.addProperty("aue", aue);
            return this;
        }

        public CommonIseClient.Builder speexSize(String speexSize) {
            this.speexSize = speexSize;
            this.paramJson.addProperty("speex_size", speexSize);
            return this;
        }

        public CommonIseClient.Builder resultLevel(String resultLevel) {
            this.resultLevel = resultLevel;
            this.paramJson.addProperty("result_level", resultLevel);
            return this;
        }

        public CommonIseClient.Builder language(String language) {
            this.language = language;
            this.paramJson.addProperty("language", language);
            return this;
        }

        public CommonIseClient.Builder category(String category) {
            this.category = category;
            this.paramJson.addProperty("category", category);
            return this;
        }

        public CommonIseClient.Builder extraAbility(String extraAbility) {
            this.extraAbility = extraAbility;
            this.paramJson.addProperty("extra_ability", extraAbility);
            return this;
        }

        public CommonIseClient.Builder audio(String audio) {
            this.audio = audio;
            return this;
        }

        public CommonIseClient.Builder text(String text) {
            this.text = text;
            return this;
        }

        public CommonIseClient.Builder maxConnections(Integer maxConnections) {
            this.maxConnections = maxConnections;
            return this;
        }

        public CommonIseClient.Builder connTimeout(Integer connTimeout) {
            this.connTimeout = connTimeout;
            return this;
        }

        public CommonIseClient.Builder soTimeout(Integer soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public CommonIseClient.Builder retryCount(Integer retryCount) {
            this.retryCount = retryCount;
            return this;
        }
    }
}

