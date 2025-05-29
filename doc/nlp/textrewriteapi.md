# 文本校对 API文档

## 简介

本客户端基于讯飞开放平台 API实现，提供文本改写能力[官方文档](https://www.xfyun.cn/doc/nlp/textRewriting/API.html)，支持以下功能：

- 文本改写

## 功能列表

| 方法名 | 功能说明 |
| ------ | -------- |
| send() | 文本改写 |

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
import cn.xfyun.api.TextRewriteClient;
import cn.xfyun.config.PropertiesConfig;

TextRewriteClient client = new TextRewriteClient
                .Builder(appId, apiKey, apiSecret)
                .readTimeout(30000)
//                .level("L1")
                .build();
        String resp = client.send("随着我国城市化脚步的不断加快，园林工程建设的数量也在不断上升，城市对于园林工程的质量要求也随之上升，" +
                "然而就当前我国园林工程管理的实践而言，就园林工程质量管理这一环节还存在许多不足之处，本文在探讨园林工程质量内涵的基础上，" +
                "深入进行质量管理策略探讨，目的是保障我国园林工程施工质量和提升整体发展效率。", "L6");
        JSONObject obj = JSON.parseObject(resp);

        // 结果获取text后解码
        byte[] decodedBytes = Base64.getDecoder().decode(obj.getJSONObject("payload").getJSONObject("result").getString("text"));
        String decodeRes = new String(decodedBytes, StandardCharsets.UTF_8);
        logger.info("请求地址：{}", client.getHostUrl());
        logger.info("请求返回结果：{}", resp);
        logger.info("文本解码后的结果：{}", decodeRes);
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/nlp/TextRewriteClientApp.java)

## 平台参数

| 名称  |  类型  |                             描述                             | 必须 | 默认 |
| :---: | :----: | :----------------------------------------------------------: | :--: | :--: |
| level | String | 改写等级<br/>L1:改写等级1<br/>L2:改写等级2<br/>L3:改写等级3<br/>L4:改写等级4<br/>L5:改写等级5<br/>L6:改写等级6 |  Y   |  /   |

## 方法详解

### 1. 文本改写
```java
public String send(String text, String level) throws IOException
```
**参数说明**：

- 参数可设置：

| 名称  |  类型  |                             描述                             | 必须 |
| :---: | :----: | :----------------------------------------------------------: | :--: |
| text  | String |                           校对文本                           |  Y   |
| level | String | 改写等级<br/>L1:改写等级1<br/>L2:改写等级2<br/>L3:改写等级3<br/>L4:改写等级4<br/>L5:改写等级5<br/>L6:改写等级6 |  Y   |

**响应示例**：

```json
{
	"header": {
		"code": 0,
		"message": "success",
		"sid": "ase000f0c8a@hu17f71911dec020b882"
	},
	"payload": {
		"result": {
			"compress": "raw",
			"encoding": "utf8",
			"format": "json",
			"text": "W1si6Zi/56Wl5a6......"
		}
	}
}
```

**根据上面返回结果，text字段base64解码后json示例**

```json
[["今日阳光明媚，万里无云", [[1, 7]]]]
```

## 注意事项

1. 目前支持单次上传base64后不超过8000字节，约2500字符。

2. 目前支持中文文本改写

4. 返回结果为base64字符串 , 需要解码

5. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）

6. 客户端默认超时时间为10秒，可通过Builder调整：

```java
new TextRewriteClient
                .Builder(appId, apiKey, apiSecret)
                .readTimeout(10)
                .level("L6")
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
