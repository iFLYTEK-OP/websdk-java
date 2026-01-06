# 星火知识库 API文档

## 简介

本客户端基于基于讯飞星火大模型的知识库问答方案，够高效检索文档信息，准确回答专业问题，为大模型补充领域知识，让大模型助你高效了解文档内容，[官方文档](https://www.xfyun.cn/doc/spark/ChatDoc-API.html)，支持以下功能：

### 1. 基础功能

- 文档上传
- 文档状态查询
- 文档问答

### 2. 高级功能

#### 2.1 文档萃取

- 提交萃取任务
- 文件萃取状态查询
- 获取萃取结果
- QA对应用
- QA对更新
- QA对删除
- QA对查询

#### 2.2 文档处理

* 发起文档总结
* 获取文档总结信息
* 文档切分
* 文本向量化
* 文档向量化
* 文档内容相似度检测
* 文档分块内容获取
* 文档详情
* 文档列表
* 文档删除

#### 2.3 知识库操作

* 知识库创建
* 知识库添加文件
* 知识库移除文件
* 知识库列表
* 知识库详情
* 知识库文件列表
* 知识库删除

## 功能列表

| 方法名              | 功能说明           |
| ------------------- | ------------------ |
| fileUpload()        | 文档上传           |
| fileStatus()        | 文档状态查询       |
| chat()              | 文档问答           |
| fileExtract()       | 提交萃取任务       |
| fileExtractStatus() | 文件萃取状态查询   |
| fileExtractResult() | 获取萃取结果       |
| qaApply()           | QA对应用           |
| qaUpdate()          | QA对更新           |
| qaDelete()          | QA对删除           |
| qaQuery()           | QA对查询           |
| fileSummaryCreate() | 发起文档总结       |
| fileSummaryQuery()  | 获取文档总结信息   |
| fileSplit()         | 文档切分           |
| embedding()         | 文本向量化         |
| fileEmbeddingV2()   | 文档向量化         |
| fileVector()        | 文档内容相似度检测 |
| fileChunks()        | 文档分块内容获取   |
| fileInfo()          | 文档详情           |
| fileList()          | 文档列表           |
| fileDelete()        | 文档删除           |
| repoCreate()        | 知识库创建         |
| repoAddFile()       | 知识库添加文件     |
| repoRemoveFile()    | 知识库移除文件     |
| repoList()          | 知识库列表         |
| repoInfo()          | 知识库详情         |
| repoFileList()      | 知识库文件列表     |
| repoDelete()        | 知识库删除         |

## 使用准备

1. 前往[**星火知识库**](https://www.xfyun.cn/services/spark_knowledge_base)页面开通权限
2. 前往[**文本向量化**](https://www.xfyun.cn/services/embedding)页面开通权限
3. 创建应用并获取以下凭证：
   - APPID 
   - APIKey（选取-文本向量化能力使用）
   - APISecret

## 快速开始

1、添加maven依赖

```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-spark</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.1.9</version>
</dependency>
```

2、Java代码

```java
import cn.xfyun.api.RagClient;
import cn.xfyun.config.ExtractionStatus;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.config.SummaryStatus;
import cn.xfyun.model.rag.*;
import cn.xfyun.model.sparkmodel.RoleContent;

            RagClient client = new RagClient.Builder(APP_ID, API_SECRET).build();
            String fileId = "ebb6fae156394a7d822a4a25e1ad388c";
            String repoId = "f865d47fdd604c8690031767fe1e51b9";

            // 文件上传
            RepoUpload upload = RepoUpload.builder()
                    .file(new File(resourcePath + filePath))
                    .build();
            String uploadResp = client.fileUpload(upload);
            logger.info("知识库文件上传结果：{}", uploadResp);

            ....

            // 文本向量化
            RoleContent user = RoleContent.builder()
                    .role("user")
                    .content(getContent())
                    .build();
            EmbeddingParam param = EmbeddingParam.builder()
                    .appId(APP_ID)
                    .apiKey("您的向量化API_KEY")
                    .apiSecret(API_SECRET)
                    .messages(Collections.singletonList(user))
                    .domain("query")
                    .build();
            String embeddingResp = client.embedding(param);
            JSONObject json = JSONUtil.parseObj(embeddingResp);
            String text = json.getJSONObject("payload").getJSONObject("feature").getStr("text");
            String result = Base64Decoder.decodeStr(text, StandardCharsets.UTF_8);
            logger.info("新版本向量化返回结果：{}", result);
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/RagClientApp.java)

## 错误码

| 错误码 | 描述                               | 处理方式           |
| ------ | ---------------------------------- | ------------------ |
| 10019  | 问答的问题或引用文段可能涉政       | 检查问题或文件内容 |
| 10013  | 问答的问题或引用文段有敏感违规信息 | 检查问题或文件内容 |
| 10014  | 问答的输出有敏感违规信息           | 尝试换个问法       |
| 60001  | 文件类型不对                       | 检查文件           |
| 60002  | 文件大小超限                       | 检查文件           |
| 60003  | 文件上传失败                       | 排查               |
| 60005  | 无文件权限                         | 检查入参           |
| 60011  | 文件字数超限                       | 检查文件           |
| 60012  | 文件无有校字符                     | 检查文件           |
| 60014  | 问答的时候未传入文件 id            | 检查入参           |
| 62001  | 问答的时候，未找到相关文本段       | 检查提问问题       |
| 68003  | 操作太过频繁                       | 自查               |
| 99999  | 内部错误                           | 排查               |

## 方法详解

### 1. 文档上传
```java
public String fileUpload(RepoUpload upload) throws IOException
```
**参数说明**：

- `RepoUpload`: 查询参数对象，可设置：

|    名称     |  类型   |                             描述                             | 必须 | 默认值 |
| :---------: | :-----: | :----------------------------------------------------------: | ---- | ------ |
|    file     |  File   | 前支持 doc/docx、pdf、md、txt 格式，单文件大小不超过 20MB，不超过 100W 字符。 | N    |        |
|     url     | String  |            文件 url （文件和文件 url 必须有一个）            | N    |        |
|  fileName   | String  | 文件名称，带后缀。文件用 url 的方式，该字段必传； </br> 传 file 的话，该字段可不传 | N    |        |
|  fileType   | String  |                 文件类型，目前传固定值"wiki"                 | N    | wiki   |
|  parseType  | String  | 文件解析类型，"AUTO"-服务端智能判断是否需要走OCR， * "TEXT"-直接读取文件文本内容，"OCR"-强制走OCR，目前仅pdf、word支持 | N    | AUTO   |
| stepByStep  | Boolean | 是否分步处理，true代表文件上传之后，服务只做了分片， * 这时候还不能去问答。需要业务做分片的确认，然后调用【文档向量化】接口，发起切片向量。待向量完成，即可问答。 | N    | false  |
| callbackUrl | String  | 文件状态回调地址，文件状态有变动时服务会调用该 url。 * 调用的时候会带上鉴权头，鉴权方式同【接口鉴权】，业务可根据需要是否做鉴权校验 | N    |        |
|   extend    | String  | 文件拆分扩展字段(转为JSONString)，也可在分步时，单独调用拆分接口重新发起拆分(可使用WikiSplitExtends对象) | N    |        |

**响应示例**：

```json
{
  "flag": true,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "code": 0,
  "desc": null,
  "data": {
    "quantity": "12",
    "parseType": "TEXT",
    "fileId": "24c4109a57944997bc6b82661243ef67"
  }
}
```

---

### 2. 文档状态查询
```java
public String fileStatus(FileStatus status) throws IOException
```
**参数说明**：

- `FileStatus`: 查询参数对象，可设置：

|  名称   |  类型  |                    描述                    | 必须 | 默认值 |
| :-----: | :----: | :----------------------------------------: | ---- | ------ |
| fileIds | String | 上传的文件id列表，多个文档id用英文逗号分割 | Y    |        |

**响应示例**：

```json
{
  "code":0,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "desc":"成功",
  "data":[
    {
      "fileId":"123",
      "fileStatus":"uploaded"
    }
  ]
}
```

---

### 3. 文档问答
```java
public void chat(FileChat chat, WebSocketListener listener) throws MalformedURLException, SignatureException
```
**参数说明**：

- `FileChat`: 查询参数对象，可设置：

|    名称     |  类型  |                             描述                             | 必须 | 默认值      |
| :---------: | :----: | :----------------------------------------------------------: | ---- | ----------- |
|   repoId    | String | 知识库id，单个知识库最多包括100个文档 (repoId、repoIds、fileIds必传其一) | Y    |             |
|   repoIds   | Array  |   知识库id列表，最大100 (repoId、repoIds、fileIds必传其一)   | Y    |             |
|   fileIds   | Array  |    文件id列表，最大200 (repoId、repoIds、fileIds必传其一)    | Y    |             |
| llmVersion  | String | 底层大模型版本,可选值generalv3.5、vdeepseekv3。 * generalv3.5：星火大模型 Spark Max，vdeepseekv3：DeepSeek V3 | N    | generalv3.5 |
|    topN     |  int   |                     向量库文本段查询数量                     | N    |             |
|  messages   | Array  |         问答内容列表，按时间正序，最后一条为最新提问         | Y    |             |
| chatExtends |  Map   |                   大模型对话自定义扩展字段                   | N    |             |

**响应示例**：

```json
{
  "code": 0,
  "content": "和指南。在此之前，我可以为您",
  "fileRefer": "{\"b2de1116b590911111119d0f3dbfc8\":[3,7,8]}",
  "sid": "870b299a48ba46b6b5e4dfbd99fe4497",
  "status": 1
}
```

---

### 4. 提交萃取任务
```java
public String fileExtract(FileExtract extract) throws IOException
```
**参数说明**：

- `FileExtract`: 查询参数对象，可设置：

|      名称       |  类型   |                             描述                             | 必须 | 默认值 |
| :-------------: | :-----: | :----------------------------------------------------------: | ---- | ------ |
|     fileId      | String  |                            文件id                            | Y    |        |
|    chunkSize    |   int   | 分片长度，根据这个长度将文件内容分块，然后根据每个分块抽取问题答案 | Y    |        |
|   numPerChunk   |   int   |    每个分片问题数（通过改该值限制抽取问题数，非100%准确）    | Y    |        |
|   answerSize    |   int   |        答案长度（通过改该值限制答案长度，非100%准确）        | N    |        |
| topicPreference |  Array  |                           主题偏好                           | N    |        |
|  includeAnswer  | Boolean |               是否包含答案，false的话仅有问题                | N    | true   |
|    notifyUrl    | String  |                           回调地址                           | N    |        |

**响应示例**：

```json
{
  "code": 0,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "desc": "成功",
  "data": "27805ef975654c01aa069f4e9aad0bb1"
}
```

---

### 5. 文件萃取状态查询
```java
public String fileExtractStatus(FileResult result) throws IOException
```
**参数说明**：

- `FileResult`: 查询参数对象，可设置：

|  名称  |  类型  |  描述  | 必须 | 默认值 |
| :----: | :----: | :----: | ---- | ------ |
| fileId | String | 文件Id | N    |        |

**响应示例**：

```json
{
  "flag": true,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "code": 0,
  "desc": null,
  "data": "EXTRACTED"
}
```

---

### 6. 获取萃取结果
```java
public String fileExtractResult(FileResult result) throws IOException
```
**参数说明**：

- `FileResult`: 查询参数对象，可设置：

|  名称  |  类型  |            描述            | 必须 | 默认值 |
| :----: | :----: | :------------------------: | ---- | ------ |
| taskId | String | 任务Id，与fileId必须传一个 | N    |        |
| fileId | String |           文件Id           |      |        |

**响应示例**：

```json
{
  "flag": true,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "code": 0,
  "desc": null,
  "data": [{
    "id": "664d98e4e241e050529c74c7",
    "question": "在《鹧鸪天》中，“殷勤昨夜三更雨，又得浮生一日凉”这句诗的含义是什么？",
    "answer": "在《鹧鸪天》中，“殷勤昨夜三更雨，又得浮生一日凉”这句诗的含义是表达了作者对自然美景的欣赏和对生活的感悟。",
    "chunkIndexReferences": [1,2,3],
    "answerChunkIndexReferences": [1,2,3]
  }]
}
```

------

### 7. QA对应用

```java
public String qaApply(QaApply qaApply) throws IOException
```

**参数说明**：

- `QaApply`: 查询参数对象，可设置：

|   名称   |  类型  |                        描述                         | 必须 | 默认值 |
| :------: | :----: | :-------------------------------------------------: | ---- | ------ |
|  repoId  | String |                      知识库id                       | Y    |        |
|  fileId  | String |                       文件Id                        | Y    |        |
| question | String |                        问题                         | Y    |        |
|  answer  | String |                        答案                         | Y    |        |
| embType  | String | 向量类型，Q-仅仅问题做向量、QA-问题和内容一起做向量 | N    | QA     |

**响应示例**：

```json
{
  "code": 0,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "desc": "成功",
  "data": "5a5f57a52f834ae5b12888ebd282ea0a"
}
```

------

### 8. QA对更新

```java
public String qaUpdate(QaApply qaApply) throws IOException
```

**参数说明**：

- `QaApply`: 查询参数对象，可设置：

|   名称   |  类型  |                        描述                         | 必须 | 默认值 |
| :------: | :----: | :-------------------------------------------------: | ---- | ------ |
|    id    | String |                       QA对id                        | Y    |        |
|  repoId  | String |                      知识库id                       | Y    |        |
|  fileId  | String |                       文件Id                        | Y    |        |
| question | String |                        问题                         | Y    |        |
|  answer  | String |                        答案                         | Y    |        |
| embType  | String | 向量类型，Q-仅仅问题做向量、QA-问题和内容一起做向量 | N    | QA     |

**响应示例**：

```json
{
  "code": 0,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "desc": "成功"
}
```

------

### 9. QA对删除

```java
public String qaDelete(List<String> ids) throws IOException
```

**参数说明**：

- `ids`: qa对id集合

**响应示例**：

```json
{
  "code": 0,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "desc": "成功"
}
```

------

### 10. QA对查询

```java
public String qaQuery(QaQuery qaQuery) throws IOException
```

**参数说明**：

- `QaQuery`: 查询参数对象，可设置：

|    名称     |  类型  |            描述            | 必须 | 默认值 |
| :---------: | :----: | :------------------------: | ---- | ------ |
|   fileId    | String | 文件id，和知识库id必传一个 | N    |        |
|   repoId    | String |          知识库id          | N    |        |
| currentPage |  int   |         当前第几页         | N    | 1      |
|  pageSize   |  int   |          每页几条          | N    | 10     |

**响应示例**：

```json
{
  "code": 0,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "desc": "成功",
  "data": "5a5f57a52f834ae5b12888ebd282ea0a"
}
```

------

### 11. 发起文档总结

```java
public String fileSummaryCreate(String fileId) throws IOException
```

**参数说明**：

- `fileId`: 文档ID

**响应示例**：

```json
{
  "flag": true,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "code": 0,
  "desc": null,
  "data": null
}
```

------

### 12. 获取文档总结信息

```java
public String fileSummaryQuery(String fileId) throws IOException
```

**参数说明**：

- `fileId`: 文档ID

**响应示例**：

```json
{
  "flag": true,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "code": 0,
  "desc": null,
  "data": null
}
```

------

### 13. 文档切分

```java
public String fileSplit(FileSplit fileSplit) throws IOException
```

**参数说明**：

- `FileSplit`: 查询参数对象，可设置：

|       名称       |  类型   |                             描述                             | 必须 | 默认值 |
| :--------------: | :-----: | :----------------------------------------------------------: | ---- | ------ |
|     fileIds      |  Array  |    文件id列表，最大200 (repoId、repoIds、fileIds必传其一)    | Y    |        |
|  isSplitDefault  | Boolean | 是否用默认切分策略，如果需要自定义切分符，该值需要设置为false | Y    | true   |
|    splitType     | String  |                按什么类型拆分，传固定 "wiki"                 | Y    | wiki   |
| wikiSplitExtends | String  |     大模型对话自定义扩展字段(可使用WikiSplitExtends对象)     | N    |        |

**响应示例**：

```json
{
  "code": 0,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "desc": "成功",
  "data": null
}	
```

------

### 14. 文档向量化

```java
public String fileEmbeddingV2(List<String> fileIds) throws IOException
```

**参数说明**：

- `fileIds`: 文档Id集合

**响应示例**：

```json
{
  "code": 0,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "desc": "成功",
  "data": null
}
```

------

### 15. 文本向量化

```java
public String embedding(EmbeddingParam param) throws IOException
```

**参数说明**：

- `EmbeddingParam`: 查询参数对象，可设置：

|   名称    |  类型  |                           描述                            | 必须 | 默认值 |
| :-------: | :----: | :-------------------------------------------------------: | ---- | ------ |
| messages  | Array  |  需要向量化的数据,最小尺寸:1B, 最大尺寸:2K,需base64编码   | Y    |        |
|  domain   | String |        query-用户问题向量化 ; para-知识原文向量化         | Y    |        |
|  userId   | String | 请求用户服务返回的uid，用户及设备级别个性化功能依赖此参数 | N    |        |
|   appId   | String |                           appId                           | Y    |        |
|  apiKey   | String |                          apiKey                           | Y    |        |
| apiSecret | String |                         apiSecret                         | Y    |        |

**响应示例**：

```json
{
    "header": {
        "code": 0,
        "message": "success",
        "sid": "ase000704fa@dx16ade44e4d87a1c802"
    },
    "payload": {
        "feature": {
            "encoding": "utf8",
            "compress": "raw",
            "format": "plain",
            "text": ""
        }
    }
}
```

------

### 16. 文档内容相似度检测

```java
public String fileVector(FileVector vector) throws IOException
```

**参数说明**：

- `FileVector`: 查询参数对象，可设置：

|    名称     |  类型  |                          描述                          | 必须 | 默认值 |
| :---------: | :----: | :----------------------------------------------------: | ---- | ------ |
|   fileIds   | Array  | 文件id列表，最大200 (repoId、repoIds、fileIds必传其一) | N    |        |
|    topN     |  int   |                  向量库文本段查询数量                  | N    | 5      |
|   content   | String |                       用户的问题                       | Y    |        |
| chatExtends |  Map   |                大模型对话自定义扩展字段                | N    |        |

**响应示例**：

```json
{
  "code":0,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "desc":"成功",
  "data":[
    {
      "content":"xxx",
      "score": 67.5,
      "fileId": "aa9efce4188b4025b6f9a3cd35e6de99"
    }]
}
```

------

### 17. 文档分块内容获取

```java
public String fileChunks(String fileId) throws IOException
```

**参数说明**：

- `fileId`: 文件Id

**响应示例**：

```json
{
  "flag": true,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "code": 0,
  "desc": null,
  "data": [
    {
      "id": "f37203b9d39d4afb8816d30080047695",
      "question": null,
      "dataType": "wiki",
      "dataIndex": 0,
      "content": "名称:智能会议解决方案\n简介:讯飞听见智能会议系统是科大讯飞核心语音技术的集大成者，能将语音实时转成文字，经过个性化订制的标准普通话，转写准确率可达到95%以上，适用于各类会议。\n公司:科大讯飞股份有限公司\n价格:面议\n产品详情:讯飞听见智能会议系统是科大讯飞核心语音技术的集大成者，能将语音实时转成文字，经过个性化订制的标准普通话，转写准确率较高，适用于各类会议。\n链接:https://www.aifuwus.com/onstage/cmddetail?id=59"
    },
    {
      "id": "6567375c79c44f28badd1f0c62794a70",
      "question": null,
      "dataType": "wiki",
      "dataIndex": 1,
      "content": "\r\n名称:服务机器人解决方案\n简介:可提供各种场景不同外观的实体机器人定制及租赁服务，应用于展厅、机场、政务、银行、酒店、商超、医院等公共区域。您留下信息后我们将安排商务经理专人对接。\n公司:科大讯飞股份有限公司\n价格:面议\n产品详情:智能机器人可提供迎宾接待、智能交互、多媒体播放等服务，可同时采集用户线下的“交互式”行为数据。融合线下场景数据、CRM数据和讯飞DMP数据，对目标用户属性、需求偏好、内容消费趋势、潜在需求意图等进行全方位的分析，最终实现深刻理解和预测用户的需求意图。帮助品牌与消费者建立一对一的高效互动，提高营销效率，同时为用户减少信息噪音。\n面对不同行业客户个性化的需求，我们提供包括硬件、软件、云平台、PC端、移动端的完整解决方案，旨在构建一个“硬件+软件+服务+营销”的智能生态圈，致力于让每一个行业享受智慧科技。\n链接:https://www.aifuwus.com/onstage/cmddetail?id=67"
    }
  ]
}
```

------

### 18. 文档详情

```java
public String fileInfo(String fileId) throws IOException
```

**参数说明**：

- `fileId`: 文件Id

**响应示例**：

```json
{
  "flag": true,
  "code": 0,
  "desc": null,
  "data": {
    "fileId": "b0862da4c91147209434023c48665957",
    "fileName": "《科大讯飞文档知识库产品说明书》.pdf",
    "extName": "pdf",
    "fileType": "wiki",
    "fileStatus": "vectored",
    "quantity": 40,
    "createTime": "2024-01-10 09:50:19",
    "expireTime": "2024-04-09 23:59:59",
    "expirationStatus": "active"
  },
  "sid": "e01f951b14bb427286bbf6048e816745"
}
```

------

### 19. 文档列表

```java
public String fileList(FileList fileList) throws IOException
```

**参数说明**：

- `FileList`: 查询参数对象，可设置：

|    名称     |  类型  |        描述        | 必须 | 默认值 |
| :---------: | :----: | :----------------: | ---- | ------ |
|  fileName   | String | 文件名称，模糊查询 | N    |        |
|   extName   | String |      文件后缀      | N    |        |
| fileStatus  | String |      文件类型      | N    |        |
| currentPage |  int   |     当前第几页     | N    | 1      |
|  pageSize   |  int   |      每页几条      | N    | 10     |

**响应示例**：

```json
{
  "flag": true,
  "code": 0,
  "desc": null,
  "data": {
    "total": 67,
    "rows": [
      {
        "fileId": "b59b99d4dbc34157b40d2106a32414ac",
        "fileName": "SFU实时音视频产品白皮书_v1.0.pdf",
        "extName": "pdf",
        "fileType": "wiki",
        "fileStatus": "splited",
        "quantity": 24,
        "createTime": "2024-01-11 19:19:35",
        "expireTime": "2024-04-10 23:59:59",
        "expirationStatus": "active"
      },
      {
        "fileId": "91d4cd682c3f4314b3642fb4d45336f8",
        "fileName": "AI中台产品白皮书_V1.0.pdf",
        "extName": "pdf",
        "fileType": "wiki",
        "fileStatus": "vectored",
        "quantity": 24,
        "createTime": "2023-11-14 18:53:14",
        "expireTime": "2024-02-12 23:59:59",
        "expirationStatus": "expired"
      }
    ]
  },
  "sid": "1a4000e8e1ae4edfbf29c0f63b391161"
}
```

------

### 20. 文档删除

```java
public String fileDelete(List<String> fileIds) throws IOException
```

**参数说明**：

- `fileIds`: 文档ID集合

**响应示例**：

```json
{
  "code": 0,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "desc": "成功",
  "data": null
}
```

------

### 21. 知识库创建

```java
public String repoCreate(RepoCreate create) throws IOException
```

**参数说明**：

- `RepoCreate`: 查询参数对象，可设置：

|   名称   |  类型  |       描述       | 必须 | 默认值 |
| :------: | :----: | :--------------: | ---- | ------ |
| repoName | String | 知识库名称，唯一 | Y    |        |
| repoDesc | String |    知识库简介    | N    |        |
| repoTags | String |    知识库标签    | N    |        |

**响应示例**：

```json
{
  "flag": true,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "code": 0,
  "desc": null,
  "data": "24c4109a57944997bc6b82661243ef67"
}
```

------

### 22. 知识库添加文件

```java
public String repoAddFile(RepoFile addFile) throws IOException
```

**参数说明**：

- `RepoFile`: 参数对象，可设置：

|  名称   |  类型  |       描述       | 必须 | 默认值 |
| :-----: | :----: | :--------------: | ---- | ------ |
| repoId  | String |     知识库id     | Y    |        |
| fileIds | Array  | 件id列表，最大20 | Y    |        |

**响应示例**：

```json
{
  "flag": true,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "code": 0,
  "desc": null,
  "data": {
    "failedList": [
      "5a5f57a52f834ae5b12888ebd282ea0a"
    ]
  }
}
```

------

### 23. 知识库移除文件

```java
public String repoRemoveFile(RepoFile addFile) throws IOException
```

**参数说明**：

- `RepoFile`: 参数对象，可设置：

|  名称   |  类型  |        描述        | 必须 | 默认值 |
| :-----: | :----: | :----------------: | ---- | ------ |
| repoId  | String |      知识库id      | Y    |        |
| fileIds | Array  | 文件id列表，最大20 | Y    |        |

**响应示例**：

```json
{
  "flag": true,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "code": 0,
  "desc": null,
  "data": {
    "failedList": [
      "5a5f57a52f834ae5b12888ebd282ea0a"
    ]
  }
}
```

------

### 24. 知识库列表

```java
public String repoList(RepoQuery repoQuery) throws IOException
```

**参数说明**：

- `RepoQuery`: 查询参数对象，可设置：

|    名称     |  类型  |         描述         | 必须 | 默认值 |
| :---------: | :----: | :------------------: | ---- | ------ |
|  repoName   | String | 知识库名称，模糊查询 | N    |        |
| currentPage |  int   |      当前第几页      | N    | 1      |
|  pageSize   |  int   |       每页几条       | N    | 10     |

**响应示例**：

```json
{
  "flag": true,
  "code": 0,
  "desc": null,
  "data": [{
    "repoId": "9d1a5beea01e4b4a80f12f66f1ee23b0",
    "repoName": "云平台产品说明",
    "repoDesc": "云平台所有产品说明",
    "repoTags": "",
    "createTime": "2024-02-18 17:47:37"
  }],
  "sid": "0b953bd85e4c4675a8700ba705d69953"
}
```

------

### 25. 知识库详情

```java
public String repoInfo(String repoId) throws IOException
```

**参数说明**：

- `repoId`: 知识库ID

**响应示例**：

```json
{
  "flag": true,
  "code": 0,
  "desc": null,
  "data": [{
    "repoId": "9d1a5beea01e4b4a80f12f66f1ee23b0",
    "repoName": "云平台产品说明",
    "repoDesc": "云平台所有产品说明",
    "repoTags": "",
    "createTime": "2024-02-18 17:47:37"
  }],
  "sid": "0b953bd85e4c4675a8700ba705d69953"
}
```

------

### 26. 知识库文件列表

```java
public String repoFileList(RepoFileList repoFileList) throws IOException
```

**参数说明**：

- `RepoFileList`: 查询参数对象，可设置：

|    名称     |  类型  |       描述       | 必须 | 默认值 |
| :---------: | :----: | :--------------: | ---- | ------ |
|   repoId    | String |     知识库id     | Y    |        |
|  fileName   | String | 件名称，模糊查询 | N    |        |
|   extName   | String |     文件后缀     | N    |        |
| currentPage |  int   |    当前第几页    | N    | 1      |
|  pageSize   |  int   |     每页几条     | N    | 20     |

**响应示例**：

| 字段名                | 类型   | 描述                                                         |
| --------------------- | ------ | ------------------------------------------------------------ |
| code                  | Int    | 错误码，成功=0                                               |
| sid                   | String | 请求唯一 id，用于问题定位                                    |
| desc                  | String | 结果描述                                                     |
| data                  | Array  | 返回结果                                                     |
| data.fileId           | String | 文件id                                                       |
| data.fileName         | String | 文件名称                                                     |
| data.fileType         | String | 文件类型wiki、qa                                             |
| data.fileStatus       | String | 文件状态                                                     |
| data.extName          | String | 文件后缀                                                     |
| data.quantity         | Int    | 文件计量数                                                   |
| data.expirationStatus | String | 文件过期状态 active-正常 expired-已过期                      |
| data.createTime       | String | 文件创建时间                                                 |
| data.expireTime       | Date   | 文件过期时间，pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" |

------

### 27. 知识库删除

```java
public String repoDelete(String repoId) throws IOException
```

**参数说明**：

- `repoId`: 知识库ID

**响应示例**：

```json
{
  "code": 0,
  "sid": "9746244f046340e2869630e5f6fe8daa",
  "desc": "成功",
  "data": null
}
```

---

## 注意事项
1. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）

2. 客户端默认超时时间为120秒，可通过Builder调整：

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
### 文档知识库的主要功能是什么？

> 答：让大模型根据文档内容回答问题，更可创建知识库聚合多文档，一次提问遍历领域知识；文档太多难以通读，快用文档总结，自动总结文档概要，快速了解文档内容等。

### 文档知识库现在支持哪些格式的文档？

> 答：目前支持 Word、PDF、Markdown、txt 格式的文档。

### 文档知识库支持什么应用平台？

> 答：目前支持 WebAPI 应用平台。

---

**更多问题请打开官方文档联系技术支持**