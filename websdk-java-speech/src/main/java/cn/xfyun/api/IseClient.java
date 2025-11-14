package cn.xfyun.api;

import cn.xfyun.base.websocket.WebSocketClient;
import cn.xfyun.common.IseConstant;
import cn.xfyun.model.request.ise.IseBusiness;
import cn.xfyun.model.request.ise.IseRequest;
import cn.xfyun.model.request.ise.IseRequestData;
import cn.xfyun.service.ise.IseSendTask;
import cn.xfyun.util.OkHttpUtils;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import cn.xfyun.model.sign.AbstractSignature;
import cn.xfyun.model.sign.Hmac256Signature;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocketListener;
import okhttp3.internal.Util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.security.SignatureException;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * @version 1.0
 * @author: <flhong2@iflytek.com>
 * @description: 语音评测客户端
 * @create: 2021-03-17 19:46
 **/
public class IseClient extends WebSocketClient {

    /**
     * 公共参数，仅在握手成功后首帧请求时上传
     */
    private JsonObject common;
    /**
     * 业务参数，在握手成功后首帧请求与后续数据发送时上传
     */
    private JsonObject business;

    /**
     * 服务类型指定
     * ise(开放评测)
     */
    private String sub;
    /**
     * 中文：cn_vip
     * 英文：en_vip
     */
    private String ent;
    /**
     * 中文题型：
     * read_syllable（单字朗读，汉语专有）
     * read_word（词语朗读）
     * read_sentence（句子朗读）
     * read_chapter(篇章朗读)
     * 英文题型：
     * read_word（词语朗读）
     * read_sentence（句子朗读）
     * read_chapter(篇章朗读)
     * simple_expression（英文情景反应）
     * read_choice（英文选择题）
     * topic（英文自由题）
     * retell（英文复述题）
     * picture_talk（英文看图说话）
     * oral_translation（英文口头翻译）
     */
    private String category;
    /**
     * 上传音频时来区分音频的状态（在cmd=auw即音频上传阶段为必传参数）
     * 1：第一帧音频
     * 2：中间的音频
     * 4：最后一帧音频
     */
    private int aus;
    /**
     * 用于区分数据上传阶段
     * ssb：参数上传阶段
     * ttp：文本上传阶段（ttp_skip=true时该阶段可以跳过，直接使用text字段中的文本）
     * auw：音频上传阶段
     */
    private String cmd;
    /**
     * 待评测文本 utf8 编码，需要加utf8bom 头
     * '\uFEFF'+text
     */
    private String text;
    /**
     * 待评测文本编码
     * utf-8
     * gbk
     */
    private String tte;
    /**
     * 跳过ttp直接使用ssb中的文本进行评测（使用时结合cmd参数查看）,默认值true
     */
    private boolean ttp_skip;
    /**
     * 拓展能力（生效条件ise_unite="1", rst="entirety"）
     * 多维度分信息显示（准确度分、流畅度分、完整度打分）
     * extra_ability值为multi_dimension（字词句篇均适用,如选多个能力，用分号；隔开。例如：add("extra_ability"," syll_phone_err_msg;pitch;multi_dimension")）
     * 单词基频信息显示（基频开始值、结束值）
     * extra_ability值为pitch ，仅适用于单词和句子题型
     * 音素错误信息显示（声韵、调型是否正确）
     * extra_ability值为syll_phone_err_msg（字词句篇均适用,如选多个能力，用分号；隔开。例如：add("extra_ability"," syll_phone_err_msg;pitch;multi_dimension")）
     */
    private String extra_ability;
    /**
     * 音频格式
     * raw: 未压缩的pcm格式音频或wav（如果用wav格式音频，建议去掉头部）
     * lame: mp3格式音频
     * speex-wb;7: 讯飞定制speex格式音频(默认值)
     */
    private String aue;
    /**
     * 音频采样率
     * 默认 audio/L16;rate=16000
     */
    private String auf;
    /**
     * 返回结果格式
     * utf8
     * gbk （默认值）
     */
    private String rstcd;
    /**
     * 针对群体不同，相同试卷音频评分结果不同 （仅中文字、词、句、篇章题型支持），此参数会影响准确度得分
     * adult（成人群体，不设置群体参数时默认为成人）
     * youth（中学群体）
     * pupil（小学群体，中文句、篇题型设置此参数值会有accuracy_score得分的返回）
     */
    private String group;
    /**
     * 设置评测的打分及检错松严门限（仅中文引擎支持）
     * easy：容易
     * common：普通
     * hard：困难
     */
    private String check_type;
    /**
     * 设置评测的学段参数 （仅中文题型：中小学的句子、篇章题型支持）
     * junior(1,2年级)
     * middle(3,4年级)
     * senior(5,6年级)
     */
    private String grade;
    /**
     * 评测返回结果与分制控制（评测返回结果与分制控制也会受到ise_unite与plev参数的影响）
     * 完整：entirety（默认值）
     * 中文百分制推荐传参（rst="entirety"且ise_unite="1"且配合extra_ability参数使用）
     * 英文百分制推荐传参（rst="entirety"且ise_unite="1"且配合extra_ability参数使用）
     * 精简：plain（评测返回结果将只有总分），如：
     * <?xml version="1.0" ?><FinalResult><ret value="0"/><total_score value="98.507320"/></FinalResult>
     */
    private String rst = "entirety";
    /**
     * 返回结果控制
     * 0：不控制（默认值）
     * 1：控制（extra_ability参数将影响全维度等信息的返回）
     */
    private String ise_unite = "0";
    /**
     * 在rst="entirety"（默认值）且ise_unite="0"（默认值）的情况下plev的取值不同对返回结果有影响。
     * plev：0(给出全部信息，汉语包含rec_node_type、perr_msg、fluency_score、phone_score信息的返回；
     * 英文包含accuracy_score、serr_msg、 syll_accent、fluency_score、standard_score、pitch信息的返回)
     */
    private String plev = "0";
    private Integer frameSize;
    private ExecutorService executorService;

    public IseClient(Builder builder) {
        if (builder.client != null) {
            // 使用用户提供的okHttpClient
            this.okHttpClient = builder.client;
        } else {
            // 复用全局的okHttpClient
            this.okHttpClient = OkHttpUtils.getDefaultClient().newBuilder()
                    .proxy(builder.proxy)
                    .connectTimeout(builder.connectTimeout, TimeUnit.MILLISECONDS)
                    .readTimeout(builder.readTimeout, TimeUnit.MILLISECONDS)
                    .writeTimeout(builder.writeTimeout, TimeUnit.MILLISECONDS)
                    .callTimeout(builder.callTimeout, TimeUnit.MILLISECONDS)
                    .pingInterval(builder.pingInterval, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(builder.retryOnConnectionFailure)
                    .build();
        }

        this.common = builder.common;
        this.business = builder.business;
        this.appId = builder.appId;
        this.sub = builder.sub;
        this.ent = builder.ent;
        this.category = builder.category;
        this.aus = builder.aus;
        this.cmd = builder.cmd;
        this.text = builder.text;
        this.tte = builder.tte;
        this.ttp_skip = builder.ttpSkip;
        this.extra_ability = builder.extraAbility;
        this.aue = builder.aue;
        this.auf = builder.auf;
        this.rstcd = builder.rstcd;
        this.group = builder.group;
        this.check_type = builder.checkType;
        this.grade = builder.grade;
        this.rst = builder.rst;
        this.ise_unite = builder.iseUnite;
        this.plev = builder.plev;
        this.originHostUrl = builder.hostUrl;
        this.apiKey = builder.apiKey;
        this.apiSecret = builder.apiSecret;
        this.frameSize = builder.frameSize;
        this.request = builder.request;
        this.signature = builder.signature;

        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.callTimeout = builder.callTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
        this.executorService = (null == builder.executorService) ? Executors.newSingleThreadExecutor() : builder.executorService;
    }

    /**
     * 语音评测服务端启动
     */
    public void start(WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        // 创建webSocket连接
        createWebSocketConnect(webSocketListener);
    }

    /**
     * 语音评测发送数据
     */
    public void sendMessage(byte[] bytes, Integer status) {
        // 发送数据,请求数据均为json字符串
        IseRequest frame = new IseRequest();

        // 填充business, 每一帧都要发送
        IseBusiness business = new IseBusiness(this);
        switch (status) {
            case 0:
                // 填充common,只有第一帧需要
                JsonObject common = new JsonObject();
                common.addProperty("app_id", appId);

                // 填充business,第一帧必须发送
                business.setCmd("ssb");
                business.setAus(null);

                // 填充data,每一帧都要发送
                IseRequestData prepareData = new IseRequestData();
                prepareData.setStatus(0);
                prepareData.setData(null);

                // 填充frame
                frame.setCommon(common);
                frame.setBusiness(business);
                frame.setData(prepareData);

                webSocket.send(StringUtils.gson.toJson(frame));

                // 间隔40毫秒
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // 发送完第一帧改变status 为 1
                frame.setCommon(null);
                prepareData.setStatus(1);
                if (bytes == null || bytes.length == 0) {
                    prepareData.setData("");
                } else {
                    prepareData.setData(Base64.getEncoder().encodeToString(bytes));
                }
                business.setCmd("auw");
                business.setAus(1);
                frame.setBusiness(business);
                webSocket.send(StringUtils.gson.toJson(frame));
                break;
            case 1:
                IseBusiness continueBusiness = new IseBusiness();
                if (!business.isTtp_skip()) {
                    continueBusiness.setCmd("ttp");
                } else {
                    continueBusiness.setCmd("auw");
                }
                continueBusiness.setAus(2);
                frame.setBusiness(continueBusiness);

                IseRequestData continueDate = new IseRequestData();
                continueDate.setStatus(1);
                if (bytes == null || bytes.length == 0) {
                    continueDate.setData("");
                } else {
                    continueDate.setData(Base64.getEncoder().encodeToString(bytes));
                }

                frame.setData(continueDate);
                webSocket.send(StringUtils.gson.toJson(frame));
                break;
            // 最后一帧音频status = 2, 标志音频发送结束
            case 2:
                IseBusiness lastBusiness = new IseBusiness();
                if (!business.isTtp_skip()) {
                    lastBusiness.setCmd("ttp");
                } else {
                    lastBusiness.setCmd("auw");
                }
                lastBusiness.setAus(4);
                frame.setBusiness(lastBusiness);

                IseRequestData lastData = new IseRequestData();
                lastData.setStatus(2);
                lastData.setData("");

                frame.setData(lastData);
                break;
        }
        webSocket.send(StringUtils.gson.toJson(frame));
    }

    /**
     * 发送文件给语音评测服务端
     *
     * @param file 发送的文件
     * @throws FileNotFoundException
     */
    public void send(File file, WebSocketListener webSocketListener) throws FileNotFoundException, MalformedURLException, SignatureException {
        FileInputStream fileInputStream = new FileInputStream(file);
        send(fileInputStream, webSocketListener);
    }

    /**
     * 发送文件流给服务端
     *
     * @param inputStream 需要发送的流
     */
    public void send(InputStream inputStream, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        if (inputStream == null) {
            return;
        }
        // 创建webSocket连接
        createWebSocketConnect(webSocketListener);

        // 数据发送任务
        IseSendTask iseSendTask = new IseSendTask();
        new IseSendTask.Builder()
                .inputStream(inputStream)
                .webSocketClient(this)
                .build(iseSendTask);

        executorService.submit(iseSendTask);
    }

    /**
     * @param bytes
     * @param closeable 需要关闭的流，可为空
     */
    public void send(byte[] bytes, Closeable closeable, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        if (bytes == null || bytes.length == 0) {
            return;
        }
        // 创建webSocket连接
        createWebSocketConnect(webSocketListener);

        IseSendTask iseSendTask = new IseSendTask();
        new IseSendTask.Builder()
                .bytes(bytes)
                .webSocketClient(this)
                .closeable(closeable)
                .build(iseSendTask);

        executorService.submit(iseSendTask);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public boolean isTtpSkip() {
        return ttp_skip;
    }

    public String getExtraAbility() {
        return extra_ability;
    }

    public String getCheckType() {
        return check_type;
    }

    public String getIseUnite() {
        return ise_unite;
    }

    public String getSub() {
        return sub;
    }

    public String getEnt() {
        return ent;
    }

    public String getCategory() {
        return category;
    }

    public int getAus() {
        return aus;
    }

    public String getCmd() {
        return cmd;
    }

    public String getText() {
        return text;
    }

    public String getTte() {
        return tte;
    }

    public String getAue() {
        return aue;
    }

    public String getAuf() {
        return auf;
    }

    public String getRstcd() {
        return rstcd;
    }

    public String getGroup() {
        return group;
    }

    public String getGrade() {
        return grade;
    }

    public String getRst() {
        return rst;
    }

    public String getPlev() {
        return plev;
    }

    public String getHostUrl() {
        return originHostUrl;
    }

    public String getOriginHostUrl() {
        return originHostUrl;
    }

    public Request getRequest() {
        return request;
    }

    public OkHttpClient getClient() {
        return okHttpClient;
    }

    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }

    public int getCallTimeout() {
        return callTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public int getPingInterval() {
        return pingInterval;
    }

    public AbstractSignature getSignature() {
        return signature;
    }

    public Integer getFrameSize() {
        return frameSize;
    }

    public static class Builder {

        private JsonObject common = new JsonObject();
        private JsonObject business = new JsonObject();

        private String appId;
        private String sub;
        private String ent;
        private String category;
        private int aus;
        private String cmd;
        private String text;
        private String tte;
        private boolean ttpSkip = true;
        private String extraAbility;
        private String aue = "raw";
        private String auf = "audio/L16;rate=16000";
        private String rstcd = "gbk";
        private String group;
        private String checkType;
        private String grade;
        private String rst = "entirety";
        private String iseUnite = "0";
        private String plev = "0";

        private String hostUrl = IseConstant.HOST_URL;
        private String apiKey;
        private String apiSecret;

        private Integer frameSize = IseConstant.ISE_SIZE_FRAME;
        private Hmac256Signature signature;
        private Request request;
        private OkHttpClient client;
        private ExecutorService executorService;
        private Proxy proxy;

        /**
         * websocket相关
         */
        boolean retryOnConnectionFailure = true;
        int callTimeout = 0;
        int connectTimeout = 10000;
        int readTimeout = 10000;
        int writeTimeout = 10000;
        int pingInterval = 0;

        public IseClient build() {
            return new IseClient(this);
        }

        public IseClient.Builder signature(String appId, String apiKey, String secretKey) {
            this.appId = appId;
            this.apiKey = apiKey;
            this.apiSecret = secretKey;
            Hmac256Signature signature = new Hmac256Signature(apiKey, secretKey, hostUrl);
            this.signature = signature;
            return this;
        }

        public IseClient.Builder hostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        public IseClient.Builder addAppId(String appId) {
            this.appId = appId;
            common.addProperty("app_id", appId);
            return this;
        }

        public IseClient.Builder addSub(String sub) {
            business.addProperty("sub", sub);
            this.sub = sub;
            return this;
        }

        public IseClient.Builder addEnt(String ent) {
            this.ent = ent;
            business.addProperty("ent", ent);
            return this;
        }

        public IseClient.Builder addCategory(String category) {
            this.category = category;
            business.addProperty("category", category);
            return this;
        }

        public IseClient.Builder addAus(int aus) {
            this.aus = aus;
            business.addProperty("aus", aus);
            return this;
        }

        public IseClient.Builder addCmd(String cmd) {
            this.cmd = cmd;
            business.addProperty("cmd", cmd);
            return this;
        }

        public IseClient.Builder addText(String text) {
            this.text = text;
            business.addProperty("text", text);
            return this;
        }

        public IseClient.Builder addTte(String tte) {
            this.tte = tte;
            business.addProperty("tte", tte);
            return this;
        }

        public IseClient.Builder addTtpSkip(boolean ttpSkip) {
            this.ttpSkip = ttpSkip;
            business.addProperty("ttp_skip", ttpSkip);
            return this;
        }

        public IseClient.Builder addExtraAbility(String extraAbility) {
            this.extraAbility = extraAbility;
            business.addProperty("extra_ability", extraAbility);
            return this;
        }

        public IseClient.Builder addAue(String aue) {
            this.aue = aue;
            business.addProperty("aue", aue);
            return this;
        }

        public IseClient.Builder addAuf(String auf) {
            this.auf = auf;
            business.addProperty("auf", auf);
            return this;
        }

        public IseClient.Builder addRstcd(String rstcd) {
            this.rstcd = rstcd;
            business.addProperty("rstcd", rstcd);
            return this;
        }

        public IseClient.Builder addGroup(String group) {
            this.group = group;
            business.addProperty("group", group);
            return this;
        }

        public IseClient.Builder addCheckType(String checkType) {
            this.checkType = checkType;
            business.addProperty("check_type", checkType);
            return this;
        }

        public IseClient.Builder addGrade(String grade) {
            this.grade = grade;
            business.addProperty("grade", grade);
            return this;
        }

        public IseClient.Builder addRst(String rst) {
            this.rst = rst;
            business.addProperty("rst", rst);
            return this;
        }

        public IseClient.Builder addIseUnite(String iseUnite) {
            this.iseUnite = iseUnite;
            business.addProperty("ise_unite", iseUnite);
            return this;
        }

        public IseClient.Builder addPlev(String plev) {
            this.plev = plev;
            business.addProperty("plev", plev);
            return this;
        }

        public IseClient.Builder addHostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        public IseClient.Builder addAppKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public IseClient.Builder addAppSecret(String apiSecret) {
            this.apiSecret = apiSecret;
            return this;
        }

        public IseClient.Builder addCommon(JsonObject common) {
            this.common = common;
            return this;
        }

        public Hmac256Signature getSignature() {
            return signature;
        }

        public IseClient.Builder addSignature(Hmac256Signature signature) {
            this.signature = signature;
            return this;
        }

        public IseClient.Builder addRequest(Request request) {
            this.request = request;
            return this;
        }


        public IseClient.Builder addClient(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public IseClient.Builder addBusiness(JsonObject business) {
            this.business = business;
            return this;
        }

        public IseClient.Builder frameSize(Integer frameSize) {
            this.frameSize = frameSize;
            return this;
        }

        public IseClient.Builder callTimeout(long timeout, TimeUnit unit) {
            this.callTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public IseClient.Builder connectTimeout(long timeout, TimeUnit unit) {
            this.connectTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public IseClient.Builder readTimeout(long timeout, TimeUnit unit) {
            this.readTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public IseClient.Builder writeTimeout(long timeout, TimeUnit unit) {
            this.writeTimeout = Util.checkDuration("timeout", timeout, unit);
            return this;
        }

        public IseClient.Builder pingInterval(long interval, TimeUnit unit) {
            this.pingInterval = Util.checkDuration("interval", interval, unit);
            return this;
        }

        public IseClient.Builder retryOnConnectionFailure(boolean retryOnConnectionFailure) {
            this.retryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }

        public IseClient.Builder executorService(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        public IseClient.Builder client(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public IseClient.Builder proxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }
    }
}
