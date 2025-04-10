package cn.xfyun.model.aippt.response;

import cn.xfyun.model.aippt.request.Outline;

/**
 * PPT生成响应体
 *
 * @author <zyding6@ifytek.com>
 **/
public class PPTCreateResponse {


    /**
     * flag : true
     * code : 0
     * desc : 成功
     * count : null
     * data : {"sid":"7416b894bdd54ccc95bab7400113989e","coverImgSrc":"PPT封面图链接","title":"合肥天气趋势分析","subTitle":"探索气候变化与城市生活影响","outline":null}
     */

    private Boolean flag;
    private Integer code;
    private String desc;
    private Integer count;
    private Data data;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        /**
         * sid : 7416b894bdd54ccc95bab7400113989e
         * coverImgSrc : PPT封面图链接
         * title : 合肥天气趋势分析
         * subTitle : 探索气候变化与城市生活影响
         * outline : null
         */

        private String sid;
        private String coverImgSrc;
        private String title;
        private String subTitle;
        private Outline outline;

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getCoverImgSrc() {
            return coverImgSrc;
        }

        public void setCoverImgSrc(String coverImgSrc) {
            this.coverImgSrc = coverImgSrc;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public Outline getOutline() {
            return outline;
        }

        public void setOutline(Outline outline) {
            this.outline = outline;
        }
    }
}
