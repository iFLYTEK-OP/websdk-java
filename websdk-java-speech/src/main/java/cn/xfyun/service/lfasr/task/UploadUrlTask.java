package cn.xfyun.service.lfasr.task;

import cn.xfyun.model.response.lfasr.LfasrResponse;
import cn.xfyun.model.sign.LfasrSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Map;

/**
 * upload接口封装
 *
 * @author : iflytek
 * @date : 2021年03月15日
 */
public class UploadUrlTask extends AbstractTask {

    private static final Logger logger = LoggerFactory.getLogger(UploadUrlTask.class);

    private static final String serverUrl = "https://raasr.xfyun.cn/v2/api/upload";

    private final String audioUrl;

    public UploadUrlTask(LfasrSignature signature, Map<String, String> param, String audioUrl) throws SignatureException {
        super(signature);
        this.audioUrl = audioUrl;
        this.param.putAll(param);
    }

    @Override
    public LfasrResponse call() {
        LfasrResponse response;
        try {
            response = resolveResponse(this.connector.post(serverUrl, this.param));
        } catch (IOException e) {
            logger.warn(getIntro() + " 处理失败", e);
            response = LfasrResponse.error("上传失败");
        }
        return response;
    }

    @Override
    public String getIntro() {
        return "上传文件接口地址: " + serverUrl + "，文件链接：" + audioUrl;
    }

}
