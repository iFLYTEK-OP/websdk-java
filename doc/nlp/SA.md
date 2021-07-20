### 情感分析

**示例代码**


```java
SaClinet saClinet = new SaClinet.Builder(appId, apiKey).build();
System.out.println(saClinet.send("你好啊"));
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/SaClientApp.java)

**情感分析参数**

| 参数名   | 类型   | 必传 | 描述                                                         | 示例    |
  | -------- | ------ | ---- | ------------------------------------------------------------ | ------- |
  | type | string | 是 | 服务类型，调用情感分析功能固定为dependent | dependent |


 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/nlp/emotion-analysis/API.html)