# 聚合搜索（万搜） API文档

## 简介

本客户端支持全文检索、结果重排、时效性识别、内容安全分类等高级能力[官方文档](https://www.xfyun.cn/doc/spark/Search_API/search_API.html)，支持以下功能：

- 联网搜索

## 功能列表

| 方法名 | 功能说明 |
| ------ | -------- |
| send() | 联网搜索 |

## 使用准备

1. 前往[能力开通](https://www.xfyun.cn/services/OneAPI)页面
2. 创建应用并获取以下凭证：
   - AppId
   - APIPassword

## 快速开始

1、添加maven依赖

```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-spark</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.1.9</version>
</dependency>
```

2、Java代码

```java
import cn.xfyun.api.WebSearchClient;
import cn.xfyun.model.websearch.WebSearchParam;

 WebSearchClient client = new WebSearchClient
                .Builder(appId, apiPassword)
                .build();

        WebSearchParam param = WebSearchParam.builder()
                .query("先有鸡还是先有蛋")
                .limit(10)
                .build();
        String resp = client.send(param);
        logger.info("请求结果：{}", resp);
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/WebSearchClientApp.java)

-----

## 错误码

| 错误码  | 错误信息                                                  |
| ------- | --------------------------------------------------------- |
| `0`     | 成功                                                      |
| `11200` | 授权错误：该 appId 没有相关功能的授权，或者业务量超过限制 |
| `11201` | 授权错误：日流控超限。超过当日最大访问量的限制            |
| `11202` | 授权错误：秒级流控超限。秒级并发超过授权路数限制          |
| `11203` | 授权错误：并发流控超限。并发路数超过授权路数限制          |
| `21001` | 缺少参数                                                  |
| `21009` | 未授权：该 appId 没有相关功能的授权                       |

-----

## 方法详解

### 1. 联网搜索
```java
public String send(WebSearchParam param) throws IOException
```
**参数说明**：

- `WebSearchParam`: 查询参数对象，可设置：

|     名称     |  类型   |                          描述                           | 必须 | 默认值 |
| :----------: | :-----: | :-----------------------------------------------------: | ---- | ------ |
|    query     | String  | 用户输入的搜索关键词或问题 * 非空字符串，建议 ≤512 字符 | Y    |        |
|    limit     |   int   |         返回结果的最大条数 * 1 ~ 20，默认为 10          | N    | 10     |
|  openRerank  | Boolean |                       重排开关项                        | N    | true   |
| openFullText | Boolean |                       全文开关项                        | N    | true   |

**响应示例**：

```json
{
  "data": {
    "meta": {
      "query": "美国现任总统是谁"
    },
    "search_results": {
      "documents": [
         {
          "summary": " 1. **原则分析**:  - 合法合规是基础,确保索赔依据符合合同及法律; - 实事求是强调以事实为依据,避免夸大或虚构; - 平等协商避免单方面施压,维系合作关系; - 注重证据通过文件资料支撑主张; - 及时性防止时效性问题导致权利丧失。2. **策略推导**:  - 目标明确保证谈判方向不偏离; - 让步需设定底线,换取对方妥协; - 利益交换通过筹码平衡诉求; - 多方案增强灵活性; - 压力策略如停工威胁需谨慎使用。3. **技巧归纳**:  - 沟通节奏掌控主动权; - 倾听提问挖掘对方真实需求; - 情绪稳定避免冲突升级; - 聚焦核心条款提高效率; - 设定期限促使对方决策。",
          "content": "",
          "url": "https://easylearn.baidu.com/edu-page/tiangong/questiondetail?id=1828512528438428075&fr=search",
          "name": "简述索赔谈判的原则、策略与技巧",
          "published_date": "2025年04月05日"
        }
        // ……其他结果
      ]
    }
  },
  "err_code": "0",
  "sid": "ocp5e0146fa@dx19a519b8ef8b8985e0",
  "success": true
}
```

---

## 注意事项
1. 非空字符串，建议 ≤512 字符
   
2. 所有生成类接口都需要处理`BusinessException`（参数校验失败）和`IOException`（网络错误）

3. 客户端默认超时时间为60秒，可通过Builder调整：

```java
new WebSearchClient
                .Builder(appId, apiKey, apiSecret)
                .readTimeout(60)
                .build();
```

## 错误处理
捕获异常示例：
```java
try {
    String result = client.create(createReq);
} catch (BusinessException e) {
    System.err.println("业务异常：" + e.getMessage());
} catch (IOException e) {
    System.err.println("网络请求失败：" + e.getMessage());
}
```

## 使用建议

- **查询优化**：建议使用完整问句（如“美国现任总统是谁？”）而非关键词（如“美国总统”），以获得更精准的时效性结果。