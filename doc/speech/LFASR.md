# 讯飞开放平台AI能力-非实时语音转写(Long Form ASR) 


### 使用
#### 非实时语音转写
##### 示例代码
```java
public class LfasrClientApp {

    private static final Logger logger = LoggerFactory.getLogger(LfasrClientApp.class);
    
    private static final Gson GSON = new Gson();

    /**
     * 服务鉴权参数
     */
    private static final String APP_ID = PropertiesConfig.getAppId();
    private static final String SECRET_KEY = PropertiesConfig.getLfasrSecretKey();

    /**
     * 音频文件路径
     * - 本地文件（默认、调用uploadFile方法）
     * - 远程Url（配合参数audioMode = urlLink使用、调用uploadUrl方法）
     */
    private static String audioFilePath;

    private static final String AUDIO_URL = "https://openres.xfyun.cn/xfyundoc/2025-03-19/e7b6a79d-124f-44e0-b8aa-0e799410f453/1742353716311/lfasr.wav";

    /**
     * 任务类型
     * - transfer：转写
     * - translate：翻译（配合参数transLanguage和transMode使用）
     * - predict：质检（配合控制台质检词库使用）
     * - transfer,predict：转写 + 质检
     */
    private static final String TASK_TYPE = "transfer";

    static {
        try {
            audioFilePath = Objects.requireNonNull(LfasrClientApp.class.getResource("/")).toURI().getPath() + "/audio/lfasr.wav";
        } catch (Exception e) {
            logger.error("资源路径获取失败", e);
        }
    }

    public static void main(String[] args) throws SignatureException, InterruptedException {
        // 1、创建客户端实例
        LfasrClient lfasrClient = new LfasrClient.Builder(APP_ID, SECRET_KEY)
                .roleType((short) 1)
                // .transLanguage("en")
                // .audioMode("urlLink")
                .build();

        // 2、上传音频文件（本地/Url）
        logger.info("音频上传中...");
        LfasrResponse uploadResponse = lfasrClient.uploadFile(audioFilePath);
        // LfasrResponse uploadResponse = lfasrClient.uploadUrl(AUDIO_URL);
        if (uploadResponse == null) {
            logger.error("上传失败，响应为空");
            return;
        }
        if (!StringUtils.equals(uploadResponse.getCode(), "000000")) {
            logger.error("上传失败，错误码：{}，错误信息：{}", uploadResponse.getCode(), uploadResponse.getDescInfo());
            return;
        }
        String orderId = uploadResponse.getContent().getOrderId();
        logger.info("转写任务orderId：{}", orderId);

        // 3、查询转写结果
        int status = LfasrOrderStatusEnum.CREATED.getKey();
        // 循环直到订单完成或失败
        while (status != LfasrOrderStatusEnum.COMPLETED.getKey() && status != LfasrOrderStatusEnum.FAILED.getKey()) {
            LfasrResponse resultResponse = lfasrClient.getResult(orderId, TASK_TYPE);
            if (!StringUtils.equals(resultResponse.getCode(), "000000")) {
                logger.error("转写任务失败，错误码：{}，错误信息：{}", resultResponse.getCode(), resultResponse.getDescInfo());
                return;
            }

            // 获取订单状态信息
            if (resultResponse.getContent() != null && resultResponse.getContent().getOrderInfo() != null) {
                status = resultResponse.getContent().getOrderInfo().getStatus();
                int failType = resultResponse.getContent().getOrderInfo().getFailType();

                // 根据状态输出日志
                LfasrOrderStatusEnum statusEnum = LfasrOrderStatusEnum.getEnum(status);
                if (statusEnum != null) {
                    logger.info("订单状态：{}", statusEnum.getValue());

                    // 如果订单失败，输出失败原因
                    if (statusEnum == LfasrOrderStatusEnum.FAILED) {
                        LfasrFailTypeEnum failTypeEnum = LfasrFailTypeEnum.getEnum(failType);
                        logger.error("订单处理失败，失败原因：{}", failTypeEnum.getValue());
                        return;
                    }
                    // 如果订单已完成，输出结果
                    if (statusEnum == LfasrOrderStatusEnum.COMPLETED) {
                        logger.info("转写结果：{}", GSON.toJson(resultResponse));
                        return;
                    }
                } else {
                    logger.error("未知的订单状态：{}", status);
                }
            } else {
                logger.error("返回结果中缺少订单信息");
            }

            TimeUnit.SECONDS.sleep(20);
        }
    }
}
```
更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/speech/LfasrClientApp.java)
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

 *注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/asr/lfasr/API.html)

