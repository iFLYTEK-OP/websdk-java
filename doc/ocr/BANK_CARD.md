# 讯飞开放平台AI能力-JAVASDK语音能力库

### 银行卡识别

**示例代码**
```java
        BankcardClient client = new BankcardClient
                .Builder(appId, apiKey)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/backcard.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.bankcard(imageBase64));
```
更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/BankcardClientApp.java)
##### 银行卡识别参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|cardNumberImage|string|否|是否返回卡号区域截图。<br>0:不返回（默认值） <br>1:则返回base64编码的卡号区域截图|cardNumberImage="0"|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/words/bankCardRecg/API.html)