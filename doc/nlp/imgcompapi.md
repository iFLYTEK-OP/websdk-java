# 图片合规API文档

## 简介

本客户端基于讯飞开放平台 API实现，提供图片合规能力[官方文档](https://www.xfyun.cn/doc/nlp/ImageModeration/API.html)，支持以下功能：

- 创建合规任务

## 功能列表

| 方法名 | 功能说明     |
| ------ | ------------ |
| send() | 创建合规任务 |

## 使用准备

1. 前往[能力开通](https://www.xfyun.cn/services/preview-picture/)页面
2. 创建应用并获取以下凭证：
   - APPID 
   - APIKey
   - APISecret

## 快速开始

1、添加maven依赖

```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-nlp</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.0.11</version>
</dependency>
```

2、Java代码

```java
import cn.xfyun.api.ImageComplianceClient;
import cn.xfyun.config.ModeType;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.util.FileUtil;

        ImageComplianceClient correctionClient = new ImageComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        ImageCompParam param = ImageCompParam.builder()
                .modeType(ModeType.BASE64)
                .content(FileUtil.fileToBase64(resourcePath + imagePath))
                .build();
        String pathResp = correctionClient.send(param);
        logger.info("图片地址返回结果: {}", pathResp);
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/nlp/ImageComplianceClientApp.java)

## 方法详解

### 1. 创建合规任务
```java
public String send(ImageCompParam param) throws IOException, SignatureException
```
**参数说明**：

- param参数可设置：

|   名称   |  类型  |                             描述                             | 必须 |
| :------: | :----: | :----------------------------------------------------------: | :--: |
| content  | String | 待识别图片信息<br/>modeType为link时，值为外链信息<br/>modeType为base64时，值为图片base64编码信息 |  Y   |
| modeType | Object | 枚举<br />modeType为link时，值为外链信息<br/>modeType为base64时，值为图片base64编码信息 |  Y   |
| bizType  | String | 指定检测的敏感分类： * pornDetection 色情 * violentTerrorism 暴恐 * political 涉政 * lowQualityIrrigation 低质量灌水 * contraband 违禁 * advertisement 广告 * uncivilizedLanguage 不文明用语 |  N   |

**响应示例**：

```json
{
  "code": "000000",
  "desc": "成功",
  "data": {
    "result": {
      "suggest": "block",
      "detail": {
        "category_list": [
          {
            "confidence": 93,
            "category": "political",
            "suggest": "block",
            "category_description": "涉政_政治象征_国旗",
            "object_detail": [
              {
                "name": "chinaflag",
                "confidence": 93,
                "location": {
                  "x": 124.0,
                  "y": 271.0,
                  "w": 415.0,
                  "h": 307.0
                }
              }
            ]
          },
          {
            "confidence": 99,
            "category": "political",
            "suggest": "block",
            "category_description": "涉政_政治象征_国旗国徽"
          }
        ]
      }
    },
    "request_id": "T20230302164002016a33b6197100000"
  },
  "sid": "a154ee7126314a029a5ab5a7491ffa1c"
}
```

---

## 注意事项
1. 支持 PNG、JPG、JPEG、BMP、GIF、WEBP 格式。图片大小限制是20M。
2. 黑名单词库创建和添加可在WordLibClient中添加获取
3. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）`SignatureException`(鉴权失败)
6. 客户端默认超时时间为10秒，可通过Builder调整：

```java
 new ImageComplianceClient
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
} catch (SignatureException e) {
    System.err.println("鉴权失败：" + e.getMessage());
}
```
