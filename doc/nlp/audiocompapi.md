# 音频合规API文档

## 简介

本客户端基于讯飞开放平台 API实现，提供音频合规能力[官方文档](https://www.xfyun.cn/doc/nlp/AudioModeration/API.html)，支持以下功能：

- 创建合规任务
- 查询合规任务结果

## 功能列表

| 方法名  | 功能说明         |
| ------- | ---------------- |
| send()  | 查询合规任务     |
| query() | 查询合规任务结果 |

## 使用准备

1. 前往[能力开通](https://www.xfyun.cn/services/audio_audit/)页面
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
import cn.xfyun.api.AudioComplianceClient;
import cn.xfyun.config.AudioFormat;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.Audio;
import cn.xfyun.util.StringUtils;

AudioComplianceClient correctionClient = new AudioComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        List<Audio> audioList = new ArrayList<>();
        for (String audioUrl : audios) {
            if (!StringUtils.isNullOrEmpty(audioUrl)) {
                Audio audio = new Audio.Builder()
                        .audioType(AudioFormat.WAV.getFormat())
                        .fileUrl(audioUrl)
                        .name("133c3269-c823-4499-94ad-e4283167402f.wav")
                        .build();
                audioList.add(audio);
            }
        }

        // 发起音频合规任务请求
        String resp = correctionClient.send(audioList);
        logger.info("音频合规调用返回：{}", resp);
        JsonObject obj = StringUtils.gson.fromJson(resp, JsonObject.class);
        String requestId = obj.getAsJsonObject("data").get("request_id").getAsString();
        logger.info("音频合规任务请求Id：{}", requestId);

        // 拿到request_id后主动查询合规结果   如果有回调函数则在完成后自动调用回调接口
        while (true) {
            String query = correctionClient.query(requestId);
            JsonObject queryObj = StringUtils.gson.fromJson(query, JsonObject.class);
            int auditStatus = queryObj.getAsJsonObject("data").get("audit_status").getAsInt();
            if (auditStatus == 0) {
                logger.info("音频合规待审核...");
            }
            if (auditStatus == 1) {
                logger.info("音频合规审核中...");
            }
            if (auditStatus == 2) {
                logger.info("音频合规审核完成：");
                logger.info(query);
                break;
            }
            if (auditStatus == 4) {
                logger.info("音频合规审核异常：");
                logger.info(query);
            }
            TimeUnit.MILLISECONDS.sleep(3000);
        }
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/nlp/AudioComplianceClientApp.java)

## 平台参数

|   名称    |  类型  |   描述   | 必须 |                    默认                    |
| :-------: | :----: | :------: | :--: | :----------------------------------------: |
| notifyUrl | String | 回调地址 |  N   |                     /                      |
| queryUrl  | String | 查询地址 |  N   | https://audit.iflyaisol.com/audit/v2/query |



## 方法详解

### 1. 创建合规任务
```java
public String send(List<Audio> audioList, String notifyUrl) throws IOException, NoSuchAlgorithmException, InvalidKeyException
```
**参数说明**：

- 参数可设置：

|    名称     |  类型  |                             描述                             | 必须 |
| :---------: | :----: | :----------------------------------------------------------: | :--: |
|  audioList  | Array  |                           音频信息                           |  Y   |
| (audioType) | String | 音频类型，如果不传，取 url 后缀名作为格式 * 支持类型：mp3、alaw、ulaw、pcm、aac、wav |  Y   |
|  (fileUrl)  | String | 音频地址，长度限制 500字符以内 * 通过URL外链的音频时长建议限制在1小时内 |  Y   |
|   (name)    | String |                           音频名称                           |  Y   |
|  notifyUrl  | String |                           回调地址                           |  N   |

**响应示例**：

```json
{
	"code": "000000",
	"desc": "请求成功",
	"data": {
		"request_id": "T2025051910000301ab46e195580d000"
	},
	"sid": "f2079052365f47be889fd68febc413ce"
}
```

---

### 2. 查询合规任务
```java
public String query(String requestId) throws IOException, NoSuchAlgorithmException, InvalidKeyException
```
**参数说明**：

- `参数可设置：

|   名称    |  类型  |      描述      | 必须 |
| :-------: | :----: | :------------: | ---- |
| requestId | String | 查询合规任务ID | Y    |

**响应示例**：

```text
{
  "code": "000000",
  "desc": "请求成功",
  "data": {
    "request_id": "T20230321155903016bba833f177a000",
    "audit_status": 2,
    "result_list": [
      {
        "name": "xxx%2F1.mp3",
        "suggest": "block",
        "detail": {
          "audios": [
            {
              "duration": 10,
              "category_list": [
                {
                  "category_description": "辱骂_人身攻击_重度人身攻击",
                  "word_list": [
                    
                  ],
                  "suggest": "block",
                  "category": "uncivilizedLanguage"
                }
              ],
              "offsetTime": "0",
              "audio_url": "http://xxx",
              "suggest": "block",
              "content": "xxx"
            },
            {
              "duration": 10,
              "category_list": [
                {
                  "category_description": "辱骂_人身攻击_重度人身攻击",
                  "word_list": [
                    
                  ],
                  "suggest": "block",
                  "category": "uncivilizedLanguage"
                }
              ],
              "offsetTime": "10",
              "audio_url": "http://xxx",
              "suggest": "block",
              "content": "xxx"
            }
          ]
        }
      }
    ]
  },
  "sid": "d7c868febced455399b373a976d3c3b8"
}
```

---

## 注意事项
1. 待识别音频列表，单次数量不能超过10个。

2. 音频类型，如果不传，取 url 后缀名作为格式, 支持类型：mp3、alaw、ulaw、pcm、aac、wav

3. 音频地址，长度限制 500字符以内 , 通过URL外链的音频时长建议限制在1小时内

4. 回调地址，未传不进行回调

5. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）`SignatureException`(鉴权失败)

6. 客户端默认超时时间为10秒，可通过Builder调整：

```java
 new AudioComplianceClient
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
