package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.exception.HttpException;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.Map;

/**
 *   歌曲识别
 *
 */
public class QbhClient extends HttpClient {

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

    public QbhClient(Builder builder) {
        super(builder);
        this.engineType = builder.engineType;
        this.aue = builder.aue;
        this.sampleRate = builder.sampleRate;
    }

    public String send(byte[] data) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("engine_type", engineType);
        jsonObject.addProperty("aue", aue);
        jsonObject.addProperty("sample_rate", sampleRate);
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, jsonObject.toString());
        return sendPost(hostUrl, FORM, header, data);
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



    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://webqbh.xfyun.cn/v1/service/v1/qbh";

        private String engineType = "afs";

        private String aue = "raw";

        private String sampleRate = "16000";

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
            this.connectTimeout(10);
            this.readTimeout(10);
            this.writeTimeout(10);
        }


        public Builder engineType(String engineType) {
            this.engineType = engineType;
            return this;
        }

        public Builder aue(String aue) {
            this.aue = aue;
            return this;
        }

        public Builder sampleRate(String sampleRate) {
            this.sampleRate = sampleRate;
            return this;
        }

        @Override
        public QbhClient build() {
            return new QbhClient(this);
        }
    }
}

