package cn.xfyun.api;

import cn.xfyun.exception.HttpException;
import cn.xfyun.model.header.BuildHttpHeader;
import cn.xfyun.model.response.ltp.LtpResponse;
import cn.xfyun.util.HttpConnector;
import cn.xfyun.util.StringUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自然语言处理，是以哈工大社会计算与信息检索研究中心研发的 “语言技术平台（LTP）” 为基础，为用户提供高效精准的 中文（简体） 自然语言处理服务。
 * 该自然语言基础处理服务包括：词法分析、依存句法分析、语义角色标注、语义依存 (依存树) 分析、语义依存 (依存图) 分析五类，其中词法分析又可以分为：
 * 中文分词、词性标注、命名实体识别。
 * @author yingpeng
 */
public class LtpClient {

    /**
     * 接口地址
     */
    private String hostUrl;

    /**
     * 应用id，详情请见讯飞开放平台控制台
     */
    private String appId;

    /**
     * 接口密钥，在讯飞开放平台控制台添加相应服务后即可获取
     */
    private String apiKey;

    /**
     *类型，可选值：dependent
     */
    private String type;

    private HttpConnector connector;

    public LtpClient(LtpClient.Builder builder){
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.hostUrl = builder.hostUrl;
        this.type = builder.type;
        this.connector = builder.connector;
    }

    /**
     * 发送请求
     * @param text 待分析文本，长度限制为500字节(中文简体)
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public LtpResponse send(String text) throws IOException, HttpException {
        /**
         * 构建请求头
         */
        String param = "{\"type\":\"" + type +"\"}";
        Map<String, String> header = BuildHttpHeader.buildHttpHeader(param, apiKey, appId);
        Map<String, String> body = new HashMap<>();
        body.put("text", text);
        String result = connector.post(hostUrl, header, body);
        Gson gson = new Gson();
        return gson.fromJson(result, LtpResponse.class);
    }

    public static final class Builder {
        private final String appId;
        private final String apiKey;
        private String hostUrl = "https://ltpapi.xfyun.cn/v1/";
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
         */
        private String func;

        /**
         * 最大连接数
         */
        private Integer maxConnections = 50;

        /**
         * 建立连接的超时时间
         */
        private Integer connTimeout = 10000;

        /**
         * 读数据包的超时时间
         */
        private Integer soTimeout = 30000;

        /**
         * 重试次数
         */
        private Integer retryCount = 3;
        private HttpConnector connector;

        public Builder(String appId, String apiKey) {
            this.appId = appId;
            this.apiKey = apiKey;
        }


        public LtpClient.Builder maxConnections(Integer maxConnections) {
            this.maxConnections = maxConnections;
            return this;
        }

        public LtpClient.Builder connTimeout(Integer connTimeout) {
            this.connTimeout = connTimeout;
            return this;
        }

        public LtpClient.Builder soTimeout(Integer soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public LtpClient.Builder hostUrl(String hostUrl){
            this.hostUrl = hostUrl;
            return this;
        }

        public LtpClient.Builder type(String type){
            this.type = type;
            return this;
        }

        public LtpClient.Builder func(String func){
            this.func = func;
            return this;
        }

        public LtpClient.Builder retryCount(Integer retryCount){
            this.retryCount = retryCount;
            return this;
        }

        public LtpClient build() throws Exception {
            this.connector = HttpConnector.build(maxConnections, connTimeout, soTimeout, retryCount);
            if (StringUtils.isNullOrEmpty(func)){
                throw new Exception("func参数为空");
            }
            /** 构建接口 */
            this.hostUrl = hostUrl + func;
            return new LtpClient(this);
        }
    }
}
