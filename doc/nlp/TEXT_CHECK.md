### 文本纠错

**示例代码**

```java
TextCheckClient client = new TextCheckClient
				.Builder(appId, apiSecret, apiKey)
				.build();
		String result = client.send("画蛇天足");
		System.out.println("返回结果: " + result);
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/TextCheckClientApp.java)
**文本纠错参数**

| 参数名   | 类型   | 必传 | 描述                                                         | 示例    |
  | -------- | ------ | ---- | ------------------------------------------------------------ | ------- |
  | header.app_id |	string|	是|	在平台申请的appid信息| app_id|
  | header.status |	string|	是|	请求状态，取值范围为：3（一次传完）|3|
  | parameter.s9a87e3ec.result.encoding |string|	否|	文本编码，可选值：utf8（默认值）|utf8|
  | parameter.s9a87e3ec.result.compress |string|	否|	文本压缩格式，可选值：raw（默认值）|raw|
  | parameter.s9a87e3ec.result.format |	string|	否|	文本格式，可选值：json（默认值）|json|
  | payload.input|object|	是|	用于上传文本数据|画蛇天足|
  | payload.input.encoding|	string|	否|	文本编码，可选值：utf8（默认值）|utf8|
  | payload.input.compress|	string|	否|	文本压缩格式，可选值：raw（默认值）|raw|
  | payload.input.encoding|	string|	否|	文本格式，可选值：json（默认值）|json|
  | payload.input.text|	string|	是|	文本数据，base64编码，最大支持7000字节，请注意中文要控制在2000个字符|5aSq6Ziz5b2T56m654Wn77yM6Iqx5YS/5a+|
  | payload.input.status|	int|否|	上传数据状态，取值范围为：3（一次传完）|3|


 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/nlp/textCorrection/API.html)
