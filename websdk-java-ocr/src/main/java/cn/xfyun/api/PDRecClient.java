package cn.xfyun.api;

import cn.xfyun.base.websocket.AbstractClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.common.request.RequestHeader;
import cn.xfyun.model.common.request.Result;
import cn.xfyun.model.document.PDRecParam;
import cn.xfyun.model.document.request.PDRecRequest;
import cn.xfyun.model.document.response.PDRecResponse;
import cn.xfyun.service.document.AbstractPDRecWebSocketListener;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.internal.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * （Picture Document Reconstruction）图片文档还原 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/words/printed-word-recognition/API.html">...</a>
 *
 * @author zyding6
 */
public class PDRecClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(PDRecClient.class);

    /**
     * ch_en_public_cloud：中英文识别
     */
    private final String category;

    /**
     * 文本编码
     * 可选值：utf8(默认)、 gb2312
     */
    private final String encoding;

    /**
     * 文本压缩格式
     * 可选值：raw(默认)、 gzip
     */
    private final String compress;

    /**
     * 文本格式
     * 可选值：plain(默认)、json、 xml
     */
    private final String format;

    public PDRecClient(Builder builder) {
        this.okHttpClient = new OkHttpClient
                .Builder()
                .callTimeout(builder.callTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(builder.callTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(builder.readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(builder.writeTimeout, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(builder.retryOnConnectionFailure)
                .build();
        this.originHostUrl = builder.hostUrl;
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.apiSecret = builder.apiSecret;

        this.category = builder.category;
        this.encoding = builder.encoding;
        this.compress = builder.compress;
        this.format = builder.format;

        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.callTimeout = builder.callTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
    }

    public String getCategory() {
        return category;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getCompress() {
        return compress;
    }

    public String getFormat() {
        return format;
    }

    /**
     * @param param 请求参数
     */
    public byte[] send(PDRecParam param) throws SignatureException, IOException {
        // 参数校验
        paramCheck(param);

        // 创建一个 CompletableFuture 对象
        CompletableFuture<byte[]> future = new CompletableFuture<>();

        // 初始化链接client
        newWebSocket(getWebSocketListener(param, future));

        // 等待socket完成
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 参数校验
     */
    private void paramCheck(PDRecParam param) {
        if (null == param) {
            throw new BusinessException("参数不能为空");
        } else if (StringUtils.isNullOrEmpty(param.getImgBase64())) {
            throw new BusinessException("图片内容不能为空");
        } else if (StringUtils.isNullOrEmpty(param.getResultType())) {
            throw new BusinessException("文档识别类型不能为空");
        } else if (StringUtils.isNullOrEmpty(param.getImgFormat())) {
            throw new BusinessException("图片格式不能为空");
        }
    }

    private AbstractPDRecWebSocketListener getWebSocketListener(PDRecParam param,
                                                                CompletableFuture<byte[]> future) throws IOException {
        return new AbstractPDRecWebSocketListener(param.getDstFile()) {
            @Override
            public void onSuccess(byte[] bytes) {
                if (null != param.getDstFile()) {
                    logger.debug("图片存储成功：{}", param.getDstFile().getAbsolutePath());
                }
                future.complete(bytes);
            }

            @Override
            public void onClose(WebSocket webSocket, int code, String reason) {
                if (!future.isDone()) {
                    future.complete(new byte[]{});
                }
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                if (!future.isDone()) {
                    future.completeExceptionally(t);
                }
            }

            @Override
            public void onBusinessFail(WebSocket webSocket, PDRecResponse response) {
                String message = response.getHeader().getMessage();
                Integer code = response.getHeader().getCode();
                if (!future.isDone()) {
                    future.completeExceptionally(new BusinessException(String.format("Error: %s (CODE=%s)", message, code)));
                }
                webSocket.close(1011, "业务处理失败");
            }

            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                // 发送数据,请求结果
                webSocket.send(getParam(param));
            }
        };
    }

    private String getParam(PDRecParam param) {
        // 发送数据, 请求数据均为json字符串
        PDRecRequest request = new PDRecRequest();

        // 请求头
        RequestHeader header = new RequestHeader();
        header.setAppId(appId);
        header.setStatus(2);
        request.setHeader(header);

        // 请求体
        PDRecRequest.Payload payload = new PDRecRequest.Payload();
        PDRecRequest.Payload.Test test = new PDRecRequest.Payload.Test();
        test.setEncoding(param.getImgFormat());
        test.setImage(param.getImgBase64());
        test.setStatus(3);
        payload.setTest(test);
        request.setPayload(payload);

        // 请求参数
        JsonObject parameter = new JsonObject();
        JsonObject innerParam1 = new JsonObject();
        JsonObject innerParam2 = new JsonObject();
        Result result = new Result(encoding, compress, format);

        innerParam1.addProperty("category", "ch_en_public_cloud");
        innerParam1.add("result", StringUtils.gson.toJsonTree(result));
        parameter.add("s15282f39", innerParam1);

        innerParam2.addProperty("result_type", param.getResultType());
        innerParam2.add("result", StringUtils.gson.toJsonTree(result));
        parameter.add("s5eac762f", innerParam2);

        JsonElement jsonTree = StringUtils.gson.toJsonTree(request);
        jsonTree.getAsJsonObject().add("parameter", parameter);

        logger.debug("图片转文档ws请求参数：{}", jsonTree);
        return jsonTree.toString();
    }

    public static final class Builder {

        private final String hostUrl = "https://ws-api.xf-yun.com/v1/private/ma008db16";
        /**
         * websocket相关
         */
        boolean retryOnConnectionFailure = true;
        int callTimeout = 0;
        int connectTimeout = 60000;
        int readTimeout = 60000;
        int writeTimeout = 60000;
        int pingInterval = 0;
        private String appId;
        private String apiKey;
        private String apiSecret;
        /**
         * ch_en_public_cloud：中英文识别
         */
        private String category;
        /**
         * 文本编码
         * 可选值：utf8(默认)、 gb2312
         */
        private String encoding = "utf8";
        /**
         * 文本压缩格式
         * 可选值：raw(默认)、 gzip
         */
        private String compress = "raw";
        /**
         * 文本格式
         * 可选值：plain(默认)、json、 xml
         */
        private String format = "plain";

        public PDRecClient build() throws MalformedURLException, SignatureException {
            return new PDRecClient(this);
        }

        public Builder signature(String appId, String apiKey, String apiSecret) {
            this.appId = appId;
            this.apiKey = apiKey;
            this.apiSecret = apiSecret;
            return this;
        }

        public Builder callTimeout(long timeout, TimeUnit unit) {
            this.callTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder connectTimeout(long timeout, TimeUnit unit) {
            this.connectTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder readTimeout(long timeout, TimeUnit unit) {
            this.readTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder writeTimeout(long timeout, TimeUnit unit) {
            this.writeTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public Builder pingInterval(long interval, TimeUnit unit) {
            this.pingInterval = Util.checkDuration("interval", interval, unit);
            return this;
        }

        public Builder retryOnConnectionFailure(boolean retryOnConnectionFailure) {
            this.retryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder encoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public Builder compress(String compress) {
            this.compress = compress;
            return this;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }
    }
}
