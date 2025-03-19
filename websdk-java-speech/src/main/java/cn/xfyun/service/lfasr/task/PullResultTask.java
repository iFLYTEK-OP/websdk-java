package cn.xfyun.service.lfasr.task;

import cn.xfyun.model.response.lfasr.LfasrResponse;
import cn.xfyun.model.sign.LfasrSignature;
import cn.xfyun.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SignatureException;

/**
 * getResult接口封装
 *
 * @author : iflytek
 * @date : 2021年03月15日
 */
public class PullResultTask extends AbstractTask {

    private static final Logger logger = LoggerFactory.getLogger(PullResultTask.class);

    private static final String serverUrl = "https://raasr.xfyun.cn/v2/api/getResult";

    private final String orderId;

    private final String resultType;

    public PullResultTask(LfasrSignature signature, String orderId, String resultType) throws SignatureException {
        super(signature);
        this.param.put("orderId", orderId);
        if (!StringUtils.isNullOrEmpty(resultType)) {
            this.param.put("resultType", resultType);
        }
        this.orderId = orderId;
        this.resultType = resultType;
    }

    @Override
    public LfasrResponse call() {
        LfasrResponse response;
        try {
            response = resolveResponse(this.connector.post(serverUrl, this.param));
        } catch (Exception e) {
            logger.warn("转写结果查询失败，详细信息：【{}】", getIntro(), e);
            response = LfasrResponse.error("转写结果查询失败");
        }
        return response;
    }

    @Override
    public String getIntro() {
        return "查询转写结果接口地址：" + serverUrl + "，订单ID: " + this.orderId + ", 结果类型: " + this.resultType;
    }
}