package cn.xfyun.model.aippt.request;

import cn.xfyun.util.StringUtils;

/**
 * PPT主题列表查询请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class PPTSearch {


    /**
     * style : 简约
     * color : 红色
     * industry : 教育培训
     * pageNum : 1
     * pageSize : 10
     */

    private String style;
    private String color;
    private String industry;
    private int pageNum;
    private int pageSize;
    private String url;

    public PPTSearch(Builder builder) {
        this.style = builder.style;
        this.color = builder.color;
        this.industry = builder.industry;
        this.pageNum = builder.pageNum;
        this.pageSize = builder.pageSize;
        this.url = builder.url;
    }

    public PPTSearch() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public static final class Builder {
        private String style;
        private String color;
        private String industry;
        private int pageNum = 1;
        private int pageSize = 10;
        private String url;

        private Builder() {
        }

        public PPTSearch build() {
            return new PPTSearch(this);
        }

        public Builder style(String style) {
            this.style = style;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder industry(String industry) {
            this.industry = industry;
            return this;
        }

        public Builder pageNum(int pageNum) {
            this.pageNum = pageNum;
            return this;
        }

        public Builder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }
    }
}
