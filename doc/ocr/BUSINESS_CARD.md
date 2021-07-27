# 讯飞开放平台AI能力-JAVASDK语音能力库

### 名片识别

**示例代码**
```java
BusinessCard client = new BusinessCard
                .Builder(appId, apiKey)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/1.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.businessCard(imageBase64));
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/BusinessCardApp.java)

##### 名片识别参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|picRequired|string|否|是否返回切边增强图像。<br>0:不返回（默认值） <br>1:返回的json结果中切边增强图片数据格式详见返回值说明|picRequired="0"|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/words/businessCardRecg/API.html)