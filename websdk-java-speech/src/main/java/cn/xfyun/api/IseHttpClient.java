package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.config.*;
import cn.xfyun.exception.HttpException;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class IseHttpClient extends HttpClient {

    /**
     * 音频编码
     * raw（未压缩的 pcm 格式音频）
     * speex（标准开源speex）
     */
    private IseAueEnum aue;

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
    private IseResultLevelEnum resultLevel;

    /**
     * 评测语种
     * en_us（英语）
     * zh_cn（汉语）
     */
    private IseLanguageEnum language;

    /**
     * 评测题型
     * read_syllable（单字朗读，汉语专有）
     * read_word（词语朗读）
     * read_sentence（句子朗读）
     * read_chapter(篇章朗读)
     */
    private IseCategoryEnum category;

    /**
     * 拓展能力
     * multi_dimension(全维度 )
     */
    private String extraAbility;

    public IseHttpClient(Builder builder) {
        super(builder);
        this.aue = builder.aue;
        this.speexSize = builder.speexSize;
        this.resultLevel = builder.resultLevel;
        this.language = builder.language;
        this.category = builder.category;
        this.extraAbility = builder.extraAbility;
    }

    public String send(String audioBase64, String text) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("aue", aue.getValue());
        jsonObject.addProperty("language", language.getValue());
        jsonObject.addProperty("category", category.getValue());
        if (aue.equals(IseAueEnum.SPEEX)) {
            jsonObject.addProperty("speex_size", speexSize);
        }
        if (Objects.nonNull(extraAbility)) {
            jsonObject.addProperty("extra_ability", extraAbility);
        }
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, jsonObject.toString());
        return sendPost(hostUrl, FORM, header, "audio=" + audioBase64 +"&text=" + text);
    }

    public IseAueEnum getAue() {
        return aue;
    }

    public String getSpeexSize() {
        return speexSize;
    }

    public IseResultLevelEnum getResultLevel() {
        return resultLevel;
    }

    public IseLanguageEnum getLanguage() {
        return language;
    }

    public IseCategoryEnum getCategory() {
        return category;
    }

    public String getExtraAbility() {
        return extraAbility;
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://api.xfyun.cn/v1/service/v1/ise";

        private IseAueEnum aue;

        private String speexSize = "70";

        private IseResultLevelEnum resultLevel = IseResultLevelEnum.ENTIRETY;

        private IseLanguageEnum language;

        private IseCategoryEnum category;

        private String extraAbility;

        public Builder(String appId, String apiKey, IseAueEnum aue, IseLanguageEnum language, IseCategoryEnum category) {
            super(HOST_URL, appId, apiKey, null);
            this.aue = aue;
            this.language = language;
            this.category = category;
        }

        public Builder aue(IseAueEnum aue) {
            this.aue = aue;
            return this;
        }

        public Builder speexSize(String speexSize) {
            this.speexSize = speexSize;
            return this;
        }

        public Builder resultLevel() {
            this.resultLevel = IseResultLevelEnum.SIMPLE;
            return this;
        }

        public Builder language(IseLanguageEnum language) {
            this.language = language;
            return this;
        }

        public Builder category(IseCategoryEnum category) {
            this.category = category;
            return this;
        }

        public Builder extraAbility() {
            this.extraAbility = "multi_dimension";
            return this;
        }

        @Override
        public IseHttpClient build() {
            return new IseHttpClient(this);
        }
    }
}

