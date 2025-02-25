package cn.xfyun.chat;


import cn.xfyun.basic.EasyOperation;
import cn.xfyun.basic.RestOperation;
import cn.xfyun.basic.TimeOperation;
import cn.xfyun.util.SignatureHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: rblu2
 * @desc: 上传星火知识库文档
 * @create: 2025-02-24 10:34
 **/
public class SparkDoc {
    private static final String baseUrl = "https://chatdoc.xfyun.cn/openapi/v1";
    private String appId;
    private String apiSecret;
    private String parseType = "AUTO";
    private List<String> fileIds;
    private int chunkSize = 2000;

    public static SparkDoc prepare(String appId, String apiSecret) {
        SparkDoc chatDoc = new SparkDoc();
        chatDoc.appId = appId;
        chatDoc.apiSecret = apiSecret;
        chatDoc.fileIds = new ArrayList<>();
        return chatDoc;
    }

    public SparkDoc addFileId(String fileId) {
        this.fileIds.add(fileId);
        return this;
    }


    public SparkDoc parseType(String parseType) {
        this.parseType = parseType;
        return this;
    }

    public SparkDoc chunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
        return this;
    }


    public String uploadFile(String filepath) {
        File file = new File(filepath);
        if(!file.exists()) {
            throw new RuntimeException("invalid filepath " + filepath);
        }
        String url = baseUrl + "/file/upload";
        Map<String, String> header = header();
        Map<String, String> param = EasyOperation.map(String.class)
                .put("fileType", "wiki").put("parseType", parseType)
                .put("extend.wikiSplitExtends.chunkSize", String.valueOf(chunkSize))
                .get();
        return RestOperation.form(url, header, param, file);
    }

    public String uploadUrl(String fileUrl, String filename) {
        String url = baseUrl + "/file/upload";
        Map<String, String> header = header();
        Map<String, String> param = EasyOperation.map(String.class)
                .put("fileType", "wiki").put("parseType", parseType).put("url", fileUrl).put("fileName", filename)
                .put("extend.wikiSplitExtends.chunkSize", String.valueOf(chunkSize))
                .get();
        return RestOperation.form(url, header, param, null);
    }

    public String status() {
        String url = baseUrl + "/file/status";
        Map<String, String> header = header();
        Map<String, String> param = EasyOperation.map(String.class)
                .put("fileIds", EasyOperation.joiner(fileIds,",").get())
                .get();
        return RestOperation.form(url, header, param, null);
    }

    public String extract(String fieldId) {
        String url = baseUrl + "/qa/extract";
        Map<String, String> header = header();
        Map<String, Object> param = EasyOperation.map()
                .put("fileId", fieldId).put("chunkSize", 2000).put("numPerChunk", 2)
                .get();
        return RestOperation.post(url, header, param);
    }

    private Map<String, String> header() {
        long timestamp = TimeOperation.time() / 1000;
        String signature = SignatureHelper.getSignature(appId, apiSecret, timestamp);
        return EasyOperation.map(String.class).put("appId", appId).put("timestamp", String.valueOf(timestamp)).put("signature", signature).get();
    }


}
