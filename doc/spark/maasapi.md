# 精调服务OpenAPI文档

## 简介

本客户端基于讯飞星辰Maas API实现，提供Maas平台精调服务调用能力[官方文档](https://www.xfyun.cn/doc/spark/%E7%B2%BE%E8%B0%83%E6%9C%8D%E5%8A%A1API-websocket.html)

## 功能列表

| 方法名 | 功能说明                                                    |
| ------ | ----------------------------------------------------------- |
| send() | 提供三种调用方式: 1-websocket ; 2-post请求 ; 3- sse流式请求 |

## 使用准备

1. 前往[星辰Maas](https://training.xfyun.cn/)平台
2. 创建训练模型
3. 获取模型卡片的参数:
   - appId
   - apiKey
   - apiSecret
   - apiPassword
   - resourceId
   - modelId
   - http请求URL
   - ws请求URL

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
import cn.hutool.json.JSONUtil;
import cn.xfyun.api.MaasClient;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sparkmodel.RoleContent;
import cn.xfyun.model.maas.MaasParam;
import cn.xfyun.model.maas.response.MaasResponse;
import cn.xfyun.service.maas.AbstractMaasWebSocketListener;
import cn.xfyun.util.StringUtils;
import com.alibaba.fastjson.JSON;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * （fine-tuning-text）精练大模型文本对话
 * 1、APPID、APISecret、APIKey、APIPassword信息获取：<a href="https://training.xfyun.cn/model/add">...</a>
 * 2、文档地址：<a href="https://www.xfyun.cn/doc/spark/%E7%B2%BE%E8%B0%83%E6%9C%8D%E5%8A%A1API-websocket.html">...</a>
 */
public class MaasClientApp {

   private static final Logger logger = LoggerFactory.getLogger(MaasClientApp.class);
   private static final String appId = PropertiesConfig.getAppId();
   private static final String apiKey = PropertiesConfig.getApiKey();
   private static final String apiSecret = PropertiesConfig.getApiSecret();
   private static final String apiPassword = "您的apiPassword";
   private static final String resourceId = "您Maas平台模型卡片的resourceId";
   private static final String modelId = "您Maas平台模型卡片的modelId";
   private static final String httpUrl = "https://maas-api.cn-huabei-1.xf-yun.com/v1/chat/completions";
   private static final String wsUrl = "wss://maas-api.cn-huabei-1.xf-yun.com/v1.1/chat";

   public static void main(String[] args) throws Exception {

      MaasParam param = MaasParam.builder()
              .messages(getMessages())
              .chatId(UUID.randomUUID().toString().substring(0, 10))
              .userId("1234567890")
              .build();

      // ws请求方式
      chatWs(param);

      // post方式
      // chatPost(param);

      // 流式请求
      // chatStream(param);
   }

   private static List<RoleContent> getMessages() {
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
      return messages;
   }

   private static void chatStream(MaasParam param) {
      MaasClient client = new MaasClient.Builder()
              .signatureHttp(resourceId, modelId, apiPassword)
              .requestUrl(httpUrl)
              .build();

      // 统计耗时
      SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
      Date dateBegin = new Date();

      // 最终回复结果
      StringBuilder finalResult = new StringBuilder();
      // 最终思维链结果
      StringBuilder thingkingResult = new StringBuilder();

      client.send(param, new Callback() {
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
                        // 去掉前缀 "data: "
                        String data = line.substring(5).trim();
                        if (extractContent(data, finalResult, thingkingResult)) {
                           // 说明数据全部返回完毕，可以关闭连接，释放资源
                           logger.info("session end");
                           Date dateEnd = new Date();
                           logger.info("{}开始", sdf.format(dateBegin));
                           logger.info("{}结束", sdf.format(dateEnd));
                           logger.info("耗时：{}ms", dateEnd.getTime() - dateBegin.getTime());
                           logger.info("完整思维链结果 ==> {}", thingkingResult);
                           logger.info("最终识别结果 ==> {}", finalResult);
                           System.exit(0);
                        }
                     }
                  }
               } catch (IOException e) {
                  logger.error("读取sse返回内容发生异常", e);
               }
            }
         }
      });
   }

   private static void chatPost(MaasParam param) throws IOException {
      MaasClient client = new MaasClient.Builder()
              .signatureHttp(resourceId, modelId, apiPassword)
              .requestUrl(httpUrl)
              .build();

      String result = client.send(param);
      logger.debug("{} 模型返回结果 ==>{}", client.getDomain(), result);
      JSONObject obj = JSON.parseObject(result);
      String content = obj.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
      String reasoningContent = obj.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("reasoning_content");
      if (null != reasoningContent) {
         logger.info("{} 大模型思维链内容 ==>{}", client.getDomain(), reasoningContent);
      }
      logger.info("{} 大模型回复内容 ==>{}", client.getDomain(), content);
   }

   private static void chatWs(MaasParam param) throws MalformedURLException, SignatureException {
      MaasClient client = new MaasClient.Builder()
              .signatureWs(resourceId, modelId, appId, apiKey, apiSecret)
              .wsUrl(wsUrl)
              .build();

      // 统计耗时
      SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
      Date dateBegin = new Date();

      // 最终回复结果
      StringBuilder finalResult = new StringBuilder();
      // 最终思维链结果
      StringBuilder thingkingResult = new StringBuilder();

      // ws方式
      client.send(param, new AbstractMaasWebSocketListener() {
         @Override
         public void onSuccess(WebSocket webSocket, MaasResponse resp) {
            logger.debug("中间返回json结果 ==>{}", JSONUtil.toJsonStr(resp));
            if (resp.getHeader().getCode() != 0) {
               logger.error("code=>{}，error=>{}，sid=>{}", resp.getHeader().getCode(), resp.getHeader().getMessage(), resp.getHeader().getSid());
               logger.warn("错误码查询链接：https://www.xfyun.cn/doc/spark/%E7%B2%BE%E8%B0%83%E6%9C%8D%E5%8A%A1API-websocket.html");
               System.exit(0);
               return;
            }

            if (null != resp.getPayload() && null != resp.getPayload().getChoices()) {
               List<MaasResponse.Payload.Choices.Text> text = resp.getPayload().getChoices().getText();
               if (null != text && !text.isEmpty()) {
                  String content = resp.getPayload().getChoices().getText().get(0).getContent();
                  String reasonContent = resp.getPayload().getChoices().getText().get(0).getReasoningContent();
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
                  webSocket.close(1000, "正常关闭");
                  System.exit(0);
               }
            }
         }

         @Override
         public void onFail(WebSocket webSocket, Throwable t, Response response) {
            webSocket.close(1000, t.getMessage());
            System.exit(0);
         }

         @Override
         public void onClose(WebSocket webSocket, int code, String reason) {
            System.exit(0);
         }
      });
   }

   /**
    * @param data            sse返回的数据
    * @param finalResult     实时回复内容
    * @param thingkingResult 实时思维链结果
    * @return 是否结束
    */
   private static boolean extractContent(String data, StringBuilder finalResult, StringBuilder thingkingResult) {
      logger.debug("sse返回数据 ==> {}", data);
      try {
         JSONObject obj = JSON.parseObject(data);
         JSONObject choice0 = obj.getJSONArray("choices").getJSONObject(0);
         JSONObject delta = choice0.getJSONObject("delta");
         // 结束原因
         String finishReason = choice0.getString("finish_reason");
         if (StrUtil.isNotEmpty(finishReason)) {
            if (finishReason.equals("stop")) {
               logger.info("本次识别sid ==> {}", obj.getString("id"));
               return true;
            }
            throw new BusinessException("异常结束: " + finishReason);
         }
         // 回复
         String content = delta.getString("content");
         if (StrUtil.isNotEmpty(content)) {
            logger.info("中间结果 ==> {}", content);
            finalResult.append(content);
         }
         // 思维链
         String reasonContent = delta.getString("reasoning_content");
         if (StrUtil.isNotEmpty(reasonContent)) {
            logger.info("思维链结果... ==> {}", reasonContent);
            thingkingResult.append(reasonContent);
         }
         // 插件
         String pluginContent = delta.getString("plugins_content");
         if (StrUtil.isNotEmpty(pluginContent)) {
            logger.info("插件信息 ==> {}", pluginContent);
         }
      } catch (BusinessException bx) {
         throw bx;
      } catch (Exception e) {
         logger.error("解析sse返回内容发生异常", e);
         logger.error("异常数据 ==> {}", data);
      }
      return false;
   }
}
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/MaasClientApp.java)

## 错误码

### 1.1 ws错误码列表

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

### 1.2 http错误码列表

| 错误码                                 | 原因                                      | 解决方案                                                |
| -------------------------------------- | ----------------------------------------- | ------------------------------------------------------- |
| 401-无效的身份验证                     | 身份验证无效。                            | 确保使用正确的API密钥及请求组织。                       |
| 401-提供的API密钥不正确                | 请求的API密钥不正确。                     | 检查所用API密钥是否正确，清除浏览器缓存或生成新的密钥。 |
| 403-不支持的国家、地区或领土           | 您正在从不支持的国家、地区或领土访问API。 | 请参考相关页面获取更多信息。                            |
| 429-请求速率限制已达上限               | 您发送请求过快。                          | 控制请求频率，阅读速率限制指南。                        |
| 429-超出当前配额，请检查计划和计费详情 | 您的额度已用尽或已达到每月最高消费限制。  | 购买更多额度或了解如何提高使用限制。                    |
| 500-服务器处理请求时发生错误           | 服务器内部出现问题。                      | 稍后重试请求；若问题持续，请联系我们查看状态页面。      |
| 503-引擎当前过载，请稍后重试           | 服务器流量过大。                          | 稍候重试您的请求。                                      |

### 1.3 内容审核说明

当返回10013或者10014错误码时，代码内容审核判断当前问题或回复的信息涉及敏感信息。返回错误的同时，在header.message字段中会携带当前的敏感提示语。

- 10013 表示用户的问题涉及敏感信息，服务侧拒绝处理此次请求。
- 10014 表示回复结果中涉及敏感信息，后续结果不可以展示给用户。
- 10019 表示当前的回复疑似敏感，结果文本可以给用户展示。服务会在返回**全部结果**后再返回该错误码，如果继续提问可能会导致被审核拦截。建议在收到该错误码后提示用户涉及敏感信息，并禁掉对话框，禁止用户继续提问。

如果需要调整内容审核的严格程度、敏感词等信息，请联系我们技术支持。

## 模型参数

| 字段          | 类型    | 是否必传 | 含义                                                         | 限制                              | 备注                                                         |
| ------------- | ------- | -------- | ------------------------------------------------------------ | --------------------------------- | ------------------------------------------------------------ |
| patchId       | array   | 否       | 调用用户微调大模型时必传，对应resourceid，否则不传           | maxLength:32                      | resourceid可从星辰网页获取                                   |
| domain        | string  | 是       | 取值为用户服务的serviceId                                    |                                   | serviceId可从星辰网页获取                                    |
| temperature   | float   | 否       | 核采样阈值。用于决定结果随机性，取值越高随机性越强即相同的问题得到的不同答案的可能性越高 | 取值：[0,1]；默认值：0.5          | 无                                                           |
| topK          | int     | 否       | 从k个候选中随机选择一个（非等概率）                          | 取值：[1, 6]；默认值：4           | 无                                                           |
| maxTokens     | int     | 否       | 回答的tokens的最大长度                                       | 取值：[1,32768]；默认值：2048     | 限制生成回复的最大 token 数量，max_tokens的限制需要满足`输入promptToken + 设置参数max_tokens <= 32768 - 1`,参数设置过大可能导致回答中断，请酌情调整，建议取值16384以下 |
| chatId        | string  | 否       | 用于关联用户会话                                             |                                   | 需保障用户下的唯一性                                         |
| searchDisable | boolean | 否       | 关闭联网检索功能                                             | 取值：[true,false]；默认值：true  | **该参数仅DeepSeek-R1和DeepSeek-V3支持**                     |
| showRefLabel  | boolean | 否       | 展示检索信源信息                                             | 取值：[true,false]；默认值：false | **该参数仅DeepSeek-R1和DeepSeek-V3支持**。开启联网检索功能后当该参数设置为true，且触发了联网检索功能，会先返回检索信源列表，然后再返回大模型回复结果，否则仅返回大模型回复结果 |
| streamOptions | Object  | 否       | 扩展配置                                                     | 默认值为{"include_usage": true}   | 针对流式响应模式的扩展配置，如控制是否在响应中包含API调用统计信息等附加数据 |

### 1. websocket调用方式
```java
public void send(MaasParam param, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
```
**参数说明**：

- `param`: 查询参数对象，可设置：

|       名称        |  类型  |                                                                                                                                                                描述                                                                                                                                                                 | 必须 | 默认值 |
| :---------------: | :----: |:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:| ---- | ------ |
|     messages      | Array  | 对话信息: 有效内容不能超过**8192Token**<br />[{"role": "user", "content": "你好"},<br />{"role": "assistant", "content": "你好！"},<br />{"role": "user", "content": "你是谁？"},<br />{"role": "assistant", "content": "我是 Spark API。"},<br />{"role": "user", "content": "你会做什么？"}]<br />按 user -> assistant -> user -> assistant 顺序传递历史记录，最后一条为当前问题 | Y    |        |
|      chatId       | String |                                                                                                                                              拓展的会话Id , 保障用户会话的唯一性 <br />仅多语种大模型联动返回                                                                                                                                               | N    |        |
|      userId       | String |                                                                                                                                                    用户的唯一id，表示一个用户，user_123456                                                                                                                                                     | N    |        |
|   extraHeaders    | Object |                                                                                                                                                    http请求方式时生效:<br />额外的请求头参数                                                                                                                                                     | N    |        |
|     extraBody     | Object |                                                                                                                                                    http请求方式时生效:<br />额外的请求体参数                                                                                                                                                     | N    |        |
| webSocketListener | Object |                                                                                                                                      自定义ws抽象监听类（可使用sdk提供的**AbstractMaasWebSocketListener**）                                                                                                                                       | Y    |        |

**响应示例**：

```json
{
	"header": {
		"code": 0,
		"message": "Success",
		"sid": "cht000b7016@dx19646a1f64eb81a700",
		"status": 0
	},
	"payload": {
		"choices": {
			"status": 0,
			"seq": 0,
			"text": [{
				"content": "大模型回复内容",
				"index": 0,
				"role": "assistant",
				"reasoningContent": "思维链内容"
			}]
		}
	}
}
```

---

### 2. post调用方式
```java
public String send(MaasParam param) throws IOException {
```
**参数说明**：

- `param`: 查询参数对象，见1参数

**响应示例**：

```json
{
	"id": "cht000b5b02@dx1964698b459b81a700",
	"model": "xdeepseekr1",
	"object": "chat.completion",
	"created": 1744941156,
	"choices": [{
		"index": 0,
		"message": {
			"role": "assistant",
			"content": "模型回复内容",
			"reasoning_content": "模型思考结果",
			"plugins_content": null
		},
		"finish_reason": "stop"
	}],
	"usage": {
		"prompt_tokens": 28,
		"completion_tokens": 363,
		"total_tokens": 391
	}
}
```

---

### 3. sse请求方式
```java
public void send(List<RoleContent> messages, Callback callback)
```
**参数说明**：

- `param`: 查询参数对象，见1参数

**响应示例**：

```text
data: {"id":"cht000abcad@dx196469ed6d7b81c700","object":"chat.completion.chunk","created":1744941537,"model":"xdeepseekr1","choices":[{"index":0,"delta":{"content":"","reasoning_content":"好的","plugins_content":null}}],"usage":{"prompt_tokens":0,"completion_tokens":0,"total_tokens":0}}

data: {"id":"cht000abcad@dx196469ed6d7b81c700","object":"chat.completion.chunk","created":1744941537,"model":"xdeepseekr1","choices":[{"index":0,"delta":{"content":"","reasoning_content":"，","plugins_content":null}}],"usage":{"prompt_tokens":0,"completion_tokens":0,"total_tokens":0}}

data: {"id":"cht000abcad@dx196469ed6d7b81c700","object":"chat.completion.chunk","created":1744941537,"model":"xdeepseekr1","choices":[{"index":0,"delta":{"content":"","reasoning_content":"用户","plugins_content":null}}],"usage":{"prompt_tokens":0,"completion_tokens":0,"total_tokens":0}}

data: {"id":"cht000abcad@dx196469ed6d7b81c700","object":"chat.completion.chunk","created":1744941537,"model":"xdeepseekr1","choices":[{"index":0,"delta":{"content":"","reasoning_content":"让我","plugins_content":null}}],"usage":{"prompt_tokens":0,"completion_tokens":0,"total_tokens":0}}

data: {"id":"cht000abcad@dx196469ed6d7b81c700","object":"chat.completion.chunk","created":1744941537,"model":"xdeepseekr1","choices":[{"index":0,"delta":{"content":"","reasoning_content":"讲一个笑话。","plugins_content":null}}],"usage":{"prompt_tokens":0,"completion_tokens":0,"total_tokens":0}}

...

data: [DONE]
```

---

## 注意事项
1. 受Token限制，有效内容不能超过8192Token。
2. 按 `user -> assistant -> user -> assistant` 顺序传递历史记录，最后一条为当前问题：
4. 建立连接需要满足以下条件：
   - 必须符合 websocket 协议规范（rfc6455）
   - 若在 60 秒内无数据交互，服务端将主动断开连接，请确保在交互结束后主动关闭连接
5. 客户端默认超时时间为60秒，**http请求可能会超时**,  可通过Builder调整：

```java
new MaasClient.Builder()
                .signatureHttp(resourceId, serviceId, apiPassword)
                .requestUrl(httpUrl)
        		.readTimeout(60)
                .build();
```

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
