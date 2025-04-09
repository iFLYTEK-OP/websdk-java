# 智能PPT生成(新版)API文档

## 接口与鉴权

### 应用申请

> 能力开通地址：https://www.xfyun.cn/services/aippt


### 实例代码

##### 示例代码

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
    private static final String APP_ID = "替换成你的appid";
    private static final String API_SECRET = "替换成你的apiSecret";
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


### 接口域名

```apl
zwapi.xfyun.cn
```

## 错误码

| 错误码 | 描述         | 处理方式                                                     |
| ------ | ------------ | ------------------------------------------------------------ |
| 20002  | 参数错误     | 确认接口入参                                                 |
| 20005  | 大纲生成失败 | 查看是否存在敏感词汇，尝试重新生成                           |
| 20006  | PPT生成失败  | PPT导出错误，请重新生成或联系技术人员                        |
| 20007  | 鉴权错误     | 检查鉴权信息                                                 |
| 9999   | 系统异常     | 确认鉴权信息、请求方式、请求参数是否有误，或联系技术人员排查相关日志 |

## 接口列表

### 1、PPT主题列表查询

**1.1 接口地址：**

```text
https://zwapi.xfyun.cn/api/ppt/v2/template/list
```

**1.2 请求示例：**

```text
{
    "style": "简约",
    "color": "红色",
    "industry": "教育培训",
    "pageNum": 1,
    "pageSize": 10
}
```

**1.3 请求查询参数：**

```text
POST，application/json
```

**注意:请求体不能为空，至少有一项。**

|   名称   |  类型   |                             描述                             | 必须 | 默认值 |
| :------: | :-----: | :----------------------------------------------------------: | ---- | ------ |
|  style   | String  | 风格类型： "简约","卡通","商务","创意","国风","清新","扁平","插画","节日" | N    |        |
|  color   | String  | 颜色类型： "蓝色","绿色","红色","紫色","黑色","灰色","黄色","粉色","橙色" | N    |        |
| industry | String  | 行业类型： "科技互联网","教育培训","政务","学院","电子商务","金融战略","法律","医疗健康","文旅体育","艺术广告","人力资源","游戏娱乐" | N    |        |
| pageNum  | Integer |                             页数                             | N    | 1      |
| pageSize | Integer |                           每页数量                           | N    | 10     |

**1.4 响应参数：**

```text
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
                "detailImage": "{\"titleCoverImageLarge\":\"\",\"titleCoverImage\":\"\",\"catalogueCoverImage\":\"\",\"chapterCoverImage\":\"\",\"contentCoverImage\":\"\",\"endCoverImage\":\"\"}"
            },
			...
			 ],
        "pageNum": 1
    }
}
```

**1.5 响应描述**

|            响应字段             |  类型   |                     描述                     |
| :-----------------------------: | :-----: | :------------------------------------------: |
|              flag               | Boolean |                   响应标识                   |
|              code               | Integer |                    错误码                    |
|              desc               | String  |                   错误详情                   |
|              count              | Integer |                不用关注，预留                |
|           data.total            |  Long   |               PPT主题列表总数                |
|          data.records           |  array  |               PPT主题列表集合                |
| data.records[]. templateIndexId | String  |              供用户检索模板的ID              |
|    data.records[]. pageCount    | Integer |                    总页数                    |
|      data.records[]. type       | String  |                     类型                     |
|      data.records[]. color      | String  |                   颜色类型                   |
|    data.records[]. industry     | String  |                   行业类型                   |
|      data.records[]. style      | String  |                   风格类型                   |
|   data.records[]. detailImage   | String  |                    详细图                    |
|     data.records[]. payType     | String  | 模板支付方式，**注意：所有模板均已免费使用** |

### [#](https://www.xfyun.cn/doc/spark/PPTv2.html#_2、ppt生成-直接根据用户输入要求-获得最终ppt)2、PPT生成（直接根据用户输入要求，获得最终PPT）

**2.1 接口描述：**

基于用户提示、文档等相关内容生成PPT，字数不得超过8000字，文件限制10M。

**2.2 接口地址：**

```text
https://zwapi.xfyun.cn/api/ppt/v2/create
```

**2.3 请求查询参数：**

```text
POST，multipart/form-data
```

|    名称    |     类型      |                             描述                             | 必须 | 默认值 |
| :--------: | :-----------: | :----------------------------------------------------------: | ---- | ------ |
|   query    |    String     | 用户生成PPT要求（最多8000字；query、file、fileUrl必填其一） 注意：query不能为空字符串、仅包含空格的字符串 | N    |        |
|    file    | MultipartFile | 上传文件 (file、fileUrl、query必填其一，如果传file或者fileUrl，fileName必填) | N    |        |
|  fileUrl   |    String     | 文件地址（file、fileUrl、query必填其一，如果传file或者fileUrl，fileName必填） | N    |        |
|  fileName  |    String     |  文件名(带文件名后缀；如果传file或者fileUrl，fileName必填)   | N    |        |
| templateId |    String     |        直接供用户检索模板的ID,从PPT主题列表查询中获取        | N    |        |
| businessId |    String     |           业务ID（非必传）- 业务方自行决定是否使用           | N    |        |
|   author   |    String     |            PPT作者名：用户自行选择是否设置作者名             | N    | 智文   |
| isCardNote |    Boolean    |                     是否生成PPT演讲备注                      | N    | false  |
|   search   |    Boolean    |                         是否联网搜索                         | N    | false  |
|  language  |    String     |                             语种                             | N    | cn     |
|  isFigure  |    Boolean    |                         是否自动配图                         | N    | false  |
|  aiImage   |    String     | ai配图类型： normal、advanced （isFigure为true的话生效）； normal-普通配图，20%正文配图；advanced-高级配图，50%正文配图 | N    |        |

**2.4 可选参数列表：**

- language：
  - cn：中文（简体）
  - en：英语
  - ja：日语
  - ru：俄语
  - ko：韩语
  - de：德语
  - fr：法语
  - pt：葡萄牙语
  - es：西班牙语
  - it：意大利语
  - th：泰语

**2.5 请求响应：**

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

**2.6 响应描述**

|     响应字段     |  类型   |      描述      |
| :--------------: | :-----: | :------------: |
|       flag       | Boolean |    响应标识    |
|       code       | Integer |     错误码     |
|       desc       | String  |    错误详情    |
|      count       | Integer | 不用关注，预留 |
|     data.sid     | string  |   请求唯一id   |
| data.CoverImgSrc | string  | PPT封面图链接  |
|    data.title    | string  |   PPT主标题    |
|  data.subTitle   | string  |   PPT副标题    |
|   data.outline   | string  |    PPT大纲     |

### [#](https://www.xfyun.cn/doc/spark/PPTv2.html#_3、大纲生成)3、大纲生成

**3.1 接口地址：**

```text
https://zwapi.xfyun.cn/api/ppt/v2/createOutline
```

**3.2 请求参数查询**

```text
POST，multipart/form-data
```

|    名称    |  类型   |                             描述                             | 必须 | 默认值 |
| :--------: | :-----: | :----------------------------------------------------------: | ---- | ------ |
|   query    | String  | 用户生成PPT要求（最多8000字） 注意：query不能为空字符串、仅包含空格的字符串 | Y    |        |
| businessId | String  |           业务ID（非必传）- 业务方自行决定是否使用           | N    |        |
|  language  | String  |                             语种                             | N    | cn     |
|   search   | Boolean |                         是否联网搜索                         | N    | false  |

**3.3 请求响应**

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

**3.4 响应描述**

|                 响应字段                  |  类型   |                    描述                     |
| :---------------------------------------: | :-----: | :-----------------------------------------: |
|                   flag                    | Boolean |                  响应标识                   |
|                   code                    | Integer |                   错误码                    |
|                   desc                    | String  |                  错误详情                   |
|                   count                   | Integer |               不用关注，预留                |
|                 data.sid                  | string  | 请求大纲唯一id，后续通过大纲生成ppt可能需要 |
|               data.outline                | object  |                  大纲数据                   |
|            data.outline.title             | string  |                  PPT主标题                  |
|           data.outline.subTitle           | string  |                  PPT副标题                  |
|   data.outline.chapters[].chapterTitle    | string  |            章节、子章节标题名称             |
| data.outline.chapters[].chapterContents[] |  array  |                二级大纲内容                 |

**3.5 大纲结构体说明（data.outline）**

```java
public class Outline {

    // 主标题
    private String title;

    // 副标题
    private String subTitle;

    /**
     * 文档的章节列表。
     */
    private List<Outline.Chapter> chapters;

    public static class Chapter {
        // 章节、子章节标题名称
        String chapterTitle;

        // 二级大纲chapterContents为空
        List<Outline.Chapter> chapterContents = null;
    }
}
```

### [#](https://www.xfyun.cn/doc/spark/PPTv2.html#_4、自定义大纲生成)4、自定义大纲生成

**4.1 接口描述：**

基于用户提示、文档等相关内容生成PPT大纲。
query参数不得超过8000字，上传文件支持pdf(不支持扫描件)、doc、docx、txt、md格式的文件，注意：txt格式限制100万字以内，其他文件限制10M。

**4.2 接口地址：**

```text
https://zwapi.xfyun.cn/api/ppt/v2/createOutlineByDoc
```

**4.3 请求查询参数：**

```text
POST，multipart/form-data
```

|    名称    |     类型      |                             描述                             | 必须 | 默认值 |
| :--------: | :-----------: | :----------------------------------------------------------: | ---- | ------ |
|   query    |    String     | 用户生成PPT要求（最多8000字） 注意：query不能为空字符串、仅包含空格的字符串 | N    |        |
|    file    | MultipartFile |               上传文件 (file、fileUrl必填其一)               | N    |        |
|  fileUrl   |    String     |              文件地址（file、fileUrl必填其一）               | N    |        |
|  fileName  |    String     |              文件名(带文件名后缀；fileName必填)              | Y    |        |
| businessId |    String     |           业务ID（非必传）- 业务方自行决定是否使用           | N    |        |
|  language  |    String     |                             语种                             | N    | cn     |
|   search   |    Boolean    |                         是否联网搜索                         | N    | false  |

**4.4请求响应与说明**

> 见3.3、3.4小节说明

### [#](https://www.xfyun.cn/doc/spark/PPTv2.html#_5、通过大纲生成ppt)5、通过大纲生成PPT

**5.1 接口地址：**

```text
https://zwapi.xfyun.cn/api/ppt/v2/createPptByOutline
```

**5.2 请求示例：**

```text
{
    "outline": {
        "title": "烧烤制作方法",
        "subTitle": "从食材准备到成品展示",
        "chapters": [
            {
                "chapterTitle": "烧烤概述",
                "chapterContents": [
                    {
                        "chapterTitle": "烧烤定义与起源与发展",
                        "chapterContents": null
                    }
                ]
            }
        ]
    },
    "language": "cn",
    "isCardNote": true,
    "aiImage": "advanced",
    "search":true,
    "isFigure":true,
    "author":"测试",
    "query":"烧烤"
}
```

**5.3 请求查询参数：**

```text
POST，application/json
```

|    名称    |    类型    |                             描述                             | 必须 | 默认值   |
| :--------: | :--------: | :----------------------------------------------------------: | ---- | -------- |
|   query    |   String   | 用户生成PPT要求（最多8000字） 注意：query不能为空字符串、仅包含空格的字符串 | Y    |          |
| outlineSid |   String   | 已生成大纲后，响应返回的请求大纲唯一id （见 **3.4** 小节返回字段data.sid） | N    |          |
|  outline   | JSONObject | 大纲内容（不得超过20个一级大纲--outline.chapters[].chapterTitle，见 **3.4** 小节返回字段data.outline） | Y    |          |
| templateId |   String   | 直接供用户检索模板的ID,从PPT主题列表查询中获取；见 **1.5** 小节返回字段templateIndexId；为空的话，从free模板中随机取一个 | N    |          |
| businessId |   String   |           业务ID（非必传）- 业务方自行决定是否使用           | N    |          |
|   author   |   String   |            PPT作者名：用户自行选择是否设置作者名             | N    | 讯飞智文 |
| isCardNote |  Boolean   |                     是否生成PPT演讲备注                      | N    | false    |
|   search   |  Boolean   |                         是否联网搜索                         | N    | false    |
|  language  |   String   |          语种（保证传入大纲语种与输入PPT语种一致）           | N    | cn       |
|  fileUrl   |   String   |                           文件地址                           | N    |          |
|  fileName  |   String   |           文件名(带文件名后缀) ，传fileUrl的话必填           | N    |          |
|  isFigure  |  Boolean   |                         是否自动配图                         | N    | false    |
|  aiImage   |   String   | ai配图类型： normal、advanced （isFigure为true的话生效）； normal-普通配图，20%正文配图；advanced-高级配图，50%正文配图 | N    |          |

**5.4 请求响应与说明**

> 见2.5、2.6小节说明

### [#](https://www.xfyun.cn/doc/spark/PPTv2.html#_6、ppt进度查询)6、PPT进度查询

**6.1 接口地址：**

```text
https://zwapi.xfyun.cn/api/ppt/v2/progress?sid={}
```

**6.2 请求查询参数：**

注：该接口设置限流，三秒访问一次

```text
GET
```

| 名称 |  类型  |    描述    | 必须 |
| :--: | :----: | :--------: | ---- |
| sid  | String | 请求唯一ID | Y    |

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

**6.3 响应描述**

|      响应字段       |  类型   |                             描述                             |
| :-----------------: | :-----: | :----------------------------------------------------------: |
|        flag         | Boolean |                           响应标识                           |
|        code         | Integer |                            错误码                            |
|        desc         | String  |                           错误详情                           |
|        count        | Integer |                        不用关注，预留                        |
|   data.pptStatus    | String  | PPT构建状态：building（构建中），done（已完成），build_failed（生成失败） |
| data.aiImageStatus  | String  |        ai配图状态：building（构建中），done（已完成）        |
| data.cardNoteStatus | String  |       演讲备注状态：building（构建中），done（已完成）       |
|     data.pptUrl     | String  |                        生成PPT的地址                         |
|     data.errMsg     | String  |                      生成PPT的失败信息                       |
|   data.totalPages   | Integer |                       生成PPT的总页数                        |
|   data.donePages    | Integer | 生成PPT的完成页数 （ai配图和演讲备注为异步任务，ppt页数完成，不代表配图和演讲备注也完成） |

## [#](https://www.xfyun.cn/doc/spark/PPTv2.html#扣量说明)扣量说明

- 基于query直接生成ppt：扣除10点量，1个并发，若需要增加备注，则增加5点量
- 基于query生成大纲：扣除2点量1个并发
- 基于sid，获取大纲（可修改）生成ppt：扣除8点量1个并发
- 基于大纲生成ppt：8点量，若需要生成备注：额外扣除5点量
- PPT自动配图：普通4点量，高级8点量
- PPT需要生成演讲备注：5点量
- PPT多语种翻译：仅生成大纲，扣1点量；大纲生成PPT，扣2点量；
- 开启联网搜索：2点量