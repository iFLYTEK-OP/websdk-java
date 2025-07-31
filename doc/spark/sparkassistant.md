# 星火助手 API文档

## 简介

本客户端基于讯飞开放平台 星辰Agent 实现，提供星火助手调用能力[官方文档](https://www.xfyun.cn/doc/spark/SparkAssistantAPI.html)

## 功能列表

| 方法名 | 功能说明 |
| ------ | -------- |
| send() | 发送请求 |

## 使用准备

1. 前往[星辰Agent-发布管理](https://agent.xfyun.cn/management/release/workflow)发布提示词指令工程为API
3. 发布成功后绑定应用获取以下凭证:
   - API Secret
   - API Key
   - assistantId(为接口地址的后缀)

## 快速开始

1、添加maven依赖

```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-spark</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.1.5</version>
</dependency>
```

2、Java代码

详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/SparkAssistantClientApp.java)

## 错误码

| 错误码 | 错误信息                                                     |
| ------ | ------------------------------------------------------------ |
| 0      | 成功                                                         |
| 10000  | 升级为ws出现错误                                             |
| 10001  | 通过ws读取用户的消息 出错                                    |
| 10002  | 通过ws向用户发送消息 出错                                    |
| 10003  | 用户的消息格式有错误                                         |
| 10004  | 用户数据的schema错误                                         |
| 10005  | 用户参数值有错误                                             |
| 10006  | 用户并发错误：当前用户已连接，同一用户不能多处同时连接。     |
| 10007  | 用户流量受限：服务正在处理用户当前的问题，需等待处理完成后再发送新的请求。 （必须要等大模型完全回复之后，才能发送下一个问题） |
| 10008  | 服务容量不足，联系服务商                                     |
| 10009  | 和引擎建立连接失败                                           |
| 10010  | 接收引擎数据的错误                                           |
| 10011  | 向引擎发送数据的错误                                         |
| 10012  | 引擎内部错误                                                 |
| 10013  | 用户问题涉及敏感信息，审核不通过，拒绝处理此次请求。         |
| 10014  | 回复结果涉及到敏感信息，审核不通过，后续结果无法展示给用户。 |
| 10015  | appid在黑名单中                                              |
| 10016  | appid授权类的错误。比如：未开通此功能，未开通对应版本，token不足，并发超过授权 等等。 （联系我们开通授权或提高限制） |
| 10018  | 用户在5分钟内持续发送ping消息，但并没有实际请求数据，会返回该错误码并断开ws连接。短链接使用无需关注 |
| 10019  | 该错误码表示返回结果疑似敏感，建议拒绝用户继续交互           |
| 10110  | 服务忙，请稍后再试。                                         |
| 10163  | 请求引擎的参数异常 引擎的schema 检查不通过                   |
| 10222  | 引擎网络异常                                                 |
| 10223  | LB找不到引擎节点                                             |
| 10907  | token数量超过上限。对话历史+问题的字数太多，需要精简输入。   |
| 11200  | 授权错误：该appId没有相关功能的授权 或者 业务量超过限制（联系我们开通授权或提高限制） |
| 11201  | 授权错误：日流控超限。超过当日最大访问量的限制。（联系我们提高限制） |
| 11202  | 授权错误：秒级流控超限。秒级并发超过授权路数限制。（联系我们提高限制） |
| 11203  | 授权错误：并发流控超限。并发路数超过授权路数限制。（联系我们提高限制） |

## 大模型参数

| 字段        | 类型   | 是否必传 | 含义                                                         | 备注                                                         |
| ----------- | ------ | -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| domain      | string | 否       | 指定访问的模型版本                                           |                                                              |
| temperature | float  | 否       | 取值范围 (0，1] ，默认值0.5                                  | 核采样阈值。取值越高随机性越强，即相同的问题得到的不同答案的可能性越大 |
| maxTokens   | int    | 否       | Pro、Max、Max-32K、4.0 Ultra 取值为[1,8192]，默认为4096; Lite、Pro-128K 取值为[1,4096]，默认为4096。 | 模型回答的tokens的最大长度                                   |
| topK        | int    | 否       | 取值为[1，6],默认为4                                         | 从k个候选中随机选择⼀个（⾮等概率）                          |

## 方法详解

### 1. 发送请求
```java
public void send(SparkChatParam sparkChatParam, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException
```
**参数说明**：

- `param`: 查询参数对象，可设置：

|       名称        |  类型  |                             描述                             | 必须 | 默认值 |
| :---------------: | :----: | :----------------------------------------------------------: | ---- | ------ |
|     messages      | Array  | 对话记录和当前问题列表集合 所有content的累计tokens长度，<br />不同版本限制不同： <br />Lite、Pro、Max、4.0 Ultra版本: 不超过8192; <br /><br />Max-32K版本: 不超过32* 1024; Pro-128K版本:不超过 128*1024; | Y    |        |
|      chatId       | String | 拓展的会话Id , 保障用户会话的唯一性 <br />仅多语种大模型联动返回 | N    |        |
|      userId       | String |           用户的唯一id，表示一个用户，user_123456            | N    |        |
| webSocketListener | Object | 自定义ws抽象监听类（可使用sdk提供的**AbstractSparkModelWebSocketListener**） | Y    |        |

**响应示例**：

```json
# 接口为流式返回，此示例为最后一次返回结果，开发者需要将接口多次返回的结果进行拼接展示
{
    "header":{
        "code":0,
        "message":"Success",
        "sid":"cht000cb087@dx18793cd421fb894542",
        "status":2
    },
    "payload":{
        "choices":{
            "status":2,
            "seq":0,
            "text":[
                {
                    "content":"我可以帮助你的吗？",
                    "role":"assistant",
                    "index":0
                }
            ]
        },
        "usage":{
            "text":{
                "question_tokens":4,
                "prompt_tokens":5,
                "completion_tokens":9,
                "total_tokens":14
            }
        }
    }
}
```

---

## 注意事项

1. 在当返回10013或者10014错误码时，代码内容审核判断当前问题或回复的信息涉及敏感信息。返回错误的同时，在header.message字段中会携带当前的敏感提示语。
3. 模型结果除了普通文本类型，为了满足排版需求，会出现以下的标记语言，建议集成方进行适配：
   - markdown（表格、列表等）
   - latex（数学公式）

## 错误处理

捕获异常示例：
```java
try {
    String result = client.send();
} catch (BusinessException e) {
    System.err.println("业务处理异常：" + e.getMessage());
} catch (IOException e) {
    System.err.println("网络请求失败：" + e.getMessage());
} catch (MalformedURLException e) {
    System.err.println("请求路径异常：" + e.getMessage());
} catch (SignatureException e) {
    System.err.println("签名异常：" + e.getMessage());
}
```
