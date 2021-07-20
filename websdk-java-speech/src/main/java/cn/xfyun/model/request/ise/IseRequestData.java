package cn.xfyun.model.request.ise;

/**
 * @author: <flhong2@iflytek.com>
 * @description:
 * @version: v1.0
 * @create: 2021-04-06 10:12
 **/
public class IseRequestData {
    /**
     * 音频的状态
     * 0 :第一帧音频
     * 1 :中间的音频
     * 2 :最后一帧音频，最后一帧必须要发送
     */
    private Integer status;

    /**
     * 音频内容，采用base64编码
     */
    private String data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
