# 星火大模型OpenAPI协议文档


## 接口与鉴权

### 应用申请

> 能力开通地址：https://xinghuo.xfyun.cn/sparkapi


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

import cn.hutool.core.util.StrUtil;
import cn.xfyun.api.SparkChatClient;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.config.SparkModel;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.RoleContent;
import cn.xfyun.model.sparkmodel.FunctionCall;
import cn.xfyun.model.sparkmodel.WebSearch;
import cn.xfyun.model.sparkmodel.SparkChatParam;
import cn.xfyun.model.sparkmodel.response.SparkChatResponse;
import cn.xfyun.service.sparkmodel.AbstractSparkModelWebSocketListener;
import cn.xfyun.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import okio.BufferedSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

/**
 * （spark-chat）星火大模型
 * 1、APPID、APISecret、APIKey、APIPassword信息获取：<a href="https://console.xfyun.cn/services/bm4">...</a>
 * 2、文档地址：<a href="https://www.xfyun.cn/doc/spark/Web.html">...</a>
 */
public class SparkChatClientApp {

    private static final Logger logger = LoggerFactory.getLogger(SparkChatClientApp.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getApiKey();
    private static final String apiSecret = PropertiesConfig.getApiSecret();
    private static final String apiPassword = "您的apiPassword";

    public static void main(String[] args) throws Exception {
        // 封装入参
        SparkChatParam sendParam = SparkChatParam.builder()
                .messages(getMessages())
                // 增加联网搜索工具  仅Pro、Max、Ultra系列模型支持
                // .webSearch(getWebSearch())
                // 增加函数调用 仅Spark Max/4.0 Ultra 支持了该功能
                // .functions(getFunctions())
                .chatId("123456")
                // .userId("testUse_123")
                .build();

        // 使用websocket方式请求大模型
        sparkChatWs(sendParam);

        // 使用post方式请求大模型
        // sparkChatPost(sendParam);

        // 使用post流式(sse)请求大模型
        // sparkChatStream(sendParam);
    }

    private static void sparkChatWs(SparkChatParam sendParam) throws MalformedURLException, SignatureException {
        SparkChatClient client = new SparkChatClient.Builder()
                .signatureWs(appId, apiKey, apiSecret, SparkModel.SPARK_4_0_ULTRA)
                .build();

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        StringBuffer finalResult = new StringBuffer();
        client.send(sendParam, new AbstractSparkModelWebSocketListener() {

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
                            // webSocket.close(1000, "");
                        }
                    }
                }
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                logger.error(t.getMessage(), t);
                webSocket.close(1000, t.getMessage());
                System.exit(0);
            }

            @Override
            public void onClose(WebSocket webSocket, int code, String reason) {
                System.exit(0);
            }
        });
    }

    private static void sparkChatPost(SparkChatParam sendParam) throws IOException {
        SparkChatClient client = new SparkChatClient.Builder()
                .signatureHttp(apiPassword, SparkModel.SPARK_4_0_ULTRA)
                .build();

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        String send = client.send(sendParam);
        logger.debug("请求结果 ==> {}", send);

        JSONObject obj = JSON.parseObject(send);
        JSONArray choices = obj.getJSONArray("choices");
        choices.forEach(message -> {
            JSONObject messageObj = (JSONObject) message;
            String content = messageObj.getJSONObject("message").getString("content");
            if (StrUtil.isNotEmpty(content)) {
                logger.info("大模型回复结果 ==> {}", content);
            }
            String role = messageObj.getJSONObject("message").getString("role");
            if (StrUtil.isNotEmpty(role)) {
                if (role.equals("tool")) {
                    JSONArray toolCalls = messageObj.getJSONObject("message").getJSONArray("tool_calls");
                    if (null != toolCalls) {
                        logger.info("大模型工具调用调用返回结果 ==> {}", toolCalls);
                    }
                } else {
                    JSONObject toolCalls = messageObj.getJSONObject("message").getJSONObject("tool_calls");
                    if (null != toolCalls) {
                        logger.info("大模型函数调用调用返回结果 ==> {}", toolCalls);
                    }
                }
            }
        });

        logger.info("session end");
        Date dateEnd = new Date();
        logger.info("{}开始", sdf.format(dateBegin));
        logger.info("{}结束", sdf.format(dateEnd));
        logger.info("耗时：{}ms", dateEnd.getTime() - dateBegin.getTime());
    }

    private static void sparkChatStream(SparkChatParam sendParam) {
        SparkChatClient client = new SparkChatClient.Builder()
                .signatureHttp(apiPassword, SparkModel.SPARK_4_0_ULTRA)
                .build();

        // 统计耗时
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        // 最终回复结果
        StringBuilder finalResult = new StringBuilder();

        client.send(sendParam, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logger.error("sse连接失败：{}", e.getMessage());
                System.exit(0);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    logger.error("请求失败，状态码：{}，原因：{}", response.code(), response.message());
                    System.exit(0);
                    return;
                }
                ResponseBody body = response.body();
                if (body != null) {
                    BufferedSource source = body.source();
                    try {
                        while (true) {
                            String line = source.readUtf8Line();
                            if (line == null) {
                                break;
                            }
                            if (line.startsWith("data:")) {
                                if (line.contains("[DONE]")) {
                                    // 说明数据全部返回完毕，可以关闭连接，释放资源
                                    logger.info("session end");
                                    Date dateEnd = new Date();
                                    logger.info("{}开始", sdf.format(dateBegin));
                                    logger.info("{}结束", sdf.format(dateEnd));
                                    logger.info("耗时：{}ms", dateEnd.getTime() - dateBegin.getTime());
                                    logger.info("最终识别结果 ==> {}", finalResult);
                                    System.exit(0);
                                }
                                // 去掉前缀 "data: "
                                String data = line.substring(5).trim();
                                extractContent(data, finalResult);
                            }
                        }
                    } catch (IOException e) {
                        logger.error("读取sse返回内容发生异常", e);
                    }
                }
            }
        });
    }

    /**
     * @param data        sse返回的数据
     * @param finalResult 实时回复内容
     */
    private static void extractContent(String data, StringBuilder finalResult) {
        logger.debug("sse返回数据 ==> {}", data);
        try {
            JSONObject dataObj = JSON.parseObject(data);
            JSONArray choices = dataObj.getJSONArray("choices");
            choices.forEach(message -> {
                JSONObject messageObj = (JSONObject) message;
                String content = messageObj.getJSONObject("delta").getString("content");
                if (StrUtil.isNotEmpty(content)) {
                    logger.info("中间结果 ==> {}", content);
                    finalResult.append(content);
                    return;
                }
                String role = messageObj.getJSONObject("delta").getString("role");
                if (StrUtil.isNotEmpty(role)) {
                    if (role.equals("tool")) {
                        JSONArray toolCalls = messageObj.getJSONObject("delta").getJSONArray("tool_calls");
                        if (null != toolCalls) {
                            logger.info("大模型工具调用调用返回结果 ==> {}", toolCalls);
                        }
                    } else {
                        JSONObject toolCalls = messageObj.getJSONObject("delta").getJSONObject("tool_calls");
                        if (null != toolCalls) {
                            logger.info("大模型函数调用调用返回结果 ==> {}", toolCalls);
                        }
                    }
                }
            });
        } catch (BusinessException bx) {
            throw bx;
        } catch (Exception e) {
            logger.error("解析sse返回内容发生异常", e);
            logger.error("异常数据 ==> {}", data);
        }
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

    private static WebSearch getWebSearch() {
        WebSearch webSearch = new WebSearch();
        webSearch.setSearchMode("deep");
        webSearch.setShowRefLabel(Boolean.TRUE);
        webSearch.setEnable(Boolean.TRUE);
        return webSearch;
    }

    private static List<RoleContent> getMessages() {
        // 多轮交互需要将之前的交互历史按照system->user->assistant->user->assistant规则进行拼接
        List<RoleContent> messages = new ArrayList<>();

        // 会话记录
        RoleContent roleContent1 = new RoleContent();
        roleContent1.setRole("system");
        roleContent1.setContent("你是一个聊天的人工智能助手，可以和人类进行对话。");
        RoleContent roleContent2 = new RoleContent();
        roleContent2.setRole("user");
        roleContent2.setContent("你好");
        RoleContent roleContent3 = new RoleContent();
        roleContent3.setRole("assistant");
        roleContent3.setContent("你好！");
        // 当前会话
        RoleContent roleContent4 = new RoleContent();
        roleContent4.setRole("user");
        roleContent4.setContent("北京今天天气怎么样");
        // roleContent4.setContent("吴艳妮最新消息");
        // roleContent4.setContent("Stell dich vor");
        // roleContent4.setContent("今日の天気はどうですか。");
        // roleContent4.setContent("오늘 날씨 어때요?");
        // roleContent4.setContent("Какая сегодня погода?");
        // roleContent4.setContent("ما هو الطقس اليوم ؟");
        // roleContent4.setContent("Quelle est la météo aujourd'hui");
        // roleContent4.setContent("¿¿ cómo está el clima hoy?");
        // roleContent4.setContent("Como está o tempo hoje?");

        // messages.add(roleContent1);
        // messages.add(roleContent2);
        // messages.add(roleContent3);
        messages.add(roleContent4);
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

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/SparkChatClientApp.java)


### 接口域名
```apl
spark-api-open.xf-yun.com
```

## 错误码

| 错误码 | 错误信息                                                     |
| ------ | ------------------------------------------------------------ |
| 0      | 成功                                                         |
| 10000  | 升级为ws出现错误                                             |
| 10001  | 通过ws读取用户的消息出错                                     |
| 10002  | 通过ws向用户发送消息出错                                     |
| 10003  | 用户的消息格式有错误                                         |
| 10004  | 用户数据的schema错误                                         |
| 10005  | 用户参数值有错误                                             |
| 10006  | 用户并发错误：当前用户已连接，同一用户不能多处同时连接。     |
| 10007  | 用户流量受限：服务正在处理用户当前的问题，需等待处理完成后再发送新的请求。（必须要等大模型完全回复之后，才能发送下一个问题） |
| 10008  | 服务容量不足，联系工作人员                                   |
| 10009  | 和引擎建立连接失败                                           |
| 10010  | 接收引擎数据的错误                                           |
| 10011  | 发送数据给引擎的错误                                         |
| 10012  | 引擎内部错误                                                 |
| 10013  | 输入内容审核不通过，涉嫌违规，请重新调整输入内容             |
| 10014  | 输出内容涉及敏感信息，审核不通过，后续结果无法展示给用户     |
| 10015  | appid在黑名单中                                              |
| 10016  | appid授权类的错误。比如：未开通此功能，未开通对应版本，token不足，并发超过授权 等等 |
| 10017  | 清除历史失败                                                 |
| 10019  | 表示本次会话内容有涉及违规信息的倾向；建议开发者收到此错误码后给用户一个输入涉及违规的提示 |
| 10021  | 输入审核不通过                                               |
| 10110  | 服务忙，请稍后再试                                           |
| 10163  | 请求引擎的参数异常 引擎的schema 检查不通过                   |
| 10222  | 引擎网络异常                                                 |
| 10907  | token数量超过上限。对话历史+问题的字数太多，需要精简输入     |
| 11200  | 授权错误：该appId没有相关功能的授权 或者 业务量超过限制      |
| 11201  | 授权错误：日流控超限。超过当日最大访问量的限制               |
| 11202  | 授权错误：秒级流控超限。秒级并发超过授权路数限制             |
| 11203  | 授权错误：并发流控超限。并发路数超过授权路数限制             |

### 

## [#](https://www.xfyun.cn/doc/spark/Web.html#_1-接口说明)1. WS接口说明

**注意： 该接口可以正式使用。如您需要申请使用，请点击前往[产品页面 ](https://xinghuo.xfyun.cn/sparkapi?scr=price)。**

**Tips：
****1. 计费包含接口的输入和输出内容；
**

**2. 1 token约等于1.5个中文汉字 或者 0.8个英文单词；
**

**3. 仅Spark Pro, Spark Max和Spark 4.0Ultra支持[搜索]、[天气]、[日期]、[诗词]、[字词]、[股票]等内置插件；
**

**4. Spark 4.0Ultra、Max现已支持system、Function Call功能；
**

**5. 当前仅Spark 4.0Ultra、Max版本支持返回联网检索的信源标题及地址。
**

### [#](https://www.xfyun.cn/doc/spark/Web.html#_1-1-请求地址)1.1 请求地址

**通用星火大模型API当前有Lite、Pro、Pro-128K、Max、Max-32K和4.0 Ultra六个版本 和 科技文献大模型（kjwx），各版本独立计量tokens。**

**传输协议 ：ws(s),为提高安全性，强烈推荐wss**

**Spark4.0 Ultra 请求地址，对应的domain参数为4.0Ultra：**

```text
wss://spark-api.xf-yun.com/v4.0/chat
```

**Spark Max-32K请求地址，对应的domain参数为max-32k**

```text
wss://spark-api.xf-yun.com/chat/max-32k
```

**Spark Max请求地址，对应的domain参数为generalv3.5**

```text
wss://spark-api.xf-yun.com/v3.5/chat
```

**Spark Pro-128K请求地址，对应的domain参数为pro-128k：**

```text
 wss://spark-api.xf-yun.com/chat/pro-128k
```

**Spark Pro请求地址，对应的domain参数为generalv3：**

```text
wss://spark-api.xf-yun.com/v3.1/chat
```

**Spark Lite请求地址，对应的domain参数为lite：**

```text
wss://spark-api.xf-yun.com/v1.1/chat
```

**行业大模型列表如下：**

**科技文献大模型，对应的domain参数为kjwx：**

```text
wss://spark-openapi-n.cn-huabei-1.xf-yun.com/v1.1/chat_kjwx
```

### [#](https://www.xfyun.cn/doc/spark/Web.html#_1-2-接口鉴权)1.2 接口鉴权

参考[通用URL鉴权文档](https://www.xfyun.cn/doc/spark/general_url_authentication.html)

### [#](https://www.xfyun.cn/doc/spark/Web.html#_1-3-接口请求)1.3 接口请求

#### [#](https://www.xfyun.cn/doc/spark/Web.html#_1-3-1-请求参数)1.3.1 请求参数

```text
# 参数构造示例如下
{
        "header": {
            "app_id": "12345",
            "uid": "12345"
        },
        "parameter": {
            "chat": {
                "domain": "generalv3.5",
                "temperature": 0.5,
                "max_tokens": 1024, 
            }
        },
        "payload": {
            "message": {
                # 如果想获取结合上下文的回答，需要开发者每次将历史问答信息一起传给服务端，如下示例
                # 注意：text里面的所有content内容加一起的tokens需要控制在8192以内，开发者如有较长对话需求，需要适当裁剪历史信息
                "text": [
                    #如果传入system参数，需要保证第一条是system
                    {"role":"system","content":"你现在扮演李白，你豪情万丈，狂放不羁；接下来请用李白的口吻和用户对话。"} #设置对话背景或者模型角色
                    {"role": "user", "content": "你是谁"} # 用户的历史问题
                    {"role": "assistant", "content": "....."}  # AI的历史回答结果
                    # ....... 省略的历史对话
                    {"role": "user", "content": "你会做什么"}  # 最新的一条问题，如无需上下文，可只传最新一条问题
                ]
        }
    }
}
```

接口请求字段由三个部分组成：header，parameter, payload。 字段解释如下

**header部分**

| 参数名称 | 类型   | 必传 | 参数要求 | 参数说明                                                |
| -------- | ------ | ---- | -------- | :------------------------------------------------------ |
| app_id   | string | 是   |          | 应用appid，从开放平台控制台创建的应用中获取             |
| uid      | string | 否   |          | 每个用户的id，非必传字段，用于后续扩展 ，"maxLength":32 |

**parameter.chat部分**

| 参数名称                        | 类型   | 必传                       | 参数要求                                                     | 参数说明                                                     |
| ------------------------------- | ------ | -------------------------- | ------------------------------------------------------------ | :----------------------------------------------------------- |
| domain                          | string | 是                         | 取值范围为： lite、 generalv3、 pro-128k、 generalv3.5、 max-32k、 4.0Ultra、 kjwx | 指定访问的模型版本: lite 指向Lite版本; generalv3 指向Pro版本; pro-128k 指向Pro-128K版本; generalv3.5 指向Max版本; max-32k 指向Max-32K版本; 4.0Ultra 指向4.0 Ultra版本; kjwx 指向科技文献大模型（重点优化论文问答、写作等垂直领域）; **注意：不同的取值对应的url也不一样！** |
| temperature                     | float  | 否                         | 取值范围 (0，1] ，默认值0.5                                  | 核采样阈值。取值越高随机性越强，即相同的问题得到的不同答案的可能性越大 |
| max_tokens                      | int    | 否                         | Pro、Max、Max-32K、4.0 Ultra 取值为[1,8192]，默认为4096; Lite、Pro-128K 取值为[1,4096]，默认为4096。 | 模型回答的tokens的最大长度                                   |
| top_k                           | int    | 否                         | 取值为[1，6],默认为4                                         | 从k个候选中随机选择⼀个（⾮等概率）                          |
| tools                           | array  | 否                         | 通过该参数控制工具使用                                       | 工具列表                                                     |
| tools.type                      | string | 是                         | 可选值：web_search                                           | 当前工具列表中，仅支持联网搜索工具； 如需使用FunctionCall ，请参见下文对应描述 |
| tools.web_search                | object | 否，默认表示开启           | `{ "type": "web_search", "web_search": { "enable": true, "show_ref_label": true, "search_mode": "deep" } }` | **仅Pro、Max、Ultra系列模型支持**                            |
| tools.web_search.enable         | bool   | 否，默认开启（true）       | true or false                                                | enable：是否开启搜索功能，设置为true,模型会根据用户输入判断是否触发联网搜索，false则完全不触发； |
| tools.web_search.show_ref_label | bool   | 否，默认关闭（false）      | true or false                                                | show_ref_label 开关控制触发联网搜索时是否返回信源信息（仅在enable为true时生效） 如果开启，则先返回搜索结果，之后再返回模型回复内容 |
| tools.web_search.search_mode    | string | 否，默认标准搜索（normal） | deep/normal                                                  | search_mode 控制联网搜索策略（仅在enable为true时生效） normal：标准搜索模式，模型引用搜索返回的摘要内容回答问题 deep：深度搜索模式，模型引用搜索返回的全文内容，回复的更准确；同时会带来额外的token消耗（返回search_prompt字段） |

**payload.message.text部分**
**注意：1、请确保text下所有content内容累计的tokens数量在模型上下文长度的限制之内。具体可参考下文中content字段的参数要求**
**2、如果传入system参数，需要保证第一条是system；多轮交互需要将之前的交互历史按照system-user-assistant-user-assistant进行拼接**

| 参数名称 | 类型   | 必传 | 参数要求                                                     | 参数说明                                                     |
| -------- | ------ | ---- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| role     | string | 是   | 取值为[system,user,assistant]                                | system用于设置对话背景（仅Max、Ultra版本支持） user表示是用户的问题， assistant表示AI的回复 |
| content  | string | 是   | 所有content的累计tokens长度，不同版本限制不同： Lite、Pro、Max、4.0 Ultra版本: 不超过8192; Max-32K版本: 不超过32* 1024; Pro-128K版本:不超过 128*1024; | 用户和AI的对话内容                                           |

### [#](https://www.xfyun.cn/doc/spark/Web.html#_1-4-接口响应)1.4 接口响应

**在不返回检索信源的情况下，大模型流式返回结构如下：**

```text
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

在不返回检索信源的情况下，接口返回字段分为两个部分，header，payload。字段解释如下

**header部分**

| 字段名  | 类型   | 字段说明                                                     |
| ------- | ------ | ------------------------------------------------------------ |
| code    | int    | 错误码，0表示正常，非0表示出错；详细释义可在接口说明文档最后的错误码说明了解 |
| message | string | 会话是否成功的描述信息                                       |
| sid     | string | 会话的唯一id，用于讯飞技术人员查询服务端会话日志使用,出现调用错误时建议留存该字段 |
| status  | int    | 会话状态，取值为[0,1,2]；0代表首次结果；1代表中间结果；2代表最后一个结果 |

**payload.choices部分**

| 字段名  | 类型   | 字段说明                                                     |
| ------- | ------ | ------------------------------------------------------------ |
| status  | int    | 文本响应状态，取值为[0,1,2]; 0代表首个文本结果；1代表中间文本结果；2代表最后一个文本结果 |
| seq     | int    | 返回的数据序号，取值为[0,9999999]                            |
| content | string | AI的回答内容                                                 |
| role    | string | 角色标识，固定为assistant，标识角色为AI                      |
| index   | int    | 结果序号，取值为[0,10]; 当前为保留字段，开发者可忽略         |

**payload.usage部分(在最后一次结果返回)**

| 字段名            | 类型 | 字段说明                                                     |
| ----------------- | ---- | ------------------------------------------------------------ |
| question_tokens   | int  | 保留字段，可忽略                                             |
| prompt_tokens     | int  | 包含历史问题的总tokens大小                                   |
| completion_tokens | int  | 回答的tokens大小                                             |
| total_tokens      | int  | prompt_tokens和completion_tokens的和，也是本次交互计费的tokens大小 |

**在返回检索信源的情况下，在大模型返回结果之前会先返回检索信源，结构如下：**

```text
{
    "header": {
        "code": 0,
        "message": "Success",
        "sid": "cht000b79a4@dx190da456b5db80a560",
        "status": 1
    },
    "payload": {
        "plugins": {
            "text": [
                {
                    "name": "ifly_search",
                    "content": "[{\"index\":1,\"url\":\"https://baike.baidu.com/item/%E6%9B%B9%E6%93%8D/6772\",\"title\":\"曹操（中国东汉末年权臣，曹魏政权的奠基者）_百度百科\"},{\"index\":2,\"url\":\"https://zhidao.baidu.com/question/437349472.html\",\"title\":\"曹操是哪一年出生的？ - 百度知道\"},{\"index\":3,\"url\":\"http://www.lidaishi.com/default.aspx?did=130019\",\"title\":\"曹操的一生事迹简介-历代史历史网\"},{\"index\":4,\"url\":\"https://zhidao.baidu.com/question/374585705.html\",\"title\":\"曹操生于哪一年? - 百度知道\"},{\"index\":5,\"url\":\"https://baike.baidu.hk/item/%E6%9B%B9%E6%93%8D/6772\",\"title\":\"曹操（中國東漢末年權臣，曹魏政權的奠基者）_百度百科\"}]",
                    "content_type": "text",
                    "content_meta": null,
                    "role": "tool",
                    "status": "finished",
                    "invoked": {
                        "namespace": "ifly_search",
                        "plugin_id": "ifly_search",
                        "plugin_ver": "",
                        "status_code": 200,
                        "status_msg": "Success",
                        "type": "local"
                    }
                }
            ]
        }
    }
}
```

**解析检索信源Python示例：**

```text
if('plugins' in data['payload']):
    text_list = data['payload']['plugins']['text']
    search_refer = text_list[0]
    refer_content = search_refer['content']
    refer_list = json.loads(refer_content)
    print("参考内容：")
    for line in refer_list:
        num = line['index']
        url = line['url']
        title = line['title']
        print(str(num) + "、" + title + "[ " + url + " ]")
```

## [#](https://www.xfyun.cn/doc/spark/Web.html#_2-function-call说明)2.Function Call说明

**Function call 作为大模型能力扩展的核心，支持大模型在交互过程中识别出需要调度的外部接口：**
**注：当前仅Spark Max/4.0 Ultra 支持了该功能；需要请求参数payload.functions中申明大模型需要辨别的外部接口，申明方式见下方请求示例**

### [#](https://www.xfyun.cn/doc/spark/Web.html#_2-1接口请求)2.1接口请求

#### [#](https://www.xfyun.cn/doc/spark/Web.html#_2-1-1-请求示例)2.1.1 请求示例

```text
# 参数构造示例如下,仅在原本生成的基础上，增加了functions.text字段，用于方法的注册
{
        "header": {
            "app_id": appid,
            "uid": "1234"
        },
        "parameter": {

            "chat": {
                "domain": domain,
                "random_threshold": 0.5,
                "max_tokens": 2048,
                "auditing": "default"
            }
        },
        "payload": {
            "message": {
                "text": [
                    {"role": "user", "content": ""} # 用户的提问
                    ]
            },
            "functions": {
                "text": [
                    {
                        "name": "天气查询",
                        "description": "天气插件可以提供天气相关信息。你可以提供指定的地点信息、指定的时间点或者时间段信息，来精准检索到天气信息。",
                        "parameters": {
                            "type": "object",
                            "properties": {
                                "location": {
                                    "type": "string",
                                    "description": "地点，比如北京。"
                                },
                                "date": {
                                    "type": "string",
                                    "description": "日期。"
                                }
                            },
                            "required": [
                                "location"
                            ]
                        }
                    },
                    {
                        "name": "税率查询",
                        "description": "税率查询可以查询某个地方的个人所得税率情况。你可以提供指定的地点信息、指定的时间点，精准检索到所得税率。",
                        "parameters": {
                            "type": "object",
                            "properties": {
                                "location": {
                                    "type": "string",
                                    "description": "地点，比如北京。"
                                },
                                "date": {
                                    "type": "string",
                                    "description": "日期。"
                                }
                            },
                            "required": [
                                "location"
                            ]
                        }
                    }
                ]
            }
        }
    }
```

#### [#](https://www.xfyun.cn/doc/spark/Web.html#_2-1-2参数说明)2.1.2参数说明

接口请求payload.functions字段解释如下：

| 参数名称                 | 类型   | 必传 | 参数要求                         | 参数说明                                                 |
| ------------------------ | ------ | ---- | -------------------------------- | -------------------------------------------------------- |
| text                     | array  | 是   | 列表形式，列表中的元素是json格式 | 元素中包含name、description、parameters属性              |
| name                     | string | 是   | function名称                     | 用户输入命中后，会返回该名称                             |
| description              | string | 是   | function功能描述                 | 描述function功能即可，越详细越有助于大模型理解该function |
| parameters               | json   | 是   | function参数列表                 | 包含type、properties、required字段                       |
| parameters.type          | string | 是   | 参数类型                         |                                                          |
| parameters.properties    | string | 是   | 参数信息描述                     | 该内容由用户定义，命中该方法时需要返回哪些参数           |
| properties.x.type        | string | 是   | 参数类型描述                     | 该内容由用户定义，需要返回的参数是什么类型               |
| properties.x.description | string | 是   | 参数详细描述                     | 该内容由用户定义，需要返回的参数的具体描述               |
| parameters.required      | array  | 是   | 必须返回的参数列表               | 该内容由用户定义，命中方法时必须返回的字段               |

### [#](https://www.xfyun.cn/doc/spark/Web.html#_2-2接口响应)2.2接口响应

#### [#](https://www.xfyun.cn/doc/spark/Web.html#_2-2-1示例如下)2.2.1示例如下

```text
// 触发了function_call的情况下，只会返回一帧结果，其中status 为2
{"header":{"code":0,"message":"Success","sid":"cht000b41d5@dx18b851e6931b894550","status":2},"payload":{"choices":{"status":2,"seq":0,"text":[{"content":"","role":"assistant","content_type":"text","function_call":{"arguments":"{\"datetime\":\"今天\",\"location\":\"合肥\"}","name":"天气查询"},"index":0}]},"usage":{"text":{"question_tokens":3,"prompt_tokens":3,"completion_tokens":0,"total_tokens":3}}}}
```

#### [#](https://www.xfyun.cn/doc/spark/Web.html#_2-2-2返回字段说明)2.2.2返回字段说明

| 字段名                  | 类型   | 字段说明                         |
| ----------------------- | ------ | -------------------------------- |
| function_call           | json   | function call 返回结果           |
| function_call.arguments | json   | 客户在请求体中定义的参数及参数值 |
| function_call.name      | string | 客户在请求体中定义的方法名称     |



## [#](https://www.xfyun.cn/doc/spark/HTTP调用文档.html#_1-接口说明)3. POST接口说明

**注意： 该接口可以正式使用。如您需要申请使用，请点击前往[产品页面 ](https://xinghuo.xfyun.cn/sparkapi?scr=price)领取免费额度。**

**Tips：**

**1. 计费包含接口的输入和输出内容；**

**2. 1 token约等于1.5个中文汉字 或者 0.8个英文单词；**

**3. 仅Spark Pro, Spark Max和Spark 4.0Ultra支持[搜索]、[天气]、[日期]、[诗词]、[字词]、[股票]等内置插件；**

**4. Spark 4.0Ultra、Max现已支持system、Function Call功能；**

**5. 当前仅Spark 4.0Ultra、Max版本支持返回联网检索的信源标题及地址。**

## [#](https://www.xfyun.cn/doc/spark/HTTP调用文档.html#_2-请求地址)4. POST请求地址

**Tips: 星火大模型API当前有Lite、Pro、Pro-128K、Max、Max-32K和4.0 Ultra六个版本，各版本独立计量tokens。**

```text
https://spark-api-open.xf-yun.com/v1/chat/completions
```

## [#](https://www.xfyun.cn/doc/spark/HTTP调用文档.html#_3-请求说明)5. 请求说明

### [#](https://www.xfyun.cn/doc/spark/HTTP调用文档.html#_3-1-请求头)5.1. 请求头

请到[**控制台** ](https://console.xfyun.cn/services/cbm)对应版本页面，获取http服务接口认证信息中的APIPassword ,假如获取到的值为123456，则请求头如下：

```text
Content-Type: application/json
Authorization: Bearer 123456
```

利用上方的请求头发起请求示例如下：

```shell
curl -i -k -X POST 'https://spark-api-open.xf-yun.com/v1/chat/completions' \
--header 'Authorization: Bearer 123456' \#注意此处把“123456”替换为自己的APIPassword
--header 'Content-Type: application/json' \
--data '{
    "model":"generalv3.5",
    "messages": [
        {
            "role": "user",
            "content": "来一个只有程序员能听懂的笑话"
        }
    ],
    "stream": true
}'
```

### [#](https://www.xfyun.cn/doc/spark/HTTP调用文档.html#_3-2-请求参数)5.2. 请求参数

```json
{
    "model": "generalv3.5",
    "user": "用户唯一id",
    "messages": [
        {
            "role": "system",
            "content": "你是知识渊博的助理"
        },
        {
            "role": "user",
            "content": "你好，讯飞星火"
        }
    ],
    // 下面是可选参数
    "temperature": 0.5,
    "top_k": 4,
    "stream": false,
    "max_tokens": 1024,
    "presence_penalty": 1,
    "frequency_penalty": 1,
    "tools": [
        {
            "type": "function",
            "function": {
                "name": "str2int",
                "description": "将字符串类型转为 int 类型",
                "parameters": {...} // 需要符合 json schema 格式
            }
        },
        {
            "type": "web_search",
            "web_search": {
                "enable": true
                "show_ref_label":true
                "search_mode":"deep" // deep:深度搜索 / normal:标准搜索,不同的搜索策略，效果不同，并且token消耗也有差异
            }
        }
    ],
    "response_format": {
        "type": "json_object"
    },
    "suppress_plugin": [
        "knowledge"
    ]
}
```

| 参数名称                        | 类型                      | 是否必传                   | 取值范围                                                     | 描述                                                         |
| ------------------------------- | ------------------------- | -------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| model                           | string                    | 是                         | lite generalv3 pro-128k generalv3.5 max-32k 4.0Ultra         | 指定访问的模型版本: lite指向Lite版本; generalv3指向Pro版本; pro-128k指向Pro-128K版本; generalv3.5指向Max版本; max-32k指向Max-32K版本; 4.0Ultra指向4.0 Ultra版本; |
| user                            | string                    | 否                         | 自定义                                                       | 用户的唯一id，表示一个用户，user_123456                      |
| messages                        | array                     | 是                         |                                                              | 输入数组                                                     |
| messages.role                   | string                    | 是                         | user assistant system tool                                   | 角色，user表示用户，assistant表示大模型，system表示命令，tool代表function call执行结果 |
| messages.content                | string                    | 是                         |                                                              | 角色对应的文本内容                                           |
| temperature                     | float                     | 否                         | 取值范围[0, 2] 默认值1.0                                     | 核采样阈值                                                   |
| top_p                           | int                       | 否                         | 取值范围(0, 1] 默认值1                                       | 生成过程中核采样方法概率阈值，例如，取值为0.8时，仅保留概率加起来大于等于0.8的最可能token的最小集合作为候选集。取值越大，生成的随机性越高；取值越低，生成的确定性越高。 |
| top_k                           | int                       | 否                         | 取值范围[1, 6] 默认值4                                       | 从k个中随机选择一个(非等概率)                                |
| presence_penalty                | float                     | 否                         | 取值范围[-2.0,2.0] 默认0                                     | 重复词的惩罚值                                               |
| frequency_penalty               | float                     | 否                         | 取值范围[-2.0,2.0] 默认0                                     | 频率惩罚值                                                   |
| stream                          | bool                      | 否                         | true false                                                   | 是否流式返回结果。默认是false 表示非流式。 如果使用流式，服务端使用SSE的方式推送结果，客户端自己适配处理结果。 |
| max_tokens                      | int                       | 否                         | Pro、Max、Max-32K、4.0 Ultra 取值为[1,8192]，默认为4096; Lite、Pro-128K 取值为[1,4096]，默认为4096。 | 模型回答的tokens的最大长度                                   |
| response_format                 | object                    | 否                         |                                                              | 指定模型的输出格式                                           |
| response_format.type            | string                    | 否                         | text json_object                                             | { "type": "json_object" } 指定模型输出json格式 使用 JSON 模式时，请始终指示模型通过对话中的某些消息（例如通过系统或用户消息）生成 JSON |
| tools                           | array                     | 否                         | 在这里用户可以控制联网搜索工具或者自定义各类function         | 工具列表                                                     |
| tools.web_search                | object                    | 否，默认表示开启           | `{ "type": "web_search", "web_search": { "enable": true, "show_ref_label": true, "search_mode": "deep" } }` | **仅Pro、Max、Ultra系列模型支持**                            |
| tools.web_search.enable         | bool                      | 否，默认开启（true）       | true or false                                                | enable：是否开启搜索功能，设置为true,模型会根据用户输入判断是否触发联网搜索，false则完全不触发； |
| tools.web_search.show_ref_label | bool                      | 否，默认关闭（false）      | true or false                                                | show_ref_label 开关控制触发联网搜索时是否返回信源信息（仅在enable为true时生效） 如果开启，则先返回搜索结果，之后再返回模型回复内容 |
| tools.web_search.search_mode    | string                    | 否，默认标准搜索（normal） | deep/normal                                                  | search_mode 控制联网搜索策略（仅在enable为true时生效） normal：标准搜索模式，模型引用搜索返回的摘要内容回答问题 deep：深度搜索模式，模型引用搜索返回的全文内容，回复的更准确；同时会带来额外的token消耗（返回search_prompt字段） |
| tools.function                  | object                    | 否                         | 示例： {"type":"function", "function":{"name": "my_function", "description": "xxx", "parameters": {...}}} |                                                              |
| tools.function.name             | string                    | 是                         | 用户自定义，长度不超过32                                     | 工具函数的名称，必须是字母、数字，可以包含下划线             |
| tools.function.description      | string                    | 是                         | 用户自定义                                                   | 工具函数的描述，供模型选择何时调用该工具                     |
| tools.function.parameters       | string                    | 是                         | 用户自定义                                                   | 工具的参数描述，需要是一个合法的JSON Schema                  |
| tool_calls_switch               | bool                      | 否，默认表示关闭           | true or false                                                | 设置为true时，触发function call结果中tool_calls以数组格式返回，默认为 false，则以json格式返回 |
| tool_choice                     | string or object Optional | 否                         | auto none required {"type": "function", "function": {"name": "my_function"}} | 设置模型自动选择调用的函数： auto：传了tool时默认为auto，模型自动选择调用的函数 none：模型禁用函数调用 required：模型始终选择一个或多个函数进行调用 {"type": "function", "function": {"name": "my_function"}} ：模型强制调用指定函数 |

## [#](https://www.xfyun.cn/doc/spark/HTTP调用文档.html#_4-响应参数)6. 响应参数

**请求错误时的响应格式：**

API

```json
{
    "error": {
        "message": "invalid user",
        "type": "api_error",
        "param": null,
        "code": null
    }
}
```

SDK

```text
openai.AuthenticationError: Error code: 401 - {'error': {'message': 'invalid user', 'type': 'api_error', 'param': None, 'code': None}}
```

**非流式请求成功时的响应格式：**

```json
{
    "code": 0,
    "message": "Success",
    "sid": "cha000b0003@dx1905cd86d6bb86d552",
    "choices": [
        {
            "message": {
                "role": "assistant",
                "content": "你好，我是由科大讯飞构建的星火认知智能模型。\n如果你有任何问题或者需要帮助的地方，请随时告诉我！我会尽力为你提供解答和支持。请问有什么可以帮到你的吗？"
            },
            "index": 0
        }
    ],
    "usage": {
        "prompt_tokens": 6,
        "completion_tokens": 42,
        "total_tokens": 48
    }
}
```

**响应的参数说明：**

| 参数名称                | 类型   | 描述                               |
| ----------------------- | ------ | ---------------------------------- |
| code                    | int    | 错误码：0表示成功，非0表示错误     |
| message                 | string | 错误码的描述信息                   |
| sid                     | string | 本次请求的唯一id                   |
| choices                 | array  | 大模型结果的数组                   |
| choices.message         | object | 大模型结果                         |
| choices.message.role    | string | 大模型的角色                       |
| choices.message.content | string | 大模型输出的内容                   |
| choices.index           | int    | 大模型的结果序号，在多候选中使用   |
| usage                   | object | 本次请求消耗的token数量            |
| usage.prompt_tokens     | int    | 用户输入信息，消耗的token数量      |
| usage.completion_tokens | int    | 大模型输出信息，消耗的token数量    |
| usage.total_tokens      | int    | 用户输入+大模型输出，总的token数量 |

流式请求成功时的响应格式：

```json
data:{"code":0,"message":"Success","sid":"cha000b000c@dx1905cf38fc8b86d552","id":"cha000b000c@dx1905cf38fc8b86d552","created":1719546385,"choices":[{"delta":{"role":"assistant","content":"你好"},"index":0}]}

data:{"code":0,"message":"Success","sid":"cha000b000c@dx1905cf38fc8b86d552","id":"cha000b000c@dx1905cf38fc8b86d552","created":1719546385,"choices":[{"delta":{"role":"assistant","content":"，很高兴"},"index":0}]}

data:{"code":0,"message":"Success","sid":"cha000b000c@dx1905cf38fc8b86d552","id":"cha000b000c@dx1905cf38fc8b86d552","created":1719546385,"choices":[{"delta":{"role":"assistant","content":"为你解答问题"},"index":0}]}

data:{"code":0,"message":"Success","sid":"cha000b000c@dx1905cf38fc8b86d552","id":"cha000b000c@dx1905cf38fc8b86d552","created":1719546385,"choices":[{"delta":{"role":"assistant","content":"。\n"},"index":0}]}

data:{"code":0,"message":"Success","sid":"cha000b000c@dx1905cf38fc8b86d552","id":"cha000b000c@dx1905cf38fc8b86d552","created":1719546387,"choices":[{"delta":{"role":"assistant","content":"我是讯飞星火认知大模型，由科大讯飞构建的认知智能系统。"},"index":0}]}

data:{"code":0,"message":"Success","sid":"cha000b000c@dx1905cf38fc8b86d552","id":"cha000b000c@dx1905cf38fc8b86d552","created":1719546388,"choices":[{"delta":{"role":"assistant","content":"我具备与人类进行自然交流的能力，可以高效地满足各领域的认知智能需求。"},"index":0}]}

data:{"code":0,"message":"Success","sid":"cha000b000c@dx1905cf38fc8b86d552","id":"cha000b000c@dx1905cf38fc8b86d552","created":1719546389,"choices":[{"delta":{"role":"assistant","content":"无论你有什么问题或者需要帮助的地方，我都将尽我所能提供支持和解决方案。请随时告诉我你的需求！"},"index":0}]}

data:{"code":0,"message":"Success","sid":"cha000b000c@dx1905cf38fc8b86d552","id":"cha000b000c@dx1905cf38fc8b86d552","created":1719546389,"choices":[{"delta":{"role":"assistant","content":""},"index":0}],"usage":{"prompt_tokens":6,"completion_tokens":68,"total_tokens":74}}

data:[DONE]
```

**响应的参数说明：**

| 参数名称                | 类型   | 描述                               |
| ----------------------- | ------ | ---------------------------------- |
| code                    | int    | 错误码：0表示成功，非0表示错误     |
| message                 | string | 错误码的描述信息                   |
| sid                     | string | 本次请求的唯一id                   |
| choices                 | array  | 大模型结果的数组                   |
| choices.delta           | object | 大模型结果                         |
| choices.delta.role      | string | 大模型的角色                       |
| choices.delta.content   | string | 大模型输出的内容                   |
| choices.index           | int    | 大模型的结果序号，在多候选中使用   |
| usage                   | object | 本次请求消耗的token数量            |
| usage.prompt_tokens     | int    | 用户输入信息，消耗的token数量      |
| usage.completion_tokens | int    | 大模型输出信息，消耗的token数量    |
| usage.total_tokens      | int    | 用户输入+大模型输出，总的token数量 |
