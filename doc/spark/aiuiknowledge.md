# 超拟人交互个性化知识库 API文档

## 简介

本客户端基于讯飞Spark API实现，提供个性化知识库创建删除等能力[官方文档](https://www.xfyun.cn/doc/spark/Interact_KM.html#%E4%B8%80%E3%80%81%E6%8E%A5%E5%8F%A3%E8%AF%B4%E6%98%8E)，支持以下功能：

- 个性化知识库创建
- 个性化知识库删除
- 个性化知识库上传文件
- 个性化知识库查询
- 个性化知识库关联应用

## 功能列表

| 方法名   | 功能说明             |
| -------- | -------------------- |
| create() | 个性化知识库创建     |
| delete() | 个性化知识库删除     |
| upload() | 个性化知识库上传文件 |
| list()   | 个性化知识库查询     |
| link()   | 个性化知识库关联应用 |

## 使用准备

1. 前往[能力开通](https://console.xfyun.cn/services/VCN)页面
2. 创建应用并获取以下凭证：
   - API_PASSWORD

## 快速开始

1、添加maven依赖

```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-spark</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.2.0</version>
</dependency>
```

2、Java代码

```java
package cn.xfyun.demo.spark;

import cn.xfyun.api.AiUiKnowledgeClient;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.model.aiui.knowledge.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Objects;

/**
 * 个性化知识库 Client
 * 1、APPID、APISecret、APIKey、APIPassword信息获取：<a href="https://console.xfyun.cn/services/VCN">...</a>
 * 2、文档地址：<a href="https://www.xfyun.cn/doc/spark/Interact_KM.html#%E4%B8%80%E3%80%81%E6%8E%A5%E5%8F%A3%E8%AF%B4%E6%98%8E">...</a>
 */
public class AiUiKnowledgeClientApp {

    private static final Logger logger = LoggerFactory.getLogger(AiUiKnowledgeClientApp.class);
    private static final String API_PASSWORD = PropertiesConfig.getApiSecret();
    private static final String APP_ID = PropertiesConfig.getAppId();
    private static final String SCENE_NAME = "场景名称";
    // 用户唯一标识ID
    private static final long UID = 1111111111L;
    private static String filePath;
    private static String resourcePath;

    static {
        try {
            filePath = "document/aiuiknowledge.txt";
            resourcePath = Objects.requireNonNull(AiUiKnowledgeClientApp.class.getResource("/")).toURI().getPath();
        } catch (URISyntaxException e) {
            logger.error("获取资源路径失败", e);
        }
    }

    public static void main(String[] args) {
        try {
            AiUiKnowledgeClient client = new AiUiKnowledgeClient.Builder(API_PASSWORD).build();

            // 创建个性化知识库
            CreateParam createParam = CreateParam.builder()
                    .uid(UID)
                    .name("测试库-001")
                    .build();
            String createResp = client.create(createParam);
            logger.info("创建结果：{}", createResp);

            // 查询个性化知识库，如果知识库没有文件，则查询结果为空
            SearchParam searchParam = SearchParam.builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .sceneName(SCENE_NAME)
                    .build();
            String searchResp = client.list(searchParam);
            logger.info("查询结果：{}", searchResp);

            // 关联知识库，管理知识库传参为全量保存方式
            LinkParam.Repo repo = new LinkParam.Repo();
            repo.setGroupId("您创建知识库返回的groupId");
            LinkParam linkParam = LinkParam.builder()
                    .appId(APP_ID)
                    .sceneName(SCENE_NAME)
                    .uid(UID)
                    .repos(Collections.singletonList(repo))
                    .build();
            String linkResp = client.link(linkParam);
            logger.info("关联结果：{}", linkResp);

            // 上传文件  支持本地文件和在线文件两种上传方式  冲突默认取本地上传文件
            UploadParam.FileInfo fileInfo = new UploadParam.FileInfo("aiuiknowledge.txt",
                    "https://oss-beijing-m8.openstorage.cn/knowledge-origin-test/knowledge/file/123123213/7741/a838a943/20250910163419/aiuiknowledge.txt",
                    43L);
            File file = new File(resourcePath + filePath);
            UploadParam uploadParam = UploadParam.builder()
                    .uid(UID)
                    .groupId("您创建知识库返回的groupId")
                    // .fileList(Collections.singletonList(fileInfo))
                    .files(Collections.singletonList(file))
                    .build();
            String uploadResp = client.upload(uploadParam);
            logger.info("上传结果：{}", uploadResp);

            // 删除知识库或者知识库内文件
            DeleteParam deleteParam = DeleteParam.builder()
                    .uid(UID)
                    .groupId("您创建知识库返回的groupId")
                    .docId("您上传文件返回的docId")
                    .build();
            String deleteResp = client.delete(deleteParam);
            logger.info("删除结果：{}", deleteResp);
        } catch (Exception e) {
            logger.error("请求失败", e);
        }
    }
}
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/AiUiKnowledgeClientApp.java)

## 错误码

| 状态码 | 含义             | 备注                               |
| ------ | ---------------- | ---------------------------------- |
| 0      | 操作成功         |                                    |
| 401    | 鉴权错误         |                                    |
| 307001 | 数据不存在       | API服务、文档无数据                |
| 307002 | 请求参数异常     | 请求参数非法                       |
| 303003 | 请求格式错误     |                                    |
| 307004 | 文件错误         | 文件大小、文件类型不合规           |
| 307005 | 业务错误         |                                    |
| 307006 | 系统数据异常     | 解析构建报错等                     |
| 307008 | 下游业务接口异常 |                                    |
| 307009 | 系统内部错误     |                                    |
| 307010 | 接口请求频繁     | 并发请求、异步处理中的接口再次发起 |

## 方法详解

### 1. 用户知识库创建(使用默认spark-1024向量库)
```java
public String create(CreateParam param) throws IOException
```
**参数说明**：

- `param`: 查询参数对象，可设置：

|    名称     |  类型  |     描述     | 必须 | 默认值 |
| :---------: | :----: | :----------: | ---- | ------ |
|     uid     |  long  |    用户ID    | Y    |        |
|    name     | String |  知识库名称  | Y    |        |
| description | String |  知识库描述  | N    |        |
|     sid     | String |   请求sid    | N    |        |
|   channel   | String | 来源渠道平台 | N    |        |

**响应示例**：

```json
{
"code": "0",
"flag": true,
"data": {
"id": 7,
"description": "",
"fromSource": "rag",
"groupId": "group_78cdcf66e9ef7873f1ec053d",
"labels": "",
"name": "sparkOS测试库",
"repoConfig": "{\"chunkSize\":1024,\"parseSplitModel\":
{\"id\":46,\"modelInfo\":\"
{\\\"layout\\\":\\\"true\\\",\\\"parseType\\\":1}\"},\"vectorModel\":
{\"id\":1,\"modelInfo\":\"
{\\\"name\\\":\\\"Turing_EN\\\",\\\"domain\\\":\\\"turing_en\\\",\\\"defaultDim
\\\":11}\"},\"dimModel\":{\"id\":11,\"modelInfo\":\"
{\\\"name\\\":\\\"1024\\\",\\\"chunkSize\\\":
[16,1024],\\\"defaultChunk\\\":\\\"1024\\\",\\\"version\\\":\\\"1.0.0\\\"}\"},\
"strategyModel\":{\"id\":-1}}",
"repoType": 1,
"status": 1,
"uid": 10000000003,
"createTime": "2025-05-20 09:36:20",
"updateTime": "2025-05-20 09:36:20"
},
"desc": "操作成功"
}
```

---

### 2. 知识库追加文件上传(内置解析拆分参数并自动构建)
```java
public String upload(UploadParam param) throws IOException
```
**参数说明**：

- `param`: 查询参数对象，可设置：

|          名称           |  类型  |          描述          | 必须 | 默认值                            |
| :---------------------: | :----: | :--------------------: | ---- | --------------------------------- |
|           uid           |  long  |         用户ID         | Y    |                                   |
|         groupId         | String |    用户知识库分组ID    | Y    |                                   |
|          files          | array  |     需要上传的文件     | N    | 与fileList二者传其一，都传取files |
|        fileList         | array  | 需要上传的文件路径信息 | N    | 与files二者传其一，都传取files    |
|  fileList[0].fileName   | String |        文件名称        |      |                                   |
|  fileList[0].filePath   | String |        文件地址        |      |                                   |
|  fileList[0].fileSize   |  long  |        文件大小        |      |                                   |
|         labels          | String |          标签          | N    |                                   |
|       parseConfig       | Object |      来源渠道平台      | N    |                                   |
|  parseConfig.chunkType  | String |   按标题/分隔符拆分    | Y    |                                   |
|  parseConfig.separator  | String |       分隔符数据       | N    |                                   |
|  parseConfig.cutLevel   | String |      标题拆分等级      | N    |                                   |
| parseConfig.lengthRange | String |   拆分chunk长度区间    | Y    |                                   |
|   parseConfig.cutOff    | String |       强行截断符       | N    |                                   |

**响应示例**：

```json
{
"code": "0",
"flag": true,
"data": [{
"buildStatus": 0,
"createTime": "2025-06-03 09:24:34",
"docName": "TXT-Template.txt",
"docType": "txt",
"docId": "cecca32dafa649ec91587e0123d1f3ad",
"extractStatus": 0,
"filePath": "https://oss-beijing-m8.openstorage.cn/knowledge-
origin-test/knowledge/file/1000002321/508/55e66251/20250603092430/TXT-
Template.txt",
"fileSize": 2550,
"id": 3452,
"parseType": 1,
"repoId": 508,
"status": 0,
"uid": 1000002321,
"upType": 1,
"updateTime": "2025-06-03 09:24:34"
}],
"desc": "操作成功"
}
```

---

### 3. 删除用户知识库或某个文件
```java
public String delete(DeleteParam param) throws IOException
```
**参数说明**：

- `param`: 查询参数对象，可设置：

|  名称   |  类型  |       描述       | 必须 | 默认值 |
| :-----: | :----: | :--------------: | ---- | ------ |
|   uid   |  long  |      用户ID      | Y    |        |
| groupId | String | 用户知识库分组ID | N    |        |
|  docId  | String |      文件ID      | N    |        |
|   sid   | String |     请求sid      | N    |        |
| repoId  | String |     知识库ID     | N    |        |

**响应示例**：

```json
{"code":"0","flag":true,"desc":"操作成功"}
```

---

### 4. 查询应用已绑定知识库列表及全量知识库列表
```java
public String list(SearchParam param) throws IOException
```
**参数说明**：

- `param`: 查询参数对象，可设置：

|   名称    |  类型  |         描述          | 必须 | 默认值 |
| :-------: | :----: | :-------------------: | ---- | ------ |
|    uid    |  long  |        用户ID         | Y    |        |
|   appId   | String |        应用ID         | Y    |        |
| sceneName | String |       场景名称        | Y    |        |
|    sid    | String |        请求sid        | N    |        |
|  channel  | String | 平台来源-用于数据隔离 | N    |        |

**响应示例**：

```json
{
"code": "0",
"flag": true,
"data": {
"repos": [{
"repoId": "insight_spark_201024_ki9u8",
"groupId": "group_09a1d9090ebe15f0e91fcd2d",
"description": "系统初始库",
"updateTime": "2024-12-18 16:19:54",
"capacity": 0,
"labels": "",
"uid": 1627,
"isDeleted": 0,
"createTime": "2024-12-16 11:51:34",
"isTop": 0,
"name": "系统初始库",
"id": 15,
"selected": true,
"fromSource": "xfyun",
"status": 1
},
{
"repoId": "insight_spark_201024_8ev8f",
"groupId": "group_c8f8293caeaebfe177bcd566",
"description": "",
"updateTime": "2025-03-24 14:08:32",
"capacity": 0,
"uid": 1627,
"isDeleted": 0,
"createTime": "2025-03-12 14:18:07",
"isTop": 0,
"name": "zjm的测试库",
"id": 96,
"fromSource": "aiCloud",
"configId": 2,
"threshold": "0.1",
"status": 1
}
]
},
"desc": "操作成功"
}
```



---

### 5. 用户应用关联绑定知识库
```java
public String link(LinkParam param) throws IOException
```
**参数说明**：

- `param`: 查询参数对象，可设置：

|        名称        |  类型  |                    描述                    | 必须 | 默认值 |
| :----------------: | :----: | :----------------------------------------: | ---- | ------ |
|        uid         |  long  |                   用户ID                   | Y    |        |
|       appId        | String |                   应用ID                   | Y    |        |
|     sceneName      | String |                  场景名称                  | Y    |        |
|        sid         | String |                  请求sid                   | N    |        |
|       repos        | array  | 需要绑定的知识库信息，**全量绑定保存逻辑** | N    |        |
|  repos[0].groupId  | String |              用户知识库分组ID              | Y    |        |
| repos[0].repoName  | String |                 知识库名称                 | N    |        |
| repos[0].threshold | String |                    阈值                    | N    |        |

**响应示例**：

```json
{
"code": "0",
"flag": true,
"data": {
},
"desc": "操作成功"
}
```



---

## 注意事项
1. 关联接口为全量关联保存逻辑， 注意全量传输repos参数
2. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）
3. 客户端默认超时时间为120秒，可通过Builder调整：

```java
new AiUiKnowledgeClient.Builder(API_PASSWORD)
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

---

**更多问题请打开官方文档联系技术支持**