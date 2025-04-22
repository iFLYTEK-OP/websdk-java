# HiDream图片生成API文档

## 简介

本客户端基于HiDream AI实现，提供图片生成能力[官方文档](https://www.xfyun.cn/doc/spark/hidream.html)，支持以下功能：

- 文生图片

## 功能列表

| 方法名  | 功能说明         |
| ------- | ---------------- |
| send()  | 图片生成         |
| query() | 图片生成进度查询 |

## 使用准备

1. 前往[能力开通](https://www.xfyun.cn/services/wtop)页面
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
    <version>2.0.5</version>
</dependency>
```

2、Java代码

```java
import cn.xfyun.api.HiDreamClient;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.model.image.HiDreamParam;
import cn.xfyun.util.FileUtil;

HiDreamClient client = new HiDreamClient
                .Builder(appId, apiKey, apiSecret)
                .build();

        logger.info("请求地址：{}", client.getHostUrl());

        HiDreamParam param = HiDreamParam.builder()
                .image(referenceImages)
                .prompt("请将此图片改为孙悟空大闹天空")
                .aspectRatio("1:1")
                .imgCount(1)
                .build();
        String sendResult = client.send(param);
        logger.info("请求返回结果：{}", sendResult);

        // 结果获取taskId
        JSONObject obj = JSON.parseObject(sendResult);
        if (obj.getJSONObject("header").getInteger("code") != 0) {
            logger.error("请求失败：{}", sendResult);
            return;
        }
        String taskId = obj.getJSONObject("header").getString("task_id");
        logger.info("hidream任务id：{}", taskId);

        while (true) {
            // 根据taskId查询任务结果
            String searchResult = client.query(taskId);
            JSONObject queryObj = JSON.parseObject(searchResult);
            String taskStatus = queryObj.getJSONObject("header").getString("task_status");
            if (Objects.equals(taskStatus, "1")) {
                logger.info("文生图任务待处理...");
            }
            if (Objects.equals(taskStatus, "2")) {
                logger.info("文生图任务处理中...");
            }
            if (Objects.equals(taskStatus, "3")) {
                logger.info("文生图任务处理完成：");
                logger.info(searchResult);
                String base64 = queryObj.getJSONObject("payload").getJSONObject("result").getString("text");
                byte[] decodedBytes = Base64.getDecoder().decode(base64);
                String decodedStr = new String(decodedBytes, StandardCharsets.UTF_8);
                logger.info("生成的图片解码后信息：{}", decodedStr);
                // 获取解码后的图片路径(demo只展示生成一张图片的情况)
                JSONArray imageInfo = JSON.parseArray(decodedStr);
                String imageUrl = imageInfo.getJSONObject(0).getString("image_wm");
                logger.info("生成的图片Url：{}", imageUrl);
                // 下载图片
                // FileUtil.downloadFileWithFolder(imageUrl, resourcePath.replaceFirst("/",""), "");
                break;
            }
            if (Objects.equals(taskStatus, "4")) {
                logger.info("文生图任务回调完成：");
                logger.info(searchResult);
                break;
            }
            TimeUnit.MILLISECONDS.sleep(3000);
        }
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/HiDreamClientApp.java)

## 错误码

| 错误码 | 错误信息（英文）                                             | 错误信息（中文）                                         | 解决方法                                         |
| ------ | ------------------------------------------------------------ | -------------------------------------------------------- | ------------------------------------------------ |
| 0      | Success                                                      | 请求成功                                                 | -                                                |
| 1015   | Invalid Image Format, must be jpg/jpeg/png/webp              | 无效的图片格式，必须为jpg/jpeg/png/webp                  | 上传指定格式的图片                               |
| 1020   | Invalid image position                                       | 无效的图片位置                                           | -                                                |
| 1022   | Invalid Aspect Ratio                                         | 无效宽高比                                               | 从文档参数说明中选择合适的宽高比                 |
| 1023   | Invalid Image Count, Must be 1-4                             | 无效的图像数量，必须是1-4                                | 重新指定生成的图片数量                           |
| 1028   | Please try again after changing the prompt                   | 请您更换咒语后再试试吧！                                 | 更换符合法律、道德规范的prompt                   |
| 1029   | The prompt exceeds length limit, please keep it within 1000 characters | 您提供的提示词超过了最大限制，请保持在1000个字符以内     | 减少提示词数量                                   |
| 1030   | The negative prompt exceeds length limit, please keep it within 1000 characters | 您提供的负向提示词超过了最大限制，请保持在1000个字符以内 | 减少负向提示词数量                               |
| 1037   | Missing necessary parameter                                  | 缺少必要参数                                             | 检查遗漏的必要参数（参考相关接口的参数说明）     |
| 1038   | Invalid parameter type                                       | 无效的参数类型                                           | 更换有效类型的参数                               |
| 1039   | Input parameter has None/Null value                          | 输入参数有None/Null值                                    | 提供非空参数                                     |
| 1040   | Required parameter cannot be empty string                    | 必选参数不能为空字符串                                   | 提供非空字符串作为参数                           |
| 1043   | Invalid base64 format                                        | 无效的base64格式                                         | 检查并提供有效的base64格式数据                   |
| 1044   | Video size exceeded, must no more than 100MB                 | 视频大小超出限制，不能大于100MB                          | 减小视频文件的大小，确保不超过100MB              |
| 1050   | Please try again after changing the image                    | 请您更换图片后再试试                                     | 更换图片后重试                                   |
| 1051   | Invalid Resolution                                           | 无效的分辨率                                             | 更换分辨率参数                                   |
| 1052   | Invalid Algo Version                                         | 无效的模型版本                                           | 更换模型版本                                     |
| 1053   | Invalid Upscale Ratio                                        | 无效的增强倍数                                           | 更换增强倍数                                     |
| 1054   | Invalid Language Type                                        | 无效的语言类型                                           | 更换语言类型                                     |
| 1055   | Invalid parameter choice                                     | 无效的参数取值                                           | 更换不支持的参数                                 |
| 2002   | Not support the module                                       | 不支持的模型类型                                         | 选择支持的模型类型                               |
| 2019   | User authentication error                                    | 用户鉴权失败                                             | 检查请求头中的token，并与用户中心的token进行比对 |
| 3020   | Exceeded User Request Limit, Please Try Again Later          | 您在单位时间内的请求数量已超出限制，请稍后再试           | 稍后片刻                                         |
| 3021   | Requests Overload, Please Try Again Later                    | 系统当前使用人数较多，请稍后再试                         | 静待系统恢复                                     |
| 3022   | Exceeded Maximum Parallel Number Limit for Running Tasks, Please Try Again Later | 超出运行中任务的最大并行数量限制，请稍后再试             | 稍候片刻，等待任务完成                           |

## 方法详解

### 1. 图片生成
```java
public String send(HiDreamParam param) throws IOException
```
**参数说明**：

- `param`: 参数对象，可设置：

|      名称      |  类型   |                   描述                    | 必须 | 默认值 |
| :------------: | :-----: | :---------------------------------------: | ---- | ------ |
|     prompt     | String  |   图片生成描述:字符长度0 ~ 2000的字符串   | N    |        |
|  aspectRatio   | String  |            图片比例, 例如 1:1             | N    | 1:1    |
| negativePrompt | String  | 禁止生成的提示词字符:长度0 ~ 2000的字符串 | N    |        |
|    imgCount    | Integer |          一次生成的图片数量[1,4]          | N    | 1      |
|   resolution   | String  |      生成图片的分辨率 (目前仅支持2k)      | N    | 2k     |
|     image      |  Array  |         图片数组, 支持url或base64         | N    |        |

**响应示例**：

```json
{
    "header":{
    	"code":0,	//返回码，0表示成功，其他为异常
        "message":"success",			
        "sid" : "xxxxxxxxxxxxxxxxxxxx",		//请求唯一id
        "task_id" : "xxxxxxxxxxxxxxxxxxx"	// 本次图片生成请求的任务id
    }
    "payload":null
}
```

---

### 2. 图片生成进度查询
```java
public String query(String taskId) throws IOException
```
**参数说明**：

|  名称  |  类型  |  描述  | 必须 |
| :----: | :----: | :----: | ---- |
| taskId | String | 任务ID | Y    |

**响应示例**：

```json
{
    "header": {
    	"code":0,	//返回码，0表示成功，其他为异常
        "message":"success", 
        "task_id": "xxxxxx",
        "task_status": "3"
    },
    "payload": {
        "result": {
            "encoding": "utf8",
            "compress": "raw",
            "format": "json",
            "status": 3,
            "text": ""  //图片信息，需base64解码
        }
    }
}
```

---

## 注意事项
1. 基于用户提示、图片等相关内容生成图片，字数不得超过 **2000**字，图片数组支持url或base64。

2. 支持通过prompt、image、prompt+image的方式，进行图片生成

3. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）

4. 客户端默认超时时间为60秒，可通过Builder调整：

```java
new HiDreamClient
    .Builder(appId, apiKey, apiSecret)
    .readTimeout(60)
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
