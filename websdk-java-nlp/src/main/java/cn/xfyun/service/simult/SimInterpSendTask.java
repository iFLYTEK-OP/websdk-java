package cn.xfyun.service.simult;

import cn.xfyun.api.SimInterpClient;
import cn.xfyun.model.simult.request.SimInterpRequest;
import cn.xfyun.service.common.AbstractNlpTask;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

/**
 * 同声传译发送任务类
 *
 * @author <zyding6@ifytek.com>
 */
public class SimInterpSendTask extends AbstractNlpTask {

    private static final Logger logger = LoggerFactory.getLogger(SimInterpSendTask.class);
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
    private SimInterpClient simInterpClient = null;

    @Override
    public String businessDataProcess(byte[] contents, boolean isEnd, Integer seq) {

        if (isEnd) {
            // 文件读完，改变status 为 2
            status = STATUS_LAST_FRAME;
        }

        // 发送数据,求数据均为json字符串
        SimInterpRequest request = new SimInterpRequest();
        // 请求头
        SimInterpRequest.Header header = new SimInterpRequest.Header(client().getAppId(), status);
        request.setHeader(header);
        // 请求体
        SimInterpRequest.Payload payload = new SimInterpRequest.Payload();
        SimInterpRequest.Payload.Data data = new SimInterpRequest.Payload.Data();
        data.setSampleRate(client().getSampleRate());
        data.setEncoding(client().getEncoding());
        data.setSeq(seq);
        data.setStatus(STATUS_FIRST_FRAME);
        if (contents == null || contents.length == 0) {
            data.setAudio("");
        } else {
            data.setAudio(Base64.getEncoder().encodeToString(contents));
        }
        payload.setData(data);
        request.setPayload(payload);
        switch (status) {
            // 第一帧音频status = 0
            case STATUS_FIRST_FRAME:
                // 请求参数
                SimInterpRequest.Parameter parameter = new SimInterpRequest.Parameter(client());
                request.setParameter(parameter);
                // 发送完第一帧改变status 为 1
                status = STATUS_CONTINUED_FRAME;
                String first = StringUtils.gson.toJson(request);
                logger.debug("发送第一帧数据：{}", first);
                return first;

            // 中间帧status = 1
            case STATUS_CONTINUED_FRAME:
                String continued = StringUtils.gson.toJson(request);
                logger.debug("发送中间帧数据：{}", continued);
                return continued;

            // 最后一帧音频status = 2 ，标志音频发送结束
            case STATUS_LAST_FRAME:
                String last = StringUtils.gson.toJson(request);
                logger.debug("发送最后一帧数据：{}", last);
                return last;
            default:
                return null;
        }
    }

    private SimInterpClient client() {
        if (simInterpClient == null) {
            return simInterpClient = (SimInterpClient) client;
        }
        return simInterpClient;
    }
}
