# 讯飞开放平台AI能力-JAVASDK语音能力库

### 活体检查sensetime

**示例代码**
```java
        SilentDetectionClient client = new SilentDetectionClient
                .Builder(appId, apiKey)
                .build();
        System.out.println(client.silentDetection(audioBase64));
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/face/SilentDetectionClientApp.java)

##### 人脸检测和属性分析参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|audioBase64|string|是|视频base64编码|无|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/face/silent-in-vivo-detection/API.html)
