package cn.xfyun.api;

import cn.xfyun.service.lfasr.task.*;
import cn.xfyun.exception.LfasrException;
import cn.xfyun.model.response.lfasr.LfasrMessage;
import cn.xfyun.model.sign.LfasrSignature;
import cn.xfyun.service.lfasr.LfasrExecutorService;
import cn.xfyun.util.SliceIdGenerator;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 转写client
 *
 * @author : iflytek
 * @date : 2021年03月15日
 */
public class LfasrClient {
    private static final Logger logger = LoggerFactory.getLogger(LfasrClient.class);

    /**
     * 最大500M
     */
    private static final int FILE_UPLOAD_MAXSIZE = 524288000;
    /**
     * 建议10M一片
     */
    private static int SLICE_SIZE = 10485760;
    private final String secretKey;
    private volatile LfasrExecutorService lfasrExecutorService;
    private String appId;
    /**
     * 文件名称（带后缀）
     */
    private String file_name;

    /**
     * 文件分片大小（建议分片大小为10M）
     */
    private Integer slice_size;

    /**
     * 文件分片数目（建议分片大小为10M，若文件<10M，则slice_num=1）
     */
    private Integer slice_num;

    /**
     * 转写类型，默认 0
     * 0: (标准版，格式: wav,flac,opus,mp3,m4a)
     * 2: (电话版，已取消)
     */
    private String lfasr_type;

    /**
     * 转写结果是否包含分词信息
     */
    private String has_participle;

    /**
     * 转写结果中最大的候选词个数
     */
    private String max_alternatives;
    /**
     * 发音人个数，可选值：0-10，0表示盲分
     * 注：发音人分离目前还是测试效果达不到商用标准，如测试无法满足您的需求，请慎用该功能。
     */
    private String speaker_number;
    /**
     * 转写结果中是否包含发音人分离信息
     */
    private String has_seperate;

    /**
     * 支持两种参数
     * 1: 通用角色分离
     * 2: 电话信道角色分离（适用于speaker_number为2的说话场景）
     */
    private String role_type;

    /**
     * 语种
     * cn:中英文&中文（默认）
     * en:英文（英文不支持热词）
     */
    private String language;

    /**
     * 垂直领域个性化参数:
     * 法院: court
     * 教育: edu
     * 金融: finance
     * 医疗: medical
     * 科技: tech
     */
    private String pd;

    /**
     * builder 字段赋值
     *
     * @param builder
     */
    public LfasrClient(LfasrClient.Builder builder) {
        this.appId = builder.appId;
        this.secretKey = builder.secretKey;
        this.file_name = builder.file_name;
        this.slice_size = builder.slice_size;
        this.lfasr_type = builder.lfasr_type;
        this.has_participle = builder.has_participle;
        this.max_alternatives = builder.max_alternatives;
        this.speaker_number = builder.speaker_number;
        this.has_seperate = builder.has_seperate;
        this.role_type = builder.role_type;
        this.language = builder.language;
        this.pd = builder.pd;
    }

    /**
     * 预处理接口
     * 上传待转写音频文件的基本信息（文件名、大小）和分片信息（建议分片大小设置为10M，若无需分片，slice_num=1）和相关的可配置参数。
     * 调用成功，返回任务ID（task_id，转写任务的唯一标识），是后续接口的必传参数。
     *
     * @param audioFilePath
     * @return
     * @throws LfasrException
     * @throws SignatureException
     */
    public LfasrMessage prepare(String audioFilePath) throws LfasrException, SignatureException {
        // 业务校验
        if (StringUtils.isNullOrEmpty(audioFilePath)) {
            throw new LfasrException("audioFilePath is null!");
        }
        File audio = new File(audioFilePath);
        if (!audio.exists()) {
            throw new LfasrException(audioFilePath + " is not exists!");
        }
        long length = audio.length();
        if (length > FILE_UPLOAD_MAXSIZE) {
            throw new LfasrException(audioFilePath + " is too large! (500M)");
        }
        Map<String, String> param = new HashMap<>(32);
        paramHandler(param, length, audio);
        return this.lfasrExecutorService.exec(new PrepareTask(new LfasrSignature(appId, secretKey), param));
    }

    /**
     * 文件分片上传接口
     * 按预处理设置的分片信息（slice_num）依次上传音频切片（文件以二进制方式读取上传），
     * 直到全部切片上传成功（如预处理时 slice_num=2，则需将音频切分成两部分，slice_id=aaaaaaaaaa和aaaaaaaaab，并按顺序调用该接口）；
     * 上一切片成功上传，才可进行下一切片的上传操作。调用过程中若出现异常，可重试若干次。
     *
     * @param taskId
     * @param audioFilePath
     * @throws LfasrException
     */
    public void uploadFile(String taskId, String audioFilePath) throws LfasrException {
        File audio = new File(audioFilePath);
        try (FileInputStream fis = new FileInputStream(audio)) {
            byte[] slice = new byte[SLICE_SIZE];
            SliceIdGenerator generator = new SliceIdGenerator();
            int len;
            while ((len = fis.read(slice)) > 0) {
                byte[] data = Arrays.copyOfRange(slice, 0, len);
                UploadTask uploadTask = new UploadTask(new LfasrSignature(appId, secretKey), taskId, generator.getNextSliceId(), data);
                this.lfasrExecutorService.exec(uploadTask);
            }
        } catch (Exception e) {
            logger.error(audioFilePath + "上传处理失败", e);
            throw new LfasrException("转写上传文件读取错误");
        }
    }

    /**
     * 合并文件接口
     * 全部文件切片上传成功后，调用该接口，通知服务端进行文件合并与转写操作。
     * 该接口不会返回转写结果，而是通知服务端将任务列入转写计划。转写的结果通过 getResult 接口获取。
     *
     * @param taskId
     * @return
     * @throws LfasrException
     * @throws SignatureException
     */
    public LfasrMessage mergeFile(String taskId) throws LfasrException, SignatureException {
        return this.lfasrExecutorService.exec(new MergeTask(new LfasrSignature(appId, secretKey), taskId));
    }

    /**
     * 文件上传接口
     * 包含：
     * 1、prepare
     * 2、uploadFile
     * 3、merge
     *
     * @param audioFilePath
     * @return
     * @throws LfasrException
     * @throws SignatureException
     */
    public LfasrMessage upload(String audioFilePath) throws LfasrException, SignatureException {
        LfasrMessage message = prepare(audioFilePath);
        String taskId = message.getData();
        logger.info("taskId: " + taskId);
        uploadFile(taskId, audioFilePath);
        mergeFile(taskId);
        return message;
    }

    /**
     * 查询处理进度接口
     * 在调用方发出合并文件请求后，服务端已将任务列入计划。在获取结果前，调用方需轮询该接口查询任务当前状态。
     * 当且仅当任务状态=9（转写结果上传完成），才可调用获取结果接口获取转写结果。
     * 轮询策略由调用方决定，建议每隔10分钟轮询一次。状态码说明见附录。
     *
     * @param taskId
     * @return
     * @throws SignatureException
     */
    public LfasrMessage getProgress(String taskId) throws SignatureException {
        return this.lfasrExecutorService.exec(new QueryProgressTask(new LfasrSignature(appId, secretKey), taskId));
    }

    /**
     * 获取结果接口
     * 当任务处理进度状态=9（见查询处理进度接口），调用该接口获取转写结果。这是转写流程的最后一步。
     *
     * @param taskId
     * @return
     * @throws SignatureException
     */
    public LfasrMessage getResult(String taskId) throws SignatureException {
        return this.lfasrExecutorService.exec(new PullResultTask(new LfasrSignature(appId, secretKey), taskId));
    }

    /**
     * 接口参数处理
     *
     * @param param
     * @param length
     * @param audio
     */
    private void paramHandler(Map<String, String> param, long length, File audio) {
        LfasrClient.SLICE_SIZE = this.slice_size == null ? SLICE_SIZE : this.slice_size;

        long sliceNum = length / SLICE_SIZE + ((length % SLICE_SIZE == 0L) ? 0L : 1L);

        param.put("file_len", length + "");
        param.put("file_name", StringUtils.isNullOrEmpty(this.file_name) ? audio.getName() : this.file_name);
        param.put("slice_num", sliceNum + "");

        if (!StringUtils.isNullOrEmpty(this.lfasr_type)) {
            param.put("lfasr_type", this.lfasr_type);
        }

        if (!StringUtils.isNullOrEmpty(this.has_participle)) {
            param.put("has_participle", this.has_participle);
        }

        if (!StringUtils.isNullOrEmpty(this.max_alternatives)) {
            param.put("max_alternatives", this.max_alternatives);
        }
        if (!StringUtils.isNullOrEmpty(this.has_seperate)) {
            param.put("has_seperate", this.has_seperate);
        }

        if (!StringUtils.isNullOrEmpty(this.speaker_number)) {
            param.put("speaker_number", this.speaker_number);
        }

        if (!StringUtils.isNullOrEmpty(this.role_type)) {
            param.put("role_type", this.role_type);
        }

        if (!StringUtils.isNullOrEmpty(this.language)) {
            param.put("language", this.language);
        }

        if (!StringUtils.isNullOrEmpty(this.pd)) {
            param.put("pd", this.pd);
        }
    }

    public static final class Builder {
        private final String appId;
        private final String secretKey;
        private Integer coreThreads = 10;
        private Integer maxThreads = 50;
        private Integer maxConnections = 50;
        private Integer connTimeout = 10000;
        private Integer soTimeout = 30000;

        private String file_name;

        private Integer slice_size;

        private String lfasr_type;

        private String has_participle;

        private String max_alternatives;

        private String speaker_number;

        private String has_seperate;

        private String role_type;

        private String language;

        private String pd;


        public Builder(String appId, String secretKey) {
            this.appId = appId;
            this.secretKey = secretKey;
        }

        public LfasrClient.Builder coreThreads(Integer coreThreads) {
            this.coreThreads = coreThreads;
            return this;
        }

        public LfasrClient.Builder maxThreads(Integer maxThreads) {
            this.maxThreads = maxThreads;
            return this;
        }

        public LfasrClient.Builder maxConnections(Integer maxConnections) {
            this.maxConnections = maxConnections;
            return this;
        }

        public LfasrClient.Builder connTimeout(Integer connTimeout) {
            this.connTimeout = connTimeout;
            return this;
        }

        public LfasrClient.Builder soTimeout(Integer soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public LfasrClient.Builder file_name(String file_name) {
            this.file_name = file_name;
            return this;
        }

        public LfasrClient.Builder slice(Integer slice_size) {
            this.slice_size = slice_size;
            return this;
        }

        public LfasrClient.Builder lfasr_type(String lfasr_type) {
            this.lfasr_type = lfasr_type;
            return this;
        }

        public LfasrClient.Builder has_participle(String has_participle) {
            this.has_participle = has_participle;
            return this;
        }

        public LfasrClient.Builder max_alternatives(String max_alternatives) {
            this.max_alternatives = max_alternatives;
            return this;
        }

        public LfasrClient.Builder speaker_number(String speaker_number) {
            this.speaker_number = speaker_number;
            return this;
        }

        public LfasrClient.Builder has_seperate(String has_seperate) {
            this.has_seperate = has_seperate;
            return this;
        }


        public LfasrClient.Builder role_type(String role_type) {
            this.role_type = role_type;
            return this;
        }

        public LfasrClient.Builder language(String language) {
            this.language = language;
            return this;
        }

        public LfasrClient.Builder pd(String pd) {
            this.pd = pd;
            return this;
        }

        public LfasrClient build() {
            LfasrClient lfasrClient = new LfasrClient(this);

            if (lfasrClient.lfasrExecutorService != null) {
                logger.info("lfasrExecutorService is exist");
            }

            synchronized (LfasrClient.class) {
                if (lfasrClient.lfasrExecutorService == null) {
                    lfasrClient.lfasrExecutorService = LfasrExecutorService.build(coreThreads, maxThreads, maxConnections, connTimeout, soTimeout);
                }
            }
            return lfasrClient;
        }
    }
}
