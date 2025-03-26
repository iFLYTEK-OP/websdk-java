package cn.xfyun.config;

/**
 * @program: websdk-java-demo
 * @description: 风格枚举类
 * @author: zyding6
 * @create: 2025/3/26 15:28
 **/
public enum Style {

    SIMPLE("简约", "simple"),
    CARTOON("卡通", "cartoon"),
    BUSINESS("商务", "business"),
    CREATIVE("创意", "creative"),
    CHINESE_STYLE("国风", "chinese_style"),
    REFRESHING("清新", "refreshing"),
    FLAT("扁平", "flat"),
    ILLUSTRATION("插画", "illustration"),
    FESTIVAL("节日", "festival");

    private final String name;
    private final String key;

    // 构造函数，初始化风格名称和小写英文key
    private Style(String name, String key) {
        this.name = name;
        this.key = key;
    }

    // 获取风格名称的方法
    public String getName() {
        return name;
    }

    // 获取小写英文key的方法
    public String getKey() {
        return key;
    }
}
