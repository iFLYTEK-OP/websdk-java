package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.config.WorkFlowEnum;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.agent.AgentChatParam;
import cn.xfyun.model.agent.AgentResumeParam;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 工作流（Agent） Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/spark/Agent01-%E5%B9%B3%E5%8F%B0%E4%BB%8B%E7%BB%8D.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */

public class AgentClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(AgentClient.class);

    public AgentClient(Builder builder) {
        super(builder);
    }

    /**
     * 执行工作流(流式)
     *
     * @param param    参数
     * @param callback 流式回调逻辑
     */
    public void completion(AgentChatParam param, Callback callback) {
        // 参数校验
        paramCheck(param);

        // 流式请求参数设置
        param.setStream(Boolean.TRUE);

        // 构建sse请求
        Request sseRequest = getSseRequest(WorkFlowEnum.COMPLETIONS.getUrl(), param.toJSONString());

        // 发送请求
        okHttpClient.newCall(sseRequest).enqueue(callback);
    }

    /**
     * 执行工作流(非流式)
     *
     * @param param 参数
     */
    public String completion(AgentChatParam param) throws IOException {
        // 参数校验
        paramCheck(param);

        // 流式请求参数设置
        param.setStream(Boolean.FALSE);

        // 构建JSON类型请求体
        RequestBody body = RequestBody.create(JSON, param.toJSONString());

        // 发送请求
        return send(WorkFlowEnum.COMPLETIONS, body);
    }

    /**
     * 恢复工作流(流式)
     *
     * @param param    参数
     * @param callback 流式回调逻辑
     */
    public void resume(AgentResumeParam param, Callback callback) {
        // 参数校验
        paramCheck(param);

        // 构建sse请求
        Request sseRequest = getSseRequest(WorkFlowEnum.RESUME.getUrl(), param.toJSONString());

        // 发送请求
        okHttpClient.newCall(sseRequest).enqueue(callback);
    }

    /**
     * 文件上传
     *
     * @param file 需要上传的文件
     */
    public String uploadFile(File file) throws IOException {
        // 参数校验
        paramCheck(file);

        // 构建JSON类型请求体
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MultipartBody.FORM, file))
                .build();

        // 发送请求
        return send(WorkFlowEnum.UPLOAD_FILE, body);
    }

    /**
     * 构建sse请求Request
     *
     * @return sseRequest
     */
    private Request getSseRequest(String url, String body) {
        HttpUrl.Builder urlBuilder = Objects
                .requireNonNull(HttpUrl.parse(url), "请求地址错误：" + url)
                .newBuilder();
        Request.Builder builder = new Request
                .Builder()
                .url(urlBuilder.build().toString())
                .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), body));
        builder.addHeader("Authorization", "Bearer " + apiKey + ":" + apiSecret);
        builder.addHeader("Accept", "text/event-stream");
        return builder.build();
    }

    /**
     * 发送请求
     */
    private String send(WorkFlowEnum workFlowEnum, RequestBody body, Object... param) throws IOException {
        // 请求头
        Map<String, String> header = new HashMap<>(6);
        header.put("Authorization", "Bearer " + apiKey + ":" + apiSecret);

        // 拼接参数获取url
        String url = workFlowEnum.getUrl();
        if (param != null) {
            url = String.format(workFlowEnum.getUrl(), param);
        }
        // 请求结果
        logger.debug("{}请求URL：{}，入参：{}", workFlowEnum.getDesc(), url, null == body ? "" : body.toString());
        return sendRequest(url, workFlowEnum.getMethod(), header, body);
    }

    /**
     * 非空参数校验
     */
    private void paramCheck(Object param) {
        if (param == null) {
            throw new BusinessException("参数不能为空");
        }
        if (param instanceof AgentChatParam) {
            ((AgentChatParam) param).selfCheck();
        } else if (param instanceof AgentResumeParam) {
            ((AgentResumeParam) param).selfCheck();
        } else if (param instanceof File) {
            if (!((File) param).exists()) {
                throw new BusinessException("文件不存在!");
            }
        }
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://xingchen-api.xf-yun.com/workflow/";

        public Builder(String apikey, String apiSecret) {
            super(HOST_URL, null, apikey, apiSecret);
            // 工作流耗时操作, 客户端等待服务器响应的时间调整为120s
            this.readTimeout(600);
        }

        @Override
        public AgentClient build() {
            return new AgentClient(this);
        }
    }
}
