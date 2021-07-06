package cn.xfyun.api;

import cn.xfyun.base.http.HttpRequestBuilder;
import cn.xfyun.base.http.HttpRequestClient;
import cn.xfyun.model.sign.Signature;
import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 *     人脸比对sensetime
 *
 *     基于商汤的人脸算法，对两张通过接口上传的人脸照片进行比对，来判断是否为同一个人。
 *     若上传的照片中包含 exif 方向信息，
 *     我们会按此信息旋转、翻转后再做后续处理。同时，我们还提供自动旋转功能，
 *     当照片方向混乱且 exif 方向信息不存在或不正确的情况下，服务会根据照片中人脸方向来检查可能正确的方向，
 *     并按照正确的方向提供人脸检测结果。
 *
 *
 *    人脸比对 WebAPI 接口调用示例 接口文档（必看）：https://doc.xfyun.cn/rest_api/%E4%BA%BA%E8%84%B8%E6%AF%94%E5%AF%B9.html
 *    人脸比对图片格式必须为JPG（JPEG）,BMP,PNG,GIF,TIFF之一,宽和高必须大于 8px,小于等于 4000px,要求编码后图片大小不超过5M
 *    (Very Important)创建完webapi应用添加合成服务之后一定要设置ip白名单，找到控制台--我的应用--设置ip白名单，如何设置参考：http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=41891
 *    错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
 *
 * @author mqgao
 * @version 1.0
 * @date 2021/7/1 14:24
 */
public class FaceVerificationClient extends HttpRequestClient {

    /**
     *  是否对图片进行自动旋转，true旋转，false不旋转，默认false
     */
    private boolean autoRotate;

    public boolean isAutoRotate() {
        return autoRotate;
    }

    public FaceVerificationClient(FaceVerificationClient.Builder builder) {
        super(builder);
        this.autoRotate = builder.autoRotate;
    }

    public String compareFace(String imageBase641, String imageBase642) throws IOException {
        JsonObject jso = new JsonObject();
        jso.addProperty("get_image", true);
        jso.addProperty("auto_rotate", autoRotate);
        String params = jso.toString();
        Map<String, String> header = Signature.signHttpHeaderCheckSum(appId, apiKey, params, null);
        return sendPost(hostUrl, FORM, header, "first_image=" + URLEncoder.encode(imageBase641, "UTF-8") + "&" + "second_image="+ URLEncoder.encode(imageBase642, "UTF-8"));
    }


    public static final class Builder extends HttpRequestBuilder<Builder> {

        private static final String HOST_URL = "https://api.xfyun.cn/v1/service/v1/image_identify/face_verification";

        private boolean autoRotate = false;

        public Builder(String appId, String apiKey) {
            super(HOST_URL, appId, apiKey, null);
        }

        public FaceVerificationClient.Builder url(String url) {
            this.hostUrl(url);
            return this;
        }

        public FaceVerificationClient.Builder autoRotate(boolean autoRotate) {
            this.autoRotate = autoRotate;
            return this;
        }

        @Override
        public FaceVerificationClient build() {
            FaceVerificationClient client = new FaceVerificationClient(this);
            return client;
        }

    }






}
