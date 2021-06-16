package cn.xfyun.service.lfasr.task;

import cn.xfyun.config.LfasrTaskStatusEnum;
import cn.xfyun.exception.HttpException;
import cn.xfyun.model.response.lfasr.LfasrMessage;
import cn.xfyun.model.sign.LfasrSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.SignatureException;

/**
 * getResult接口封装
 *
 * @author : iflytek
 * @date : 2021年03月15日
 */
public class PullResultTask extends AbstractTask {
    private static final Logger logger = LoggerFactory.getLogger(PullResultTask.class);

    private final String taskId;

    public PullResultTask(LfasrSignature signature, String taskId) throws SignatureException {
        super(signature);
        this.param.put("task_id", taskId);
        this.taskId = taskId;
    }

    @Override
    public LfasrMessage call() {
        LfasrMessage message = new LfasrMessage();
        try {
            message = resolveMessage(this.connector.post("https://raasr.xfyun.cn/api/getResult", this.param));
        } catch (Exception e) {
            logger.warn(getIntro() + " 处理失败", e);
            message.setOk(LfasrTaskStatusEnum.STATUS_1.getKey());
        }
        return message;
    }

    @Override
    public String getIntro() {
        return "pull result task: " + this.taskId;
    }
}