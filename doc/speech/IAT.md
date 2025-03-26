### 语音听写

**示例代码**

```java
import cn.xfyun.api.IatClient;

// 设置听写参数,这里的appid,apiKey,apiSecret是在开放平台控制台获得
IatClient iatClient = new IatClient.Builder()
                .signature(appId, apiKey, apiSecret)
                //... 这里可以继续设置听写相关参数，参数见下面表格
                .build();


iatClient.send(file, new AbstractIatWebSocketListener() {
            @Override
            public void onSuccess(WebSocket webSocket, IatResponse iatResponse) {
              // 根据服务端成功响应，进行数据处理，处理方法可见demo
            }

            @Override
            public void onFail(WebSocket webSocket, Throwable t, Response response) {
              // 根据业务需求，对失败时的数据进行处理
            }
        });

```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/speech/IatClientApp.java)

**听写参数**

  | 参数名   | 类型   | 必传 | 描述                                                         | 示例    |
  | -------- | ------ | ---- | ------------------------------------------------------------ | ------- |
  | language | string | 是   | 语种<br>zh_cn：中文（支持简单的英文识别）<br>en_us：英文<br>其他小语种：可到控制台-语音听写（流式版）-方言/语种处添加试用或购买，添加后会显示该小语种参数值，若未授权无法使用会报错11200。<br>另外，小语种接口URL与中英文不同。 | "zh_cn" |
  | domain   | string | 是   | 应用领域<br>iat：日常用语<br>medical：医疗<br>gov-seat-assistant：政务坐席助手<br>seat-assistant：金融坐席助手<br>gov-ansys：政务语音分析<br>gov-nav：政务语音导航<br>fin-nav：金融语音导航<br>fin-ansys：金融语音分析<br>*注*：除日常用语领域外其他领域若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处添加试用或购买；若未授权无法使用会报错11200。<br>坐席助手、语音导航、语音分析相关垂直领域仅适用于8k采样率的音频数据，另外三者的区别详见下方。 | "iat"   |
  |accent|string|是|方言，当前仅在language为中文时，支持方言选择。<br>mandarin：中文普通话、其他语种<br>其他方言：可到控制台-语音听写（流式版）-方言/语种处添加试用或购买，添加后会显示该方言参数值；方言若未授权无法使用会报错11200。|"mandarin"
  |vad_eos|int|否|用于设置端点检测的静默时间，单位是毫秒。<br>即静默多长时间后引擎认为音频结束。<br>默认2000（小语种除外，小语种不设置该参数默认为未开启VAD）。|3000|
  |dwa|string|否|（仅中文普通话支持）动态修正<br>wpgs：开启流式结果返回功能<br>*注：该扩展功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处免费开通；若未授权状态下设置该参数并不会报错，但不会生效。*|"wpgs"|
  |pd|string|否|（仅中文支持）领域个性化参数<br>game：游戏<br>health：健康<br>shopping：购物<br>trip：旅行<br>*注：该扩展功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处添加试用或购买；若未授权状态下设置该参数并不会报错，但不会生效。*|"game"|
  |ptt|int|否|（仅中文支持）是否开启标点符号添加<br>1：开启（默认值）<br>0：关闭|0|
  |rlang|string|否|（仅中文支持）字体 <br>zh-cn :简体中文（默认值）<br>zh-hk :繁体香港<br>*注：该繁体功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处免费开通；若未授权状态下设置为繁体并不会报错，但不会生效。*|"zh-cn"|
  |vinfo|int|否|返回子句结果对应的起始和结束的端点帧偏移值。端点帧偏移值表示从音频开头起已过去的帧长度。<br>0：关闭（默认值）<br>1：开启<br>开启后返回的结果中会增加data.result.vad字段，详见下方返回结果。<br>*注：若开通并使用了动态修正功能，则该功能无法使用。*|1|
  |nunum|int|否|（中文普通话和日语支持）将返回结果的数字格式规则为阿拉伯数字格式，默认开启<br>0：关闭<br>1：开启|0|
  |speex_size|int|否|speex音频帧长，仅在speex音频时使用<br>1 当speex编码为标准开源speex编码时必须指定<br>2 当speex编码为讯飞定制speex编码时不要设置<br>注：标准开源speex以及讯飞定制SPEEX编码工具请参考这里 [speex编码](https://www.xfyun.cn/doc/asr/voicedictation/Audio.html) 。|70|
  |nbest|int|否|取值范围[1,5]，通过设置此参数，获取在发音相似时的句子多侯选结果。设置多候选会影响性能，响应时间延迟200ms左右。<br>*注：该扩展功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处免费开通；若未授权状态下设置该参数并不会报错，但不会生效。*|3|
  |wbest|int|否|取值范围[1,5]，通过设置此参数，获取在发音相似时的词语多侯选结果。设置多候选会影响性能，响应时间延迟200ms左右。<br>*注：该扩展功能若未授权无法使用，可到控制台-语音听写（流式版）-高级功能处免费开通；若未授权状态下设置该参数并不会报错，但不会生效。*|5|
 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/asr/voicedictation/API.html#%E6%8E%A5%E5%8F%A3%E8%B0%83%E7%94%A8%E6%B5%81%E7%A8%8B)