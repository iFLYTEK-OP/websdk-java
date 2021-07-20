package cn.xfyun.base;


/**
 * @author <ydwang16@iflytek.com>
 * @description client父类
 * @date 2021/6/15
 */
public class Client {
    
    protected String hostUrl;

    protected String appId;

    protected String apiKey;

    protected String apiSecret;


    public String getHostUrl() {
        return hostUrl;
    }

    public String getAppId() {
        return appId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }
}
