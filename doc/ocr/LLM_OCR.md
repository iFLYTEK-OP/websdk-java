# 大模型通用文档识别 API文档

## 简介

本客户端基于讯飞开放平台 API实现，提供通用文档识别能力[官方文档](https://www.xfyun.cn/doc/words/OCRforLLM/API.html)，支持以下功能：

- 大模型通用文档识别

## 功能列表

| 方法名 | 功能说明     |
| ------ | ------------ |
| send() | 通用文档识别 |

## 使用准备

1. 前往[能力开通](https://www.xfyun.cn/services/ocr_model)页面
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
    <version>2.0.9</version>
</dependency>
```

2、Java代码

```java
import cn.xfyun.api.ImageGenClient;
import cn.xfyun.api.LLMOcrClient;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.model.llmocr.LLMOcrParam;
import cn.xfyun.util.FileUtil;


LLMOcrClient client = new LLMOcrClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        logger.info("请求地址：{}", client.getHostUrl());
        LLMOcrParam param = LLMOcrParam.builder()
                .imageBase64(FileUtil.fileToBase64(resourcePath + imagePath))
                .format("jpg")
                .build();
        String resp = client.send(param);
        JSONObject obj = JSON.parseObject(resp);
        if (obj.getJSONObject("header").getInteger("code") != 0) {
            logger.error("请求失败: {}", resp);
            return;
        }

        // 结果获取text后解码
        String base64;
        try {
            base64 = obj.getJSONObject("payload")
                    .getJSONObject("result")
                    .getString("text");
        } catch (Exception e) {
            throw new RuntimeException("返回结果解析失败", e);
        }
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        String decodedStr = new String(decodedBytes, StandardCharsets.UTF_8);
        logger.info("解码后结果: {}", decodedStr);
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/ocr/LLMOcrClientApp.java)

## 平台参数

| 字段                    | 含义                                                         | 类型   | 限制                                                         | 是否必传 |
| ----------------------- | ------------------------------------------------------------ | ------ | ------------------------------------------------------------ | -------- |
| imei                    | 设备imei信息                                                 | string | "maxLength":50                                               | 否       |
| imsi                    | 设备imsi信息                                                 | string | "maxLength":50                                               | 否       |
| mac                     | 设备mac信息                                                  | string | "maxLength":50                                               | 否       |
| netType                 | 网络类型，可选值为wifi、2G、3G、4G、5G                       | string | wifi、2G、3G、4G、5G                                         | 否       |
| netIsp                  | 运营商信息，可选值为CMCC、CUCC、CTCC、other                  | string | CMCC、CUCC、CTCC、other                                      | 否       |
| resId                   | 个性化资源ID                                                 | string | "maxLength":1024                                             | 否       |
| resultOption            | 输出结果级别，以逗号分隔，默认为“normal”，取值范围可枚举     | string | normal:输出OCR识别结果和行坐标, normal,char:输出OCR识别结果和字符单元（CharUnit）结果, normal,no_line_position:输出不带行坐标的OCR识别结果, normal,char,no_line_position:输出不带行坐标的OCR识别结果、字符单元（CharUnit）结果 | 否       |
| result_format           | 输出结果格式,默认为“json”，取值范围可枚举                    | string | json:输出结果为JSON字符串格式, json,markdown:输出结果为json、markdown格式, json,sed:输出结果为json、简单要素文档（sed）格式, json,markdown,sed:输出结果为json、markdown、简单要素文档（sed）格式 | 否       |
| output_type             | 结果输出方式，默认为”one_shot”，取值范围可枚举，当前版本仅支持one_shot | string | one_shot:一次性输出全量结果, streaming_layout:流式输出两次，第一次输出版面信息，第二次输出全量结果 | 否       |
| exif_option             | 是否解析图片exif头，默认为“0”，取值范围可枚举                | string | 0:不解析, 1:解析                                             | 否       |
| json_element_option     | (保留字段暂不支持)默认为空字符串，所有要素均输出；针对每个要素有特殊需求时可使用本参数进行设定 输入格式为: “element_name1=value1,element_name2=value2” | string | 最小长度:0, 最大长度:1000                                    | 否       |
| markdown_element_option | 默认为空字符串，所有要素均输出；针对每个要素有特殊需求时可使用本参数进行设定，输入格式为: “element_name1=value1,element_name2=value2”其中element_name可选值有：seal:印章，information_bar:信息栏，fingerprint:手印，qrcode:二维码，watermark:水印，barcode:条形码，page_header:页眉 ，page_footer:页脚，page_number:页码，layout:版面，title:标题，region:区域，paragraph:段落，textline:文本行，table:表格，graph:插图，list:列表，pseudocode:伪代码，code:代码，footnote:脚注，formula:公式；value值可选值有：0:不输出，1:输出，默认值；说明：当element_name为table时，1表示同时识别有线表和少线表，2表示只识别有线表。 | string | 最小长度:0, 最大长度:1000                                    | 否       |
| sed_element_option      | 默认为空字符串，所有要素均输出；针对每个要素有特殊需求时可使用本参数进行设定，输入格式为:“element_name1=value1,element_name2=value2”其中element_name可选值有：seal:印章，information_bar:信息栏，fingerprint:手印，qrcode:二维码，watermark:水印，barcode:条形码，page_header:页眉，page_footer:页脚，page_number:页码，layout:版面，title:标题，region:区域，paragraph:段落，textline:文本行，table:表格，graph:插图，list:列表，pseudocode:伪代码，code:代码，footnote:脚注，formula:公式；value值可选值有：0:不输出，1:输出，默认值；说明：当element_name为table时，1表示同时识别有线表和少线表，2表示只识别有线表。 | string | 最小长度:0, 最大长度:1000                                    | 否       |
| alpha_option            | 是否解析图片的alpha通道，默认为“0”，取值范围可枚举           | string | 0:不解析图片的alpha通道，即仅读取图片的RGB通道。若alpha通道为透明（即alpha通道的值为0）的区域存在文字，该区域的文字不可见，但识别结果中会出现该区域的文字，即出现识别结果与看到的内容不一致的现象, 1:解析图片的alpha通道，若图片存在alpha通道，将alpha通道为透明（alpha通道的值为0）的像素的RGB值设为白色（#FFFFFF），若alpha通道为透明的区域存在文字，识别结果中不会出现透明区域的文字，即识别结果与看到的内容保持一致。 | 否       |
| rotation_min_angle      | 图像的最小旋转角度阈值，当图像绕中心顺、逆时针旋转的角度绝对值超过该阈值时，对图像进行旋转，取值范围为[0,180]，其中： 0：有旋转角度的图像均进行旋转。 180：所有图像均不进行旋转。 默认值为“5”，即当图像旋转角度的绝对值超过5°时才进行旋转。 | float  | 最小值:0, 最大值:180                                         | 否       |

## 方法详解

### 1. 文档识别
```java
public String send(LLMOcrParam param) throws IOException
```
**参数说明**：

- param参数可设置：

|    名称     |  类型  |                             描述                             | 必须 |
| :---------: | :----: | :----------------------------------------------------------: | :--: |
|     uid     | String |  请求用户服务返回的uid，用户及设备级别个性化功能依赖此参数   |  N   |
|     did     | String |    请求方确保唯一的设备标志，设备级别个性化功能依赖此参数    |  N   |
|  requestId  | String |                   客户端请求的会话唯一标识                   |  N   |
| imageBase64 | String |          图像数据, 最小尺寸:1B, 最大尺寸:10485760B           |  Y   |
|   format    | String | 图像编码jpg:jpg格式, jpeg:jpeg格式, png:png格式, bmp:bmp格式 |  Y   |

**响应协议示例：**

```json
{
    "header": {
        "code": 0,
        "message": "success",
        "sid": "ase000704fa@dx16ade44e4d87a1c802",
        "status": 0
    },
    "payload": {
        "result": {
            "encoding": "utf8",
            "compress": "raw",
            "format": "plain",
            "status": 0,
            "seq": 0,
            "text": ""
        }
    }
}
```

## 注意事项

1. 图像base64编码后数据，最小尺寸:1B，最大尺寸:1048576B (1MB)。
2. 图片格式支持 jpg/jpeg/png/bmp
3. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）
6. 客户端默认超时时间为10秒，可通过Builder调整：

```java
new LLMOcrClient
                .Builder(appId, apiKey, apiSecret)
                .readTimeout(10)
                .build();
```

## 错误处理
捕获异常示例：
```java
        try {
            client.send();
        } catch (IOException e) {
            logger.error("请求失败", e);
        } catch (BusinessException e) {
            logger.error("业务处理失败", e);
        }
```
