package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.RoleContent;
import cn.xfyun.model.image.request.ImageGenRequest;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 图片生成 Client
 *
 * @author zyding6
 */
public class ImageGenClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(ImageGenClient.class);

    /**
     * 图片的宽度
     * 分辨率（width * height）	图点数(计费规则)
     * 512x512	                  6
     * 640x360	                  6
     * 640x480	                  6
     * 640x640	                  7
     * 680x512	                  7
     * 512x680	                  7
     * 768x768	                  8
     * 720x1280	                  12
     * 1280x720	                  12
     * 1024x1024	              14
     */
    private final int width;

    /**
     * 图片的高度
     * 分辨率（width * height）	图点数(计费规则)
     * 512x512	                  6
     * 640x360	                  6
     * 640x480	                  6
     * 640x640	                  7
     * 680x512	                  7
     * 512x680	                  7
     * 768x768	                  8
     * 720x1280	                  12
     * 1280x720	                  12
     * 1024x1024	              14
     */
    private final int height;

    /**
     * 图片生成的域
     * general
     */
    private final String domain;

    /**
     * 是否打印日志
     */
    private final boolean logRequest;

    public ImageGenClient(Builder builder) {
        super(builder);
        this.height = builder.height;
        this.width = builder.width;
        this.domain = builder.domain;
        this.logRequest = builder.logRequest;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getDomain() {
        return domain;
    }

    public boolean getLogRequest() {
        return logRequest;
    }

    /**
     * @param messages 图片描述
     * @param width    图片宽度
     * @param height   图片高度
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String send(List<RoleContent> messages, Integer width, Integer height) throws Exception {
        if (null == messages || messages.isEmpty()) {
            throw new BusinessException("图片描述不能为空");
        }
        String realUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        String body = buildParam(messages, width, height);
        if (this.logRequest) {
            logger.info("图片生成请求URL: {}, 请求体: {}", realUrl, body);
        }
        return sendPost(realUrl, JSON, null, body);
    }

    /**
     * 默认图片大小 512 * 512
     *
     * @param messages 描述会话记录
     * @return 返回结果
     * @throws Exception 错误信息
     */
    public String send(List<RoleContent> messages) throws Exception {
        return send(messages, null, null);
    }

    private String buildParam(List<RoleContent> messages, Integer width, Integer height) {
        // 发送数据,求数据均为json字符串
        ImageGenRequest request = new ImageGenRequest();
        // 请求头
        ImageGenRequest.Header header = new ImageGenRequest.Header();
        header.setAppId(appId);
        request.setHeader(header);

        // 请求参数
        ImageGenRequest.Parameter parameter = new ImageGenRequest.Parameter();
        ImageGenRequest.Parameter.Chat chat = new ImageGenRequest.Parameter.Chat();
        chat.setDomain(domain);
        chat.setWidth(null == width ? this.width : width);
        chat.setHeight(null == height ? this.height : height);
        parameter.setChat(chat);
        request.setParameter(parameter);

        // 请求体
        ImageGenRequest.Payload payload = new ImageGenRequest.Payload();
        ImageGenRequest.Payload.Message message = new ImageGenRequest.Payload.Message();
        message.setText(messages);
        payload.setMessage(message);
        request.setPayload(payload);
        return StringUtils.gson.toJson(request);
    }


    public static class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://spark-api.cn-huabei-1.xf-yun.com/v2.1/tti";

        private int height = 512;

        private int width = 512;

        private String domain = "general";

        private boolean logRequest = false;

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
        }

        @Override
        public ImageGenClient build() {
            return new ImageGenClient(this);
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public Builder logRequest(boolean logRequest) {
            this.logRequest = logRequest;
            return this;
        }
    }
}
