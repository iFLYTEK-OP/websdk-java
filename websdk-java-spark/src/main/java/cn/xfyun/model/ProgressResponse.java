package cn.xfyun.model;

/**
 * PPT的进度查询结果
 * @author junzhang27
 */
public class ProgressResponse {
    private int code;
    private String desc;
    private Data data;

    public static class Data {
        /**
         * 生成进度：30-大纲生成完毕、70-PPT生成完毕、100-PPT导出完毕
         */
        private int process;
        /**
         * PPT的 ID
         */
        private String pptId;
        /**
         * PPT的下载链接
         */
        private String pptUrl;

        public int getProcess() {
            return process;
        }

        public void setProcess(int process) {
            this.process = process;
        }

        public String getPptId() {
            return pptId;
        }

        public void setPptId(String pptId) {
            this.pptId = pptId;
        }

        public String getPptUrl() {
            return pptUrl;
        }

        public void setPptUrl(String pptUrl) {
            this.pptUrl = pptUrl;
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

