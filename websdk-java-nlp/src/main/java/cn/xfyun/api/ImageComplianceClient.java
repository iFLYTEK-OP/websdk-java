package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.config.ModeType;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Map;

/**
 * 图片合规 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/nlp/ImageModeration/API.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class ImageComplianceClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(ImageComplianceClient.class);

    /**
     * 指定检测的敏感分类：
     * pornDetection 色情
     * violentTerrorism 暴恐
     * political 涉政
     * lowQualityIrrigation 低质量灌水
     * contraband 违禁
     * advertisement 广告
     * uncivilizedLanguage 不文明用语
     */
    private final String bizType;

    public ImageComplianceClient(Builder builder) {
        super(builder);
        this.bizType = builder.bizType;
    }

    public String getBizType() {
        return bizType;
    }

    /**
     * 支持 PNG、JPG、JPEG、BMP、GIF、WEBP 格式。图片大小限制是20M。
     * 图片合规既审查图片画面，同时也审核图片中的文字是否合规
     * 可以添加自定义的违规图片库，同时可以指定放行的图片库。后续将会开放此功能
     *
     * @param content  待识别图片信息
     * @param modeType 文本模式
     *                 modeType为link时，值为外链信息
     *                 modeType为base64时，值为图片base64编码信息
     */
    public String send(String content, ModeType modeType) throws IOException, SignatureException {
        // 参数校验
        paramCheck(content, modeType);

        // 构建鉴权参数
        Map<String, String> parameters = Signature.getImageAuth(appId, apiKey, apiSecret, modeType);

        // 发送请求
        return sendPost(hostUrl, JSON, null, buildParam(content), parameters);
    }

    /**
     * 构建参数
     */
    private String buildParam(String content) {
        JsonObject param = new JsonObject();
        param.addProperty("content", content);
        param.addProperty("biz_type", bizType);
        logger.debug("图片合规请求参数: {}", param);
        return param.toString();
    }

    /**
     * 参数校验
     */
    private void paramCheck(String content, ModeType modeType) {
        if (StringUtils.isNullOrEmpty(content) || null == modeType) {
            throw new BusinessException("content或modeType不能为空");
        }
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://audit.iflyaisol.com/audit/v2/image";
        private String bizType;

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
        }

        @Override
        public ImageComplianceClient build() {
            return new ImageComplianceClient(this);
        }

        public Builder bizType(String bizType) {
            this.bizType = bizType;
            return this;
        }
    }
}
