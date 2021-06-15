### 语音评测

**示例代码**

```java
import cn.xfyun.api.IseClient;

// 设置评测参数,这里的appid,apiKey,apiSecret是在开放平台控制台获得
IseClient iseClient = new IseClient.Builder()
                .signature(appId, apiKey, apiSecret)
                //... 这里可以继续设置评测相关参数，参数见下面表格
                .build();


iseClient.send(file, new AbstractIseWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IseResponseData iseResponseData) {
              // 根据服务端成功响应，进行数据处理，处理方法可见demo
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
              // 根据业务需求，对失败时的数据进行处理
            }
        });

```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/IseClientApp.java)

**评测参数**

  | 参数名   | 类型   | 必传 | 描述                                                         | 示例    |
  | -------- | ------ | ---- | ------------------------------------------------------------ | ------- |
  | sub | string | 是 | 服务类型指定<br>ise(开放评测) | "ise" |
  | ent   | string | 是 | 中文：cn_vip<br>英文：en_vip | "cn_vip" |
  |category|string|是|中文题型：<br>read_syllable（单字朗读，汉语专有）<br>read_word（词语朗读）<br>read_sentence（句子朗读）<br>read_chapter(篇章朗读)<br>英文题型：<br>read_word（词语朗读）<br>read_sentence（句子朗读）<br>read_chapter(篇章朗读)<br>simple_expression（英文情景反应）<br>read_choice（英文选择题）<br>topic（英文自由题）<br>retell（英文复述题）<br>picture_talk（英文看图说话）<br>oral_translation（英文口头翻译）|"read_sentence"
  | aus | int | 是   | 上传音频时来区分音频的状态（在cmd=auw即音频上传阶段为必传参数）<br>1：第一帧音频<br>2：中间的音频<br>4：最后一帧音频 | 根据上传阶段取值 |
  | cmd | string | 是 | 用于区分数据上传阶段<br>ssb：参数上传阶段<br>ttp：文本上传阶段（ttp_skip=true时该阶段可以跳过，直接使用text字段中的文本）<br>auw：音频上传阶段 | 根据上传阶段取值 |
  | text | string | 是 | 待评测文本 utf8 编码，需要加utf8bom 头 | '\uFEFF'+text |
  | tte | string | 是 | 待评测文本编码<br>utf-8<br>gbk | "utf-8" |
  | ttp_skip | bool | 是 | 跳过ttp直接使用ssb中的文本进行评测（使用时结合cmd参数查看）,默认值true | true |
  | extra_ability | string | 否 | 拓展能力（生效条件ise_unite="1", rst="entirety"）多维度分信息显示（准确度分、流畅度分、完整度打分）extra_ability值为multi_dimension（字词句篇均适用,如选多个能力，用分号；隔开。例如：add("extra_ability"," syll_phone_err_msg;pitch;multi_dimension")）单词基频信息显示（基频开始值、结束值）extra_ability值为pitch ，仅适用于单词和句子题型音素错误信息显示（声韵、调型是否正确）extra_ability值为syll_phone_err_msg（字词句篇均适用,如选多个能力，用分号；隔开。例如：add("extra_ability"," syll_phone_err_msg;pitch;multi_dimension")） |"multi_dimension" |
  | aue | string | 否 | 音频格式<br>raw: 未压缩的pcm格式音频或wav（如果用wav格式音频，建议去掉头部）<br>lame: mp3格式音频<br>speex-wb;7: 讯飞定制speex格式音频(默认值)| "raw" |
  | auf | string | 否 | 音频采样率<br>默认 audio/L16;rate=16000 | "audio L16；rate=16000" |
  | rstcd | string | 否 | 返回结果格式<br>utf8<br>gbk （默认值） | "utf8" |
  | group | string | 否 | 针对群体不同，相同试卷音频评分结果不同 （仅中文字、词、句、篇章题型支持），此参数会影响准确度得分<br>adult（成人群体，不设置群体参数时默认为成人）<br>youth（中学群体<br>pupil（小学群体，中文句、篇题型设置此参数值会有accuracy_score得分的返回）） | "adult" |
  | check_type | string | 否 | 设置评测的打分及检错松严门限（仅中文引擎支持）<br>easy：容易<br>common：普通<br>hard：困难| "common" |
  | grade | string | 否 | 设置评测的学段参数 （仅中文题型：中小学的句子、篇章题型支持）<br>junior(1,2年级)<br>middle(3,4年级)<br>senior(5,6年级)	| "middle" |
  | rst | string | 否 | 评测返回结果与分制控制（评测返回结果与分制控制也会受到ise_unite与plev参数的影响）<br>完整：entirety（默认值）<br>中文百分制推荐传参（rst="entirety"且ise_unite="1"且配合extra_ability参数使用）<br>英文百分制推荐传参（rst="entirety"且ise_unite="1"且配合extra_ability参数使用）<br>精简：plain（评测返回结果将只有总分），如：<br><?xml version="1.0" ?><FinalResult><ret value="0"/><total_score value="98.507320"/></FinalResult>| "entirety" |
  | ise_unite | string | 否 | 返回结果控制<br>0：不控制（默认值）<br>1：控制（extra_ability参数将影响全维度等信息的返回）| "0" |
  | plev | string | 否 | 在rst="entirety"（默认值）且ise_unite="0"（默认值）的情况下plev的取值不同对返回结果有影响。<br>plev：0(给出全部信息，汉语包含rec_node_type、perr_msg、fluency_score、phone_score信息的返回；英文包含accuracy_score、serr_msg、 syll_accent、fluency_score、standard_score、pitch信息的返回)	| "0" |

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/Ise/IseAPI.html)