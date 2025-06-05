# 视频合规API文档

## 简介

本客户端基于讯飞开放平台 API实现，提供视频合规能力[官方文档](https://www.xfyun.cn/doc/nlp/VideoModeration/API.html)，支持以下功能：

- 创建合规任务
- 查询合规任务

## 功能列表

| 方法名  | 功能说明     |
| ------- | ------------ |
| send()  | 创建合规任务 |
| query() | 查询合规任务 |

## 使用准备

1. 前往[能力开通](https://www.xfyun.cn/services/video_audit/)页面
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
import cn.xfyun.api.VideoComplianceClient;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.config.VideoFormat;
import cn.xfyun.model.Video;
import cn.xfyun.util.StringUtils;

VideoComplianceClient correctionClient = new VideoComplianceClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        // 创建视频信息列表
        List<Video> videoList = new ArrayList<>();
        for (String videoUrl : videos) {
            if (!StringUtils.isNullOrEmpty(videoUrl)) {
                Video video = new Video.Builder()
                        .videoType(VideoFormat.MP4.getFormat())
                        .fileUrl(videoUrl)
                        .name("1.mp4")
                        .build();
                videoList.add(video);
            }
        }

        // 发起音频合规任务请求
        String resp = correctionClient.send(videoList);
        logger.info("视频合规调用返回：{}", resp);
        JsonObject obj = StringUtils.gson.fromJson(resp, JsonObject.class);
        String requestId = obj.getAsJsonObject("data").get("request_id").getAsString();
        logger.info("视频合规任务请求Id：{}", requestId);

        // 拿到request_id后主动查询合规结果   如果有回调函数则在完成后自动调用回调接口
        while (true) {
            String query = correctionClient.query(requestId);
            JsonObject queryObj = StringUtils.gson.fromJson(query, JsonObject.class);
            int auditStatus = queryObj.getAsJsonObject("data").get("audit_status").getAsInt();
            if (auditStatus == 0) {
                logger.info("视频合规待审核...");
            }
            if (auditStatus == 1) {
                logger.info("视频合规审核中...");
            }
            if (auditStatus == 2) {
                logger.info("视频合规审核完成：");
                logger.info(query);
                break;
            }
            if (auditStatus == 4) {
                logger.info("视频合规审核异常：");
                logger.info(query);
                break;
            }
            TimeUnit.MILLISECONDS.sleep(3000);
        }
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/nlp/VideoComplianceClientApp.java)

## 平台参数

|   名称    |  类型  |   描述   | 必须 |                    默认                    |
| :-------: | :----: | :------: | :--: | :----------------------------------------: |
| notifyUrl | String | 回调地址 |  N   |                     /                      |
| queryUrl  | String | 查询地址 |  N   | https://audit.iflyaisol.com/audit/v2/query |

## 方法详解

### 1. 创建合规任务
```java
public String send(List<Video> videoList) throws IOException, SignatureException
```
**参数说明**：

- 参数可设置：

|    名称     |  类型  |                             描述                             | 必须 |
| :---------: | :----: | :----------------------------------------------------------: | :--: |
|  videoList  | Array  |             待识别视频列表，单次数量不能超过10个             |  Y   |
| (videoType) | String | 视频格式 * 支持mp4、3gp、asf、avi、rmvb、mpeg、wmv、rm、mpeg4、mpv、mkv、flv、vob格式 |  Y   |
|  (fileUrl)  | String | 视频地址，长度限制 500字符以内 * 通过URL外链的视频时长建议限制在2小时内 |  Y   |
|   (name)    | String |                           视频名称                           |  Y   |

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
public String query(String requestId) throws IOException, SignatureException 
```

**参数说明**：

- 参数可设置：

|   名称    |  类型  |    描述    | 必须 |
| :-------: | :----: | :--------: | :--: |
| requestId | String | 合规任务ID |  Y   |

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

## 注意事项

1. 待识别视频列表，单次数量不能超过10个。
2. 视频格式 * 支持mp4、3gp、asf、avi、rmvb、mpeg、wmv、rm、mpeg4、mpv、mkv、flv、vob格式
3. 视频地址，长度限制 500字符以内 * 通过URL外链的音频时长建议限制在2小时内
4. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）和 `SignatureException` (鉴权失败)
5. 客户端默认超时时间为10秒，可通过Builder调整：

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
