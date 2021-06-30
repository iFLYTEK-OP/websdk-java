# 讯飞开放平台AI能力-关键词提取


### 使用
#### 关键词提取
##### 示例代码
```java
public static void main(String[] args) {
        try {
            LtpClient ltpClient = new LtpClient.Builder(APP_ID, API_KEY)
                    .func("ke")
                    .build();
            LtpResponse response = ltpClient.send("我来自北方");
            System.out.println(response.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("错误码查询链接：https://www.xfyun.cn/document/error-code");
        }
    }
```
更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/LtpClientApp.java)
##### 合成参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|appId|string|是|讯飞开放平台应用ID|595f23df|
|APIKey|string|是||af45b49cdeca84c839e9b683f8085ea3|
|func|string|是|能力标识 中文分词(cws);词性标注(pos);命名实体识别(ner);依存句法分析(dp);语义角色标注(srl);语义依存 (依存树) 分析(sdp);语义依存 (依存图) 分析(sdgp);关键词提取(ke)|ke|
|text|string|是|待分析文本(中文简体)，长度限制为30000字节|"我来自北方"|
|maxConnections|int|否|HTTP最大连接数|30|
|connTimeout|int|否|连接超时时间|10000(单位：毫秒)|
|soTimeout|int|否|响应超时时间|30000(单位：毫秒)|
|retryCount|int|否|重试次数|3(单位：次)|



