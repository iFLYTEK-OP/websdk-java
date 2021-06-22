package cn.xfyun.model.response.trans;

import java.util.Map;

/**
 * @author <ydwang16@iflytek.com>
 * @description 翻译结果
 * @date 2021/6/15
 */
public class TransData {
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result{
        /**
         * 源语种，如果请求设置auto则自动返回识别到的源语种参数
         */
        private String from;

        /**
         * 目标语种
         */
        private String to;

        /**
         * 翻译结果
         * trans_result.src	string	源文本
         * trans_result.dst	string	目标文本
         */
        private Map<String,String> trans_result;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public Map<String,String> getTrans_result() {
            return trans_result;
        }

        public void setTrans_result(Map<String,String> trans_result) {
            this.trans_result = trans_result;
        }
    }
}


