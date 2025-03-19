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
public class PullResultTaskV2 extends AbstractTaskV2 {

    private static final Logger logger = LoggerFactory.getLogger(PullResultTaskV2.class);

    private static final String serverUrl = "https://raasr.xfyun.cn/v2/api/getResult";

    private final String orderId;

    private final String resultType;

    public PullResultTaskV2(LfasrSignature signature, String orderId, String resultType) throws SignatureException {
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
            response = resolveMessage(this.connector.post(serverUrl, this.param));
        } catch (Exception e) {
            logger.warn(getIntro() + " 处理失败", e);
            response = LfasrResponse.error("转写结果查询失败");
        }
        return response;
    }

    @Override
    public String getIntro() {
        return "pull result orderId: " + this.orderId + ", resultType: " + this.resultType;
    }
}