package cn.xfyun.api;

import cn.xfyun.base.http.HttpRequestBuilder;
import cn.xfyun.base.http.HttpRequestClient;
import cn.xfyun.config.LtpFunctionEnum;
import cn.xfyun.model.sign.Signature;

import java.io.IOException;
import java.util.Map;

/**
 * 自然语言处理，是以哈工大社会计算与信息检索研究中心研发的 “语言技术平台（LTP）” 为基础，为用户提供高效精准的 中文（简体） 自然语言处理服务。
 * 该自然语言基础处理服务包括：词法分析、依存句法分析、语义角色标注、语义依存 (依存树) 分析、语义依存 (依存图) 分析五类，其中词法分析又可以分为：
 * 中文分词、词性标注、命名实体识别。
 *
 * @author yingpeng
 */
public class LtpClient extends HttpRequestClient {

    /**
     *类型，可选值：dependent
     */
    private String type;

    /**
     * 能力标识
     * 中文分词(cws)
     * 词性标注(pos)
     * 命名实体识别(ner)
     * 依存句法分析(dp)
     * 语义角色标注(srl)
     * 语义依存 (依存树) 分析(sdp)
     * 语义依存 (依存图) 分析(sdgp)
     * 关键词提取(ke)
     */
    private LtpFunctionEnum func;

    public String getType() {
        return type;
    }

    public String getFunc() {
        return func.getValue();
    }

    public LtpClient(LtpClient.Builder builder){
       super(builder);
       this.func = builder.func;
       this.type = builder.type;
    }

    /**
     * 发送请求
     * @param text 待分析文本，长度限制为500字节(中文简体)
     * @return
     * @throws IOException
     */
    public String send(String text) throws IOException {
        String param = "{\"type\":\"" + type +"\"}";
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, param, null);
        return sendPost(hostUrl + func, FORM, header, "text=" + text);
    }

    public static final class Builder extends HttpRequestBuilder<Builder> {

        private static final String HOST_URL = "https://ltpapi.xfyun.cn/v1/";

        private String type = "dependent";

        /**
         * 能力标识
         * 中文分词(cws)
         * 词性标注(pos)
         * 命名实体识别(ner)
         * 依存句法分析(dp)
         * 语义角色标注(srl)
         * 语义依存 (依存树) 分析(sdp)
         * 语义依存 (依存图) 分析(sdgp)
         * 关键词提取(ke)
         */
        private LtpFunctionEnum func;


        public Builder(String appId, String apiKey, LtpFunctionEnum func) {
           super(HOST_URL, appId, apiKey, null);
           this.func = func;
        }


        public LtpClient.Builder type(String type){
            this.type = type;
            return this;
        }

        public LtpClient.Builder func(LtpFunctionEnum func){
            this.func = func;
            return this;
        }

        @Override
        public LtpClient build() {
            return new LtpClient(this);
        }
    }
}
