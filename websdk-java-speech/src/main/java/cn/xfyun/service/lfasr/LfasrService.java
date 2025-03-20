package cn.xfyun.service.lfasr;

import cn.xfyun.model.response.lfasr.LfasrResponse;
import cn.xfyun.model.sign.LfasrSignature;
import cn.xfyun.util.FileUtil;
import cn.xfyun.util.HttpConnector;
import cn.xfyun.util.StringUtils;

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
 * 语音转写服务类
 * 提供语音文件上传、URL上传和获取转写结果等功能
 */
public class LfasrService {
    
    private static final Logger logger = LoggerFactory.getLogger(LfasrService.class);

    // 语音文件上传接口地址
    private static final String UPLOAD_URL = "https://raasr.xfyun.cn/v2/api/upload";

    // 获取转写结果接口地址
    private static final String GET_RESULT_URL = "https://raasr.xfyun.cn/v2/api/getResult";
    
    // HTTP连接器，用于发送请求
    private final HttpConnector connector;

    // 签名参数，包含appId、签名和时间戳
    private Map<String, String> signatureParam = new HashMap<>();
    
    /**
     * 私有构造方法，初始化HTTP连接器和签名参数
     * 
     * @param connector HTTP连接器
     * @param signature 签名对象
     * @throws SignatureException 签名异常
     */
    private LfasrService(HttpConnector connector, LfasrSignature signature) throws SignatureException {
        this.connector = connector;
        this.signatureParam.put("appId", signature.getId());
        this.signatureParam.put("signa", signature.getSigna());
        this.signatureParam.put("ts", signature.getTs());
    }
    
    /**
     * 构建LfasrService实例
     * 
     * @param appId 应用ID
     * @param secretKey 密钥
     * @param maxConnections 最大连接数
     * @param connTimeout 连接超时时间(毫秒)
     * @param soTimeout Socket超时时间(毫秒)
     * @return LfasrService实例
     * @throws SignatureException 签名异常
     */
    public static LfasrService build(String appId, String secretKey, int maxConnections, int connTimeout, int soTimeout) throws SignatureException {
        HttpConnector connector = HttpConnector.build(maxConnections, connTimeout, soTimeout, 3);
        LfasrSignature signature = new LfasrSignature(appId, secretKey);
        return new LfasrService(connector, signature);
    }
    
    /**
     * 上传本地音频文件进行转写
     * 
     * @param param 请求参数，包含文件名、语种等信息
     * @param audioFile 音频文件对象
     * @return 转写响应结果
     * @throws SignatureException 签名异常
     */
    public LfasrResponse uploadFile(Map<String, String> param, File audioFile) throws SignatureException {
        // 添加签名参数
        param.putAll(this.signatureParam);
        // 设置请求头
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/octet-stream");
        header.put("Chunked", "false");
        try {
            // 构建带参数的URL
            URIBuilder uriBuilder = new URIBuilder(UPLOAD_URL);
            for (Map.Entry<String, String> entry : param.entrySet()) {
                uriBuilder.addParameter(entry.getKey(), entry.getValue());
            }
            String finalUrl = uriBuilder.build().toString();
            // 读取音频文件数据（使用FileUtil处理大文件）
            byte[] audioData = FileUtil.readFileToByteArray(audioFile);
            // 发送请求并获取响应
            String response = connector.postByBytes(finalUrl, header, audioData);
            return StringUtils.gson.fromJson(response, LfasrResponse.class);
        } catch (IOException e) {
            logger.error("音频文件上传失败", e);
            return LfasrResponse.error("文件上传失败: " + e.getMessage());
        } catch (URISyntaxException e) {
            logger.error("构建URL失败", e);
            return LfasrResponse.error("构建请求URL失败");
        }
    }
    
    /**
     * 上传音频URL进行转写
     * 
     * @param param 请求参数
     * @param audioUrl 音频URL地址
     * @return 转写响应结果
     * @throws SignatureException 签名异常
     */
    public LfasrResponse uploadUrl(Map<String, String> param, String audioUrl) throws SignatureException {
        param.putAll(this.signatureParam);
        try {
            String response = connector.post(UPLOAD_URL, param);
            return StringUtils.gson.fromJson(response, LfasrResponse.class);
        } catch (IOException e) {
            logger.error("音频URL上传失败", e);
            return LfasrResponse.error("URL上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取转写结果
     * 
     * @param orderId 订单ID，上传接口返回
     * @param resultType 结果类型，默认为transfer
     * @return 转写结果响应
     * @throws SignatureException 签名异常
     */
    public LfasrResponse getResult(String orderId, String resultType) throws SignatureException {
        Map<String, String> param = new HashMap<>();
        param.put("orderId", orderId);
        if (!StringUtils.isNullOrEmpty(resultType)) {
            param.put("resultType", resultType);
        }
        param.putAll(signatureParam);
        try {
            String response = connector.post(GET_RESULT_URL, param);
            return StringUtils.gson.fromJson(response, LfasrResponse.class);
        } catch (IOException e) {
            logger.error("获取转写结果失败", e);
            return LfasrResponse.error("获取结果失败: " + e.getMessage());
        }
        
    }
}