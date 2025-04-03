# 超拟人合成OpenAPI协议文档


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

import cn.xfyun.api.OralClient;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.model.oral.response.OralResponse;
import cn.xfyun.service.oral.AbstractOralWebSocketListener;
import cn.xfyun.util.AudioPlayer;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.SignatureException;
import java.util.Objects;
import java.util.UUID;

/**
 * 超拟人的接口demo
 *
 * @author zyding6
 */
public class OralClientApp {
    private static final Logger logger = LoggerFactory.getLogger(OralClientApp.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getApiKey();
    private static final String apiSecret = PropertiesConfig.getApiSecret();

    private static String filePath;

    private static String resourcePath;

    static {
        try {
            filePath = "audio/" + UUID.randomUUID() + ".mp3";
            resourcePath = Objects.requireNonNull(OralClientApp.class.getResource("/")).toURI().getPath();
        } catch (URISyntaxException e) {
            logger.error("获取资源路径失败", e);
        }
    }


    public static void main(String[] args) throws MalformedURLException, SignatureException, UnsupportedEncodingException, FileNotFoundException {
        OralClient oralClient = new OralClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .vcn("x4_lingfeizhe_oral")
                .encoding("raw")
                .build();

        // 合成后音频存储路径
        File file = new File(resourcePath + filePath);
        try {

            // 开启语音实时播放
            AudioPlayer audioPlayer = new AudioPlayer();
            audioPlayer.start();

            oralClient.send("我是科大讯飞超拟人, 请问有什么可以帮到您", new AbstractOralWebSocketListener(file) {
                @Override
                public void onSuccess(byte[] bytes) {
                    logger.info("success");
                }

                @Override
                public void onClose(WebSocket webSocket, int code, String reason) {
                    logger.info("关闭连接,code是{},reason:{}", code, reason);
                    audioPlayer.stop();
                    System.exit(0);
                }

                @Override
                public void onFail(WebSocket webSocket, Throwable throwable, Response response) {
                    logger.error(throwable.getMessage());
                    audioPlayer.stop();
                    System.exit(0);
                }

                @Override
                public void onBusinessFail(WebSocket webSocket, OralResponse response) {
                    logger.error(response.toString());
                    audioPlayer.stop();
                    System.exit(0);
                }

                @Override
                public void onPlay(byte[] bytes) {
                    audioPlayer.play(bytes);
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.error("错误码查询链接：https://www.xfyun.cn/document/error-code");
        }
    }
}

```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/OralClientApp.java)


### 接口域名
```apl
cbm01.cn-huabei-1.xf-yun.com
```

## 错误码

| 错误码        | 说明                                         | 处理策略                                                     |
| ------------- | -------------------------------------------- | ------------------------------------------------------------ |
| 10009         | 输入数据非法                                 | 检查输入数据                                                 |
| 10010         | 没有授权许可或授权数已满                     | 提交工单                                                     |
| 10019         | session超时                                  | 检查是否数据发送完毕但未关闭连接                             |
| 10043         | 音频解码失败                                 | 检查aue参数，如果为speex，请确保音频是speex音频并分段压缩且与帧大小一致 |
| 10114         | session 超时                                 | 会话时间超时，检查是否发送数据时间超过了60s                  |
| 10139         | 参数错误                                     | 检查参数是否正确                                             |
| 10160         | 请求数据格式非法                             | 检查请求数据是否是合法的json                                 |
| 10161         | base64解码失败                               | 检查发送的数据是否使用base64编码了                           |
| 10163         | 参数校验失败                                 | 具体原因见详细的描述                                         |
| 10200         | 读取数据超时                                 | 检查是否累计10s未发送数据并且未关闭连接                      |
| 10222         | 1.上传的数据超过了接口上限； 2.SSL证书无效； | 1.检查接口上传的数据（文本、音频、图片等）是否超越了接口的最大限制，可到相应的接口文档查询具体的上限； 2. 请将log导出发到工单：https://console.xfyun.cn/workorder/commit； |
| 10223         | lb 找不到节点                                | 提交工单                                                     |
| 10313         | appid和apikey不匹配                          | 检查appid是否合法                                            |
| 10317         | 版本非法                                     | 请到控制台提交工单联系技术人员                               |
| 10700         | 引擎异常                                     | 按照报错原因的描述，对照开发文档检查输入输出，如果仍然无法排除问题，请提供sid以及接口返回的错误信息，到控制台提交工单联系技术人员排查。 |
| 11200         | 功能未授权                                   | 请先检查appid是否正确，并且确保该appid下添加了相关服务。若没问题，则按照如下方法排查。 1. 确认总调用量是否已超越限制，或者总次数授权已到期，若已超限或者已过期请联系商务人员。 2. 查看是否使用了未授权的功能，或者授权已过期。 |
| 11201         | 该APPID的每日交互次数超过限制                | 根据自身情况提交应用审核进行服务量提额，或者联系商务购买企业级正式接口，获得海量服务量权限以便商用。 |
| 11503         | 服务内部响应数据错误                         | 提交工单                                                     |
| 11502         | 服务配置错误                                 | 提交工单                                                     |
| 100001~100010 | 调用引擎时出现错误                           | 请根据message中包含的errno前往 5.2引擎错误码 查看对应的说明及处理策略 |

## 接口与鉴权

### 请求示例

调用地址

```text
wss://cbm01.cn-huabei-1.xf-yun.com/v1/private/mcd9m97e6
```

> Note:
>
> 1. 鉴权参考: [ws接口鉴权](https://www.xfyun.cn/doc/spark/general_url_authentication.html)

- 请求体示例

```text
{
    "header": {
        "app_id": "123456",
        "status": 2,
    },
    "parameter": {
        "oral": {
            "oral_level":"mid"
        },
        "tts": {
            "vcn": "x4_lingfeiyi_oral",
            "speed": 50,
            "volume": 50,
            "pitch": 50,
            "bgs": 0,
            "reg": 0,
            "rdn": 0,
            "rhy": 0,
            "audio": {
                "encoding": "lame",
                "sample_rate": 24000,
                "channels": 1,
                "bit_depth": 16,
                "frame_size": 0
            }
        }
    },
    "payload": {
        "text": {
            "encoding": "utf8",
            "compress": "raw",
            "format": "plain",
            "status": 2,
            "seq": 0,
            "text": "xxxxxxx"
        }
    }
}
```

**协议结构说明**

| 字段                | 含义       | 类型   | 说明                                          | 是否必传 |
| ------------------- | ---------- | ------ | --------------------------------------------- | -------- |
| header              | 协议头部   | Object | 协议头部，用于描述平台特性的参数              | 是       |
| parameter           | 能力参数   | Object | AI 能力功能参数，用于控制 AI 引擎特性的开关。 | 是       |
| parameter.oral      | 服务别名   | Object | oral能力功能参数                              | 是       |
| parameter.tts       | 服务别名   | Object | tts能力功能参数                               | 是       |
| parameter.tts.audio | 期望输出   | Object | 期望输出                                      | 是       |
| payload             | 输入数据段 | Object | 数据段，携带请求的数据。                      | 是       |
| payload.text        | 输入数据   | Object | 待合成文本                                    | 是       |

**header参数**

| 字段   | 含义                                       | 类型   | 限制           | 是否必传 |
| ------ | ------------------------------------------ | ------ | -------------- | -------- |
| app_id | 在平台申请的app id信息                     | string | "maxLength":50 | 是       |
| status | 请求状态，可选值为：0:开始, 1:中间, 2:结束 | int    | 0,1,2          | 是       |

**口语化配置参数 parameter.oral**

| 功能标识     | 功能描述                 | 数据类型 | 取值范围                | 必填 | 默认值 |
| ------------ | ------------------------ | -------- | ----------------------- | ---- | ------ |
| oral_level   | 口语化等级               | string   | 高:high, 中:mid, 低:low | 否   | mid    |
| spark_assist | 是否通过大模型进行口语化 | int      | 开启:1, 关闭:0          | 否   | 1      |
| stop_split   | 关闭服务端拆句           | int      | 不关闭：0，关闭：1      | 否   | 0      |
| remain       | 是否保留原书面语的样子   | int      | 保留:1, 不保留:0        | 否   | 0      |

**remain=1, 保留书面语，即移除所有新增填充语、重复语、语气词和话语符号，保留原书面语的样子。**
**remain=0, 不保留书面语，即包含所有新增填充语、重复语、语气词和话语符号，不保留原书面语的样子。**

**功能参数 parameter.tts**

| 功能标识 | 功能描述                                                     | 数据类型 | 必填 | 默认值 |
| -------- | ------------------------------------------------------------ | -------- | ---- | ------ |
| vcn      | 发音人                                                       | string   | 是   |        |
| speed    | 语速：0对应默认语速的1/2，100对应默认语速的2倍               | int      | 否   | 50     |
| volume   | 音量：0是静音，1对应默认音量1/2，100对应默认音量的2倍        | int      | 否   | 50     |
| pitch    | 语调：0对应默认语速的1/2，100对应默认语速的2倍               | int      | 否   | 50     |
| bgs      | 背景音                                                       | int      | 否   | 0      |
| reg      | 英文发音方式，0:自动判断处理，如果不确定将按照英文词语拼写处理（缺省）, 1:所有英文按字母发音, 2:自动判断处理，如果不确定将按照字母朗读 | int      | 否   | 0      |
| rdn      | 合成音频数字发音方式，0:自动判断, 1:完全数值, 2:完全字符串, 3:字符串优先 | int      | 否   | 0      |
| rhy      | 是否返回拼音标注，0:不返回拼音, 1:返回拼音（纯文本格式，utf8编码） | int      | 否   | 0      |

**音频格式控制参数 parameter.tts.audio**

| 字段        | 含义     | 数据类型 | 取值范围              | 默认值 | 说明                               | 必填 |
| ----------- | -------- | -------- | --------------------- | ------ | ---------------------------------- | ---- |
| encoding    | 音频编码 | string   | lame, raw             |        | lame: mp3格式音频 raw: pcm格式音频 | 是   |
| sample_rate | 采样率   | int      | 16000, 8000, 24000    | 24000  | 音频采样率，可枚举                 | 否   |
| channels    | 声道数   | int      | 1, 2                  | 1      | 声道数，可枚举                     | 否   |
| bit_depth   | 位深     | int      | 16, 8                 | 16     | 单位bit，可枚举                    | 否   |
| frame_size  | 帧大小   | int      | 最小值:0, 最大值:1024 | 0      | 帧大小，默认0                      | 否   |

> 推荐使用 `lame`编解码格式，24000的采样率。

**请求数据**

| 字段     | 含义         | 数据类型 | 取值范围                         | 默认值 | 说明                                               | 必填 |
| -------- | ------------ | -------- | -------------------------------- | ------ | -------------------------------------------------- | ---- |
| encoding | 文本编码     | string   | utf8                             | utf8   | 必须是 utf8                                        | 是   |
| compress | 文本压缩格式 | string   | raw                              | raw    | 取值范围可枚举                                     | 是   |
| format   | 文本格式     | string   | plain, json                      | plain  | 取值范围可枚举                                     | 是   |
| status   | 数据状态     | int      | **0:开始, 1:中间, 2:结束**       |        | **0,1,2 流式传输** (一次性合成直接传2)             | 是   |
| seq      | 数据序号     | int      | 最小值:0, 最大值:9999999         |        | 数据序号，比如1,2,3,4...                           | 是   |
| text     | 文本数据     | string   | 最小尺寸1字节，最大尺寸:8000字节 |        | 文本内容，base64编码后不超过8000字节，约2000个字符 | 是   |

### [#](https://www.xfyun.cn/doc/spark/super smart-tts.html#响应示例)响应示例

- 响应体

```text
{
    "header": {
        "code": 0,
        "message": "success",
        "sid": "aso000ede92@dx18caf514baab832882",
        "status": 1
    },
    "payload": {
        "audio": {
            "encoding": "lame",
            "sample_rate": 24000,
            "channels": 1,
            "bit_depth": 16,
            "status": 0,
            "seq": 0,
            "frame_size": 0,
            "audio": "xxxxx",
        },
        "pybuf": {
            "encoding": "utf8",
            "compress": "raw",
            "format": "plain",
            "status": 0,
            "seq": 0,
            "text": "xxxxx"
        }
    }
}
```

**返回结构说明**

| 字段    | 含义       | 类型   | 说明                             |
| ------- | ---------- | ------ | -------------------------------- |
| header  | 协议头部   | Object | 协议头部，用于描述平台特性的参数 |
| payload | 响应数据块 | Object | 数据段，携带响应的数据。         |
| audio   | 响应数据块 | Object | 输出数据                         |
| pybuf   | 响应数据块 | Object | 输出数据                         |

**header参数**

| 字段    | 含义                            | 类型   |
| ------- | ------------------------------- | ------ |
| code    | 返回码，0表示成功，其它表示异常 | int    |
| message | 错误描述                        | string |
| sid     | 本次会话的id                    | string |

**payload.audio响应数据参数**

| 字段        | 含义                   | 数据类型 | 取值范围                        | 默认值 | 说明               |
| ----------- | ---------------------- | -------- | ------------------------------- | ------ | ------------------ |
| encoding    | 音频编码               | string   | lame, raw                       | --     | --                 |
| sample_rate | 采样率                 | int      | 16000, 8000, 24000              | 24000  | 音频采样率，可枚举 |
| channels    | 声道数                 | int      | 1, 2                            | 1      | 声道数，可枚举     |
| bit_depth   | 位深                   | int      | 16, 8                           | 16     | 单位bit，可枚举    |
| status      | 数据状态               | int      | 0:开始, 1:中间, 2:结束          |        | 流式传输           |
| seq         | 数据序号               | int      | 最小值:0, 最大值:9999999        | 0      | 标明数据为第几块   |
| audio       | base64编码后的音频数据 | string   | 最小尺寸:0B, 最大尺寸:10485760B |        | 音频大小：0-10M    |
| frame_size  | 帧大小                 | int      | 最小值:0, 最大值:1024           | 0      | 帧大小，默认0      |
| ced         | 合成音频对应的文本进度 | string   | xxx                             |        | ced的单位是字节    |

pybuf, 当 rhy = 1 时返回。

| 字段     | 含义                   | 数据类型 | 取值范围                                  | 默认值 | 说明           |
| -------- | ---------------------- | -------- | ----------------------------------------- | ------ | -------------- |
| encoding | 文本编码               | string   | utf8                                      | utf8   | --             |
| compress | 文本压缩格式           | string   | raw                                       | raw    | --             |
| format   | 文本格式               | string   | plain, json                               | plain  | --             |
| status   | 数据状态               | int      | 0:开始, 1:中间, 2:结束(一次性合成直接传2) |        | 流式传输       |
| seq      | 数据序号               | int      | 最小值:0, 最大值:9999999                  |        | 数据序号       |
| text     | base64编码后的文本数据 | string   | 最小尺寸:0B, 最大尺寸:1048576B            |        | 文本大小：0-1M |

text解码后的数据包含音素信息。注：如果文本中包含英文，英文音素目前不带声调信息；音素时长的单位是5毫秒。

例如：输入的待合成的文本”科大讯飞语音合成系统“，text解码后得到信息如下

```text
sil:6;欢[=huan1]-h1:16;@-uan1:24;迎[=ying2]-ing2:20;使[=shi3]-sh3:24;@-iii3:14;用[=yong4]-iong4:24;科[=ke1]-k1:20;@-e1:14;大[=da4]-d4:12;@-a4:24;讯[=xun4]-x4:22;@-vn4:20;飞[=fei1]-f1:16;@-ei1:22;语[=yu3]-v3:32;音[=yin1]-in1:26;合[=he2]-h2:26;@-e2:18;成[=cheng2]-ch2:18;@-eng2:14;系[=xi4]-x4:20;@-i4:12;统[=tong3]*-t3:20;@-ong3:12;sil:82;
```

| 符号 | 解释                                                         |
| ---- | ------------------------------------------------------------ |
| ;    | 音素分割符，将不同的音素分割开                               |
| :    | 音素时长分割符，后的数字为该音素的帧数（目前1帧代表5ms）。例如"sil:8"表示音素sil的发音时长为8*5=40毫秒 |
| -    | 音节分割符，将音素和该音素对应的音节分割开。例如"欢[=huan1]-h1:16"中‘-’之后的"h1"表示音素 |
| @    | 表示当前音素和前一个是一个文本                               |
| *    | L1韵律的分割符。L1韵律分割符放在音节信息后面。               |
| []   | 音节信息。例如"科"--->[=ke1]是该音素对应的音节和词。         |
| sil  | 表示句首和句末的清音, sil不带声调信息                        |
| sp   | 是句中的清音, sp的声调信息和前一个音素一致                   |

## [#](https://www.xfyun.cn/doc/spark/super smart-tts.html#合成接口-静音停顿、多音字读法)合成接口-静音停顿、多音字读法

**合成时，加入静音停顿**
1、格式： [p] (=无符号整数)
2、参数： * – 静音的时间长度，单位：毫秒(ms)
文本举例：你好[p500]科大讯飞
该句合成时，将会在“你好”后加入500ms的静音

**指定汉字发音**
1、格式： [=] (=拼音/音标)
2、参数： * – 为前一个汉字/单词设定的拼音/音标
3、说明： 汉字：声调用后接一位数字15分别表示阴平、阳平、上声、去声和轻声5个声调。
文本举例：着[=zhuo2]手
其中，“着”字将读作“zhuó”
