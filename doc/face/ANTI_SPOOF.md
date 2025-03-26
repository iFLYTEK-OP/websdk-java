# 讯飞开放平台AI能力-JAVASDK语音能力库

### 静默活体检测

**示例代码**
```java
        AntiSpoofClient client = new AntiSpoofClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        InputStream inputStream = new FileInputStream(new File(resourcePath + filePath));
        byte[] imageByteArray = IOUtils.readFully(inputStream, -1, true);
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.faceContrast(imageBase64, "jpg"));
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/face/AntiSpoofClientApp.java)

##### 静默活体检测参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|imageBase64|string|是|图像base64编码|无|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/face/xf-silent-in-vivo-detection/API.html)