# 讯飞开放平台AI能力-JAVASDK语音能力库

### 配合式活体检测

**示例代码**
```java
        FaceStatusClient client = new FaceStatusClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        InputStream inputStream = new FileInputStream(new File(resourcePath + filePath));
        byte[] imageByteArray = IOUtils.readFully(inputStream, -1, true);
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.faceContrast(imageBase64, "jpg"));
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/FaceStatusClientApp.java)

##### 配合式活体检测参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|imageBase64|string|是|人脸图片base64编码|无|
|encoding|string|是|人脸图片图片格式|jpg|
|encoding|string|否|返回值文本编码，可选值：utf8（默认值）|utf8|
|compress|string|否|返回值文本压缩格式，可选值：raw（默认值）|raw|
|format|string|否|返回值文本格式，可选值：json（默认值）|json|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/face/xf-cooperation-living-body-detection/API.html)
