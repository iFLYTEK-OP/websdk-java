package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.config.AIPPTEnum;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.aippt.request.PPTCreate;
import cn.xfyun.model.aippt.request.PPTSearch;
import cn.xfyun.model.sign.Signature;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 智能PPT Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/spark/PPTv2.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class AIPPTV2Client extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(AIPPTV2Client.class);

    public AIPPTV2Client(Builder builder) {
        super(builder);
    }

    /**
     * PPT主题列表查询
     */
    public String list(PPTSearch pptSearch) throws IOException {
        // 非空校验
        nullCheck(pptSearch);

        // 构建JSON类型请求体
        RequestBody body = RequestBody.create(JSON, pptSearch.toJSONString());

        // 发送请求
        return send(AIPPTEnum.LIST, body);
    }

    /**
     * PPT生成（直接根据用户输入要求，获得最终PPT）
     * 基于用户提示、文档等相关内容生成PPT，字数不得超过8000字，文件限制10M。
     */
    public String create(PPTCreate pptCreate) throws IOException {
        // 非空校验
        nullCheck(pptCreate);

        // 参数校验
        pptCreate.createCheck();

        // 发送请求
        return send(AIPPTEnum.CREATE, pptCreate.toFormDataBody());
    }

    /**
     * 大纲生成
     */
    public String createOutline(PPTCreate pptCreate) throws IOException {
        // 非空校验
        nullCheck(pptCreate);

        // 参数校验
        pptCreate.createOutLineCheck();

        // 发送请求
        return send(AIPPTEnum.CREATE_OUTLINE, pptCreate.toFormDataBody());
    }

    /**
     * 自定义大纲生成
     * 基于用户提示、文档等相关内容生成PPT大纲。
     * query参数不得超过8000字，上传文件支持pdf(不支持扫描件)、doc、docx、txt、md格式的文件，
     * 注意：txt格式限制100万字以内，其他文件限制10M
     */
    public String createOutlineByDoc(PPTCreate pptCreate) throws IOException {
        // 非空校验
        nullCheck(pptCreate);

        // 参数校验
        pptCreate.createOutlineByDocCheck();

        // 发送请求
        return send(AIPPTEnum.CREATE_OUTLINE_BY_DOC, pptCreate.toFormDataBody());
    }

    /**
     * 通过大纲生成PPT
     */
    public String createPptByOutline(PPTCreate pptCreate) throws IOException {
        // 非空校验
        nullCheck(pptCreate);

        // 参数校验
        pptCreate.createPptByOutlineCheck();

        // 构建JSON类型请求体
        RequestBody body = RequestBody.create(JSON, pptCreate.toJSONString());

        // 发送请求
        return send(AIPPTEnum.CREATE_PPT_BY_OUTLINE, body);
    }

    /**
     * PPT进度查询
     */
    public String progress(String sid) throws IOException {
        // 发送请求
        return send(AIPPTEnum.PROGRESS, null, sid);
    }

    /**
     * 非空参数校验
     */
    private void nullCheck(Object param) {
        if (param == null) {
            throw new BusinessException("参数不能为空");
        }
    }

    /**
     * 发送请求
     */
    private String send(AIPPTEnum aipptEnum, RequestBody body, Object... param) throws IOException {
        // 请求头
        Map<String, String> header = new HashMap<>(6);
        Long timestamp = System.currentTimeMillis() / 1000;
        String signature = Signature.generateSignature(appId, timestamp, apiSecret);
        header.put("signature", signature);
        header.put("appId", appId);
        header.put("timestamp", String.valueOf(timestamp));

        // 拼接参数获取url
        String url = aipptEnum.getUrl();
        if (param != null) {
            url = String.format(aipptEnum.getUrl(), param);
        }
        // 请求结果
        logger.debug("{}请求URL：{}，入参：{}", aipptEnum.getDesc(), url, null == body ? "" : body.toString());
        return sendRequest(url, aipptEnum.getMethod(), header, body);
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://zwapi.xfyun.cn/";

        public Builder(String appId, String apiSecret) {
            super(HOST_URL, appId, null, apiSecret);
            // ppt生成有耗时操作, 客户端等待服务器响应的时间调整为30s
            this.readTimeout(30);
        }

        @Override
        public AIPPTV2Client build() {
            return new AIPPTV2Client(this);
        }
    }
}
