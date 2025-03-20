package cn.xfyun.service.sparkiat;

import cn.xfyun.api.SparkIatClient;
import cn.xfyun.config.SparkIatModelEnum;
import cn.xfyun.service.common.AbstractTimedTask;
import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.HashMap;

/**
 * @author <zyding6@iflytek.com>
 * @description 大模型听写通用发送任务
 * @date 2025/3/12
 */
public class SparkIatSendTask extends AbstractTimedTask {

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
            //文件读完，改变status 为 2
            status = STATUS_LAST_FRAME;
        }

        //发送数据,求数据均为json字符串
        HashMap<String, Object> request = new HashMap<>();
        request.put("header", new HashMap<String, Object>() {{
            //请求头必须带的参数
            put("app_id", client().getAppId());
            put("status", status);
        }});
        switch (status) {
            // 第一帧音频status = 0
            case STATUS_FIRST_FRAME:
                request.put("parameter", new HashMap<String, Object>() {{
                    put("iat", new HashMap<String, Object>() {{
                        put("domain", client().getDomain());
                        put("language", client().getLanguage());
                        put("accent", client().getAccent());
                        put("eos", client().getEos());
                        put("vinfo", client().getVinfo());
                        put("dwa", client().getDwa());

                        if (SparkIatModelEnum.ZH_CN_MULACC.codeEquals(client().getLangType())) {
                            put("nbest", client().getNbest());
                            put("wbest", client().getWbest());
                            put("ptt", client().getPtt());
                            put("smth", client().getSmth());
                            put("nunum", client().getNunum());
                            put("opt", client().getOpt());
                            put("dhw", client().getDhw());
                            put("rlang", client().getRlang());
                            put("ltc", client().getLtc());
                        }

                        put("result", new HashMap<String, Object>() {{
                            put("encoding", client().getTextEncoding());
                            put("compress", client().getTextCompress());
                            put("format", client().getTextFormat());
                        }});
                    }});
                }});
                request.put("payload", new HashMap<String, Object>() {{
                    put("audio", new HashMap<String, Object>() {{
                        put("encoding", client().getEncoding());
                        put("sample_rate", client().getSampleRate());
                        put("channels", client().getChannels());
                        put("bit_depth", client().getBitDepth());
                        put("seq", seq);
                        put("status", 0);
                        put("audio", Base64.getEncoder().encodeToString(contents));
                    }});
                }});

                // 发送完第一帧改变status 为 1
                status = STATUS_CONTINUED_FRAME;
                String json1 = StringUtils.toJson(request);
                logger.debug("发送第一帧数据：{}", json1);
                return json1;

            //中间帧status = 1
            case STATUS_CONTINUED_FRAME:
                request.put("payload", new HashMap<String, Object>() {{
                    put("audio", new HashMap<String, Object>() {{
                        put("encoding", client().getEncoding());
                        put("sample_rate", client().getSampleRate());
                        put("channels", client().getChannels());
                        put("bit_depth", client().getBitDepth());
                        put("seq", seq);
                        put("status", status);
                        put("audio", Base64.getEncoder().encodeToString(contents));
                    }});
                }});
                String json2 = StringUtils.toJson(request);
                logger.debug("发送中间帧数据：{}", json2);
                return json2;

            // 最后一帧音频status = 2 ，标志音频发送结束
            case STATUS_LAST_FRAME:
                request.put("payload", new HashMap<String, Object>() {{
                    put("audio", new HashMap<String, Object>() {{
                        put("encoding", client().getEncoding());
                        put("sample_rate", client().getSampleRate());
                        put("channels", client().getChannels());
                        put("bit_depth", client().getBitDepth());
                        put("seq", seq);
                        put("status", status);
                        put("audio", "");
                    }});
                }});
                String json3 = StringUtils.toJson(request);
                logger.debug("发送最后一帧数据：{}", json3);
                return json3;
            default:
                return null;
        }
    }

    private SparkIatClient client() {
        if (sparkIatClient == null) {
            return sparkIatClient = (SparkIatClient) webSocketClient;
        }

        return sparkIatClient;
    }
}
