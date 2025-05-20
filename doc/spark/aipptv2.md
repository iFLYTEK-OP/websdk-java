# 智能PPT生成(新版)API文档

## 简介

本客户端基于讯飞Spark API实现，提供智能PPT生成能力[官方文档](https://www.xfyun.cn/doc/spark/PPTv2.html)，支持以下功能：

- PPT主题列表查询
- 智能PPT生成（直接生成最终PPT）
- 大纲生成（基于用户输入）
- 文档自定义大纲生成（支持PDF/DOC等格式）
- 大纲转PPT生成
- PPT生成进度查询

## 功能列表

| 方法名               | 功能说明               |
| -------------------- | ---------------------- |
| list()               | 查询PPT主题列表        |
| create()             | 直接生成完整PPT        |
| createOutline()      | 生成大纲               |
| createOutlineByDoc() | 基于文档生成自定义大纲 |
| createPptByOutline() | 通过大纲生成PPT        |
| progress()           | 查询PPT生成进度        |

## 使用准备

1. 前往[能力开通](https://www.xfyun.cn/)页面
2. 创建应用并获取以下凭证：
   - APPID 
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
package cn.xfyun.demo.spark;

import cn.hutool.json.JSONUtil;
import cn.xfyun.api.AIPPTV2Client;
import cn.xfyun.model.aippt.request.Outline;
import cn.xfyun.model.aippt.request.PPTCreate;
import cn.xfyun.model.aippt.request.PPTSearch;
import cn.xfyun.model.aippt.response.PPTCreateResponse;
import cn.xfyun.model.aippt.response.PPTProgressResponse;
import cn.xfyun.model.aippt.response.PPTThemeResponse;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * （ai-ppt-v2）智能PPT（新）
 * 1、APPID、APISecret、APIKey、APIPassword信息获取：<a href="https://console.xfyun.cn/services/zwapi">...</a>
 * 2、文档地址：<a href="https://www.xfyun.cn/doc/spark/PPTv2.html">...</a>
 */
public class AIPPTV2ClientApp {

    private static final Logger logger = LoggerFactory.getLogger(AIPPTV2ClientApp.class);
    private static final String APP_ID = "你的appid";
    private static final String API_SECRET = "你的apiSecret";
    private static String filePath;
    private static String resourcePath;

    static {
        try {
            filePath = "document/aipptpro.pdf";
            resourcePath = Objects.requireNonNull(AIPPTV2ClientApp.class.getResource("/")).toURI().getPath();
        } catch (URISyntaxException e) {
            logger.error("获取资源路径失败", e);
        }
    }

    public static void main(String[] args) {
        try {
            AIPPTV2Client client = new AIPPTV2Client.Builder(APP_ID, API_SECRET).build();

            PPTSearch searchParam = PPTSearch.builder()
                    .pageNum(1)
                    .pageSize(2)
                    .build();
            String themeList = client.list(searchParam);
            PPTThemeResponse themeResponse = JSONUtil.toBean(themeList, PPTThemeResponse.class);
            logger.info("ppt主题列表：{}", JSONUtil.toJsonStr(themeResponse));

            PPTCreate createParam = PPTCreate.builder()
                    .query("根据提供的文件生成ppt")
                    .file(new File(resourcePath + filePath), "aipptpro.pdf")
                    .build();
            String create = client.create(createParam);
            PPTCreateResponse createResponse = JSONUtil.toBean(create, PPTCreateResponse.class);
            logger.info("ppt生成返回结果：{}", JSONUtil.toJsonStr(createResponse));

            PPTCreate createOutlineParam = PPTCreate.builder()
                    .query("生成一个介绍科大讯飞的大纲")
                    .build();
            String createOutline = client.createOutline(createOutlineParam);
            PPTCreateResponse createOutlineResp = JSONUtil.toBean(createOutline, PPTCreateResponse.class);
            logger.info("ppt大纲生成返回结果：{}", JSONUtil.toJsonStr(createOutlineResp));

            PPTCreate docParam = PPTCreate.builder()
                    .query("模仿这个文件生成一个随机的大纲")
                    .file(new File(resourcePath + filePath), "aipptpro.pdf")
                    .build();
            String docResult = client.createOutlineByDoc(docParam);
            PPTCreateResponse docResponse = JSONUtil.toBean(docResult, PPTCreateResponse.class);
            logger.info("自定义大纲生成返回结果：{}", JSONUtil.toJsonStr(docResponse));

            PPTCreate outLine = PPTCreate.builder()
                    .query("生成一个介绍科大讯飞的ppt")
                    .outline(getMockOutLine())
                    .build();
            String ppt = client.createPptByOutline(outLine);
            PPTCreateResponse pptResponse = JSONUtil.toBean(ppt, PPTCreateResponse.class);
            logger.info("通过大纲生成PPT生成返回结果：{}", JSONUtil.toJsonStr(pptResponse));

            String progress = client.progress(outLine.getOutlineSid());
            PPTProgressResponse progressResp = JSONUtil.toBean(progress, PPTProgressResponse.class);
            logger.info("查询PPT进度返回结果：{}", JSONUtil.toJsonStr(progressResp));
        } catch (Exception e) {
            logger.error("请求失败", e);
        }
    }

    public static Outline getMockOutLine() {
        String outLineStr = "{\"title\":\"科大讯飞技术与创新概览\",\"subTitle\":\"探索语音识别与人工智能的前沿发展\",\"chapters\":[{\"chapterTitle\":\"科大讯飞简介\",\"chapterContents\":[{\"chapterTitle\":\"公司历史\"},{\"chapterTitle\":\"主要业务\"}]},{\"chapterTitle\":\"技术与创新\",\"chapterContents\":[{\"chapterTitle\":\"语音识别技术\"},{\"chapterTitle\":\"人工智能应用\"}]},{\"chapterTitle\":\"产品与服务\",\"chapterContents\":[{\"chapterTitle\":\"智能语音产品\"},{\"chapterTitle\":\"教育技术服务\"}]},{\"chapterTitle\":\"市场地位\",\"chapterContents\":[{\"chapterTitle\":\"行业领导者\"},{\"chapterTitle\":\"国际影响力\"}]},{\"chapterTitle\":\"未来展望\",\"chapterContents\":[{\"chapterTitle\":\"发展战略\"},{\"chapterTitle\":\"持续创新计划\"}]}]}";
        return JSON.parseObject(outLineStr, Outline.class);
    }
}
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/AIPPTV2ClientApp.java)

## 错误码

| 错误码 | 描述         | 处理方式                                                     |
| ------ | ------------ | ------------------------------------------------------------ |
| 20002  | 参数错误     | 确认接口入参                                                 |
| 20005  | 大纲生成失败 | 查看是否存在敏感词汇，尝试重新生成                           |
| 20006  | PPT生成失败  | PPT导出错误，请重新生成或联系技术人员                        |
| 20007  | 鉴权错误     | 检查鉴权信息                                                 |
| 9999   | 系统异常     | 确认鉴权信息、请求方式、请求参数是否有误，或联系技术人员排查相关日志 |

## 方法详解

### 1. PPT主题列表查询
```java
public String list(PPTSearch pptSearch) throws IOException
```
**参数说明**：

- `pptSearch`: 查询参数对象，可设置：

|   名称   |  类型   |                             描述                             | 必须 | 默认值 |
| :------: | :-----: | :----------------------------------------------------------: | ---- | ------ |
|  style   | String  | 风格类型： "简约","卡通","商务","创意","国风","清新","扁平","插画","节日" | N    |        |
|  color   | String  | 颜色类型： "蓝色","绿色","红色","紫色","黑色","灰色","黄色","粉色","橙色" | N    |        |
| industry | String  | 行业类型： "科技互联网","教育培训","政务","学院","电子商务","金融战略","法律","医疗健康","文旅体育","艺术广告","人力资源","游戏娱乐" | N    |        |
| pageNum  | Integer |                             页数                             | N    | 1      |
| pageSize | Integer |                           每页数量                           | N    | 10     |

**响应示例**：

```json
{
    "flag": true,
    "code": 0,
    "desc": "成功",
    "count": null,
    "data": {
        "total": 110,
        "records": [
            {
                "templateIndexId": "202407171E27C9D",
                "pageCount": 5,
                "type": "system_template",
                "color": "蓝色",
                "industry": "教育培训",
                "style": "卡通",
                "detailImage": "{\"titleCoverImageLarge\":\"ppt图片地址\",\"titleCoverImage\":\"ppt图片地址\",\"catalogueCoverImage\":\"ppt图片地址\",\"chapterCoverImage\":\"ppt图片地址\",\"contentCoverImage\":\"ppt图片地址\",\"endCoverImage\":\"ppt图片地址\"}"
            },
			...
			 ],
        "pageNum": 1
    }
}
```

---

### 2. 生成PPT
```java
public String create(PPTCreate pptCreate) throws IOException
```
**参数说明**：

- `pptCreate`: 查询参数对象，可设置：

|    名称    |  类型   |                             描述                             | 必须 | 默认值 |
| :--------: | :-----: | :----------------------------------------------------------: | ---- | ------ |
|   query    | String  | 用户生成PPT要求（最多8000字） 注意：query不能为空字符串、仅包含空格的字符串 | Y    |        |
| businessId | String  |           业务ID（非必传）- 业务方自行决定是否使用           | N    |        |
|  language  | String  |                             语种                             | N    | cn     |
|   search   | Boolean |                         是否联网搜索                         | N    | false  |

**响应示例**：

```text
{
    "flag": true,
    "code": 0,
    "desc": "成功",
    "count": null,
    "data": {
        "sid": "7416b894bdd54ccc95bab7400113989e",
        "coverImgSrc": "https://bjcdn.openstorage.cn/xinghuo-privatedata/zhiwen/2024-11-07/3d19478b-cac2-47c2-a238-ae25b9d4b900/81b87056834d4a1fb923dcf03bea4918.png",
        "title": "合肥天气趋势分析",
        "subTitle": "探索气候变化与城市生活影响",
        "outline": null
    }
}
```

---

### 3. 生成大纲
```java
public String createOutline(PPTCreate pptCreate)
```
**参数说明**：

- `pptCreate`: 查询参数对象，可设置：

|    名称    |  类型   |                             描述                             | 必须 | 默认值 |
| :--------: | :-----: | :----------------------------------------------------------: | ---- | ------ |
|   query    | String  | 用户生成PPT要求（最多8000字） 注意：query不能为空字符串、仅包含空格的字符串 | Y    |        |
| businessId | String  |           业务ID（非必传）- 业务方自行决定是否使用           | N    |        |
|  language  | String  |                             语种                             | N    | cn     |
|   search   | Boolean |                         是否联网搜索                         | N    | false  |

**响应示例**：

```text
{
    "flag": true,
    "code": 0,
    "desc": "成功",
    "count": null,
    "data": {
        "sid": "a88306c606c746178c2816b996c45125",
        "outline": {
            "title": "秋分时节的农业管理策略",
            "subTitle": "提升农作物产量的关键措施",
            "chapters": [
                {
                    "chapterTitle": "秋分简介",
                    "chapterContents": [
                        {
                            "chapterTitle": "定义与时间"
                        },
                        {
                            "chapterTitle": "历史背景"
                        }
                    ]
                },
                {
                    "chapterTitle": "秋分的天文意义",
                    "chapterContents": [
                        {
                            "chapterTitle": "昼夜平分"
                        },
                        {
                            "chapterTitle": "太阳直射点变化"
                        }
                    ]
                }
            ]
        }
    }
}
```

---

### 4. 自定义大纲生成
```java
public String createOutlineByDoc(PPTCreate pptCreate)
```
**参数说明**：

- `pptCreate`: 查询参数对象，可设置：

|    名称    |  类型   |                             描述                             | 必须 | 默认值 |
| :--------: | :-----: | :----------------------------------------------------------: | ---- | ------ |
|   query    | String  | 用户生成PPT要求（最多8000字） 注意：query不能为空字符串、仅包含空格的字符串 | N    |        |
|    file    |  File   |               上传文件 (file、fileUrl必填其一)               | N    |        |
|  fileUrl   | String  |              文件地址（file、fileUrl必填其一）               | N    |        |
|  fileName  | String  |              文件名(带文件名后缀；fileName必填)              | Y    |        |
| businessId | String  |           业务ID（非必传）- 业务方自行决定是否使用           | N    |        |
|  language  | String  |                             语种                             | N    | cn     |
|   search   | Boolean |                         是否联网搜索                         | N    | false  |

**响应示例**：见3

---

### 5. 通过大纲生成PPT
```java
public String createPptByOutline(PPTCreate pptCreate)
```
**参数说明**：

- `pptCreate`: 查询参数对象，可设置：

|    名称    |    类型     |                             描述                             | 必须 | 默认值   |
| :--------: | :---------: | :----------------------------------------------------------: | ---- | -------- |
|   query    |   String    | 用户生成PPT要求（最多8000字） 注意：query不能为空字符串、仅包含空格的字符串 | Y    |          |
| outlineSid |   String    |            已生成大纲后，响应返回的请求大纲唯一id            | N    |          |
|  outline   | Outline对象 | 大纲内容（不得超过20个一级大纲--outline.chapters[].chapterTitle | Y    |          |
| templateId |   String    | 直接供用户检索模板的ID,从PPT主题列表查询中获取，为空的话，从free模板中随机取一个 | N    |          |
| businessId |   String    |           业务ID（非必传）- 业务方自行决定是否使用           | N    |          |
|   author   |   String    |            PPT作者名：用户自行选择是否设置作者名             | N    | 讯飞智文 |
| isCardNote |   Boolean   |                     是否生成PPT演讲备注                      | N    | false    |
|   search   |   Boolean   |                         是否联网搜索                         | N    | false    |
|  language  |   String    |          语种（保证传入大纲语种与输入PPT语种一致）           | N    | cn       |
|  fileUrl   |   String    |                           文件地址                           | N    |          |
|  fileName  |   String    |           文件名(带文件名后缀) ，传fileUrl的话必填           | N    |          |
|  isFigure  |   Boolean   |                         是否自动配图                         | N    | false    |
|  aiImage   |   String    | ai配图类型： normal、advanced （isFigure为true的话生效）； normal-普通配图，20%正文配图；advanced-高级配图，50%正文配图 | N    |          |

**响应示例**：见3

---

### 6. 查询生成进度
```java
public String progress(String sid) 
```
**参数说明**：

- sid: 任务ID（从生成接口返回）

**响应示例**：

```text
{
    "flag": true,
    "code": 0,
    "desc": "成功",
    "count": null,
    "data": {
        "pptStatus": "done",
        "aiImageStatus": "done",
        "cardNoteStatus": "done",
        "pptUrl": "https://bjcdn.openstorage.cn/xinghuo-privatedata/zhiwen/2024-11-07/7c8fde0c-2d3e-4a1d-a6bb-b1a5688f10c0/9b1c22980ddf478c9557eedbc51a4d2d.pptx",
        "errMsg": null,
        "totalPages": 21,
        "donePages": 21
    }
}
```

**注**：该接口设置限流，三秒访问一次

---

## 注意事项
1. 基于用户提示、文档等相关内容生成PPT，字数不得超过 **8000 **字，文件限制  **10M**。
   
2. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）

3. 客户端默认超时时间为120秒，可通过Builder调整：

```java
new Builder(appId, apiSecret)
    .readTimeout(120)
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
Q: 生成PPT时超时怎么办？  
A: 适当增加readTimeout时间，复杂PPT生成可能需要更长时间

Q: 如何获取生成的PPT文件？  
A: 生成成功后，响应结果会包含PPT下载链接（具体字段参考官方API文档）

---

**更多问题请打开官方文档联系技术支持**

## 扣量说明

- 基于query直接生成ppt：扣除10点量，1个并发，若需要增加备注，则增加5点量
- 基于query生成大纲：扣除2点量1个并发
- 基于sid，获取大纲（可修改）生成ppt：扣除8点量1个并发
- 基于大纲生成ppt：8点量，若需要生成备注：额外扣除5点量
- PPT自动配图：普通4点量，高级8点量
- PPT需要生成演讲备注：5点量
- PPT多语种翻译：仅生成大纲，扣1点量；大纲生成PPT，扣2点量；
- 开启联网搜索：2点量