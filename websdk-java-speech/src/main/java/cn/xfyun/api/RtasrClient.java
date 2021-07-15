package cn.xfyun.api;

import cn.xfyun.base.webscoket.WebSocketBuilder;
import cn.xfyun.base.webscoket.WebSocketClient;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.service.rta.AbstractRtasrWebSocketListener;
import okhttp3.*;

import java.io.*;

/**
 * 实时语音转写
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/3/17 10:24
 */
public class RtasrClient extends WebSocketClient {

    public static final String SEND_END = "{\"end\": true}";

    /*
     *  标点过滤控制，默认返回标点，punc=0会过滤结果中的标点
     */
    private String punc;

    /**
     * 垂直领域个性化参数, 设置示例：pd="edu" 参数pd为非必须设置，不设置参数默认为通用
     */
    private String pd;

    /**
     * 创建 client对象
     *
     * @param builder
     */
    public RtasrClient(Builder builder) {
        super(builder);
        this.pd = builder.pd;
        this.punc = builder.punc;
    }

    public WebSocket newWebSocket(AbstractRtasrWebSocketListener webSocketListener) {
        createWebSocketConnect(webSocketListener);
        return webSocket;
    }

    @Override
    public String getSignature() {
        return Signature.rtasrSignature(hostUrl, appId, apiKey);
    }

    public void send(AbstractRtasrWebSocketListener webSocketListener) {
        createWebSocketConnect(webSocketListener);
    }


    public void sendEnd() {
        webSocket.send(SEND_END);
        closeWebsocket();
    }

    public String getPunc() {
        return punc;
    }

    public String getPd() {
        return pd;
    }

    public static final class Builder extends WebSocketBuilder<Builder> {

        private static final String HOST_URL = "wss://rtasr.xfyun.cn/v1/ws";

        private String punc;

        private String pd;

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        public RtasrClient.Builder punc() {
            this.punc = "0";
            return this;
        }

        public RtasrClient.Builder pd(String pd) {
            this.pd = pd;
            return this;
        }


        @Override
        public RtasrClient build() {
            RtasrClient rtasrClient = new RtasrClient(this);
            return rtasrClient;
        }

    }
}
