package cn.xfyun.base.http.platform;


import cn.xfyun.base.http.HttpRequestClient;
import com.google.gson.JsonObject;

/**
 *    自研能力使用
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/2 15:04
 */
public abstract class PlatformHttpClient extends HttpRequestClient {

    /**
     * 服务引擎ID
     */
    protected String serviceId;

    /**
     *   请求状态，取值范围为：3（一次传完）
     */
    protected int status;

    /**
     *    文本编码，可选值：utf8（默认值
     */
    protected String encoding;

    /**
     *   文本压缩格式，可选值：raw（默认值）
     */
    protected String compress;

    /**
     *    文本格式，可选值：json（默认值）
     */
    protected String format;


    public PlatformHttpClient(PlatformBuilder builder) {
        super(builder);
        this.serviceId = builder.getServiceId();
        this.status = builder.getStatus();
        this.encoding = builder.getEncoding();
        this.compress = builder.getCompress();
        this.format = builder.getFormat();
    }


    public String getServiceId() {
        return serviceId;
    }

    public int getStatus() {
        return status;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getCompress() {
        return compress;
    }

    public String getFormat() {
        return format;
    }

    protected JsonObject buildHeader() {
        /** header **/
        JsonObject header = new JsonObject();
        header.addProperty("app_id", appId);
        header.addProperty("status", status);
        return header;
    }

    protected JsonObject buildResult() {
        JsonObject faceCompareResult = new JsonObject();
        faceCompareResult.addProperty("encoding", encoding);
        faceCompareResult.addProperty("format", format);
        faceCompareResult.addProperty("compress", compress);
        return faceCompareResult;
    }


}
