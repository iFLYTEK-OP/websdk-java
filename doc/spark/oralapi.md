# 超拟人合成OpenAPI文档

## 简介

本客户端基于讯飞Spark API实现，提供超拟人的合成能力[官方文档](https://www.xfyun.cn/doc/spark/super%20smart-tts.html)

## 功能列表

| 方法名 | 功能说明                           |
| ------ | ---------------------------------- |
| send() | websocket调用 , 文本合成超拟人语音 |

## 使用准备

1. 前往[超拟人服务](https://www.xfyun.cn/services/smart-tts/)开通能力
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
    <version>2.1.0</version>
</dependency>
```

2、Java代码

```java
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
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
public class OralClientApp {
    private static final Logger logger = LoggerFactory.getLogger(OralClientApp.class);
    private static final String appId = PropertiesConfig.getAppId();
    private static final String apiKey = PropertiesConfig.getApiKey();
    private static final String apiSecret = PropertiesConfig.getApiSecret();
    private static String filePath;
    private static String resourcePath;
    static {
        try {
            filePath = "audio/" + UUID.randomUUID() + ".pcm";
            resourcePath = Objects.requireNonNull(OralClientApp.class.getResource("/")).toURI().getPath();
        } catch (URISyntaxException e) {
            logger.error("获取资源路径失败", e);
        }
    }
    public static void main(String[] args) {
        OralClient oralClient = new OralClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .vcn("x4_lingfeizhe_oral")
                .encoding("raw")
                .sampleRate(16000)
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
                public void onMessage(WebSocket webSocket, String text) {
                    super.onMessage(webSocket, text);
                    OralResponse resp = JSON.fromJson(text, OralResponse.class);
                    if (resp != null) {
                        OralResponse.HeaderBean header = resp.getHeader();
                        OralResponse.PayloadBean payload = resp.getPayload();
                        if (header.getCode() != 0) {
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
            logger.error(e.getMessage(), e);
            logger.error("错误码查询链接：https://www.xfyun.cn/document/error-code");
        }
    }
}
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/OralClientApp.java)

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

## 合成参数

| 功能标识    | 功能描述                                                     | 数据类型 | 取值范围                                           | 必填 | 默认值                  |
| ----------- | ------------------------------------------------------------ | -------- | -------------------------------------------------- | ---- |----------------------|
| oralLevel   | 口语化等级                                                   | string   | 高:high, 中:mid, 低:low                            | 否   | mid                  |
| sparkAssist | 是否通过大模型进行口语化                                     | int      | 开启:1, 关闭:0                                     | 否   | 1                    |
| stopSplit   | 关闭服务端拆句                                               | int      | 不关闭：0，关闭：1                                 | 否   | 0                    |
| remain      | 是否保留原书面语的样子<br />**remain=1, 保留书面语，即移除所有新增填充语、重复语、语气词和话语符号，保留原书面语的样子。**<br/>**remain=0, 不保留书面语，即包含所有新增填充语、重复语、语气词和话语符号，不保留原书面语的样子。** | int      | 保留:1, 不保留:0                                   | 否   | 0                    |
| vcn         | 发音人                                                       | string   |                                                    | 是   | x4_lingxiaoxuan_oral |
| speed       | 语速：0对应默认语速的1/2，100对应默认语速的2倍               | int      |                                                    | 否   | 50                   |
| volume      | 音量：0是静音，1对应默认音量1/2，100对应默认音量的2倍        | int      |                                                    | 否   | 50                   |
| pitch       | 语调：0对应默认语速的1/2，100对应默认语速的2倍               | int      |                                                    | 否   | 50                   |
| bgs         | 背景音                                                       | int      |                                                    | 否   | 0                    |
| reg         | 英文发音方式，0:自动判断处理，如果不确定将按照英文词语拼写处理（缺省）, 1:所有英文按字母发音, 2:自动判断处理，如果不确定将按照字母朗读 | int      |                                                    | 否   | 0                    |
| rdn         | 合成音频数字发音方式，0:自动判断, 1:完全数值, 2:完全字符串, 3:字符串优先 | int      |                                                    | 否   | 0                    |
| rhy         | 是否返回拼音标注，0:不返回拼音, 1:返回拼音（纯文本格式，utf8编码） | int      |                                                    | 否   | 0                    |
| encoding    | 音频编码，可枚举                                             | string   | raw,lame, speex, opus, opus-wb, opus-swb, speex-wb | 否   | lame                 |
| sample_rate | 音频采样率，可枚举                                           | int      | 16000, 8000, 24000                                 | 否   | 24000                |
| channels    | 声道数，可枚举                                               | int      | 1, 2                                               | 否   | 1                    |
| bit_depth   | 单位bit，可枚举                                              | int      | 16, 8                                              | 否   | 16                   |
| frame_size  | 帧大小，默认0                                                | int      | 最小值:0, 最大值:1024                              | 否   | 0                    |
| textFormat  | 文本格式                                                     | string   | plain, json                                        | 否   | plain                |

## 方法详解

### 1. websocket调用方式
```java
public void send(String text, WebSocketListener webSocketListener) throws MalformedURLException, SignatureException {
```
**参数说明**：

|       名称        |  类型  |                             描述                             | 必须 | 默认值 |
| :---------------: | :----: | :----------------------------------------------------------: | ---- | ------ |
|       text        | Array  |  合成文本: base64编码后不超过**8000字节**，约**2000**个字符  | Y    |        |
| webSocketListener | Object | 自定义ws抽象监听类（可使用sdk提供的**AbstractOralWebSocketListener**） | Y    |        |

**响应示例**：

```json
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

---

## 注意事项
1. 合成文本内容，base64编码后不超过8000字节，约2000个字符
2. 返回结果是base64编码 , 需要解码后获取结果
3. text解码后的数据包含[音素信息](https://www.xfyun.cn/doc/spark/super smart-tts.html#响应示例)。注：如果文本中包含英文，英文音素目前不带声调信息；音素时长的单位是5毫秒。
4. 推荐使用 `lame`编解码格式（lame对应mp3格式音频），24000的采样率。

## 错误处理

捕获异常示例：
```java
try {
    String result = client.send(createReq);
} catch (BusinessException e) {
    System.err.println("业务处理异常：" + e.getMessage());
} catch (IOException e) {
    System.err.println("网络请求失败：" + e.getMessage());
} catch (MalformedURLException e) {
    System.err.println("请求路径异常：" + e.getMessage());
} catch (SignatureException e) {
    System.err.println("签名异常：" + e.getMessage());
}
```
