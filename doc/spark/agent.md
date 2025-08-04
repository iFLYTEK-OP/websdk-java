# 星火智能体 API文档

## 简介

本客户端基于讯飞开放平台 星辰Agent 实现，提供星火智能体工作流调用能力[官方文档](https://www.xfyun.cn/doc/spark/Agent04-API%E6%8E%A5%E5%85%A5.html#_2-%E5%B7%A5%E4%BD%9C%E6%B5%81-api-%E9%9B%86%E6%88%90)

## 功能列表

| 方法名       | 功能说明                |
| ------------ | ----------------------- |
| completion() | 执行工作流(流式/非流式) |
| resume()     | 恢复工作流(流式)        |
| uploadFile() | 文件上传                |

## 使用准备

1. 前往[星辰Agent-发布管理](https://agent.xfyun.cn/management/release/workflow)发布工作流为API
3. 发布成功后绑定应用获取以下凭证:
   - API Secret
   - API Key
   - API Flowid

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

```java
package cn.xfyun.demo.spark;

import cn.xfyun.api.AgentClient;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.agent.AgentChatParam;
import cn.xfyun.model.agent.AgentResumeParam;
import cn.xfyun.service.agent.AgentCallback;
import cn.xfyun.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Call;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

/**
 * （ai-ppt-v2）智能体工作流
 * 1、APPID、APISecret、APIKey、APIPassword信息获取：<a href="https://www.xfyun.cn/doc/spark/Agent01-%E5%B9%B3%E5%8F%B0%E4%BB%8B%E7%BB%8D.html">...</a>
 * 2、文档地址：<a href="https://www.xfyun.cn/doc/spark/PPTv2.html">...</a>
 */
public class AgentClientApp {

    private static final Logger logger = LoggerFactory.getLogger(AgentClientApp.class);
    private static final String API_KEY = PropertiesConfig.getApiKey();
    private static final String API_SECRET = PropertiesConfig.getApiSecret();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
    private static String filePath;
    private static String resourcePath;

    static {
        try {
            filePath = "image/hidream_1.jpg";
            resourcePath = Objects.requireNonNull(AIPPTV2ClientApp.class.getResource("/")).toURI().getPath();
        } catch (URISyntaxException e) {
            logger.error("获取资源路径失败", e);
        }
    }

    public static void main(String[] args) {
        try {
            AgentClient client = new AgentClient.Builder(API_KEY, API_SECRET).build();

            JSONObject parameter = JSONObject.parseObject("{\"AGENT_USER_INPUT\": \"今天天气怎么样\"}");
            AgentChatParam agentChatParam = AgentChatParam.builder()
                    // .flowId("7351173267335847938")
                    .flowId("7351431612989308928")
                    .parameters(parameter)
                    .build();

            // 流式请求
            stream(client, agentChatParam);

            // 非流式请求
            // generate(client, agentChatParam);

            // 上传文件
            // uploadFile(client, new File(resourcePath + filePath));
        } catch (Exception e) {
            logger.error("请求失败", e);
        }
    }

    private static void uploadFile(AgentClient client, File file) throws IOException {
        String result = client.uploadFile(file);
        logger.info(result);
    }

    private static void generate(AgentClient client, AgentChatParam agentChatParam) throws IOException {
        String result = client.completion(agentChatParam);
        logger.info("工作流返回结果：{}", result);
        JSONObject obj = JSON.parseObject(result);
        int code = obj.getIntValue("code");
        if (code != 0) {
            logger.error(result);
            return;
        }
        JSONObject messages = obj.getJSONArray("choices").getJSONObject(0).getJSONObject("delta");
        logger.info("解析结果: {}", messages);
    }

    private static void stream(AgentClient client, AgentChatParam agentChatParam) {
        Date dateBegin = new Date();
        StringBuilder finalResult = new StringBuilder();
        StringBuilder thingkingResult = new StringBuilder();

        client.completion(agentChatParam, getCallback(finalResult, thingkingResult, client, dateBegin));
    }

    private static AgentCallback getCallback(StringBuilder finalResult, StringBuilder thingkingResult, AgentClient client, Date dateBegin) {
        return new AgentCallback() {
            @Override
            public void onEvent(Call call, String id, String type, String data) {
                resultHandler(data, finalResult, thingkingResult, client, dateBegin);
            }

            @Override
            public void onFail(Call call, Throwable t) {
                logger.error("sse通信出错", t);
            }

            @Override
            public void onClosed(Call call) {
                logger.info("sse断开链接");
                call.cancel();
            }

            @Override
            public void onOpen(Call call, Response response) {
                logger.info("sse建立链接");
            }
        };
    }

    /**
     * @param data            sse返回的数据
     * @param finalResult     实时回复内容
     * @param thingkingResult 实时思维链结果
     */
    private static void resultHandler(String data,
                                          StringBuilder finalResult,
                                          StringBuilder thingkingResult,
                                          AgentClient client,
                                          Date dateBegin) {
        // logger.info("sse返回数据 ==> {}", data);
        try (Scanner scanner = new Scanner(System.in)) {
            JSONObject obj = JSON.parseObject(data);
            JSONObject choice0 = obj.getJSONArray("choices").getJSONObject(0);
            JSONObject delta = choice0.getJSONObject("delta");
            JSONObject eventData = obj.getJSONObject("event_data");
            JSONObject step = obj.getJSONObject("workflow_step");
            // 触发事件
            if (null != eventData) {
                String eventId = eventData.getString("event_id");
                JSONObject value = eventData.getJSONObject("value");
                String type = value.getString("type");
                String content = value.getString("content");
                if ("option".equals(type)) {
                    logger.info(content);
                    JSONArray options = value.getJSONArray("option");
                    for (Object option : options) {
                        JSONObject item = JSON.parseObject(JSON.toJSONString(option));
                        logger.info("{}: {}", item.getString("id"), item.getString("text"));
                    }
                }
                String choice = scanner.nextLine();
                AgentResumeParam param = AgentResumeParam.builder()
                        .eventId(eventId)
                        .eventType("resume")
                        .content(choice)
                        .build();
                client.resume(param, getCallback(finalResult, thingkingResult, client, dateBegin));
            }
            // 回复
            String content = delta.getString("content");
            if (!StringUtils.isNullOrEmpty(content)) {
                logger.info("返回结果 ==> {}", content);
                finalResult.append(content);
            }
            // 思维链
            String reasonContent = delta.getString("reasoning_content");
            if (!StringUtils.isNullOrEmpty(reasonContent)) {
                logger.info("思维链结果... ==> {}", reasonContent);
                thingkingResult.append(reasonContent);
            }
            // 插件
            String pluginContent = delta.getString("plugins_content");
            if (!StringUtils.isNullOrEmpty(pluginContent)) {
                logger.info("插件信息 ==> {}", pluginContent);
            }
            // 结束原因
            String finishReason = choice0.getString("finish_reason");
            if (!StringUtils.isNullOrEmpty(finishReason)) {
                if (finishReason.equals("stop")/* || finishReason.equals("interrupt")*/) {
                    if (null != step) {
                        Integer seq = step.getInteger("seq");
                        Float progress = step.getFloat("progress");
                        logger.info(">>>>>>>>>>>>第{}次返回, 进度{}%>>>>>>>>>>>>\r\n\n", seq + 1, progress*100);
                    }
                    logger.info("本次识别sid ==> {}", obj.getString("id"));
                    // 说明数据全部返回完毕，可以关闭连接，释放资源
                    Date dateEnd = new Date();
                    logger.info("{}开始", sdf.format(dateBegin));
                    logger.info("{}结束", sdf.format(dateEnd));
                    logger.info("耗时：{}ms", dateEnd.getTime() - dateBegin.getTime());
                    if (!StringUtils.isNullOrEmpty(thingkingResult.toString())) {
                        logger.info("完整思维链结果 ==> {}", thingkingResult);
                    }
                    logger.info("最终识别结果 ==> {}", finalResult);
                    return;
                }
            }
            // 进度打印
            if (null != step) {
                Integer seq = step.getInteger("seq");
                Float progress = step.getFloat("progress");
                logger.info(">>>>>>>>>>>>第{}次返回, 进度{}%>>>>>>>>>>>>\r\n\n", seq + 1, progress*100);
            }
        } catch (BusinessException bx) {
            throw bx;
        } catch (Exception e) {
            logger.error("解析sse返回内容发生异常", e);
            logger.info("异常数据 ==> {}", data);
        }
    }
}

```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/AgentClientApp.java)

## 错误码

### 1 工作流错误

| 错误码 | 描述                 |
| ------ | -------------------- |
| 20201  | 未查找到对应 Flow ID |
| 20202  | Flow ID 非法         |
| 20204  | 工作流未发布         |
| 20207  | 工作流为草稿状态     |

### 2 模型错误

| 错误码 | 描述                                                         |
| ------ | ------------------------------------------------------------ |
| 20303  | 模型请求失败                                                 |
| 20350  | 升级为 ws 出现错误                                           |
| 20351  | 通过 ws 读取用户的消息出错                                   |
| 20352  | 通过 ws 向用户发送消息出错                                   |
| 20353  | 用户的消息格式有错误                                         |
| 20354  | 用户数据的 schema 错误                                       |
| 20355  | 用户参数值有错误                                             |
| 20356  | 用户并发错误：当前用户已连接，同一用户不能多处同时连接。     |
| 20357  | 用户流量受限：服务正在处理用户当前的问题，需等待处理完成后再发送新的请求。（必须要等大模型完全回复之后，才能发送下一个问题）") |
| 20358  | 服务容量不足，联系工作人员                                   |
| 20359  | 和引擎建立连接失败                                           |
| 20360  | 接收引擎数据的错误                                           |
| 20361  | 发送数据给引擎的错误                                         |
| 20362  | 引擎内部错误                                                 |
| 20363  | 输入内容审核不通过，涉嫌违规，请重新调整输入内容             |
| 20364  | 输出内容涉及敏感信息，审核不通过，后续结果无法展示给用户     |
| 20365  | appid 在黑名单中                                             |
| 20366  | appid 授权类的错误。比如：未开通此功能，未开通对应版本，token 不足，并发超过授权 等等 |
| 20367  | 清除历史失败                                                 |
| 20368  | 表示本次会话内容有涉及违规信息的倾向；建议开发者收到此错误码后给用户一个输入涉及违规的提示 |
| 20369  | 服务忙，请稍后再试                                           |
| 20370  | 请求引擎的参数异常 引擎的 schema 检查不通过                  |
| 20371  | 引擎网络异常                                                 |
| 20372  | token 数量超过上限。对话历史+问题的字数太多，需要精简输入    |
| 20373  | 授权错误：该 appId 没有相关功能的授权 或者 业务量超过限制    |
| 20374  | 授权错误：日流控超限。超过当日最大访问量的限制               |
| 20375  | 授权错误：秒级流控超限。秒级并发超过授权路数限制             |
| 20376  | 授权错误：并发流控超限。并发路数超过授权路数限制             |
| 20380  | 外部大模型请求失败                                           |

### 3 API 授权

| 错误码 | 描述                                                 |
| ------ | ---------------------------------------------------- |
| 20900  | 鉴权失败：授权限制，服务未授权或授权已到期           |
| 20901  | 计量鉴权失败：服务超限，业务会话总量超限或日流控超限 |
| 20902  | 鉴权失败：服务超限，QPS 秒级流控超限                 |
| 20903  | 并发鉴权失败：服务超限，并发路数超限                 |

### 4 文生图

| 错误码 | 描述                                   |
| ------ | -------------------------------------- |
| 21200  | 图片生成失败                           |
| 21201  | 图片存储失败                           |
| 21203  | 用户的消息格式有错误                   |
| 21204  | 用户数据的 schema 错误                 |
| 21205  | 用户参数值有错误                       |
| 21206  | 服务容量不足                           |
| 21207  | 输入审核不通过                         |
| 21208  | 模型生产的图片涉及敏感信息，审核不通过 |
| 21209  | 文生图超时                             |

### 5 工具错误

| 错误码 | 描述                      |
| ------ | ------------------------- |
| 21800  | 工具请求失败              |
| 21801  | 工具初始化失败            |
| 21802  | 工具 json 协议解析失败    |
| 21803  | 工具协议校验失败          |
| 21804  | 工具 openapi 协议解析失败 |
| 21805  | 工具 body 类型不支持      |
| 21806  | 工具 server 不存在        |
| 21807  | 官方工具请求失败          |
| 21808  | 工具不存在                |
| 21809  | 工具 Operation 不存在     |
| 21810  | 工具请求失败，连接异常    |
| 21811  | 三方工具执行失败          |
| 21812  | 三方工具请求失败          |

### 6 节点执行错误

| 错误码 | 描述                           |
| ------ | ------------------------------ |
| 20500  | 知识库请求异常                 |
| 20501  | 知识库节点执行异常             |
| 20502  | 知识库参数异常                 |
| 22500  | 开始节点协议有误               |
| 22600  | 结束节点协议有误               |
| 22601  | 结束节点执行失败               |
| 22701  | 消息节点执行失败               |
| 21900  | 参数提取失败                   |
| 21600  | 代码执行失败                   |
| 21601  | 代码解释器节点构建失败         |
| 21602  | 代码节点返回结果类型不符合要求 |
| 21603  | 代码执行超时                   |
| 22801  | 工作流节点执行失败             |
| 22802  | 工作流节点执行相应结果格式错误 |
| 22900  | 变量节点执行失败               |
| 23100  | 分支节点执行失败               |
| 23200  | 迭代节点执行失败               |
| 23300  | 大模型节点执行失败             |
| 23400  | 工具节点执行失败               |
| 23500  | 文本拼接节点执行失败           |
| 23700  | Agent 节点执行失败             |
| 23800  | 问答节点执行失败               |

### 7 会话

| 错误码 | 描述                 |
| ------ | -------------------- |
| 20804  | OpenAPI 输出超时     |
| 23900  | 该对话已超时或不存在 |

## 

## 方法详解

### 1. 执行工作流(流式)
```java
public void completion(AgentChatParam param, Callback callback)
```
**参数说明**：

- `param`: 查询参数对象，可设置：

|    名称    |  类型   |                             描述                             | 必须  | 默认值 |
| :--------: | :-----: | :----------------------------------------------------------: | ----- | ------ |
|   flowId   | String  |                          工作流 id                           | **Y** |        |
| parameters | Object  | 工作流开始节点的输入参数及取值，你可以在指定工作流的编排页面查看参数列表 {"input1": "xxxxx", "input2": "xxxxx"} | **Y** |        |
|  callback  | Object  | 自定义callback抽象监听类（可使用sdk提供的**AgentCallback**） | **Y** |        |
|    uid     | String  |                           用户 id                            | N     |        |
|   stream   | Boolean |           是否启用流式返回(流式时只能设置**true**)           | N     | true   |
|    ext     | Object  |    用于指定一些额外字段，比如一些插件隐藏字段 暂时用不到     | N     |        |
|   chatId   | String  |     会话 id，用于区分不同的工作流会话，长度不超过 32 位      | N     |        |
|  history   |  Array  | 历史对话信息[history_message object]集合 例如 [{"role": "user", "content_type": "text", "content": "你好" },{"role": "assistant", "content_type": "text", "content": "你好,我是你的工作助手，请问有什么可以帮您？" }] | N     |        |

**响应示例**：

```json
# 接口为流式返回，此示例为最后一次返回结果，开发者需要将接口多次返回的结果进行拼接展示

#流式输出过程帧
{
  "code": 0,
  "message": "Success",
  "id": "cha000c0076@dx191c21ce879b8f3532",
  "created": 123412324431,
  "workflow_step": {
    "seq": 0,
    "progress": 0.4
  },
  "choices": [
    {
      "delta": {
        "role": "assistant",
        "content": "你好，",
        "reasoning_content": ""
      },
      "index": 0,
      "finish_reason": null
    }
  ]
}

#流式输出结束帧
{
  "code": 0,
  "message": "Success",
  "id": "spf0016609f@dx193193f43cba44d782",
  "created": 123412324431,
  "workflow_step": {
    "seq": 6,
    "progress": 1
  },
  "choices": [
    {
      "delta": {
        "role": "assistant",
        "content": "",
        "reasoning_content": ""
      },
      "index": 0,
      "finish_reason": "stop"
    }
  ],
  "usage": {
    "prompt_tokens": 1,
    "completion_tokens": 0,
    "total_tokens": 9
  }
}
```

---

### 2. 执行工作流(非流式)
```java
public String completion(AgentChatParam param) throws IOException
```
**参数说明**：

- `param`: 查询参数对象，详情见1

**响应示例**：

```json
{
  "code": 0,
  "message": "Success",
  "id": "cha000b0003@dx1905cd86d6bb86d552",
  "choices": [
    {
      "delta": {
        "role": "assistant",
        "content": "你好，我是由科大讯飞构建的星火认知智能模型。\n如果你有任何问题或者需要帮助的地方，请随时告诉我！我会尽力为你提供解答和支持。请问有什么可以帮到你的吗？",
        "reasoning_content": ""
      },
      "index": 0,
      "finish_reason": "stop",
      "finish_reason": ""
    }
  ],
  "usage": {
    "prompt_tokens": 6,
    "completion_tokens": 42,
    "total_tokens": 48
  }
}
```

---

### 3. 恢复工作流(流式)
```java
public void resume(AgentResumeParam param, Callback callback)
```
**参数说明**：

- `param`: 查询参数对象，可设置：

|   名称    |  类型  |                             描述                             | 必须  | 默认值 |
| :-------: | :----: | :----------------------------------------------------------: | ----- | ------ |
|  eventId  | String | 事件 id，中断类事件发生时由 chat 和 resume 接口返回，用于标识同一工作流中一次请求产生的多个事件，值保持一致。 | **Y** |        |
| eventType | Object | 用于处理事件，默认走恢复。 resume: 恢复 ignore: 忽略 abort: 结束 | **Y** |        |
|  content  | Object |         回答内容, 如果是选项回答，只需传选项信息 A-Z         | **Y** |        |

**响应示例**：见1

### 4. 上传文件

```java
public String uploadFile(File file) throws IOException
```

**参数说明**：

- `file`: 需要上传的文件

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "sid": "spf001b23c7@dx1939b17d9e3a4f3700",
  "data": {
      "url": "xxxxxxxxxx"
  }
}
```

---

## 注意事项

1. 在处理 history_message object 时，首个元素的 role 必须为 user。交互历史应按照 user -> assistant -> user -> assistant 的顺序进行拼接，默认情况下，user 与 assistant 之间的一对交互视为一轮对话。按对话时间从先到后依次填充。如[{第一次},{第二次}...]
2. 当您的工作流执行到问答节点时，工作流会被暂时中断，并返回相应的事件 ID 和工作流中设定的问题。此时开发者应调用此接口把用户的答复以及事件 ID 等信息进行上传，重新恢复工作流的运行。
3. 模型结果除了普通文本类型，为了满足排版需求，会出现以下的标记语言，建议集成方进行适配：
   - markdown（表格、列表等）

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
}
```
