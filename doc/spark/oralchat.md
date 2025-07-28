# 超拟人交互API文档

## 简介

本客户端基于讯飞开放平台 API实现，提供超拟交互会话能力[官方文档](https://www.xfyun.cn/doc/spark/sparkos_interactive.html)

## 功能列表

| 方法名    | 功能说明           |
| --------- | ------------------ |
| start()   | 超拟人交互启动     |
| stop()    | 超拟人交互停止     |
| sendMsg() | 超拟人交互消息发送 |

## 使用准备

1. 前往[超拟人交互服务](https://www.xfyun.cn/solutions/sparkos_interactive)开通能力
2. 开通后获取以下凭证：
   - appId
   - apiKey
   - apiSecret

## 快速开始

1、添加maven依赖

```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-spark</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.1.5</version>
</dependency>
```

2、Java代码

详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/OralChatClientApp.java)



## 合成参数

| 功能标识      | 功能描述                             | 数据类型  | 取值范围                  | 必填 | 默认值 |
| ------------- | ------------------------------------ | --------- | ------------------------- | ---- | ------ |
| vgap          | 静音断句阈值<br />默认80(800ms)<br/> | int       | 40-1000(400-10000ms)      | 否   | 80     |
| textEncoding  | iat/nlp 结果编码                     | string    | utf8                      | 否   | utf8   |
| textCompress  | iat/nlp 压缩类型                     | string    | raw                       | 否   | raw    |
| textFormat    | iat/nlp 结果格式                     | string    | json                      | 否   | json   |
| encodingIn    | 输入编码格式                         | string    | raw/opus                  | 否   | raw    |
| encodingOut   | 输出音频编码                         | intstring | raw/lame/opus-wb/opus-swb | 否   | raw    |
| sampleRateIn  | 音频采样率 (输入)                    | int       | 8000/16000                | 否   | 16000  |
| sampleRateOut | 音频采样率 (输出)                    | int       | 16000/24000               | 否   | 16000  |
| channelsIn    | 声道数 (输入)                        | int       | 1/2                       | 否   | 1      |
| channelsOut   | 声道数 (输出)                        | int       | 1                         | 否   | 1      |
| bitDepthIn    | 比特位(输入)                         | int       | 8/16                      | 否   | 16     |
| bitDepthOut   | 比特位 (输出)                        | int       | 16                        | 否   | 16     |
| frameSize     | 压缩帧大小<br />传0不压缩            | string    | [0-1024]                  | 否   | 0      |

## 方法详解

### 1. 启动
```java
public BlockingQueue<String> start(OralChatParam param, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException
```
**参数说明**：

|         名称          |  类型  |                             描述                             | 必须 | 默认值              |
| :-------------------: | :----: | :----------------------------------------------------------: | ---- | ------------------- |
|       param.uid       | string |       授权的用户ID, maxLength:64，需保证在appid下唯一        | Y    |                     |
|      param.scene      | string |         情景模式, maxLength:16，从AIUI/飞云平台创建          | N    | sos_app             |
|     param.mscLat      | float  |                             纬度                             | N    |                     |
|     param.mscLng      | float  |                             经度                             | N    |                     |
|     param.prompt      | string |     通过该参数设定大模型回复风格、格式以及其他回答要求等     | N    |                     |
|      param.osSys      | string |                             系统                             | N    |                     |
|   param.newSession    | string | 是否新会话<br />"true"/"global":清空历史<br />"false":保留历史 | N    |                     |
|  param.interactMode   | string | 交互模式<br />continuous(全双工) <br /> continuous_vad(单工) | Y    |                     |
|      param.botId      | string |                          指定bot ID                          | N    |                     |
|       param.vcn       | string | 发音人<br />x5_lingxiaoyue_flow(聆小玥，女性助理) <br />x5_lingfeiyi_flow(聆飞逸，男性助理) | N    | x5_lingxiaoyue_flow |
|      param.speed      |  int   |                         语速 [0,100]                         | N    | 50                  |
|     param.volume      |  int   |                         音量 [0,100]                         | N    | 50                  |
|      param.pitch      |  int   |                         音调 [0,100]                         | N    | 50                  |
|    param.personal     | string |                           人设 id                            | N    |                     |
|      param.resId      | string |                            声纹ID                            | N    |                     |
|    param.resGender    | string |                           声纹性别                           | N    |                     |
|    param.persParam    | string |                           扩展参数                           | N    |                     |
| param.avatar.avatarId | string |                            形象ID                            | N    |                     |
|  param.avatar.image   | string |                           形象照片                           | N    |                     |
| param.avatar.encoding | string |                           照片格式                           | N    |                     |
|  param.avatar.width   |  int   |                              宽                              | N    |                     |
|     param.avatar      |  int   |                              高                              | N    |                     |
|   webSocketListener   | Object |                      自定义ws抽象监听类                      | Y    |                     |

**响应示例**：队列信息



### 2. 停止

```java
public void stop(BlockingQueue<String> queue)
```

**参数说明**：队列信息

**响应示例**：无



### 3. 发送消息

```java
public void sendMessage(BlockingQueue<String> queue, byte[] bytes, int status)
```

**参数说明**：

|  名称  |  类型  |                             描述                             | 必须 | 默认值 |
| :----: | :----: | :----------------------------------------------------------: | ---- | ------ |
| queue  | Object |                       方法1返回的队列                        | Y    |        |
| bytes  |  byte  |         情景模式, maxLength:16，从AIUI/飞云平台创建          | N    |        |
| status |  int   | 0:首帧(表示开启一轮对话)，1:中间帧，2:末帧(表示一轮对话结束) | Y    |        |

**响应示例**：无

---

## 注意事项
1. 注意官方文档单工模式和双工模式的区别, 按需使用 , sdk做了无感知兼容
