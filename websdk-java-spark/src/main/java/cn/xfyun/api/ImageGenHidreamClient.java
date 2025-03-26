package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.image.ImageInfo;
import cn.xfyun.model.image.request.ImageHiDreamRequest;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.List;

/**
 * 图片生成（Hidream） Client
 *
 * @author zyding6
 */
public class ImageGenHidreamClient extends PlatformHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(ImageGenHidreamClient.class);

    /**
     * 图片比例
     * 默认1：1
     */
    private final String aspectRatio;

    /**
     * 禁止生成的提示词
     * 字符长度0 ~ 2000的字符串
     */
    private final String negativePrompt;

    /**
     * 一次生成的图片数量
     * 默认1
     */
    private final int imgCount;

    /**
     * 生成图片的分辨率 (目前仅支持2k)
     */
    private final String resolution;

    /**
     * 查询结果Url
     */
    private final String searchUrl;

    /**
     * 参考图片路劲
     * url或者base64
     */
    private final List<String> referenceImages;

    /**
     * 是否打印日志
     */
    private final boolean logRequest;

    public ImageGenHidreamClient(Builder builder) {
        super(builder);
        this.aspectRatio = builder.aspectRatio;
        this.negativePrompt = builder.negativePrompt;
        this.imgCount = builder.imgCount;
        this.resolution = builder.resolution;
        this.searchUrl = builder.searchUrl;
        this.referenceImages = builder.referenceImages;
        this.logRequest = builder.logRequest;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public String getNegativePrompt() {
        return negativePrompt;
    }

    public int getImgCount() {
        return imgCount;
    }

    public String getResolution() {
        return resolution;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public List<String> getReferenceImages() {
        return referenceImages;
    }

    public boolean getLogRequest() {
        return logRequest;
    }

    public String send(String prompt) throws Exception {
        return send(null, prompt, null, null, null);
    }

    public String send(String prompt, String negativePrompt) throws Exception {
        return send(null, prompt, negativePrompt, null, null);
    }

    public String send(String prompt, String negativePrompt, int imgCount) throws Exception {
        return send(null, prompt, negativePrompt, null, imgCount);
    }

    /**
     * hidream图片生成方法
     *
     * @param referenceImages 参考图片内容（可以多张） ，数组内容为url或者base64
     * @param prompt          图片描述     字符长度0 ~ 2000的字符串
     * @param negativePrompt  负面提示词   字符长度0 ~ 2000的字符串
     * @param aspectRatio     图片比例 1:1
     * @param imgCount        一次生成图片的数量[1,4]
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String send(List<String> referenceImages, String prompt, String negativePrompt, String aspectRatio, Integer imgCount) throws Exception {
        if (StringUtils.isNullOrEmpty(prompt)) {
            throw new BusinessException("图片描述不能为空");
        }
        String realUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);
        String body = buildParam(referenceImages, prompt, negativePrompt, aspectRatio, imgCount);
        if (this.logRequest) {
            logger.info("HiDream图片生成请求URL：{}，入参：{}", realUrl, body);
        }
        return sendPost(realUrl, JSON, null, body);
    }

    /**
     * hidream图片生成结果查询
     *
     * @param taskId 任务ID
     * @throws Exception 异常信息
     */
    public String query(String taskId) throws Exception {
        if (StringUtils.isNullOrEmpty(taskId)) {
            throw new BusinessException("taskId不能为空");
        }
        String realUrl = Signature.signHostDateAuthorization(searchUrl, "POST", apiKey, apiSecret);
        JsonObject param = new JsonObject();
        JsonObject header = new JsonObject();
        header.addProperty("app_id", appId);
        header.addProperty("task_id", taskId);
        param.add("header", header);
        if (this.logRequest) {
            logger.info("HiDream图片生成结果查询请求URL：{}，入参：{}", realUrl, param);
        }
        return sendPost(realUrl, JSON, null, param.toString());
    }

    private String buildParam(List<String> referenceImages, String prompt, String negativePrompt, String aspectRatio, Integer imgCount) {
        // 发送数据,求数据均为json字符串
        ImageHiDreamRequest request = new ImageHiDreamRequest();
        // 请求头
        ImageHiDreamRequest.Header header = new ImageHiDreamRequest.Header();
        header.setAppId(appId);
        header.setStatus(status);
        header.setChannel("default");
        header.setCallbackUrl("default");
        request.setHeader(header);

        // 请求参数
        ImageHiDreamRequest.Parameter parameter = new ImageHiDreamRequest.Parameter(this);
        request.setParameter(parameter);

        // 请求体
        ImageHiDreamRequest.Payload payload = new ImageHiDreamRequest.Payload(this);
        // 图片详情
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setImage(referenceImages);
        imageInfo.setAspectRatio(StringUtils.isNullOrEmpty(aspectRatio) ? this.aspectRatio : aspectRatio);
        imageInfo.setImgCount(null == imgCount ? this.imgCount : imgCount);
        imageInfo.setPrompt(prompt);
        imageInfo.setResolution(resolution);
        imageInfo.setNegativePrompt(StringUtils.isNullOrEmpty(negativePrompt) ? this.negativePrompt : negativePrompt);
        // 设置图片详情数据
        String text = Base64.getEncoder().encodeToString(StringUtils.gson.toJson(imageInfo).getBytes());
        payload.getOig().setText(text);
        request.setPayload(payload);
        return StringUtils.gson.toJson(request);
    }


    public static final class Builder extends PlatformBuilder<Builder> {
        private static final String HOST_URL = "https://cn-huadong-1.xf-yun.com/v1/private/s3fd61810/create";
        private String searchUrl = "https://cn-huadong-1.xf-yun.com/v1/private/s3fd61810/query";
        private static final String SERVICE_ID = "s3fd61810";
        private String aspectRatio = "1:1";
        private String negativePrompt;
        private int imgCount = 1;
        private String resolution = "2k";
        private List<String> referenceImages;
        private boolean logRequest = false;

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVICE_ID, appId, apiKey, apiSecret);
        }

        @Override
        public ImageGenHidreamClient build() {
            return new ImageGenHidreamClient(this);
        }

        public Builder aspectRatio(String aspectRatio) {
            this.aspectRatio = aspectRatio;
            return this;
        }

        public Builder negativePrompt(String negativePrompt) {
            this.negativePrompt = negativePrompt;
            return this;
        }

        public Builder imgCount(int imgCount) {
            this.imgCount = imgCount;
            return this;
        }

        public Builder resolution(String resolution) {
            this.resolution = resolution;
            return this;
        }

        public Builder searchUrl(String searchUrl) {
            this.searchUrl = searchUrl;
            return this;
        }

        public Builder referenceImages(List<String> referenceImages) {
            this.referenceImages = referenceImages;
            return this;
        }

        public Builder logRequest(boolean logRequest) {
            this.logRequest = logRequest;
            return this;
        }
    }
}
