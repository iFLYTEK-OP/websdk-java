# 讯飞开放平台AI能力-JAVASDK语音能力库

### 图片类识别（营业执照,出租车发票,火车票,增值税发票 ,身份证,印刷文字）

**示例代码**
```java
        ImageWordClient client = new ImageWordClient
                .Builder(appId, apiKey, apiSecret, ImageWordEnum.IDCARD)
                .build();
        InputStream inputStream = new FileInputStream(new File(resourcePath + "/image/car.jpg"));
        byte[] imageByteArray = IOUtils.readFully(inputStream, -1, true);
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.imageWord(imageBase64, "jpg"));
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/ImageWordClientApp.java)

##### 图片类识别参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|imageWordEnum|ImageWordEnum|是|识别类别。<br>BUSINESS_LICENSE:营业执照识别 <br>TAXI_INVOICE:出租车发票识别 <br>TRAIN_TICKET:火车票识别 <br>INVOICE:增值税发票识别 <br>IDCARD:身份证识别 <br>PRINTED_WORD:多语种文字识别|ImageWordEnum.PRINTED_WORD|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/words/id_card/API.html)
