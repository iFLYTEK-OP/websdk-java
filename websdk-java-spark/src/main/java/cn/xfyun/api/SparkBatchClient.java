package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.config.SparkBatchEnum;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 星火大模型批处理client
 * 支持generalv3.5、4.0Ultra。generalv3.5指向Spark Max，4.0Ultra指向Spark4.0 Ultra。
 * 文档地址: <a href="https://www.xfyun.cn/doc/spark/BatchAPI.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class SparkBatchClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(SparkBatchClient.class);

    public SparkBatchClient(Builder builder) {
        super(builder);
    }

    /**
     * 上传文件
     * 格式：只支持 jsonl 文件名后缀
     * 上传的文件默认保存 48h。
     *
     * @param file 需要上传的文件
     */
    public String upload(File file) throws IOException {
        // 参数校验
        uploadCheck(file);

        // 获取请求体
        RequestBody body = getFormDataBody(file);

        // 发送请求
        return send(SparkBatchEnum.UPLOAD_FILE, body);
    }

    /**
     * 查询文件列表
     * 获取 appid 下的文件列表。
     * 上传的文件默认保存 48h；接口返回的文件按照文件生成的时间开始计时，默认保存 30 天。
     *
     * @param pageNum  文件页码
     * @param pageSize 文件页大小
     */
    public String listFile(int pageNum, int pageSize) throws IOException {
        // 发送请求
        return send(SparkBatchEnum.GET_FILES, null, pageNum, pageSize);
    }

    /**
     * 查询单个文件
     * 根据文件的file_id获取该文件信息。
     * 上传的文件默认保存 48h；接口返回的文件按照文件生成的时间开始计时，默认保存 30 天。
     *
     * @param fileId 文件ID
     */
    public String getFile(String fileId) throws IOException {
        // 非空校验
        nullCheck(fileId);

        // 发送请求
        return send(SparkBatchEnum.GET_FILE, null, fileId);
    }

    /**
     * 删除文件
     * 根据文件的file_id删除该文件
     *
     * @param fileId 文件ID
     */
    public String deleteFile(String fileId) throws IOException {
        // 非空校验
        nullCheck(fileId);

        // 发送请求
        return send(SparkBatchEnum.DELETE_FILE, null, fileId);
    }

    /**
     * 下载文件
     * #接口描述
     * 根据文件的file_id获取文件的详细内容。 上传的文件默认保存 48h；接口返回的文件按照文件生成的时间开始计时，默认保存 30 天。
     *
     * @param fileId 文件ID
     */
    public String download(String fileId) throws IOException {
        // 非空校验
        nullCheck(fileId);

        // 发送请求
        return send(SparkBatchEnum.DOWNLOAD_FILE, null, fileId);
    }

    /**
     * 创建Batch任务
     * #接口描述
     * 调用该接口前需要先上传jsonl文件，通过上传文件得到的file_id来创建Batch任务。 单个Batch任务最多包含5万个请求（一个请求对应jsonl文件的一行），每个请求的body不超过6KB
     *
     * @param fileId   文件ID
     * @param metadata 批任务附加信息，例如：
     *                 "customer_id": "user_123456789"
     *                 "batch_description": "Sentiment classification"
     */
    public String create(String fileId, Map<String, String> metadata) throws IOException {
        // 非空校验
        nullCheck(fileId);

        // 获取请求体
        RequestBody body = getCreateParam(fileId, metadata);

        // 发送请求
        return send(SparkBatchEnum.CREATE, body, fileId);
    }

    /**
     * 查询Batch任务
     * 根据batch_id获取该Batch任务的详细信息
     *
     * @param batchId 任务ID
     */
    public String getBatch(String batchId) throws IOException {
        // 非空校验
        nullCheck(batchId);

        // 发送请求
        return send(SparkBatchEnum.GET_BATCH, null, batchId);
    }

    /**
     * 取消Batch任务
     * 根据batch_id取消该Batch任务
     *
     * @param batchId 任务ID
     */
    public String cancel(String batchId) throws IOException {
        // 非空校验
        nullCheck(batchId);

        // 获取请求体
        RequestBody body = RequestBody.create(JSON, "{}");

        // 发送请求
        return send(SparkBatchEnum.CANCEL, body, batchId);
    }

    /**
     * 查询Batch列表
     * 获取 appid 下的Batch列表
     *
     * @param limit   响应中列表长度（最大为100）
     * @param batchId 从此 batch_id 开始查询（响应中不包括此 batch_id）
     */
    public String listBatch(int limit, String batchId) throws IOException {
        // 发送请求
        return send(SparkBatchEnum.GET_BATHES, null, limit, batchId);
    }

    /**
     * 发送请求
     */
    private String send(SparkBatchEnum batchEnum, RequestBody body, Object... param) throws IOException {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + apiKey);

        // 拼接参数获取url
        String url = batchEnum.getUrl();
        if (param != null) {
            url = String.format(batchEnum.getUrl(), param);
        }

        // 请求结果
        logger.debug("{}请求URL：{}，入参：{}", batchEnum.getDesc(), url, null == body ? "" : body.toString());
        return sendRequest(url, batchEnum.getMethod(), header, body);
    }

    /**
     * 校验参数
     *
     * @param file 文件
     */
    private void uploadCheck(File file) {
        if (null == file || !file.exists() || file.length() == 0) {
            throw new BusinessException("无效的jsonl文件");
        } else if (!file.getName().endsWith(".jsonl")) {
            throw new BusinessException("暂不支持该格式的文件");
        }
    }

    /**
     * 校验参数
     *
     * @param fileId 文件ID
     */
    private void nullCheck(String fileId) {
        if (StringUtils.isNullOrEmpty(fileId)) {
            throw new BusinessException("参数不能为空");
        }
    }

    /**
     * 获取formData的body
     *
     * @param file 文件
     * @return RequestBody
     */
    private RequestBody getFormDataBody(File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("file", file.getName(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file));
        builder.addFormDataPart("purpose", "batch");
        return builder.build();
    }

    /**
     * 获取创建任务入参
     *
     * @param fileId   文件ID
     * @param metadata 批任务附加信息
     * @return RequestBody
     */
    private RequestBody getCreateParam(String fileId, Map<String, String> metadata) {
        JsonObject obj = new JsonObject();
        obj.addProperty("input_file_id", fileId);
        // 处理批任务的服务的 path 路径,当前仅支持 /v1/chat/completions
        obj.addProperty("endpoint", "/v1/chat/completions");
        // 批处理任务完成的时间窗口,当前仅支持 24h，在此时间内完成，超时则不再处理，未处理任务视为失败
        obj.addProperty("completion_window", "24h");
        if (null != metadata) {
            // 批任务附加信息
            obj.add("metadata", StringUtils.gson.toJsonTree(metadata));
        }
        return RequestBody.create(JSON, obj.toString());
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://spark-api-open.xf-yun.com/";

        public Builder(String appId, String apiPassword) {
            super(HOST_URL, appId, apiPassword, null);
        }

        @Override
        public SparkBatchClient build() {
            return new SparkBatchClient(this);
        }
    }
}
