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
 * upload接口封装
 *
 * @author : iflytek
 * @date : 2021年03月15日
 */
public class UploadTask extends AbstractTask {
    private static final Logger logger = LoggerFactory.getLogger(UploadTask.class);

    private final byte[] slice;

    private final String taskId;

    private final String sliceId;

    public UploadTask(LfasrSignature signature, String taskId, String sliceId, byte[] slice) throws SignatureException {
        super(signature);
        this.param.put("task_id", taskId);
        this.param.put("slice_id", sliceId);
        this.slice = slice;
        this.taskId = taskId;
        this.sliceId = sliceId;
    }

    @Override
    public LfasrMessage call() {
        LfasrMessage message = new LfasrMessage();
        try {
            message = resolveMessage(this.connector.post("https://raasr.xfyun.cn/api/upload", this.param, this.slice));
        } catch (IOException e) {
            logger.warn(getIntro() + " 处理失败", e);
            message.setOk(LfasrTaskStatusEnum.STATUS_1.getKey());
        }
        return message;
    }

    @Override
    public String getIntro() {
        return "upload task: " + this.taskId + ", sliceId: " + this.sliceId;
    }
}
