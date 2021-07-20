package cn.xfyun.model.request.ise;

import cn.xfyun.api.IseClient;

/**
 * @author: <flhong2@iflytek.com>
 * @description: 语音评测业务参数
 * @version: v1.0
 * @create: 2021-04-06 10:08
 **/
public class IseBusiness {

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
    private Integer aus;
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
    private boolean ttp_skip = true;
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
    private String aue = "raw";
    /**
     * 音频采样率
     * 默认 audio/L16;rate=16000
     */
    private String auf = "audio/L16;rate=16000";
    /**
     * 返回结果格式
     * utf8
     * gbk （默认值）
     */
    private String rstcd = "gbk";
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

    public IseBusiness() {
    }

    public IseBusiness(IseClient iseClient) {
        this.sub = iseClient.getSub();
        this.ent = iseClient.getEnt();
        this.category = iseClient.getCategory();
        this.aus = iseClient.getAus();
        this.cmd = iseClient.getCmd();
        this.text = iseClient.getText();
        this.tte = iseClient.getTte();
        this.ttp_skip = iseClient.isTtpSkip();
        this.extra_ability = iseClient.getExtraAbility();
        this.aue = iseClient.getAue();
        this.auf = iseClient.getAuf();
        this.rstcd = iseClient.getRstcd();
        this.group = iseClient.getGroup();
        this.check_type = iseClient.getCheckType();
        this.grade = iseClient.getGrade();
        this.rst = iseClient.getRst();
        this.ise_unite = iseClient.getIseUnite();
        this.plev = iseClient.getPlev();
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getEnt() {
        return ent;
    }

    public void setEnt(String ent) {
        this.ent = ent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAus() {
        return aus;
    }

    public void setAus(Integer aus) {
        this.aus = aus;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTte() {
        return tte;
    }

    public void setTte(String tte) {
        this.tte = tte;
    }


    public String getAue() {
        return aue;
    }

    public void setAue(String aue) {
        this.aue = aue;
    }

    public String getAuf() {
        return auf;
    }

    public void setAuf(String auf) {
        this.auf = auf;
    }

    public String getRstcd() {
        return rstcd;
    }

    public void setRstcd(String rstcd) {
        this.rstcd = rstcd;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getRst() {
        return rst;
    }

    public void setRst(String rst) {
        this.rst = rst;
    }

    public boolean isTtp_skip() {
        return ttp_skip;
    }

    public String getExtra_ability() {
        return extra_ability;
    }

    public String getCheck_type() {
        return check_type;
    }

    public String getIse_unite() {
        return ise_unite;
    }

    public String getPlev() {
        return plev;
    }

    public void setPlev(String plev) {
        this.plev = plev;
    }
}
