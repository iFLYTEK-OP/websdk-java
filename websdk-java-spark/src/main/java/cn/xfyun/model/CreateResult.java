package cn.xfyun.model;

/**
 * PPT的生成结果
 * @author junzhang27
 */
public class CreateResult {
    private int code;
    private String desc;
    private Data data;

    public static class Data {
        /**
         * 请求唯一ID
         */
        private String sid;
        /**
         * 封面地址
         */
        private String coverImgSrc;
        /**
         * 主标题
         */
        private String title;
        /**
         * 副标题
         */
        private String subTitle;

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
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
