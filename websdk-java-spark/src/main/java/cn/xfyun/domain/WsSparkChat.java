package cn.xfyun.domain;

import cn.xfyun.config.SparkModelEum;
import cn.xfyun.model.RoleMessage;
import cn.xfyun.request.WsChatRequest;
import cn.xfyun.util.EasyOperation;
import cn.xfyun.util.SignatureHelper;
import okhttp3.Response;
import okhttp3.WebSocket;

import java.util.Objects;

/**
 * @author: rblu2
 * @desc: websocket 星火对话
 * @create: 2025-02-20 13:59
 **/
public class WsSparkChat extends WebsocketTemplate<WsSparkChat> {

    private static final EasyOperation.EasyLog<WsSparkChat> easyLog = EasyOperation.log(WsSparkChat.class);
    private SparkModelEum modelEum;
    private String url;
    private String apiKey;
    private String apiSecret;
    private WsChatRequest chatRequest;
    private Runnable onMessageEnding;
    private long startTime;

    public static WsSparkChat prepare(SparkModelEum modelEum, String appId, String apiKey, String apiSecret) {
        WsSparkChat sparkChat = new WsSparkChat();
        sparkChat.modelEum = modelEum;
        sparkChat.apiKey = apiKey;
        sparkChat.apiSecret = apiSecret;
        sparkChat.chatRequest = WsChatRequest.create(appId, modelEum.getCode());
        return sparkChat.prefect();
    }
    
    public WsSparkChat prefect() {
        String httpUrl = modelEum.getWsUrl().replace("ws://", "http://").replace("wss://", "https://");
        this.url = SignatureHelper.getAuthUrl(httpUrl, apiKey, apiSecret);
        return this;
    }

    public WsSparkChat append(RoleMessage roleMessage) {
        this.chatRequest.getPayload().getMessage().getText().add(roleMessage);
        return this;
    }
    
    public WsSparkChat temperature(float temperature) {
        this.chatRequest.getParameter().getChat().temperature(temperature);
        return this;
    }

    public WsSparkChat max_tokens(int max_tokens){
        if(modelEum == SparkModelEum.LITE || modelEum == SparkModelEum.PRO_128K) {
            if(max_tokens < 1 || max_tokens > 4096) {
                throw new IllegalArgumentException("max_tokens must be in range [1,4096]");
            }
        }

        if(modelEum == SparkModelEum.MAX_32K || modelEum == SparkModelEum.V4_ULTRA) {
            if(max_tokens < 1 || max_tokens > 8192) {
                throw new IllegalArgumentException("max_tokens must be in range [1,8192]");
            }
        }

        this.chatRequest.getParameter().getChat().max_tokens(max_tokens);
        return this;
    }

    public WsSparkChat top_k(int top_k){
        if(top_k < 1 || top_k > 6) {
            throw new IllegalArgumentException("top_k must be in range [1,6]");
        }
        this.chatRequest.getParameter().getChat().top_k(top_k);
        return this;
    }

    public WsSparkChat webSearch() {
        if(modelEum != SparkModelEum.MAX_32K && modelEum != SparkModelEum.GENERAL_V35 && modelEum != SparkModelEum.V4_ULTRA) {
            throw new IllegalArgumentException("At present,webSearch support by spark as " + SparkModelEum.MAX_32K.getCode() + ", " + SparkModelEum.GENERAL_V35.getCode() + ", " + SparkModelEum.V4_ULTRA.getCode());
        }
        this.chatRequest.getParameter().getChat().show_ref_label(true);
        return this;
    }

    public WsSparkChat onMessageEnding(Runnable onMessageEnding) {
        this.onMessageEnding = onMessageEnding;
        return this;
    }

    private Runnable defaultOnMessageEnding() {
        return () -> easyLog.trace(logger -> logger.debug("所有消息接收完毕... cost {}ms", System.currentTimeMillis() - startTime));
    }


    @Override
    public EasyOperation.EasyLog<WsSparkChat> easyLog() {
        return easyLog;
    }

    @Override
    protected WsSparkChat self() {
        return this;
    }

    @Override
    public String url() {
        return this.url;
    }

    @Override
    public void pointOnOpen(WebSocket webSocket, Response response) {
        webSocket.send(EasyOperation.toJson(chatRequest));
    }

    @Override
    public void pointOnMessage(WebSocket webSocket, String text) {
        //结束标志
        if(endFlag(text)) {
            if(Objects.isNull(onMessageEnding)) {
                onMessageEnding = defaultOnMessageEnding();
            }
            onMessageEnding.run();
            webSocket.close(1000, "正常关闭");
        }
    }

    @Override
    public void preExecute() {
        easyLog.trace(logger -> logger.debug("请求详情: " + "\n" + "URI          : {} " + "\n" + "Param        : {} " + "\n", url, EasyOperation.toJson(chatRequest)));
        startTime = System.currentTimeMillis();
    }

    private boolean endFlag(String text) {
        return EasyOperation.parseObject(text, SparkChatResponse.class).getHeader().getStatus() == 2;
    }

    public static class SparkChatResponse {
        private Header header;
        public Header getHeader() {
            return header;
        }

        public static class Header {
            private String code;
            private String message;
            private String sid;
            private Integer status;

            public String getCode() {
                return code;
            }
            public String getMessage() {
                return message;
            }
            public String getSid() {
                return sid;
            }
            public Integer getStatus() {
                return status;
            }

        }
    }

}
