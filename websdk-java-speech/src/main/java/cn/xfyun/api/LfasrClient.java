package cn.xfyun.api;

import cn.xfyun.service.lfasr.task.*;
import cn.xfyun.exception.LfasrException;
import cn.xfyun.model.response.lfasr.LfasrResponse;
import cn.xfyun.model.sign.LfasrSignature;
import cn.xfyun.service.lfasr.LfasrExecutorService;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * 录音文件转写客户端
 */
public class LfasrClient {

    private static final Logger logger = LoggerFactory.getLogger(LfasrClient.class);

    /**
     * 文件限制最大500M
     */
    private static final int FILE_UPLOAD_MAXSIZE = 524288000;

    /**
     * 应用ID（必填）
     */
    private String appId;

    /**
     * 应用密钥（必填）
     */
    private String secretKey;

    /**
     * 转写执行服务
     */
    private volatile LfasrExecutorService lfasrExecutorService;

    /**
     * 音频文件名称，最好携带音频真实的后缀名，避免影响转码（必填）
     */
    private String fileName;

    /**
     * 音频文件大小（字节数）（必填）
     * 当前只针对本地文件流方式校验，使用url外链方式不校验，可随机传一个数字
     */
    private Long fileSize;

    /**
     * 音频真实时长，当前未验证，可随机传一个数字（必填）
     */
    private Long duration;

    /**
     * 语种类型（非必填）
     * cn：中文，通用方言（包括普通话、天津、河北、东北、甘肃、山东、太原）（默认）
     * en：英文
     * ja：日语
     * ko：韩语
     * ru：俄语
     * fr：法语
     * es：西班牙语
     * vi：越南语
     * ar：阿拉伯语
     * cn_xinanese：西南官话（包括四川、重庆、云南、贵州）
     * cn_cantonese：粤语
     * cn_henanese： 河南
     * cn_uyghur：维吾尔语
     * cn_tibetan：藏语
     * ar:阿拉伯语
     * de:德语
     * it:意大利语
     */
    private String language;

    /**
     * 回调地址（非必填）
     * 订单完成时回调该地址通知完成支持get请求，长度限制512
     * 参数：orderId为订单号、status为订单状态: 1(转写识别成功)、-1(转写识别失败)
     */
    private String callbackUrl;

    /**
     * 热词，用以提升专业词汇的识别率（非必填）
     * 格式：热词1|热词2|热词3
     * 单个热词长度：[2,16]，热词个数限制200个
     */
    private String hotWord;

    /**
     * 多候选开关（非必填）
     * 0：关闭(默认)
     * 1：打开
     */
    private Short candidate;

    /**
     * 是否开启角色分离（非必填）
     * 0：不开启角色分离(默认)
     * 1：通用角色分离
     */
    private Short roleType;

    /**
     * 说话人数，取值范围0-10，默认为0进行盲分（非必填）
     * 注：该字段只有在开通了角色分离功能的前提下才会生效
     */
    private Short roleNum;

    /**
     * 领域个性化参数（非必填）
     * court：法律
     * edu：教育
     * finance：金融
     * medical：医疗
     * tech：科技
     * culture：人文历史
     * isp：运营商
     * sport：体育
     * gov：政府
     * game：游戏
     * ecom：电商
     * mil：军事
     * com：企业
     * life：生活
     * ent：娱乐
     * car：汽车
     */
    private String pd;

    /**
     * 转写音频上传方式（非必填）
     * fileStream：文件流(默认)
     * urlLink：音频url外链
     */
    private String audioMode;

    /**
     * 音频url外链地址（非必填，audioMode为urlLink时必填）
     * 当audioMode为urlLink时该值必传
     * 如果url中包含特殊字符，audioUrl需要UrlEncode，长度限制512
     */
    private String audioUrl;

    /**
     * 是否标准pcm/wav(16k/16bit/单声道)（非必填）
     * 0：非标准wav(默认)
     * 1：标准pcm/wav
     */
    private Integer standardWav;

    /**
     * 语言识别模式选择（非必填）
     * language为cn时：
     * 1：自动中英文模式(默认)
     * 2：中文模式（可能包含少量英文）
     * 4：纯中文模式（不包含英文）
     */
    private Integer languageType;

    /**
     * 按声道分轨转写模式（支持语种：cn、en）（非必填）
     * 1：不分轨模式(默认)
     * 2：双声道分轨模式
     * 备注：如果转写任务使用双声道分轨模式，角色分离(roleType)功能失效
     */
    private Short trackMode;

    /**
     * 需要翻译的语种(转写语种和翻译语种不能相同)（非必填）
     */
    private String transLanguage;

    /**
     * 翻译模式（默认2：按段落进行翻译，目前只支持按段落进行翻译）（非必填）
     * 1：按VAD进行翻译
     * 2：按段落进行翻译
     * 3：按整篇进行翻译
     */
    private Short transMode;

    /**
     * 控制分段的最大字数，取值范围[0-500]，不传使用引擎默认值（非必填）
     */
    private Integer engSegMax;

    /**
     * 控制分段的最小字数，取值范围[0-50]，不传使用引擎默认值（非必填）
     */
    private Integer engSegMin;

    /**
     * 控制分段字数的权重，权重比越高，表示引擎分段逻辑采用字数控制分段的比重越高（非必填）
     * 取值(0-0.05)不传即不采用字数控制分段，采用引擎默认分段逻辑
     */
    private Float engSegWeight;

    /**
     * 顺滑开关（非必填）
     * true：表示开启(默认)
     * false：表示关闭
     */
    private Boolean engSmoothproc;

    /**
     * 口语规整开关，口语规整是顺滑的升级版本（非必填）
     * true：表示开启
     * false：表示关闭(默认)
     */
    private Boolean engColloqproc;

    /**
     * 远近场模式（非必填）
     * 1：远场模式(默认)
     * 2：近场模式
     */
    private Integer engVadMdn;

    /**
     * 首尾是否带静音信息（非必填）
     * 0：不显示
     * 1：显示(默认)
     */
    private Integer engVadMargin;

    /**
     * 针对粤语转写后的字体转换（非必填）
     * 0：输出简体
     * 1：输出繁体(默认)
     */
    private Integer engRlang;

    /**
     * 通过Builder构建LfasrClient实例
     *
     * @param builder 构建器
     */
    public LfasrClient(LfasrClient.Builder builder) {


        this.appId = builder.appId;
        this.secretKey = builder.secretKey;
        this.fileName = builder.fileName;
        this.fileSize = builder.fileSize;
        this.duration = builder.duration;
        this.language = builder.language;
        this.callbackUrl = builder.callbackUrl;
        this.hotWord = builder.hotWord;
        this.candidate = builder.candidate;
        this.roleType = builder.roleType;
        this.roleNum = builder.roleNum;
        this.pd = builder.pd;
        this.audioMode = builder.audioMode;
        this.audioUrl = builder.audioUrl;
        this.standardWav = builder.standardWav;
        this.languageType = builder.languageType;
        this.trackMode = builder.trackMode;
        this.transLanguage = builder.transLanguage;
        this.transMode = builder.transMode;
        this.engSegMax = builder.engSegMax;
        this.engSegMin = builder.engSegMin;
        this.engSegWeight = builder.engSegWeight;
        this.engSmoothproc = builder.engSmoothproc;
        this.engColloqproc = builder.engColloqproc;
        this.engVadMdn = builder.engVadMdn;
        this.engVadMargin = builder.engVadMargin;
        this.engRlang = builder.engRlang;
    }

    /**
     * 本地音频文件上传接口
     *
     * @param audioFilePath 音频文件路径
     * @return 调用成功时返回orderId（转写任务的唯一标识），是后续接口的必传参数。
     * @throws LfasrException     转写异常
     * @throws SignatureException 签名异常
     */
    public LfasrResponse uploadFile(String audioFilePath) throws LfasrException, SignatureException {
        // 业务校验
        if (StringUtils.isNullOrEmpty(audioFilePath)) {
            throw new LfasrException("音频文件地址为空!");
        }

        // 文件存在性和大小校验
        File audioFile = new File(audioFilePath);
        if (!audioFile.exists()) {
            throw new LfasrException(audioFilePath + "不存在!");
        }
        if (audioFile.length() > FILE_UPLOAD_MAXSIZE) {
            throw new LfasrException(audioFilePath + " 文件过大! (500M)");
        }

        // 构建参数
        Map<String, String> param = new HashMap<>(32);
        paramHandler(param, audioFile, null);

        // 执行文件任务
        return this.lfasrExecutorService.exec(new UploadFileTask(new LfasrSignature(appId, secretKey), param, audioFile));
    }

    /**
     * 音频URL上传接口
     *
     * @param audioUrl 音频链接
     * @return 调用成功时返回orderId（转写任务的唯一标识），是后续接口的必传参数。
     * @throws LfasrException     转写异常
     * @throws SignatureException 签名异常
     */
    public LfasrResponse uploadUrl(String audioUrl) throws LfasrException, SignatureException {

        if (StringUtils.isNullOrEmpty(audioUrl)) {
            throw new LfasrException("音频链接为空!");
        }

        // 构建参数
        this.audioMode = "urlLink";
        this.audioUrl = audioUrl;
        this.fileName = StringUtils.isNullOrEmpty(this.fileName) ? audioUrl.substring(audioUrl.lastIndexOf("/") + 1) : this.fileName;
        this.fileSize = this.fileSize != null ? this.fileSize : 1L;
        Map<String, String> param = new HashMap<>(32);
        paramHandler(param, null, audioUrl);

        // 执行文件任务
        return this.lfasrExecutorService.exec(new UploadUrlTask(new LfasrSignature(appId, secretKey), param, audioUrl));
    }

    /**
     * 获取结果接口
     *
     * @param orderId 转写订单ID
     * @return 转写结果
     * @throws SignatureException 签名异常
     */
    public LfasrResponse getResult(String orderId) throws SignatureException {
        return getResult(orderId, null);
    }

    /**
     * 获取结果接口
     *
     * @param orderId    转写订单ID
     * @param resultType 查询结果类型
     *                   - 转写结果：transfer（默认）
     *                   - 翻译结果：translate
     *                   - 质检结果：predict
     *                   - 组合结果查询：多个类型结果使用”,”隔开，目前只支持转写和质检结果一起返回，不支持转写和翻译结果一起返回（如果任务有失败则只返回处理成功的结果）
     *                   - 转写和质检结果组合返回：transfer,predict
     *                   - 注：使用质检功能请先在控制台开启
     * @return 转写结果
     * @throws SignatureException 签名异常
     */
    public LfasrResponse getResult(String orderId, String resultType) throws SignatureException {
        return this.lfasrExecutorService.exec(new PullResultTask(new LfasrSignature(appId, secretKey), orderId, resultType));
    }

    /**
     * 接口参数处理
     * 处理所有请求参数并添加到参数Map中
     *
     * @param param  参数Map
     * @param length 文件长度
     * @param audio  音频文件
     */
    private void paramHandler(Map<String, String> param, File audioFile, String audioUrl) {
        // 必填参数设置
        param.put("fileName", StringUtils.isNullOrEmpty(this.fileName) ? audioFile.getName() : this.fileName);
        param.put("fileSize", this.fileSize == null ? audioFile.length() + "" : this.fileSize + "");
        param.put("duration", this.duration != null ? this.duration + "" : "1");

        if ("urlLink".equals(this.audioMode)) {
            if (StringUtils.isNullOrEmpty(this.audioUrl)) {
                throw new LfasrException("音频链接为空!");
            }
            try {
                String encodedUrl = URLEncoder.encode(this.audioUrl, "UTF-8");
                param.put("audioUrl", encodedUrl);
            } catch (java.io.UnsupportedEncodingException e) {
                throw new LfasrException("URL编码失败: " + e.getMessage());
            }
            param.put("audioUrl", this.audioUrl);
        }

        if (!StringUtils.isNullOrEmpty(this.language)) {
            param.put("language", this.language);
        }

        if (!StringUtils.isNullOrEmpty(this.callbackUrl)) {
            param.put("callbackUrl", this.callbackUrl);
        }

        if (!StringUtils.isNullOrEmpty(this.hotWord)) {
            param.put("hot_word", this.hotWord);
        }

        if (this.candidate != null) {
            param.put("candidate", this.candidate + "");
        }

        if (this.roleType != null) {
            param.put("roleType", this.roleType + "");
        }

        if (this.roleNum != null) {
            param.put("roleNum", this.roleNum + "");
        }

        if (!StringUtils.isNullOrEmpty(this.pd)) {
            param.put("pd", this.pd);
        }

        if (!StringUtils.isNullOrEmpty(this.audioMode)) {
            param.put("audioMode", this.audioMode);
        }

        if (!StringUtils.isNullOrEmpty(this.audioUrl)) {
            param.put("audioUrl", this.audioUrl);
        }

        if (this.standardWav != null) {
            param.put("standardWav", this.standardWav + "");
        }

        if (this.languageType != null) {
            param.put("languageType", this.languageType + "");
        }

        if (this.trackMode != null) {
            param.put("trackMode", this.trackMode + "");
        }

        if (!StringUtils.isNullOrEmpty(this.transLanguage)) {
            param.put("transLanguage", this.transLanguage);
        }

        if (this.transMode != null) {
            param.put("transMode", this.transMode + "");
        }

        if (this.engSegMax != null) {
            param.put("eng_seg_max", this.engSegMax + "");
        }

        if (this.engSegMin != null) {
            param.put("eng_seg_min", this.engSegMin + "");
        }

        if (this.engSegWeight != null) {
            param.put("eng_seg_weight", this.engSegWeight + "");
        }

        if (this.engSmoothproc != null) {
            param.put("eng_smoothproc", this.engSmoothproc ? "true" : "false");
        }

        if (this.engColloqproc != null) {
            param.put("eng_colloqproc", this.engColloqproc ? "true" : "false");
        }

        if (this.engVadMdn != null) {
            param.put("eng_vad_mdn", this.engVadMdn + "");
        }

        if (this.engVadMargin != null) {
            param.put("eng_vad_margin", this.engVadMargin + "");
        }

        if (this.engRlang != null) {
            param.put("eng_rlang", this.engRlang + "");
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

        private String fileName;
        private Long fileSize;
        private Long duration;
        private String language;
        private String callbackUrl;
        private String hotWord;
        private Short candidate;
        private Short roleType;
        private Short roleNum;
        private String pd;
        private String audioMode;
        private String audioUrl;
        private Integer standardWav;
        private Integer languageType;
        private Short trackMode;
        private String transLanguage;
        private Short transMode;
        private Integer engSegMax;
        private Integer engSegMin;
        private Float engSegWeight;
        private Boolean engSmoothproc;
        private Boolean engColloqproc;
        private Integer engVadMdn;
        private Integer engVadMargin;
        private Integer engRlang;

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

        public LfasrClient.Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public LfasrClient.Builder fileSize(Long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public LfasrClient.Builder duration(Long duration) {
            this.duration = duration;
            return this;
        }

        public LfasrClient.Builder language(String language) {
            this.language = language;
            return this;
        }

        public LfasrClient.Builder callbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
            return this;
        }

        public LfasrClient.Builder hotWord(String hotWord) {
            this.hotWord = hotWord;
            return this;
        }

        public LfasrClient.Builder candidate(Short candidate) {
            this.candidate = candidate;
            return this;
        }

        public LfasrClient.Builder roleType(Short roleType) {
            this.roleType = roleType;
            return this;
        }

        public LfasrClient.Builder roleNum(Short roleNum) {
            this.roleNum = roleNum;
            return this;
        }

        public LfasrClient.Builder pd(String pd) {
            this.pd = pd;
            return this;
        }

        public LfasrClient.Builder audioMode(String audioMode) {
            this.audioMode = audioMode;
            return this;
        }

        public LfasrClient.Builder audioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
            return this;
        }

        public LfasrClient.Builder standardWav(Integer standardWav) {
            this.standardWav = standardWav;
            return this;
        }

        public LfasrClient.Builder languageType(Integer languageType) {
            this.languageType = languageType;
            return this;
        }

        public LfasrClient.Builder trackMode(Short trackMode) {
            this.trackMode = trackMode;
            return this;
        }

        public LfasrClient.Builder transLanguage(String transLanguage) {
            this.transLanguage = transLanguage;
            return this;
        }

        public LfasrClient.Builder transMode(Short transMode) {
            this.transMode = transMode;
            return this;
        }

        public LfasrClient.Builder engSegMax(Integer engSegMax) {
            this.engSegMax = engSegMax;
            return this;
        }

        public LfasrClient.Builder engSegMin(Integer engSegMin) {
            this.engSegMin = engSegMin;
            return this;
        }

        public LfasrClient.Builder engSegWeight(Float engSegWeight) {
            this.engSegWeight = engSegWeight;
            return this;
        }

        public LfasrClient.Builder engSmoothproc(Boolean engSmoothproc) {
            this.engSmoothproc = engSmoothproc;
            return this;
        }

        public LfasrClient.Builder engColloqproc(Boolean engColloqproc) {
            this.engColloqproc = engColloqproc;
            return this;
        }

        public LfasrClient.Builder engVadMdn(Integer engVadMdn) {
            this.engVadMdn = engVadMdn;
            return this;
        }

        public LfasrClient.Builder engVadMargin(Integer engVadMargin) {
            this.engVadMargin = engVadMargin;
            return this;
        }

        public LfasrClient.Builder engRlang(Integer engRlang) {
            this.engRlang = engRlang;
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