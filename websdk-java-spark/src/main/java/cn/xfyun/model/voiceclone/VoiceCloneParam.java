package cn.xfyun.model.voiceclone;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.StringUtils;

/**
 * 一句话复刻请求参数
 *
 * @author zyding
 */
public class VoiceCloneParam {

    /**
     * 合成文本
     * 文本数据[1,8000]
     * 文本内容，base64编码后不超过8000字节，约2000个字符
     */
    private String text;

    /**
     * 训练得到的音色id
     */
    private String resId;

    /**
     * 发音风格
     * 仅中文美化版本可以用
     */
    private String style;

    /**
     * 合成的语种
     * 注意：需要和训练时指定的语种保持一致
     * 中：0 英：1 日：2 韩：3 俄：4
     * 默认 0
     */
    private Integer languageId;

    /**
     * 语速[0,100]
     * 语速：0对应默认语速的1/2，100对应默认语速的2倍
     */
    private Integer speed;

    /**
     * 音量[0,100]
     * 音量：0对应默认音量的1/2，100对应默认音量的2倍
     */
    private Integer volume;

    /**
     * 语调[0,100]
     * 语调：0对应默认语速的1/2，100对应默认语速的2倍
     */
    private Integer pitch;

    /**
     * 背景音   默认0
     */
    private Integer bgs;

    /**
     * 英文发音方式，
     * 0:自动判断处理，如果不确定将按照英文词语拼写处理（缺省）,
     * 1:所有英文按字母发音
     * 2:自动判断处理，如果不确定将按照字母朗读
     */
    private Integer reg;

    /**
     * 合成音频数字发音方式，
     * 0:自动判断（缺省）,
     * 1:完全数值,
     * 2:完全字符串,
     * 3:字符串优先
     */
    private Integer rdn;

    /**
     * 是否返回拼音标注，
     * 0:不返回拼音,
     * 1:返回拼音（纯文本格式，utf8编码）,
     * 3:支持文本中的标点符号输出（纯文本格式，utf8编码）
     * 默认 0
     */
    private Integer rhy;

    /**
     * 发言人名称
     * 固定值x5_clone
     */
    private String vcn;

    public VoiceCloneParam(Builder builder) {
        this.text = builder.text;
        this.vcn = builder.vcn;
        this.style = builder.style;
        this.speed = builder.speed;
        this.volume = builder.volume;
        this.pitch = builder.pitch;
        this.resId = builder.resId;
        this.rhy = builder.rhy;
        this.rdn = builder.rdn;
        this.reg = builder.reg;
        this.bgs = builder.bgs;
        this.languageId = builder.languageId;
    }

    public VoiceCloneParam() {
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getPitch() {
        return pitch;
    }

    public void setPitch(Integer pitch) {
        this.pitch = pitch;
    }

    public Integer getBgs() {
        return bgs;
    }

    public void setBgs(Integer bgs) {
        this.bgs = bgs;
    }

    public Integer getReg() {
        return reg;
    }

    public void setReg(Integer reg) {
        this.reg = reg;
    }

    public Integer getRdn() {
        return rdn;
    }

    public void setRdn(Integer rdn) {
        this.rdn = rdn;
    }

    public Integer getRhy() {
        return rhy;
    }

    public void setRhy(Integer rhy) {
        this.rhy = rhy;
    }

    public String getVcn() {
        return vcn;
    }

    public void setVcn(String vcn) {
        this.vcn = vcn;
    }

    public void selfCheck() {
        if (StringUtils.isNullOrEmpty(resId)) {
            throw new BusinessException("声纹信息不能为空");
        }
        if (StringUtils.isNullOrEmpty(text)) {
            throw new BusinessException("合成文本不能为空");
        }
    }

    public static VoiceCloneParam.Builder builder() {
        return new VoiceCloneParam.Builder();
    }

    public static final class Builder {

        private String text;
        private String resId;
        private Integer languageId;
        private Integer speed;
        private Integer volume;
        private Integer pitch;
        private Integer bgs;
        private Integer reg;
        private Integer rdn;
        private Integer rhy;
        private String vcn;
        private String style;

        private Builder() {
        }

        public VoiceCloneParam build() {
            return new VoiceCloneParam(this);
        }

        public Builder style(String style) {
            this.style = style;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder resId(String resId) {
            this.resId = resId;
            return this;
        }

        public Builder languageId(Integer languageId) {
            this.languageId = languageId;
            return this;
        }

        public Builder speed(Integer speed) {
            this.speed = speed;
            return this;
        }

        public Builder volume(Integer volume) {
            this.volume = volume;
            return this;
        }

        public Builder pitch(Integer pitch) {
            this.pitch = pitch;
            return this;
        }

        public Builder bgs(Integer bgs) {
            this.bgs = bgs;
            return this;
        }

        public Builder reg(Integer reg) {
            this.reg = reg;
            return this;
        }

        public Builder rdn(Integer rdn) {
            this.rdn = rdn;
            return this;
        }

        public Builder rhy(Integer rhy) {
            this.rhy = rhy;
            return this;
        }

        public Builder vcn(String vcn) {
            this.vcn = vcn;
            return this;
        }
    }
}
