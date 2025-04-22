# 图片生成API文档

## 简介

本客户端基于讯飞Spark API实现，提供图片生成能力[官方文档](https://www.xfyun.cn/doc/spark/ImageGeneration.html)，支持以下功能：

- 文生图片

## 功能列表

| 方法名 | 功能说明 |
| ------ | -------- |
| send() | 文生图片 |

## 使用准备

1. 前往[能力开通](https://www.xfyun.cn/services/Image_generation)页面
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
    <version>2.0.5</version>
</dependency>
```

2、Java代码

```java
import cn.xfyun.api.ImageGenClient;
import cn.xfyun.config.PropertiesConfig;

ImageGenClient client = new ImageGenClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        logger.info("请求地址：{}", client.getHostUrl());
        String resp = client.send("帮我画一个小鸟");
        JSONObject obj = JSON.parseObject(resp);
        if (obj.getJSONObject("header").getInteger("code") != 0) {
            logger.error("请求失败: {}", resp);
            return;
        }

        // 结果获取text后解码
        String base64;
        try {
            base64 = obj.getJSONObject("payload")
                    .getJSONObject("choices")
                    .getJSONArray("text")
                    .getJSONObject(0)
                    .getString("content");
        } catch (Exception e) {
            throw new RuntimeException("返回结果解析失败", e);
        }
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        // base64解码后的图片字节数组写入文件
        try (FileOutputStream imageOutFile = new FileOutputStream(resourcePath + imagePath)) {
            // 将字节数组写入文件
            imageOutFile.write(decodedBytes);
            logger.info("图片已成功保存到: {}", resourcePath + imagePath);
        } catch (IOException e) {
            logger.error("保存图片时出错: {}", e.getMessage(), e);
        }
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/ImageGenClientApp.java)

## 错误码

| 错误码 | 错误信息                               |
| ------ | -------------------------------------- |
| 0      | 成功                                   |
| 10003  | 用户的消息格式有错误                   |
| 10004  | 用户数据的schema错误                   |
| 10005  | 用户参数值有错误                       |
| 10008  | 服务容量不足                           |
| 10021  | 输入审核不通过                         |
| 10022  | 模型生产的图片涉及敏感信息，审核不通过 |

## 合成参数

| 段     | 类型   | 是否必传 | 含义               | 限制    | 备注         |
| ------ | ------ | -------- | ------------------ | ------- | ------------ |
| width  | int    | 否       | 图片宽             |         | 默认 512     |
| height | int    | 否       | 图片长             |         | 默认 512     |
| domain | string | 否       | 图片生成使用的模型 | general | 默认 general |

## 方法详解

### 1. 图片生成
```java
public String send(ImageGenParam param) throws IOException
```
**参数说明**：

- `param`: 查询参数对象，可设置：

|   名称   | 类型  |              描述              | 必须 | 默认值 |
| :------: | :---: | :----------------------------: | ---- | ------ |
|  width   |  int  |             图片宽             | N    | 512    |
|  height  |  int  |             图片长             | N    | 512    |
| messages | Array | 模型生成的对话记录(最多1000字) | N    |        |

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
                  "content": "base64",                     
                  "index": 0,
                  "role": "assistant"
                }
            ]
        }
    }
}
```

---

### 2. 图片生成
```java
public String send(String text) throws IOException
```
**参数说明**：

| 名称 |  类型  |              描述              | 必须 | 默认值 |
| :--: | :----: | :----------------------------: | ---- | ------ |
| text | String | 用户生成图片要求（最多1000字） | Y    |        |

**响应示例**：见1返回参数

---

## 注意事项
1. 基于用户提示内容生成PPT，字数不得超过 **1000 **字
   
2. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）

3. 客户端默认超时时间为60秒，可通过Builder调整：

```java
new ImageGenClient
                .Builder(appId, apiKey, apiSecret)
                .readTimeout(60)
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

## 常见问题
#### 图片生成的主要功能是什么？

> 答：根据用户输入的文字内容，生成符合语义描述的不同风格的图像。

#### 图片生成支持什么应用平台？

> 答：目前支持Web API应用平台。

#### 图片生成的默认大小为多少？

> 答：分辨率512*512。

---

**更多问题请打开官方文档联系技术支持**

## 扣量说明

**注：** 图片生成按点数计费，不同分辨率计费不同，具体如下

| 分辨率（width * height） | 图点数 |
| ------------------------ | ------ |
| 512x512                  | 6      |
| 640x360                  | 6      |
| 640x480                  | 6      |
| 640x640                  | 7      |
| 680x512                  | 7      |
| 512x680                  | 7      |
| 768x768                  | 8      |
| 720x1280                 | 12     |
| 1280x720                 | 12     |
| 1024x1024                | 14     |