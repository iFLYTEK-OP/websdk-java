### 语音评测（普通版）

**示例代码**

```java
import cn.xfyun.api.CommonIseClient;

// 设置评测参数,这里的appid,apiKey是在开放平台控制台获得
 CommonIseClient commonIseClient = new CommonIseClient.Builder()
                .appId(appId).apiKey(apiKey)
 //... 这里可以继续设置评测相关参数，参数见下面表格
                .build();

 // 进行数据处理，处理方法可见demo
 String result = commonIseClient.send(new File(filePath));

```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/CommonIseApp.java)

**评测参数**

  | 参数名   | 类型   | 必传 | 说明                                                         | 示例    |
  | -------- | ------ | ---- | ------------------------------------------------------------ | ------- |
  | aue | string | 是 | 音频编码 <br>raw（未压缩的 pcm 格式音频）<br>speex（标准开源speex） | raw |
  | speex_size | string | 否 | 标准speex解码帧的大小 <br>当aue=speex时，若传此参数，表明音频格式为标准speex | 70 |
  |result_level|string| 否 |评测结果等级<br>entirety（默认值）<br>simple | entirety
  | language | string | 是 | 评测语种<br>en_us（英语）<br>zh_cn（汉语）| zh_cn |
  | category | string | 是 | 评测题型<br>read_syllable（单字朗读，汉语专有）<br>read_word（词语朗读）<br>read_sentence（句子朗读）<br>read_chapter(篇章朗读) | read_sentence |
  | extra_ability | string | 否 | 拓展能力<br>multi_dimension(全维度 ) | multi_dimension |
  
 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/voiceservice/ise/API.html)