# 大模型中文语音识别 API 文档

## [#](https://www.xfyun.cn/doc/spark/spark_zh_iat.html#一、接口说明)一、接口说明

大模型中文语音识别能力，将中文短音频(≤60秒)精准识别成文字，实时返回文字结果，真实还原语音内容。

## [#](https://www.xfyun.cn/doc/spark/spark_zh_iat.html#二、接口demo)二、接口Demo

部分开发语言Demo如下，其他开发语言请参照文档进行开发，欢迎大家到[讯飞开放平台社区 ](https://developer.xfyun.cn/)交流集成经验。

[大模型中文语音识别 Demo java语言](https://openres.xfyun.cn/xfyundoc/2024-05-09/cdf02653-43d6-47b0-a51b-b7a44e59677e/1715234675163/iat_model_zh_java_demo.zip)
[大模型中文语音识别 Demo python语言](https://openres.xfyun.cn/xfyundoc/2024-05-09/2d1f5ba1-57d4-4425-827f-b7617de91dea/1715234711039/大模型中文语音识别.7z)

## [#](https://www.xfyun.cn/doc/spark/spark_zh_iat.html#三、接口要求)三、接口要求

| 内容     | 说明                                                         |
| :------- | :----------------------------------------------------------- |
| 请求协议 | ws[s]（为提高安全性，强烈推荐wss）                           |
| 请求地址 | 中文语音地址：ws[s]: //iat.xf-yun.com/v1                     |
| 接口鉴权 | 签名机制，详情请参照下方[接口鉴权](https://www.xfyun.cn/doc/spark/spark_zh_iat.html#接口鉴权) |
| 字符编码 | UTF-8                                                        |
| 响应格式 | 统一采用JSON格式                                             |
| 开发语言 | 任意，只要可以向讯飞云服务发起HTTP请求的均可                 |
| 音频属性 | 采样率16k或8K、位长16bit、单声道                             |
| 音频格式 | pcm、mp3                                                     |
| 音频长度 | 最长60s                                                      |

## [#](https://www.xfyun.cn/doc/spark/spark_zh_iat.html#四、接口鉴权)四、接口鉴权

### [#](https://www.xfyun.cn/doc/spark/spark_zh_iat.html#鉴权方法)鉴权方法

通过在请求地址后面加上鉴权相关参数的方式。示例url：

```text
https://iat.xf-yun.com/v1?authorization=YXBpX2tleT0ia2V5eHh4eHh4eHg4ZWUyNzkzNDg1MTlleHh4eHh4eHgiLCBhbGdvcml0aG09ImhtYWMtc2hhMjU2IiwgaGVhZGVycz0iaG9zdCBkYXRlIHJlcXVlc3QtbGluZSIsIHNpZ25hdHVyZT0iUzY2RmVxVEpsdmtkK0tmSmcrYTczQkFhYm9jd1JnMnNjS2ZsT05JOG84MD0i&date=Tue%2C%2014%20May%202024%2008%3A46%3A48%20GMT&host=iat.xf-yun.com
```

鉴权参数：

| 参数          | 类型   | 必须 | 说明                                                  | 示例                              |
| :------------ | :----- | :--- | :---------------------------------------------------- | :-------------------------------- |
| host          | string | 是   | 请求主机                                              | iat.xf-yun.com                    |
| date          | string | 是   | 当前时间戳，RFC1123格式                               | Tue, 14 May 2024 08:46:48 GMT     |
| authorization | string | 是   | 使用base64编码的签名相关信息(签名基于hmac-sha256计算) | 参考下方authorization参数生成规则 |

**· date参数生成规则**

date必须是UTC+0或GMT时区，RFC1123格式(Tue, 14 May 2024 08:46:48 GMT)。
服务端会对Date进行时钟偏移检查，最大允许300秒的偏差，超出偏差的请求都将被拒绝。

**· authorization参数生成规则**

1）获取接口密钥APIKey 和 APISecret。
在讯飞开放平台控制台，创建WebAPI平台应用并添加语音听写（流式版）服务后即可查看，均为32位字符串。

2）参数authorization base64编码前（authorization_origin）的格式如下。

```text
api_key="$api_key",algorithm="hmac-sha256",headers="host date request-line",signature="$signature"
```

其中 api_key 是在控制台获取的APIKey，algorithm 是加密算法（仅支持hmac-sha256），headers 是参与签名的参数（见下方注释）。
signature 是使用加密算法对参与签名的参数签名后并使用base64编码的字符串，详见下方。

***注：\* headers是参与签名的参数，请注意是固定的参数名（"host date request-line"），而非这些参数的值。**

3）signature的原始字段(signature_origin)规则如下。

signature原始字段由 host，date，request-line三个参数按照格式拼接成，
拼接的格式为(\n为换行符,’:’后面有一个空格)：

```text
host: $host\ndate: $date\n$request-line
```

假设

```text
请求url = wss://iat.xf-yun.com/v1
date = Tue, 14 May 2024 08:46:48 GMT
```

那么 signature原始字段(signature_origin)则为：

```text
host: iat.xf-yun.com
date: Tue, 14 May 2024 08:46:48 GMT
GET /v1 HTTP/1.1
```

4）使用hmac-sha256算法结合apiSecret对signature_origin签名，获得签名后的摘要signature_sha。

```text
signature_sha=hmac-sha256(signature_origin,$apiSecret)
```

其中 apiSecret 是在控制台获取的APISecret

5）使用base64编码对signature_sha进行编码获得最终的signature。

```text
signature=base64(signature_sha)
```

假设

```text
APISecret = secretxxxxxxxx2df7900c09xxxxxxxx	
date = Tue, 14 May 2024 08:46:48 GMT
```

则signature为

```text
signature=S66FeqTJlvkd+KfJg+a73BAabocwRg2scKflONI8o80=
```

6）根据以上信息拼接authorization base64编码前（authorization_origin）的字符串，示例如下。

```text
api_key="keyxxxxxxxx8ee279348519exxxxxxxx", algorithm="hmac-sha256", headers="host date request-line", signature="S66FeqTJlvkd+KfJg+a73BAabocwRg2scKflONI8o80="
```

*注：* headers是参与签名的参数，请注意是固定的参数名（"host date request-line"），而非这些参数的值。

7）最后再对authorization_origin进行base64编码获得最终的authorization参数。

```text
authorization = base64(authorization_origin)
示例：
https://iat.xf-yun.com/v1?authorization=YXBpX2tleT0ia2V5eHh4eHh4eHg4ZWUyNzkzNDg1MTlleHh4eHh4eHgiLCBhbGdvcml0aG09ImhtYWMtc2hhMjU2IiwgaGVhZGVycz0iaG9zdCBkYXRlIHJlcXVlc3QtbGluZSIsIHNpZ25hdHVyZT0iUzY2RmVxVEpsdmtkK0tmSmcrYTczQkFhYm9jd1JnMnNjS2ZsT05JOG84MD0i&date=Tue%2C%2014%20May%202024%2008%3A46%3A48%20GMT&host=iat.xf-yun.com
```

## [#](https://www.xfyun.cn/doc/spark/spark_zh_iat.html#五、数据传输接收与请求、返回示例)五、数据传输接收与请求、返回示例

### [#](https://www.xfyun.cn/doc/spark/spark_zh_iat.html#_1、数据传输接收)1、数据传输接收

握手成功后客户端和服务端会建立Websocket连接，客户端通过Websocket连接可以同时上传和接收数据。
当服务端有识别结果时，会通过Websocket连接推送识别结果到客户端。

发送数据时，如果间隔时间太短，可能会导致引擎识别有误。
建议每次发送音频间隔40ms，每次发送音频字节数（即java示例demo中的frameSize）为一帧音频大小的整数倍。

```java
//连接成功，开始发送数据
int frameSize = 1280; //每一帧音频大小的整数倍，请注意不同音频格式一帧大小字节数不同，可参考下方建议
int intervel = 40;
int status = 0;  // 音频的状态
try (FileInputStream fs = new FileInputStream(file)) {
    byte[] buffer = new byte[frameSize];
    // 发送音频
```

我们建议：未压缩的PCM格式，每次发送音频间隔40ms，每次发送音频字节数1280B；

### [#](https://www.xfyun.cn/doc/spark/spark_zh_iat.html#_2、请求json示例)2、请求json示例

第一帧数据：

```json
{
  "header": {
    "app_id": "your_appid",
    "status": 0
  },
  "parameter": {
    "iat": {
      "domain": "slm",
      "language": "zh_cn",
      "accent": "mandarin",
      "eos": 6000,
      "vinfo": 1,
      "dwa": "wpgs",
      "result": {
        "encoding": "utf8",
        "compress": "raw",
        "format": "json"
      }
    }
  },
  "payload": {
    "audio": {
      "encoding": "raw",
      "sample_rate": 16000,
      "channels": 1,
      "bit_depth": 16,
      "seq": 1,
      "status": 0,
      "audio": "AAAAAP..."
    }
  }
}
```

中间帧数据：

```json
{
  "header": {
    "app_id": "your_appid",
    "status": 1
  },
  "payload": {
    "audio": {
      "encoding": "raw",
      "sample_rate": 16000,
      "channels": 1,
      "bit_depth": 16,
      "seq": 2,
      "status": 1,
      "audio": "AAAAAA..."
    }
  }
}
```

最后一帧数据：

```json
{
  "header": {
    "app_id": "your_appid",
    "status": 2
  },
  "payload": {
    "audio": {
      "encoding": "raw",
      "sample_rate": 16000,
      "channels": 1,
      "bit_depth": 16,
      "seq": 591,
      "status": 2,
      "audio": ""
    }
  }
}
```

### [#](https://www.xfyun.cn/doc/spark/spark_zh_iat.html#_3、返回json示例)3、返回json示例

第一帧返回数据示例：

```json
{
  "header": {
    "code": 0,
    "message": "success",
    "sid": "iat000e0044@hu18f5b16b0330324...",
    "status": 0
  }
}
```

中间帧返回数据示例：

```json
{
  "header": {
    "code": 0,
    "message": "success",
    "sid": "iat000e0044@hu18f5b16b033032...",
    "status": 1
  },
  "payload": {
    "result": {
      "compress": "raw",
      "encoding": "utf8",
      "format": "json",
      "seq": 2,
      "status": 1,
      "text": "eyJzbiI6Miwib..."
    }
  }
}
```

最后一帧返回数据示例：

```json
{
  "header": {
    "code": 0,
    "message": "success",
    "sid": "iat000e0044@hu18f5b16b033032...",
    "status": 2
  },
  "payload": {
    "result": {
      "compress": "raw",
      "encoding": "utf8",
      "format": "json",
      "seq": 76,
      "status": 2,
      "text": "eyJzbiI6NzYs..."
    }
  }
}
```

## [#](https://www.xfyun.cn/doc/spark/spark_zh_iat.html#六、参数说明)六、参数说明

### [#](https://www.xfyun.cn/doc/spark/spark_zh_iat.html#_1、请求参数说明)1、请求参数说明

| 参数名                    | 类型   | 必传 | 描述                                        |
| ------------------------- | ------ | ---- | ------------------------------------------- |
| header                    | object | 是   | 用于上传平台参数                            |
| header.app_id             | string | 是   | 在平台申请的appid信息                       |
| header.status             | int    | 是   | 音频传输状态 0:首帧 1：中间帧 2:最后一帧    |
| parameter                 | object | 是   | 用于上传服务特性参数                        |
| parameter.iat             | object | 是   | 服务名称，大模型中文语音识别                |
| parameter.iat.domain      | string | 是   | 指定访问的领域 slm                          |
| parameter.iat.language    | string | 是   | 语种 zh_cn                                  |
| parameter.iat.accent      | string | 是   | 方言 mandarin（代表普通话）                 |
| parameter.iat.eos         | int    | 否   | 静音多少秒停止识别 如6000毫秒               |
| parameter.iat.vinfo       | int    | 否   | 句子级别帧对齐                              |
| parameter.iat.dwa         | string | 否   | 流式识别PGS 返回速度更快，仅中文支持        |
| parameter.iat.result      | obejct | 否   | 响应数据字段                                |
| payload                   | object | 是   | 请求数据携带                                |
| payload.audio             | object | 是   | 音频数据模块                                |
| payload.audio.encoding    | string | 否   | 音频编码 raw或lame（代表pcm和mp3格式）      |
| payload.audio.sample_rate | int    | 否   | 音频采样率 16000, 8000                      |
| payload.audio.channels    | int    | 否   | 音频声道 1                                  |
| payload.audio.bit_depth   | int    | 否   | 音频位深 16                                 |
| payload.audio.seq         | int    | 否   | 数据序号 0-999999                           |
| payload.audio.status      | int    | 否   | 取值范围为：0（开始）、1（继续）、2（结束） |
| payload.audio.audio       | string | 是   | 音频数据base64 音频时长不要超过60秒         |

### [#](https://www.xfyun.cn/doc/spark/spark_zh_iat.html#_2、返回参数说明)2、返回参数说明

| 参数名                  | 类型   | 描述                                                         |
| ----------------------- | ------ | ------------------------------------------------------------ |
| header                  | object | 协议头部                                                     |
| header.message          | string | 描述信息                                                     |
| header.code             | int    | 返回码 0表示会话调用成功（并不一定表示服务调用成功，服务是否调用成功以text字段中的ret为准） 其它表示会话调用异常 |
| header.sid              | string | 本次会话id                                                   |
| header.status           | int    | 数据状态 0:开始, 1:继续, 2:结束                              |
| payload                 | object | 数据段，用于携带响应的数据                                   |
| payload.result.compress | string | 文本压缩格式                                                 |
| payload.result.encoding | string | 文本编码                                                     |
| payload.result.format   | string | 文本格式                                                     |
| payload.result.seq      | int    | 数据序号 0-999999                                            |
| payload.result.status   | int    | 0:开始, 1:继续, 2:结束                                       |
| payload.result.text     | string | 听写数据文本 base64编码                                      |

**text字段base64解码后参数说明请：**

| 参数                                | 类型       | 描述                                                         |
| ----------------------------------- | ---------- | ------------------------------------------------------------ |
| sn                                  | int        | 返回结果的序号                                               |
| ls                                  | bool       | 是否是最后一片结果                                           |
| bg                                  | int        | 保留字段，无需关心                                           |
| ed                                  | int        | 保留字段，无需关心                                           |
| ws                                  | array      | 听写结果                                                     |
| ws.bg                               | int        | 起始的端点帧偏移值，单位：帧（1帧=10ms） 注：以下两种情况下bg=0，无参考意义： 1)返回结果为标点符号或者为空；2)本次返回结果过长。 |
| ws.cw                               | array      | 中文分词                                                     |
| ws.cw.w                             | string     | 字词                                                         |
| ws.cw.lg                            | string     | 源语种                                                       |
| ws.cw.其他字段 sc/wb/wc/we/wp/ng/ph | int/string | 均为保留字段，无需关心。如果解析sc字段，建议float与int数据类型都做兼容 |

## [#](https://www.xfyun.cn/doc/spark/spark_zh_iat.html#七、常见问题)七、常见问题

### [#](https://www.xfyun.cn/doc/spark/spark_zh_iat.html#大模型中文语音识别与语音听写的区别)大模型中文语音识别与语音听写的区别？

> 答：大模型中文语音识别基于大模型进行的训练，数据量更丰富，听写效果上限更高，未来发展性更强。

### [#](https://www.xfyun.cn/doc/spark/spark_zh_iat.html#大模型中文语音识别支持多少分钟的音频识别)大模型中文语音识别支持多少分钟的音频识别？

> 答：支持1分钟以内的音频识别。





# 方言大模型 协议

## [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#一、-方言大模型描述)一、 方言大模型描述

方言大模型，支持普通话，简单英语和202种方言全免切，无需显示指定语种。

## [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#二、接口demo)二、接口Demo

部分开发语言Demo如下，其他开发语言请参照文档进行开发，欢迎大家到[讯飞开放平台社区 ](https://developer.xfyun.cn/)交流集成经验。

[大模型方言语音识别 Demo java语言](https://xfyun-doc.xfyun.cn/lc-sp-spark_mucl_cn_iat_demo_java-1735546021682.zip)
[大模型方言语音识别 Demo python语言](https://xfyun-doc.xfyun.cn/lc-sp-spark_slm_iat_python-1735526511442.zip)

## [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#三、接口要求)三、接口要求

| 内容     | 说明                                                         |
| :------- | :----------------------------------------------------------- |
| 请求协议 | ws[s]（为提高安全性，强烈推荐wss）                           |
| 请求地址 | 方言识别地址：ws[s]: //iat.cn-huabei-1.xf-yun.com/v1         |
| 接口鉴权 | 签名机制，详情请参照下方[接口鉴权](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#接口鉴权) |
| 字符编码 | UTF-8                                                        |
| 响应格式 | 统一采用JSON格式                                             |
| 开发语言 | 任意，只要可以向讯飞云服务发起HTTP请求的均可                 |
| 音频属性 | 采样率16k或8K、位长16bit、单声道                             |
| 音频格式 | pcm                                                          |
| 音频长度 | 最长60s                                                      |

## [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#四、接口鉴权)四、接口鉴权

通过在请求地址后面加上鉴权相关参数的方式。示例url：

```text
wss://iat.cn-huabei-1.xf-yun.com/v1?authorization=YXBpX2tleT0iYTFkNjc0NmRkMWJiMTlmMTlkNTkyMDVhZDAwMDc0NjQiLCBhbGdvcml0aG09ImhtYWMtc2hhMjU2IiwgaGVhZGVycz0iaG9zdCBkYXRlIHJlcXVlc3QtbGluZSIsIHNpZ25hdHVyZT0iRDZFQzFCRHk5Wjl2Y1RqdE55aW0wenNFZi9LQUxIQmg1TlNxYVcwMUNJbz0i&amp;date=Mon%2C+30+Dec+2024+07%3A40%3A12+GMT&amp;host=iat.cn-huabei-1.xf-yun.com
```

### [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#鉴权方法)鉴权方法

| 参数          | 类型   | 必须 | 说明                                                  | 示例                              |
| :------------ | :----- | :--- | :---------------------------------------------------- | :-------------------------------- |
| host          | string | 是   | 请求主机                                              | iat.xf-yun.com                    |
| date          | string | 是   | 当前时间戳，RFC1123格式                               | Tue, 14 May 2024 08:46:48 GMT     |
| authorization | string | 是   | 使用base64编码的签名相关信息(签名基于hmac-sha256计算) | 参考下方authorization参数生成规则 |

**· date参数生成规则**

date必须是UTC+0或GMT时区，RFC1123格式(Tue, 14 May 2024 08:46:48 GMT)。 服务端会对Date进行时钟偏移检查，最大允许300秒的偏差，超出偏差的请求都将被拒绝。

**· authorization参数生成规则**

1）获取接口密钥APIKey 和 APISecret。 在讯飞开放平台控制台，创建WebAPI平台应用并添加语音听写（流式版）服务后即可查看，均为32位字符串。

2）参数authorization base64编码前（authorization_origin）的格式如下。

```text
api_key="$api_key",algorithm="hmac-sha256",headers="host date request-line",signature="$signature"
```

其中 api_key 是在控制台获取的APIKey，algorithm 是加密算法（仅支持hmac-sha256），headers 是参与签名的参数（见下方注释）。 signature 是使用加密算法对参与签名的参数签名后并使用base64编码的字符串，详见下方。

***注：\* headers是参与签名的参数，请注意是固定的参数名（"host date request-line"），而非这些参数的值。**

3）signature的原始字段(signature_origin)规则如下。

signature原始字段由 host，date，request-line三个参数按照格式拼接成， 拼接的格式为(\n为换行符,’:’后面有一个空格)：

```text
host: $host\ndate: $date\n$request-line
```

假设

```text
请求url = wss://iat.cn-huabei-1.xf-yun.com/v1
date = Tue, 14 May 2024 08:46:48 GMT
```

那么 signature原始字段(signature_origin)则为：

```text
host: wss://iat.cn-huabei-1.xf-yun.com
date: Tue, 14 May 2024 08:46:48 GMT
GET /v1 HTTP/1.1
```

4）使用hmac-sha256算法结合apiSecret对signature_origin签名，获得签名后的摘要signature_sha。

```text
signature_sha=hmac-sha256(signature_origin,$apiSecret)
```

其中 apiSecret 是在控制台获取的APISecret

5）使用base64编码对signature_sha进行编码获得最终的signature。

```text
signature=base64(signature_sha)
```

假设

```text
APISecret = secretxxxxxxxx2df7900c09xxxxxxxx	
date = Tue, 14 May 2024 08:46:48 GMT
```

则signature为

```text
signature=S66FeqTJlvkd+KfJg+a73BAabocwRg2scKflONI8o80=
```

6）根据以上信息拼接authorization base64编码前（authorization_origin）的字符串，示例如下。

```text
api_key="keyxxxxxxxx8ee279348519exxxxxxxx", algorithm="hmac-sha256", headers="host date request-line", signature="S66FeqTJlvkd+KfJg+a73BAabocwRg2scKflONI8o80="
```

*注：* headers是参与签名的参数，请注意是固定的参数名（"host date request-line"），而非这些参数的值。

7）最后再对authorization_origin进行base64编码获得最终的authorization参数。

```text
authorization = base64(authorization_origin)
示例：
wss://iat.cn-huabei-1.xf-yun.com/v1?authorization=YXBpX2tleT0iYTFkNjc0NmRkMWJiMTlmMTlkNTkyMDVhZDAwMDc0NjQiLCBhbGdvcml0aG09ImhtYWMtc2hhMjU2IiwgaGVhZGVycz0iaG9zdCBkYXRlIHJlcXVlc3QtbGluZSIsIHNpZ25hdHVyZT0iRDZFQzFCRHk5Wjl2Y1RqdE55aW0wenNFZi9LQUxIQmg1TlNxYVcwMUNJbz0i&amp;date=Mon%2C+30+Dec+2024+07%3A40%3A12+GMT&amp;host=iat.cn-huabei-1.xf-yun.com
```

## [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#五、数据传输接收与请求、返回示例)五、数据传输接收与请求、返回示例

### [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#_5-1-数据传输接收)5.1. 数据传输接收

握手成功后客户端和服务端会建立Websocket连接，客户端通过Websocket连接可以同时上传和接收数据。 当服务端有识别结果时，会通过Websocket连接推送识别结果到客户端。

发送数据时，如果间隔时间太短，可能会导致引擎识别有误。 建议每次发送音频间隔40ms，每次发送音频字节数（即java示例demo中的frameSize）为一帧音频大小的整数倍。

```java
//连接成功，开始发送数据
int frameSize = 1280; //每一帧音频大小的整数倍，请注意不同音频格式一帧大小字节数不同，可参考下方建议
int intervel = 40;
int status = 0;  // 音频的状态
try (FileInputStream fs = new FileInputStream(file)) {
    byte[] buffer = new byte[frameSize];
    // 发送音频
```

我们建议：未压缩的PCM格式，每次发送音频间隔40ms，每次发送音频字节数1280B；

### [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#_5-2-请求协议示例)5.2. 请求协议示例

```text
{
    "header": {
        "app_id": "123456",
    },
    "parameter": {
        "iat": {
            "language": "zh_cn",
            "accent": "mulacc",
            "domain": "slm",
            "eos": 1800,
            "dwa": "wpgs",
            "ptt": 1,
            "nunum": 1,
            "ltc": 0,
            "result": {
                "encoding": "utf8",
                "compress": "raw",
                "format": "json"
            }
        }
    },
    "payload": {
        "audio": {
            "encoding": "raw",
            "sample_rate": 16000,
            "channels": 1,
            "bit_depth": 16,
            "status": 0,
            "seq": 0,
            "audio": "AAAAAP...",
        }
    }
}
```

**协议结构说明**

| 字段      | 含义         | 类型   | 说明                                                         |
| :-------- | :----------- | :----- | :----------------------------------------------------------- |
| header    | 协议头部     | Object | 协议头部，用于描述平台特性的参数，详见 5.2 平台参数。        |
| parameter | 能力参数     | Object | AI 特性参数，用于控制 AI 引擎特性的开关。                    |
| iat       | 服务别名     | Object |                                                              |
| result    | 响应数据控制 | Object | 数据格式预期，用于描述返回结果的编码等相关约束，不同的数据类型，约束维度亦不相同，此 object 与响应结果存在对应关系。 |
| payload   | 输入数据段   | Object | 数据段，携带请求的数据。                                     |
| audio     | 输入数据     | Object | 输入数据，详见 5.2 请求数据。                                |

### [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#_5-3-请求参数)5.3. 请求参数

#### [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#_5-3-1-平台参数)5.3.1. 平台参数

| 字段   | 含义                                       | 类型   | 是否必传 |
| :----- | :----------------------------------------- | :----- | :------- |
| app_id | 在平台申请的app id信息                     | string | 是       |
| status | 请求状态，可选值为：0-开始、1-继续、2-结束 | int    | 是       |

#### [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#_5-3-2-服务特性参数)5.3.2. 服务特性参数

**功能参数**

| 功能标识 | 功能描述                                                     | 数据类型 | 取值范围                                                     | 必填 | 默认值 |
| :------- | :----------------------------------------------------------- | :------- | :----------------------------------------------------------- | :--- | :----- |
| language | 语种设置                                                     | String   | 仅支持：zh_cn                                                | 是   | zh_cn  |
| accent   | 方言设置                                                     | String   | 仅支持：mulacc 支持202种方言免切换，具体可查看[支持方言明细](https://xfyun-doc.xfyun.cn/lc-sp-方言大模型支持方言明细-1736922316173.xlsx) | 是   | mulacc |
| domain   | 应用领域设置                                                 | String   | 仅支持：slm                                                  | 是   | slm    |
| eos      | 尾静音截断：引擎判定结束的时间，连续检测给定时间长度的音频，均为静音，则引擎停止识别 | int      | 最小值:600, 最大值:60000                                     | 否   | 1800   |
| nbest    | 句子多候选：通过参数控制输出的n个最优的结果，而不是1个       | int      | 最小值:0, 最大值:5                                           | 否   | 0      |
| wbest    | 词级多候选：通过控制输出槽内的n个结果，而不是1个             | int      | 最小值:0, 最大值:5                                           | 否   | 0      |
| vinfo    | 句子级别帧对齐:给出一次会话中，子句的vad边界信息             | int      | 0:不返回vad信息, 1:返回vad信息                               | 否   | 0      |
| dwa      | 流式识别PGS：流式识别功能，打开后，会话过程中实时给出语音识别的结果，而不是子句结束时才给结果 | string   | 最小长度:0, 最大长度:10                                      | 否   |        |
| ptt      | 标点预测：在语音识别结果中增加标点符号                       | int      | 0:关闭, 1:开启                                               | 否   | 1      |
| smth     | 顺滑功能：将语音识别结果中的顺滑词（语气词、叠词）进行标记，业务侧通过标记过滤语气词最终展现识别结果 | int      | 0:关闭, 1:开启                                               | 否   | 0      |
| nunum    | 数字规整：将语音识别结果中的原始文字串转为相应的阿拉伯数字或者符号 | int      | 0:关闭, 1:开启                                               | 否   | 1      |
| opt      | 是否输出属性                                                 | int      | 0:json格式输出，不带属性, 1:文本格式输出，不带属性, 2:json格式输出，带文字属性"wp":"n"和标点符号属性"wp":"p" | 否   |        |
| dhw      | 会话热词，支持utf-8和gb2312； 取值样例：“dhw=db2312;你好\|大家”（对应gb2312编码）； “dhw=utf-8;你好\|大家”（对应utf-8编码） | string   | 最小长度:0, 最大长度:1024                                    | 否   |        |
| rlang    | 返回字体指定zh-cn/zh-hk/zh-mo/zh-tw，服务默认是简体字        | string   | 最小长度:0, 最大长度:100                                     | 否   |        |
| ltc      | 是否进行中英文筛选                                           | int      | 1:不进行筛选, 2:只出中文, 3:只出英文                         | 否   |        |

**响应数据参数**

result 段的参数（默认返回）

| 字段     | 含义         | 数据类型 | 取值范围         | 默认值 | 说明           | 必填 |
| :------- | :----------- | :------- | :--------------- | :----- | :------------- | :--- |
| encoding | 文本编码     | string   | utf8, gb2312     | utf8   | 取值范围可枚举 | 否   |
| compress | 文本压缩格式 | string   | raw, gzip        | raw    | 取值范围可枚举 | 否   |
| format   | 文本格式     | string   | plain, json, xml | json   | 取值范围可枚举 | 否   |

#### [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#_5-3-3-请求数据)5.3.3. 请求数据

audio（默认请求）

| 字段        | 含义     | 数据类型 | 取值范围                                          | 默认值   | 说明                                        | 必填 |
| :---------- | :------- | :------- | :------------------------------------------------ | :------- | :------------------------------------------ | :--- |
| encoding    | 音频编码 | string   | lame（即mp3格式）, speex, opus, opus-wb, speex-wb | speex-wb | 取值范围可枚举                              | 否   |
| sample_rate | 采样率   | int      | 16000, 8000                                       | 16000    | 音频采样率，可枚举                          | 否   |
| channels    | 声道数   | int      | 1, 2                                              | 1        | 声道数，可枚举                              | 否   |
| bit_depth   | 位深     | int      | 16, 8                                             | 16       | 单位bit，可枚举                             | 否   |
| status      | 数据状态 | int      | 0:开始, 1:继续, 2:结束                            | 0        | 取值范围为：0（开始）、1（继续）、2（结束） | 否   |
| seq         | 数据序号 | int      | 最小值:0, 最大值:9999999                          | 0        | 标明数据为第几块                            | 否   |
| audio       | 音频数据 | string   | 最小尺寸:0B, 最大尺寸:10485760B                   |          | 音频大小：0-10M                             | 是   |
| frame_size  | 帧大小   | int      | 最小值:0, 最大值:1024                             | 0        | 帧大小，默认0                               | 否   |

### [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#_5-4-响应)5.4. 响应

#### [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#_5-4-1-响应协议示例)5.4.1. 响应协议示例

```text
{
    "header": {
        "code": 0,
        "message": "success",
        "sid": "ase000e065f@dx193f81b97fb750c882",
        "status": 2
    },
    "payload": {
        "result": {
            "encoding": "utf8",
            "compress": "raw",
            "format": "json",
            "status": 0,
            "seq": 0,
            "text": "eyJzbiI6MSwibHMiOnRydWUsImJnIjowLCJlZCI6MCwid3MiOlt7ImJnIjowLCJjdyI6W3……"
        }
    }
}
```

**协议结构说明**

| 字段    | 含义       | 类型   | 说明                                                    |
| :------ | :--------- | :----- | :------------------------------------------------------ |
| header  | 协议头部   | Object | 协议头部，用于描述平台特性的参数，详见 5.5.1 平台参数。 |
| payload | 响应数据块 | Object | 数据段，携带响应的数据。                                |
| result  | 响应数据块 | Object | 输出数据，详见 5.5.3 响应数据参数。                     |

### [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#_5-5-响应参数)5.5. 响应参数

#### [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#_5-5-1-平台参数)5.5.1. 平台参数

| 字段    | 含义                            | 类型   | 是否必选 |
| :------ | :------------------------------ | :----- | :------- |
| code    | 返回码，0表示成功，其它表示异常 | int    | 是       |
| message | 错误描述                        | string | 是       |
| sid     | 本次会话的id                    | string | 是       |

#### [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#_5-5-2-响应数据参数)5.5.2. 响应数据参数

result（默认返回）

| 字段     | 含义         | 数据类型 | 取值范围                     | 默认值 | 说明                                        | 必填 |
| :------- | :----------- | :------- | :--------------------------- | :----- | :------------------------------------------ | :--- |
| encoding | 文本编码     | string   | utf8, gb2312                 | utf8   | 取值范围可枚举                              | 否   |
| compress | 文本压缩格式 | string   | raw, gzip                    | raw    | 取值范围可枚举                              | 否   |
| format   | 文本格式     | string   | plain, json, xml             | json   | 取值范围可枚举                              | 否   |
| status   | 数据状态     | int      | 0:开始, 1:继续, 2:结束       | 0      | 取值范围为：0（开始）、1（继续）、2（结束） | 否   |
| seq      | 数据序号     | int      | 最小值:0, 最大值:9999999     | 0      |                                             | 是   |
| text     | 文本数据     | string   | 最小长度:0, 最大长度:1000000 |        |                                             | 是   |

#### [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#_5-5-3-响应数据解析)5.5.3. 响应数据解析

result.text

**示例：**

```text
{
    "bg": 0,
    "ed": 0,
    "ls": false,
    "sn": 15,
    "pgs": rpl,
    "rst": pgs,
    "rg": [
        1,14
    ],
    "ws": [
        {
            "wb": null,
            "wc": null,
            "we": null,
            "wp": null,
            "cw": [
                {
                    "sc": 0,
                    "ph": null,
                    "w": 科大讯飞
                }
            ]
        }
    ]
}
```

**解析：**

|      | 字段 | 含义 | 数据类型 | 取值范围    | 默认值 | 说明                                                         |
| :--- | :--- | :--- | :------- | :---------- | :----- | :----------------------------------------------------------- |
|      | bg   |      | float    | --          | 140    | 本次识别结果的语音开始端点，以ms为单位                       |
|      | ed   |      | float    | --          | 2280   | 本次识别结果的语音结束端点，以ms为单位                       |
|      | ls   |      | boolean  | true:false: | false  | 本次结果是否为最后一块结果                                   |
|      | sn   |      | float    | --          | 1      | 本次识别结果在总体识别结果中的序号                           |
|      | pgs  |      | string   | --          | rpl    | 流式识别场景下，本次识别结果操作方式，rpl 为替换前一次识别结果，apd为替换前一次识别结果 |
|      | rst  |      | string   | --          | rlt    | 流式识别场景下，本地识别结果的类型，rlt为子句最终结果，pgs 为子句过程的流式结果 |
|      | rg   |      | array    | --          | []     | 流式识别场景下，结果标识字段，字段为2维数组，第一个值为 sn 的值，第二个为替换子句的终止sn号 |
|      | ws   |      | array    | --          | []     | 本次识别结果的内容，是一个多维数组，每个值表示一个槽         |

## [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#六、错误码列表)六、错误码列表

错误码示例：

```text
{
    "code":10003, // 平台通用错误码，详细信息请参照 5.1 平台通用错误码
    "message":"WrapperInitErr;errno=101", // errno 为引擎错误码
    "sid":"ocr00088c7d@dx170194697e9a11d902"
}
```

### [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#_6-1-平台通用错误码)6.1. 平台通用错误码

| 错误码 | 错误描述                                     | 说明                                         | 处理策略                                                     |
| :----- | :------------------------------------------- | :------------------------------------------- | :----------------------------------------------------------- |
| 10009  | input invalid data                           | 输入数据非法                                 | 检查输入数据                                                 |
| 10010  | service license not enough                   | 没有授权许可或授权数已满                     | 提交工单                                                     |
| 10019  | service read buffer timeout, session timeout | session超时                                  | 检查是否数据发送完毕但未关闭连接                             |
| 10043  | Syscall AudioCodingDecode error              | 音频解码失败                                 | 检查aue参数，如果为speex，请确保音频是speex音频并分段压缩且与帧大小一致 |
| 10114  | session timeout                              | session 超时                                 | 会话时间超时，检查是否发送数据时间超过了60s                  |
| 10139  | invalid param                                | 参数错误                                     | 检查参数是否正确                                             |
| 10160  | parse request json error                     | 请求数据格式非法                             | 检查请求数据是否是合法的json                                 |
| 10161  | parse base64 string error                    | base64解码失败                               | 检查发送的数据是否使用base64编码了                           |
| 10163  | param validate error:...                     | 参数校验失败                                 | 具体原因见详细的描述                                         |
| 10200  | read data timeout                            | 读取数据超时                                 | 检查是否累计10s未发送数据并且未关闭连接                      |
| 10222  | context deadline exceeded                    | 1.上传的数据超过了接口上限； 2.SSL证书无效； | 1.检查接口上传的数据（文本、音频、图片等）是否超越了接口的最大限制，可到相应的接口文档查询具体的上限； 2. 请将log导出发到工单：https://console.xfyun.cn/workorder/commit； |
| 10223  | RemoteLB: can't find valued addr             | lb 找不到节点                                | 提交工单                                                     |
| 10313  | invalid appid                                | appid和apikey不匹配                          | 检查appid是否合法                                            |
| 10317  | invalid version                              | 版本非法                                     | 请到控制台提交工单联系技术人员                               |
| 10700  | not authority                                | 引擎异常                                     | 按照报错原因的描述，对照开发文档检查输入输出，如果仍然无法排除问题，请提供sid以及接口返回的错误信息，到控制台提交工单联系技术人员排查。 |
| 11200  | auth no license                              | 功能未授权                                   | 请先检查appid是否正确，并且确保该appid下添加了相关服务。若没问题，则按照如下方法排查。 1. 确认总调用量是否已超越限制，或者总次数授权已到期，若已超限或者已过期请联系商务人员。 2. 查看是否使用了未授权的功能，或者授权已过期。 |
| 11201  | auth no enough license                       | 该APPID的每日交互次数超过限制                | 根据自身情况提交应用审核进行服务量提额，或者联系商务购买企业级正式接口，获得海量服务量权限以便商用。 |
| 11503  | server error :atmos return an error data     | 服务内部响应数据错误                         | 提交工单                                                     |
| 11502  | server error: too many datas in resp         | 服务配置错误                                 | 提交工单                                                     |

## [#](https://www.xfyun.cn/doc/spark/spark_slm_iat.html#七、常见问题)七、常见问题

**方言大模型的APIKey在哪里查询到？**

> 答：控制台--我的应用---找到对应应用的方言识别大模型服务---即能查看到。

**方言大模型支持多少方言？**

> 答：方言大模型除支持中英文外，还支持202种方言免切换，具体支持明细可点击下载查看-[方言大模型支持方言明细 ](https://xfyun-doc.xfyun.cn/lc-sp-方言大模型支持方言明细-1736922316173.xlsx)。

**方言大模型最长支持多少秒之内的音频？**

> 答：支持识别60s之内的音频。







# 大模型多语种语音识别 API 文档

## [#](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#一、接口说明)一、接口说明

大模型多语种语音识别能力，将多语种短音频(≤60秒)精准识别成文字，实时返回文字结果，真实还原语音内容。

## [#](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#二、接口demo)二、接口Demo

部分开发语言Demo如下，其他开发语言请参照文档进行开发，欢迎大家到[讯飞开放平台社区 ](https://developer.xfyun.cn/)交流集成经验。

[大模型多语种语音识别 Demo java语言](https://openres.xfyun.cn/xfyundoc/2024-05-14/b6359052-b439-4024-9305-ec760ec30262/1715677394126/iat_model_mul_java_demo.zip)
[大模型多语种语音识别 Demo python语言](https://openres.xfyun.cn/xfyundoc/2024-05-14/a8cb429e-413e-45db-a9e5-c3d533e81e5d/1715677469709/spark_mucl_cn_iat.zip)

## [#](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#三、接口要求)三、接口要求

| 内容     | 说明                                                         |
| :------- | :----------------------------------------------------------- |
| 请求协议 | ws[s]（为提高安全性，强烈推荐wss）                           |
| 请求地址 | 多语种语音地址：ws[s]: //iat.cn-huabei-1.xf-yun.com/v1       |
| 接口鉴权 | 签名机制，详情请参照下方[接口鉴权](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#接口鉴权) |
| 字符编码 | UTF-8                                                        |
| 响应格式 | 统一采用JSON格式                                             |
| 开发语言 | 任意，只要可以向讯飞云服务发起HTTP请求的均可                 |
| 音频属性 | 采样率16k或8K、位长16bit、单声道                             |
| 音频格式 | pcm、mp3                                                     |
| 音频长度 | 最长60s                                                      |

## [#](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#四、接口鉴权)四、接口鉴权

### [#](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#鉴权方法)鉴权方法

通过在请求地址后面加上鉴权相关参数的方式。示例url：

```text
websocket url: wss://iat.cn-huabei-1.xf-yun.com/v1?authorization=YXBpX2tleT0iNDRkNDQ5YTcxMTQ3NDg2MWIzOTcyZjIwNGYxODFkMmMiLCBhbGdvcml0aG09ImhtYWMtc2hhMjU2IiwgaGVhZGVycz0iaG9zdCBkYXRlIHJlcXVlc3QtbGluZSIsIHNpZ25hdHVyZT0iM2RiU1FUcEZpU0dkOUNRZ2xGQWpRc2tnK3JYU0UwbjZqdXd4alhUR0lPWT0i&date=Tue%2C+26+Nov+2024+08%3A44%3A08+GMT&host=iat.cn-huabei-1.xf-yun.com
```

鉴权参数：

| 参数          | 类型   | 必须 | 说明                                                  | 示例                              |
| :------------ | :----- | :--- | :---------------------------------------------------- | :-------------------------------- |
| host          | string | 是   | 请求主机                                              | iat.cn-huabei-1.xf-yun.com        |
| date          | string | 是   | 当前时间戳，RFC1123格式                               | Wed, 10 Jul 2019 07:35:43 GMT     |
| authorization | string | 是   | 使用base64编码的签名相关信息(签名基于hmac-sha256计算) | 参考下方authorization参数生成规则 |

**· date参数生成规则**

date必须是UTC+0或GMT时区，RFC1123格式(Wed, 10 Jul 2019 07:35:43 GMT)。
服务端会对Date进行时钟偏移检查，最大允许300秒的偏差，超出偏差的请求都将被拒绝。

**· authorization参数生成规则**

1）获取接口密钥APIKey 和 APISecret。
在讯飞开放平台控制台，创建WebAPI平台应用并添加语音听写（流式版）服务后即可查看，均为32位字符串。

2）参数authorization base64编码前（authorization_origin）的格式如下。

```text
api_key="$api_key",algorithm="hmac-sha256",headers="host date request-line",signature="$signature"
```

其中 api_key 是在控制台获取的APIKey，algorithm 是加密算法（仅支持hmac-sha256），headers 是参与签名的参数（见下方注释）。
signature 是使用加密算法对参与签名的参数签名后并使用base64编码的字符串，详见下方。

***注：\* headers是参与签名的参数，请注意是固定的参数名（"host date request-line"），而非这些参数的值。**

3）signature的原始字段(signature_origin)规则如下。

signature原始字段由 host，date，request-line三个参数按照格式拼接成，
拼接的格式为(\n为换行符,’:’后面有一个空格)：

```text
host: $host\ndate: $date\n$request-line
```

假设

```text
请求url = wss://iat.cn-huabei-1.xf-yun.com/v1
date = Tue, 14 May 2024 08:43:39 GMT
```

那么 signature原始字段(signature_origin)则为：

```text
host: iat.cn-huabei-1.xf-yun.com
date: Tue, 14 May 2024 08:43:39 GMT
GET /v1 HTTP/1.1
```

4）使用hmac-sha256算法结合apiSecret对signature_origin签名，获得签名后的摘要signature_sha。

```text
signature_sha=hmac-sha256(signature_origin,$apiSecret)
```

其中 apiSecret 是在控制台获取的APISecret

5）使用base64编码对signature_sha进行编码获得最终的signature。

```text
signature=base64(signature_sha)
```

假设

```text
APISecret = secretxxxxxxxx2df7900c09xxxxxxxx	
date = Tue, 14 May 2024 08:43:39 GMT
```

则signature为

```text
signature=fgG1OuqkHU6l/hNZ6Zs466ci+jUvOQjSNKWkLhvSoNM=
```

6）根据以上信息拼接authorization base64编码前（authorization_origin）的字符串，示例如下。

```text
api_key="keyxxxxxxxx8ee279348519exxxxxxxx", algorithm="hmac-sha256", headers="host date request-line", signature="fgG1OuqkHU6l/hNZ6Zs466ci+jUvOQjSNKWkLhvSoNM="
```

*注：* headers是参与签名的参数，请注意是固定的参数名（"host date request-line"），而非这些参数的值。

7）最后再对authorization_origin进行base64编码获得最终的authorization参数。

```text
authorization = base64(authorization_origin)
示例：
authorization=YXBpX2tleT0ia2V5eHh4eHh4eHg4ZWUyNzkzNDg1MTlleHh4eHh4eHgiLCBhbGdvcml0aG09ImhtYWMtc2hhMjU2IiwgaGVhZGVycz0iaG9zdCBkYXRlIHJlcXVlc3QtbGluZSIsIHNpZ25hdHVyZT0iZmdHMU91cWtIVTZsL2hOWjZaczQ2NmNpK2pVdk9RalNOS1drTGh2U29OTT0i
```

## [#](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#五、数据传输接收与请求、返回示例)五、数据传输接收与请求、返回示例

### [#](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#_1、数据传输接收)1、数据传输接收

握手成功后客户端和服务端会建立Websocket连接，客户端通过Websocket连接可以同时上传和接收数据。
当服务端有识别结果时，会通过Websocket连接推送识别结果到客户端。

发送数据时，如果间隔时间太短，可能会导致引擎识别有误。
建议每次发送音频间隔40ms，每次发送音频字节数（即java示例demo中的frameSize）为一帧音频大小的整数倍。

```java
//连接成功，开始发送数据
int frameSize = 1280; //每一帧音频大小的整数倍，请注意不同音频格式一帧大小字节数不同，可参考下方建议
int intervel = 40;
int status = 0;  // 音频的状态
try (FileInputStream fs = new FileInputStream(file)) {
    byte[] buffer = new byte[frameSize];
    // 发送音频
```

我们建议：未压缩的PCM格式，每次发送音频间隔40ms，每次发送音频字节数1280B；

### [#](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#_2、请求json示例)2、请求json示例

第一帧数据：

```json
{
  "header": {
    "app_id": "your_appid",
    "status": 0
  },
  "parameter": {
    "iat": {
      "domain": "slm",
      "language": "mul_cn",
      "accent": "mandarin",
      "eos": 6000,
      "vinfo": 1,
      "result": {
        "encoding": "utf8",
        "compress": "raw",
        "format": "json"
      }
    }
  },
  "payload": {
    "audio": {
      "encoding": "raw",
      "sample_rate": 16000,
      "channels": 1,
      "bit_depth": 16,
      "seq": 1,
      "status": 0,
      "audio": "AAAAAP..."
    }
  }
}
```

中间帧数据：

```json
{
  "header": {
    "app_id": "your_appid",
    "status": 1
  },
  "payload": {
    "audio": {
      "encoding": "raw",
      "sample_rate": 16000,
      "status": 1,
      "audio": "AAAAAA..."
    }
  }
}
```

最后一帧数据：

```json
{
  "header": {
    "app_id": "your_appid",
    "status": 2
  },
  "payload": {
    "audio": {
      "encoding": "raw",
      "sample_rate": 16000,
      "status": 2,
      "audio":"AAAAAA..."
    }
  }
}
```

### [#](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#_3、返回json示例)3、返回json示例

第一帧返回数据示例：

```json
{
  "header": {
    "code": 0,
    "message": "success",
    "sid": "iat000e0044@hu18f5b16b0330324...",
    "status": 0
  }
}
```

中间帧返回数据示例：

```json
{
  "header": {
    "code": 0,
    "message": "success",
    "sid": "iat000e0044@hu18f5b16b033032...",
    "status": 1
  },
  "payload": {
    "result": {
      "compress": "raw",
      "encoding": "utf8",
      "format": "json",
      "seq": 2,
      "status": 1,
      "text": "eyJzbiI6Miwib..."
    }
  }
}
```

最后一帧返回数据示例：

```json
{
  "header": {
    "code": 0,
    "message": "success",
    "sid": "iat000e0044@hu18f5b16b033032...",
    "status": 2
  },
  "payload": {
    "result": {
      "compress": "raw",
      "encoding": "utf8",
      "format": "json",
      "seq": 76,
      "status": 2,
      "text": "eyJzbiI6NzYs..."
    }
  }
}
```

## [#](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#六、参数说明)六、参数说明

### [#](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#_1、请求参数说明)1、请求参数说明

| 参数名                    | 类型   | 必传 | 描述                                                         |
| ------------------------- | ------ | ---- | ------------------------------------------------------------ |
| header                    | object | 是   | 用于上传平台参数                                             |
| header.app_id             | string | 是   | 在平台申请的appid信息                                        |
| header.status             | int    | 是   | 音频传输状态 0:首帧 1：中间帧 2:最后一帧                     |
| parameter                 | object | 是   | 用于上传服务特性参数                                         |
| parameter.iat             | object | 是   | 服务名称，大模型多语种语音识别                               |
| parameter.iat.domain      | string | 是   | 指定访问的领域 slm                                           |
| parameter.iat.language    | string | 是   | 语种 mul_cn                                                  |
| parameter.iat.accent      | string | 是   | mandarin                                                     |
| parameter.iat.ln          | string | 否   | 语种参数：支持两种模式，指定语种，如识别英文ln=en，可参考语种列表；免切模式，不需要指定语种参数或传参ln=none |
| parameter.iat.eos         | int    | 否   | 静音多少秒停止识别 如6000毫秒                                |
| parameter.iat.vinfo       | int    | 否   | 句子级别帧对齐                                               |
| parameter.iat.result      | obejct | 否   | 响应数据字段                                                 |
| payload                   | object | 是   | 请求数据携带                                                 |
| payload.audio             | object | 是   | 音频数据模块                                                 |
| payload.audio.encoding    | string | 否   | 音频编码 raw或lame（代表pcm或mp3格式）                       |
| payload.audio.sample_rate | int    | 否   | 音频采样率 16000, 8000                                       |
| payload.audio.channels    | int    | 否   | 音频声道 1                                                   |
| payload.audio.bit_depth   | int    | 否   | 音频位深 16                                                  |
| payload.audio.seq         | int    | 否   | 数据序号 0-999999                                            |
| payload.audio.status      | int    | 否   | 取值范围为：0（开始）、1（继续）、2（结束）                  |
| payload.audio.audio       | string | 是   | 音频数据base64 音频时长不要超过60秒                          |

### [#](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#_2、返回参数说明)2、返回参数说明

| 参数名                  | 类型   | 描述                                                         |
| ----------------------- | ------ | ------------------------------------------------------------ |
| header                  | object | 协议头部                                                     |
| header.message          | string | 描述信息                                                     |
| header.code             | int    | 返回码 0表示会话调用成功（并不一定表示服务调用成功，服务是否调用成功以text字段中的ret为准） 其它表示会话调用异常 |
| header.sid              | string | 本次会话id                                                   |
| header.status           | int    | 数据状态 0:开始, 1:继续, 2:结束                              |
| payload                 | object | 数据段，用于携带响应的数据                                   |
| payload.result.compress | string | 文本压缩格式                                                 |
| payload.result.encoding | string | 文本编码                                                     |
| payload.result.format   | string | 文本格式                                                     |
| payload.result.seq      | int    | 数据序号 0-999999                                            |
| payload.result.status   | int    | 0:开始, 1:继续, 2:结束                                       |
| payload.result.text     | string | 听写数据文本 base64编码                                      |

**text字段base64解码后参数说明请：**

| 参数                                | 类型       | 描述                                                         |
| ----------------------------------- | ---------- | ------------------------------------------------------------ |
| sn                                  | int        | 返回结果的序号                                               |
| ls                                  | bool       | 是否是最后一片结果                                           |
| bg                                  | int        | 保留字段，无需关心                                           |
| ed                                  | int        | 保留字段，无需关心                                           |
| ws                                  | array      | 听写结果                                                     |
| ws.bg                               | int        | 起始的端点帧偏移值，单位：帧（1帧=10ms） 注：以下两种情况下bg=0，无参考意义： 1)返回结果为标点符号或者为空；2)本次返回结果过长。 |
| ws.cw                               | array      | 分词                                                         |
| ws.cw.w                             | string     | 字词                                                         |
| ws.cw.lg                            | string     | 源语种                                                       |
| ws.cw.其他字段 sc/wb/wc/we/wp/ng/ph | int/string | 均为保留字段，无需关心。如果解析sc字段，建议float与int数据类型都做兼容 |

## [#](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#七、语种列表)七、语种列表

| 语种     | 参数 | 语种       | 参数 |
| -------- | ---- | ---------- | ---- |
| 中文     | zh   | 乌尔都语   | ur   |
| 英文     | en   | 孟加拉语   | bn   |
| 日语     | ja   | 泰米尔语   | ta   |
| 韩语     | ko   | 乌克兰语   | uk   |
| 俄语     | ru   | 哈萨克语   | kk   |
| 法语     | fr   | 乌兹别克语 | uz   |
| 西班牙语 | es   | 波兰语     | pl   |
| 阿拉伯语 | ar   | 蒙语       | mn   |
| 德语     | de   | 斯瓦西里语 | sw   |
| 泰语     | th   | 豪撒语     | ha   |
| 越南语   | vi   | 波斯语     | fa   |
| 印地语   | hi   | 荷兰语     | nl   |
| 葡萄牙语 | pt   | 瑞典语     | sv   |
| 意大利语 | it   | 罗马尼亚语 | ro   |
| 马来语   | ms   | 保加利亚语 | bg   |
| 印尼语   | id   | 维语       | ug   |
| 菲律宾语 | fil  | 藏语       | tib  |
| 土耳其语 | tr   |            |      |
| 希腊语   | el   |            |      |
| 捷克语   | cs   |            |      |

## [#](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#八、常见问题)八、常见问题

### [#](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#大模型多语种语音识别与语音听写的区别)大模型多语种语音识别与语音听写的区别？

> 答：大模型多语种语音识别基于大模型进行的训练，数据量更丰富，听写效果上限更高，未来发展性更强。

### [#](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html#大模型多语种语音识别支持多少分钟的音频识别)大模型多语种语音识别支持多少分钟的音频识别？

> 答：支持1分钟以内的音频识别。
