package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.config.TupApiEnum;
import cn.xfyun.model.sign.Signature;

import java.io.IOException;
import java.util.Map;

/**
 *    人脸特征分析年龄
 *
 *    人脸特征分析，基于图普的深度学习算法，
 *    可以检测图像中的人脸并进行一系列人脸相关的特征分析，当前支持识别出包括性别、颜值、年龄、表情多维度人脸信息。
 *    可用作基础人脸信息的解析，智能分析人群特征。
 *
 *    人脸特征分析年龄WebAPI接口调用示例接口文档(必看)：https://doc.xfyun.cn/rest_api/%E4%BA%BA%E8%84%B8%E7%89%B9%E5%BE%81%E5%88%86%E6%9E%90-%E5%B9%B4%E9%BE%84.html
 *    图片属性：png、jpg、jpeg、bmp、tif图片大小不超过800k
 *    (Very Important)创建完webapi应用添加服务之后一定要设置ip白名单，找到控制台--我的应用--设置ip白名单，如何设置参考：http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=41891
 *
 *    错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/1 17:12
 */
public class TupApiClient extends HttpClient {

    private TupApiEnum func;

    public TupApiEnum getFunc() {
        return func;
    }

    public TupApiClient(TupApiClient.Builder builder) {
        super(builder);
        this.func = builder.func;
    }

    public String recognition(String imageName, byte[] imageByteArray) throws IOException {
        String param = "{\"image_name\":\"" + imageName + "\"}";
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, param);
        return sendPost(hostUrl + func.getValue(), BINARY, header, imageByteArray);
    }


    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "http://tupapi.xfyun.cn/v1/";

        private TupApiEnum func;


        public Builder(String appId, String apiKey, TupApiEnum func) {
            super(HOST_URL, appId, apiKey, null);
            this.func = func;
        }

        public TupApiClient.Builder func(TupApiEnum func) {
            this.func = func;
            return this;
        }

        @Override
        public TupApiClient build() {
            TupApiClient client = new TupApiClient(this);
            return client;
        }

    }

}
