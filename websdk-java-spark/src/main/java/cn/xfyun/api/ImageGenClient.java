package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.config.Role;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sparkmodel.RoleContent;
import cn.xfyun.model.image.ImageGenParam;
import cn.xfyun.model.image.request.ImageGenRequest;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片生成 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/spark/ImageGeneration.html">...</a>
 *
 * @author <zyding6@ifytek.com>
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

    public ImageGenClient(Builder builder) {
        super(builder);
        this.domain = builder.domain;
        this.width = builder.width;
        this.height = builder.height;
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

    /**
     * 默认图片大小 512 * 512
     *
     * @param param 请求参数
     * @return 返回结果
     * @throws IOException 请求异常信息
     */
    public String send(ImageGenParam param) throws IOException {
        // 参数校验
        paramCheck(param);

        // 获取鉴权的URL
        String realUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);

        // 构建请求体
        String body = buildParam(param, realUrl);

        // 发送请求
        return sendPost(realUrl, JSON, null, body);
    }

    /**
     * 默认图片大小 512 * 512
     *
     * @param text 图片描述
     * @return 返回结果
     * @throws IOException 异常信息
     */
    public String send(String text) throws IOException {
        // 构建请求参数
        ImageGenParam param = buildParam(text);
        return send(param);
    }

    /**
     * 构建参数
     */
    private ImageGenParam buildParam(String text) {
        // 封装会话记录
        List<RoleContent> messages = new ArrayList<>();
        RoleContent roleContent = new RoleContent();
        roleContent.setRole(Role.USER.getValue());
        roleContent.setContent(text);
        messages.add(roleContent);
        // 返回参数
        return ImageGenParam.builder()
                .messages(messages)
                .build();
    }

    /**
     * 参数校验
     */
    private void paramCheck(ImageGenParam param) {
        if (null == param) {
            throw new BusinessException("请求参数不能为空");
        }

        if (null == param.getMessages() || param.getMessages().isEmpty()) {
            throw new BusinessException("图片描述不能为空");
        }
    }

    /**
     * 构建参数
     */
    private String buildParam(ImageGenParam param, String realUrl) {
        // 发送数据,请求数据均为json字符串
        ImageGenRequest request = new ImageGenRequest();
        // 请求头
        ImageGenRequest.Header header = new ImageGenRequest.Header();
        header.setAppId(appId);
        request.setHeader(header);

        // 请求参数
        int width = (param.getWidth() == null) ? this.width : param.getWidth();
        int height = (param.getHeight() == null) ? this.height : param.getHeight();
        ImageGenRequest.Parameter parameter = new ImageGenRequest.Parameter();
        ImageGenRequest.Parameter.Chat chat = new ImageGenRequest.Parameter.Chat();
        chat.setDomain(domain);
        chat.setWidth(width);
        chat.setHeight(height);
        parameter.setChat(chat);
        request.setParameter(parameter);

        // 请求体
        ImageGenRequest.Payload payload = new ImageGenRequest.Payload();
        ImageGenRequest.Payload.Message message = new ImageGenRequest.Payload.Message();
        message.setText(param.getMessages());
        payload.setMessage(message);
        request.setPayload(payload);

        String json = StringUtils.gson.toJson(request);
        logger.debug("图片生成请求URL: {}, 请求体: {}", realUrl, json);
        return json;
    }


    public static class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://spark-api.cn-huabei-1.xf-yun.com/v2.1/tti";
        private String domain = "general";
        private int width = 512;
        private int height = 512;

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
            this.readTimeout(60);
        }

        @Override
        public ImageGenClient build() {
            return new ImageGenClient(this);
        }

        public Builder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }
    }
}
