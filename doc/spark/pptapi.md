# 讯飞智文OpenAPI协议文档


## 接口与鉴权

### 应用申请

> 提供工单开通接口权限


### 实例代码

##### 示例代码

1、添加maven依赖
```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-spark</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.0.3</version>
</dependency>
```

2、Java代码
```java
import cn.hutool.json.JSONUtil;
import cn.xfyun.api.AIPPTClient;
import cn.xfyun.model.CreateParam;
import cn.xfyun.model.CreateResult;
import cn.xfyun.model.ThemeResponse;

import java.util.UUID;
/**
 * 智文PPT的接口demo
 *
 * @author junzhang27
 */
public class AIPPTClientApp {
  private static final String APP_ID = "替换成你的appid";
  private static final String API_SECRET = "替换成你的apiSecret";

  public static void main(String[] args) {
    try {
      AIPPTClient client = new AIPPTClient.Builder(APP_ID, API_SECRET).readTimeout(10000).build();

      String themeList = client.themeList();
      ThemeResponse themeResponse = JSONUtil.toBean(themeList, ThemeResponse.class);
      System.out.println("ppt主题列表：" + JSONUtil.toJsonStr(themeResponse));

      CreateParam createParam = new CreateParam.Builder()
              .query("介绍一下中国的春节习俗")
              .businessId(UUID.randomUUID().toString())
              .build();
      String response = client.create(createParam);
      CreateResult createResult = JSONUtil.toBean(response, CreateResult.class);
      System.out.println("ppt生成首图结果：" + response);

      String response1 = client.progress(createResult.getData().getSid());
      System.out.println("ppt当前的进度信息：" + response1);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
```

更详细请参见 [Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/spark/AIPPTClientApp.java)


### 接口域名
```apl
zwapi.xfyun.cn
```

## 错误码

| 错误码 | 描述         | 处理方式                                                     |
| ------ | ------------ | ------------------------------------------------------------ |
| 20002  | 参数错误     | 确认接口入参                                                 |
| 20005  | 大纲生成失败 | 查看是否存在敏感词汇，尝试重新生成                           |
| 20006  | PPT生成失败  | PPT导出错误，请重新生成或联系技术人员                        |
| 20007  | 鉴权错误     | 检查鉴权信息                                                 |
| 9999   | 系统异常     | 确认鉴权信息、请求方式、请求参数是否有误，或联系技术人员排查相关日志 |



## 接口列表

### 1 POST PPT生成

**1.1 接口描述：**

 基于用户提示、要求等相关内容生成PPT，字数不得超过8000字。

**1.2 接口地址：**

```text
https://zwapi.xfyun.cn/api/aippt/create
```

**1.3 请求查询参数：**

```text
POST，application/json
```

|     名称      |  类型  |                      描述                       | 必须 | 默认值   |
| :-----------: | :----: | :---------------------------------------------: | ---- | -------- |
|     query     | String |          用户生成PPT要求（最多8000字）          | Y    |          |
| create_model  | String | PPT生成类型：文本生成、话题生成、程序判断(默认) | N    | auto     |
|     theme     | String |     PPT生成主题：随机主题(默认)、紫影幽蓝……     | N    | auto     |
| content_level | String |        PPT详细程度：标准版(默认)、复杂版        | N    | standard |
|  business_id  | String |    业务ID（非必传）- 业务方自行决定是否使用     | N    |          |

**1.4 可选参数列表：**

- create_model：

  - auto： 自动，由程序自行判断；

  - topic：话题生成（建议150字以内）

  - text：文本生成，基于长文本生成


- theme：

  - auto：自动，随机主题

  - purple：紫色主题

  - green：绿色主题

  - lightblue：清逸天蓝

  - taupe：质感之境

  - blue：星光夜影

  - telecomRed：炽热暖阳

  - telecomGreen：幻翠奇旅


- content_level：

  - standard： 标准

  - complex：复杂


**1.5 请求响应：**

```
{
    "code":0,
    "desc":"成功",
    "data":{
    	"sid":"zhiwen@xxxxxxxxxxxxxxxxxxxxx",	// 请求唯一ID，此处后端可对应到OpenAPI业务表即可
        "coverImgSrc":"xxxxxxxxxxxxxx",			// 封面地址
        "title" : "xxx",						// 主标题
        "subTitle" : "xxx"						// 副标题
    }
}
```

**1.6 响应描述**

|     响应字段     |  类型  |     描述      |
| :--------------: | :----: | :-----------: |
|       code       |  int   |    错误码     |
|       desc       | string |   错误详情    |
|     data.sid     | string |  请求唯一id   |
| data.CoverImgSrc | string | PPT封面图链接 |
|    data.title    | string |   PPT主标题   |
|  data.subTitle   | string |   PPT副标题   |

### 2 PPT进度查询

**2.1 接口地址：**

```
https://zwapi.xfyun.cn/api/aippt/progress?sid={}
```

**2.2 请求查询参数：**

```text
GET
```

| 名称 |  类型  |    描述    | 必须 | 默认值 |
| :--: | :----: | :--------: | ---- | ------ |
| sid  | String | 请求唯一ID | Y    |        |

**2.3 请求响应：**

```
{
    "code":0,
    "desc":"成功",
    "data":{
    	"process" : 0			// 生成进度：30-大纲生成完毕、70-PPT生成完毕、100-PPT到处完毕
    	"pptId" : xxxx			// pptId
    	"pptUrl" : xxxx			// ppt下载链接
    }
}
```

### 3 PPT主题列表查询

**3.1 接口地址：**

```
https://zwapi.xfyun.cn/api/aippt/themeList
```

**3.2 请求查询参数：**

```text
GET
```

**3.3 请求响应：**

```
{
    "flag": true,
    "code": 0,
    "desc": "成功",
    "count": null,
    "data": [
        {
            "key": purple,					// 主题名
            "name": "紫影幽蓝",
            "thumbnail": "xxx",				// 缩略图
        }
    ]
}
```



