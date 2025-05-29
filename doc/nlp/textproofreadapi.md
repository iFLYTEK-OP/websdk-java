# 文本校对 API文档

## 简介

本客户端基于讯飞开放平台 API实现，提供文本校对能力[官方文档](https://www.xfyun.cn/doc/nlp/textCorrectionOfficial/API.html)，支持以下功能：

- 校对文本

## 功能列表

| 方法名 | 功能说明     |
| ------ | ------------ |
| send() | 查询合规任务 |

## 使用准备

1. 前往[能力开通](https://www.xfyun.cn/services/textCorrectionOfficial)页面
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
    <version>2.0.6</version>
</dependency>
```

2、Java代码

```java
import cn.xfyun.api.TextProofreadClient;
import cn.xfyun.config.PropertiesConfig;

TextProofreadClient client = new TextProofreadClient
                .Builder(appId, apiKey, apiSecret)
                .build();
        String resp = client.send("在干什么你在");
        JSONObject obj = JSON.parseObject(resp);

        // 结果获取text后解码
        byte[] decodedBytes = Base64.getDecoder().decode(obj.getJSONObject("payload").getJSONObject("output_result").getString("text"));
        String decodeRes = new String(decodedBytes, StandardCharsets.UTF_8);
        logger.info("求地址：{}", client.getHostUrl());
        logger.info("请求返回结果：{}", resp);
        logger.info("文本解码后的结果：{}", decodeRes);
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/nlp/TextProofreadClientApp.java)

## 方法详解

### 1. 文本校对
```java
public String send(String text) throws IOException
```
**参数说明**：

- 参数可设置：

| 名称 |  类型  |   描述   | 必须 |
| :--: | :----: | :------: | :--: |
| text | String | 校对文本 |  Y   |

**响应示例**：

```json
{
  "header": {
    "code": 0,
    "message": "success",
    "sid": "ase000e2b1b@hu18a3f00e65d1323882"
  },
  "payload": {
    "output_result": {
      "compress": "raw",
      "encoding": "utf8",
      "format": "json",
      "seq": "0",
      "status": "3",
      "text": "eyJjb2RlIjogMjAwLCAibX..."
    }
  }
}
```

**根据上面返回结果，text字段base64解码后json示例**

```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "checklist": [
      {
        "wordHtml": "第二个百年目标",
        "explanation": "推荐使用更常用的公文规范表述",
        "type": {
          "id": 8,
          "belongId": 8,
          "name": "xxx",
          "desc": "xxx"
        },
        "word": "第二个百年目标",
        "action": {
          "id": 2
        },
        "htmlWords": [
          {
            "word": "第二个百年目标",
            "position": 0
          }
        ],
        "suggest": [
          "第二个百年奋斗目标"
        ],
        "context": "第二个百年目标",
        "position": 0,
        "length": 7,
        "source": 2,
        "um_error_level": 1
      }
    ]
  },
  "umeiTransactionId": "0e49fe88029aeb10"
}
```

---

### 错误能力ID对照表

| 错误能力ID（belongId） | 说明                   |
| ---------------------- | ---------------------- |
| 9                      | 错别字、词             |
| 31                     | 多字错误               |
| 32                     | 少字错误               |
| 35                     | 语义重复               |
| 34                     | 语序错误               |
| 39                     | 量和单位差错           |
| 36                     | 数字差错               |
| 20                     | 句式杂糅               |
| 21                     | 标点符号差错           |
| 24                     | 句子查重               |
| 119                    | 重要讲话引用           |
| 123                    | 地理名词               |
| 19                     | 机构名称               |
| 124                    | 专有名词及术语         |
| 122                    | 媒体报道禁用词和慎用词 |
| 6                      | 常识差错               |
| 111                    | 涉低俗辱骂             |
| 118                    | 其他敏感内容           |



## 注意事项

1. 原请求的校对文本不能为空且不能超过220000个字符，汉字、英文字母、标点都算做一个字符。

2. 公文校对给出position的同时，也给出了length错误词长度，用position+length就是对应错误词在文本中的结束位置音频地址

4. 返回结果为base64字符串 , 需要解码

5. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）

6. 客户端默认超时时间为10秒，可通过Builder调整：

```java
 new TextProofreadClient
                .Builder(appId, apiKey, apiSecret)
                .readTimeout(10)
                .build();
```

## 错误处理
捕获异常示例：
```java
try {
    String result = client.create(createReq);
} catch (BusinessException e) {
    System.err.println("业务异常：" + e.getMessage());
} catch (IOException e) {
    System.err.println("网络请求失败：" + e.getMessage());
}
```
