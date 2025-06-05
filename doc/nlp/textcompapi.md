# 文本合规API文档

## 简介

本客户端基于讯飞开放平台 API实现，提供文本合规能力[官方文档](https://www.xfyun.cn/doc/nlp/TextModeration/API.html)，支持以下功能：

- 创建合规任务

## 功能列表

| 方法名 | 功能说明   |
| ------ |--------|
| send() | 创建合规任务 |

## 使用准备

1. 前往[能力开通](https://www.xfyun.cn/services/preview-text/)页面
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
    <version>2.0.7</version>
</dependency>
```

2、Java代码

```java
import cn.xfyun.api.TextComplianceClient;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.model.textcomp.TextCompParam;

        TextComplianceClient client = new TextComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        TextCompParam param = TextCompParam.builder()
                .content("塔利班组织联合东突组织欲图。").build();
        String result = client.send(param);
        logger.info("返回结果: {}", result);
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/nlp/TextComplianceClientApp.java)

## 平台参数

|    名称    | 类型  |                             描述                             | 必须 | 默认 |
| :--------: | :---: | :----------------------------------------------------------: | :--: | :--: |
| isMatchAll |  int  | 是否全匹配：<br/>1 代表是<br/>0 代表否<br/>默认取值0，匹配到敏感词则不再匹配，不会返回所有敏感分类。 |  N   |  0   |
| categories | Array | 指定检测的敏感分类：<br/>pornDetection 色情<br/>violentTerrorism 暴恐<br/>political 涉政<br/>lowQualityIrrigation 低质量灌水<br/>contraband 违禁<br/>advertisement 广告<br/>uncivilizedLanguage 不文明用语 |  N   |  /   |
|   libIds   | Array | 指定自定义词库id列表，通过接口创建词库后返回，可以同时携带多个黑白名单id |  N   |  /   |

## 方法详解

### 1. 创建合规任务
```java
public String send(TextCompParam param) throws SignatureException, IOException
```
**参数说明**：

- param参数可设置：

|    名称    |  类型  |                             描述                             | 必须 |
| :--------: | :----: | :----------------------------------------------------------: | :--: |
| isMatchAll |  int   | 是否全匹配：<br/>1 代表是<br/>0 代表否<br/>默认取值0，匹配到敏感词则不再匹配，不会返回所有敏感分类。 |  N   |
|  content   | String |            待识别文本，文本长度最大支持 5 千字符             |  Y   |
|   libIds   | Array  | 指定自定义词库id列表，通过接口创建词库后返回，可以同时携带多个黑白名单id |  N   |
| categories | Array  | 指定检测的敏感分类：<br/>pornDetection 色情<br/>violentTerrorism 暴恐<br/>political 涉政<br/>lowQualityIrrigation 低质量灌水<br/>contraband 违禁<br/>advertisement 广告<br/>uncivilizedLanguage 不文明用语 |  N   |

**响应示例**：

```json
{
  "code": "000000",
  "desc": "成功",
  "data": {
    "result": {
      "suggest": "block",
      "detail": {
        "content": "塔利班组织联合东突组织欲图",
        "category_list": [
          {
            "confidence": 70,
            "category": "violentTerrorism",
            "suggest": "block",
            "category_description": "暴恐_恐怖组织_恐怖组织",
            "word_list": [
              "塔利班"
            ],
            "word_infos": [
              {
                "word": "塔利班",
                "positions": [
                  0,
                  1,
                  2
                ]
              }
            ]
          }
        ]
      }
    },
    "request_id": "T20230406102001116cff41968935000"
  },
  "sid": "18d9e15b7147465084fe064c40556edf"
}
```

---

## 注意事项
1. 待识别文本，文本长度最大支持 5 千字符。
2. 黑名单词库创建和添加可在WordLibClient中添加获取
3. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）`SignatureException`(鉴权失败)
6. 客户端默认超时时间为10秒，可通过Builder调整：

```java
 new TextComplianceClient
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
