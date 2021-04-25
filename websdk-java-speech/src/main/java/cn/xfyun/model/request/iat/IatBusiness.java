package cn.xfyun.model.request.iat;

import cn.xfyun.api.IatClient;

/**
 * @author <ydwang16@iflytek.com>
 * @description 语音听写业务参数
 * @date 2021/3/24
 */
public class IatBusiness {
    /**
     * 语种
     * zh_cn：中文（支持简单的英文识别）
     * en_us：英文
     * 其他小语种：可到控制台-语音听写（流式版）-方言/语种处添加试用或购买，添加后会显示该小语种参数值，若未授权无法使用会报错11200
     */
    private String language = "zh_cn";

    /**
     * 应用领域
     * iat：日常用语
     * medical：医疗
     * gov-seat-assistant：政务坐席助手
     * seat-assistant：金融坐席助手
     * gov-ansys：政务语音分析
     * gov-nav：政务语音导航
     * fin-nav：金融语音导航
     * fin-ansys：金融语音分析
     * 注：除日常用语领域外其他领域若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处添加试用或购买；若未授权无法使用会报错11200。
     * 坐席助手、语音导航、语音分析相关垂直领域仅适用于8k采样率的音频数据，另外三者的区别详见下方。
     */
    private String domain = "iat";

    /**
     * 方言，当前仅在language为中文时，支持方言选择。
     * mandarin：中文普通话、其他语种
     * 其他方言：可到控制台-语音听写（流式版）-方言/语种处添加试用或购买，添加后会显示该方言参数值；方言若未授权无法使用会报错11200。
     */
    private String accent;

    /**
     * 用于设置端点检测的静默时间，单位是毫秒。
     * 即静默多长时间后引擎认为音频结束。
     * 默认2000（小语种除外，小语种不设置该参数默认为未开启VAD）。
     */
    private Integer vad_eos;

    /**
     * （仅中文普通话支持）动态修正
     * wpgs：开启流式结果返回功能
     * 注：该扩展功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处免费开通；若未授权状态下设置该参数并不会报错，但不会生效。
     */
    private String dwa;

    /**
     * 仅中文支持）领域个性化参数
     * game：游戏
     * health：健康
     * shopping：购物
     * trip：旅行
     * 注：该扩展功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处添加试用或购买；若未授权状态下设置该参数并不会报错，但不会生效。
     */
    private String pd;

    /**
     * （仅中文支持）是否开启标点符号添加
     * 1：开启（默认值）
     * 0：关闭
     */
    private Integer ptt = 1;

    /**
     * （仅中文支持）字体
     * zh-cn :简体中文（默认值）
     * zh-hk :繁体香港
     * 注：该繁体功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处免费开通；若未授权状态下设置为繁体并不会报错，但不会生效。
     */
    private String rlang = "zh-cn";

    /**
     * 返回子句结果对应的起始和结束的端点帧偏移值。端点帧偏移值表示从音频开头起已过去的帧长度。
     * 0：关闭（默认值）
     * 1：开启
     * 开启后返回的结果中会增加data.result.vad字段，详见下方返回结果。
     * 注：若开通并使用了动态修正功能，则该功能无法使用。
     */
    private Integer vinfo = 0;

    /**
     * （中文普通话和日语支持）将返回结果的数字格式规则为阿拉伯数字格式，默认开启
     * 0：关闭
     * 1：开启
     */
    private Integer nunum = 1;

    /**
     * speex音频帧长，仅在speex音频时使用
     * 1 当speex编码为标准开源speex编码时必须指定
     * 2 当speex编码为讯飞定制speex编码时不要设置
     */
    private Integer speex_size;

    /**
     * 取值范围[1,5]，通过设置此参数，获取在发音相似时的句子多侯选结果。设置多候选会影响性能，响应时间延迟200ms左右。
     * 注：该扩展功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处免费开通；若未授权状态下设置该参数并不会报错，但不会生效。
     */
    private Integer nbest;

    /**
     * 取值范围[1,5]，通过设置此参数，获取在发音相似时的词语多侯选结果。设置多候选会影响性能，响应时间延迟200ms左右。
     * 注：该扩展功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处免费开通；若未授权状态下设置该参数并不会报错，但不会生效。
     */
    private Integer wbest;

    public IatBusiness(IatClient iatClient) {
        this.accent = iatClient.getAccent();
        this.domain = iatClient.getDomain();
        this.dwa = iatClient.getDwa();
        this.language = iatClient.getLanguage();
        this.nbest = iatClient.getNbest();
        this.nunum = iatClient.getNunum();
        this.pd = iatClient.getPd();
        this.ptt = iatClient.getPtt();
        this.rlang = iatClient.getRlang();
        this.speex_size = iatClient.getSpeex_size();
        this.vad_eos = iatClient.getVad_eos();
        this.vinfo = iatClient.getVinfo();
        this.wbest = iatClient.getWbest();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAccent() {
        return accent;
    }

    public void setAccent(String accent) {
        this.accent = accent;
    }

    public String getDwa() {
        return dwa;
    }

    public void setDwa(String dwa) {
        this.dwa = dwa;
    }

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public Integer getPtt() {
        return ptt;
    }

    public void setPtt(Integer ptt) {
        this.ptt = ptt;
    }

    public String getRlang() {
        return rlang;
    }

    public void setRlang(String rlang) {
        this.rlang = rlang;
    }

    public Integer getVinfo() {
        return vinfo;
    }

    public void setVinfo(Integer vinfo) {
        this.vinfo = vinfo;
    }

    public Integer getNunum() {
        return nunum;
    }

    public void setNunum(Integer nunum) {
        this.nunum = nunum;
    }

    public Integer getNbest() {
        return nbest;
    }

    public void setNbest(Integer nbest) {
        this.nbest = nbest;
    }

    public Integer getWbest() {
        return wbest;
    }

    public void setWbest(Integer wbest) {
        this.wbest = wbest;
    }

    public Integer getVad_eos() {
        return vad_eos;
    }

    public void setVad_eos(Integer vad_eos) {
        this.vad_eos = vad_eos;
    }

    public Integer getSpeex_size() {
        return speex_size;
    }

    public void setSpeex_size(Integer speex_size) {
        this.speex_size = speex_size;
    }
}
