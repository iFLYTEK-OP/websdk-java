package cn.xfyun.model.response.lfasr;

import java.io.Serializable;


public class LfasrResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码，"000000"表示成功
     */
    private String code;

    /**
     * 响应描述信息
     */
    private String descInfo;

    /**
     * 响应内容
     */
    private Content content;

    public LfasrResponse(String code, String descInfo, Content content) {
        this.code = code;
        this.descInfo = descInfo;
        this.content = content;
    }

    public static LfasrResponse error(String errorInfo) {
        return new LfasrResponse("-1", errorInfo, null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescInfo() {
        return descInfo;
    }

    public void setDescInfo(String descInfo) {
        this.descInfo = descInfo;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    /**
     * 响应内容类
     */
    public static class Content implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 订单ID（上传音频接口不返回OrderInfo，只返回单独一个ID）
         */
        private String orderId;

        /**
         * 订单信息
         */
        private OrderInfo orderInfo;

        /**
         * 转写结果（JSON字符串）
         */
        private String orderResult;

        /**
         * 翻译结果（JSON字符串）
         */
        private String transResult;

        /**
         * 质检结果（JSON字符串）
         */
        private String predictResult;

        /**
         * 任务预估时间
         */
        private Integer taskEstimateTime;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public OrderInfo getOrderInfo() {
            return orderInfo;
        }

        public void setOrderInfo(OrderInfo orderInfo) {
            this.orderInfo = orderInfo;
        }

        public String getOrderResult() {
            return orderResult;
        }

        public void setOrderResult(String orderResult) {
            this.orderResult = orderResult;
        }

        public String getTransResult() {
            return transResult;
        }

        public void setTransResult(String transResult) {
            this.transResult = transResult;
        }

        public String getPredictResult() {
            return predictResult;
        }

        public void setPredictResult(String predictResult) {
            this.predictResult = predictResult;
        }

        public Integer getTaskEstimateTime() {
            return taskEstimateTime;
        }

        public void setTaskEstimateTime(Integer taskEstimateTime) {
            this.taskEstimateTime = taskEstimateTime;
        }

    }

    /**
     * 订单信息类
     */
    public static class OrderInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 订单ID
         */
        private String orderId;

        /**
         * 失败类型，0表示成功
         */
        private Integer failType;

        /**
         * 订单状态
         * 0: 创建中
         * 1: 等待中
         * 2: 处理中
         * 3: 处理完成
         * 4: 转写结果上传完成
         * -1: 转写失败
         */
        private Integer status;

        /**
         * 原始音频时长（毫秒）
         */
        private Integer originalDuration;

        /**
         * 实际音频时长（毫秒）
         */
        private Integer realDuration;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Integer getFailType() {
            return failType;
        }

        public void setFailType(Integer failType) {
            this.failType = failType;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getOriginalDuration() {
            return originalDuration;
        }

        public void setOriginalDuration(Integer originalDuration) {
            this.originalDuration = originalDuration;
        }

        public Integer getRealDuration() {
            return realDuration;
        }

        public void setRealDuration(Integer realDuration) {
            this.realDuration = realDuration;
        }

    }

    @Override
    public String toString() {
        return "LfasrResponse{" +
                "code='" + code + '\'' +
                ", descInfo='" + descInfo + '\'' +
                ", content=" + content +
                '}';
    }

}