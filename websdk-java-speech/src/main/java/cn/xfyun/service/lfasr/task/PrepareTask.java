package cn.xfyun.service.lfasr.task;

import cn.xfyun.config.LfasrTaskStatusEnum;
import cn.xfyun.exception.HttpException;
import cn.xfyun.model.response.lfasr.LfasrMessage;
import cn.xfyun.model.sign.LfasrSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Map;

/**
 * prepare接口封装
 *
 * @author : iflytek
 * @date : 2021年03月15日
 */
public class PrepareTask extends AbstractTask {
    private static final Logger logger = LoggerFactory.getLogger(PrepareTask.class);

    private final String fileName;

    public PrepareTask(LfasrSignature signature, Map<String, String> param) throws SignatureException {
        super(signature);
        this.param.putAll(param);
        this.fileName = param.get("file_name");
    }

    @Override
    public LfasrMessage call() {
        LfasrMessage message = new LfasrMessage();
        try {
            message = resolveMessage(this.connector.post("https://raasr.xfyun.cn/api/prepare", this.param));
            if (message.getOk() == LfasrTaskStatusEnum.STATUS_FAILED.getKey()) {
                message.setOk(LfasrTaskStatusEnum.STATUS_1.getKey());
            }
        } catch (HttpException | IOException e) {
            logger.warn(getIntro() + " 处理失败", e);
            message.setOk(LfasrTaskStatusEnum.STATUS_1.getKey());
        }
        return message;
    }

    @Override
    public String getIntro() {
        return "prepare task: " + this.fileName;
    }
}
