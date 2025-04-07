package cn.xfyun.model.aippt.request;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.StringUtils;

import java.io.File;

/**
 * PPT生成请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class PPTCreate {

    /**
     * 用户生成PPT要求（最多8000字；query、file、fileUrl必填其一）
     * 注意：query不能为空字符串、仅包含空格的字符串
     */
    private String query;

    /**
     * 上传文件 (file、fileUrl、query必填其一，如果传file或者fileUrl，fileName必填)
     * 支持上传pdf、doc、docx、txt、md等文件
     */
    private File file;

    /**
     * 文件地址（file、fileUrl、query必填其一，如果传file或者fileUrl，fileName必填）
     */
    private String fileUrl;

    /**
     * 文件名(带文件名后缀；如果传file或者fileUrl，fileName必填)
     */
    private String fileName;

    /**
     * 直接供用户检索模板的ID,从PPT主题列表查询中获取
     */
    private String templateId;

    /**
     * 业务ID（非必传）- 业务方自行决定是否使用
     */
    private String businessId;

    /**
     * PPT作者名：用户自行选择是否设置作者名
     */
    private String author;

    /**
     * 是否生成PPT演讲备注
     */
    private Boolean isCardNote;

    /**
     * 是否联网搜索
     */
    private Boolean search;

    /**
     * 语种
     * language：
     * cn：中文（简体）
     * en：英语
     * ja：日语
     * ru：俄语
     * ko：韩语
     * de：德语
     * fr：法语
     * pt：葡萄牙语
     * es：西班牙语
     * it：意大利语
     * th：泰语
     */
    private String language;

    /**
     * 是否自动配图
     */
    private Boolean isFigure;

    /**
     * ai配图类型： normal、advanced （isFigure为true的话生效）；
     * normal-普通配图，20%正文配图；
     * advanced-高级配图，50%正文配图
     */
    private String aiImage;

    /**
     * 大纲内容
     * 不得超过20个一级大纲--outline.chapters[].chapterTitle
     */
    private Outline outline;

    /**
     * 已生成大纲后，响应返回的请求大纲唯一id
     */
    private String outlineSid;

    public PPTCreate(Builder builder) {
        this.query = builder.query;
        this.file = builder.file;
        this.fileUrl = builder.fileUrl;
        this.fileName = builder.fileName;
        this.templateId = builder.templateId;
        this.businessId = builder.businessId;
        this.author = builder.author;
        this.isCardNote = builder.isCardNote;
        this.search = builder.search;
        this.language = builder.language;
        this.isFigure = builder.isFigure;
        this.aiImage = builder.aiImage;
        this.outline = builder.outline;
        this.outlineSid = builder.outlineSid;
    }

    public PPTCreate() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getCardNote() {
        return isCardNote;
    }

    public void setCardNote(Boolean cardNote) {
        isCardNote = cardNote;
    }

    public Boolean getSearch() {
        return search;
    }

    public void setSearch(Boolean search) {
        this.search = search;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getFigure() {
        return isFigure;
    }

    public void setFigure(Boolean figure) {
        isFigure = figure;
    }

    public String getAiImage() {
        return aiImage;
    }

    public void setAiImage(String aiImage) {
        this.aiImage = aiImage;
    }

    public Outline getOutline() {
        return outline;
    }

    public void setOutline(Outline outline) {
        this.outline = outline;
    }

    public String getOutlineSid() {
        return outlineSid;
    }

    public void setOutlineSid(String outlineSid) {
        this.outlineSid = outlineSid;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public void createCheck() {
        if (!StringUtils.isNullOrEmpty(query) && query.length() > 8000) {
            throw new BusinessException("query参数最大8000字符");
        }

        if (file == null && StringUtils.isNullOrEmpty(fileUrl) && StringUtils.isNullOrEmpty(query)) {
            throw new BusinessException("query、file、fileUrl参数必填其一");
        }

        if ((null != file || !StringUtils.isNullOrEmpty(fileUrl)) && StringUtils.isNullOrEmpty(fileName)) {
            throw new BusinessException("文件名称不能为空");
        }
    }

    public void createOutLineCheck() {
        if (StringUtils.isNullOrEmpty(query) || query.length() > 8000) {
            throw new BusinessException("query参数不合法");
        }
    }

    public void createOutlineByDocCheck() {
        if (StringUtils.isNullOrEmpty(fileName)) {
            throw new BusinessException("fileName不能为空");
        }

        if (StringUtils.isNullOrEmpty(fileUrl) && null == file) {
            throw new BusinessException("(file、fileUrl必填其一");
        }
    }

    public void createPptByOutlineCheck() {
        if (StringUtils.isNullOrEmpty(query) || query.length() > 8000) {
            throw new BusinessException("query参数不合法");
        }

        if (null == outline) {
            throw new BusinessException("大纲内容不能为空");
        }
    }

    public static final class Builder {

        private String query;
        private File file;
        private String fileUrl;
        private String fileName;
        private String templateId;
        private String businessId;
        private String author = "讯飞智文";
        private Boolean isCardNote = false;
        private Boolean search = false;
        private String language = "cn";
        private Boolean isFigure = false;
        private String aiImage;
        private Outline outline;
        private String outlineSid;

        private Builder() {
        }

        public PPTCreate build() {
            return new PPTCreate(this);
        }

        public Builder query(String query) {
            this.query = query;
            return this;
        }

        public Builder file(File file, String fileName) {
            this.file = file;
            this.fileName = fileName;
            return this;
        }

        public Builder fileUrl(String fileUrl, String fileName) {
            this.fileUrl = fileUrl;
            this.fileName = fileName;
            return this;
        }

        public Builder templateId(String templateId) {
            this.templateId = templateId;
            return this;
        }

        public Builder businessId(String businessId) {
            this.businessId = businessId;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder isCardNote(Boolean isCardNote) {
            this.isCardNote = isCardNote;
            return this;
        }

        public Builder search(Boolean search) {
            this.search = search;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder isFigure(Boolean isFigure) {
            this.isFigure = isFigure;
            return this;
        }

        public Builder aiImage(String aiImage) {
            this.aiImage = aiImage;
            return this;
        }

        public Builder outline(Outline outline) {
            this.outline = outline;
            return this;
        }

        public Builder outlineSid(String outlineSid) {
            this.outlineSid = outlineSid;
            return this;
        }
    }
}
