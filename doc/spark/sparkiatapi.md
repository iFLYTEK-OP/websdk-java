# 大模型语音识别 API 文档

## 简介

本客户端基于讯飞Spark API实现，提供大模型语音识别能力，可参考以下文档:

[中英大模型](https://www.xfyun.cn/doc/spark/spark_zh_iat.html)

[方言大模型](https://www.xfyun.cn/doc/spark/spark_slm_iat.html)

[多语种大模型](https://www.xfyun.cn/doc/spark/spark_mul_cn_iat.html)

## 功能列表

| 方法名 | 功能说明                                           |
| ------ | -------------------------------------------------- |
| send() | websocket调用 , 支持字节数组,文件流,文件等方式调用 |

## 使用准备

1. 前往[语音识别大模型](https://www.xfyun.cn/services/speech_big_model)开通能力
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

[中英大模型Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/SparkIatZhClientApp.java)

[方言大模型Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/SparkIatMulZhClientApp.java)

[多语种大模型Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/SparkIatMulLangClientApp.java)

## 错误码

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

## 合成参数

| 功能标识     | 功能描述                                 | 数据类型 | 取值范围                                                     | 必填 | 默认值       |
| ------------ | ---------------------------------------- | -------- | ------------------------------------------------------------ | ---- | ------------ |
| executor     | 用户自定义的线程池                       | Object   |                                                              | 否   | 单例线程池   |
| langType     | 语种类型（SparkIatModelEnum）            | Integer  | -                                                            | 否   | -            |
| language     | 语种                                     | String   | zh_cn（中文或方言）、mul_cn（多语种）                        | 否   | -            |
| domain       | 应用领域                                 | String   | 固定值"slm"                                                  | 否   | "slm"        |
| accent       | 方言设置                                 | String   | mandarin（普通话）、mulacc（中文方言）、其他授权方言         | 否   | -            |
| eos          | 端点检测静默时间（毫秒）                 | int      | [600, 60000]                                                 | 否   | 1800（方言） |
| vinfo        | 返回子句结果的端点帧偏移值               | int      | 0（关闭）、1（开启）                                         | 否   | 0            |
| dwa          | （仅中文普通话）动态修正功能             | String   | "wpgs"（开启）                                               | 否   | -            |
| nbest        | 发音相似时的句子多候选结果数量           | Integer  | [1, 5]                                                       | 否   | -            |
| wbest        | 发音相似时的词语多候选结果数量           | Integer  | [1, 5]                                                       | 否   | -            |
| ptt          | （仅中文）标点预测                       | Integer  | 0（关闭）、1（开启）                                         | 否   | 1            |
| smth         | （仅中文）顺滑功能（标记语气词/叠词）    | Integer  | 0（关闭）、1（开启）                                         | 否   | 0            |
| nunum        | （仅中文）数字规整（转阿拉伯数字或符号） | Integer  | 0（关闭）、1（开启）                                         | 否   | 1            |
| opt          | 输出格式                                 | Integer  | 0（json无属性）、1（文本无属性）、2（json带属性）            | 否   | -            |
| dhw          | 会话热词（UTF-8/GB2312编码）             | String   | 最大1024字符                                                 | 否   | -            |
| rlang        | （仅中文）字体                           | String   | zh-cn（简体）、zh-hk（繁体香港）、zh-mo（新加坡）、zh-tw（台湾） | 否   | zh-cn        |
| ltc          | （仅中文）中英文筛选                     | Integer  | 1（不筛选）、2（仅中文）、3（仅英文）                        | 否   | -            |
| encoding     | 音频数据格式                             | String   | raw、lame、speex、opus、opus-wb、speex-wb（依模型类型不同）  | 否   | -            |
| sampleRate   | 音频采样率                               | Integer  | 8000、16000                                                  | 否   | -            |
| channels     | 声道数                                   | Integer  | 1、2                                                         | 否   | -            |
| bitDepth     | 位深                                     | Integer  | 8、16                                                        | 否   | -            |
| frameSize    | 每帧音频字节数建议值                     | Integer  | 如PCM建议1280B、speex建议61B/60B整数倍                       | 否   | -            |
| textEncoding | 文本编码                                 | String   | utf8、gb2312                                                 | 否   | utf8         |
| textCompress | 文本压缩格式                             | String   | raw、gzip                                                    | 否   | raw          |
| textFormat   | 文本格式                                 | String   | plain、json、xml                                             | 否   | json         |
| ln           | 语种参数（指定语种或免切模式）           | String   | 如"en"（英文）、"none"（免切模式）                           | 否   | -            |

## 方法详解

### 1. 文件方式请求
```java
public void send(File file, WebSocketListener webSocketListener) throws FileNotFoundException, MalformedURLException, SignatureException {
```
**参数说明**：

|       名称        |  类型  |                             描述                             | 必须 | 默认值 |
| :---------------: | :----: | :----------------------------------------------------------: | ---- | ------ |
|       file        |  File  |                 需要转写的文件: 音频(≤60秒)                  | Y    |        |
| webSocketListener | Object | 自定义ws抽象监听类（可使用sdk提供的**AbstractSparkIatWebSocketListener**） | Y    |        |

**响应示例**：

```json
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

### 2. 流方式请求

```java
public void send(InputStream inputStream, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
```

**参数说明**：

|       名称        |  类型  |                             描述                             | 必须 | 默认值 |
| :---------------: | :----: | :----------------------------------------------------------: | ---- | ------ |
|    inputStream    | Object |                需要转写的文件流: 音频(≤60秒)                 | Y    |        |
| webSocketListener | Object | 自定义ws抽象监听类（可使用sdk提供的**AbstractSparkIatWebSocketListener**） | Y    |        |

**响应示例**：见1响应

### 3. 字节数组方式请求

```java
public void send(byte[] bytes, Closeable closeable, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
```

**参数说明**：

|       名称        |  类型  |                             描述                             | 必须 | 默认值 |
| :---------------: | :----: | :----------------------------------------------------------: | ---- | ------ |
|       bytes       | byte[] |             需要转写的文件字节数组: 音频(≤60秒)              | Y    |        |
| webSocketListener | Object | 自定义ws抽象监听类（可使用sdk提供的**AbstractSparkIatWebSocketListener**） | Y    |        |

**响应示例**：见1响应

---



## 注意事项

1. 音频长度需要≤60秒
2. 返回结果是base64编码 , 需要解码后获取结果

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
