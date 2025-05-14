# 一句话训练API文档

## 简介

本客户端基于讯飞Spark API实现，提供一句话复刻能力[官方文档](https://www.xfyun.cn/doc/spark/reproduction.html)，支持以下功能：

- PPT主题列表查询
- 智能PPT生成（直接生成最终PPT）
- 大纲生成（基于用户输入）
- 文档自定义大纲生成（支持PDF/DOC等格式）
- 大纲转PPT生成
- PPT生成进度查询
- 一句话合成

## 功能列表

| 方法名            | 功能说明                                                     |
| ----------------- | ------------------------------------------------------------ |
| refreshToken()    | 刷新token(自动)                                              |
| trainText()       | 获取训练文本                                                 |
| createTask()      | 创建音色训练任务                                             |
| audioAdd()        | 向训练任务添加音频（url链接）                                |
| submit()          | 音色训练任务提交训练(异步)                                   |
| submitWithAudio() | 向训练任务添加音频（本地文件）并提交训练任务                 |
| result()          | 根据任务id查询音色训练任务的状态，任务完成后返回训练得到的音色id |
| send()            | 一句话合成接口                                               |

## 使用准备

1. 前往[能力开通](https://www.xfyun.cn/services/quick_tts)页面
2. 创建应用并获取以下凭证：
   - appId
   - apiKey
   - apiSecret

## 快速开始

### 1、添加maven依赖

```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-spark</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.0.5</version>
</dependency>
```

### 2、一句话训练代码

```java
VoiceTrainClient client = new VoiceTrainClient.Builder(APP_ID, API_KEY).build();
            logger.info("token：{}, 到期时间：{}", client.getToken(), client.getTokenExpiryTime());

            // 获取到训练文本
            String trainTextTree = client.trainText(5001L);
            logger.info("获取到训练文本列表：{}", trainTextTree);

            // 创建任务
            CreateTaskParam createTaskParam = CreateTaskParam.builder()
                    .taskName("task-01")
                    .sex(2)
                    .ageGroup(2)
                    .thirdUser("")
                    .language("cn")
                    .resourceName("中文女发音人")
                    .build();
            String taskResp = client.createTask(createTaskParam);
            JsonObject taskObj = StringUtils.gson.fromJson(taskResp, JsonObject.class);
            String taskId = taskObj.get("data").getAsString();
            logger.info("创建任务：{}，返回taskId：{}", taskResp, taskId);

            // 添加链接音频
            // AudioAddParam audioAddParam1 = AudioAddParam.builder()
            //         .audioUrl("https开头,wav|mp3|m4a|pcm文件结尾的URL地址")
            //         .taskId(taskId)
            //         .textId(5001L)
            //         .textSegId(1L)
            //         .build();
            // String audioResp = client.audioAdd(audioAddParam1);
            // logger.info("添加链接音频：{}", audioResp);

            // 提交任务
            // String submit = client.submit(taskId);
            // logger.info("提交任务：{}", submit);

            // 提交文件任务(不需要单独调用submit接口)
            AudioAddParam audioAddParam2 = AudioAddParam.builder()
                    .file(new File(resourcePath + filePath))
                    .taskId(taskId)
                    .textId(5001L)
                    .textSegId(1L)
                    .build();
            String submitWithAudio = client.submitWithAudio(audioAddParam2);
            logger.info("提交任务：{}", submitWithAudio);

            while (true) {
                // 根据taskId查询任务结果
                String result = client.result(taskId);
                JSONObject queryObj = JSON.parseObject(result);
                Integer taskStatus = queryObj.getJSONObject("data").getInteger("trainStatus");
                if (Objects.equals(taskStatus, -1)) {
                    logger.info("一句话复刻训练中...");
                }
                if (Objects.equals(taskStatus, 0)) {
                    String message = queryObj.getJSONObject("data").getString("failedDesc");
                    logger.info("一句话复刻训练失败: {}", message);
                    break;
                }
                if (Objects.equals(taskStatus, 2)) {
                    logger.info("一句话复刻训练任务未提交: {}", result);
                    break;
                }
                if (Objects.equals(taskStatus, 1)) {
                    String string = queryObj.getJSONObject("data").getString("assetId");
                    logger.info("一句话复刻训练完成, 声纹ID: {}", string);
                    break;
                }
                TimeUnit.MILLISECONDS.sleep(3000);
            }
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/VoiceTrainClientApp.java)

### 3、一句话合成代码

详情请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/VoiceCloneClientApp.java)

## 错误码

### 1.1 一句话训练

| 参数名称 | 参数描述                         | 解决方式                                               |
| -------- | -------------------------------- | ------------------------------------------------------ |
| 10000    | token过期无效                    | 检查token是否过期，刷新token                           |
| 10001    | 缺少请求头参数(X-AppId或X-Token) | 检查头部参数                                           |
| 10015    | 训练任务无权操作                 | 任务不属于该应用，无权限操作                           |
| 10016    | 无效的appid                      | 联系我方分配appid权限                                  |
| 10017    | 未授权该训练类型                 | 联系我方授权该训练类型                                 |
| 10018    | 未分配训练路数                   | 联系我方对合作方appid授权训练路数                      |
| 10021    | 未分配训练次数                   | 联系我方对合作方appid授权训练次数                      |
| 10019    | appid授权已过期                  | 联系我方业务员是否增加授权到期时间                     |
| 10020    | 请求ip地址未授权                 | 服务端开启白名单校验时，需要提供ip给我方配置           |
| 20001    | 该textId无效或训练文本内容为空   | 检查textId和textSegId是否正确(通过/train/text接口确认) |
| 20002    | 该textSegId无效                  | 检查textId和textSegId是否正确(通过/train/text接口确认) |
| 60000    | 训练任务不存在                   | 检测taskId是否正确                                     |
| 90001    | 请求非法                         | 按照接口协议检查请求结构                               |
| 90002    | 请求参数不正确(详细原因)         | 例如：textId must not be blank                         |
| 99999    | 系统内部异常                     | 联系我方排查解决                                       |

### 1.2 一句话合成

| 错误码        | 错误描述                                     | 说明                                         | 处理策略                                                     |
| ------------- | -------------------------------------------- | -------------------------------------------- | ------------------------------------------------------------ |
| 10009         | input invalid data                           | 输入数据非法                                 | 检查输入数据                                                 |
| 10010         | service license not enough                   | 没有授权许可或授权数已满                     | 提交工单                                                     |
| 10019         | service read buffer timeout, session timeout | session超时                                  | 检查是否数据发送完毕但未关闭连接                             |
| 10043         | Syscall AudioCodingDecode error              | 音频解码失败                                 | 检查aue参数，如果为speex，请确保音频是speex音频并分段压缩且与帧大小一致 |
| 10114         | session timeout                              | session 超时                                 | 会话时间超时，检查是否发送数据时间超过了60s                  |
| 10139         | invalid param                                | 参数错误                                     | 检查参数是否正确                                             |
| 10160         | parse request json error                     | 请求数据格式非法                             | 检查请求数据是否是合法的json                                 |
| 10161         | parse base64 string error                    | base64解码失败                               | 检查发送的数据是否使用base64编码了                           |
| 10163         | param validate error:...                     | 参数校验失败                                 | 具体原因见详细的描述                                         |
| 10200         | read data timeout                            | 读取数据超时                                 | 检查是否累计10s未发送数据并且未关闭连接                      |
| 10222         | context deadline exceeded                    | 1.上传的数据超过了接口上限； 2.SSL证书无效； | 1.检查接口上传的数据（文本、音频、图片等）是否超越了接口的最大限制，可到相应的接口文档查询具体的上限； 2. 请将log导出发到工单：https://console.xfyun.cn/workorder/commit； |
| 10223         | RemoteLB: can't find valued addr             | lb 找不到节点                                | 提交工单                                                     |
| 10313         | invalid appid                                | appid和apikey不匹配                          | 检查appid是否合法                                            |
| 10317         | invalid version                              | 版本非法                                     | 请到控制台提交工单联系技术人员                               |
| 10700         | not authority                                | 引擎异常                                     | 按照报错原因的描述，对照开发文档检查输入输出，如果仍然无法排除问题，请提供sid以及接口返回的错误信息，到控制台提交工单联系技术人员排查。 |
| 11200         | auth no license                              | 功能未授权                                   | 请先检查appid是否正确，并且确保该appid下添加了相关服务。若没问题，则按照如下方法排查。 1. 确认总调用量是否已超越限制，或者总次数授权已到期，若已超限或者已过期请联系商务人员。 2. 查看是否使用了未授权的功能，或者授权已过期。 |
| 11201         | auth no enough license                       | 该APPID的每日交互次数超过限制                | 根据自身情况提交应用审核进行服务量提额，或者联系商务购买企业级正式接口，获得海量服务量权限以便商用。 |
| 11503         | server error :atmos return an error data     | 服务内部响应数据错误                         | 提交工单                                                     |
| 11502         | server error: too many datas in resp         | 服务配置错误                                 | 提交工单                                                     |
| 100001~100010 | WrapperInitErr                               | 调用引擎时出现错误                           | 请根据message中包含的errno前往 5.2引擎错误码 查看对应的说明及处理策略 |

## 合成参数

| 字段         | 类型                         | 是否必传 | 含义                                                | 限制 | 备注       |
| ------------ | ---------------------------- | -------- | --------------------------------------------------- | ---- |----------|
| textEncoding | 文本编码                     | String   | utf8, gb2312, gbk                                   | 否   | utf8     |
| textCompress | 文本压缩格式                 | String   | raw, gzip                                           | 否   | raw      |
| textFormat   | 文本格式                     | String   | plain, json, xml                                    | 否   | plain    |
| resId        | 训练得到的音色id             | String   | -                                                   | 是   | -        |
| languageId   | 合成的语种（需与训练时一致） | int      | 0(中),1(英),2(日),3(韩),4(俄)                       | 否   | 0        |
| speed        | 语速                         | int      | [0,100] (0=默认1/2,100=默认2倍)                     | 否   | -        |
| volume       | 音量                         | int      | [0,100] (0=默认1/2,100=默认2倍)                     | 否   | -        |
| pitch        | 语调                         | int      | [0,100] (0=默认1/2,100=默认2倍)                     | 否   | -        |
| bgs          | 背景音                       | int      | -                                                   | 否   | 0        |
| reg          | 英文发音方式                 | int      | 0(自动判断),1(字母发音),2(自动判断优先字母)         | 否   | 0        |
| rdn          | 数字发音方式                 | int      | 0(自动判断),1(完全数值),2(完全字符串),3(字符串优先) | 否   | 0        |
| rhy          | 是否返回拼音标注             | int      | 0(不返回),1(返回拼音),3(支持标点符号输出)           | 否   | 0        |
| encoding     | 音频编码                     | String   | lame, speex, opus, opus-wb, speex-wb                | 否   | lame     |
| sampleRate   | 音频采样率                   | int      | 16000, 8000, 24000                                  | 否   | 24000    |
| vcn          | 发言人名称                   | String   | 固定值x5_clone                                      | 是   | x5_clone |
| status       | 数据状态                     | int      | 固定值2(一次性传完)                                 | 是   | 2        |

## 方法详解

### 1. 刷新token
```java
public String refreshToken()
```
**参数说明**：

 无

**响应参数**：

| 参数名称    | 类型   | 是否必传 | 参数说明                 |
| ----------- | ------ | -------- | ------------------------ |
| retcode     | string | 是       | 状态码                   |
| accesstoken | string | 是       | Access Token             |
| expiresin   | string | 是       | 有效期：默认7200，单位秒 |

---

### 2. 获取训练文本
```java
public String trainText(Long textId) throws Exception
```
**参数说明**：

| 参数名称 | 类型 | 是否必需 | 参数说明                        |
| -------- | ---- | -------- | ------------------------------- |
| textId   | Long | true     | 可使用通用训练文本(textId=5001) |

**响应参数**：

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

---

### 3. 创建训练任务
```java
public String createTask(CreateTaskParam param) throws Exception
```
**请求体：**

| 参数名称      | 类型   | 是否必需 | 参数说明                                                     |
| ------------- | ------ | -------- | ------------------------------------------------------------ |
| taskName      | string | false    | 创建任务名称, 默认””                                         |
| sex           | int    | false    | 性别, 1:男2:女, 默认1                                        |
| ageGroup      | int    | false    | 1:儿童、2:青年、3:中年、4:中老年, 默认1                      |
| resourceType  | int    | true     | 12:一句话合成                                                |
| thirdUser     | string | false    | 用户标识, 默认””                                             |
| language      | string | false    | 训练的语种, 默认”” 中文：不传language参数，默认中文 英：en 日：jp 韩：ko 俄：ru |
| resourceName  | string | false    | 音库名称, 默认””                                             |
| callbackUrl   | string | false    | 任务结果回调地址，训练结束时进行回调                         |
| denoiseSwitch | int    | false    | 降噪开关, 默认0<br/>0: 关闭降噪 1:开启降噪                   |
| mosRatio      | float  | false    | 范围0.0～5.0，单位0.1，默认0.0<br/>大于0，则开启音频检测。该值为对应的检测阈值,音频得分高于该值时将会生成音频特征 |

**响应体**

| 参数名称 | 类型   | 是否必需 | 参数说明         |
| -------- | ------ | -------- | ---------------- |
| code     | int    | true     | 返回码 0表示成功 |
| desc     | string | true     | 返回描述         |
| data     | string | true     | 任务id           |
| flag     | bool   | true     | true or false    |

---

### 4. 向训练任务添加音频（url链接）
```java
public String audioAdd(AudioAddParam param) throws Exception
```
**请求体：**

| 参数名称  | 类型   | 是否必需 | 参数说明                                                     |
| --------- | ------ | -------- | ------------------------------------------------------------ |
| taskId    | string | true     | 训练任务唯一id                                               |
| audioUrl  | string | true     | 文件上传的url地址, 必须是http/https开头，以mp3/wav/m4a/pcm结尾 |
| textId    | Long   | true     | 文本ID, 可使用通用训练文本(textId=5001)                      |
| textSegId | Long   | true     | 训练样例文本段落ID, 例：1, 2, 3 ……                           |

**响应体**

| 参数名称 | 类型   | 是否必需 | 参数说明         |
| -------- | ------ | -------- | ---------------- |
| code     | int    | true     | 返回码 0表示成功 |
| desc     | string | true     | 返回描述         |
| data     | null   |          |                  |
| flag     | bool   | true     | true or false    |

---

### 5. 音色训练任务提交训练(异步)
```java
public String submit(String taskId) throws Exception
```
**请求体：**

| 参数名称 | 类型   | 是否必需 | 参数说明   |
| -------- | ------ | -------- | ---------- |
| taskId   | string | true     | 训练任务id |

**响应体**

| 参数名称 | 类型   | 是否必需 | 参数说明         |
| -------- | ------ | -------- | ---------------- |
| code     | int    | true     | 返回码 0表示成功 |
| desc     | string | true     | 返回描述         |
| data     | null   |          |                  |
| flag     | bool   | true     | true or false    |

---

### 6. 向训练任务添加音频（本地文件）并提交训练任务
```java
public String submitWithAudio(AudioAddParam param) throws Exception
```
**请求体：**

| 参数名称      | 类型          | 是否必需 | 参数说明                                                     |
| ------------- | ------------- | -------- | ------------------------------------------------------------ |
| file          | MultipartFile | true     | 上传的音频文件                                               |
| taskId        | string        | true     | 训练任务唯一id                                               |
| textId        | string        | true     | 文本ID, 可使用通用训练文本(textId=5001)                      |
| textSegId     | string        | true     | 训练样例文本段落ID, 例：1, 2, 3 ……                           |
| denoiseSwitch | int           | false    | 降噪开关, 默认0<br/>0: 关闭降噪 1:开启降噪                   |
| mosRatio      | float         | false    | 范围0.0～5.0，单位0.1，默认0.0<br/>大于0，则开启音频检测。该值为对应的检测阈值,音频得分高于该值时将会生成音频特征 |

**响应体**

| 参数名称 | 类型   | 是否必需 | 参数说明         |
| -------- | ------ | -------- | ---------------- |
| code     | int    | true     | 返回码 0表示成功 |
| desc     | string | true     | 返回描述         |
| data     | null   |          |                  |
| flag     | bool   | true     | true or false    |

---

### 7. 根据任务id查询音色训练任务的状态

```java
public String result(String taskId) throws Exception
```

**请求体：**

| 参数名称 | 类型   | 是否必需 | 参数说明   |
| -------- | ------ | -------- | ---------- |
| taskId   | string | true     | 训练任务id |

**响应体**

| 参数名称 | 类型   | 是否必需 | 参数说明         |
| -------- | ------ | -------- | ---------------- |
| code     | int    | true     | 返回码 0表示成功 |
| desc     | string | true     | 返回描述         |
| data     | null   |          |                  |
| flag     | bool   | true     | true or false    |

### 8. 一句话合成

```java
public void send(String text, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
```

**请求体：**

| 参数名称 | 类型   | 是否必需 | 参数说明                                           |
| -------- | ------ | -------- | -------------------------------------------------- |
| text     | string | true     | 文本内容，base64编码后不超过8000字节，约2000个字符 |

**响应体：**

**响应头header部分：**

| 字段    | 含义                                 | 类型   | 是否必选 |
| ------- | ------------------------------------ | ------ | -------- |
| code    | 返回码，0表示成功，其它表示异常      | int    | 是       |
| message | 错误描述                             | string | 是       |
| sid     | 本次会话的id                         | string | 是       |
| status  | 响应数据状态，0:开始, 1:中间, 2:结束 | int    | 是       |

**payload.audio部分**

| 字段        | 含义                   | 数据类型 | 取值范围                             |
| ----------- | ---------------------- | -------- | ------------------------------------ |
| encoding    | 音频编码               | string   | lame, speex, opus, opus-wb, speex-wb |
| sample_rate | 采样率                 | int      | 16000, 8000, 24000                   |
| channels    | 声道数                 | int      | 1, 2                                 |
| bit_depth   | 位深                   | int      | 16, 8                                |
| status      | 数据状态               | int      | 0:开始, 1:开始, 2:结束               |
| seq         | 数据序号               | int      | 最小值:0, 最大值:9999999             |
| audio       | base64编码后的音频数据 | string   |                                      |
| frame_size  | 帧大小                 | int      | 最小值:0, 最大值:1024                |

**payload.pybuf部分**

| 字段     | 含义         | 数据类型 | 取值范围                                           |
| -------- | ------------ | -------- | -------------------------------------------------- |
| encoding | 文本编码     | string   | utf8, gb2312, gbk                                  |
| compress | 文本压缩格式 | string   | raw, gzip                                          |
| format   | 文本格式     | string   | plain, json, xml                                   |
| status   | 数据状态     | int      | 0:开始, 1:开始, 2:结束                             |
| seq      | 数据序号     | int      | 最小值:0, 最大值:9999999                           |
| text     | 文本数据     | string   | 文本内容，base64编码后不超过8000字节，约2000个字符 |

---



## 注意事项

1. 支持合成的语种包括：中、英、日、韩、俄。全链路请求会话时长不超过1分钟

2. 合成返回结果为base64 , 需要解码后使用

3. token会自动刷新 , 异常情况下需要手动刷新

4. 训练接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）

5. 客户端默认超时时间为10秒，可通过Builder调整：

```java
new Builder(appId, apiSecret)
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
