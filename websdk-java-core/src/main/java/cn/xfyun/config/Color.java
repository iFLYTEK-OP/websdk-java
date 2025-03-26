package cn.xfyun.config;

/**
 * @program: websdk-java-demo
 * @description: 颜色枚举类
 * @author: zyding6
 * @create: 2025/3/26 15:23
 **/
public enum Color {

    BLUE("蓝色", "blue"),
    GREEN("绿色", "green"),
    RED("红色", "red"),
    PURPLE("紫色", "purple"),
    BLACK("黑色", "black"),
    GRAY("灰色", "gray"),
    YELLOW("黄色", "yellow"),
    PINK("粉色", "pink"),
    ORANGE("橙色", "orange");

    private final String name;
    private final String key;

    // 构造函数
    Color(String name, String key) {
        this.name = name;
        this.key = key;
    }

    // 获取颜色名称的方法
    public String getName() {
        return name;
    }

    // 获取小写驼峰英文名称的方法
    public String getKey() {
        return key;
    }
}
