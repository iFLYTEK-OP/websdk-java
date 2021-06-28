package cn.xfyun.api;

import cn.xfyun.config.Client;
import cn.xfyun.exception.HttpException;
import cn.xfyun.model.response.trans.TransResponse;
import cn.xfyun.model.sign.TranSignatrue;
import cn.xfyun.util.AuthUtil;
import cn.xfyun.util.CryptTools;
import cn.xfyun.util.HttpConnector;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.URL;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <ydwang16@iflytek.com>
 * @description 翻译客户端
 * @date 2021/6/15
 */
public class TransClient extends Client {

    /**
     * 自研机器翻译服务端地址
     */
    private static final String ITS_SERVER_URL = "https://itrans.xfyun.cn/v2/its";

    /**
     * 小牛翻译服务端地址
     */
    private static final String NIUTRANS_SERVER_URL = "https://ntrans.xfyun.cn/v2/ots";

    private HttpConnector connector;


    public String getHostUrl() {
        return hostUrl;
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
    public TransResponse sendIst(String text, String to) throws SignatureException, IOException {
        this.hostUrl = ITS_SERVER_URL;
        return sendIst(text, "cn", to);
    }

    public TransResponse sendIst(String text, String from, String to) throws SignatureException, IOException {
        this.hostUrl = ITS_SERVER_URL;
        return translate(text, from, to);
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
    public TransResponse sendNiuTrans(String text, String to) throws SignatureException, IOException {
        this.hostUrl = NIUTRANS_SERVER_URL;
        return sendNiuTrans(text, "auto", to);
    }

    public TransResponse sendNiuTrans(String text, String from, String to) throws SignatureException, IOException {
        this.hostUrl = NIUTRANS_SERVER_URL;
        return translate(text, from, to);
    }

    /**
     * 翻译
     *
     * @param text
     * @param from 源语种
     * @param to   目标语种
     * @return
     * @throws SignatureException
     * @throws IOException
     */
    private TransResponse translate(String text, String from, String to) throws SignatureException, IOException {
        String body = buildHttpBody(text, from, to);
        this.signature = new TranSignatrue(apiKey, apiSecret, hostUrl, true).setHttpBody(body);

        URL url = new URL(hostUrl);
        // 请求头
        Map<String, String> header = new HashMap<String, String>(8);
        header.put("Authorization", AuthUtil.generateTransAuthorization(signature, "hmac-sha256"));
        header.put("Content-Type", "application/json");
        header.put("Accept", "application/json,version=1.0");
        header.put("Host", url.getHost());
        header.put("Date", signature.getTs());


        // 对body进行sha256签名,生成digest头部，POST请求必须对body验证
        String digestBase64 = "SHA-256=" + CryptTools.base64Encode(DigestUtils.sha256Hex(body));
        header.put("Digest", digestBase64);

        String result = connector.postByBytes(hostUrl, header, body.getBytes());
        Gson gson = new Gson();
        return gson.fromJson(result, TransResponse.class);
    }


    /**
     * 组装http请求体
     */
    private String buildHttpBody(String text, String from, String to) {
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
        data.addProperty("text", CryptTools.base64Encode(text));
        //填充body
        body.add("common", common);
        body.add("business", business);
        body.add("data", data);
        return body.toString();
    }

    public TransClient(Builder builder) {
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.apiSecret = builder.apiSecret;
        this.hostUrl = builder.hostUrl;
        this.connector = builder.connector;
    }

    public static class Builder {
        private String appId;
        private String apiKey;
        private String apiSecret;

        private String hostUrl;

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

        private HttpConnector connector;

        public TransClient build() {
            this.connector = HttpConnector.build(maxConnections, connTimeout, soTimeout, retryCount);
            return new TransClient(this);
        }

        public TransClient.Builder signature(String appId, String apiKey, String apiSecret) {
            this.appId = appId;
            this.apiKey = apiKey;
            this.apiSecret = apiSecret;
            return this;
        }

        public TransClient.Builder hostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        public TransClient.Builder maxConnections(Integer maxConnections) {
            this.maxConnections = maxConnections;
            return this;
        }

        public TransClient.Builder connTimeout(Integer connTimeout) {
            this.connTimeout = connTimeout;
            return this;
        }

        public TransClient.Builder soTimeout(Integer soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public TransClient.Builder retryCount(Integer retryCount) {
            this.retryCount = retryCount;
            return this;
        }
    }
}
