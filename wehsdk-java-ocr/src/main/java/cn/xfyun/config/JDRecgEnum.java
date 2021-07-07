package cn.xfyun.config;


/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 16:15
 */
public enum JDRecgEnum {

    /**
     *   行驶证识别
     */
    JD_OCR_VEHICLE("jd_ocr_vehicle", "vehicleLicenseRes", "jd_ocr_vehicle", "vehicleLicense"),

    /**
     *   驾驶证识别
     */
    JD_OCR_DRIVER("jd_ocr_driver", "driverLicenseRes", "jd_ocr_driver", "driverLicense"),

    /**
     *   车牌识别
     */
    JD_OCR_CAR("jd_ocr_car", " carLicenseRes", "jd_ocr_car", "carImgBase64Str");

    private String value;
    private String service;
    private String parameter;
    private String payload;

    JDRecgEnum(String value, String service, String parameter, String payload){
        this.value = value;
        this.service = service;
        this.parameter = parameter;
        this.payload = payload;
    }


    public String getValue() {
        return value;
    }

    public String getService() {
        return service;
    }

    public String getParameter() {
        return parameter;
    }

    public String getPayload() {
        return payload;
    }
}
