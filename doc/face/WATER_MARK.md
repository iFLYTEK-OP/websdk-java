# 讯飞开放平台AI能力-JAVASDK语音能力库

### 人脸水印照比对

**示例代码**
```java
        WatermarkVerificationClient client = new WatermarkVerificationClient
                .Builder(appId, apiKey)
                .build();
        byte[] imageByteArray1 = read(resourcePath + "/image/1.jpg");
        String imageBase641 = Base64.getEncoder().encodeToString(imageByteArray1);
        byte[] imageByteArray2 = read(resourcePath + "/image/2.png");
        String imageBase642 = Base64.getEncoder().encodeToString(imageByteArray2);
        System.out.println(client.compare(imageBase641, imageBase642));
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/face/WatermarkVerificationClientApp.java)

##### 人脸水印照比对参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|imageBase641|string|是|第一张人脸图片base64编码|无|
|imageBase642|string|是|第二张人脸图片base64编码|无|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/face/faceWaterPhotoComparisonRecg/API.html)
