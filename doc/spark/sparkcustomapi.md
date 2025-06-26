# 星火可定制化大模型OpenAPI文档

## 简介

本客户端基于讯飞Spark API实现，提供星火自定义大模型调用能力[官方文档](https://www.xfyun.cn/doc/spark/OptionalAPI.html)

## 功能列表

| 方法名   | 功能说明                |
| -------- | ----------------------- |
| send()   | websocket方式请求大模型 |
| create() | 创建知识库              |
| upload() | 上传文件到知识库        |

## 使用准备

1. 前往[星火认知大模型](https://console.xfyun.cn/services/custom_api)开通能力
3. 开通后获取以下凭证:
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
    <version>2.1.4</version>
</dependency>
```

2、Java代码

```java
package cn.xfyun.demo.spark;

import cn.xfyun.api.SparkCustomClient;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.model.sparkmodel.FileContent;
import cn.xfyun.model.sparkmodel.FunctionCall;
import cn.xfyun.model.sparkmodel.request.KnowledgeFileUpload;
import cn.xfyun.model.sparkmodel.response.SparkChatResponse;
import cn.xfyun.service.sparkmodel.AbstractSparkModelWebSocketListener;
import cn.xfyun.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

/**
 * （spark-customize）星火自定义大模型
 * 1、APPID、APISecret、APIKey、APIPassword信息获取：<a href="https://console.xfyun.cn/services/custom_api">...</a>
 * 2、文档地址：<a href="https://www.xfyun.cn/doc/spark/OptionalAPI.html">...</a>
 */
public class SparkCustomClientApp {

    private static final Logger logger = LoggerFactory.getLogger(SparkCustomClientApp.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getApiKey();
    private static final String apiSecret = PropertiesConfig.getApiSecret();
    private static String filePath;
    private static String resourcePath;

    static {
        try {
            filePath = "document/private.md";
            resourcePath = Objects.requireNonNull(SparkBatchClientApp.class.getResource("/")).toURI().getPath();
        } catch (URISyntaxException e) {
            logger.error("获取资源路径失败", e);
        }
    }

    public static void main(String[] args) throws Exception {
        // 创建知识库
        create("test-1");

        // 上传文件到知识库
        String fileId = upload(new File(resourcePath + filePath), "test-1");

        // 使用知识库文件进行大模型问答(可使用函数调用)
        sparkChatWs(getMessages(fileId), null);
    }

    private static void sparkChatWs(List<FileContent> messages, List<FunctionCall> functions) throws MalformedURLException, UnsupportedEncodingException, SignatureException, InterruptedException {
        SparkCustomClient client = new SparkCustomClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        StringBuffer finalResult = new StringBuffer();
        client.send(messages, functions, new AbstractSparkModelWebSocketListener() {

            @Override
            public void onSuccess(WebSocket webSocket, SparkChatResponse resp) {
                if (resp.getHeader().getCode() != 0) {
                    logger.error("code=>{}，error=>{}，sid=>{}", resp.getHeader().getCode(), resp.getHeader().getMessage(), resp.getHeader().getSid());
                    logger.warn("错误码查询链接：https://www.xfyun.cn/doc/spark/%E7%B2%BE%E8%B0%83%E6%9C%8D%E5%8A%A1API-websocket.html");
                    System.exit(0);
                    return;
                }

                if (null != resp.getPayload()) {
                    if (null != resp.getPayload().getPlugins()) {
                        List<SparkChatResponse.Payload.Plugin.Text> plugins = resp.getPayload().getPlugins().getText();
                        if (null != plugins && !plugins.isEmpty()) {
                            logger.info("本次会话使用了插件，数量：{}", plugins.size());
                            IntStream.range(0, plugins.size()).forEach(index -> {
                                SparkChatResponse.Payload.Plugin.Text plugin = plugins.get(index);
                                logger.info("插件{} ==> 类型：{}，插件内容：{}", index + 1, plugin.getName(), plugin.getContent());
                            });
                        }
                    }
                    if (null != resp.getPayload().getChoices()) {
                        List<SparkChatResponse.Payload.Choices.Text> text = resp.getPayload().getChoices().getText();
                        // 是否进行了函数调用
                        if (null != text && !text.isEmpty()) {
                            IntStream.range(0, text.size()).forEach(index -> {
                                String content = resp.getPayload().getChoices().getText().get(index).getContent();
                                SparkChatResponse.Payload.Choices.Text.FunctionCall call = resp.getPayload().getChoices().getText().get(index).getFunctionCall();
                                if (null != call) {
                                    logger.info("函数{} ==> 名称：{}，函数调用内容：{}", index + 1, call.getName(), call.getArguments());
                                }
                                if (!StringUtils.isNullOrEmpty(content)) {
                                    finalResult.append(content);
                                    logger.info("中间结果 ==> {}", content);
                                }
                            });
                        }

                        if (resp.getPayload().getChoices().getStatus() == 2) {
                            // 说明数据全部返回完毕，可以关闭连接，释放资源
                            logger.info("session end");
                            Date dateEnd = new Date();
                            logger.info("{}开始", sdf.format(dateBegin));
                            logger.info("{}结束", sdf.format(dateEnd));
                            logger.info("耗时：{}ms", dateEnd.getTime() - dateBegin.getTime());
                            logger.info("最终识别结果 ==> {}", finalResult);
                            logger.info("本次识别sid ==> {}", resp.getHeader().getSid());
                            webSocket.close(1000, "");
                        }
                    }
                }
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                webSocket.close(1000, t.getMessage());
                System.exit(0);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
                webSocket.close(1000, t.getMessage());
                System.exit(0);
            }

            @Override
            public void onClose(WebSocket webSocket, int code, String reason) {
                System.exit(0);
            }
        });
    }

    private static String upload(File file, String knowledgeName) throws IOException {
        SparkCustomClient client = new SparkCustomClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        KnowledgeFileUpload upload = KnowledgeFileUpload.builder()
                .file(file)
                .knowledgeName(knowledgeName)
                .build();
        String result = client.upload(upload);
        logger.info("上传文件到知识库返回结果 ==> {}", result);
        //获取文件ID
        return JSON.parseObject(result).getJSONObject("result").getString("file_id");
    }

    private static void create(String knowledgeName) throws IOException {
        SparkCustomClient client = new SparkCustomClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        String result = client.create(knowledgeName);
        logger.info("创建知识库接口返回结果 ==> {}", result);
    }

    private static List<FunctionCall> getFunctions() {
        List<FunctionCall> functions = new ArrayList<>();
        FunctionCall function = new FunctionCall();
        function.setName("天气查询");
        function.setDescription("天气插件可以提供天气相关信息。你可以提供指定的地点信息、指定的时间点或者时间段信息，来精准检索到天气信息。");
        function.setParameters(getParameters());
        functions.add(function);
        return functions;
    }

    private static List<FileContent> getMessages(String... fileId) {
        // 多轮交互需要将之前的交互历史按照system->user->assistant->user->assistant规则进行拼接
        List<FileContent> messages = new ArrayList<>();

        // 会话记录
        FileContent fileContent1 = new FileContent();
        fileContent1.setRole("system");
        fileContent1.setContent("你是一个聊天的人工智能助手，可以和人类进行对话。");

        FileContent fileContent2 = new FileContent();
        fileContent2.setRole("user");
        fileContent2.setContent("你好");

        FileContent fileContent3 = new FileContent();
        fileContent3.setRole("assistant");
        fileContent3.setContent("你好！");

        FileContent fileContent4 = new FileContent();
        fileContent4.setRole("user");

        List<FileContent.Content> content = new ArrayList<>();
        FileContent.Content content1 = new FileContent.Content();
        content1.setType("file");
        content1.setFile(Arrays.asList(fileId));
        content.add(content1);

        FileContent.Content content2 = new FileContent.Content();
        content2.setType("text");
        content2.setText("帮我总结一下这一篇文章讲的什么");
        content.add(content2);

        fileContent4.setRole("user");
        fileContent4.setContent(content);

        // messages.add(roleContent1);
        // messages.add(roleContent2);
        // messages.add(roleContent3);
        messages.add(fileContent4);
        return messages;
    }

    private static FunctionCall.Parameters getParameters() {
        FunctionCall.Parameters parameters = new FunctionCall.Parameters();
        parameters.setType("object");
        // 函数的字段
        JSONObject properties = new JSONObject();
        // 字段1 地点
        FunctionCall.Parameters.Field location = new FunctionCall.Parameters.Field();
        location.setType("string");
        location.setDescription("地点，比如北京。");
        // 字段2 日期
        FunctionCall.Parameters.Field date = new FunctionCall.Parameters.Field();
        date.setType("string");
        date.setDescription("日期。");
        // 放到properties转换成字符串存储
        properties.put("location", location);
        properties.put("date", date);
        parameters.setProperties(properties.toJSONString());
        // 必须要返回的字段(示例: 返回地点)
        parameters.setRequired(Collections.singletonList("location"));
        return parameters;
    }
}

```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/SparkCustomClientApp.java)

## 大模型参数

| 字段               | 类型   | 是否必传 | 含义                         | 备注                                                         |
| ------------------ | ------ | -------- | ---------------------------- | ------------------------------------------------------------ |
| domain             | string | 否       | 指定访问的模型版本           | 取值为max                                                    |
| temperature        | float  | 否       | 取值范围 (0，1] ，默认值0.5  | 核采样阈值。取值越高随机性越强，即相同的问题得到的不同答案的可能性越大 |
| maxTokens          | int    | 否       | 取值范围[1,8192]，默认为4096 | 模型回答的tokens的最大长度                                   |
| topK               | int    | 否       | 取值为[1，6],默认为4         | 从k个候选中随机选择⼀个（⾮等概率）                          |
| topP               | int    | 否       | 取值范围(0, 1] 默认值1       | 生成过程中核采样方法概率阈值                                 |
| functions          | array  | 否       | 通过该参数控制工具使用       | 工具列表                                                     |
| uploadFileUrl      | string | 否       | 知识库文件上传接口url        | 默认https://sparkcube-api.xf-yun.com/v1/files                |
| createKnowledgeUrl | string | 否       | 创建知识库的url              | 默认https://sparkcube-api.xf-yun.com/v1/knowledge/create     |
| userId             | string | 否       | 每个用户的id                 | 非必传字段，用于后续扩展                                     |

## 方法详解

### 1. websocket调用方式
```java
public void send(List<FileContent> text, List<FunctionCall> functions, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException
```
**参数说明**：

|       名称        |  类型  |                             描述                             | 必须 | 默认值 |
| :---------------: | :----: | :----------------------------------------------------------: | ---- | ------ |
|     functions     | Array  | 支持大模型在交互过程中识别出需要调度的外部接口 触发了function_call的情况下，只会返回一帧结果，其中status 为2 | N    |        |
|     messages      | Array  | 对话记录和当前问题列表集合 所有content的累计tokens长度，<br />不同版本限制不同： <br />Lite、Pro、Max、4.0 Ultra版本: 不超过8192; <br /><br />Max-32K版本: 不超过32* 1024; Pro-128K版本:不超过 128*1024; | Y    |        |
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

### 2. 创建知识库
```java
public String create(String knowledgeName) throws IOException 
```
**参数说明**：

**请求头**

| 字段名  | 类型   | 是否必传 | 含义                       | 备注 | 限制          |
| ------- | ------ | -------- | -------------------------- | ---- | ------------- |
| x-appid | string | 是       | 应用的app_id，在控制台查看 |      | "maxLength":8 |

**请求体**

| 字段名 | 类型   | 是否必传 | 含义       | 备注                                                       | 限制                   |
| ------ | ------ | -------- | ---------- | ---------------------------------------------------------- | ---------------------- |
| kb_id  | string | 是       | 知识库名称 | 仅支持：中文 英文 数字 _- 需要保证唯一性，建议加上业务前缀 | 长度不超过32，合法字符 |

**响应示例**：

```json
{
    "code": 0,
    "message": "",
    "sid": ""
}
```

---

### 3. 上传文件到知识库
```java
public String upload(KnowledgeFileUpload upload) throws IOException
```
**参数说明**：

**请求头**

| 字段名  | 类型   | 是否必传 | 含义                       | 备注 | 限制          |
| ------- | ------ | -------- | -------------------------- | ---- | ------------- |
| x-appid | string | 是       | 应用的app_id，在控制台查看 |      | "maxLength":8 |

**请求体**

注意：请求体为表单数据，采用Content-Type: multipart/form-data传输文件数据

| 字段名  | 类型   | 是否必传 | 含义                                               | 备注                   | 限制 |
| ------- | ------ | -------- | -------------------------------------------------- | ---------------------- | ---- |
| purpose | string | 是       | 该文件使用功能，目前取值：文件提取（file-extract） |                        |      |
| kb_id   | string | 否       | 知识库名称，当purpose为 file-extract 必传          | 通过知识库创建接口创建 |      |

**响应示例**：

```text
{
    "code": 0,
    "message": "",
    "sid": "",
    "result": {
        "file_id": "file-abc123",
        "object": "file",
        "bytes": 120000,
        "created_at": 1677610602,
        "file_name": "mydata.pdf",
        "purpose": "file-extract"
    }
}
```

---

## 注意事项

1. 仅Spark Pro, Spark Max和Spark 4.0Ultra支持[搜索]、[天气]、[日期]、[诗词]、[字词]、[股票]等内置插件；
2. Spark 4.0Ultra、Max现已支持system、Function Call功能；
3. 当前仅Spark 4.0Ultra、Max版本支持返回联网检索的信源标题及地址
4. 多语种当前仅支持日、韩、俄、阿、法、西、葡、德 8种语言
5. 所有content的累计tokens长度，不同版本限制不同：
   Lite、Pro、Max、4.0 Ultra版本: 不超过8192;
   Max-32K版本: 不超过32* 1024;
   Pro-128K版本:不超过 128*1024;
6. 如果传入system参数，需要保证第一条是system；多轮交互需要将之前的交互历史按照system-user-assistant-user-assistant进行拼接

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
