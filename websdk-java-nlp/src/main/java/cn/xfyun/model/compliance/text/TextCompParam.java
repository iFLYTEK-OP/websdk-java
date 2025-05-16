package cn.xfyun.model.compliance.text;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.StringUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 文本合规请求参数
 *
 * @author <zyding6@ifytek.com>
 **/
public class TextCompParam {

    /**
     * 是否全匹配：
     * 1 代表是
     * 0 代表否
     * 默认取值0，匹配到敏感词则不再匹配，不会返回所有敏感分类。
     */
    @SerializedName("is_match_all")
    private Integer isMatchAll;

    /**
     * 待识别文本，文本长度最大支持 5 千字符
     */
    private String content;

    /**
     * 指定自定义词库id列表，通过接口创建词库后返回，可以同时携带多个黑白名单id
     */
    @SerializedName("lib_ids")
    private List<String> libIds;

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
    private List<String> categories;

    public TextCompParam(Builder builder) {
        this.isMatchAll = builder.isMatchAll;
        this.content = builder.content;
        this.libIds = builder.libIds;
        this.categories = builder.categories;
    }

    public Integer getIsMatchAll() {
        return isMatchAll;
    }

    public void setIsMatchAll(Integer isMatchAll) {
        this.isMatchAll = isMatchAll;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getLibIds() {
        return libIds;
    }

    public void setLibIds(List<String> libIds) {
        this.libIds = libIds;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
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

        private Integer isMatchAll;
        private String content;
        private List<String> libIds;
        private List<String> categories;

        private Builder() {
        }

        public TextCompParam build() {
            return new TextCompParam(this);
        }

        public Builder isMatchAll(int isMatchAll) {
            this.isMatchAll = isMatchAll;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder libIds(List<String> libIds) {
            this.libIds = libIds;
            return this;
        }

        public Builder categories(List<String> categories) {
            this.categories = categories;
            return this;
        }
    }
}
