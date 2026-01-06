package cn.xfyun.model.websearch;


/**
 * 聚合搜素哦请求实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class WebSearchParam {

    /**
     * 用户输入的搜索关键词或问题
     * 非空字符串，建议 ≤512 字符
     */
    private String query;

    /**
     * 返回结果的最大条数
     * 1 ~ 20，默认为 10
     */
    private Integer limit;

    /**
     * 重排开关项
     */
    private Boolean openRerank;

    /**
     * 全文开关项
     */
    private Boolean openFullText;

    public WebSearchParam(Builder builder) {
        this.query = builder.query;
        this.limit = builder.limit;
        this.openRerank = builder.openRerank;
        this.openFullText = builder.openFullText;
    }

    public WebSearchParam() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Boolean getOpenRerank() {
        return openRerank;
    }

    public void setOpenRerank(Boolean openRerank) {
        this.openRerank = openRerank;
    }

    public Boolean getOpenFullText() {
        return openFullText;
    }

    public void setOpenFullText(Boolean openFullText) {
        this.openFullText = openFullText;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String query;
        private Integer limit;
        private Boolean openRerank;
        private Boolean openFullText;

        private Builder() {
        }

        public WebSearchParam build() {
            return new WebSearchParam(this);
        }

        public Builder query(String query) {
            this.query = query;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder openRerank(boolean openRerank) {
            this.openRerank = openRerank;
            return this;
        }

        public Builder openFullText(boolean openFullText) {
            this.openFullText = openFullText;
            return this;
        }
    }
}
