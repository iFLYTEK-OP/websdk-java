# 简历生成API文档

## 简介

本客户端基于讯飞Spark API实现，提供智能简历生成能力[官方文档](https://www.xfyun.cn/doc/spark/resume.html)，支持以下功能：

- 简历生成

## 功能列表

| 方法名 | 功能说明             |
| ------ | -------------------- |
| send() | 根据用户要求生成简历 |

## 使用准备

1. 前往[能力开通](https://www.xfyun.cn/services/resume)页面
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
    <version>2.1.0</version>
</dependency>
```

2、Java代码

```java
import cn.xfyun.api.ResumeGenClient;
import cn.xfyun.config.PropertiesConfig;

 ResumeGenClient client = new ResumeGenClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        logger.info("请求地址：{}", client.getHostUrl());
        String resp = client.send("我是一名从业5年的java开发程序员, 今年25岁, 邮箱是xxx@qq.com , 电话13000000000, 性别男 , 就业地址合肥, 期望薪资20k , 主要从事AI大模型相关的项目经历");
        logger.info("请求返回结果：{}", resp);

        JSONObject obj = JSONObject.parseObject(resp);
        int code = obj.getJSONObject("header").getIntValue("code");
        if (0 == code) {
            // 结果获取text后解码
            byte[] decodedBytes = Base64.getDecoder().decode(obj.getJSONObject("payload").getJSONObject("resData").getString("text"));
            String decodeRes = new String(decodedBytes, StandardCharsets.UTF_8);
            logger.info("文本解码后的结果：{}", decodeRes);
        } else {
            logger.error("code=>{}，error=>{}", code, obj.getJSONObject("header").getString("message"));
        }
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/ResumeGenClientApp.java)

## 错误码

| 错误码        | 错误描述                                     | 说明                                      | 处理策略                                                     |
| ------------- | -------------------------------------------- | ----------------------------------------- | ------------------------------------------------------------ |
| 10009         | input invalid data                           | 输入数据非法                              | 检查输入数据                                                 |
| 10010         | service license not enough                   | 没有授权许可或授权数已满                  | 提交工单                                                     |
| 10019         | service read buffer timeout, session timeout | session超时                               | 检查是否数据发送完毕但未关闭连接                             |
| 10043         | Syscall AudioCodingDecode error              | 音频解码失败                              | 检查aue参数，如果为speex，请确保音频是speex音频并分段压缩且与帧大小一致 |
| 10114         | session timeout                              | session 超时                              | 会话时间超时，检查是否发送数据时间超过了60s                  |
| 10139         | invalid param                                | 参数错误                                  | 检查参数是否正确                                             |
| 10160         | parse request json error                     | 请求数据格式非法                          | 检查请求数据是否是合法的json                                 |
| 10161         | parse base64 string error                    | base64解码失败                            | 检查发送的数据是否使用base64编码了                           |
| 10163         | param validate error:...                     | 参数校验失败                              | 具体原因见详细的描述                                         |
| 10200         | read data timeout                            | 读取数据超时                              | 检查是否累计10s未发送数据并且未关闭连接                      |
| 10222         | context deadline exceeded                    | 1.上传的数据超过了接口上限；2.SSL证书无效 | 1.检查接口上传的数据（文本、音频、图片等）是否超越了接口的最大限制，可到相应的接口文档查询具体的上限；2.请将log导出发到工单：[提交工单 ](https://console.xfyun.cn/workorder/commit)； |
| 10223         | RemoteLB: can't find valued addr             | lb 找不到节点                             | 提交工单                                                     |
| 10313         | invalid appid                                | appid和apikey不匹配                       | 检查appid是否合法                                            |
| 10317         | invalid version                              | 版本非法                                  | 请到控制台提交工单联系技术人员                               |
| 10700         | not authority                                | 引擎异常                                  | 按照报错原因的描述，对照开发文档检查输入输出，如果仍然无法排除问题，请提供sid以及接口返回的错误信息，到控制台提交工单联系技术人员排查。 |
| 11200         | auth no license                              | 功能未授权                                | 请先检查appid是否正确，并且确保该appid下添加了相关服务。若没问题，则按照如下方法排查。1.确认总调用量是否已超越限制，或者总次数授权已到期，若已超限或者已过期请联系商务人员。2.查看是否使用了未授权的功能，或者授权已过期。 |
| 11201         | auth no enough license                       | 该APPID的每日交互次数超过限制             | 根据自身情况提交应用审核进行服务量提额，或者联系商务购买企业级正式接口，获得海量服务量权限以便商用。 |
| 11503         | server error :atmos return an error data     | 服务内部响应数据错误                      | 提交工单                                                     |
| 11502         | server error: too many datas in resp         | 服务配置错误                              | 提交工单                                                     |
| 100001~100010 | WrapperInitErr                               | 调用引擎时出现错误                        | 请根据message中包含的errno前往 5.2引擎错误码 查看对应的说明及处理策略 |

## 模型参数

| 字段     | 类型   | 是否必传 | 含义         | 限制              | 备注       |
| -------- | ------ | -------- | ------------ | ----------------- | ---------- |
| encoding | string | 否       | 文本编码     | utf8, gb2312, gbk | 默认utf8   |
| compress | string | 否       | 文本压缩格式 | raw, gzip         | 默认值raw  |
| format   | string | 否       | 文本格式     | plain, json, xml  | 默认值json |

## 方法详解

### 1. 简历生成
```java
public String send(String text) throws IOException
```
**参数说明**：

- 参数对象，可设置：

| 名称 |  类型  |             描述             | 必须 | 默认值 |
| :--: | :----: | :--------------------------: | ---- | ------ |
| text | String | 需base64编码，文本大小：0-4M | Y    |        |

**响应示例**：

```json
{
    "header": {
        "code": 0,
        "message": "success",
        "sid": "ase000704fa@dx16ade44e4d87a1c802"
    },
    "payload": {
        "resData": {
            "encoding": "utf8",
            "compress": "raw",
            "format": "json",
            "text": ""
        }
    }
}
```

resData.text

**示例：**

```text
{
    "links": [
        {
            "img_url": null,
            "word_url": null
        }
    ]
}
```

---

## 注意事项
1. 基于用户提示内容生成简历，最小尺寸:1B, 最大尺寸:4194304B，0-4 M。
   
2. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）

3. 客户端默认超时时间为120秒，可通过Builder调整：

```java
new ResumeGenClient
                .Builder(appId, apiKey, apiSecret)
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
A: 生成成功后，响应结果会包含简历下载链接（具体字段参考官方API文档）

---

**更多问题请打开官方文档联系技术支持**