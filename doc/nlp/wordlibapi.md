# 词库 API文档

## 简介

本客户端基于讯飞开放平台 API实现，提供词库操作能力[官方文档](https://www.xfyun.cn/doc/nlp/TextModeration/API.html)，支持以下功能：

- 创建词库
- 添加词条
- 删除词条
- 查询词条
- 查询所有词库
- 删除词库

## 功能列表

| 方法名           | 功能说明                    |
| ---------------- | --------------------------- |
| createBlackLib() | 创建黑名单词库              |
| createWhiteLib() | 创建白名单词库              |
| addWord()        | 根据lib_id添加黑名单词条    |
| delWord()        | 根据lib_id删除词条          |
| info()           | 根据lib_id查询词条明细      |
| listLib()        | 根据appid查询账户下所有词库 |

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
    <artifactId>websdk-java-spark</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.0.6</version>
</dependency>
```

2、Java代码

```java
import cn.xfyun.api.WordLibClient;
import cn.xfyun.config.CategoryEnum;
import cn.xfyun.config.PropertiesConfig;

      WordLibClient client = new WordLibClient.Builder(APP_ID, API_KEY, API_SECRET).build();

            // 根据appid查询账户下所有词库
            String listLib = client.listLib();
            logger.info("{} 账户下所有词库结果：{}", APP_ID, listLib);

            // 创建白名单
            String whiteLib = client.createWhiteLib("白名单词库1");
            logger.info("创建白名单词库返回结果：{}", whiteLib);
            String whiteLibId = JSON.parseObject(whiteLib).getJSONObject("data").getString("lib_id");
            logger.info("白名单词库id：{}", whiteLibId);

            // 创建黑名单
            String blackLib = client.createBlackLib("黑名单词库1", CategoryEnum.CONTRABAND.getCode());
            logger.info("创建黑名单词库返回结果：{}", blackLib);
            String blackLibId = JSON.parseObject(blackLib).getJSONObject("data").getString("lib_id");
            logger.info("黑名单词库id：{}", blackLibId);

            // 查询词条明细
            String info = client.info(blackLib);
            logger.info("查询词条明细结果：{}", info);

            // 添加词条
            List<String> search = Arrays.asList("傻缺", "蠢才");
            String addWord = client.addWord(blackLib, search);
            logger.info("添加词条结果：{}", addWord);

            // 删除词条
            List<String> delList = Arrays.asList("蠢才");
            String delWord = client.delWord(blackLib, delList);
            logger.info("删除词条结果：{}", delWord);

            // 根据lib_id删除词库
            String deleteLib = client.deleteLib(blackLib);
            logger.info("删除词库结果：{}", deleteLib);
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/nlp/WordLibClientApp.java)

## 

## 方法详解

### 1. 创建黑/白名单词库
```java
public String createBlackLib(String name, String category, String suggestion) throws IOException, SignatureException
    
public String createWhiteLib(String name) throws IOException, SignatureException
```
**参数说明**：

- 参数：

|    名称    |  类型  |         描述         | 必须 | 默认值 |
| :--------: | :----: | :------------------: | ---- | ------ |
|    name    | String |       词库名称       | Y    |        |
|  category  | String |  指定检测的敏感分类  | Y    |        |
| suggestion | String | 处理方式 block：违规 | N    | block  |

**响应示例**：

```json
{
  "code": "000000",
  "desc": "success",
  "data": {
    "lib_id": "c92f643417fc4f0a8d677355667acb55"
  },
  "sid": "5e9da0e81f86423ab85811a108c3480b"
}
```

---

### 2. 根据lib_id添加黑名单词条
```java
public String addWord(String libId, List<String> wordList) throws IOException, SignatureException
```
**参数说明**：

- 参数：

|   名称   |  类型  |                             描述                             | 必须 | 默认值 |
| :------: | :----: | :----------------------------------------------------------: | ---- | ------ |
|  libId   | String |                            词库ID                            | Y    | /      |
| wordList | Array  | 词条列表：<br/>单次添加不能超过 500，总数不能超过 10000；<br/>仅支持中文词条，单个长度最长不超过 20；<br/>不能包含特殊符号，如："[`~!@#$%^*_+\-=<>?,./;':" \t]" | Y    | /      |

**响应示例**：

```text
{
  "code": "000000",
  "desc": "success",
  "sid": "f2460be6414646bc906c0f035a21a200"
}
```

---

### 3. 根据lib_id删除词条
```java
public String delWord(String libId, List<String> wordList) throws IOException, SignatureException
```
**参数说明**：见2

**响应示例**：见2

---

### 4. 根据lib_id查询词条明细
```java
public String info(String libId, boolean returnWord) throws IOException, SignatureException
```
**参数说明**：

- 参数：

|    名称    |  类型   |         描述         | 必须 | 默认值 |
| :--------: | :-----: | :------------------: | ---- | ------ |
|   libId    | String  |        词库ID        | Y    | /      |
| returnWord | boolean | 决定是否返回词条明细 | N    | true   |

**响应示例**：见3

---

### 5. 根据appid查询账户下所有词库
```java
public String listLib() throws IOException, SignatureException
```
**参数说明**：无

**响应示例**：

```json
{
  "code": "000000",
  "desc": "success",
  "data": {
    "list": [
      {
        "lib_id": "317ec840bfa94d5598d0f9f3bdb0400a",
        "name": "黑名单库1",
        "category": "contraband",
        "category_name": "违禁",
        "suggestion": "block",
        "type": 1,
        "scope": 1,
        "enable": true,
        "create_time": "2023-03-13 15:49:33",
        "update_time": "2023-03-13 15:49:33"
      },
      {
        "lib_id": "e1dd4b6338894c2086bcd5a1860cd282",
        "name": "白名单库1",
        "suggestion": "pass",
        "type": 2,
        "scope": 1,
        "enable": true,
        "create_time": "2023-03-13 16:23:49",
        "update_time": "2023-03-13 16:23:49"
      }
    ],
    "total": 2
  },
  "sid": "e5ea2eb2123b436281f0e1a36cd1774a"
}
```



---

### 6. 根据lib_id删除词库
```java
 public String deleteLib(String libId) throws IOException, SignatureException
```
**参数说明**：

- libId:  词库ID

**响应示例**：

```text
{
  "code": "000000",
  "desc": "success",
  "sid": "50cb6167dc1f43179321b5ac2133b384"
}
```

---

## 注意事项
1. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）`SignatureException`（鉴权失败）

2. 客户端默认超时时间为10秒，可通过Builder调整：

```java
new WordLibClient
                    .Builder(APP_ID, API_KEY, API_SECRET)
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
