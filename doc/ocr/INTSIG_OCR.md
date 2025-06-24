# 讯飞开放平台AI能力-JAVASDK语音能力库

### 身份证识别 营业执照识别 增值税发票识别 印刷文字识别（多语种）

**示例代码**
```java
        IntsigOcrClient client = new IntsigOcrClient
                // 身份证识别             IntsigRecgEnum.IDCARD
                // 营业执照识别           IntsigRecgEnum.BUSINESS_LICENSE
                // 增值税发票识别         IntsigRecgEnum.INVOICE
                // 印刷文字识别（多语种）  IntsigRecgEnum.RECOGNIZE_DOCUMENT
                // 通用文本识别（多语种）  IntsigRecgEnum.COMMON_WORD
                // .Builder(appId, apiKey, apiSecret, IntsigRecgEnum.COMMON_WORD)
                .Builder(appId, apiKey, IntsigRecgEnum.RECOGNIZE_DOCUMENT)
                .build();
        InputStream inputStream = Files.newInputStream(new File(resourcePath + filePath).toPath());
        byte[] bytes = IoUtil.readBytes(inputStream);
        String imageBase64 = Base64.getEncoder().encodeToString(bytes);
        logger.info("请求地址：{}", client.getHostUrl());
        String result = client.intsigRecg(imageBase64);
        logger.info("请求结果：{}", result);
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/ocr/IntsigOcrClientApp.java)

##### 识别参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|intsigRecgEnum|IntsigRecgEnum|是|识别类型。<br>IDCARD:身份证识别 <br>BUSINESS_LICENSE:营业执照识别 <br>INVOICE:增值税发票识别 <br>RECOGNIZE_DOCUMENT:印刷文字识别（多语种）|IntsigRecgEnum.IDCARD|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/words/printed-word-recognition/API.html)
