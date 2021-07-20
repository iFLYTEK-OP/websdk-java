package cn.xfyun.service.iat;

import cn.xfyun.model.request.iat.IatBusiness;
import cn.xfyun.model.request.iat.IatRequest;
import cn.xfyun.model.request.iat.IatRequestData;
import cn.xfyun.service.common.AbstractTimedTask;
import com.google.gson.JsonObject;
import cn.xfyun.api.IatClient;
import cn.xfyun.util.StringUtils;

import java.util.Base64;

/**
 * @author <ydwang16@iflytek.com>
 * @description 语音听写发送任务
 * @date 2021/3/27
 */
public class IatSendTask extends AbstractTimedTask {

    /**
     * 发送状态，0代表第一帧，1代表中间数据，2代表最后一帧
     */
    private static final int STATUS_FIRST_FRAME = 0;
    private static final int STATUS_CONTINUED_FRAME = 1;
    private static final int STATUS_LAST_FRAME = 2;

    /**
     * 标识当前数据发送状态
     */
    private int status = 0;

    private IatClient iatClient = null;

    @Override
    public String businessDataProcess(byte[] contents, boolean isEnd) {

        if (isEnd) {
            //文件读完，改变status 为 2
            status = STATUS_LAST_FRAME;
        }

        switch (status) {
            // 第一帧音频status = 0
            case STATUS_FIRST_FRAME:
                //第一帧必须发送的参数
                IatRequest frame = new IatRequest();
                JsonObject common = new JsonObject();

                // 填充common，第一帧必须发送
                common.addProperty("app_id", this.getIatClient().getAppId());

                //填充business，第一帧必须发送
                IatBusiness business = new IatBusiness(this.getIatClient());

                //填充data，每一帧都要发送
                IatRequestData data = new IatRequestData(this.getIatClient());
                data.setStatus(STATUS_FIRST_FRAME);
                data.setAudio(Base64.getEncoder().encodeToString(contents));

                //填充frame
                frame.setCommon(common);
                frame.setBusiness(business);
                frame.setData(data);

                // 发送完第一帧改变status 为 1
                status = STATUS_CONTINUED_FRAME;
                return StringUtils.gson.toJson(frame);

            //中间帧status = 1
            case STATUS_CONTINUED_FRAME:
                IatRequest continueFrame = new IatRequest();
                IatRequestData continueDate = new IatRequestData(this.getIatClient());
                continueDate.setStatus(STATUS_CONTINUED_FRAME);
                continueDate.setAudio(Base64.getEncoder().encodeToString(contents));

                continueFrame.setData(continueDate);
                return StringUtils.gson.toJson(continueFrame);

            // 最后一帧音频status = 2 ，标志音频发送结束
            case STATUS_LAST_FRAME:
                IatRequest lastFrame = new IatRequest();
                IatRequestData lastData = new IatRequestData(this.getIatClient());
                lastData.setStatus(STATUS_LAST_FRAME);
                lastData.setAudio("");

                lastFrame.setData(lastData);
                return StringUtils.gson.toJson(lastFrame);

            default:
                return null;
        }
    }

    private IatClient getIatClient() {
        if (iatClient == null) {
            return iatClient = (IatClient) webSocketClient;
        }

        return iatClient;
    }
}
