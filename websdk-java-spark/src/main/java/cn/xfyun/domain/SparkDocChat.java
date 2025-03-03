package cn.xfyun.domain;

import cn.xfyun.model.RoleMessage;
import cn.xfyun.request.WsChatDocRequest;
import cn.xfyun.util.EasyOperation;
import cn.xfyun.util.RestOperation;
import cn.xfyun.util.SignatureHelper;
import okhttp3.Response;
import okhttp3.WebSocket;

/**
 * @author: rblu2
 * @desc: 上传星火知识库文档
 * @create: 2025-02-24 10:34
 **/
public class SparkDocChat extends WebsocketTemplate<SparkDocChat> {
    
    private static final EasyOperation.EasyLog<SparkDocChat> easyLog = EasyOperation.log(SparkDocChat.class);
    private String url = "wss://chatdoc.xfyun.cn/openapi/chat";
    private String appId;
    private String apiSecret;
    private WsChatDocRequest chatRequest;

    public static SparkDocChat prepare(String appId, String apiSecret) {
        SparkDocChat sparkChat = new SparkDocChat();
        sparkChat.appId = appId;
        sparkChat.apiSecret = apiSecret;
        sparkChat.chatRequest = new WsChatDocRequest();
        return sparkChat.prefect();
    }

    public SparkDocChat prefect() {
        long timestamp = System.currentTimeMillis() / 1000;
        String signature = SignatureHelper.getSignature(appId, apiSecret, timestamp);
        this.url += RestOperation.buildParams(EasyOperation.map().put("appId", appId).put("timestamp", timestamp).put("signature", signature).get());
        return this;
    }

    public SparkDocChat addFileId(String fileId) {
        this.chatRequest.getFileIds().add(fileId);
        return this;
    }

    public SparkDocChat sparkFlag(boolean spark) {
        this.chatRequest.getChatExtends().spark(spark);
        return this;
    }

    public SparkDocChat temperature(float temperature) {
        if(temperature <= 0f || temperature > 1f) {
            throw new RuntimeException("temperature must be in range (0,1]");
        }
        this.chatRequest.getChatExtends().temperature(temperature);
        return this;
    }

    public SparkDocChat append(RoleMessage roleMessage) {
        this.chatRequest.getMessages().add(roleMessage);
        return this;
    }


    public SparkDocChat onMessageEnding(Runnable onMessageEnding) {
        this.onMessageEnding = onMessageEnding;
        return this;
    }

    @Override
    public EasyOperation.EasyLog<SparkDocChat> easyLog() {
        return easyLog;
    }

    @Override
    protected SparkDocChat self() {
        return this;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public void pointOnOpen(WebSocket webSocket, Response response) {
        webSocket.send(EasyOperation.toJson(chatRequest));
    }

    @Override
    public boolean pointOnMessage(WebSocket webSocket, String text) {
        return endFlag(text);
    }


    public boolean endFlag(String text) {
        return EasyOperation.parseObject(text, ResponseData.class).getStatus() == 2;
    }

    @Override
    public void preExecute() {
        easyLog.trace(logger -> logger.debug("请求详情: " + "\n" + "URI          : {} " + "\n" + "Param        : {} " + "\n", url, EasyOperation.toJson(chatRequest)));
        startTime = System.currentTimeMillis();
    }

    public static class ResponseData {
        private String code;
        private Integer status;
        private String sid;

        public String getCode() {
            return code;
        }

        public Integer getStatus() {
            return status;
        }

        public String getSid() {
            return sid;
        }
    }
}
