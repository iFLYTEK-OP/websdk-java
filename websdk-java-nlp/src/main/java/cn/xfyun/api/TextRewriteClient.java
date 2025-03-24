package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.model.textrewrite.TextReWriteRequest;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;


/**
 * 文本改写 API
 *
 * @author zyding
 * @version 1.0
 * @date 2025/3/13 14:32
 */
public class TextRewriteClient extends PlatformHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(TextRewriteClient.class);

    /**
     * 改写等级
     * L1:改写等级1
     * L2:改写等级2
     * L3:改写等级3
     * L4:改写等级4
     * L5:改写等级5
     * L6:改写等级6
     */
    private final int level;

    public int getLevel() {
        return level;
    }

    public TextRewriteClient(Builder builder) {
        super(builder);
        this.level = builder.level;
    }

    /**
     * @param text 需要改写的文本
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String send(String text) throws Exception {
        return send(text, null);
    }

    /**
     * @param text  需要改写的文本
     * @param level 改写等级
     *              L1:改写等级1
     *              L2:改写等级2
     *              L3:改写等级3
     *              L4:改写等级4
     *              L5:改写等级5
     *              L6:改写等级6
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String send(String text, Integer level) throws Exception {
        if (StringUtils.isNullOrEmpty(text)) {
            throw new BusinessException("text不能为空");
        }
        String realUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        return sendPost(realUrl, JSON, null, buildParam(text, level));
    }

    private String buildParam(String text, Integer level) {
        // 发送数据,求数据均为json字符串
        TextReWriteRequest request = new TextReWriteRequest();
        // 请求头
        TextReWriteRequest.Header header = new TextReWriteRequest.Header(appId, status);
        request.setHeader(header);
        // 请求参数
        JsonObject parameter = new JsonObject();
        TextReWriteRequest.ServiceModel serviceModel = new TextReWriteRequest.ServiceModel(this);
        serviceModel.setLevel(String.format("<L%s>", null == level ? TextRewriteClient.this.level : level));
        parameter.add(this.serviceId, StringUtils.gson.toJsonTree(serviceModel));
        // 请求体
        TextReWriteRequest.Payload payload = new TextReWriteRequest.Payload(this);
        payload.getInput().setText(Base64.getEncoder().encodeToString(text.getBytes()));
        request.setPayload(payload);
        // 将参数转换成JsonObject  塞入parameter参数
        JsonObject requestObj = StringUtils.gson.toJsonTree(request).getAsJsonObject();
        requestObj.add("parameter", parameter);

        String requestStr = requestObj.toString();
        logger.debug("文本改写 API 入参：{}", requestStr);
        return requestStr;
    }


    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://api.xf-yun.com/v1/private/se3acbe7f";

        private static final String SERVICE_ID = "se3acbe7f";

        /**
         * 改写等级
         * L1:改写等级1
         * L2:改写等级2
         * L3:改写等级3
         * L4:改写等级4
         * L5:改写等级5
         * L6:改写等级6
         */
        private int level = 1;

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVICE_ID, appId, apiKey, apiSecret);
        }

        @Override
        public TextRewriteClient build() {
            return new TextRewriteClient(this);
        }

        public Builder level(Integer level) {
            this.level = level;
            return this;
        }
    }
}
