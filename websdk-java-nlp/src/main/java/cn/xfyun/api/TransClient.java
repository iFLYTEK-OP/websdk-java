package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.model.translate.TransParam;
import cn.xfyun.model.translate.request.TransV2Request;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Map;

/**
 * <p1>
 * 机器翻译，基于讯飞自主研发的机器翻译引擎，
 * 已经支持包括英、日、法、西、俄等70多种语言(其中维语、藏语需申请开通)，效果更优质，
 * 已在讯飞翻译机上应用并取得优异成绩，详细请参照 语种列表 。通过调用该接口，将源语种文字转化为目标语
 * <p2>
 * 机器翻译（新），基于讯飞自主研发的机器翻译引擎，已经支持包括英、日、法、西、俄等多种语言，
 * 效果更优质，已在讯飞翻译机上应用并取得优异成绩，详细请参照语种列表 。
 * 通过调用该接口，可以将源语种文字转化为目标语种文字，并且支持术语词语的个性化翻译。
 * <p3>
 * niuTrans机器翻译2.0，基于小牛翻译自主研发的多语种机器翻译引擎，
 * 已经支持包括英、日、韩、法、西、俄等100多种语言
 *
 * @author <ydwang16@iflytek.com>
 * @description 翻译客户端
 * @date 2021/6/15
 */
public class TransClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(TransClient.class);

    /**
     * 源语种
     */
    private final String from;

    /**
     * 目标语种
     */
    private final String to;

    /**
     * 个性化术语ID
     * 1、个性化术语资源id
     * 2、在机器翻译控制台自定义 <a href="https://console.xfyun.cn/services/its">...</a>
     * （翻译术语热词格式为：原文本1|目标文本1，如：开放平台|KFPT，一个术语资源id支持定义多个术语，多个术语之间用换行符间隔）
     * 3、请注意使用参数值和控制台自定义的值保持一致
     */
    private final String resId;

    public TransClient(Builder builder) {
        super(builder);
        this.from = builder.from;
        this.to = builder.to;
        this.resId = builder.resId;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getResId() {
        return resId;
    }

    /**
     * 自研翻译，默认源语种为中文
     *
     * @param text 文本
     * @return 返回结果
     */
    public String sendIst(String text) throws Exception {
        // 封装参数
        TransParam param = TransParam.builder().text(text).build();

        // 发送请求
        return send(Builder.ITS_SERVER_URL, param);
    }

    public String sendIst(TransParam param) throws Exception {
        // 发送请求
        return send(Builder.ITS_SERVER_URL, param);
    }

    /**
     * (新)自研翻译，默认源语种为中文
     * 支持个性化术语设置
     * 1.目前仅支持中文与其他语种的互译，不包含中文的两个语种之间不能直接翻译。
     * 2.翻译文本不能超过5000个字符，即汉语不超过15000个字节，英文不超过5000个字节。
     * 3.此接口调用返回时长上有优化、通过个性化术语资源使用可以做到词语个性化翻译、后面会支持更多的翻译
     *
     * @param param 翻译参数
     */
    public String sendIstV2(TransParam param) throws Exception {
        // 参数校验
        paramCheck(param);

        // 构建请求体
        String body = builtV2Body(param);

        // 获取鉴权请求地址
        String realUrl = Signature.signHostDateAuthorization(Builder.ITS_PRO_SERVER_URL, "POST", apiKey, apiSecret);

        // 发送请求
        return sendPost(realUrl, JSON, null, body);
    }

    public String sendIstV2(String text) throws Exception {
        // 封装参数
        TransParam param = TransParam.builder().text(text).build();

        // 发送请求
        return sendIstV2(param);
    }

    /**
     * 小牛翻译，默认源语种为auto
     *
     * @param text 文本
     * @return 返回结果
     */
    public String sendNiuTrans(String text) throws Exception {
        // 封装参数
        TransParam param = TransParam.builder().text(text).build();

        // 发送请求
        return send(Builder.NIU_TRANS_SERVER_URL, param);
    }

    public String sendNiuTrans(TransParam param) throws Exception {
        // 发送请求
        return send(Builder.NIU_TRANS_SERVER_URL, param);
    }

    /**
     * 参数校验
     */
    private void paramCheck(TransParam param) {
        if (null == param) {
            throw new BusinessException("参数不能为空");
        } else if (StringUtils.isNullOrEmpty(param.getText())) {
            throw new BusinessException("翻译文本不能为空");
        }
    }

    /**
     * 公共请求方法
     *
     * @param url   请求地址
     * @param param 翻译参数
     * @return 请求结果
     */
    private String send(String url, TransParam param) throws IOException, SignatureException {
        // 参数校验
        paramCheck(param);

        try {
            String body = buildV1Body(param);
            Map<String, String> header = Signature.signHttpHeaderDigest(url, "POST", apiKey, apiSecret, body);
            return sendPost(url, JSON, header, body);
        } catch (InvalidKeyException e) {
            throw new SignatureException("InvalidKeyException:" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new SignatureException("NoSuchAlgorithmException:" + e.getMessage());
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 组装http请求体
     */
    private String buildV1Body(TransParam param) {
        JsonObject body = new JsonObject();
        JsonObject business = new JsonObject();
        JsonObject common = new JsonObject();
        JsonObject data = new JsonObject();
        // 填充common
        common.addProperty("app_id", appId);
        // 填充business
        business.addProperty("from", StringUtils.isNullOrEmpty(param.getFrom()) ? this.from : param.getFrom());
        business.addProperty("to", StringUtils.isNullOrEmpty(param.getTo()) ? this.to : param.getTo());
        // 填充data
        data.addProperty("text", Base64.getEncoder().encodeToString(param.getText().getBytes(StandardCharsets.UTF_8)));
        // 填充body
        body.add("common", common);
        body.add("business", business);
        body.add("data", data);
        logger.debug("翻译请求入参：{}", body);
        return body.toString();
    }

    /**
     * 组装新版本引擎http请求体
     */
    private String builtV2Body(TransParam param) {
        TransV2Request request = new TransV2Request();
        // 填充header
        TransV2Request.Header header = new TransV2Request.Header();
        header.setAppId(appId);
        header.setStatus(3);
        header.setResId(StringUtils.isNullOrEmpty(param.getResId()) ? this.resId : param.getResId());
        request.setHeader(header);
        // 填充parameter
        TransV2Request.Parameter parameter = new TransV2Request.Parameter();
        TransV2Request.Parameter.Its its = new TransV2Request.Parameter.Its();
        its.setFrom(StringUtils.isNullOrEmpty(param.getFrom()) ? this.from : param.getFrom());
        its.setTo(StringUtils.isNullOrEmpty(param.getTo()) ? this.to : param.getTo());
        its.setResult(new Object());
        parameter.setIts(its);
        request.setParameter(parameter);
        // 填充payload
        TransV2Request.Payload payload = new TransV2Request.Payload();
        TransV2Request.Payload.InputData inputData = new TransV2Request.Payload.InputData();
        inputData.setEncoding("utf8");
        // 一次传完
        inputData.setStatus(3);
        inputData.setText(Base64.getEncoder().encodeToString(param.getText().getBytes(StandardCharsets.UTF_8)));
        payload.setInputData(inputData);
        request.setPayload(payload);
        String requestStr = StringUtils.gson.toJson(request);
        logger.debug("新版本翻译请求入参：{}", requestStr);
        return requestStr;
    }

    public static final class Builder extends HttpBuilder<Builder> {

        /**
         * 自研机器翻译服务端地址
         */
        private static final String ITS_SERVER_URL = "https://itrans.xfyun.cn/v2/its";
        /**
         * 自研机器翻译（新）服务端地址
         */
        private static final String ITS_PRO_SERVER_URL = "https://itrans.xf-yun.com/v1/its";
        /**
         * 小牛翻译服务端地址
         */
        private static final String NIU_TRANS_SERVER_URL = "https://ntrans.xfyun.cn/v2/ots";
        private String from = "cn";
        private String to = "en";
        /**
         * 个性化术语ID   仅新版本支持
         * 1、个性化术语资源id
         * 2、在机器翻译控制台自定义 <a href="https://console.xfyun.cn/services/its">...</a>
         * （翻译术语热词格式为：原文本1|目标文本1，如：开放平台|KFPT，一个术语资源id支持定义多个术语，多个术语之间用换行符间隔）
         * 3、请注意使用参数值和控制台自定义的值保持一致
         */
        private String resId;

        public Builder(String appId, String apiKey, String apiSecret) {
            super(ITS_SERVER_URL, appId, apiKey, apiSecret);
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder to(String to) {
            this.to = to;
            return this;
        }

        public Builder resId(String resId) {
            this.resId = resId;
            return this;
        }

        @Deprecated
        @Override
        public Builder hostUrl(String hostUrl) {
            return super.hostUrl(hostUrl);
        }

        @Override
        public TransClient build() {
            return new TransClient(this);
        }
    }
}
