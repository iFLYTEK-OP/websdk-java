# 讯飞开放平台AI能力-JAVASDK语音能力库

### 行驶证识别  驾驶证识别  车牌识别

**示例代码**
```java
        JDOcrClient client = new JDOcrClient
                .Builder(appId, apiKey, apiSecret, JDRecgEnum.JD_OCR_CAR)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/car.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.handle(imageBase64, "jpg"));
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/JDOcrClientApp.java)

##### 识别参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|jDRecgEnum|JDRecgEnum|是|识别类型。<br>JD_OCR_VEHICLE:行驶证识别 <br>JD_OCR_DRIVER:驾驶证识别 <br>JD_OCR_CAR:车牌识别|JDRecgEnum.JD_OCR_VEHICLE|