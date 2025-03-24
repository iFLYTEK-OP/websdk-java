package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 图片合规 API
 *
 * @author zyding
 * @version 1.0
 * @date 2025/3/14 9:33
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
     * @return
     */
    public String send(String content, String modeType) throws Exception {
        if (StringUtils.isNullOrEmpty(content) || StringUtils.isNullOrEmpty(modeType)) {
            throw new BusinessException("content或modeType不能为空");
        }
        // 参数
        JsonObject param = new JsonObject();
        param.addProperty("content", content);
        param.addProperty("biz_type", bizType);
        Map<String, String> parameters = Signature.getImageAuth(appId, apiKey, apiSecret, modeType);
        return sendPost(hostUrl, JSON, null, param.toString(), parameters);
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
