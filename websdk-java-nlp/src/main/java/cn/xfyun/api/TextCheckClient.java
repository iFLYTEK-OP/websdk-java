package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;

/**
 * 文本纠错 API
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/6/8 14:32
 */
public class TextCheckClient extends PlatformHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(TextCheckClient.class);

    public TextCheckClient(TextCheckClient.Builder builder) {
        super(builder);
    }


    public String send(String text) throws Exception {
        if (StringUtils.isNullOrEmpty(text)) {
            throw new BusinessException("text不能为空");
        }
        String realUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        return sendPost(realUrl, JSON, null, buildParam(text));
    }

    private String buildParam(String text) throws IOException {
        JsonObject req = new JsonObject();
        // 平台参数
        req.add("header", buildHeader());
        // 功能参数
        JsonObject parameter = new JsonObject();

        JsonObject result = new JsonObject();

        result.add("result", buildResult());
        parameter.add(this.serviceId, result);
        // 请求数据
        JsonObject payload = new JsonObject();
        JsonObject input = new JsonObject();
        // jpg:jpg格式,jpeg:jpeg格式,png:png格式,bmp:bmp格式
        input.addProperty("encoding", encoding);
        // raw,gzip
        input.addProperty("compress", compress);
        // plain,json,xml
        input.addProperty("format", format);
        // 3:一次性传完
        input.addProperty("status", status);
        // 文本数据，base64
        input.addProperty("text", Base64.getEncoder().encodeToString(text.getBytes("UTF-8")));
        payload.add("input", input);

        req.add("parameter", parameter);
        req.add("payload", payload);

        logger.debug("文本纠错请求入参：{}", req.toString());
        return req.toString();
    }


    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/s9a87e3ec";

        private static final String SERVICE_ID = "s9a87e3ec";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVICE_ID, appId, apiKey, apiSecret);
        }

        @Override
        public TextCheckClient build() {
            TextCheckClient client = new TextCheckClient(this);
            return client;
        }

    }


}


