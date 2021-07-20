### 小牛翻译及自研机器翻译

**示例代码**

```java

TransClient client = new TransClient.Builder(appId, apiKey, apiSecret)
                //... 这里可以继续设置翻译相关参数，参数见下面表格
                .build();


// 小牛翻译，该方法后三个参数分别为 text：待翻译文本、 from:源语种、to：目标语种，具体介绍见下面表格
TransResponse niuResponse = client.sendNiuTrans("神舟十二号载人飞船发射任务取得圆满成功","cn","en");

// 自研机器翻译，该方法后三个参数分别为 text：待翻译文本、 from:源语种、to：目标语种，具体介绍见下面表格
TransResponse itsResponse = client.sendIst("6月9号是科大讯飞司庆日","cn","en");

// niuResponse及itsResponse为小牛翻译及自研翻译的翻译返回结果，可根据该结果进行业务处理，举例如下
Map<String,String> niuResult = niuResponse.getData().getResult().getTrans_result();
System.out.println("小牛翻译原文为："+niuResult.get("src"));
System.out.println("小牛翻译结果为："+niuResult.get("dst"));

Map<String,String> itsResult = itsResponse.getData().getResult().getTrans_result();
System.out.println("自研翻译原文为："+itsResult.get("src"));
System.out.println("自研翻译结果为："+itsResult.get("dst"));
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/TranslateApp.java)

**小牛翻译参数**

  | 参数名   | 类型   | 必传 | 描述                                                         | 示例    |
  | -------- | ------ | ---- | ------------------------------------------------------------ | ------- |
  | from | string | 是   | 源语种，请参见 [语种列表](https://www.xfyun.cn/doc/nlp/niutrans/API.html#%E8%AF%AD%E7%A7%8D%E5%88%97%E8%A1%A8)<br>可以指定语种参数，也可以指定auto自动识别源语种<br>注：目前自动识别语种（auto）的效果，对长文本及非同语系的文本较为理想，对短文本及同语系的效果还在逐步优化中，请根据您的实际需求场景使用。 | "cn" |
  | to | string | 是   | 目标语种，请参见 [语种列表](https://www.xfyun.cn/doc/nlp/niutrans/API.html#%E8%AF%AD%E7%A7%8D%E5%88%97%E8%A1%A8)<br> | "en" |
  | text   | string | 是   | 待翻译文本数据<br>字数长度请勿超过5000。 | "科大讯飞是亚太地区知名的智能语音和人工智能上市企业,致力于让机器能听会说,能理解会思考,用人工智能建设美好世界"   |

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/nlp/niutrans/API.html)
 
 **自研机器翻译参数**

  | 参数名   | 类型   | 必传 | 描述                                                         | 示例    |
  | -------- | ------ | ---- | ------------------------------------------------------------ | ------- |
  | from | string | 是   | 源语种，请参见 [语种列表](https://www.xfyun.cn/doc/nlp/xftrans/API.html#%E8%AF%AD%E7%A7%8D%E5%88%97%E8%A1%A8) | "cn" |
  | to | string | 是   | 目标语种，请参见 [语种列表](https://www.xfyun.cn/doc/nlp/xftrans/API.html#%E8%AF%AD%E7%A7%8D%E5%88%97%E8%A1%A8)<br> | "en" |
  | text   | string | 是   | 待翻译文本数据<br>字数长度请勿超过256。 | "科大讯飞是亚太地区知名的智能语音和人工智能上市企业,致力于让机器能听会说,能理解会思考,用人工智能建设美好世界"   |

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/nlp/xftrans/API.html)
