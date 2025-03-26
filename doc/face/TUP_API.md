# 讯飞开放平台AI能力-JAVASDK语音能力库

### 人脸特征分析

**示例代码**
```java
        TupApiClient client = new TupApiClient
                // 年龄      TupApiEnum.AGE
                // 性别      TupApiEnum.SEX
                // 表情      TupApiEnum.EXPRESSION
                // 颜值      TupApiEnum.FACE_SCORE
                .Builder(appId, apiKey, TupApiEnum.AGE)
                .build();
        InputStream inputStream = new FileInputStream(new File(resourcePath + filePath));
        byte[] bytes = IOUtils.readFully(inputStream, -1, true);
        System.out.println("请求地址：" + client.getHostUrl());
        System.out.println(client.recognition("测试", bytes));
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/face/AntiSpoofClientApp.java)

##### 人脸特征分析年龄参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|tupApiEnum|TupApiEnum|是|EXPRESSION:表情 <br>SEX:性别 <br>AGE:年龄 <br>FACE_SCORE:颜值|TupApiEnum.AGE|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/face/face-feature-analysis/ageAPI.html)
