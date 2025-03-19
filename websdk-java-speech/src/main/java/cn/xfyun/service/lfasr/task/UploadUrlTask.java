package cn.xfyun.service.lfasr.task;

import cn.xfyun.model.response.lfasr.LfasrResponse;
import cn.xfyun.model.sign.LfasrSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * upload接口封装
 *
 * @author : iflytek
 * @date : 2021年03月15日
 */
public class UploadUrlTask extends AbstractTaskV2 {

    private static final Logger logger = LoggerFactory.getLogger(UploadUrlTask.class);

    private static final String serverUrl = "https://raasr.xfyun.cn/v2/api/upload";

    private final String ContentType;

    private final File audioFile;

    public UploadUrlTask(LfasrSignature signature, Map<String, String> queryParam, String ContentType, File audioFile) throws SignatureException {
        super(signature);
        this.ContentType = ContentType;
        this.audioFile = audioFile;
        this.param.putAll(queryParam);
    }

    @Override
    public LfasrResponse call() {
        LfasrResponse response;
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json;charset=UTF-8");
        header.put("Chunked", "false");
        try {
            response = resolveMessage(this.connector.post(serverUrl, header, this.param));
        } catch (IOException e) {
            logger.warn(getIntro() + " 处理失败", e);
            response = LfasrResponse.error("上传失败");
        }
        return response;
    }

    @Override
    public String getIntro() {
        return "upload url: " + serverUrl;
    }
}
