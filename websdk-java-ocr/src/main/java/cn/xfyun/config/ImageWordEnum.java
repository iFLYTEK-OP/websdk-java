package cn.xfyun.config;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/8 13:40
 */
public enum ImageWordEnum {

    /**
     * 营业执照识别
     */
    BUSINESS_LICENSE("营业执照识别", "sff4ea3cf", "bus_license", "sff4ea3cf_data_1"),

    /**
     * 出租车发票识别
     */
    TAXI_INVOICE("出租车发票识别", "sb6db0171", "taxi_ticket", "sb6db0171_data_1"),

    /**
     * 火车票识别
     */
    TRAIN_TICKET("火车票识别", "s19cfe728", "train_ticket", "s19cfe728_data_1"),

    /**
     * 增值税发票识别
     */
    INVOICE("增值税发票识别", "s824758f1", "vat_invoice", "s824758f1_data_1"),

    /**
     * 身份证识别
     */
    IDCARD("身份证识别", "s5ccecfce", "id_card", "s5ccecfce_data_1"),

    /**
     * 多语种文字识别
     */
    PRINTED_WORD("多语种文字识别", "s00b65163", "vat_invoice", "s00b65163_data_1"),

    /**
     * 通用文字识别
     */
    COMMON_WORD("通用文字识别", "sf8e6aca1", "vat_invoice", "sf8e6aca1_data_1");

    private final String name;

    private final String serviceId;

    private final String templateList;

    private final String payload;

    ImageWordEnum(String name, String serviceId, String templateList, String payload) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    public static ImageWordEnum getEnumByServiceId(String serviceId) {
        for (ImageWordEnum e : ImageWordEnum.values()) {
            if (e.serviceId.equals(serviceId)) {
                return e;
            }
        }
        return null;
    }
}
