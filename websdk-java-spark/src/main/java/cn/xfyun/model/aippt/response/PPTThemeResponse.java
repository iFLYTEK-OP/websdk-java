package cn.xfyun.model.aippt.response;

import java.util.List;

/**
 * PPT主题响应体
 *
 * @author <zyding6@ifytek.com>
 **/
public class PPTThemeResponse {

    private Boolean flag;
    private Integer code;
    private String desc;
    private Integer count;
    private Data data;
    private String sid;

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

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public static class Data {

        private Long total;
        private Integer pageNum;
        private List<Records> records;

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public Integer getPageNum() {
            return pageNum;
        }

        public void setPageNum(Integer pageNum) {
            this.pageNum = pageNum;
        }

        public List<Records> getRecords() {
            return records;
        }

        public void setRecords(List<Records> records) {
            this.records = records;
        }

        public static class Records {

            private String templateIndexId;
            private Integer pageCount;
            private String type;
            private String color;
            private String industry;
            private String style;
            private String detailImage;
            private String payType;

            public String getTemplateIndexId() {
                return templateIndexId;
            }

            public void setTemplateIndexId(String templateIndexId) {
                this.templateIndexId = templateIndexId;
            }

            public Integer getPageCount() {
                return pageCount;
            }

            public void setPageCount(Integer pageCount) {
                this.pageCount = pageCount;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
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

            public String getStyle() {
                return style;
            }

            public void setStyle(String style) {
                this.style = style;
            }

            public String getDetailImage() {
                return detailImage;
            }

            public void setDetailImage(String detailImage) {
                this.detailImage = detailImage;
            }

            public String getPayType() {
                return payType;
            }

            public void setPayType(String payType) {
                this.payType = payType;
            }
        }
    }
}
