package cn.xfyun.service.ise;

import cn.xfyun.service.common.AbstractTask;
import com.google.gson.JsonObject;
import cn.xfyun.api.IseClient;
import cn.xfyun.common.IseConstant;

import cn.xfyun.util.StringUtils;

import java.util.Base64;

/**
 * @Author: <flhong2@iflytek.com>
 * @description: 语音评测发送任务
 * @version:v1.0
 * @create: 2021-04-06 09:57
 **/
public class IseSendTask extends AbstractTask {
    private static final int STATUS_PREPARE_FRAME = -1;
    private static final int STATUS_FIRST_FRAME = 0;
    private static final int STATUS_CONTINUED_FRAME = 1;
    private static final int STATUS_LAST_FRAME = 2;
    private int status = -1;

    private IseClient iseClient = null;

    /**
     * 编码
     */
    final static Base64.Encoder ENCODER = Base64.getEncoder();

    public IseSendTask(AbstractBuilder builder) {
        super(builder);
    }

    @Override
    public String businessDataProcess(byte[] contents, boolean isEnd) throws InterruptedException {
        if (isEnd) {
            //文件读完，改变status 为 2
            status = STATUS_LAST_FRAME;
        }
        IseBusiness business = new IseBusiness(this.getIseClient());
        switch (status) {
            // 参数第一次上传status = -1
            case STATUS_PREPARE_FRAME:

                IseRequest prepareFrame = new IseRequest();

                JsonObject common = new JsonObject();

                // 填充common,只有第一帧需要
                common.addProperty("app_id", this.getIseClient().getAppId());

                //填充business,第一帧必须发送
                business.setCmd("ssb");
                business.setAus(null);
                //填充data,每一帧都要发送
                IseRequestData prepareData = new IseRequestData();
                prepareData.setStatus(STATUS_FIRST_FRAME);
                prepareData.setData(null);
                //填充frame
                prepareFrame.setCommon(common);
                prepareFrame.setBusiness(business);
                prepareFrame.setData(prepareData);

                this.getIseClient().getWebSocket().send(StringUtils.gson.toJson(prepareFrame));

                Thread.sleep(IseConstant.ISE_FRAME_INTERVEL);

                //发送完第一帧改变status 为 1
                status = STATUS_CONTINUED_FRAME;
                prepareFrame.setCommon(null);
                prepareData.setStatus(status);
                prepareData.setData(ENCODER.encodeToString(contents));
                business.setCmd("auw");
                business.setAus(1);
                prepareFrame.setBusiness(business);

                return StringUtils.gson.toJson(prepareFrame);

            case STATUS_CONTINUED_FRAME:
                IseRequest continueFrame = new IseRequest();
                IseBusiness continueBusiness = new IseBusiness();
                if (!business.isTtp_skip()){
                    continueBusiness.setCmd("ttp");
                }else {
                    continueBusiness.setCmd("auw");
                }
                continueBusiness.setAus(2);
                continueFrame.setBusiness(continueBusiness);

                IseRequestData continueDate = new IseRequestData();
                continueDate.setStatus(STATUS_CONTINUED_FRAME);
                continueDate.setData(ENCODER.encodeToString(contents));

                continueFrame.setData(continueDate);
                return StringUtils.gson.toJson(continueFrame);

                //最后一帧音频status = 2,志音频发送结束
            case STATUS_LAST_FRAME:
                IseRequest lastFrame = new IseRequest();
                IseBusiness lastBusiness = new IseBusiness();
                if (!business.isTtp_skip()){
                    lastBusiness.setCmd("ttp");
                }else {
                    lastBusiness.setCmd("auw");
                }
                lastBusiness.setAus(4);
                lastFrame.setBusiness(lastBusiness);

                IseRequestData lastData = new IseRequestData();
                lastData.setStatus(STATUS_LAST_FRAME);
                lastData.setData("");

                lastFrame.setData(lastData);
                return StringUtils.gson.toJson(lastFrame);

            default:
                return null;
        }
    }

    private IseClient getIseClient() {
        if (iseClient == null){
            return iseClient = (IseClient) webSocketClient;
        }

        return iseClient;
    }
}
