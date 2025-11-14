package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.compliance.image.ImageCompParam;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import okhttp3.RequestBody;
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

    public ImageComplianceClient(Builder builder) {
        super(builder);
    }

    /**
     * 支持 PNG、JPG、JPEG、BMP、GIF、WEBP 格式。图片大小限制是20M。
     * 图片合规既审查图片画面，同时也审核图片中的文字是否合规
     * 可以添加自定义的违规图片库，同时可以指定放行的图片库。后续将会开放此功能
     *
     * @param param  入参
     */
    public String send(ImageCompParam param) throws IOException, SignatureException {
        // 参数校验
        paramCheck(param);

        // 构建鉴权参数
        Map<String, String> parameters = Signature.getImageAuth(appId, apiKey, apiSecret, param.getModeType());

        // 构建请求体
        RequestBody body = RequestBody.create(JSON, buildParam(param));

        // 发送请求
        return sendPost(hostUrl, null, body, parameters);
    }

    /**
     * 构建参数
     */
    private String buildParam(ImageCompParam param) {
        JsonObject request = new JsonObject();
        request.addProperty("content", param.getContent());
        request.addProperty("biz_type", param.getBizType());
        logger.debug("图片合规请求参数: {}", request);
        return request.toString();
    }

    /**
     * 参数校验
     */
    private void paramCheck(ImageCompParam param) {
        if (null == param) {
            throw new BusinessException("参数不能为空");
        }
        if (StringUtils.isNullOrEmpty(param.getContent())) {
            throw new BusinessException("content不能为空");
        }
        if (null == param.getModeType()) {
            throw new BusinessException("modeType不能为空");
        }
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://audit.iflyaisol.com/audit/v2/image";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
        }

        @Override
        public ImageComplianceClient build() {
            return new ImageComplianceClient(this);
        }
    }
}
