### 在线语音合成

**示例代码**

```java
import com.iflytek.api.TtsClient;

// 设置合成参数,这里的appid,apiKey,apiSecret是在开放平台控制台获得
TtsClient ttsClient = new TtsClient.Builder()
                .signature(appId, apiKey, apiSecret)
    			.aue("lame")
    			//...
                .build();

// 可以直接通过file存入本地文件
ttsClient.send("语音合成流式接口将文字信息转化为声音信息", new AbstractTtsWebSocketListener(file) {
    			//返回格式为音频文件的二进制数组bytes
                @Override
                public void onSuccess(byte[] bytes) {
                }
				
    			//授权失败通过throwable.getMessage()获取对应错误信息
                @Override
                public void onFail(WebSocket webSocket, Throwable throwable, Response response) {
                    System.out.println(throwable.getMessage());
                }

    			//业务失败通过ttsResponse获取错误码和错误信息
                @Override
                public void onBusinessFail(WebSocket webSocket, TtsResponse ttsResponse) {
                    System.out.println(ttsResponse.toString());
                }
            });
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/TtsClientApp.java)

**合成参数**

| 参数名 | 类型   | 必传 | 描述                                                         | 示例                                                         |
| ------ | ------ | ---- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| aue    | string | 是   | 音频编码，可选值：<br/>raw：未压缩的pcm<br/>lame：mp3 **(当aue=lame时需传参sfl=1)**<br/>speex-org-wb;7： 标准开源speex（for speex_wideband，即16k）数字代表指定压缩等级（默认等级为8）<br/>speex-org-nb;7： 标准开源speex（for speex_narrowband，即8k）数字代表指定压缩等级（默认等级为8）<br/>speex;7：压缩格式，压缩等级1-10，默认为7（8k讯飞定制speex）<br/>speex-wb;7：压缩格式，压缩等级1-10，默认为7（16k讯飞定制speex） | "raw"<br/>"speex-org-wb;7" 数字代表指定压缩等级（默认等级为8），数字必传<br/>标准开源speex编码以及讯飞定制speex说明请参考[音频格式说明](https://www.xfyun.cn/doc/asr/voicedictation/Audio.html#speex编码) |
| sfl    | int    | 否   | 需要配合aue=lame使用，开启流式返回<br/>mp3格式音频<br/>取值：1 开启 | 1                                                            |
| auf    | string | 否   | 音频采样率，可选值：<br/>audio/L16;rate=8000：合成8K 的音频<br/>audio/L16;rate=16000：合成16K 的音频<br/>auf不传值：合成16K 的音频 | "audio/L16;rate=16000"                                       |
| vcn    | string | 是   | 发音人，可选值：请到控制台添加试用或购买发音人，添加后即显示发音人参数值 | "xiaoyan"                                                    |
| speed  | int    | 否   | 语速，可选值：[0-100]，默认为50                              | 50                                                           |
| volume | int    | 否   | 音量，可选值：[0-100]，默认为50                              | 50                                                           |
| pitch  | int    | 否   | 音高，可选值：[0-100]，默认为50                              | 50                                                           |
| bgs    | int    | 否   | 合成音频的背景音<br/>0:无背景音（默认值）<br/>1:有背景音     | 0                                                            |
| tte    | string | 否   | 文本编码格式<br/>GB2312<br/>GBK<br/>BIG5<br/>UNICODE(小语种必须使用UNICODE编码，合成的文本需使用utf16小端的编码方式<br/>GB18030<br/>UTF8 | "UTF8"                                                       |
| reg    | string | 否   | 设置英文发音方式：<br/>0：自动判断处理，如果不确定将按照英文词语拼写处理（缺省）<br/>1：所有英文按字母发音<br/>2：自动判断处理，如果不确定将按照字母朗读<br/>默认按英文单词发音 | "2"                                                          |
| rdn    | string | 否   | 合成音频数字发音方式<br/>0：自动判断（默认值）<br/>1：完全数值<br/>2：完全字符串<br/>3：字符串优先 | "0"                                                          |

