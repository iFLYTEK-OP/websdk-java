package cn.xfyun.api;

import cn.xfyun.base.webscoket.WebSocketBuilder;
import cn.xfyun.base.webscoket.WebSocketClient;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import okhttp3.WebSocketListener;

import java.io.*;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.concurrent.*;


/**
 * @author: <flhong2@iflytek.com>
 * @description: 语音评测客户端
 * @version 1.0
 * @create: 2021-03-17 19:46
 **/
public class IseClient extends WebSocketClient {

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
     * 待评测文本编码
     * utf-8
     * gbk
     */
    private String tte;

    /**
     * 跳过ttp直接使用ssb中的文本进行评测（使用时结合cmd参数查看）,默认值true
     */
    private boolean ttpSkip;

    /**
     * 拓展能力（生效条件ise_unite="1", rst="entirety"）
     * 多维度分信息显示（准确度分、流畅度分、完整度打分）
     * extra_ability值为multi_dimension（字词句篇均适用,如选多个能力，用分号；隔开。例如：add("extra_ability"," syll_phone_err_msg;pitch;multi_dimension")）
     * 单词基频信息显示（基频开始值、结束值）
     * extra_ability值为pitch ，仅适用于单词和句子题型
     * 音素错误信息显示（声韵、调型是否正确）
     * extra_ability值为syll_phone_err_msg（字词句篇均适用,如选多个能力，用分号；隔开。例如：add("extra_ability"," syll_phone_err_msg;pitch;multi_dimension")）
     */
    private String extraAbility;

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
    private String checkType;

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
    private String plev ="0";

    private Integer frameSize;

    public IseClient(Builder builder) {
        super(builder);
        this.sub = builder.sub;
        this.ent = builder.ent;
        this.category = builder.category;
        this.aus = builder.aus;
        this.cmd = builder.cmd;
        this.tte = builder.tte;
        this.ttpSkip = builder.ttpSkip;
        this.extraAbility = builder.extraAbility;
        this.aue = builder.aue;
        this.auf = builder.auf;
        this.rstcd = builder.rstcd;
        this.group = builder.group;
        this.checkType = builder.checkType;
        this.grade = builder.grade;
        this.rst = builder.rst;
        this.ise_unite = builder.iseUnite;
        this.plev = builder.plev;
        this.frameSize = builder.frameSize;
    }

    private static ExecutorService executorService =
            new ThreadPoolExecutor(
                    1,
                    1,
                    60,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(),
                    new ThreadFactoryBuilder().setNameFormat("ise-task").build(),
                    new ThreadPoolExecutor.AbortPolicy()
            );

    /**
     * 发送文件给语音评测服务端
     *
     * @param file 发送的文件
     * @throws FileNotFoundException
     */
    public void send(File file, WebSocketListener webSocketListener) throws FileNotFoundException, MalformedURLException, SignatureException {
        createWebSocketConnect(webSocketListener);
        FileInputStream fileInputStream = new FileInputStream(file);
        send(fileInputStream, webSocketListener);
    }

    /**
     * 发送文件流给服务端
     *
     * @param inputStream 需要发送的流
     */
    public void send(InputStream inputStream, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
        // 创建webSocket连接
        createWebSocketConnect(webSocketListener);
        if (inputStream == null) {
            webSocket.close(1000, null);
            return;
        }

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
        // 创建webSocket连接
        createWebSocketConnect(webSocketListener);
        if (bytes == null || bytes.length == 0) {
            webSocket.close(1000, null);
            return;
        }

        IseSendTask iseSendTask = new IseSendTask();
        new IseSendTask.Builder()
                .bytes(bytes)
                .webSocketClient(this)
                .closeable(closeable)
                .build(iseSendTask);

        executorService.submit(iseSendTask);
    }



    public boolean isTtpSkip() {
        return ttpSkip;
    }

    public String getExtraAbility() {
        return extraAbility;
    }

    public String getCheckType() {
        return checkType;
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

    public Integer getFrameSize() {
        return frameSize;
    }

    @Override
    public String getSignature() {
        return null;
    }

    public static final class Builder extends WebSocketBuilder<Builder> {

        private static final String HOST_URL = "wss://ise-api.xfyun.cn/v2/open-ise";

        private String sub;

        private String ent;

        private String category;

        private int aus;

        private String cmd;

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

        private String plev ="0";

        private Integer frameSize = 1280;

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
        }

        @Override
        public IseClient build() {
            return new IseClient(this);
        }

        public IseClient.Builder sub(String sub){
            this.sub = sub;
            return this;
        }

        public IseClient.Builder ent(String ent){
            this.ent = ent;
            return this;
        }

        public IseClient.Builder category(String category){
            this.category = category;
            return this;
        }

        public IseClient.Builder aus(int aus){
            this.aus = aus;
            return this;
        }

        public IseClient.Builder cmd(String cmd){
            this.cmd = cmd;
            return this;
        }

        public IseClient.Builder tte(String tte){
            this.tte = tte;
            return this;
        }

        public IseClient.Builder ttpSkip(boolean ttpSkip){
            this.ttpSkip = ttpSkip;
            return this;
        }

        public IseClient.Builder extraAbility(String extraAbility){
            this.extraAbility = extraAbility;
            return this;
        }

        public IseClient.Builder aue(String aue){
            this.aue = aue;
            return this;
        }

        public IseClient.Builder auf(String auf){
            this.auf = auf;
            return this;
        }

        public IseClient.Builder rstcd(String rstcd){
            this.rstcd = rstcd;
            return this;
        }

        public IseClient.Builder group(String group){
            this.group = group;
            return this;
        }

        public IseClient.Builder checkType(String checkType){
            this.checkType = checkType;
            return this;
        }

        public IseClient.Builder grade(String grade){
            this.grade = grade;
            return this;
        }

        public IseClient.Builder rst(String rst){
            this.rst = rst;
            return this;
        }

        public IseClient.Builder iseUnite(String iseUnite){
            this.iseUnite = iseUnite;
            return this;
        }

        public IseClient.Builder plev(String plev){
            this.plev = plev;
            return this;
        }

        public IseClient.Builder frameSize(Integer frameSize) {
            this.frameSize = frameSize;
            return this;
        }

    }
}
