package cn.xfyun.config;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/8 13:40
 */
public enum ImageWordEnum {

    /**
     *   营业执照识别
     */
    BUSINESS_LICENSE("sff4ea3cf", "bus_license", "sff4ea3cf_data_1"),

    /**
     *   出租车发票识别
     */
    TAXI_INVOICE("sb6db0171", "taxi_ticket", "sb6db0171_data_1"),

    /**
     *   火车票识别
     */
    TRAIN_TICKET("s19cfe728", "train_ticket", "s19cfe728_data_1"),

    /**
     *   增值税发票识别
     */
    INVOICE("s824758f1", "vat_invoice", "s824758f1_data_1"),

    /**
     *   身份证识别
     */
    IDCARD("s5ccecfce", null, "s5ccecfce_data_1"),

    /**
     *   多语种文字识别
     */
    PRINTED_WORD("s00b65163", "vat_invoice", "s00b65163_data_1");

    private String serviceId;

    private String templateList;

    private String payload;

    ImageWordEnum(String serviceId, String templateList, String payload){
        this.serviceId = serviceId;
        this.templateList = templateList;
        this.payload = payload;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getTemplateList() {
        return templateList;
    }

    public String getPayload() {
        return payload;
    }
}
