package cn.xfyun.base.http.platform;


import cn.xfyun.base.http.HttpRequestBuilder;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/2 15:48
 */
public abstract class PlatformBuilder<T>  extends HttpRequestBuilder<T>  {

    private String serviceId;

    private int status = 3;

    private String encoding = "utf8";

    private String compress = "raw";

    private String format = "json";


    public String getServiceId() {
        return serviceId;
    }

    public T serviceId(String serviceId) {
        this.serviceId = serviceId;
        return (T)this;
    }

    public int getStatus() {
        return status;
    }

    public T status(int status) {
        this.status = status;
        return (T)this;
    }

    public String getEncoding() {
        return encoding;
    }

    public T encoding(String encoding) {
        this.encoding = encoding;
        return (T)this;
    }

    public String getCompress() {
        return compress;
    }

    public T compress(String compress) {
        this.compress = compress;
        return (T)this;
    }

    public String getFormat() {
        return format;
    }

    public T format(String format) {
        this.format = format;
        return (T)this;
    }

    public PlatformBuilder(String hostUrl, String serviceId, String appId, String apiKey, String apiSecret) {
        super(hostUrl, appId, apiKey, apiSecret);
        this.serviceId = serviceId;
    }
}
