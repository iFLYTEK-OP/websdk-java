# 精调服务OpenAPI协议文档


## 接口与鉴权

### 应用申请

> 星辰Mass平台地址：https://training.xfyun.cn/


### 实例代码

##### 示例代码

1、添加maven依赖
```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-spark</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.0.5</version>
</dependency>
```

2、Java代码
```java
package cn.xfyun.demo.spark;

import cn.hutool.json.JSONUtil;
import cn.xfyun.api.MassClient;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.model.RoleContent;
import cn.xfyun.model.finetuning.response.MassResponse;
import cn.xfyun.service.finetuning.AbstractMassWebSocketListener;
import cn.xfyun.util.StringUtils;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * （fine-tuning-text）精练大模型文本对话
 * 1、APPID、APISecret、APIKey信息获取：<a href="https://training.xfyun.cn/model/add">...</a>
 * 2、文档地址：<a href="https://www.xfyun.cn/doc/spark/%E7%B2%BE%E8%B0%83%E6%9C%8D%E5%8A%A1API-websocket.html">...</a>
 */
public class MassClientApp {
    private static final Logger logger = LoggerFactory.getLogger(MassClientApp.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getApiKey();
    private static final String apiSecret = PropertiesConfig.getApiSecret();


    public static void main(String[] args) throws Exception {
        MassClient client = new MassClient.Builder()
                // .signatureWs("0", "xdeepseekv3", appId, apiKey, apiSecret)
                .signatureWs("0", "xdeepseekr1", appId, apiKey, apiSecret)
                // .signatureHttp("0", "xdeepseekr1", apiKey)
                .wsUrl("wss://maas-api.cn-huabei-1.xf-yun.com/v1.1/chat")
                // .requestUrl("https://maas-api.cn-huabei-1.xf-yun.com/v1")
                .build();

        List<RoleContent> messages = new ArrayList<>();
        RoleContent roleContent = new RoleContent();
        roleContent.setRole("user");
        roleContent.setContent("你好");
        RoleContent roleContent1 = new RoleContent();
        roleContent1.setRole("assistant");
        roleContent1.setContent("你好！");
        RoleContent roleContent2 = new RoleContent();
        roleContent2.setRole("user");
        roleContent2.setContent("你是谁");
        RoleContent roleContent3 = new RoleContent();
        roleContent3.setRole("assistant");
        roleContent3.setContent("我是Spark API。");
        RoleContent roleContent4 = new RoleContent();
        roleContent4.setRole("user");
        roleContent4.setContent("帮我讲一个笑话");

        messages.add(roleContent);
        messages.add(roleContent1);
        messages.add(roleContent2);
        messages.add(roleContent3);
        messages.add(roleContent4);

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        StringBuffer finalResult = new StringBuffer();
        StringBuffer thingkingResult = new StringBuffer();
        client.send(messages, new AbstractMassWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, MassResponse resp) {
                logger.debug("中间返回json结果 ==>{}", JSONUtil.toJsonStr(resp));
                if (resp.getHeader().getCode() != 0) {
                    logger.error("code=>{}，error=>{}，sid=>{}", resp.getHeader().getCode(), resp.getHeader().getMessage(), resp.getHeader().getSid());
                    logger.warn("错误码查询链接：https://www.xfyun.cn/doc/spark/%E7%B2%BE%E8%B0%83%E6%9C%8D%E5%8A%A1API-websocket.html");
                    return;
                }

                if (null != resp.getPayload() && null != resp.getPayload().getChoices()) {
                    List<MassResponse.Payload.Choices.Text> text = resp.getPayload().getChoices().getText();
                    if (null != text && !text.isEmpty()) {
                        String content = resp.getPayload().getChoices().getText().get(0).getContent();
                        String reasonContent = resp.getPayload().getChoices().getText().get(0).getReasoning_content();
                        if (!StringUtils.isNullOrEmpty(reasonContent)) {
                            thingkingResult.append(reasonContent);
                            logger.info("思维链结果... ==> {}", reasonContent);
                        } else if (!StringUtils.isNullOrEmpty(content)) {
                            finalResult.append(content);
                            logger.info("中间结果 ==> {}", content);
                        }
                    }

                    if (resp.getPayload().getChoices().getStatus() == 2) {
                        // 说明数据全部返回完毕，可以关闭连接，释放资源
                        logger.info("session end");
                        Date dateEnd = new Date();
                        logger.info("{}开始", sdf.format(dateBegin));
                        logger.info("{}结束", sdf.format(dateEnd));
                        logger.info("耗时：{}ms", dateEnd.getTime() - dateBegin.getTime());
                        logger.info("完整思维链结果 ==> {}", thingkingResult);
                        logger.info("最终识别结果 ==> {}", finalResult);
                        logger.info("本次识别sid ==> {}", resp.getHeader().getSid());
                        client.closeWebsocket();
                        System.exit(0);
                    }
                }
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                client.closeWebsocket();
            }

            @Override
            public void onClose(WebSocket webSocket, int code, String reason) {

            }
        });

        // post方式
        // String result = client.send(messages);
        // logger.debug("{} 模型返回结果 ==>{}", client.getDomain(), result);
        // JSONObject obj = JSON.parseObject(result);
        // String content = obj.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        // logger.info("{} 大模型回复内容 ==>{}", client.getDomain(), content);
    }
}

```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/MassClientApp.java)


### 接口域名
```apl
maas-api.cn-huabei-1.xf-yun.com
```

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
| 10007  | 用户流量受限：服务正在处理用户当前的问题，需等待处理完成后再发送新的请求。（必须要等大模型完全回复之后，才能发送下一个问题） |
| 10008  | 服务容量不足，联系服务商                                     |
| 10009  | 和引擎建立连接失败                                           |
| 10010  | 接收引擎数据的错误                                           |
| 10011  | 向引擎发送数据的错误                                         |
| 10012  | 引擎内部错误                                                 |
| 10013  | 用户问题涉及敏感信息，审核不通过，拒绝处理此次请求。         |
| 10014  | 回复结果涉及到敏感信息，审核不通过，后续结果无法展示给用户。（建议清空当前结果，并给用户提示/警告：该答案涉及到敏感/政治/恐怖/色情/暴力等方面，不予显示/回复） |
| 10015  | appid在黑名单中                                              |
| 10016  | appid授权类的错误。比如：未开通此功能，未开通对应版本，token不足，并发超过授权等等。（联系我们开通授权或提高限制） |
| 10018  | 用户在5分钟内持续发送ping消息，但并没有实际请求数据，会返回该错误码并断开ws连接。短链接使用无需关注 |
| 10019  | 该错误码表示返回结果疑似敏感，建议拒绝用户继续交互           |
| 10110  | 服务忙，请稍后再试。                                         |
| 10163  | 请求引擎的参数异常，引擎的schema检查不通过                   |
| 10222  | 引擎网络异常                                                 |
| 10223  | LB找不到引擎节点                                             |
| 10907  | token数量超过上限。对话历史+问题的字数太多，需要精简输入。   |
| 11200  | 授权错误：该appId没有相关功能的授权或者业务量超过限制（联系我们开通授权或提高限制） |
| 11201  | 授权错误：日流控超限。超过当日最大访问量的限制。（联系我们提高限制） |
| 11202  | 授权错误：秒级流控超限。秒级并发超过授权路数限制。（联系我们提高限制） |
| 11203  | 授权错误：并发流控超限。并发路数超过授权路数限制。（联系我们提高限制） |

### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_5-2-内容审核说明)5.2 内容审核说明

当返回10013或者10014错误码时，代码内容审核判断当前问题或回复的信息涉及敏感信息。返回错误的同时，在header.message字段中会携带当前的敏感提示语。

- 10013 表示用户的问题涉及敏感信息，服务侧拒绝处理此次请求。
- 10014 表示回复结果中涉及敏感信息，后续结果不可以展示给用户。
- 10019 表示当前的回复疑似敏感，结果文本可以给用户展示。服务会在返回**全部结果**后再返回该错误码，如果继续提问可能会导致被审核拦截。建议在收到该错误码后提示用户涉及敏感信息，并禁掉对话框，禁止用户继续提问。

如果需要调整内容审核的严格程度、敏感词等信息，请联系我们技术支持。



# 精调服务_WebSocket协议

## 1. 接口概述

### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_1-1-请求地址)1.1 请求地址

传输协议：支持 ws/wss，为确保数据安全，建议使用 wss 默认请求地址如下：

```text
wss://maas-api.cn-huabei-1.xf-yun.com/v1.1/chat
```

**说明**：部分模型因部署配置不同，其请求地址可能略有差异，具体可参考**服务管控**>**模型服务列表**右侧调用信息

### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_1-2-接口要求)1.2 接口要求

接口类型：流式 WebSocket 接口 接口对接：需要按照文档标准的方法进行对接，服务仅支持文档明确描述的功能，未提及的功能可能导致兼容性问题

### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_1-3-鉴权方式)1.3 鉴权方式

鉴权机制：使用签名机制进行鉴权，签名详情参考[通用URL鉴权文档](https://www.xfyun.cn/doc/spark/general_url_authentication.html)

### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_1-4-请求demo)1.4 请求demo

在使用demo之前，请修改代码中的app_id、key、secret （用于签名鉴权）以及patch_id（调用微调大模型时必传）
[demo python语言](https://openres.xfyun.cn/xfyundoc/2025-02-25/2440a0cc-11f3-46e7-920b-f49714f54003/1740484845579/python_ws_demo.zip)
[demo java语言](https://openres.xfyun.cn/xfyundoc/2025-02-09/d8c9a423-5c0d-40e3-8480-a51045bb8e43/1739115838803/Java_demo.zip)

## [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_2-接口请求)2. 接口请求

### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_2-1-请求示例)2.1 请求示例

以下示例展示了一个完整的请求结构：

```json
{
    "header": {
        "app_id": "123456",
        "uid": "39769795890",
        "patch_id":["xxx"]  // 调用微调大模型时必传,否则不传。对应为模型服务卡片上的resourceId
    },
    "parameter": {
        "chat": {
            "domain": "patch",  // 调用微调大模型时，对应为模型服务卡片上的serviceid
            "temperature": 0.5,
            "top_k": 4,
            "max_tokens": 2048,
            "auditing": "default",
            "chat_id":"xxx"
        }
    },
    "payload": {
        "message": {
            "text": [
                {
                    "role": "system",
                    "content": "你是星火认知大模型"
                },
                {
                    "role": "user",
                    "content": "今天的天气"  
                }
            ]
        }
    }
}
```

### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_2-2-请求参数)2.2 请求参数

接口请求字段由三个部分组成：header，parameter, payload。 字段解释如下：

#### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_2-2-1-header参数-平台参数)2.2.1 **Header**参数（平台参数）

| 字段     | 类型   | 是否必传 | 含义                                               | 限制         |
| -------- | ------ | -------- | -------------------------------------------------- | ------------ |
| app_id   | string | 是       | 应用的app_id，需要在平台申请                       | maxLength:8  |
| uid      | string | 否       | 每个用户的id，非必传字段，用于后续扩展             | maxLength:32 |
| patch_id | array  | 否       | 调用用户微调大模型时必传，对应resourceid，否则不传 | maxLength:32 |

#### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_2-2-2-parameter参数-服务特性参数)2.2.2 **Parameter**参数（服务特性参数）

##### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_2-2-2-1-对话服务参数-parameter-chat)2.2.2.1 对话服务参数（**parameter.chat**）

| 字段        | 类型   | 是否必传 | 含义                                                         | 限制                          | 备注                                                         |
| ----------- | ------ | -------- | ------------------------------------------------------------ | ----------------------------- | ------------------------------------------------------------ |
| domain      | string | 是       | 取值为用户服务的serviceId                                    |                               | serviceId可从星辰网页获取                                    |
| temperature | float  | 否       | 核采样阈值。用于决定结果随机性，取值越高随机性越强即相同的问题得到的不同答案的可能性越高 | 取值：[0,1]；默认值：0.5      | 无                                                           |
| top_k       | int    | 否       | 从k个候选中随机选择一个（非等概率）                          | 取值：[1, 6]；默认值：4       | 无                                                           |
| max_tokens  | int    | 否       | 回答的tokens的最大长度                                       | 取值：[1,32768]；默认值：2048 | 限制生成回复的最大 token 数量，max_tokens的限制需要满足`输入promptToken + 设置参数max_tokens <= 32768 - 1`,参数设置过大可能导致回答中断，请酌情调整，建议取值16384以下 |
| chat_id     | string | 否       | 用于关联用户会话                                             |                               | 需保障用户下的唯一性                                         |

#### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_2-2-3-payload参数-请求数据)2.2.3 **payload**参数（请求数据）

**payload.message** 中的 **text** 字段为文本数据，类型为 JSON 数组，示例如下：

| 字段 | 类型              | 是否必传 | 含义     | 限制                                   | 备注 |
| ---- | ----------------- | -------- | -------- | -------------------------------------- | ---- |
| text | json object array | 是       | 文本数据 | 受Token限制，有效内容不能超过8192Token |      |

**单轮交互**： 仅传递一条用户消息：

```json
[
    {"role": "user", "content": "你会做什么？"}  
]
```

**多轮交互**： 按 `user -> assistant -> user -> assistant` 顺序传递历史记录，最后一条为当前问题：

```json
[
    {"role": "user", "content": "你好"},
    {"role": "assistant", "content": "你好！"},
    {"role": "user", "content": "你是谁？"},
    {"role": "assistant", "content": "我是 Spark API。"},
    {"role": "user", "content": "你会做什么？"}
]
```

**字段说明：**

| 字段    | 类型   | 是否必传 | 含义     | 限制                            | 备注                                                         |
| ------- | ------ | -------- | -------- | ------------------------------- | ------------------------------------------------------------ |
| role    | string | 是       | 角色     | 取值范围：system,user,assistant | **通过system设置对话背景信息，user表示用户的问题，assistant表示AI的回复** |
| content | string | 是       | 文本内容 | 无                              | 该角色的对话内容                                             |

## [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_3-接口响应)3. 接口响应

### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_3-1-响应示例)3.1 响应示例

#### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_3-1-1-成功响应示例)3.1.1 成功响应示例

```json
{
    "header": {
        "code": 0,
        "message": "Success",
        "sid": "cht000704fa@dx16ade44e4d87a1c802",
        "status": 0
    },
    "payload": {
        "choices": {
            "status": 2,
            "seq": 0,
            "text": [
                {
                    "content": "xxxxs",
                    "index": 0,
                    "role": "assistant"
                }
            ]
        },
        "usage": {
            "text": {
                "completion_tokens": 0,
                "question_tokens": 0,
                "prompt_tokens": 0,
                "total_tokens": 0
            }
        }
    }
}
```

> 注意： WebSocket模式中，接口为流式返回，此示例为最后一次返回结果，开发者需要将接口多次返回的结果进行拼接展示。

#### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_3-1-2-异常结果示例)3.1.2 异常结果示例

```json
{
    "header": {
        "code": 10110,     // 错误码(重要)
        "message": "xxxx", // 错误描述信息(重要)
        "sid": "cht00120013@dx181c8172afb0001102",
        "status": 2
    }
}
```

### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_3-2-响应参数说明)3.2 响应参数说明

#### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_3-2-1-header参数)3.2.1 **header**参数

| 字段    | 类型   | 含义         | 备注                                                         |
| ------- | ------ | ------------ | ------------------------------------------------------------ |
| code    | int    | 服务错误码   | 0表示正常，非0表示出错                                       |
| sid     | string | 会话的sid    |                                                              |
| status  | int    | 会话的状态   | 取值[0，1，2]，**其中0表示第一个结果, 1表示中间结果, 2表示最后一个结果** |
| message | string | 返回消息描述 | 错误码的描述信息                                             |

#### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_3-2-2-响应数据参数)3.2.2 响应数据参数

**文本响应**（字段choices，默认返回）：

| 字段   | 类型              | 是否必传 | 含义     | 取值范围                | 默认值 | 说明              |
| ------ | ----------------- | -------- | -------- | ----------------------- | ------ | ----------------- |
| status | int               | 是       | 数据状态 | 0:开始,1:进行中,2:结束  |        | 2表示文本响应结束 |
| seq    | int               | 是       | 数据序号 | 最小值:0,最大值:9999999 |        | 数据序号          |
| text   | json object array | 是       | 文本结果 |                         |        | 是一个JSON数组    |

**token消耗信息**（字段 usage，仅在最终响应时返回）：

| 字段       | 类型        | 是否必传 | 含义     |
| ---------- | ----------- | -------- | -------- |
| usage.text | json object | 是       | 文本数据 |

### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_3-3-响应数据解析)3.3 响应数据解析

#### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_3-3-1-payload-choices-text格式解析)3.3.1 payload.choices.text格式解析

```json
[
    {
        "content": "这是AI的回复文本",
        "index": 0,
        "role": "assistant"
    }
]
```

| 字段              | 类型   | 是否必传 | 含义                                                 | 取值范围  | 说明             |
| ----------------- | ------ | -------- | ---------------------------------------------------- | --------- | ---------------- |
| content           | string | 是       | 回答的结果                                           |           |                  |
| reasoning_content | string | 是       | 模型生成的思考文本内容(支持深度思考的模型才有此字段) |           |                  |
| index             | int    | 是       | 结果序号                                             | 0         | 在多候选中使用   |
| role              | string | 是       | 角色                                                 | assistant | 说明这是AI的回复 |

#### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_3-3-2-payload-usage-text格式解析)3.3.2 payload.usage.text格式解析

示例：

```json
{
    "prompt_tokens": 0,
    "question_tokens": 0,
    "completion_tokens": 0,
    "total_tokens": 0
}
```

| 字段              | 类型 | 是否必传 | 含义                     | 说明                                    |
| ----------------- | ---- | -------- | ------------------------ | --------------------------------------- |
| completion_tokens | int  | 是       | 回答tokens大小           |                                         |
| question_tokens   | int  | 是       | 问题不带历史的tokens大小 | 单轮情况下，此数值会略小于prompt_tokens |
| prompt_tokens     | int  | 是       | 问题总tokens大小         |                                         |
| total_tokens      | int  | 是       | 问题和回答的tokens大小   |                                         |

#### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_3-3-3-结果格式补充说明)3.3.3 结果格式补充说明

模型返回的结果除了普通文本之外，可能还包含以下标记语言，建议集成方做好适配：

- markdown（表格、列表等）
- latex（数学公式）

## [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_4-连接管理)4. 连接管理

### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_4-1-建立连接)4.1 建立连接

此协议对应的接口为长连接接口，连接建立之后可以进行长时间的交互，**用户交互完成之后应该主动关闭连接**。

建立连接需要满足以下条件：

- 必须符合 websocket 协议规范（rfc6455）
- 若在 60 秒内无数据交互，服务端将主动断开连接，请确保在交互结束后主动关闭连接

### [#](https://www.xfyun.cn/doc/spark/精调服务API-websocket.html#_4-2-关闭连接)4.2 关闭连接

正常交互结束后，客户端可以通过websocket协议发送Close类型消息关闭连接。 **建议用户在使用完毕之后，主动关闭websocket连接，不要故意长时间的占用ws连接资源！**

参考GoLand代码如下：

```go
// 用户侧关闭连接
closeInfo := websocket.FormatCloseMessage(websocket.CloseNormalClosure, "close ws conn")
_ = conn.WriteControl(websocket.CloseMessage, closeInfo, time.Now().Add(2*time.Second))
_ = conn.Close()
```

**提示**：除正常交互外，以下情况也会导致连接断开：

- 客户端连续 60 秒未发送数据；
- 服务升级、熔断等特殊情况下，API 可能主动断开连接，需要业务集成时，请确保做好异常处理。





# 精调服务_HTTP协议

## [#](https://www.xfyun.cn/doc/spark/精调服务API-http.html#_1-接口说明)1. 接口说明

协议 ：HTTP 请求方法：POST

**若使用 http client 的方式，直接发起request请求，地址如下：**

```http
https://maas-api.cn-huabei-1.xf-yun.com/v1/chat/completions
```

**若使用 openai sdk，url地址如下：**

```http
https://maas-api.cn-huabei-1.xf-yun.com/v1
```

部分模型因为部署原因可能略有差异，具体可参考**服务管控** > **模型服务列表**右侧调用信息。

## [#](https://www.xfyun.cn/doc/spark/精调服务API-http.html#_2-接口请求)2. 接口请求

### [#](https://www.xfyun.cn/doc/spark/精调服务API-http.html#_2-1-请求示例)2.1 请求示例

下面基于openAI SDK请求的 Python Demo 示例：

```python
from openai import OpenAI  

api_key = "<从服务管控页面获取 对应服务的APIKey>"  # 请替换为您的 API Key  
api_base = "http://maas-api.cn-huabei-1.xf-yun.com/v1"  

client = OpenAI(api_key=api_key,base_url=api_base)  

try:
    response = client.chat.completions.create(
        model="<从服务管控获取要访问服务的serviceID>",
        messages=[{"role": "user", "content": "你好"}],
        stream=False,
        temperature=0.7,
        max_tokens=4096,
        extra_headers={"lora_id": "0"},  
        stream_options={"include_usage": True} 
    )

    print(response.choices[0].message.content)
    
except Exception as e:
    print(f"Error: {e}")
```

**注意**：在执行此代码之前，请务必替换 `api_key` 为您的API Key。

如果想使用 HTTP 请求的 流式输出，请参考如下实例：

```python
from openai import OpenAI  

api_key = "<从服务管控页面获取 对应服务的APIKey>"  # 请替换为您的 API Key  
api_base = "http://maas-api.cn-huabei-1.xf-yun.com/v1"  

client = OpenAI(api_key=api_key,base_url=api_base)  

try:
    response = client.chat.completions.create(
        model="<从服务管控获取要访问服务的serviceID>",
        messages=[{"role": "user", "content": "你好"}],
        stream=True,
        temperature=0.7,
        max_tokens=4096,
        extra_headers={"lora_id": "0"},  
        stream_options={"include_usage": True} 
    )

    full_response = ""
    for chunk in response:
        # 只对支持深度思考的模型才有此字段
        if hasattr(chunk.choices[0].delta, 'reasoning_content') and chunk.choices[0].delta.reasoning_content is not None:
            reasoning_content = chunk.choices[0].delta.reasoning_content
            print(reasoning_content, end="", flush=True)  # 实时打印思考模型输出的思考过程每个片段
        
        if hasattr(chunk.choices[0].delta, 'content') and chunk.choices[0].delta.content is not None:
            content = chunk.choices[0].delta.content
            print(content, end="", flush=True)  # 实时打印每个片段
            full_response += content
    
    print("\n\n ------完整响应：", full_response)    
except Exception as e:
    print(f"Error: {e}")
```

**注意**：在运行此代码之前，请务必替换 `api_key` 为您的API Key。

### [#](https://www.xfyun.cn/doc/spark/精调服务API-http.html#_2-2-请求参数)2.2 请求参数

| 参数           | 类型    | 是否必填 | 要求                                           | 说明                                                         |
| -------------- | ------- | -------- | ---------------------------------------------- | ------------------------------------------------------------ |
| model          | string  | 是       |                                                | 指定要调用的对话生成模型ID                                   |
| messages       | array   | 是       | `[{"role": "user", "content":"用户输入内容"}]` | 表示对话上下文的消息列表，支持多轮对话交互。其中，`role` 用于标识消息发送方（例如 `user` 表示用户、`assistant` 表示模型回复、 `system` 用以设置对话背景信息），`content` 则为实际文本内容。 |
| stream         | boolean | 否       | 取值为 `true` 或 `false`，默认值为 `false`     | 指定是否采用流式响应模式。若设置为 `true`，系统将逐步返回生成的回复内容；否则，将一次性返回完整响应 |
| temperature    | float   | 否       | 取值为`[0,1]`,默认值为`0.7`                    | 核采样阈值。用于决定结果随机性，取值越高随机性越强即相同的问题得到的不同答案的可能性越高 |
| max_tokens     | int     | 否       | 取值为`[1,32768]`，默认值为`2048`              | 限制生成回复的最大 token 数量，max_tokens的限制需要满足`输入promptToken + 设置参数max_tokens <= 32768 - 1`,参数设置过大可能导致回答中断，请酌情调整，建议取值16384以下 |
| extra_headers  | object  | 否       | 默认值为`{"lora_id": "0"}`                     | 通过传递 `lora_id` 加载特定的LoRA模型                        |
| stream_options | object  | 否       | 默认值为`{"include_usage": True}`              | 针对流式响应模式的扩展配置，如控制是否在响应中包含API调用统计信息等附加数据。 |

#### [#](https://www.xfyun.cn/doc/spark/精调服务API-http.html#_2-2-1-messages-参数说明)2.2.1 `messages` 参数说明

messages 参数用于传递对话内容，包括用户输入和 AI 回复

| 字段    | 含义     | 数据类型 | 取值范围              | 默认值 | 说明                                                         |
| ------- | -------- | -------- | --------------------- | ------ | ------------------------------------------------------------ |
| role    | 角色     | string   | system,user,assistant |        | **通过system设置对话背景信息，user表示用户的问题，assistant表示AI的回复** |
| content | 文本内容 | string   | --                    |        | 该角色的对话内容                                             |

**示例：单轮交互** 单轮交互只需要传递一个user角色的数据

```json
[
    {"role": "user", "content": "你会做什么？"}  
]
```

**示例：多轮交互** 多轮交互需要将之前的交互历史按照user->assistant->user->assistant规则进行拼接，并保证最后一条是user的当前问题。

```json
[
    {"role": "user", "content": "你好"},
    {"role": "assistant", "content": "你好！"},
    {"role": "user", "content": "你是谁？"},
    {"role": "assistant", "content": "我是 Spark API。"},
    {"role": "user", "content": "你会做什么？"}
]
```

## [#](https://www.xfyun.cn/doc/spark/精调服务API-http.html#_3-接口响应)3. 接口响应

### [#](https://www.xfyun.cn/doc/spark/精调服务API-http.html#_3-1-响应示例)3.1 响应示例

#### [#](https://www.xfyun.cn/doc/spark/精调服务API-http.html#_3-1-1-成功响应示例)3.1.1 成功响应示例

```python
Response: ChatCompletion(
    id='cht000b920a@dx194e0205ccbb8f3700',
    choices=[
        Choice(
            finish_reason='stop',
            index=0,
            logprobs=None,
            message=ChatCompletionMessage(
                content='大模型回复',
                refusal=None,
                role='assistant',
                audio=None,
                function_call=None,
                tool_calls=None
            )
        )
    ],
    created=1738927005,
    model=None,
    object='chat.completion',
    service_tier=None,
    system_fingerprint=None,
    usage=CompletionUsage(
        completion_tokens=42,
        prompt_tokens=44,
        total_tokens=86,
        completion_tokens_details=None,
        prompt_tokens_details=None
    )
```

#### [#](https://www.xfyun.cn/doc/spark/精调服务API-http.html#_3-1-2-异常结果示例)3.1.2 异常结果示例

```json
Error: Error code: 403 - {'error': {'message': '该令牌无权使用模型：xqwen257bxxx (request id: 2025020809381060443349905703260)', 'type': 'one_api_error'}}
```

### [#](https://www.xfyun.cn/doc/spark/精调服务API-http.html#_3-2-响应数据参数)3.2 响应数据参数

字段说明如下：

| 字段名                     | 类型   | 字段说明                                             |
| -------------------------- | ------ | ---------------------------------------------------- |
| id                         | string | 唯一标识符，标识本次对话调用的唯一ID，用于跟踪和调试 |
| choices                    | array  | 包含模型生成回复候选项的数组                         |
| •finish_reason             | string | 指示回复生成结束的原因，如`"stop"`                   |
| •index                     | int    | 回复候选项在数组中的索引位置，从0开始                |
| •logprobs                  | object | 如启用token概率日志，则返回具体信息                  |
| •message                   | object | 描述回复消息内容的对象，其内部字段如下               |
| ◦content                   | string | 模型生成的回复文本内容                               |
| ◦reasoning_content         | string | 模型生成的思考文本内容(支持深度思考的模型才有此字段) |
| ◦refusal                   | object | 模型拒绝回答时返回拒绝信息                           |
| ◦role                      | string | 消息发送方，通常为`"assistant"`                      |
| ◦audio                     | object | 如支持语音回复则返回音频数据                         |
| ◦function_call             | objec  | 模型调用外部函数时返回调用信息                       |
| ◦tool_calls                | object | 模型调用工具时返回调用详情，                         |
| created                    | int    | 响应生成时间的Unix时间戳（秒级）                     |
| model                      | string | 实际调用的模型名称                                   |
| object                     | string | 表示响应对象类型                                     |
| service_tier               | string | 表示调用所属的服务层级                               |
| system_fingerprint         | string | 系统指纹或配置标识                                   |
| usage                      | object | 包含token使用统计信息，其内部字段如下：              |
| •completion_tokens         | int    | 回复文本消耗的token数量                              |
| •prompt_tokens             | int    | 输入prompt消耗的token数量                            |
| •total_tokens              | int    | prompt与回复消耗token数量的总和                      |
| •completion_tokens_details | object | 回复生成过程中token的详细统计信息，若无则为`null`    |
| •prompt_tokens_details     | object | prompt部分token的详细统计信息                        |

## [#](https://www.xfyun.cn/doc/spark/精调服务API-http.html#_4-错误码列表)4 . 错误码列表

| 错误码                                 | 原因                                      | 解决方案                                                |
| -------------------------------------- | ----------------------------------------- | ------------------------------------------------------- |
| 401-无效的身份验证                     | 身份验证无效。                            | 确保使用正确的API密钥及请求组织。                       |
| 401-提供的API密钥不正确                | 请求的API密钥不正确。                     | 检查所用API密钥是否正确，清除浏览器缓存或生成新的密钥。 |
| 403-不支持的国家、地区或领土           | 您正在从不支持的国家、地区或领土访问API。 | 请参考相关页面获取更多信息。                            |
| 429-请求速率限制已达上限               | 您发送请求过快。                          | 控制请求频率，阅读速率限制指南。                        |
| 429-超出当前配额，请检查计划和计费详情 | 您的额度已用尽或已达到每月最高消费限制。  | 购买更多额度或了解如何提高使用限制。                    |
| 500-服务器处理请求时发生错误           | 服务器内部出现问题。                      | 稍后重试请求；若问题持续，请联系我们查看状态页面。      |
| 503-引擎当前过载，请稍后重试           | 服务器流量过大。                          | 稍候重试您的请求。                                      |





