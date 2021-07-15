package cn.xfyun.api;

import cn.xfyun.base.webscoket.WebSocketClient;
import com.google.gson.JsonObject;
import cn.xfyun.service.tts.AbstractTtsWebSocketListener;
import okhttp3.OkHttpClient;
import okhttp3.WebSocket;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Base64;

/**
 * @author yingpeng@iflytek.com
 * 在线语音合成客户端
 */
public class TtsClient extends WebSocketClient {

    /**
     * 小语种的tte值（编码格式）
     */
    private static final String MINOR_LANGUAGE = "UNICODE";

    /**
     * mp3格式的aue值
     */
    private static final String MP3 = "lame";

    /**
     * 音频编码，可选值：
     * raw：未压缩的pcm
     * lame：mp3 (当aue=lame时需传参sfl=1)
     * speex-org-wb;7： 标准开源speex（for speex_wideband，即16k）数字代表指定压缩等级（默认等级为8）
     * speex-org-nb;7： 标准开源speex（for speex_narrowband，即8k）数字代表指定压缩等级（默认等级为8）
     * speex;7：压缩格式，压缩等级1~10，默认为7（8k讯飞定制speex）
     * speex-wb;7：压缩格式，压缩等级1~10，默认为7（16k讯飞定制speex）
     */
    private String aue;

    /**
     * 需要配合aue=lame使用，开启流式返回mp3格式音频
     * 取值：1 开启
     */
    private Integer sfl;

    /**
     * 音频采样率，可选值：
     * audio/L16;rate=8000：合成8K 的音频
     * audio/L16;rate=16000：合成16K 的音频
     * auf不传值：合成16K 的音频
     */
    private String auf;

    /**
     * 发音人，可选值：请到控制台添加试用或购买发音人，添加后即显示发音人参数值
     */
    private String vcn;

    /**
     * 语速，可选值：[0-100]，默认为50
     */
    private Integer speed;

    /**
     * 音量，可选值：[0-100]，默认为50
     */
    private Integer volume;

    /**
     * 音高，可选值：[0-100]，默认为50
     */
    private Integer pitch;

    /**
     * 合成音频的背景音
     * 0:无背景音（默认值）
     * 1:有背景音
     */
    private Integer bgs;

    /**
     * 文本编码格式
     * GB2312
     * GBK
     * BIG5
     * UNICODE(小语种必须使用UNICODE编码，合成的文本需使用utf16小端的编码方式，详见java示例demo)
     * GB18030
     * UTF8
     */
    private String tte;

    /**
     * 设置英文发音方式：
     * 0：自动判断处理，如果不确定将按照英文词语拼写处理（缺省）
     * 1：所有英文按字母发音
     * 2：自动判断处理，如果不确定将按照字母朗读
     * 默认按英文单词发音
     */
    private String reg;

    /**
     * 合成音频数字发音方式
     * 0：自动判断（默认值）
     * 1：完全数值
     * 2：完全字符串
     * 3：字符串优先
     */
    private String rdn;

    /**
     * 引擎类型，可选值：
     * aisound（普通效果）
     * intp65（中文）
     * intp65_en（英文）
     * mtts（小语种，需配合小语种发音人使用）
     * x（优化效果）
     * 默认为intp65
     */
    private String ent;

    public TtsClient(Builder builder) {
        super(null);
        this.okHttpClient = new OkHttpClient().newBuilder().build();
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.apiSecret = builder.apiSecret;
        this.aue = builder.aue;
        this.sfl = builder.sfl;
        this.auf = builder.auf;
        this.vcn = builder.vcn;
        this.speed = builder.speed;
        this.volume = builder.volume;
        this.pitch = builder.pitch;
        this.bgs = builder.bgs;
        this.tte = builder.tte;
        this.reg = builder.reg;
        this.rdn = builder.rdn;
        this.ent = builder.ent;
    }

    @Override
    public String getAppId() {
        return appId;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String getApiSecret() {
        return apiSecret;
    }

    public String getAue() {
        return aue;
    }

    public Integer getSfl() {
        return sfl;
    }

    public String getAuf() {
        return auf;
    }

    public String getVcn() {
        return vcn;
    }

    public Integer getSpeed() {
        return speed;
    }

    public Integer getVolume() {
        return volume;
    }

    public Integer getPitch() {
        return pitch;
    }

    public Integer getBgs() {
        return bgs;
    }

    public String getTte() {
        return tte;
    }

    public String getReg() {
        return reg;
    }

    public String getRdn() {
        return rdn;
    }

    public String getEnt() {
        return ent;
    }

    public OkHttpClient getClient() {
        return okHttpClient;
    }

    /**
     * 在线语音合成处理方法
     *
     * @param text 合成文本
     * @throws UnsupportedEncodingException 编码异常
     */
    public void send(String text, AbstractTtsWebSocketListener webSocketListener) throws UnsupportedEncodingException, MalformedURLException, SignatureException {
        createWebSocketConnect(webSocketListener);
        //小语种必须使用UNICODE编码，合成的文本需使用utf16小端的编码方式
        if (MINOR_LANGUAGE.equals(tte)) {
            text = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_16LE));
        } else {
            text = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
        }
        //发送数据
        JsonObject frame = new JsonObject();
        JsonObject common = new JsonObject();
        JsonObject business = new JsonObject();
        JsonObject data = new JsonObject();
        //填充common
        common.addProperty("app_id", appId);
        //填充business
        business.addProperty("aue", aue);
        business.addProperty("sfl", sfl);
        business.addProperty("auf", auf);
        business.addProperty("vcn", vcn);
        business.addProperty("speed", speed);
        business.addProperty("volume", volume);
        business.addProperty("pitch", pitch);
        business.addProperty("bgs", bgs);
        business.addProperty("tte", tte);
        business.addProperty("reg", reg);
        business.addProperty("rdn", rdn);
        business.addProperty("ent", ent);

        //填充data
        data.addProperty("text", text);
        //固定位2
        data.addProperty("status", 2);
        //填充frame
        frame.add("common", common);
        frame.add("business", business);
        frame.add("data", data);
        webSocket.send(frame.toString());
    }

    @Override
    public String getSignature() {
        return null;
    }

    public static final class Builder {
        private String hostUrl = "https://tts-api.xfyun.cn/v2/tts";
        private String appId;
        private String apiKey;
        private String apiSecret;
        private String aue = "lame";
        private Integer sfl = 1;
        private String auf = "audio/L16;rate=16000";
        private String vcn = "xiaoyan";
        private Integer speed = 50;
        private Integer volume = 50;
        private Integer pitch = 50;
        private Integer bgs = 0;
        private String tte = "UTF8";
        private String reg = "0";
        private String rdn = "0";
        private String ent = "intp65";
        public TtsClient build() throws MalformedURLException, SignatureException {
            return new TtsClient(this);
        }

        public TtsClient.Builder signature(String appId, String apiKey, String apiSecret) {
            this.appId = appId;
            this.apiKey = apiKey;
            this.apiSecret = apiSecret;
            return this;
        }

        public TtsClient.Builder hostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        public TtsClient.Builder aue(String aue) {
            this.aue = aue;
            return this;
        }

        public TtsClient.Builder sfl(Integer sfl) {
            if (MP3.equals(this.aue)) {
                this.sfl = 1;
            } else {
                this.sfl = sfl;
            }
            return this;
        }

        public TtsClient.Builder auf(String auf) {
            this.auf = auf;
            return this;
        }

        public TtsClient.Builder vcn(String vcn) {
            this.vcn = vcn;
            return this;
        }

        public TtsClient.Builder speed(Integer speed) {
            this.speed = speed;
            return this;
        }

        public TtsClient.Builder volume(Integer volume) {
            this.volume = volume;
            return this;
        }

        public TtsClient.Builder pitch(Integer pitch) {
            this.pitch = pitch;
            return this;
        }

        public TtsClient.Builder bgs(Integer bgs) {
            this.bgs = bgs;
            return this;
        }

        public TtsClient.Builder tte(String tte) {
            this.tte = tte;
            return this;
        }

        public TtsClient.Builder reg(String reg) {
            this.reg = reg;
            return this;
        }

        public TtsClient.Builder rdn(String rdn) {
            this.rdn = rdn;
            return this;
        }

        public TtsClient.Builder ent(String ent) {
            this.ent = ent;
            return this;
        }
    }
}
