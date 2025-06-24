# 国内通用票证识别sinosecu API文档

## 简介

本客户端基于讯飞开放平台 API实现，提供国内通用票证识别 能力[官方文档](https://www.xfyun.cn/doc/words/invoiceIdentification/API.html)，支持以下功能：

- 通用票证识别

## 功能列表

| 方法名 | 功能说明     |
| ------ | ------------ |
| send() | 通用票证识别 |

## 使用准备

1. 前往[能力开通](https://www.xfyun.cn/services/invoiceIdentification)页面
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
import cn.xfyun.api.PDRecClient;
import cn.xfyun.config.DocumentEnum;
import cn.xfyun.config.ImgFormat;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.document.PDRecParam;
import cn.xfyun.util.FileUtil;

 SinoOCRClient client = new SinoOCRClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        String execute = client.send(FileUtil.fileToBase64(resourcePath + filePath), "jpg");
        logger.info("识别返回结果: {}", execute);
        JSONObject obj = JSON.parseObject(execute);
        String content = obj.getJSONObject("payload").getJSONObject("output_text_result").getString("text");
        byte[] decode = Base64.getDecoder().decode(content);
        String result = new String(decode, StandardCharsets.UTF_8);
        logger.info("base64解码后结果: {}", result);
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/ocr/SinoOCRClientApp.java)

## 错误码列表

**错误码示例:**

```json
{
    "code":10003,  
    "message":"WrapperInitErr;errno=101",       //errno为引擎错误码
    "sid":"ocr00088c7d@dx170194697e9a11d902"
}
```

**平台通用错误码**

| 错误码        | 错误描述                                     | 说明                       | 处理策略                                                     |
| ------------- | -------------------------------------------- | -------------------------- | ------------------------------------------------------------ |
| 10003         | invalid service operation                    | 平台通用错误码             | 提交工单                                                     |
| 10004         | invalid session mode                         | session模式非法            | 提交工单                                                     |
| 10008         | service instance invalid                     | 句柄错误(忽略)             | 提交工单                                                     |
| 10009         | input invalid data                           | 输入数据非法               | 提交工单                                                     |
| 10010         | service license not enough                   | 授权不足                   | 提交工单                                                     |
| 10019         | service read buffer timeout, session timeout | session 超时               | 提交工单                                                     |
| 10043         | Syscall AudioCodingDecode error              | 音频解码失败               | 检查aue参数，如果为speex，请确保音频是speex音频并分段压缩且与帧大小一致 |
| 10101         | engine inavtive                              | 引擎回话已结束（忽略）     | 提交工单                                                     |
| 10114         | session timeout                              | session超时                | 会话时间超时，检查是否发送数据时间超过了60s                  |
| 10118         | server cannot parse response data            | 服务端无法解析后端响应数据 | 提交工单                                                     |
| 10139         | invalid param                                | 参数错误                   | 提交工单                                                     |
| 10160         | parse request json error                     | 请求数据格式非法           | 检查请求数据是否是合法的json                                 |
| 10161         | parse base64 string error                    | base64解码失败             | 检查发送的数据是否使用base64编码了                           |
| 10163         | param validate error:...                     | 参数校验失败               | 具体原因见详细的描述                                         |
| 10200         | read data timeout                            | 读取数据超时               | 检查是否累计10s未发送数据并且未关闭连接                      |
| 10221         | no useful connecton                          | 服务端没有可用连接         | 提交工单                                                     |
| 10222         | Remote LB,cannot find valued lb              | LB找不到有效节点           | 提交工单                                                     |
| 10223         | RemoteLB: can't find valued addr             | lb 找不到节点              | 提交工单                                                     |
| 10225         | Finder: can't find busin service             | 找不到atmos                | 提交工单                                                     |
| 10300         | seqBuffer empty right now                    | 排序缓冲区为空             | 提交工单                                                     |
| 10301         | seq channel already closed                   | 排序channel已关闭          | 提交工单                                                     |
| 10313         | invalid appid                                | appid和apikey不匹配        | 检查appid是否合法                                            |
| 10317         | invalid version                              | 版本非法                   | 提交工单                                                     |
| 10400         | marshal pb message fail                      | pb 协议序列化错误          | 提交工单                                                     |
| 10401         | unmarshal pb message fail                    | pb 协议反序列化错误        | 提交工单                                                     |
| 10500         | fin routine                                  | 内部同步错误               | 提交工单                                                     |
| 10600         | nil event                                    | 事件异常错误               | 提交工单                                                     |
| 10700         | not authority                                | 引擎异常                   | 提交工单                                                     |
| 11200         | auth no license                              | 功能未授权                 | 提交工单                                                     |
| 11201         | auth no enough license                       | 日流控超限                 | 提交工单                                                     |
| 11503         | server error :atmos return an error data     | 服务内部响应数据错误       | 提交工单                                                     |
| 11502         | server error: too many datas in resp         | 服务配置错误               | 提交工单                                                     |
| 100001~100010 | WrapperInitErr                               | 调用引擎时出现错误         | 提交工单                                                     |

## 方法详解

### 1. 国内通用票证识别
```java
public String send(String imgBase64, String imgFormat) throws IOException
```
**参数说明**：

- param参数可设置：

|   名称    |  类型  |                         描述                          | 必须 |
| :-------: | :----: | :---------------------------------------------------: | :--: |
| imgBase64 | String |                     图片的base64                      |  Y   |
| imgFormat | String | 图像编码,可选值：<br/>jpg、jpeg、png、bmp、webp、tiff |  N   |

**响应示例**：

```json
{
	"header": {
		"code": 0,
		"message": "success",
		"sid": "ase000eae20@hu188420b5d7605c2882"
	},
	"payload": {
		"output_text_result": {
			"compress": "raw",
			"encoding": "utf8",
			"format": "plain",
			"seq": "0",
			"status": "3",
			"text": "W3snaW1nT......"
		}
	}
}
```

**返回参数说明:**

| 参数名                              | 类型   | 描述                                                         |
| ----------------------------------- | ------ | ------------------------------------------------------------ |
| header                              | Object | 协议头部，用于描述平台特性的参数，详见平台参数               |
| header.code                         | int    | 错误码，错误码见："5 错误码描述" 必选                        |
| header.message                      | string | 必选                                                         |
| header.sid                          | Object | 可选 ，返回消息中携带的会话句柄                              |
| payload                             | Object | 数据段，用于携带响应的数据                                   |
| payload.output_text_result          | Object | 输出数据                                                     |
| payload.output_text_result.encoding | string | 文本编码，取值范围：utf8、gb2312、gbk                        |
| payload.output_text_result.compress | string | 文本压缩格式，raw、gzip                                      |
| payload.output_text_result.format   | string | 文本格式 ，plain、json、xml                                  |
| payload.output_text_result.text     | string | 文本数据，最小长度:1B，最大长度:1048576B，需base64编码，文本大小：0-1M |

**注意:** 不通增值税发票返回结果不通 , 详情见[在线文档](https://www.xfyun.cn/doc/words/invoiceIdentification/API.html#%E8%BF%94%E5%9B%9E%E7%BB%93%E6%9E%9C)

## 注意事项

1. 图片大小，最小尺寸:0B，最大尺寸:10485760B (10MB) , 编码后不超过4MB。
2. 图片格式支持 jpg、jpeg、png、bmp、webp、tiff
3. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）`SignatureException`（鉴权失败）
4. 结构化识别身份证、结婚证、营业执照、驾驶证、户口本等常用25种证件和15种票据；广泛适用于身份认证、金融开户、征信评估、商户入驻等业务场景。
5. 客户端默认超时时间为10秒，可通过Builder调整：

```java
new TicketOCRClient
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
