# 星火大模型OpenAPI文档

## 简介

本客户端基于讯飞Spark API实现，提供星火大模型调用能力[官方文档](https://www.xfyun.cn/doc/spark/Web.html)

## 功能列表

| 方法名 | 功能说明                                                    |
| ------ | ----------------------------------------------------------- |
| send() | 提供三种调用方式: 1-websocket ; 2-post请求 ; 3- sse流式请求 |

## 使用准备

1. 前往[星火认知大模型](https://xinghuo.xfyun.cn/sparkapi)开通能力
3. 开通后获取以下凭证:
   - appId
   - apiKey
   - apiSecret
   - apiPassword

## 快速开始

1、添加maven依赖

```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-spark</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.1.1</version>
</dependency>
```

2、Java代码

```java
import cn.hutool.core.util.StrUtil;
import cn.xfyun.api.SparkChatClient;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.config.SparkModel;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sparkmodel.RoleContent;
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
                            webSocket.close(1000, "");
                        }
                    }
                }
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
                logger.error(t.getMessage(), t);
                webSocket.close(1000, t.getMessage());
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
                    if (client.getToolCallsSwitch()) {
                        JSONArray toolCalls = messageObj.getJSONObject("message").getJSONArray("tool_calls");
                        if (null != toolCalls) {
                            logger.info("大模型函数调用调用返回结果 ==> {}", toolCalls);
                        }
                    } else {
                        JSONObject toolCalls = messageObj.getJSONObject("message").getJSONObject("tool_calls");
                        if (null != toolCalls) {
                            logger.info("大模型函数调用调用返回结果 ==> {}", toolCalls);
                        }
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
        // roleContent4.setContent("光刻机的原理是什么");
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

## 错误码

### 1.1 ws错误码列表

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

### 1.2 http错误码列表

| 错误码 | 错误信息                                                     |
| ------ | ------------------------------------------------------------ |
| 0      | 成功                                                         |
| 10007  | 用户流量受限：服务正在处理用户当前的问题，需等待处理完成后再发送新的请求。（必须要等大模型完全回复之后，才能发送下一个问题） |
| 10013  | 输入内容审核不通过，涉嫌违规，请重新调整输入内容             |
| 10014  | 输出内容涉及敏感信息，审核不通过，后续结果无法展示给用户     |
| 10019  | 表示本次会话内容有涉及违规信息的倾向；建议开发者收到此错误码后给用户一个输入涉及违规的提示 |
| 10907  | token数量超过上限。对话历史+问题的字数太多，需要精简输入     |
| 11200  | 授权错误：该appId没有相关功能的授权 或者 业务量超过限制      |
| 11201  | 授权错误：日流控超限。超过当日最大访问量的限制               |
| 11202  | 授权错误：秒级流控超限。秒级并发超过授权路数限制             |
| 11203  | 授权错误：并发流控超限。并发路数超过授权路数限制             |

## 大模型参数

| 字段                   | 类型    | 是否必传                   | 含义                                                         | 备注                                                         |
| ---------------------- | ------- | -------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| sparkModel             | Object  | 是                         | 指定访问的模型版本                                           | 指定访问的模型版本: lite 指向Lite版本; generalv3 指向Pro版本; pro-128k 指向Pro-128K版本; generalv3.5 指向Max版本; max-32k 指向Max-32K版本; 4.0Ultra 指向4.0 Ultra版本; kjwx 指向科技文献大模型（重点优化论文问答、写作等垂直领域）; |
| temperature            | float   | 否                         | 取值范围 (0，1] ，默认值0.5                                  | 核采样阈值。取值越高随机性越强，即相同的问题得到的不同答案的可能性越大 |
| maxTokens              | int     | 否                         | Pro、Max、Max-32K、4.0 Ultra 取值为[1,8192]，默认为4096; Lite、Pro-128K 取值为[1,4096]，默认为4096。 | 模型回答的tokens的最大长度                                   |
| topK                   | int     | 否                         | 取值为[1，6],默认为4                                         | 从k个候选中随机选择⼀个（⾮等概率）                          |
| topP                   | int     | 否                         | 取值范围(0, 1] 默认值1                                       | 生成过程中核采样方法概率阈值                                 |
| functions              | array   | 否                         | 通过该参数控制工具使用                                       | 工具列表                                                     |
| webSearch              | object  | 否，默认表示开启           | `{ "type": "web_search", "web_search": { "enable": true, "show_ref_label": true, "search_mode": "deep" } }` | **仅Pro、Max、Ultra系列模型支持**                            |
| webSearch.enable       | bool    | 否，默认开启（true）       | true or false                                                | enable：是否开启搜索功能，设置为true,模型会根据用户输入判断是否触发联网搜索，false则完全不触发； |
| webSearch.showRefLabel | bool    | 否，默认关闭（false）      | true or false                                                | show_ref_label 开关控制触发联网搜索时是否返回信源信息（仅在enable为true时生效） 如果开启，则先返回搜索结果，之后再返回模型回复内容 |
| webSearch.searchMode   | string  | 否，默认标准搜索（normal） | deep/normal                                                  | search_mode 控制联网搜索策略（仅在enable为true时生效） normal：标准搜索模式，模型引用搜索返回的摘要内容回答问题 deep：深度搜索模式，模型引用搜索返回的全文内容，回复的更准确；同时会带来额外的token消耗（返回search_prompt字段） |
| frequencyPenalty       | float   | 否                         | 取值范围[-2.0,2.0] 默认0                                     | 频率惩罚值                                                   |
| presencePenalty        | float   | 否                         | 取值范围[-2.0,2.0] 默认0                                     | 重复词的惩罚值                                               |
| toolCallsSwitch        | boolean | 否，默认关闭（false）      |                                                              | 设置为true时，触发function call结果中tool_calls以数组格式返回，默认为 false，则以json格式返回 |
| toolChoice             | Object  | 否                         |                                                              | 设置模型自动选择调用的函数： auto：传了tool时默认为auto，模型自动选择调用的函数 <br />none：模型禁用函数调用 <br />required：模型始终选择一个或多个函数进行调用 <br />{"type": "function", "function": {"name": "my_function"}} ：模型强制调用指定函数 |
| responseType           | string  | 否                         |                                                              | { "type": "json_object" } 指定模型输出json格式 使用 JSON 模式时，请始终指示模型通过对话中的某些消息（例如通过系统或用户消息）生成 JSON |
| suppressPlugin         | array   | 否                         |                                                              | 不使用的插件                                                 |

## 方法详解

### 1. websocket调用方式
```java
public void send(SparkChatParam param, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
```
**参数说明**：

- `param`: 查询参数对象，可设置：

|       名称        |  类型  |                             描述                             | 必须 | 默认值 |
| :---------------: | :----: | :----------------------------------------------------------: | ---- | ------ |
|     webSearch     | Object | 当前工具列表中，仅支持联网搜索工具 可选: web_search web_search 仅Pro、Max、Ultra系列模型支持 | N    |        |
|     functions     | Array  | 支持大模型在交互过程中识别出需要调度的外部接口 触发了function_call的情况下，只会返回一帧结果，其中status 为2 <br />仅Spark Max/4.0 Ultra 支持了该功能 | N    |        |
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

### 2. post调用方式
```java
public String send(SparkChatParam param) throws IOException {
```
**参数说明**：

- `param`: 查询参数对象，详情见1

**响应示例**：

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

---

### 3. sse请求方式
```java
public void send(SparkChatParam param, Callback callback) {
```
**参数说明**：

- `param`: 查询参数对象，详情见1

**响应示例**：

```text
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
