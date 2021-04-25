package cn.xfyun.model.request.iat;

import com.google.gson.JsonObject;

/**
 * @author <ydwang16@iflytek.com>
 * @description 语音听写请求
 * @date 2021/3/24
 */
public class IatRequest {
    /**
     * 公共参数，仅在握手成功后首帧请求时上传
     * 目前仅有app_id
     */
    private JsonObject common;

    /**
     * 业务参数，仅在握手成功后首帧请求时上传
     */
    private IatBusiness business;

    /**
     * 业务数据流参数，在握手成功后的所有请求中都需要上传，
     */
    private IatRequestData data;

    public JsonObject getCommon() {
        return common;
    }

    public void setCommon(JsonObject common) {
        this.common = common;
    }

    public IatBusiness getBusiness() {
        return business;
    }

    public void setBusiness(IatBusiness business) {
        this.business = business;
    }

    public IatRequestData getData() {
        return data;
    }

    public void setData(IatRequestData data) {
        this.data = data;
    }
}
