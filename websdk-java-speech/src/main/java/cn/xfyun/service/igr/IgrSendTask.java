package cn.xfyun.service.igr;

import cn.xfyun.api.IgrClient;
import cn.xfyun.common.IgrConstant;
import cn.xfyun.model.request.igr.IgrRequest;
import cn.xfyun.service.common.AbstractTimedTask;
import cn.xfyun.util.StringUtils;
import com.google.common.base.Strings;
import com.google.gson.JsonObject;

import java.util.Base64;
import java.util.Objects;

/**
 * @author: <flhong2@iflytek.com>
 * @description: 性别年龄识别发送任务
 * @version: v1.0
 * @create: 2021-06-02 11:10
 **/
public class IgrSendTask extends AbstractTimedTask {

    private static final int STATUS_FIRST_FRAME = 0;
    private static final int STATUS_CONTINUED_FRAME = 1;
    private static final int STATUS_LAST_FRAME = 2;
    private int status = 0;
    private IgrClient igrClient = null;

    /**
     * 编码
     */
    final static Base64.Encoder ENCODER = Base64.getEncoder();

    @Override
    public AbstractTimedTask build(Builder builder) {
        return super.build(builder);
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    public String businessDataProcess(byte[] contents, boolean isEnd) throws InterruptedException {
        if (isEnd) {
            //文件读完，改变status 为 2
            status = STATUS_LAST_FRAME;
        }
        igrClient = this.getIgrClient();
        JsonObject data = new JsonObject();
        switch (status) {

            case STATUS_FIRST_FRAME:

                IgrRequest firstFrame = new IgrRequest();

                // 填充common,只有第一帧需要
                JsonObject common = new JsonObject();
                common.addProperty("app_id", igrClient.getAppId());

                //填充business,第一帧必须发送
                JsonObject business = new JsonObject();
                business.addProperty("ent", igrClient.getEnt());
                business.addProperty("aue", igrClient.getAue());
                business.addProperty("rate", igrClient.getRate());

                data.addProperty("status", STATUS_FIRST_FRAME);
                data.addProperty("audio", ENCODER.encodeToString(contents));

                //填充frame
                firstFrame.setCommon(common);
                firstFrame.setBusiness(business);
                firstFrame.setData(data);

                //发送完第一帧,改变status 为 1
                status = STATUS_CONTINUED_FRAME;

                return StringUtils.gson.toJson(firstFrame);

            case STATUS_CONTINUED_FRAME:

                IgrRequest continueFrame = new IgrRequest();
                data.addProperty("status", STATUS_CONTINUED_FRAME);
                data.addProperty("audio", ENCODER.encodeToString(contents));
                continueFrame.setData(data);
                return StringUtils.gson.toJson(continueFrame);

            //最后一帧音频status = 2,志音频发送结束
            case STATUS_LAST_FRAME:
                IgrRequest lastFrame = new IgrRequest();
                data.addProperty("status", STATUS_LAST_FRAME);

                if (Objects.isNull(contents)){
                    data.addProperty("audio", "");
                }else {
                    data.addProperty("audio", ENCODER.encodeToString(contents));
                }
                lastFrame.setData(data);
                return StringUtils.gson.toJson(lastFrame);

            default:
                return null;
        }
    }

    private IgrClient getIgrClient() {
        if (igrClient == null){
            return igrClient = (IgrClient) webSocketClient;
        }

        return igrClient;
    }
}
