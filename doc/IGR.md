### 性别年龄识别

**示例代码**

```java
import cn.xfyun.api.IgrClient;

// 设置性别年龄识别参数,这里的appid,apiKey,apiSecret是在开放平台控制台获得
 IgrClient igrClient = new IgrClient.Builder()
                //... 这里可以继续设置评测相关参数，参数见下面表格
                .build();

 File file = new File(filePath);
        igrClient.send(file, new AbstractIgrWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IgrResponseData igrResponseData) {
            // 根据服务端成功响应，进行数据处理，处理方法可见demo
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
            // 根据业务需求，对失败时的数据进行处理
            }
        });

```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/IgrClientApp.java)

**评测参数**

  | 参数名   | 类型   | 必传 | 描述                                                        
  | -------- | ------ | ---- | ------------------------------------------------------------ 
  | ent | string | 是 | 引擎类型，目前仅支持igr |
  | aue | string | 是 | 音频格式<br>raw：原生音频数据pcm格式<br>speex：speex格式（rate需设置为8000）<br>speex-wb：宽频speex格式（rate需设置为16000）<br>amr：amr格式（rate需设置为8000）<br>amr-wb：宽频amr格式（rate需设置为16000）| 
  |rate |int |是| 音频采样率 16000/8000|
 
 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/voiceservice/sound-feature-recg/API.html)