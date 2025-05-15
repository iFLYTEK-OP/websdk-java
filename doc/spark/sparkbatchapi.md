# 大模型批处理API文档

## 简介

本客户端基于讯飞Spark API实现，提供智能大模型批量处理能力[官方文档](https://www.xfyun.cn/doc/spark/BatchAPI.html)，支持以下功能：

- 上传文件
- 查询文件列表
- 查询单个文件
- 删除文件
- 下载文件
- 创建Batch任务
- 查询batch任务
- 取消batch任务
- 查询batch任务列表

## 功能列表

| 方法名       | 功能说明                                                     |
| ------------ | ------------------------------------------------------------ |
| upload()     | 上传文件 , 格式：只支持 jsonl 文件名后缀 ,上传的文件默认保存 48h。 |
| listFile()   | 获取 appid 下的文件列表。                                    |
| getFile()    | 根据文件的file_id获取该文件信息。                            |
| deleteFile() | 根据文件的file_id删除该文件                                  |
| download()   | 根据文件的file_id获取文件的详细内容。 上传的文件默认保存 48h；接口返回的文件按照文件生成的时间开始计时，默认保存 30 天。 |
| create()     | 调用该接口前需要先上传jsonl文件，通过上传文件得到的file_id来创建Batch任务。 单个Batch任务最多包含5万个请求（一个请求对应jsonl文件的一行），每个请求的body不超过6KB |
| getBatch()   | 根据batch_id获取该Batch任务的详细信息                        |
| cancel()     | 根据batch_id取消该Batch任务                                  |
| listBatch()  | 获取 appid 下的Batch列表                                     |

## 使用准备

1. 前往[能力开通](https://console.xfyun.cn/services/bm3.5batch)页面
2. 创建应用并获取以下凭证：
   - APPID 
   - API_PASSWORD

## 快速开始

1、添加maven依赖

```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-spark</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.1.0</version>
</dependency>
```

2、Java代码

```java
import cn.xfyun.api.AIPPTV2Client;
import cn.xfyun.model.aippt.request.Outline;
import cn.xfyun.model.aippt.request.PPTCreate;
import cn.xfyun.model.aippt.request.PPTSearch;
import cn.xfyun.model.aippt.response.PPTCreateResponse;
import cn.xfyun.model.aippt.response.PPTProgressResponse;
import cn.xfyun.model.aippt.response.PPTThemeResponse;


 SparkBatchClient client = new SparkBatchClient.Builder(APP_ID, API_PASSWORD).build();

            String upload = client.upload(new File(resourcePath + filePath));
            FileInfo uploadResp = JSON.parseObject(upload, FileInfo.class);
            logger.info("文件上传返回结果 ==> {}", JSON.toJSONString(uploadResp));

            String fileList = client.listFile(1, 20);
            logger.info("文件查询返回结果 ==> {}", fileList);

            String getFile = client.getFile(uploadResp.getId());
            FileInfo getFileResp = JSON.parseObject(getFile, FileInfo.class);
            logger.info("获取文件信息返回结果 ==> {}", JSON.toJSONString(getFileResp));

            String download = client.download(uploadResp.getId());
            logger.info("下载文件返回结果 ==> {}", download);

            String deleteFile = client.deleteFile(uploadResp.getId());
            DeleteResponse deleteResp = JSON.parseObject(deleteFile, DeleteResponse.class);
            logger.info("删除文件返回结果 ==> {}", JSON.toJSONString(deleteResp));

            String upload1 = client.upload(new File(resourcePath + filePath));
            FileInfo uploadResp1 = JSON.parseObject(upload1, FileInfo.class);
            logger.info("文件上传返回结果 ==> {}", JSON.toJSONString(uploadResp1));

            String create = client.create(uploadResp1.getId(), null);
            BatchInfo createResp = JSON.parseObject(create, BatchInfo.class);
            logger.info("创建任务返回结果 ==> {}", JSON.toJSONString(createResp));

            String getBatch = client.getBatch(createResp.getId());
            BatchInfo getBatchResp = JSON.parseObject(getBatch, BatchInfo.class);
            logger.info("获取任务信息返回结果 ==> {}", JSON.toJSONString(getBatchResp));

            String cancel = client.cancel(createResp.getId());
            BatchInfo cancelResp = JSON.parseObject(cancel, BatchInfo.class);
            logger.info("取消任务返回结果 ==> {}", JSON.toJSONString(cancelResp));

            String listBatch = client.listBatch(10, null);
            BatchListResponse listBatchResp = JSON.parseObject(listBatch, BatchListResponse.class);
            logger.info("查询Batch列表返回结果 ==> {}", JSON.toJSONString(listBatchResp));
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/SparkBatchClientApp.java)

## 方法详解

### 1. 上传文件
```java
public String upload(File file) throws IOException
```
**参数说明**：

- `file`: 上传文件

**响应示例**：

```json
{
  "id": "file-abc123",
  "object": "file",
  "bytes": 120000,
  "created_at": 1677610602,
  "filename": "mydata.jsonl",
  "purpose": "batch"
}
```

---

### 2. 查询文件列表
```java
public String listFile(int pageNum, int pageSize) throws IOException
```
**参数说明**：

|   名称   | 类型 |  描述  | 必须 | 默认值 |
| :------: | :--: | :----: | ---- | ------ |
| pageNum  | int  |  页码  | Y    |        |
| pageSize | int  | 页大小 | Y    |        |

**响应示例**：

```text
{
  "data": [
    {
      "id": "file-abc123",
      "object": "file",
      "bytes": 175,
      "created_at": 1613677385,
      "filename": "mydata.jsonl",
      "purpose": "batch"
    },
    {
      "id": "file-abc123",
      "object": "file",
      "bytes": 140,
      "created_at": 1613779121,
      "filename": "puppy.jsonl",
      "purpose": "batch"
    }
  ],
  "object": "list"
}
```

---

### 3. 查询单个文件
```java
public String getFile(String fileId) throws IOException
```
**参数说明**：

|  名称  |  类型  |  描述  | 必须 | 默认值 |
| :----: | :----: | :----: | ---- | ------ |
| fileId | String | 文件Id | Y    |        |

**响应示例**：

```text
{
  "id": "file-abc123",
  "object": "file",
  "bytes": 120000,
  "created_at": 1677610602,
  "filename": "mydata.jsonl",
  "purpose": "batch"
}
```

---

### 4. 删除文件
```java
public String deleteFile(String fileId) throws IOException 
```
**参数说明**：

|  名称  |  类型  |  描述  | 必须 | 默认值 |
| :----: | :----: | :----: | ---- | ------ |
| fileId | String | 文件Id | Y    |        |

**响应示例**：

```json
{
  "id": "file-abc123",
  "object": "file",
  "deleted": true
}
```

---

### 5. 下载文件
```java
public String download(String fileId) throws IOException
```
**参数说明**：

|  名称  |  类型  |  描述  | 必须 | 默认值 |
| :----: | :----: | :----: | ---- | ------ |
| fileId | String | 文件Id | Y    |        |

**响应示例**：

```json
{"custom_id": "request-1", "method": "POST", "url": "/v1/chat/completions", "body": {"model": "4.0Ultra", "messages": [{"role": "system", "content": "You are a helpful assistant."},{"role": "user", "content": "1+1=?"}],"max_tokens": 1000}}
```

---

### 6. 创建Batch任务
```java
public String create(String fileId, Map<String, String> metadata) throws IOException
```
**参数说明**：

|   名称   |  类型  |      描述      | 必须 | 默认值 |
| :------: | :----: | :------------: | ---- | ------ |
|  fileId  | String |     文件Id     | Y    |        |
| metadata | Object | 批任务附加信息 | N    |        |

**响应示例**：

##### 响应参数说明

| 字段名                   | 类型   | 描述                                                         |
| ------------------------ | ------ | ------------------------------------------------------------ |
| id                       | string | 批任务唯一标识（全局唯一） 格式为：batch_xxxxx               |
| object                   | string | 结构类型，当前仅为 batch                                     |
| endpoint                 | string | 处理批任务的服务的 path 路径，当前仅为 /v1/chat/completions  |
| errors                   | object | 错误列表                                                     |
| errors.object            | string | 描述 data 类型 (list)                                        |
| errors.data              | array  | [{code string, message string, param string or null, line int or null}] |
| input_file_id            | string | 文件id（用户入参）                                           |
| status                   | string | 任务状态（枚举值）                                           |
| output_file_id           | string | 结果文件id                                                   |
| error_file_id            | string | 错误文件id                                                   |
| created_at               | int    | 任务创建时间（unix时间戳，下面的时间也均为unix时间戳）       |
| in_progress_at           | int    | 任务开始处理时间                                             |
| expires_at               | int    | 任务预期超时时间                                             |
| finalizing_at            | int    | 任务完成开始时间(写 out_file && error_file 开始)             |
| completed_at             | int    | 任务完成结束时间(写 out_file && error_file 结束)             |
| failed_at                | int    | 任务失败时间(所有任务失败时返回)                             |
| expired_at               | int    | 任务实际超时时间                                             |
| cancelling_at            | int    | 任务取消开始时间 （收到取消请求时间）                        |
| cancelled_at             | int    | 任务取消结束时间 （任务实际取消时间）                        |
| request_counts           | object | 批任务信息                                                   |
| request_counts.total     | int    | 批任务当前完成总数                                           |
| request_counts.completed | int    | 任务当前成功数                                               |
| request_counts.failed    | int    | 任务当前失败数                                               |
| metadata                 | object | 批任务附加信息（用户入参）                                   |

##### status字段说明

| 枚举值      | 描述     |
| ----------- | -------- |
| queuing     | 排队     |
| failed      | 处理失败 |
| in_progress | 在处理   |
| finalizing  | 上传中   |
| completed   | 处理成功 |
| expired     | 超时     |
| canceled    | 已取消   |

### 7. 查询Batch任务

```java
public String getBatch(String batchId) throws IOException 
```

**参数说明**：

|  名称   |  类型  |  描述  | 必须 | 默认值 |
| :-----: | :----: | :----: | ---- | ------ |
| batchId | String | 任务ID | Y    |        |

**响应示例**：见6

### 8. 取消Batch任务

```java
public String cancel(String batchId) throws IOException
```

**参数说明**：

|  名称   |  类型  |  描述  | 必须 | 默认值 |
| :-----: | :----: | :----: | ---- | ------ |
| batchId | String | 任务ID | Y    |        |

**响应示例**：见6

### 9. 查询Batch列表

```java
public String listBatch(int limit, String batchId) throws IOException
```

**参数说明**：

|  名称   |  类型  |   描述   | 必须 | 默认值 |
| :-----: | :----: | :------: | ---- | ------ |
| batchId | String |  任务ID  | Y    |        |
|  limit  |  int   | 查询条数 | Y    |        |

**响应示例**：

```json
{
  "object": "list",
  "data": [
    {
      "id": "batch_abc123",
      "object": "batch",
      "endpoint": "/v1/chat/completions",
      "errors": null,
      "input_file_id": "file-abc123",
      "completion_window": "24h",
      "status": "completed",
      "output_file_id": "file-cvaTdG",
      "error_file_id": "file-HOWS94",
      "created_at": 1711471533,
      "in_progress_at": 1711471538,
      "expires_at": 1711557933,
      "finalizing_at": 1711493133,
      "completed_at": 1711493163,
      "failed_at": null,
      "expired_at": null,
      "cancelling_at": null,
      "cancelled_at": null,
      "request_counts": {
        "total": 100,
        "completed": 95,
        "failed": 5
      },
      "metadata": {
        "customer_id": "user_123456789",
        "batch_description": "Sentiment classification"
      }
    },
    ......
  ],
  "first_id": "batch_abc123",
  "last_id": "batch_abc456",
  "has_more": true
}
```

---

## 注意事项
1. **文件保存时间：** 上传的文件默认保存 48h；接口返回的文件按照文件生成的时间开始计时，默认保存 30 天。

2. **文件要求：**
   格式：只支持 jsonl 文件名后缀
   custom_id：全文件不能重复
   method：只支持 POST
   url：只支持 /v1/chat/completions
   model：支持generalv3.5、4.0Ultra。**generalv3.5指向Spark Max，4.0Ultra指向Spark4.0 Ultra。**

3. **单个文件不超过5万行，文件中的一行对应一个请求，每个请求中body 长度 <= 6KB，body 中 model 全文件保持相同**

4. 文件示例:

   ```json
   {"custom_id": "request-1", "method": "POST", "url": "/v1/chat/completions", "body": {"model": "4.0Ultra", "messages": [{"role": "system", "content": "You are a helpful assistant."},{"role": "user", "content": "1+1=?"}],"max_tokens": 1000}}
   ```

5. 客户端默认超时时间为10秒，可通过Builder调整：

```java
new SparkBatchClient.Builder(APP_ID, API_PASSWORD)
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
}
```
