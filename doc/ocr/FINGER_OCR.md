# 讯飞开放平台AI能力-JAVASDK语音能力库

### 指尖文字识别

**示例代码**
```java
        FingerOcrClient client = new FingerOcrClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/finger.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.fingerOcr(imageBase64));
```
更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/ocr/FingerOcrClientApp.java)

##### 指尖文字识别参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|cutWScale|float|否|根据指尖位置选取ROI（感兴趣区域）的宽度倍数。即设置ROI的宽度是手指宽度的几倍（宽度= cut_w_scale * 手指宽度），默认3.0，取值范围：[0,65536]|cutWScale=5f|
|cutHScale|float|否|根据指尖位置选取ROI（感兴趣区域）的高度倍数。即设置ROI的高度是手指宽度的几倍（高度= cut_h_scale * 手指宽度），默认2.0，取值范围：[0,65536]|cutHScale=2f|
|cutShift|float|否|根据指尖位置选取ROI（感兴趣区域）的往下平移的倍数。即设置ROI往下平移的距离是ROI宽度的几倍（平移量= cut_shift * 手指宽度），默认0.3，取值范围：[0,1]|cutShift=0.1f|
|resizeW|int|否|引擎内部处理模块输入图像宽度，取值范围：[1,65536]。若应用端上传图像宽为input_w，scale为缩放系数，则resize_w=input_w*scale。若不缩放直接按原图处理，引擎耗时会变长，建议根据实际情况测试以寻求最佳值。|resizeW=1000|
|resizeH|int|否|引擎内部处理模块输入图像高度，取值范围：[1,65536]。若应用端上传图像高为input_h，scale为缩放系数，则resize_h=input_h*scale。若不缩放直接按原图处理，引擎耗时会变长，建议根据实际情况测试以寻求最佳值。|resizeH=1000|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/words/formula-discern/API.html)