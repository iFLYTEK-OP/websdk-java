package cn.xfyun.domain;

import cn.xfyun.util.EasyOperation;
import cn.xfyun.util.RestOperation;
import cn.xfyun.util.SignatureHelper;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author: rblu2
 * @desc: 实时语音转写
 * @create: 2025-02-24 10:34
 **/
public class RealTimeAsr extends WebsocketTemplate<RealTimeAsr> {

    private static final EasyOperation.EasyLog<RealTimeAsr> easyLog = EasyOperation.log(RealTimeAsr.class);

    private static final String ACTION_STARTED = "started";
    private static final String ACTION_RESULT = "result";
    private static final String ACTION_ERROR = "error";
    private String url = "wss://rtasr.xfyun.cn/v1/ws";
    private String appId;
    private String apiKey;
    private Consumer<String> onResult;
    private Consumer<String> onError;
    private WebSocket webSocket;
    private String state;
    private final CountDownLatch handshake = new CountDownLatch(1);

    public static RealTimeAsr prepare(String appId, String apiKey) {
        RealTimeAsr sparkChat = new RealTimeAsr();
        sparkChat.appId = appId;
        sparkChat.apiKey = apiKey;
        sparkChat.state = "INIT";
        return sparkChat.prefect().handshake();
    }

    private RealTimeAsr prefect() {
        long timestamp = System.currentTimeMillis() / 1000;
        String signa = SignatureHelper.getSignature(appId, apiKey, timestamp);
        this.url += RestOperation.buildParams(EasyOperation.map().put("appid", appId).put("ts", timestamp).put("signa", signa).get());
        return this;
    }

    private RealTimeAsr handshake() {
        execute();
        return this;
    }

    private void await() {
        try {
            if(!handshake.await(30, TimeUnit.SECONDS)) {
                easyLog.logger().error("has not finished within the 30s");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isStarted() {
        return Objects.equals(state, ACTION_STARTED);
    }

    public boolean isResult() {
        return Objects.equals(state, ACTION_RESULT);
    }

    public boolean unprepared() {
        return !isStarted() && !isResult();
    }


    public RealTimeAsr onResult(Consumer<String> onResult) {
        this.onResult = onResult;
        return this;
    }

    public RealTimeAsr onError(Consumer<String> onError) {
        this.onError = onError;
        return this;
    }

    public void send(byte[] buffer) {
        if(unprepared()) {
            throw new RuntimeException("not ready handshake yet...");
        }

        int start = 0;
        int chunkSize = 1280;
        while (start < buffer.length) {
            // 计算当前块的结束位置
            int end = Math.min(buffer.length, start + chunkSize);
            // 计算当前块的长度
            int length = end - start;

            // 创建子数组
            byte[] chunk = new byte[length];
            // 复制数据到子数组
            System.arraycopy(buffer, start, chunk, 0, length);

            // 将子数组添加到结果列表
            webSocket.send(ByteString.of(chunk));

            EasyOperation.sleep(40, TimeUnit.MILLISECONDS);

            // 更新起始位置
            start += chunkSize;
        }

    }

    public void send(String filepath) {
        if(unprepared()) {
            throw new RuntimeException("not ready handshake yet...");
        }

        File audioFile = new File(filepath);
        if(!audioFile.exists()) {
            throw new RuntimeException("file path not exist...");
        }

        try {
            send(Files.readAllBytes(Paths.get(filepath)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            sendOver();
        }
    }

    public void sendOver() {
        webSocket.send(ByteString.of("{\"end\": true}".getBytes()));
    }


    @Override
    public EasyOperation.EasyLog<RealTimeAsr> easyLog() {
        return easyLog;
    }

    @Override
    protected RealTimeAsr self() {
        return this;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public void preExecute() {
        easyLog.trace(logger -> logger.debug("请求详情: " + "\n" + "URI          : {} " + "\n", url));
    }

    @Override
    public void afterExecute() {
        await();
    }

    @Override
    public boolean pointOnMessage(WebSocket socket, String text) {
        ResponseData data = EasyOperation.parseObject(text, ResponseData.class);
        if(data.isStarted()) {
            easyLog.logger().info("握手成功，进入实时通信阶段... ");
            state = ACTION_STARTED;
            webSocket = socket;
            handshake.countDown();
            startTime = System.currentTimeMillis();
        }

        if(data.isResult()) {
            state = ACTION_RESULT;
            EasyOperation.getOrDefault(onResult, this::defaultOnResult).accept(data.getData());
        }

        if(data.isError()) {
            state = ACTION_ERROR;
            EasyOperation.getOrDefault(onError, this::defaultOnError).accept(text);
            handshake.countDown();
            webSocket.close(1000, "正常关闭");
        }

        return data.isResult() && endFlag(data.data);
    }


    public boolean endFlag(String text) {
        return EasyOperation.readTree(text).path("ls").asBoolean(false);
    }

    @Override
    public void pointOnFailure( WebSocket webSocket, Throwable t, Response response) {
        handshake.countDown();
    }

    private Consumer<String> defaultOnResult() {
        return message -> easyLog.trace(logger -> logger.debug("Result: {}", message));
    }

    private Consumer<String> defaultOnError() {
        return message -> easyLog.logger().error("error: {}", message);
    }


    public static class ResponseData {
        private String action;
        private String code;
        private String data;
        private String desc;

        private String sid;

        public String getAction() {
            return action;
        }

        public String getCode() {
            return code;
        }

        public String getData() {
            return data;
        }

        public String getDesc() {
            return desc;
        }

        public String getSid() {
            return sid;
        }

        public boolean isStarted() {
            return Objects.equals(code, "0") && Objects.equals(action,ACTION_STARTED);
        }

        public boolean isResult() {
            return Objects.equals(code, "0") && Objects.equals(action,ACTION_RESULT);
        }

        public boolean isError() {
            return Objects.equals(action,ACTION_ERROR);
        }

    }

}
