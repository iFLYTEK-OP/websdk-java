# 讯飞开放平台AI能力-JAVASDK语音能力库

### 图片类识别（营业执照,出租车发票,火车票,增值税发票 ,身份证,印刷文字）

**示例代码**
```java
        ImageWordClient client = new ImageWordClient
                // 身份证识别      ImageWordEnum.IDCARD
                // 营业执照识别    ImageWordEnum.BUSINESS_LICENSE
                // 出租车发票识别  ImageWordEnum.TAXI_INVOICE
                // 火车票识别      ImageWordEnum.TRAIN_TICKET
                // 增值税发票识别  ImageWordEnum.INVOICE
                // 多语种文字识别  ImageWordEnum.PRINTED_WORD
                // 通用文字识别  ImageWordEnum.COMMON_WORD
                .Builder(appId, apiKey, apiSecret, ImageWordEnum.COMMON_WORD)
                .build();
        InputStream inputStream = Files.newInputStream(new File(resourcePath + filePath).toPath());
        byte[] bytes = IoUtil.readBytes(inputStream);
        String imageBase64 = Base64.getEncoder().encodeToString(bytes);
        String result = client.imageWord(imageBase64, "jpg");
        logger.info("请求结果：{}", result);
        JSONObject obj = JSON.parseObject(result);
        String encodeStr = obj.getJSONObject("payload").getJSONObject("result").getString("text");
        String decodeStr = new String(Base64.getDecoder().decode(encodeStr), StandardCharsets.UTF_8);
        logger.info("解码后结果：{}", decodeStr);
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/ocr/ImageWordClientApp.java)

##### 图片类识别参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|imageWordEnum|ImageWordEnum|是|识别类别。<br>BUSINESS_LICENSE:营业执照识别 <br>TAXI_INVOICE:出租车发票识别 <br>TRAIN_TICKET:火车票识别 <br>INVOICE:增值税发票识别 <br>IDCARD:身份证识别 <br>PRINTED_WORD:多语种文字识别|ImageWordEnum.PRINTED_WORD|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/words/id_card/API.html)
