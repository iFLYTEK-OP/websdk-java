package cn.xfyun.config;


/**
 * 词条敏感分类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum CategoryEnum {

    PORNO_DETECTION("pornDetection", "色情"),
    VIOLENT_TERRORISM("violentTerrorism", "暴恐"),
    POLITICAL("political", "涉政"),
    LOW_QUALITY_IRRIGATION("lowQualityIrrigation", "低质量灌水"),
    CONTRABAND("contraband", "违禁"),
    ADVERTISEMENT("advertisement", "广告"),
    UNCIVILIZED_LANGUAGE("uncivilizedLanguage", "不文明用语");

    private final String code;
    private final String description;

    CategoryEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static CategoryEnum fromCode(String code) {
        for (CategoryEnum category : CategoryEnum.values()) {
            if (category.getCode().equalsIgnoreCase(code)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown content category code: " + code);
    }
}
