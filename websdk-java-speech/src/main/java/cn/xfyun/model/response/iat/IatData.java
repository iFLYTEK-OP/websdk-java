package cn.xfyun.model.response.iat;

/**
 * @author <ydwang16@iflytek.com>
 * @description 语音听写响应结果
 * @date 2021/3/24
 */
public class IatData {

    /**
     * 音频的状态
     * 0 :第一帧音频
     * 1 :中间的音频
     * 2 :最后一帧音频，最后一帧必须要发送
     */
    private int status;

    /**
     * 听写结果
     */
    private IatResult result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public IatResult getResult() {
        return result;
    }

    public void setResult(IatResult result) {
        this.result = result;
    }
}
