package cn.xfyun.model;


import java.util.List;

/**
 * PPT的主题查询结果
 * @author junzhang27
 */
public class ThemeResponse {
    private boolean flag;
    private int code;
    private String desc;
    private Integer count;
    private List<Data> data;

    public static class Data {
        /**
         * 主题key
         */
        private String key;
        /**
         * 主题name
         */
        private String name;
        /**
         * 缩略图
         */
        private String thumbnail;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
