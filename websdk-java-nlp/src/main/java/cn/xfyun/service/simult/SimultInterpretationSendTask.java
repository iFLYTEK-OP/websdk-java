package cn.xfyun.service.simult;

import cn.xfyun.api.SimultInterpretationClient;
import cn.xfyun.model.simult.request.SimultInterpretationRequest;
import cn.xfyun.service.common.AbstractNlpTask;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Base64;

/**
 * @program: websdk-java
 * @description: 同声传译发送任务类
 * @author: zyding6
 * @create: 2025/3/23 22:09
 **/
public class SimultInterpretationSendTask extends AbstractNlpTask {

    private static final Logger logger = LoggerFactory.getLogger(SimultInterpretationSendTask.class);

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

    private SimultInterpretationClient simultInterpretationClient = null;

    @Override
    public String businessDataProcess(byte[] contents, boolean isEnd, Integer seq) {

        if (isEnd) {
            // 文件读完，改变status 为 2
            status = STATUS_LAST_FRAME;
        }

        // 发送数据,求数据均为json字符串
        SimultInterpretationRequest request = new SimultInterpretationRequest();
        // 请求头
        SimultInterpretationRequest.Header header = new SimultInterpretationRequest.Header(client().getAppId(), status);
        request.setHeader(header);
        // 请求体
        SimultInterpretationRequest.Payload payload = new SimultInterpretationRequest.Payload();
        SimultInterpretationRequest.Payload.Data data = new SimultInterpretationRequest.Payload.Data();
        data.setSampleRate(SimultInterpretationClient.INPUT_AUDIO_SAMPLERATE);
        data.setEncoding(SimultInterpretationClient.INPUT_AUDIO_ENCODING);
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
                SimultInterpretationRequest.Parameter parameter = new SimultInterpretationRequest.Parameter(client());
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

    private SimultInterpretationClient client() {
        if (simultInterpretationClient == null) {
            return simultInterpretationClient = (SimultInterpretationClient) webSocketClient;
        }

        return simultInterpretationClient;
    }
}
