package cn.xfyun.api;

import cn.xfyun.base.http.platform.PlatformBuilder;
import cn.xfyun.base.http.platform.PlatformHttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.image.HiDreamParam;
import cn.xfyun.model.image.request.ImageHiDreamRequest;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 图片生成（Hidream） Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/spark/hidream.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class HiDreamClient extends PlatformHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(HiDreamClient.class);

    /**
     * 查询结果Url
     */
    private final String searchUrl;

    public HiDreamClient(Builder builder) {
        super(builder);
        this.searchUrl = builder.searchUrl;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    /**
     * @param param 请求参数
     * @return 返回结果
     * @throws IOException 异常错误
     */
    public String send(HiDreamParam param) throws IOException {
        // 参数校验
        paramCheck(param);

        // 获取鉴权URL
        String realUrl = Signature.signHostDateAuthorization(hostUrl, "POST", apiKey, apiSecret);

        // 获取请求体
        String body = buildParam(param, realUrl);

        // 发送请求
        return sendPost(realUrl, JSON, null, body);
    }

    /**
     * hidream图片生成结果查询
     *
     * @param taskId 任务ID
     * @throws IOException 异常信息
     */
    public String query(String taskId) throws IOException {
        // 非空校验
        if (StringUtils.isNullOrEmpty(taskId)) {
            throw new BusinessException("taskId不能为空");
        }

        // 获取鉴权URL
        String realUrl = Signature.signHostDateAuthorization(searchUrl, "POST", apiKey, apiSecret);

        // 构建请求参数
        String body = buildQuery(taskId, realUrl);

        // 发送请求
        return sendPost(realUrl, JSON, null, body);
    }

    /**
     * 构建查询参数
     */
    private String buildQuery(String taskId, String realUrl) {
        JsonObject param = new JsonObject();
        JsonObject header = new JsonObject();
        header.addProperty("app_id", appId);
        header.addProperty("task_id", taskId);
        param.add("header", header);
        logger.debug("HiDream图片生成结果查询请求URL：{}，入参：{}", realUrl, param);
        return param.toString();
    }

    /**
     * 参数校验
     */
    private void paramCheck(HiDreamParam param) {
        if (null == param) {
            throw new BusinessException("参数不能为空");
        }

        if (StringUtils.isNullOrEmpty(param.getPrompt()) &&
                (null == param.getImage() || param.getImage().isEmpty())) {
            throw new BusinessException("图片描述或参考图片不能同时为空");
        }
    }

    /**
     * 构建请求参数
     */
    private String buildParam(HiDreamParam param, String realUrl) {
        // 发送数据,请求数据均为json字符串
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
        // 设置图片详情数据
        String text = Base64.getEncoder().encodeToString(StringUtils.gson.toJson(param).getBytes(StandardCharsets.UTF_8));
        payload.getOig().setText(text);
        request.setPayload(payload);

        String json = StringUtils.gson.toJson(request);
        logger.debug("HiDream图片生成请求URL：{}，入参：{}", realUrl, json);
        return json;
    }

    public static final class Builder extends PlatformBuilder<Builder> {

        private static final String HOST_URL = "https://cn-huadong-1.xf-yun.com/v1/private/s3fd61810/create";
        private String searchUrl = "https://cn-huadong-1.xf-yun.com/v1/private/s3fd61810/query";
        private static final String SERVICE_ID = "s3fd61810";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, SERVICE_ID, appId, apiKey, apiSecret);
            this.readTimeout(60);
        }

        @Override
        public HiDreamClient build() {
            return new HiDreamClient(this);
        }

        public Builder searchUrl(String searchUrl) {
            this.searchUrl = searchUrl;
            return this;
        }

        public Builder hostUrl(String hostUrl) {
            super.hostUrl(hostUrl);
            return this;
        }
    }
}
