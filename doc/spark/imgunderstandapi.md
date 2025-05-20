# 图像理解API文档

## 简介

本客户端基于讯飞Spark API实现，提供图像理解能力[官方文档](https://www.xfyun.cn/doc/spark/ImageUnderstanding.html)，支持以下功能：

- 图像理解

## 功能列表

| 方法名 | 功能说明           |
| ------ | ------------------ |
| send() | 图像理解           |
| send() | 图像理解一次性会话 |

## 使用准备

1. 前往[能力开通](https://www.xfyun.cn/services/ptow)页面
2. 创建应用并获取以下凭证：
   - APPID 
   - APIKey
   - APISecret

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
import cn.xfyun.api.ImageUnderstandClient;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.model.sparkmodel.RoleContent;
import cn.xfyun.model.sparkmodel.response.ImageUnderstandResponse;
import cn.xfyun.service.sparkmodel.AbstractImgUnderstandWebSocketListener;
import cn.xfyun.util.FileUtil;
import cn.xfyun.util.StringUtils;

ImageUnderstandClient client = new ImageUnderstandClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
        Date dateBegin = new Date();

        StringBuffer finalResult = new StringBuffer();
        client.send(messages, new AbstractImgUnderstandWebSocketListener() {

            @Override
            public void onSuccess(WebSocket webSocket, ImageUnderstandResponse resp) {
                if (resp.getHeader().getCode() != 0) {
                    logger.error("code=>{}，error=>{}，sid=>{}", resp.getHeader().getCode(), resp.getHeader().getMessage(), resp.getHeader().getSid());
                    logger.warn("错误码查询链接：https://www.xfyun.cn/doc/spark/%E7%B2%BE%E8%B0%83%E6%9C%8D%E5%8A%A1API-websocket.html");
                    System.exit(0);
                    return;
                }

                if (null != resp.getPayload()) {
                    if (null != resp.getPayload().getChoices()) {
                        List<ImageUnderstandResponse.Payload.Choices.Text> text = resp.getPayload().getChoices().getText();
                        // 是否进行了函数调用
                        if (null != text && !text.isEmpty()) {
                            IntStream.range(0, text.size()).forEach(index -> {
                                String content = resp.getPayload().getChoices().getText().get(index).getContent();
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
                System.exit(0);
            }

            @Override
            public void onClose(WebSocket webSocket, int code, String reason) {
                System.exit(0);
            }
        });
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/ImageUnderstandClientApp.java)

## 错误码

| 错误码 | 错误信息                                                   |
| ------ | ---------------------------------------------------------- |
| 0      | 成功                                                       |
| 10003  | 用户的消息格式有错误                                       |
| 10004  | 用户数据的schema错误                                       |
| 10005  | 用户参数值有错误                                           |
| 10006  | 用户并发错误：当前用户已连接，同一用户不能多处同时连接。   |
| 10013  | 用户问题涉及敏感信息，审核不通过，拒绝处理此次请求。       |
| 10022  | 模型生产的图片涉及敏感信息，审核不通过                     |
| 10029  | 图片任何一边的长度超过12800，请检查图片                    |
| 10041  | 图片分辨率不符合要求，50×50 < 图片总像素值 < 6000×6000。   |
| 10907  | token数量超过上限。对话历史+问题的字数太多，需要精简输入。 |

## 模型参数

| 字段        | 类型   | 是否必传 | 含义                                                         | 限制                              | 备注               |
| ----------- | ------ | -------- | ------------------------------------------------------------ | --------------------------------- | ------------------ |
| domain      | string | 否       | 模型版本<br/>1、高级版本效果更优；<br/>2、针对相同图片token计量不同，高级版token为动态计量，图片内容越复杂，token消耗越高。<br/>请根据业务选择 | general (基础版)、imagev3(高级版) | 默认general 基础版 |
| temperature | float  | 否       | 核采样阈值,向上调整可以增加结果的随机程度                    | 取值范围 (0，1]                   | 默认值0.5          |
| topK        | int    | 否       | 从k个中随机选择一个(非等概率)                                | 整数值：1~6                       | 默认值4            |
| maxTokens   | int    | 否       | 回答的tokens的最大长度                                       | 最小值是1, 最大值是8192           | 默认值2048         |
| chatId      | string | 否       | 用于关联会话chat ，需要保障用户下唯一                        |                                   | 用于后续扩展       |
| userId      | string | 否       | 用户uid                                                      | 最大长度：32                      | 用于后续扩展       |

## 方法详解

### 1. 图像理解
```java
public void send(SparkChatParam sparkChatParam, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException
```
**参数说明**：

`sparkChatParam`: 参数对象，可设置：

|   名称   |  类型  |                             描述                             | 必须 | 默认值 |
| :------: | :----: | :----------------------------------------------------------: | ---- | ------ |
| messages | Array  | 有效内容不能超过8192Token<br />user(image)->user->assistant->user->assistant规则进行拼接，保证最后一条是user的当前问题 | Y    |        |
|  chatId  | string |            用于关联会话chat ，需要保障用户下唯一             | N    |        |
|  userId  | string |                  请求头的uid , 用于后续扩展                  | N    |        |

`webSocketListener`: 参数对象，可设置：自定义ws抽象监听类（可使用sdk提供的**AbstractImgUnderstandWebSocketListener**）

**响应示例**：

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
                    "content": "这是AI的回复文本",
  	            "content_type": "text",
      	            "content_meta": {
        	            "desc": "xxxx",
        	            "url": false,
    		            },
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

---

### 2. 图像理解(一次性会话)

```java
public String send(String question, String imgBase64) throws IOException, SignatureException
```

**参数说明**：

|   名称    |  类型  |       描述       | 必须 | 默认值 |
| :-------: | :----: | :--------------: | ---- | ------ |
| question  | string |     用户问题     | Y    |        |
| imgBase64 | string | 图片的base64编码 | Y    |        |

**响应示例**：大模型回复内容

## 注意事项

1. 受Token限制，有效内容不能超过8192Token，图片支持base64。

2. 支持通过一次性会话和多轮会话两种方式

3. 所有生成类接口都需要处理`BusinessException`（参数校验失败）

4. 客户端默认超时时间为20秒，可通过Builder调整：

```java
new ImageUnderstandClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .callTimeout(30, TimeUnit.SECONDS)
                .build();
```

## 错误处理
捕获异常示例：
```java
try {
    String result = client.send(question, imgBase64);
} catch (BusinessException e) {
    System.err.println("业务异常：" + e.getMessage());
} catch (IOException e) {
    System.err.println("网络请求失败：" + e.getMessage());
}
```
