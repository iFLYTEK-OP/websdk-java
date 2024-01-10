package cn.xfyun.model;


/**
 * PPT的生成参数
 * @author junzhang27
 */
public class CreateParam {
    /**
     * 用户生成PPT要求（最多8000字）
     */
    private String query;

    /**
     * PPT生成类型：文本生成、话题生成、程序判断(默认)
     * <p>
     * auto： 自动，由程序自行判断；
     * topic：话题生成（建议150字以内）
     * text：文本生成，基于长文本生成
     */
    private String createModel = "auto";

    /**
     * PPT生成主题：随机主题(默认)、紫影幽蓝
     * auto：自动，随机主题
     * purple：紫色主题
     * green：绿色主题
     * lightblue：清逸天蓝
     * taupe：质感之境
     * blue：星光夜影
     * telecomRed：炽热暖阳
     * telecomGreen：幻翠奇旅
     */
    private String theme = "auto";

    /**
     * PPT详细程度：标准版(默认)、复杂版
     * standard： 标准
     * complex：复杂
     */
    private String contentLevel = "standard";

    /**
     * 业务ID（非必传）- 业务方自行决定是否使用
     */
    private String businessId;

    private CreateParam() {
        // Private constructor to enforce the use of the builder
    }

    public static class Builder {
        private CreateParam createParam;

        public Builder() {
            createParam = new CreateParam();
        }

        public Builder query(String query) {
            createParam.query = query;
            return this;
        }

        public Builder createModel(String createModel) {
            createParam.createModel = createModel;
            return this;
        }

        public Builder theme(String theme) {
            createParam.theme = theme;
            return this;
        }

        public Builder contentLevel(String contentLevel) {
            createParam.contentLevel = contentLevel;
            return this;
        }

        public Builder businessId(String businessId) {
            createParam.businessId = businessId;
            return this;
        }

        public CreateParam build() {
            return createParam;
        }
    }

    public String getQuery() {
        return query;
    }

    public String getCreateModel() {
        return createModel;
    }

    public String getTheme() {
        return theme;
    }

    public String getContentLevel() {
        return contentLevel;
    }

    public String getBusinessId() {
        return businessId;
    }
}

