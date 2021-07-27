# 讯飞开放平台AI能力-JAVASDK语音能力库

### 身份证识别 营业执照识别 增值税发票识别 印刷文字识别（多语种）

**示例代码**
```java
        IntsigOcrClient client = new IntsigOcrClient
                .Builder(appId, apiKey, IntsigRecgEnum.IDCARD)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/car.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.intsigRecg(imageBase64));
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/IntsigOcrClientApp.java)

##### 识别参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|intsigRecgEnum|IntsigRecgEnum|是|识别类型。<br>IDCARD:身份证识别 <br>BUSINESS_LICENSE:营业执照识别 <br>INVOICE:增值税发票识别 <br>RECOGNIZE_DOCUMENT:印刷文字识别（多语种）|IntsigRecgEnum.IDCARD|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/words/printed-word-recognition/API.html)