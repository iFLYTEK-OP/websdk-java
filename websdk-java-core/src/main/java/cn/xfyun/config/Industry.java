package cn.xfyun.config;

/**
 * @program: websdk-java-demo
 * @description: 行业类型枚举类
 * @author: zyding6
 * @create: 2025/3/26 15:28
 **/
public enum Industry {

    TECHNOLOGY_INTERNET("科技互联网", "technologyInternet"),
    EDUCATION_TRAINING("教育培训", "educationTraining"),
    GOVERNMENT_AFFAIRS("政务", "governmentAffairs"),
    COLLEGE("学院", "college"),
    ECOMMERCE("电子商务", "ecommerce"),
    FINANCIAL_STRATEGY("金融战略", "financialStrategy"),
    LAW("法律", "law"),
    MEDICAL_HEALTH("医疗健康", "medicalHealth"),
    CULTURE_TOURISM_SPORTS("文旅体育", "cultureTourismSports"),
    ART_ADVERTISING("艺术广告", "artAdvertising"),
    HUMAN_RESOURCES("人力资源", "humanResources"),
    GAME_ENTERTAINMENT("游戏娱乐", "gameEntertainment");

    private final String name;
    private final String key;

    Industry(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
