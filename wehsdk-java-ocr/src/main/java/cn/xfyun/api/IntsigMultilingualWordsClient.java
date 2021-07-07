package cn.xfyun.api;

import cn.xfyun.base.http.HttpRequestBuilder;
import cn.xfyun.base.http.HttpRequestClient;
import cn.xfyun.model.sign.Signature;

import java.io.IOException;
import java.util.Map;

/**
 *     印刷文字识别（多语种）intsig
 *
 *     印刷文字识别（多语种），通过 OCR（光学字符识别 Optical Character Recognition）技术，
 *     自动对文档 OCR 进行识别，返回文档上的纯文本信息，可以省去用户手动录入的过程，
 *     并会返回图片中文字的坐标位置，方便二次开发。 自动完成文档 OCR 信息的采集，
 *     可以很方便对接客户的后台数据系统，给用户带来极大的便利。
 *     该印刷文字识别接口支持语种包括：中(简体和繁体)、英、日、韩、德、法、意、葡、西、荷，接口会自动判断文字语种。
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 10:15
 */
public class IntsigMultilingualWordsClient extends HttpRequestClient {

    /**
     *  X-Param 为各配置参数组成的 JSON 串经 BASE64 编码之后的字符串
     */
    private static final String PARAM = "{\"engine_type\":\"recognize_document\"}";

    public IntsigMultilingualWordsClient(Builder builder) {
        super(builder);
    }


    /**
     *    该接口图片仅支持jpg格式
     *
     * @param imageBase64
     * @return
     * @throws IOException
     */
    public String wordsRecognition(String imageBase64) throws IOException {
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, PARAM, null);
        return sendPost(hostUrl, FORM, header, "image=" + imageBase64);
    }

    public static final class Builder extends HttpRequestBuilder<Builder> {


        private static final String HOST_URL = "https://webapi.xfyun.cn/v1/service/v1/ocr/recognize_document";

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        @Override
        public IntsigMultilingualWordsClient build() {
            return new IntsigMultilingualWordsClient(this);
        }
    }
}
