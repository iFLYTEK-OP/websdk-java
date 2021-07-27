# 讯飞开放平台AI能力-自然语言处理


### 使用
#### 自然语言处理
##### 示例代码
```java
LtpClient ltpClient = new LtpClient
                .Builder(appId, apiKey, LtpFunctionEnum.NER)
                .build();
        String response = ltpClient.send("我来自北方");
        System.out.println(response);
```
更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/LtpClientApp.java)
##### 合成参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|appId|string|是|讯飞开放平台应用ID|595f23df|
|apiKey|string|是||af45b49cdeca84c839e9b683f8085ea3|
|func|enum|是|能力标识 中文分词(cws);词性标注(pos);命名实体识别(ner);依存句法分析(dp);语义角色标注(srl);语义依存 (依存树) 分析(sdp);语义依存 (依存图) 分析(sdgp);关键词提取(ke)|ke|
|text|string|是|待分析文本(中文简体)，长度限制为30000字节|"我来自北方"|

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/nlp/dependencyParsing/API.html)



