# 讯飞开放平台AI能力-JAVASDK语音能力库

### 场景识别和物体识别

**示例代码**
```java
        ImageRecClient client = new ImageRecClient
                .Builder(appId, apiKey, ImageRecEnum.SCENE)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/car.jpg");
        System.out.println(client.send( "测试", imageByteArray));
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/ocr/ImageRecClientApp.java)

##### 图片类识别参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|imageRecEnum|ImageRecEnum|是|识别类别。<br>SCENE:场景识别 <br>CURRENCY:物体识别 | ImageRecEnum.SCENE|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/image/scene-recg/API.html)
