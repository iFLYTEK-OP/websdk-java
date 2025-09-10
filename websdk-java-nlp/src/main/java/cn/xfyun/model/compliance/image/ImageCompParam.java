package cn.xfyun.model.compliance.image;

import cn.xfyun.config.ModeType;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.StringUtils;

/**
 * 图片合规请求参数
 *
 * @author <zyding6@ifytek.com>
 **/
public class ImageCompParam {

    /**
     * 待识别文本，文本长度最大支持 5 千字符
     */
    private String content;

    /**
     * 文本模式
     * modeType为link时，值为外链信息
     * modeType为base64时，值为图片base64编码信息
     */
    private ModeType modeType;

    /**
     * 指定检测的敏感分类：
     * pornDetection 色情
     * violentTerrorism 暴恐
     * political 涉政
     * lowQualityIrrigation 低质量灌水
     * contraband 违禁
     * advertisement 广告
     * uncivilizedLanguage 不文明用语
     */
    private String bizType;

    public ImageCompParam(Builder builder) {
        this.modeType = builder.modeType;
        this.content = builder.content;
        this.bizType = builder.bizType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ModeType getModeType() {
        return modeType;
    }

    public void setModeType(ModeType modeType) {
        this.modeType = modeType;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public void selfCheck() {
        if (StringUtils.isNullOrEmpty(content) || content.length() > 5000) {
            throw new BusinessException("content参数不合法");
        }
    }

    public static final class Builder {

        private String content;
        private ModeType modeType;
        private String bizType;

        private Builder() {
        }

        public ImageCompParam build() {
            return new ImageCompParam(this);
        }

        public Builder modeType(ModeType modeType) {
            this.modeType = modeType;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder bizType(String bizType) {
            this.bizType = bizType;
            return this;
        }
    }
}
