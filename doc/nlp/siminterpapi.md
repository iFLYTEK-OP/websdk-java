# 同声传译识别 API 文档

## 简介

本客户端基于讯飞开放平台 API实现，提供同声传译能力，可参考以下文档:

[同声传译](https://www.xfyun.cn/doc/nlp/simultaneous-interpretation/API.html)

## 功能列表

| 方法名        | 功能说明                           |
| ------------- | ---------------------------------- |
| start()       | 同声传译服务端启动                 |
| sendMessage() | 发送消息                           |
| send()        | 支持byte数组、文件、流三种方式传译 |

## 使用准备

1. 前往[同声传译](https://www.xfyun.cn/services/msi)开通能力
2. 开通后获取以下凭证：
   - appId
   - apiKey
   - apiSecret

## 快速开始

1、添加maven依赖

```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-spark</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.1.2</version>
</dependency>
```

2、Java代码

详细请参见 :

[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/nlp/SimInterpClientApp.java)

## 错误码

如出现错误码，可到 [这里 ](https://www.xfyun.cn/document/error-code)查询。

如果鉴权失败，则根据不同错误类型返回不同HTTP Code状态码，同时携带错误描述信息，详细错误说明如下：

| HTTP Code | 说明                  | 错误描述信息                                                 | 解决方法                                                     |
| --------- | --------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 401       | 缺少authorization参数 | {"message":"Unauthorized"}                                   | 检查是否有authorization参数，详情见[authorization参数详细生成规则](https://www.xfyun.cn/doc/nlp/simultaneous-interpretation/API.html#鉴权方法) |
| 401       | 签名参数解析失败      | {“message”:”HMAC signature cannot be verified”}              | 检查签名的各个参数是否有缺失是否正确，特别确认下复制的**api_key**是否正确 |
| 401       | 签名校验失败          | {“message”:”HMAC signature does not match”}                  | 签名验证失败，可能原因有很多。 1. 检查api_key,api_secret 是否正确。 2.检查计算签名的参数host，date，request-line是否按照协议要求拼接。 3. 检查signature签名的base64长度是否正常(正常44个字节)。 |
| 403       | 时钟偏移校验失败      | {“message”:”HMAC signature cannot be verified, a valid date or x-date header is required for HMAC Authentication”} | 检查服务器时间是否标准，相差5分钟以上会报此错误              |

## 平台参数

| 功能标识     | 功能描述                                                     | 数据类型 | 取值范围                                                     | 必填 | 默认值      |
| ------------ | ------------------------------------------------------------ | -------- | ------------------------------------------------------------ | ---- | ----------- |
| executor     | 用户自定义的线程池                                           | Object   |                                                              | 否   | 单例线程池  |
| languageType | 语言过滤筛选<br/>1：中英文模式，中文英文均可识别（默认）<br/>2：中文模式，可识别出简单英文<br/>3：英文模式，只识别出英文<br/>4：纯中文模式，只识别出中文<br/>注意：中文引擎支持该参数，其他语言不支持。 | int      | -                                                            | 否   | 1           |
| language     | 转写语种                                                     | String   | zh_cn                                                        | 否   | zh_cn       |
| domain       | 应用领域                                                     | String   | ist_ed_open                                                  | 否   | ist_ed_open |
| accent       | 口音取值范围                                                 | String   | 目前固定为mandarin                                           | 否   | mandarin    |
| eos          | 用于设置端点检测的静默时间，单位是毫秒。<br/>即静默多长时间后引擎认为音频结束 | int      | 取值范围0~99999999                                           | 否   | 600000      |
| vto          | vad强切控制，单位毫秒，                                      | int      | 默认15000                                                    | 否   | 15000       |
| nunum        | 将返回结果的数字格式规则为阿拉伯数字格式，默认开启<br/>0：关闭<br/>1：开启 | int      | 0（关闭）、1（开启）                                         | 否   | 1           |
| from         | 源语种                                                       | String   |                                                              | 否   | cn          |
| to           | 目标语种                                                     | String   |                                                              | 否   | en          |
| vcn          | 对应同传发音人                                               | String   | 有以下可选值：<br/>英文女性：x2_catherine<br/>英文男性：x2_john<br/>成年女性：x2_xiaoguo<br/>成年男性：x2_xiaozhong<br/>儿童女声：x2_xiaofang_cts<br/>童声开心：x2_mengmenghappy<br/>童声自然：x2_mengmengnetural | 否   | x2_xiaoguo  |
| encoding     | 音频编码，注意更改生成文件的后缀（如.pcm或.mp3），可选值：   | String   | raw：合成pcm音频<br/>lame：合成mp3音频                       | 否   | raw         |
| sampleRate   | 音频采样率                                                   | int      | 16000                                                        | 否   | 16000       |
| channels     | 声道数                                                       | int      | 1                                                            | 否   | 1           |
| bitDepth     | 位深                                                         | int      | 16                                                           | 否   | 16          |

## 方法详解

### 1. 同声传译服务端启动
```java
public WebSocket start(WebSocketListener webSocketListener) throws MalformedURLException, SignatureException
```
**参数说明**：

|       名称        |  类型  |                             描述                             | 必须 | 默认值 |
| :---------------: | :----: | :----------------------------------------------------------: | ---- | ------ |
| webSocketListener | Object | 自定义ws抽象监听类（可使用sdk提供的**SimInterpWebSocketListener**） | Y    |        |

**响应示例**：websocket对象

### 2. 同声传译发送数据

```java
 public void sendMessage(WebSocket webSocket, byte[] bytes, Integer status)
```

**参数说明**：

|   名称    |  类型  |                      描述                      | 必须 | 默认值 |
| :-------: | :----: | :--------------------------------------------: | ---- | ------ |
| webSocket | Object |                 websocket对象                  | Y    |        |
|   bytes   | Object |               用户需要传译的数据               | Y    |        |
|  status   |  int   | 数据状态：<br/>0：开始<br/>1：继续<br/>2：结束 | Y    |        |

### 3. 文件方式请求

```java
public void send(File file, WebSocketListener webSocketListener) throws FileNotFoundException, MalformedURLException, SignatureException
```

**参数说明**：

|       名称        |  类型  |                             描述                             | 必须 | 默认值 |
| :---------------: | :----: | :----------------------------------------------------------: | ---- | ------ |
|       file        |  File  |                           传译文件                           | Y    |        |
| webSocketListener | Object | 自定义ws抽象监听类（可使用sdk提供的**SimInterpWebSocketListener**） | Y    |        |

---

### 4. 文件流方式请求

```java
public void send(InputStream inputStream, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException
```

**参数说明**：

|       名称        |  类型  |                             描述                             | 必须 | 默认值 |
| :---------------: | :----: | :----------------------------------------------------------: | ---- | ------ |
|    inputStream    | Object |                       需要传译的文件流                       | Y    |        |
| webSocketListener | Object | 自定义ws抽象监听类（可使用sdk提供的**SimInterpWebSocketListener**） | Y    |        |

### 5. 字节数组方式请求

```java
public void send(byte[] bytes, Closeable closeable, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException
```

## 返回结果参数说明

**参数说明**：

|       名称        |  类型  |                             描述                             | 必须 | 默认值 |
| :---------------: | :----: | :----------------------------------------------------------: | ---- | ------ |
|       bytes       | Object |                    需要传译的文件字节数组                    | Y    |        |
|     closeable     | Object |                         需要关闭的流                         | Y    |        |
| webSocketListener | Object | 自定义ws抽象监听类（可使用sdk提供的**SimInterpWebSocketListener**） | Y    |        |

**返回参数示例（识别响应）：**

```json
{
  "header": {
    "code": 0,
    "message": "success",
    "sid": "aso000e287f@hu17db2d3721205c3882",
    "status": 1
  },
  "payload": {
    "recognition_results": {
      "format": "json",
      "status": 1,
      "text": "eyJiZyI6MjAw...",
      "encoding": "utf8"
    }
  }
}
```

text字段Base64解码后示例（识别响应）：

```json
{
  "bg": 200,
  "ed": 800,
  "ls": false,
  "pgs": "rpl",
  "rg": [
    1,
    1
  ],
  "sn": 2,
  "sub_end": false,
  "ws": [
    {
      "bg": 20,
      "cw": [
        {
          "rl": 0,
          "sc": 0,
          "w": "科大讯飞",
          "wb": 20,
          "wc": 0,
          "we": 40,
          "wp": "n"
        }
      ]
    },
    {
      "bg": 40,
      "cw": [
        {
          "rl": 0,
          "sc": 0,
          "w": "是",
          "wb": 40,
          "wc": 0,
          "we": 60,
          "wp": "n"
        }
      ]
    }
  ]
}
```

**返回参数示例（翻译响应）：**

```json
{
  "header": {
    "code": 0,
    "message": "success",
    "sid": "aso000de6a1@hu17b90e965460212882",
    "status": 1
  },
  "payload": {
    "streamtrans_results": {
      "text": "eyJzcmMiOiLkuID...",
      "seq": "5",
      "status": 1,
      "encoding": "utf8",
      "format": "json",
      "compress": "raw"
    }
  }
}
```

text字段Base64解码后示例（翻译响应）：

```json
{
  "src": "一个面向全球的中文学习爱好者的一个",
  "dst": " A global Chinese learningenthusiasts for a",
  "wb": 10,
  "we": 2480,
  "is_final": 0
}
```

**返回参数示例（合成响应）：**

```json
{
  "header": {
    "code": 0,
    "message": "success",
    "sid": "aso000de6a1@hu17b90e965460212882",
    "status": 1
  },
  "payload": {
    "tts_results": {
      "encoding": "raw",
      "channels": "1",
      "id": "2",
      "seq": "3",
      "audio": "wRUal1l021bJlzu1wI...",
      "sample_rate": "16000",
      "status": "1",
      "bit_depth": "16",
      "type": "0",
      "ced": "68"
    }
  }
}
```

audio字段为合成后的音频片段，采用base64编码，base64解码后写到文件即可。

**返回参数说明（识别）：**

| 参数名                               | 类型   | 描述                                                         |
| ------------------------------------ | ------ | ------------------------------------------------------------ |
| header                               | object | 用于描述平台特性的参数                                       |
| header.code                          | int    | 0表示会话调用成功（并不一定表示服务调用成功，服务是否调用成功以text字段为准） 其它表示会话调用异常，详情请参考[错误码](https://www.xfyun.cn/doc/nlp/simultaneous-interpretation/API.html#错误码) |
| header.message                       | string | 描述信息                                                     |
| header.sid                           | string | 本次会话唯一标识id                                           |
| header.status                        | int    | 流式接口响应状态，可选值为：0,1,2。 第一次响应值为0 中间响应值为1 最后一次响应值为2 |
| payload                              | object | 数据段，用于携带响应的数据                                   |
| payload.recognition_results          | object | 识别响应数据块                                               |
| payload.recognition_results.text     | string | 响应结果，采用base64编码。长度范围：0~1000000                |
| payload.recognition_results.format   | string | 文本格式                                                     |
| payload.recognition_results.encoding | string | 文本编码                                                     |
| payload.recognition_results.status   | int    | 数据状态 0：开始 1：继续 2：结束                             |

text字段base64解码后信息如下（识别），请重点关注：

| 参数名  | 类型   | 描述                                                         |
| ------- | ------ | ------------------------------------------------------------ |
| bg      | int    | 本次输入对应的开始时间戳                                     |
| ed      | int    | 本次输入对应的结束时间戳                                     |
| sn      | int    | 返回结果的序号                                               |
| pgs     | string | 取值为"apd"时表示该片结果是追加到前面的最终结果，取值 为"rpl" 时表示替换前面的部分结果，替换范围为rg字段。默认值为0 |
| rg      | array  | 替换范围                                                     |
| sub_end | bool   | 字句是否结果                                                 |
| ls      | bool   | 是否是最后一片结果                                           |
| wb      | int    | 词在本句中的开始时间，单位是帧，中间结果的wb为0              |
| we      | int    | 词在本句中的结束时间，单位是帧，中间结果的we为0              |
| wp      | string | 词标识 n-普通词 s-顺滑词（语气词） p-标点                    |
| w       | string | 词识别结果                                                   |

**返回参数说明（翻译）：**

| 参数名                               | 类型   | 描述                                                         |
| ------------------------------------ | ------ | ------------------------------------------------------------ |
| header                               | object | 用于描述平台特性的参数                                       |
| header.code                          | int    | 0表示会话调用成功（并不一定表示服务调用成功，服务是否调用成功以text字段为准） 其它表示会话调用异常，详情请参考[错误码](https://www.xfyun.cn/doc/nlp/simultaneous-interpretation/API.html#错误码) |
| header.message                       | string | 描述信息                                                     |
| header.sid                           | string | 本次会话唯一标识id                                           |
| header.status                        | int    | 流式接口响应状态，可选值为：0,1,2。 第一次响应值为0 中间响应值为1 最后一次响应值为2 |
| payload                              | object | 数据段，用于携带响应的数据                                   |
| payload.streamtrans_results          | object | 翻译结果                                                     |
| payload.streamtrans_results.text     | string | 文本数据，响应结果，采用base64编码                           |
| payload.streamtrans_results.encoding | string | 文本编码                                                     |
| payload.streamtrans_results.format   | string | 文本格式                                                     |
| payload.streamtrans_results.status   | int    | 数据状态 0：开始 1：继续 2：结束                             |

text字段base64解码后信息如下（翻译），请重点关注：

| 参数名   | 类型   | 描述                                                         |
| -------- | ------ | ------------------------------------------------------------ |
| src      | string | 原文本                                                       |
| dst      | string | 目标文本                                                     |
| wb       | int    | 起始偏移量，本次结果对应的开始时间戳，识别结果第一个字符相对原始音频绝对时间戳的偏移时间，单位是毫秒 |
| we       | int    | 结束偏移量，本次结果对应的结束时间戳，识别结果第一个字符相对原始音频绝对时间戳的偏移时间，单位是毫秒 |
| is_final | int    | 本次结果是否是最终结果 中间结果：0 确定结果：1               |

**返回参数说明（合成）：**

| 参数名                          | 类型   | 描述                                                         |
| ------------------------------- | ------ | ------------------------------------------------------------ |
| header                          | string | 用于描述平台特性的参数                                       |
| header.code                     | int    | 0表示会话调用成功（并不一定表示服务调用成功，服务是否调用成功以text字段为准） 其它表示会话调用异常，详情请参考[错误码](https://www.xfyun.cn/doc/nlp/simultaneous-interpretation/API.html#错误码) |
| header.message                  | string | 描述信息                                                     |
| header.sid                      | Object | 本次会话唯一标识id                                           |
| header.status                   | int    | 流式接口响应状态，可选值为：0,1,2。 第一次响应值为0 中间响应值为1 最后一次响应值为2 |
| payload                         | obejct | 数据段，用于携带响应的数据                                   |
| payload.tts_results             | object | 合成结果                                                     |
| payload.tts_results.encoding    | string | 音频编码，注意更改生成文件的后缀（如.pcm或.mp3），可选值： raw：合成pcm音频 lame：合成mp3音频 |
| payload.tts_results.sample_rate | int    | 采样率，可选值：16000                                        |
| payload.tts_results.channels    | int    | 声道数，可选值：1                                            |
| payload.tts_results.bit_depth   | int    | 位深，可选值：16                                             |
| payload.tts_results.status      | int    | 数据状态 0：开始 1：继续 2：结束                             |
| payload.tts_results.seq         | int    | 数据序号，标明数据为第几块 。取值范围：0~9999999             |
| payload.tts_results.audio       | string | 响应结果，采用base64编码。长度范围：0~1000000                |
| payload.tts_results.frame_size  | int    | 帧大小，取值范围0~1024                                       |

## 注意事项

1. 采样率为16k、采样深度为16bit、单声道的pcm格式的音频
2. 返回结果是base64编码 , 需要解码后获取结果
3. 目前暂不支持源语种的自动识别
4. 目前支持中文-英文的互译

## 错误处理

捕获异常示例：
```java
try {
    String result = client.send(createReq);
} catch (BusinessException e) {
    System.err.println("业务处理异常：" + e.getMessage());
} catch (IOException e) {
    System.err.println("网络请求失败：" + e.getMessage());
} catch (MalformedURLException e) {
    System.err.println("请求路径异常：" + e.getMessage());
} catch (SignatureException e) {
    System.err.println("签名异常：" + e.getMessage());
} catch (FileNotFoundException e) {
    System.err.println("文件读取异常：" + e.getMessage());
}
```
