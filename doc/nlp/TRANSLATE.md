### 小牛翻译及自研机器翻译

**示例代码**

```java
TransParam param = TransParam.builder()
                    .text("神舟十二号载人飞船发射任务取得圆满成功")
                    .from("cn")
                    .to("en")
                    .resId("您的个性化术语ID")
                    .build();
            // 小牛翻译 (默认中译英)
            // String niuResponse = client.sendNiuTrans("神舟十二号载人飞船发射任务取得圆满成功");
            String niuResponse = client.sendNiuTrans(param);
            logger.info("niuResponse:{}", niuResponse);

            // 自研机器翻译 (默认中译英)
            // String itsResponse = client.sendIst("6月9号是科大讯飞司庆日");
            String itsResponse = client.sendIst(param);
            logger.info("itsResponse:{}", itsResponse);

            // 自研机器翻译（新） (默认中译英)
            // String itsProResponse = client.sendIstV2("6月9号是科大讯飞司庆日");
            String itsProResponse = client.sendIstV2(param);
            logger.info("itsProResponse:{}", itsProResponse);
            JSONObject obj = JSON.parseObject(itsProResponse);
            String text = obj.getJSONObject("payload").getJSONObject("result").getString("text");
            byte[] decodedBytes = Base64.getDecoder().decode(text);
            String decodeRes = new String(decodedBytes, StandardCharsets.UTF_8);
            logger.info("itsPro翻译结果:{}", decodeRes);
```

更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/nlp/TranslateApp.java)

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

**自研机器翻译(新)参数**

| 参数名 | 类型   | 必传 | 描述                                                         | 示例                                                         |
| ------ | ------ | ---- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| from   | string | 是   | 源语种，请参见 [语种列表](https://www.xfyun.cn/doc/nlp/xftrans_new/API.html#%E8%AF%AD%E7%A7%8D%E5%88%97%E8%A1%A8) | "cn"                                                         |
| to     | string | 是   | 目标语种，请参见 [语种列表](https://www.xfyun.cn/doc/nlp/xftrans_new/API.html#%E8%AF%AD%E7%A7%8D%E5%88%97%E8%A1%A8)<br> | "en"                                                         |
| text   | string | 是   | 待翻译文本数据<br>字符要大于0且小于5000。                    | "科大讯飞是亚太地区知名的智能语音和人工智能上市企业,致力于让机器能听会说,能理解会思考,用人工智能建设美好世界" |
| resId  | string | 否   | 1、个性化术语资源id<br />2、在机器翻译控制台自定义（翻译术语热词格式为：原文本1<br />3、请注意使用参数值和控制台自定义的值保持一致 |                                                              |

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/nlp/xftrans_new/API.html)
