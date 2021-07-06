package cn.xfyun.api;

import cn.xfyun.base.http.HttpRequestBuilder;
import cn.xfyun.base.http.HttpRequestClient;
import cn.xfyun.exception.HttpException;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Map;

/**
 *      机器翻译，基于讯飞自主研发的机器翻译引擎，
 *      已经支持包括英、日、法、西、俄等70多种语言(其中维语、藏语需申请开通)，效果更优质，
 *      已在讯飞翻译机上应用并取得优异成绩，详细请参照 语种列表 。通过调用该接口，将源语种文字转化为目标语种文字
 *
 *
 *      niuTrans机器翻译2.0，基于小牛翻译自主研发的多语种机器翻译引擎，
 *      已经支持包括英、日、韩、法、西、俄等100多种语言
 *
 * @author <ydwang16@iflytek.com>
 * @description 翻译客户端
 * @date 2021/6/15
 */
public class TransClient extends HttpRequestClient {

    /**
     *   源语种
     */
    private String from;

    /**
     *  目标语种
     */
    private String to;

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    /**
     * 自研翻译，默认源语种为中文
     *
     * @param text
     * @return
     * @throws SignatureException
     * @throws IOException
     * @throws HttpException
     */
    public String sendIst(String text) throws Exception {
       return send(Builder.ITS_SERVER_URL, text);
    }

    /**
     * 小牛翻译，默认源语种为auto
     *
     * @param text
     * @return
     * @throws SignatureException
     * @throws IOException
     * @throws HttpException
     */
    public String sendNiuTrans(String text) throws Exception {
       return send(Builder.NIUTRANS_SERVER_URL, text);
    }

    private String send(String url, String text) throws Exception {
        String body = buildHttpBody(text);
        Map<String, String> header = Signature.signHttpHeaderDigest(url, "POST", apiKey, apiSecret, body);
        return sendPost(url, JSON, header, body);
    }

    /**
     * 组装http请求体
     */
    private String buildHttpBody(String text) throws UnsupportedEncodingException {
        JsonObject body = new JsonObject();
        JsonObject business = new JsonObject();
        JsonObject common = new JsonObject();
        JsonObject data = new JsonObject();
        //填充common
        common.addProperty("app_id", appId);
        //填充business
        business.addProperty("from", from);
        business.addProperty("to", to);
        //填充data
        byte[] textByte = text.getBytes("UTF-8");
        data.addProperty("text", Base64.getEncoder().encodeToString(textByte));
        //填充body
        body.add("common", common);
        body.add("business", business);
        body.add("data", data);
        return body.toString();
    }

    public TransClient(Builder builder) {
        super(builder);
        this.from = builder.from;
        this.to = builder.to;
    }

    public static final class Builder extends HttpRequestBuilder<Builder> {
        /**
         * 自研机器翻译服务端地址
         */
        private static final String ITS_SERVER_URL = "https://itrans.xfyun.cn/v2/its";

        /**
         * 小牛翻译服务端地址
         */
        private static final String NIUTRANS_SERVER_URL = "https://ntrans.xfyun.cn/v2/ots";

        private String from = "cn";

        private String to = "en";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(ITS_SERVER_URL, appId, apiKey, apiSecret);
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder to(String to) {
            this.to = to;
            return this;
        }

        @Deprecated
        @Override
        public Builder hostUrl(String hostUrl) {
            return super.hostUrl(hostUrl);
        }

        @Override
        public TransClient build() {
            return new TransClient(this);
        }
    }
}
