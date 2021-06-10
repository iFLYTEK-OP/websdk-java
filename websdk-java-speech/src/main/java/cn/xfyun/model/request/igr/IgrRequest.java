package cn.xfyun.model.request.igr;

import com.google.gson.JsonObject;

/**
 * @author: <flhong2@iflytek.com>
 * @description: 性别年龄识别请求体
 * @version: v1.0
 * @create: 2021-06-02 14:20
 **/
public class IgrRequest {
    /**
     * 公共参数，仅在握手成功后首帧请求时上传
     * 目前仅有app_id
     */
    private JsonObject common;

    /**
     * 业务，仅在握手成功后首帧请求时上传
     * ent  引擎类型，目前仅支持igr
     * aue  音频格式
     * rate 音频采样率 16000/8000
     */
    private JsonObject business;

    /**
     * 业务数据流参数，在握手成功后的所有请求中都需要上传
     * status   音频的状态 0 :第一帧音频 1 :中间的音频 2 :最后一帧音频，最后一帧必须要发送
     * audio    音频的数据，需进行base64编码
     */
    private JsonObject data;

    public void setCommon(JsonObject common) {
        this.common = common;
    }

    public void setBusiness(JsonObject business) {
        this.business = business;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }
}
