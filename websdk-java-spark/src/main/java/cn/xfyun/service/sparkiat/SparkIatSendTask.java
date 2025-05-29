package cn.xfyun.service.sparkiat;

import cn.xfyun.api.SparkIatClient;
import cn.xfyun.model.sparkiat.request.SparkIatRequest;
import cn.xfyun.service.common.AbstractTask;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

/**
 * 大模型听写通用发送任务
 *
 * @author <zyding6@ifytek.com>
 */
public class SparkIatSendTask extends AbstractTask {

    private static final Logger logger = LoggerFactory.getLogger(SparkIatSendTask.class);

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

    private SparkIatClient sparkIatClient = null;

    @Override
    public String businessDataProcess(byte[] contents, boolean isEnd, Integer seq) {

        if (isEnd) {
            // 文件读完，改变status 为 2
            status = STATUS_LAST_FRAME;
        }

        // 发送数据,请求数据均为json字符串
        SparkIatRequest request = new SparkIatRequest();
        // 请求头
        SparkIatRequest.Header header = new SparkIatRequest.Header(client().getAppId(), status);
        request.setHeader(header);
        switch (status) {
            // 第一帧音频status = 0
            case STATUS_FIRST_FRAME:
                // 请求参数
                SparkIatRequest.Parameter parameter = new SparkIatRequest.Parameter(client());
                request.setParameter(parameter);
                // 请求体
                SparkIatRequest.Payload firstPayload = new SparkIatRequest.Payload(client());
                firstPayload.getAudio().setAudio(Base64.getEncoder().encodeToString(contents));
                firstPayload.getAudio().setSeq(seq);
                firstPayload.getAudio().setStatus(STATUS_FIRST_FRAME);
                request.setPayload(firstPayload);
                // 发送完第一帧改变status 为 1
                status = STATUS_CONTINUED_FRAME;
                String first = StringUtils.gson.toJson(request);
                logger.debug("发送第一帧数据：{}", first);
                return first;

            // 中间帧status = 1
            case STATUS_CONTINUED_FRAME:
                // 请求体
                SparkIatRequest.Payload continuedPayload = new SparkIatRequest.Payload(client());
                continuedPayload.getAudio().setAudio(Base64.getEncoder().encodeToString(contents));
                continuedPayload.getAudio().setSeq(seq);
                continuedPayload.getAudio().setStatus(STATUS_CONTINUED_FRAME);
                request.setPayload(continuedPayload);
                String continued = StringUtils.gson.toJson(request);
                logger.debug("发送中间帧数据：{}", continued);
                return continued;

            // 最后一帧音频status = 2 ，标志音频发送结束
            case STATUS_LAST_FRAME:
                // 请求体
                SparkIatRequest.Payload lastPayload = new SparkIatRequest.Payload(client());
                lastPayload.getAudio().setAudio("");
                lastPayload.getAudio().setSeq(seq);
                lastPayload.getAudio().setStatus(STATUS_LAST_FRAME);
                request.setPayload(lastPayload);
                String last = StringUtils.gson.toJson(request);
                logger.debug("发送最后一帧数据：{}", last);
                return last;
            default:
                return null;
        }
    }

    private SparkIatClient client() {
        if (sparkIatClient == null) {
            return sparkIatClient = (SparkIatClient) client;
        }

        return sparkIatClient;
    }
}
