package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.model.sign.VoiceCloneSignature;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

/**
 * 一句话复刻client
 *
 * @author zyding6
 */
public class VoiceTrainClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(VoiceTrainClient.class);

    private final boolean logRequest;

    public VoiceTrainClient(Builder builder) {
        super(builder);
        this.logRequest = builder.logRequest;
    }

    public boolean getLogRequest() {
        return logRequest;
    }

    /**
     * 获取token
     * 统一鉴权服务，会返回鉴权token和有效期（默认2小时）。
     * 在token有效期内，访问声音复刻的其他接口需要携带此token。
     */
    public String token() throws Exception {
        String timestamp = String.valueOf(System.currentTimeMillis());
        // 请求体 body
        JsonObject body = new JsonObject();
        body.addProperty("model", "remote");
        JsonObject base = new JsonObject();
        base.addProperty("appid", appId);
        base.addProperty("timestamp", timestamp);
        base.addProperty("version", "v1");
        body.add("base", base);
        String bodyStr = StringUtils.gson.toJson(body);

        // 请求头header
        Map<String, String> header = VoiceCloneSignature.tokenSign(apiKey, timestamp, bodyStr);

        // 请求结果
        if (this.logRequest) {
            logger.info("获取token请求URL：{}，入参：{}", "http://avatar-hci.xfyousheng.com/aiauth/v1/token", bodyStr);
        }
        return sendPost("http://avatar-hci.xfyousheng.com/aiauth/v1/token", JSON, header, bodyStr);
    }

    /**
     * 获取训练文本
     * 查询训练文本列表，后续根据选择的文本进行录音
     * 注意：要求录音音频和文本保持一致，否则会导致音频检测失败。
     * 通用训练文本(textId=5001)
     */
    public String trainText(String token, Long textId) throws Exception {
        // 请求体 body
        JsonObject body = new JsonObject();
        body.addProperty("textId", textId);
        String bodyStr = StringUtils.gson.toJson(body);

        // 请求头header
        Map<String, String> header = VoiceCloneSignature.commonSign(appId, apiKey, bodyStr, token);

        // 请求结果
        if (this.logRequest) {
            logger.info("获取训练文本请求URL：{}，入参：{}", hostUrl + "task/traintext", bodyStr);
        }
        return sendPost(hostUrl + "task/traintext", JSON, header, bodyStr);
    }

    /**
     * 创建音色训练任务
     *
     * @param taskName     创建任务名称, 默认””
     * @param sex          性别, 1:男2:女, 默认1
     * @param ageGroup     1:儿童、2:青年、3:中年、4:中老年, 默认1
     * @param thirdUser    用户标识, 默认””
     * @param language     训练的语种, 默认””
     *                     中文：不传language参数，默认中文
     *                     英：en
     *                     日：jp
     *                     韩：ko
     *                     俄：ru
     * @param resourceName 音库名称, 默认””
     * @param callbackUrl  任务结果回调地址，训练结束时进行回调不穿默认不回调
     *                     回调参数1            taskName	string	false	任务名称
     *                     回调参数2            trainVid	string	true	音库id
     *                     回调参数3            trainVcn	string	true	训练得到的音色id，后续根据该音色id进行音频合成
     *                     回调参数4            resourceType	string	true	12：一句话
     *                     回调参数5            taskId	string	true	任务唯一id
     *                     回调参数6            trainStatus	string	true	-1训练中 0 失败 1成功 2草稿
     * @param token        token
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String createTask(String taskName, Integer sex, Integer ageGroup, String thirdUser, String language, String resourceName, String callbackUrl, String token) throws Exception {
        // 请求体 body
        JsonObject body = new JsonObject();
        body.addProperty("taskName", taskName);
        body.addProperty("sex", sex);
        body.addProperty("ageGroup", ageGroup);
        // 12:一句话合成
        body.addProperty("resourceType", 12);
        body.addProperty("thirdUser", thirdUser);
        body.addProperty("language", language);
        body.addProperty("resourceName", resourceName);
        body.addProperty("callbackUrl", callbackUrl);
        String bodyStr = StringUtils.gson.toJson(body);

        // 请求头header
        Map<String, String> header = VoiceCloneSignature.commonSign(appId, apiKey, bodyStr, token);

        // 请求结果
        if (this.logRequest) {
            logger.info("创建音色训练任务请求URL：{}，入参：{}", hostUrl + "task/add", bodyStr);
        }
        return sendPost(hostUrl + "task/add", JSON, header, bodyStr);
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
     * @param taskId    训练任务唯一id
     * @param audioUrl  文件上传的url地址, 必须是http/https开头，以mp3/wav/m4a/pcm结尾
     * @param textId    文本ID, 可使用通用训练文本(textId=5001)
     * @param textSegId 训练样例文本段落ID, 例：1, 2, 3 ……
     * @param token     token
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String audioAdd(String taskId, String audioUrl, Long textId, Long textSegId, String token) throws Exception {
        // 请求体 body
        JsonObject body = new JsonObject();
        body.addProperty("taskId", taskId);
        body.addProperty("audioUrl", audioUrl);
        body.addProperty("textId", textId);
        body.addProperty("textSegId", textSegId);
        String bodyStr = StringUtils.gson.toJson(body);

        // 请求头header
        Map<String, String> header = VoiceCloneSignature.commonSign(appId, apiKey, bodyStr, token);

        // 请求结果
        if (this.logRequest) {
            logger.info("向训练任务添加音频请求URL：{}，入参：{}", hostUrl + "audio/v1/add", bodyStr);
        }
        return sendPost(hostUrl + "audio/v1/add", JSON, header, bodyStr);
    }


    /**
     * 音色训练任务提交训练（异步
     *
     * @param taskId 训练任务id
     * @param token  token
     * @return 范湖结果
     * @throws Exception 异常信息
     */
    public String submit(String taskId, String token) throws Exception {
        // 请求体 body
        JsonObject body = new JsonObject();
        body.addProperty("taskId", taskId);
        String bodyStr = StringUtils.gson.toJson(body);

        // 请求头header
        Map<String, String> header = VoiceCloneSignature.commonSign(appId, apiKey, bodyStr, token);

        // 请求结果
        if (this.logRequest) {
            logger.info("音色训练任务提交请求URL：{}，入参：{}", hostUrl + "task/submit", bodyStr);
        }
        return sendPost(hostUrl + "task/submit", JSON, header, bodyStr);
    }


    /**
     * 向训练任务添加音频（本地文件）并提交训练任务
     * 训练任务添加音频（使用音频的本地文件），调用此接口前需要先调用获取训练文本接口，
     * 根据选择的文本进行录音，要求录音音频和文本保持一致，否则会导致音频检测失败。
     * 音频要求：
     * 1、音频格式限制wav、mp3、m4a、pcm，推荐使用无压缩wav格式
     * 2、单通道，采样率24k及以上，位深度16bit，时长无严格限制，音频大小限制3M。
     *
     * @param file      上传的音频文件
     * @param taskId    训练任务唯一id
     * @param textId    文本ID, 可使用通用训练文本(textId=5001)
     * @param textSegId 训练样例文本段落ID, 例：1, 2, 3 ……
     * @param token     token
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String submitWithAudio(File file, String taskId, String textId, String textSegId, String token) throws Exception {
        // 请求体 body
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("file", file.getName(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file));
        builder.addFormDataPart("taskId", taskId);
        builder.addFormDataPart("textId", textId);
        builder.addFormDataPart("textSegId", textSegId);
        RequestBody httpBody = builder.build();
        String body = httpBody.toString();

        // 请求头header
        Map<String, String> header = VoiceCloneSignature.commonSign(appId, apiKey, body, token);

        // 请求结果
        if (this.logRequest) {
            logger.info("向训练任务添加音频（本地文件）请求URL：{}，入参：{}", hostUrl + "task/submitWithAudio", body);
        }
        return sendBodyPost(hostUrl + "task/submitWithAudio", header, httpBody);
    }

    /**
     * 根据任务id查询音色训练任务的状态，任务完成后返回训练得到的音色id
     *
     * @param taskId 任务id
     * @param token  token
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String result(String taskId, String token) throws Exception {
        // 请求体 body
        JsonObject body = new JsonObject();
        body.addProperty("taskId", taskId);
        String bodyStr = StringUtils.gson.toJson(body);

        // 请求头header
        Map<String, String> header = VoiceCloneSignature.commonSign(appId, apiKey, bodyStr, token);

        // 请求结果
        if (this.logRequest) {
            logger.info("根据任务id查询音色训练任务请求URL：{}，入参：{}", hostUrl + "task/result", bodyStr);
        }
        return sendPost(hostUrl + "task/result", JSON, header, bodyStr);
    }

    public static final class Builder extends HttpBuilder<Builder> {
        private static final String HOST_URL = "http://opentrain.xfyousheng.com/voice_train/";

        private boolean logRequest = false;

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        public Builder logRequest(boolean logRequest) {
            this.logRequest = logRequest;
            return this;
        }

        @Override
        public VoiceTrainClient build() {
            return new VoiceTrainClient(this);
        }
    }
}
