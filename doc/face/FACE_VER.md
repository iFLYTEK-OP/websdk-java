# 讯飞开放平台AI能力-JAVASDK语音能力库

### 人脸比对sensetime

**示例代码**
```java
        FaceVerificationClient client = new FaceVerificationClient
                .Builder(appId, apiKey)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/12.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.compareFace(imageBase64, imageBase64));
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/FaceVerificationClientApp.java)

##### 人脸检测和属性分析参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|imageBase641|string|是|第一张人脸图片base64编码|无|
|imageBase642|string|是|第二张人脸图片base64编码|无|
