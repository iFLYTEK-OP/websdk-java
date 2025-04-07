# 一句话训练API文档

## 接口与鉴权

### 应用申请

> 提供工单开通接口权限


### 实例代码

##### 示例代码

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
package cn.xfyun.demo.spark;

import cn.xfyun.api.VoiceTrainClient;
import cn.xfyun.model.voiceclone.request.AudioAddParam;
import cn.xfyun.model.voiceclone.request.CreateTaskParam;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * （voice-train）一句话训练
 * 1、APPID、APISecret、APIKey信息获取：<a href="https://console.xfyun.cn/services/oneSentence">...</a>
 * 2、文档地址：<a href="https://www.xfyun.cn/doc/spark/reproduction.html">...</a>
 */
public class VoiceTrainClientApp {

    private static final Logger logger = LoggerFactory.getLogger(VoiceTrainClientApp.class);
    private static final String APP_ID = "您的APP_ID";
    private static final String API_KEY = "您的API_KEY";

    public static void main(String[] args) {
        try {
            VoiceTrainClient client = new VoiceTrainClient.Builder(APP_ID, API_KEY).build();
            logger.info("token：{}, 到期时间：{}", client.getToken(), client.getTokenExpiryTime());

            // 获取到训练文本
            String trainTextTree = client.trainText(5001L);
            logger.info("获取到训练文本列表：{}", trainTextTree);

            // 创建任务
            CreateTaskParam createTaskParam = CreateTaskParam.builder()
                    .taskName("2025-03-11测试")
                    .sex(2)
                    .ageGroup(2)
                    .thirdUser("百度翻译")
                    .language("en")
                    .resourceName("百度翻译女发音人")
                    .build();
            String taskResp = client.createTask(createTaskParam);
            JsonObject taskObj = StringUtils.gson.fromJson(taskResp, JsonObject.class);
            String taskId = taskObj.get("data").getAsString();
            logger.info("创建任务：{}，返回taskId：{}", taskResp, taskId);

            // 添加链接音频
            AudioAddParam audioAddParam1 = AudioAddParam.builder()
                    .audioUrl("https开头,wav|mp3|m4a|pcm文件结尾的URL地址")
                    .taskId(taskId)
                    .textId(5001L)
                    .textSegId(1L)
                    .build();
            String audioResp = client.audioAdd(audioAddParam1);
            logger.info("添加链接音频：{}", audioResp);

            // 提交任务
            String submit = client.submit(taskId);
            logger.info("提交任务：{}", submit);

            // 提交文件任务
            AudioAddParam audioAddParam2 = AudioAddParam.builder()
                    .file(new File("wav/mp3/m4a/pcm文件地址"))
                    .taskId(taskId)
                    .textId(5001L)
                    .textSegId(1L)
                    .build();
            String submitWithAudio = client.submitWithAudio(audioAddParam2);
            logger.info("提交任务：{}", submitWithAudio);

            // 任务结果
            String result = client.result(taskId);
            logger.info("任务结果：{}", result);
        } catch (Exception e) {
            logger.error("请求失败", e);
        }
    }
}

```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/VoiceTrainClientApp.java)


### 接口域名

```apl
opentrain.xfyousheng.com、avatar-hci.xfyousheng.com
```

## 错误码

| 参数名称 | 参数描述                         | 解决方式                                               |      |
| -------- | -------------------------------- | ------------------------------------------------------ | ---- |
| 10000    | token过期无效                    | 检查token是否过期，刷新token                           |      |
| 10001    | 缺少请求头参数(X-AppId或X-Token) | 检查头部参数                                           |      |
| 10015    | 训练任务无权操作                 | 任务不属于该应用，无权限操作                           |      |
| 10016    | 无效的appid                      | 联系我方分配appid权限                                  |      |
| 10017    | 未授权该训练类型                 | 联系我方授权该训练类型                                 |      |
| 10018    | 未分配训练路数                   | 联系我方对合作方appid授权训练路数                      |      |
| 10021    | 未分配训练次数                   | 联系我方对合作方appid授权训练次数                      |      |
| 10019    | appid授权已过期                  | 联系我方业务员是否增加授权到期时间                     |      |
| 10020    | 请求ip地址未授权                 | 服务端开启白名单校验时，需要提供ip给我方配置           |      |
| 20001    | 该textId无效或训练文本内容为空   | 检查textId和textSegId是否正确(通过/train/text接口确认) |      |
| 20002    | 该textSegId无效                  | 检查textId和textSegId是否正确(通过/train/text接口确认) |      |
| 60000    | 训练任务不存在                   | 检测taskId是否正确                                     |      |
| 90001    | 请求非法                         | 按照接口协议检查请求结构                               |      |
| 90002    | 请求参数不正确(详细原因)         | 例如：textId must not be blank                         |      |
| 99999    | 系统内部异常                     | 联系我方排查解决                                       |      |

## 接口列表

## 音色训练接口

### [#](https://www.xfyun.cn/doc/spark/reproduction.html#_1、获取鉴权token)1、获取鉴权token

**1.1、接口描述：**
统一鉴权服务，会返回鉴权token和有效期（默认2小时）。
在token有效期内，访问声音复刻的其他接口需要携带此token。（具体参考调用流程图）

**1.2、接口地址：**

```text
http://avatar-hci.xfyousheng.com/aiauth/v1/token
```

**1.3、请求参数**

**请求参数说明**

```text
POST，application/json
```

**请求头：**

| 参数名称      | 类型   | 是否必传 | 参数说明         |
| ------------- | ------ | -------- | ---------------- |
| Content-Type  | string | 是       | application/json |
| Authorization | string | 是       | 签名的md5值。    |

Authorization 通过apikey、timestamp、body字符串格式等md5加密生成：

```text
timeStamp = int(time.time() * 1000)
print(timeStamp)
data = '{"base":{"appid":"' + appId + '","version":"v1","timestamp":"' + str(timeStamp) + '"},"model":"remote"}'
keySign = hashlib.md5((appKey + str(timeStamp)).encode('utf-8')).hexdigest()
sign = hashlib.md5((keySign + data).encode("utf-8")).hexdigest()
headers = {}
headers['Authorization'] = sign
```

**请求体：**

| 参数名称 | 类型      | 是否必传 | 参数说明                 |
| -------- | --------- | -------- | ------------------------ |
| base     | BaseParam | 是       | 基础参数，json格式       |
| model    | string    | 是       | 鉴权模式，可选值：remote |

| 参数名称  | 类型   | 是否必传 | 参数说明                      |
| --------- | ------ | -------- | ----------------------------- |
| appid     | string | 是       | appid，创建应用后从控制台获取 |
| timestamp | string | 是       | 13位时间戳                    |
| version   | string | 是       | 版本号，可选值 v1             |

**1.4、响应参数**

| 参数名称    | 类型   | 是否必传 | 参数说明                 |
| ----------- | ------ | -------- | ------------------------ |
| retcode     | string | 是       | 状态码                   |
| accesstoken | string | 是       | Access Token             |
| expiresin   | string | 是       | 有效期：默认7200，单位秒 |

**1.5、常见错误码**

| 参数名称 | 参数描述                               | 解决方式                         |
| -------- | -------------------------------------- | -------------------------------- |
| 000000   | 成功                                   | 无                               |
| 999999   | 系统内部异常                           | 联系业务方排查                   |
| 000004   | 请求参数缺失                           | 按上述文档要求，检查请求参数格式 |
| 000006   | 请求参数格式异常（时间戳参数为字符串） | 检查请求参数格式                 |
| 000007   | sign校验失败                           | 检查确认token生成格式            |

### [#](https://www.xfyun.cn/doc/spark/reproduction.html#_2、获取训练文本)2、获取训练文本

**2.1、接口描述：**
查询训练文本列表，后续根据选择的文本进行录音
**注意：要求录音音频和文本保持一致，否则会导致音频检测失败。**

**2.2、接口地址：**

```text
http://opentrain.xfyousheng.com/voice_train/task/traintext
```

**2.3、请求参数**

**请求参数说明**

```text
POST，application/json
```

**通用请求头：**

| 参数名称 | 类型   | 是否必需 | 参数说明                                         |
| -------- | ------ | -------- | ------------------------------------------------ |
| X-Sign   | string | true     | MD5(apikey + X-Time + Md5(body))，值为 32 位小写 |
| X-Token  | string | true     | 鉴权token，见上述鉴权接口获取                    |
| X-AppId  | string | true     | 应用id                                           |
| X-Time   | string | true     | Unix 毫秒时间戳                                  |

**请求体：**

| 参数名称 | 类型 | 是否必需 | 参数说明                        |
| -------- | ---- | -------- | ------------------------------- |
| textId   | Long | true     | 可使用通用训练文本(textId=5001) |

**2.4、响应参数**

**通用响应参数**

| 参数名称 | 类型   | 是否必需 | 参数说明         |
| -------- | ------ | -------- | ---------------- |
| code     | int    | true     | 返回码 0表示成功 |
| desc     | string | true     | 返回描述         |
| data     | object | true     | 响应数据         |
| flag     | bool   | true     | true or false    |

**data部分响应参数**

| 参数名称 | 类型      | 是否必需 | 参数说明 |
| -------- | --------- | -------- | -------- |
| textId   | Long      | true     | 文本ID   |
| textName | string    | true     | 文本标题 |
| textSegs | TextSeg[] | true     | 文本列表 |

**TextSeg数据结构**

| 参数名称 | 类型   | 是否必需 | 参数说明               |
| -------- | ------ | -------- | ---------------------- |
| segId    | string | true     | 段落ID，表示第几条文本 |
| segText  | string | true     | 文本内容               |

### [#](https://www.xfyun.cn/doc/spark/reproduction.html#_3、创建训练任务)3、创建训练任务

**3.1、接口描述：**
创建音色训练任务

**3.2、接口地址：**

```text
http://opentrain.xfyousheng.com/voice_train/task/add
```

**3.3、请求参数**

**请求参数说明**

```text
POST，application/json
```

**通用请求头：**

| 参数名称 | 类型   | 是否必需 | 参数说明                                         |
| -------- | ------ | -------- | ------------------------------------------------ |
| X-Sign   | string | true     | MD5(apikey + X-Time + Md5(body))，值为 32 位小写 |
| X-Token  | string | true     | 鉴权token，见上述鉴权接口获取                    |
| X-AppId  | string | true     | 应用id                                           |
| X-Time   | string | true     | Unix 毫秒时间戳                                  |

**请求体：**

| 参数名称     | 类型   | 是否必需 | 参数说明                                                     |
| ------------ | ------ | -------- | ------------------------------------------------------------ |
| taskName     | string | false    | 创建任务名称, 默认””                                         |
| sex          | int    | false    | 性别, 1:男2:女, 默认1                                        |
| ageGroup     | int    | false    | 1:儿童、2:青年、3:中年、4:中老年, 默认1                      |
| resourceType | int    | true     | 12:一句话合成                                                |
| thirdUser    | string | false    | 用户标识, 默认””                                             |
| language     | string | false    | 训练的语种, 默认”” 中文：不传language参数，默认中文 英：en 日：jp 韩：ko 俄：ru |
| resourceName | string | false    | 音库名称, 默认””                                             |
| callbackUrl  | string | false    | 任务结果回调地址，训练结束时进行回调                         |

**3.4、响应参数**

| 参数名称 | 类型   | 是否必需 | 参数说明         |
| -------- | ------ | -------- | ---------------- |
| code     | int    | true     | 返回码 0表示成功 |
| desc     | string | true     | 返回描述         |
| data     | string | true     | 任务id           |
| flag     | bool   | true     | true or false    |

**3.5 训练完成回调信息**

```text
/task/add接口传的callbackUrl，训练结束时进行回调
```

**训练回调参数：**

| 参数名称     | 类型   | 是否必需 | 参数说明                                       |
| ------------ | ------ | -------- | ---------------------------------------------- |
| taskName     | string | false    | 任务名称                                       |
| trainVid     | string | true     | 音库id                                         |
| trainVcn     | string | true     | 训练得到的音色id，后续根据该音色id进行音频合成 |
| resourceType | string | true     | 12：一句话                                     |
| taskId       | string | true     | 任务唯一id                                     |
| trainStatus  | string | true     | -1训练中 0 失败 1成功 2草稿                    |

**响应结果**
状态码为200即认为回调成功

### [#](https://www.xfyun.cn/doc/spark/reproduction.html#_4、向训练任务添加音频-url链接)4、向训练任务添加音频（url链接）

**4.1、接口描述：**
训练任务添加音频（使用音频文件的url地址），调用此接口前需要先调用获取训练文本接口， 根据选择的文本进行录音，要求录音音频和文本保持一致，否则会导致音频检测失败。
**音频要求：**
**1、音频格式限制wav、mp3、m4a、pcm，推荐使用无压缩wav格式**
**2、单通道，采样率24k及以上，位深度16bit，时长无严格限制，音频大小限制3M。**
**录制音频前，建议先查看[一句话复刻录音指导 ](https://www.xfyun.cn/doc/spark/recording.html)。**

**4.2、接口地址：**

```text
http://opentrain.xfyousheng.com/voice_train/audio/v1/add
```

**4.3、请求参数**

**请求参数说明**

```text
POST，application/json
```

**通用请求头：**

| 参数名称 | 类型   | 是否必需 | 参数说明                                         |
| -------- | ------ | -------- | ------------------------------------------------ |
| X-Sign   | string | true     | MD5(apikey + X-Time + Md5(body))，值为 32 位小写 |
| X-Token  | string | true     | 鉴权token，见上述鉴权接口获取                    |
| X-AppId  | string | true     | 应用id                                           |
| X-Time   | string | true     | Unix 毫秒时间戳                                  |

**请求体：**

| 参数名称  | 类型   | 是否必需 | 参数说明                                                     |
| --------- | ------ | -------- | ------------------------------------------------------------ |
| taskId    | string | true     | 训练任务唯一id                                               |
| audioUrl  | string | true     | 文件上传的url地址, 必须是http/https开头，以mp3/wav/m4a/pcm结尾 |
| textId    | Long   | true     | 文本ID, 可使用通用训练文本(textId=5001)                      |
| textSegId | Long   | true     | 训练样例文本段落ID, 例：1, 2, 3 ……                           |

**4.4、响应参数**

| 参数名称 | 类型   | 是否必需 | 参数说明         |
| -------- | ------ | -------- | ---------------- |
| code     | int    | true     | 返回码 0表示成功 |
| desc     | string | true     | 返回描述         |
| data     | null   |          |                  |
| flag     | bool   | true     | true or false    |

### [#](https://www.xfyun.cn/doc/spark/reproduction.html#_5、提交训练任务)5、提交训练任务

**5.1、接口描述：**
音色训练任务提交训练（异步）

**5.2、接口地址：**

```text
http://opentrain.xfyousheng.com/voice_train/task/submit
```

**5.3、请求参数**

**请求参数说明**

```text
POST，application/json
```

**通用请求头：**

| 参数名称 | 类型   | 是否必需 | 参数说明                                         |
| -------- | ------ | -------- | ------------------------------------------------ |
| X-Sign   | string | true     | MD5(apikey + X-Time + Md5(body))，值为 32 位小写 |
| X-Token  | string | true     | 鉴权token，见上述鉴权接口获取                    |
| X-AppId  | string | true     | 应用id                                           |
| X-Time   | string | true     | Unix 毫秒时间戳                                  |

**请求体：**

| 参数名称 | 类型   | 是否必需 | 参数说明   |
| -------- | ------ | -------- | ---------- |
| taskId   | string | true     | 训练任务id |

**5.4、响应参数**

| 参数名称 | 类型   | 是否必需 | 参数说明         |
| -------- | ------ | -------- | ---------------- |
| code     | int    | true     | 返回码 0表示成功 |
| desc     | string | true     | 返回描述         |
| data     | null   |          |                  |
| flag     | bool   | true     | true or false    |

### [#](https://www.xfyun.cn/doc/spark/reproduction.html#_6、向训练任务添加音频-本地文件-并提交训练任务)6、向训练任务添加音频（本地文件）并提交训练任务

**6.1、接口描述：**
训练任务添加音频（使用音频的本地文件），调用此接口前需要先调用获取训练文本接口， 根据选择的文本进行录音，要求录音音频和文本保持一致，否则会导致音频检测失败。
**音频要求：**
**1、音频格式限制wav、mp3、m4a、pcm，推荐使用无压缩wav格式**
**2、单通道，采样率24k及以上，位深度16bit，时长无严格限制，音频大小限制3M。**
**录制音频前，建议先查看[一句话复刻录音指导 ](https://www.xfyun.cn/doc/spark/recording.html)。**

**6.2、接口地址：**

```text
http://opentrain.xfyousheng.com/voice_train/task/submitWithAudio
```

**6.3、请求参数**

**请求参数说明**

```text
POST，application/form-data
```

**请求头：**

| 参数名称     | 类型   | 是否必需 | 参数说明                                         |
| ------------ | ------ | -------- | ------------------------------------------------ |
| Content-Type | string | true     | application/form-data                            |
| X-Sign       | string | true     | MD5(apikey + X-Time + Md5(body))，值为 32 位小写 |
| X-Token      | string | true     | 鉴权token，见上述鉴权接口获取                    |
| X-AppId      | string | true     | 应用id                                           |
| X-Time       | string | true     | Unix 毫秒时间戳                                  |

**请求体：**

| 参数名称  | 类型          | 是否必需 | 参数说明                                |
| --------- | ------------- | -------- | --------------------------------------- |
| file      | MultipartFile | true     | 上传的音频文件                          |
| taskId    | string        | true     | 训练任务唯一id                          |
| textId    | string        | true     | 文本ID, 可使用通用训练文本(textId=5001) |
| textSegId | string        | true     | 训练样例文本段落ID, 例：1, 2, 3 ……      |

**6.4、响应参数**

| 参数名称 | 类型   | 是否必需 | 参数说明         |
| -------- | ------ | -------- | ---------------- |
| code     | int    | true     | 返回码 0表示成功 |
| desc     | string | true     | 返回描述         |
| data     | null   |          |                  |
| flag     | bool   | true     | true or false    |

### [#](https://www.xfyun.cn/doc/spark/reproduction.html#_7、查询训练任务状态)7、查询训练任务状态

**7.1、接口描述：**
根据任务id查询音色训练任务的状态，任务完成后返回训练得到的音色id

**7.2、接口地址：**

```text
http://opentrain.xfyousheng.com/voice_train/task/result
```

**7.3、请求参数**

**请求参数说明**

```text
POST，application/json
```

**通用请求头：**

| 参数名称 | 类型   | 是否必需 | 参数说明                                         |
| -------- | ------ | -------- | ------------------------------------------------ |
| X-Sign   | string | true     | MD5(apikey + X-Time + Md5(body))，值为 32 位小写 |
| X-Token  | string | true     | 鉴权token，见上述鉴权接口获取                    |
| X-AppId  | string | true     | 应用id                                           |
| X-Time   | string | true     | Unix 毫秒时间戳                                  |

**请求体：**

| 参数名称 | 类型   | 是否必需 | 参数说明   |
| -------- | ------ | -------- | ---------- |
| taskId   | string | true     | 训练任务id |

**7.4、响应参数**

| 参数名称 | 类型   | 是否必需 | 参数说明         |
| -------- | ------ | -------- | ---------------- |
| code     | int    | true     | 返回码 0表示成功 |
| desc     | string | true     | 返回描述         |
| data     | null   |          |                  |
| flag     | bool   | true     | true or false    |

**data部分响应参数**

| 参数名称     | 类型   | 是否必需 | 参数说明                                       |
| ------------ | ------ | -------- | ---------------------------------------------- |
| taskName     | string | false    | 任务名称                                       |
| resourceName | string | false    | 音库名称                                       |
| sex          | int    | false    | 音库性别                                       |
| ageGroup     | int    | false    | 音库年龄段                                     |
| trainVid     | string | true     | 音库id                                         |
| assetId      | string | true     | 训练得到的音色id，后续根据该音色id进行音频合成 |
| trainId      | string | true     | 任务唯一id                                     |
| appId        | string | true     | 应用id                                         |
| trainStatus  | int    | true     | -1训练中，1成功，0失败 2草稿                   |
| failedDesc   | string | true     | 训练失败原因                                   |