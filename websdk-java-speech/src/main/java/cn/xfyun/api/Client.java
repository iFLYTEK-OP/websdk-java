package cn.xfyun.api;

import cn.xfyun.model.sign.AbstractSignature;

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

    protected AbstractSignature signature;
}
