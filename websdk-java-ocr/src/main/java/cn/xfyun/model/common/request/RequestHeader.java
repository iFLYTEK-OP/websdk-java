package cn.xfyun.model.common.request;

import com.google.gson.annotations.SerializedName;

/**
 * @program: websdk-java
 * @description: 通用请求头
 * @author: zyding6
 * @create: 2025/3/25 9:46
 **/
public class RequestHeader {

    @SerializedName("app_id")
    private String appId;
    private Integer status;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
