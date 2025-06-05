# 图片文档还原 API文档

## 简介

本客户端基于讯飞开放平台 API实现，提供图片文档还原能力[官方文档](https://www.xfyun.cn/doc/words/picture-document-reconstruction/API.html)，支持以下功能：

- 图片还原文档

## 功能列表

| 方法名 | 功能说明     |
| ------ | ------------ |
| send() | 图片还原文档 |

## 使用准备

1. 前往[能力开通](https://www.xfyun.cn/services/document_reduction)页面
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

PDRecClient client = new PDRecClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .build();

        try {
            // 构造入参
            PDRecParam param = PDRecParam.builder()
                    .resultType(DocumentEnum.DOC.getCode())
                    .imgBase64(FileUtil.fileToBase64(resourcePath + docImgPath))
                    .imgFormat(ImgFormat.JPG.getDesc())
                    // 设置dstFile参数 , 则会自动保存文件到此位置
                    // .dstFile(new File(resourcePath + dstFilePath))
                    .build();

            // 发送请求
            byte[] send = client.send(param);
            logger.info("生成大小为: {}B的 {}格式文件", send.length, DocumentEnum.getDescByCode(param.getResultType()));
        } catch (IOException e) {
            logger.error("请求失败", e);
        } catch (SignatureException e) {
            logger.error("认证失败", e);
        } catch (BusinessException e) {
            logger.error("业务处理失败", e);
        }
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/ocr/PDRecClientApp.java)

## 平台参数

|   名称   |  类型  |                   描述                    | 必须 |              默认              |
| :------: | :----: | :---------------------------------------: | :--: | :----------------------------: |
| category | String |                   策略                    |  N   | ch_en_public_cloud：中英文识别 |
| encoding | String |   文本编码，可选值：utf8(默认)、 gb2312   |  N   |              utf8              |
| compress | String |  文本压缩格式，可选值：raw(默认)、 gzip   |  N   |              raw               |
|  format  |        | 文本格式，可选值：plain(默认)、json、 xml |  N   |             plain              |

## 方法详解

### 1. 文档还原
```java
public byte[] send(PDRecParam param) throws SignatureException, IOException
```
**参数说明**：

- param参数可设置：

|    名称    |  类型  |                             描述                             | 必须 |
| :--------: | :----: | :----------------------------------------------------------: | :--: |
| imgBase64  | String |                         图片的base64                         |  Y   |
| resultType | String |     结果文件获，可选值：<br/>0:excel<br/>1:doc<br/>2:ppt     |  Y   |
| imgFormat  | String | 图像编码,可选值：<br/>jpg：jpg格式(默认)<br/>jpeg：jpeg格式<br/>png：png格式<br/>bmp：bmp格式 |  Y   |
|  dstFile   |  File  |         需要保存文件的位置 , 不传则不保存 , 取返回值         |  N   |

**响应示例**：字节数组

## 注意事项

1. 图像base64编码后数据，最小尺寸:0B，最大尺寸:10485760B (10MB)。
2. 图片格式支持 jpg/jpeg/png/bmp
3. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）`SignatureException`（鉴权失败）
6. 客户端默认超时时间为10秒，可通过Builder调整：

```java
new PDRecClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
```

## 错误处理
捕获异常示例：
```java
        try {
            client.send();
        } catch (IOException e) {
            logger.error("请求失败", e);
        } catch (SignatureException e) {
            logger.error("认证失败", e);
        } catch (BusinessException e) {
            logger.error("业务处理失败", e);
        }
```
