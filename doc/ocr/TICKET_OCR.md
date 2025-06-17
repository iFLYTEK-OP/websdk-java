# 通用票证识别 API文档

## 简介

本客户端基于讯飞开放平台 API实现，提供通用票证识别 能力[官方文档](https://www.xfyun.cn/doc/words/TicketIdentification/API.html#%E6%8E%A5%E5%8F%A3%E8%AF%B4%E6%98%8E)，支持以下功能：

- 通用票证识别

## 功能列表

| 方法名 | 功能说明     |
| ------ | ------------ |
| send() | 通用票证识别 |

## 使用准备

1. 前往[能力开通](https://www.xfyun.cn/services/Invoice_recognition)页面
2. 创建应用并获取以下凭证：
   - APPID 
   - APIKey
   - APISecret

## 快速开始

1、添加maven依赖

```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-ocr</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.0.8</version>
</dependency>
```

2、Java代码

```java
import cn.xfyun.api.TicketOCRClient;
import cn.xfyun.config.DocumentType;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.model.ticket.TicketOCRParam;
import cn.xfyun.util.FileUtil;

TicketOCRClient client = new TicketOCRClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        DocumentType type = DocumentType.BANK_CARD;
        TicketOCRParam param = TicketOCRParam.builder()
                .documentType(type)
                .imageBase64(FileUtil.fileToBase64(resourcePath + filePath))
                .imageFormat("jpg")
                .build();
        String execute = client.send(param);
        logger.info("{} 识别返回结果: {}", type.getDesc(), execute);
        JSONObject obj = JSON.parseObject(execute);
        String content = obj.getJSONObject("payload").getJSONObject("result").getString("text");
        byte[] decode = Base64.getDecoder().decode(content);
        String result = new String(decode, StandardCharsets.UTF_8);
        logger.info("base64解码后结果: {}", result);
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/ocr/TicketOCRClientApp.java)



## 方法详解

### 1. 通用票证识别
```java
public String send(TicketOCRParam param) throws IOException
```
**参数说明**：

- param参数可设置：

|     名称     |  类型  |                             描述                             | 必须 |
| :----------: | :----: | :----------------------------------------------------------: | :--: |
| documentType | Object |                         票证类型枚举                         |  Y   |
|     uid      | String |                    请求用户服务返回的uid                     |  N   |
| imageBase64  | String |                       图片的base64信息                       |  Y   |
| imageFormat  | String |         图像编码，jpg、 jpeg、 png、bmp、webp、tiff          |  Y   |
|    level     |  int   | 1:输出文本行识别结果及位置，不输出图像信息,  <br />3:仅输出文本行的top1结果，输出文本行及文本行位置,文本行base64到json中 |  N   |

**响应示例**：

```json
{
    "header": {
        "code": "0",
        "message": "success",
        "sid": "ase000704fa@dx16ade44e4d87a1c802",
        "status": 3
    },
    "payload": {
        "result": {
            "encoding": "utf8",
            "compress": "raw",
            "format": "json",
            "text": "AGAjvgDA8D······",
            "status": 3
        }
    }
}
```

**注意:** 详情见[在线文档](https://www.xfyun.cn/doc/words/TicketIdentification/API.html#%E8%BF%94%E5%9B%9E%E7%BB%93%E6%9E%9C)

## 注意事项

1. 图片大小，最小尺寸:0B，最大尺寸:10485760B (10MB) , 编码后不超过4MB。
2. 图片格式支持 jpg、jpeg、png、bmp、webp、tiff
3. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）`SignatureException`（鉴权失败）
4. 客户端默认超时时间为10秒，可通过Builder调整：

```java
new SinoOCRClient
                .Builder(appId, apiKey, apiSecret)
                .readTimeout(10)
                .build();
```

## 错误处理
捕获异常示例：
```java
        try {
            client.send()
        } catch (IOException e) {
            logger.error("请求失败", e);
        } catch (SignatureException e) {
            logger.error("认证失败", e);
        } catch (BusinessException e) {
            logger.error("业务处理失败", e);
        }
```
