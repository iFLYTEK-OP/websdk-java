package cn.xfyun.config;

/**
 * @program: websdk-java
 * @description: 图片类型实体类
 * @author: zyding6
 * @create: 2025/3/25 10:04
 **/
public enum ImgFormat {

    JPG("jpg"),
    JPEG("jpeg"),
    BMP("bmp"),
    PNG("png");

    private final String desc;

    ImgFormat(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
