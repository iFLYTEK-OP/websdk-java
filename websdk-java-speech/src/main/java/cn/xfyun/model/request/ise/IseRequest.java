package cn.xfyun.model.request.ise;

import com.google.gson.JsonObject;

/**
 * @author: <flhong2@iflytek.com>
 * @description: 语音评测请求
 * @version: v1.0
 * @create: 2021-04-06 10:07
 **/
public class IseRequest {
    /**
     * 公共参数，仅在握手成功后首帧请求时上传
     * 目前仅有app_id
     */
    private JsonObject common;

    /**
     * 业务参数，仅在握手成功后首帧请求时上传
     */
    private IseBusiness business;

    /**
     * 业务数据流参数，在握手成功后的所有请求中都需要上传，
     */
    private IseRequestData data;

    public JsonObject getCommon() {
        return common;
    }

    public void setCommon(JsonObject common) {
        this.common = common;
    }

    public IseBusiness getBusiness() {
        return business;
    }

    public void setBusiness(IseBusiness business) {
        this.business = business;
    }

    public IseRequestData getData() {
        return data;
    }

    public void setData(IseRequestData data) {
        this.data = data;
    }
}
