### 歌曲识别

**示例代码**

```java
QbhClient qbhClient = new QbhClient.Builder(appId，apiKey)
                //... 这里可以继续设置评测相关参数，参数见下面表格
                .build();

     // 进行数据处理，处理方法可见demo
    String result = qbhClient.send();

```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/QbhClientApp.java)

**评测参数**

  | 参数名   | 类型   | 必传 | 说明                                                         | 示例    |
  | -------- | ------ | ---- | ------------------------------------------------------------ | ------- |
  | engine_type | string | 是 | 引擎类型，可选值：afs（哼唱) | afs |
  | aue | string | 否 | 音频编码，可选值：raw（pcm、wav格式）、aac，默认raw | raw |
  |sample_rate|string| 否 | 采样率，可选值：8000、16000，默认16000，aue是aac，sample_rate必须是8000 | 8000 |
  | audio_url | string | 否 | 哼唱音频存放地址url	|  |
  
 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/voiceservice/song-recognition/API.html)
