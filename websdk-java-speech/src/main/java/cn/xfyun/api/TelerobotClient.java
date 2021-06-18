package cn.xfyun.api;

import cn.xfyun.exception.HttpException;
import cn.xfyun.model.request.telerobot.Callout;
import cn.xfyun.model.response.telerobot.TelerobotResponse;
import cn.xfyun.util.HttpConnector;
import cn.xfyun.util.StringUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * AI客服平台能力中间件
 *
 * @author : jun
 * @date : 2021年06月15日
 */
public class TelerobotClient {
    /**
     * 接口地址
     */
    private String hostUrl;

    /**
     * 接口密钥，在讯飞开放平台控制台添加相应服务后即可获取
     */
    private String appKey;

    /**
     * 接口密钥，在讯飞开放平台控制台添加相应服务后即可获取
     */
    private String appSecret;



    private HttpConnector connector;

    public TelerobotClient(TelerobotClient.Builder builder){
        this.hostUrl = builder.hostUrl;
        this.appKey = builder.appKey;
        this.appSecret = builder.appSecret;
        this.connector = builder.connector;
    }

    /**
     * 获取token
     */
    public TelerobotResponse token() throws IOException, HttpException {
        hostUrl = "https://callapi.xfyun.cn/v1/service/v1/aicall/oauth/v1/token";
        Map<String, String> body = new HashMap<>();
        body.put("app_key", appKey);
        body.put("app_secret", appSecret);
        String result = connector.postByJson(hostUrl, StringUtils.gson.toJson(body));
        System.out.println(result);
        Gson gson = new Gson();
        return gson.fromJson(result, TelerobotResponse.class);
    }

    /**
     * 查询配置
     */
    public TelerobotResponse query(String token, Integer type) throws IOException, HttpException {
        hostUrl = "https://callapi.xfyun.cn/v1/service/v1/aicall/config/v1/query?token="+token;
        Map<String, String> body = new HashMap<>();
        body.put("type", type.toString());
        String result = connector.postByJson(hostUrl, StringUtils.gson.toJson(body));
        System.out.println(result);
        Gson gson = new Gson();
        return gson.fromJson(result, TelerobotResponse.class);
    }


    /**
     * 直接外呼
     */
    public TelerobotResponse callout(String token, Callout callout) throws IOException, HttpException {
        hostUrl = "https://callapi.xfyun.cn/v1/service/v1/aicall/outbound/v1/task/callout?token="+token;
        String result = connector.postByJson(hostUrl, StringUtils.gson.toJson(callout));
        System.out.println(result);
        Gson gson = new Gson();
        return gson.fromJson(result, TelerobotResponse.class);
    }

    public static final class Builder {
        private final String appKey;
        private final String appSecret;
        private String hostUrl = "";


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

        public Builder(String appKey, String appSecret) {
            this.appKey = appKey;
            this.appSecret = appSecret;
        }

        public TelerobotClient.Builder maxConnections(Integer maxConnections) {
            this.maxConnections = maxConnections;
            return this;
        }

        public TelerobotClient.Builder connTimeout(Integer connTimeout) {
            this.connTimeout = connTimeout;
            return this;
        }

        public TelerobotClient.Builder soTimeout(Integer soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public TelerobotClient.Builder hostUrl(String hostUrl){
            this.hostUrl = hostUrl;
            return this;
        }

        public TelerobotClient.Builder retryCount(Integer retryCount){
            this.retryCount = retryCount;
            return this;
        }

        public TelerobotClient build() {
            this.connector = HttpConnector.build(maxConnections, connTimeout, soTimeout, retryCount);
            return new TelerobotClient(this);
        }
    }
}
