# 讯飞开放平台AI能力-JAVASDK语音能力库

### 印刷文字识别和手写文字识别

**示例代码**
```java
        GeneralWordsClient client = new GeneralWordsClient
                .Builder(appId, apiKey, OcrWordsEnum.HANDWRITING)
                .build();
        byte[] imageByteArray = read(resourcePath + "/image/1.jpg");
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        System.out.println(client.generalWords(imageBase64));
```
更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/GeneralWordsClientApp.java)

##### 识别参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|ocrTypeEnum|OcrWordsEnum|是|文字识别类别。取值PRINT（印刷文字识别）HANDWRITING（手写文字识别）|OcrWordsEnum.HANDWRITING|
|language|LanguageEnum|否|识别语言。EN（英文）或者CN（中英文混合）|LanguageEnum.CN|
|location|LocationEnum|否|返回文本位置信息。ON返回或者OFF不返回|LocationEnum.ON|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/words/wordRecg/API.html)
