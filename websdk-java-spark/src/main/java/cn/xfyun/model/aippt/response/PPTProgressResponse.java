package cn.xfyun.model.aippt.response;

/**
 * PPT生成进度响应体
 *
 * @author <zyding6@ifytek.com>
 **/
public class PPTProgressResponse {


    /**
     * flag : 响应标识
     * code : 错误码
     * desc : 错误详情
     * count : 不用关注，预留
     * data : {"pptStatus":"done","aiImageStatus":"done","cardNoteStatus":"done","pptUrl":"ppt的url地址","errMsg":null,"totalPages":21,"donePages":21}
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
         * pptStatus : PPT构建状态：building（构建中），done（已完成），build_failed（生成失败）
         * aiImageStatus : ai配图状态：building（构建中），done（已完成）
         * cardNoteStatus : 演讲备注状态：building（构建中），done（已完成）
         * pptUrl : ppt的url地址
         * errMsg : 生成PPT的失败信息
         * totalPages : 生成PPT的总页数
         * donePages : 生成PPT的完成页数 （ai配图和演讲备注为异步任务，ppt页数完成，不代表配图和演讲备注也完成）
         */

        private String pptStatus;
        private String aiImageStatus;
        private String cardNoteStatus;
        private String pptUrl;
        private Object errMsg;
        private Integer totalPages;
        private Integer donePages;

        public String getPptStatus() {
            return pptStatus;
        }

        public void setPptStatus(String pptStatus) {
            this.pptStatus = pptStatus;
        }

        public String getAiImageStatus() {
            return aiImageStatus;
        }

        public void setAiImageStatus(String aiImageStatus) {
            this.aiImageStatus = aiImageStatus;
        }

        public String getCardNoteStatus() {
            return cardNoteStatus;
        }

        public void setCardNoteStatus(String cardNoteStatus) {
            this.cardNoteStatus = cardNoteStatus;
        }

        public String getPptUrl() {
            return pptUrl;
        }

        public void setPptUrl(String pptUrl) {
            this.pptUrl = pptUrl;
        }

        public Object getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(Object errMsg) {
            this.errMsg = errMsg;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }

        public Integer getDonePages() {
            return donePages;
        }

        public void setDonePages(Integer donePages) {
            this.donePages = donePages;
        }
    }
}
