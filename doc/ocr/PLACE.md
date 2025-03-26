# 讯飞开放平台AI能力-JAVASDK语音能力库

### 场所识别

**示例代码**
```java
        PlaceRecClient client = new PlaceRecClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        InputStream inputStream = new FileInputStream(new File(resourcePath + filePath));
        byte[] imageByteArray = IOUtils.readFully(inputStream, -1, true);
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.send(imageBase64, "jpg"));
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/ocr/PlaceRecClientApp.java)

##### 名片识别参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|imageBase64|String|是|图片的base64编码|无|
|type|String|是|图片的格式|"jpg"|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/image/place-recg/API.html)
