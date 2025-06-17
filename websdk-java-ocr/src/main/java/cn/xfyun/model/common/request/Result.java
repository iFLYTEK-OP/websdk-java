package cn.xfyun.model.common.request;

/**
 * @program: websdk-java
 * @description: 通用返回结果实体类
 * @author: zyding6
 * @create: 2025/3/25 9:40
 **/
public class Result {

    private String encoding;
    private String compress;
    private String format;
    
    public Result() {
    }
    
    public Result(String encoding, String compress, String format) {
        this.encoding = encoding;
        this.compress = compress;
        this.format = format;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getCompress() {
        return compress;
    }

    public void setCompress(String compress) {
        this.compress = compress;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
