# 讯飞开放平台AI能力-非实时语音转写(Long Form ASR) 


### 使用
#### 非实时语音转写
##### 示例代码
```java
public class LfasrClientApp {
    private static final Logger logger = LoggerFactory.getLogger(LfasrClient.class);

    private static final String APP_ID = PropertiesConfig.getLfasrAppId();
    private static final String SECRET_KEY = PropertiesConfig.getSecretKey();
    private static String AUDIO_FILE_PATH;

    static {
        try {
            AUDIO_FILE_PATH = LfasrClientApp.class.getResource("/").toURI().getPath() + "/audio/lfasr.wav";
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SignatureException, InterruptedException {

        //1、创建客户端实例
        LfasrClient lfasrClient = new LfasrClient.Builder(APP_ID, SECRET_KEY).slice(102400).build();

        //2、上传
        LfasrMessage task = lfasrClient.upload(AUDIO_FILE_PATH);
        String taskId = task.getData();
        logger.info("转写任务 taskId：" + taskId);


        //3、查看转写进度
        int status = 0;
        while (LfasrTaskStatusEnum.STATUS_9.getKey() != status) {
            LfasrMessage message = lfasrClient.getProgress(taskId);

            logger.info(message.toString());
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(message.getData(), new TypeToken<Map<String, String>>() {
            }.getType());
            status = Integer.parseInt(map.get("status"));
            TimeUnit.SECONDS.sleep(2);
        }
        //4、获取结果
        LfasrMessage result = lfasrClient.getResult(taskId);
        logger.info("转写结果: \n" + result.getData());
        System.exit(0);
    }
}
```
更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/LfasrClientApp.java)
##### 合成参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|appId|string|是|讯飞开放平台应用ID|595f23df|
|secretKey|string|是|语音转写secretKey, [从控制台获取](https://console.xfyun.cn/services/lfasr)|af45b49cdeca84c839e9b683f8085ea3|
|file_name|string|否|需要配合aue=lame使用，开启流式返回<br>mp3格式音频<br>取值：1 开启|1|
|slice_size|int|否|文件分片数目（建议分片大小为10M，若文件<10M，则slice_num=1）|10485760|
|lfasr_type|string|否|转写类型，默认 0<br/>0:  (标准版，格式: wav,flac,opus,mp3,m4a)<br/>2: (电话版，已取消)|0|
|has_participle|string|否|转写结果是否包含分词信息|false或true， 默认false|
|max_alternatives|string|否|转写结果中最大的候选词个数|默认：0，最大不超过5|
|speaker_number|string|否|发音人个数，可选值：0-10，0表示盲分<br>*注*：发音人分离目前还是测试效果达不到商用标准，如测试无法满足您的需求，请慎用该功能。|默认：2（适用通话时两个人对话的场景）|
|has_seperate|string|否|转写结果中是否包含发音人分离信息|false或true，默认为false|
|role_type|string|否|支持两种参数<br/>1: 通用角色分离<br/>2: 电话信道角色分离（适用于speaker_number为2的说话场景）该字段只有在开通了角色分离功能的前提下才会生效，正确传入该参数后角色分离效果会有所提升。 如果该字段不传，默认采用 1 类型|
|language|string|否|语种<br>cn:中英文&中文（默认）<br>en:英文（英文不支持热词）|cn|
|pd|string|否|垂直领域个性化参数:<br>法院: court<br>教育: edu<br>金融: finance<br>医疗: medical<br>科技: tech 设置示例:prepareParam.put("pd", "edu")<br>pd为非必须设置参数，不设置参数默认为通用|
|coreThreads|int|否|线程池核心线程数|10|
|maxThreads|int|否|线程池最大线程数|50|
|maxConnections|int|否|HTTP最大连接数|50|
|connTimeout|int|否|连接超时时间|10000(单位：毫秒)|
|soTimeout|int|否|响应超时时间|30000(单位：毫秒)|

