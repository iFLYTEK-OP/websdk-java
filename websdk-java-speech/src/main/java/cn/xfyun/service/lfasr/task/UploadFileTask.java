package cn.xfyun.service.lfasr.task;

import cn.xfyun.model.response.lfasr.LfasrResponse;
import cn.xfyun.model.sign.LfasrSignature;
import cn.xfyun.util.FileUtil;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * upload接口封装
 *
 * @author : iflytek
 * @date : 2021年03月15日
 */
public class UploadFileTask extends AbstractTask {

    private static final Logger logger = LoggerFactory.getLogger(UploadFileTask.class);

    private static final String serverUrl = "https://raasr.xfyun.cn/v2/api/upload";

    private final File audioFile;

    public UploadFileTask(LfasrSignature signature, Map<String, String> param, File audioFile) throws SignatureException {
        super(signature);
        this.audioFile = audioFile;
        this.param.putAll(param);
    }

    @Override
    public LfasrResponse call() {
        LfasrResponse response;
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/octet-stream");
        header.put("Chunked", "false");
        try {
            URIBuilder uriBuilder = new URIBuilder(serverUrl);
            for (Map.Entry<String, String> entry : this.param.entrySet()) {
                uriBuilder.addParameter(entry.getKey(), entry.getValue());
            }
            String finalUrl = uriBuilder.build().toString();
            byte[] fileBytes = FileUtil.readFileToByteArray(audioFile);
            response = resolveResponse(this.connector.postByBytes(finalUrl, header, fileBytes));
        } catch (IOException e) {
            logger.warn("音频文件上传失败，详细信息：【{}】", getIntro(), e);
            response = LfasrResponse.error("音频文件上传失败");
        } catch (URISyntaxException e) {
            logger.error("构建URL失败", e);
            response = LfasrResponse.error("构建请求URL失败");
        }
        return response;
    }

    @Override
    public String getIntro() {
        return "上传文件接口地址: " + serverUrl + "，文件名：" + audioFile.getName();
    }
}
