# 讯飞开放平台AI能力-JAVASDK语音能力库

### 拍照速算识别 公式识别

**示例代码**
```java
        ItrClient client = new ItrClient
                .Builder(appId, apiKey, apiSecret, ItrEntEnum.MATH_ARITH)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/itr.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.itr(imageBase64));
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/ocr/ItrClientApp.java)

##### 识别参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|itrEntEnum|ItrEntEnum|是|识别类型。<br>MATH_ARITH:拍照速算识别 <br>TEACH_PHOTO_PRINT:公式识别|ItrEntEnum.TEACH_PHOTO_PRINT|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/words/photo-calculate-recg/API.html)