package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.config.VoiceTrainEnum;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.VoiceCloneSignature;
import cn.xfyun.model.voiceclone.request.AudioAddParam;
import cn.xfyun.model.voiceclone.request.CreateTaskParam;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 一句话复刻client
 * 文档地址: <a href="https://www.xfyun.cn/doc/spark/reproduction.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class VoiceTrainClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(VoiceTrainClient.class);

    /**
     * token缓存自动刷新提前时间
     * 单位秒
     */
    private static final long TOKEN_AUTO_REFRESH_TIME = 1800;

    /**
     * token令牌
     * client初始化自动获取
     * 过期通过tokenPeriod参数自动刷新
     */
    private String token;

    /**
     * token过期时间
     */
    private long tokenExpiryTime;

    public VoiceTrainClient(Builder builder) {
        super(builder);
        refreshToken();
    }

    public String getToken() {
        return token;
    }

    /**
     * @param token      token令牌
     * @param expireTime token有效时长
     */
    public void setToken(String token, long expireTime) {
        this.token = token;
        this.tokenExpiryTime = System.currentTimeMillis() + expireTime;
    }

    public long getTokenExpiryTime() {
        return tokenExpiryTime;
    }

    /**
     * 获取token
     * 统一鉴权服务，会返回鉴权token和有效期（默认2小时）。
     * 在token有效期内，访问声音复刻的其他接口需要携带此token。
     */
    public String refreshToken() {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());

            // 请求体 body
            String bodyStr = buildTokenParam(timestamp);

            // 发送请求
            String token = send(VoiceTrainEnum.TOKEN, bodyStr, timestamp, null);
            cacheToken(token);
            return token;
        } catch (Exception e) {
            logger.error("token刷新失败", e);
        }
        return null;
    }

    /**
     * 获取训练文本
     * 查询训练文本列表，后续根据选择的文本进行录音
     * 注意：要求录音音频和文本保持一致，否则会导致音频检测失败。
     * 通用训练文本(textId=5001)
     */
    public String trainText(Long textId) throws Exception {
        // token校验
        tokenCheck(textId);

        // 请求体 body
        JsonObject body = new JsonObject();
        body.addProperty("textId", textId);
        String bodyStr = StringUtils.gson.toJson(body);

        // 发送请求
        return send(VoiceTrainEnum.TRAIN_TEXT, bodyStr, null, null);
    }

    /**
     * 创建音色训练任务
     *
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String createTask(CreateTaskParam param) throws Exception {
        // token校验
        tokenCheck(param);

        // 封装请求体
        String bodyStr = param.toJsonString();

        // 发送请求
        return send(VoiceTrainEnum.TASK_ADD, bodyStr, null, null);
    }

    /**
     * 向训练任务添加音频（url链接）
     * 训练任务添加音频（使用音频文件的url地址），调用此接口前需要先调用获取训练文本接口，
     * 根据选择的文本进行录音，要求录音音频和文本保持一致，否则会导致音频检测失败。
     * 音频要求：
     * 1、音频格式限制wav、mp3、m4a、pcm，推荐使用无压缩wav格式
     * 2、单通道，采样率24k及以上，位深度16bit，时长无严格限制，音频大小限制3M。
     * 录制音频前，建议先查看一句话复刻录音指导 。
     *
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String audioAdd(AudioAddParam param) throws Exception {
        // token校验
        tokenCheck(param);

        // 参数校验
        param.selfCheckUrl();

        // 请求体 body
        String bodyStr = param.toJsonString();

        // 发送请求
        return send(VoiceTrainEnum.AUDIO_ADD, bodyStr, null, null);
    }

    /**
     * 音色训练任务提交训练(异步)
     *
     * @param taskId 训练任务id
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String submit(String taskId) throws Exception {
        // token校验
        tokenCheck(taskId);

        // 请求体 body
        JsonObject body = new JsonObject();
        body.addProperty("taskId", taskId);
        String bodyStr = StringUtils.gson.toJson(body);

        // 发送请求
        return send(VoiceTrainEnum.TASK_SUBMIT, bodyStr, null, null);
    }

    /**
     * 向训练任务添加音频（本地文件）并提交训练任务
     * 训练任务添加音频（使用音频的本地文件），调用此接口前需要先调用获取训练文本接口，
     * 根据选择的文本进行录音，要求录音音频和文本保持一致，否则会导致音频检测失败。
     * 音频要求：
     * 1、音频格式限制wav、mp3、m4a、pcm，推荐使用无压缩wav格式
     * 2、单通道，采样率24k及以上，位深度16bit，时长无严格限制，音频大小限制3M。
     *
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String submitWithAudio(AudioAddParam param) throws Exception {
        // token校验
        tokenCheck(param);

        // 参数校验
        param.selfCheckFile();

        // 请求体 body
        RequestBody httpBody = getRequestBody(param);

        // 发送请求
        return send(VoiceTrainEnum.AUDIO_SUBMIT, httpBody.toString(), null, httpBody);
    }

    /**
     * 根据任务id查询音色训练任务的状态，任务完成后返回训练得到的音色id
     *
     * @param taskId 任务id
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String result(String taskId) throws Exception {
        // token校验
        tokenCheck(taskId);

        // 请求体 body
        JsonObject body = new JsonObject();
        body.addProperty("taskId", taskId);
        String bodyStr = StringUtils.gson.toJson(body);

        // 发送请求
        return send(VoiceTrainEnum.TASK_RESULT, bodyStr, null, null);
    }

    /**
     * 获取formData格式的请求体
     */
    private RequestBody getRequestBody(AudioAddParam request) {
        File file = request.getFile();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("file", file.getName(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file));
        builder.addFormDataPart("taskId", request.getTaskId());
        builder.addFormDataPart("textId", String.valueOf(request.getTextId()));
        builder.addFormDataPart("textSegId", String.valueOf(request.getTextSegId()));
        if (request.getDenoiseSwitch() != null) {
            builder.addFormDataPart("denoiseSwitch", String.valueOf(request.getDenoiseSwitch()));
        }
        if (request.getMosRatio() != null) {
            builder.addFormDataPart("mosRatio", String.valueOf(request.getMosRatio()));
        }
        return builder.build();
    }

    /**
     * 发送请求
     */
    private String send(VoiceTrainEnum trainEnum, String bodyStr, String timestamp, RequestBody body) throws IOException {
        Map<String, String> header;
        if (trainEnum == VoiceTrainEnum.TOKEN) {
            header = VoiceCloneSignature.tokenSign(apiKey, timestamp, bodyStr);
        } else {
            header = VoiceCloneSignature.commonSign(appId, apiKey, bodyStr, token);
        }
        // 请求结果
        logger.debug("{}请求URL：{}，入参：{}", trainEnum.getDesc(), trainEnum.getUrl(), bodyStr);
        if (null != body) {
            return sendPost(trainEnum.getUrl(), header, body);
        } else {
            return sendPost(trainEnum.getUrl(), JSON, header, bodyStr);
        }
    }

    /**
     * 参数校验
     */
    private void tokenCheck(Object param) {
        // 非空校验
        if (param == null) {
            throw new BusinessException("参数不能为空");
        }

        // token有效期校验
        if (token == null || System.currentTimeMillis() > tokenExpiryTime) {
            refreshToken();
        }
    }

    /**
     * 缓存token
     *
     * @param token token接口返回结果
     */
    private void cacheToken(String token) {
        if (!StringUtils.isNullOrEmpty(token)) {
            // 自动刷新token缓存
            JsonObject tokenObj = StringUtils.gson.fromJson(token, JsonObject.class);
            this.token = tokenObj.get("accesstoken").getAsString();
            long expiresin = tokenObj.get("expiresin").getAsLong();
            // 如果提前刷新token时间比系统返回的token有效期长 , 则token有效期取差值
            if (expiresin > TOKEN_AUTO_REFRESH_TIME) {
                expiresin = expiresin - TOKEN_AUTO_REFRESH_TIME;
            }
            this.tokenExpiryTime = System.currentTimeMillis() + expiresin * 1000;
        }
    }

    /**
     * 构建token请求的入参
     */
    private String buildTokenParam(String timestamp) {
        JsonObject body = new JsonObject();
        body.addProperty("model", "remote");
        JsonObject base = new JsonObject();
        base.addProperty("appid", appId);
        base.addProperty("timestamp", timestamp);
        base.addProperty("version", "v1");
        body.add("base", base);
        return StringUtils.gson.toJson(body);
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "http://opentrain.xfyousheng.com/voice_train/";

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        @Override
        public VoiceTrainClient build() {
            return new VoiceTrainClient(this);
        }
    }
}
