# 一句话复刻API文档

## 接口与鉴权

### 应用申请

> 能力开通地址：https://www.xfyun.cn/services/quick_tts


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

import cn.xfyun.api.VoiceCloneClient;
import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.config.VoiceCloneLangEnum;
import cn.xfyun.model.voiceclone.response.VoiceCloneResponse;
import cn.xfyun.service.voiceclone.AbstractVoiceCloneWebSocketListener;
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
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

/**
 * （voice-clone）一句话复刻
 * 1、APPID、APISecret、APIKey信息获取：<a href="https://console.xfyun.cn/services/oneSentence">...</a>
 * 2、文档地址：<a href="https://www.xfyun.cn/doc/spark/reproduction.html">...</a>
 */
public class VoiceCloneClientApp {

    private static final Logger logger = LoggerFactory.getLogger(VoiceCloneClientApp.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getApiKey();
    private static final String apiSecret = PropertiesConfig.getApiSecret();
    private static String filePath;
    private static String resourcePath;

    static {
        try {
            filePath = "audio/voiceclone_" + UUID.randomUUID() + ".mp3";
            resourcePath = Objects.requireNonNull(VoiceCloneClientApp.class.getResource("/")).toURI().getPath();
        } catch (URISyntaxException e) {
            logger.error("获取资源路径失败", e);
        }
    }

    public static void main(String[] args) throws MalformedURLException, SignatureException, UnsupportedEncodingException, FileNotFoundException {
        String text = "欢迎使用本语音合成测试文本，本测试旨在全面检验语音合成系统在准确性、流畅性以及自然度等多方面的性能表现。";
        VoiceCloneClient voiceCloneClient = new VoiceCloneClient.Builder()
                .signature("您的一句话复刻生成的声纹Id", VoiceCloneLangEnum.CN, appId, apiKey, apiSecret)
                .encoding("raw")
                .build();

        File file = new File(resourcePath + filePath);
        try {

            // 开启语音实时播放
            AudioPlayer audioPlayer = new AudioPlayer();
            audioPlayer.start();

            voiceCloneClient.send(text, new AbstractVoiceCloneWebSocketListener(file) {
                @Override
                public void onSuccess(byte[] bytes) {
                    logger.info("success");
                }

                @Override
                public void onFail(WebSocket webSocket, Throwable throwable, Response response) {
                    logger.error(throwable.getMessage());
                    audioPlayer.stop();
                    System.exit(0);
                }

                @Override
                public void onBusinessFail(WebSocket webSocket, VoiceCloneResponse response) {
                    logger.error(response.toString());
                    audioPlayer.stop();
                    System.exit(0);
                }

                @Override
                public void onClose(WebSocket webSocket, int code, String reason) {
                    logger.info("连接关闭，原因：" + reason);
                    audioPlayer.stop();
                    System.exit(0);
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    super.onMessage(webSocket, text);
                    VoiceCloneResponse resp = JSON.fromJson(text, VoiceCloneResponse.class);
                    if (resp != null) {
                        VoiceCloneResponse.PayloadBean payload = resp.getPayload();

                        if (resp.getHeader().getCode() != 0) {
                            onBusinessFail(webSocket, resp);
                        }

                        if (null != payload && null != payload.getAudio()) {
                            String result = payload.getAudio().getAudio();
                            if (result != null) {
                                byte[] audio = Base64.getDecoder().decode(result);
                                audioPlayer.play(audio);
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error("错误码查询链接：https://www.xfyun.cn/document/error-code");
        }
    }
}

```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/VoiceCloneClientApp.java)


### 接口域名

```apl
cn-huabei-1.xf-yun.com
```

## 错误码

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

## 接口列表

### 音频合成接口

**接口描述：**
音色训练成功后，通过训练得到的音色id合成指定文本的音频。

**接口地址：**
**注意：本接口支持合成的语种包括：中、英、日、韩、俄。全链路请求会话时长不超过1分钟**
**原中文合成接口不再对外开放，已接入使用的用户仍可继续使用，建议尽快迁移至新的接口。迁移过程遇到问题，可扫描接口文档上方二维码获取1v1支持服务**

```text
ws(s)://cn-huabei-1.xf-yun.com/v1/private/voice_clone
```

**接口要求**

**接口类型：**
流式 [ws(s)]

**接口鉴权：**
使用签名机制进行鉴权，签名详情参照 “[接口鉴权 ](https://www.xfyun.cn/doc/spark/general_url_authentication.html#_1-鉴权说明)”

**请求参数示例**

```text
{
    "header": {
        "app_id": "123456",
        "status": 2,
        "res_id": ""
    },
    "parameter": {
        "tts": {
            "vcn": "x5_clone",
            "volume": 50,
            "rhy": 0,
            "pybuffer": 1,
            "speed": 50,
            "pitch": 50,
            "bgs": 0,
            "reg": 0,
            "rdn": 0,
            "audio": {
                "encoding": "speex-wb",
                "sample_rate": 16000
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
            "text": ""
        }
    }
}
```

**请求头header部分：**

| 字段   | 含义                                                         | 类型   | 限制             | 是否必传 |
| ------ | ------------------------------------------------------------ | ------ | ---------------- | -------- |
| app_id | 控制台获取您的appid，[控制台](https://console.xfyun.cn/services/oneSentence) | string | "maxLength":50   | 是       |
| res_id | 训练得到的音色id                                             | string | "maxLength":1024 | 是       |
| status | 请求状态，文本只能一次性传输，status必须传2                  | int    | 2                | 是       |

**请求体：**

**parameter.tts部分**

| 功能标识   | 功能描述                                              | 数据类型 | 取值范围                                                     | 必填 | 默认值            |
| ---------- | ----------------------------------------------------- | -------- | ------------------------------------------------------------ | ---- | ----------------- |
| vcn        | 发言人名称                                            | string   | x5_clone                                                     | 是   |                   |
| LanguageID | 合成的语种 注意：需要和训练时指定的语种保持一致       | int      | 中：0 英：1 日：2 韩：3 俄：4                                | 是   | 不传默认为0：中文 |
| volume     | 音量：0是静音，1对应默认音量1/2，100对应默认音量的2倍 | int      | 最小值:0, 最大值:100                                         | 否   | 50                |
| rhy        | 是否返回音素信息/拼音标注                             | int      | 0:不返回拼音, 1:返回拼音（纯文本格式，utf8编码）, 3:支持文本中的标点符号输出（纯文本格式，utf8编码） | 否   | 0                 |
| pybuffer   | 输出音素时长信息                                      | int      | 1:打开                                                       | 否   | 1                 |
| speed      | 语速：0对应默认语速的1/2，100对应默认语速的2倍        | int      | 最小值:0, 最大值:100                                         | 否   | 50                |
| pitch      | 语调：0对应默认语速的1/2，100对应默认语速的2倍        | int      | 最小值:0, 最大值:100                                         | 否   | 50                |
| bgs        | 背景音                                                | int      | 0:无背景音, 1:内置背景音1, 2:内置背景音2                     | 否   | 0                 |
| reg        | 英文发音方式                                          | int      | 0:自动判断处理，如果不确定将按照英文词语拼写处理（缺省）, 1:所有英文按字母发音, 2:自动判断处理，如果不确定将按照字母朗读 | 否   | 0                 |
| rdn        | 合成音频数字发音方式                                  | int      | 0:自动判断, 1:完全数值, 2:完全字符串, 3:字符串优先           | 否   | 0                 |
| audio      | 响应数据控制                                          | Object   | 数据格式预期，用于描述返回结果的编码等相关约束，不同的数据类型，约束维度亦不相同，此 object 与响应结果存在对应关系。 | 是   |                   |

**parameter.tts.audio部分**

| 字段        | 含义     | 数据类型 | 取值范围                             | 默认值   | 说明               | 必填 |
| ----------- | -------- | -------- | ------------------------------------ | -------- | ------------------ | ---- |
| encoding    | 音频编码 | string   | lame, speex, opus, opus-wb, speex-wb | speex-wb | 取值范围可枚举     | 否   |
| sample_rate | 采样率   | int      | 16000, 8000, 24000                   | 16000    | 音频采样率，可枚举 | 否   |

**payload.text部分**

| 字段     | 含义         | 数据类型 | 取值范围                                           | 默认值 | 说明           | 必填 |
| -------- | ------------ | -------- | -------------------------------------------------- | ------ | -------------- | ---- |
| encoding | 文本编码     | string   | utf8, gb2312, gbk                                  | utf8   | 取值范围可枚举 | 否   |
| compress | 文本压缩格式 | string   | raw, gzip                                          | raw    | 取值范围可枚举 | 否   |
| format   | 文本格式     | string   | plain, json, xml                                   | plain  | 取值范围可枚举 | 否   |
| status   | 数据状态     | int      | 2:一次性传完                                       |        | 一次性传完     | 是   |
| seq      | 数据序号     | int      | 最小值:0, 最大值:9999999                           |        | 数据序号       | 是   |
| text     | 文本数据     | string   | 文本内容，base64编码后不超过8000字节，约2000个字符 |        | 需base64编码   | 是   |

**响应参数示例**

```text
{
    "header": {
        "code": 0,
        "message": "success",
        "sid": "ase000704fa@dx16ade44e4d87a1c802",
        "status": 0
    },
    "payload": {
        "audio": {
            "encoding": "speex-wb",
            "sample_rate": 16000,
            "channels": 1,
            "bit_depth": 16,
            "status": 0,
            "seq": 0,
            "audio": "",
            "frame_size": 0
        },
        "pybuf": {
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

pybuf, 当请求参数的 rhy = 1 时text解码后的数据包含音素信息/拼音标注。
注：如果文本中包含英文，英文音素目前不带声调信息；音素时长的单位是5毫秒。

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

## [#](https://www.xfyun.cn/doc/spark/reproduction.html#合成接口-静音停顿、多音字读法)合成接口-静音停顿、多音字读法

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