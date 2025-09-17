package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.config.WordLibEnum;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.SignatureException;
import java.util.List;
import java.util.Map;

/**
 * 词条操作 Client
 * 文档地址: <a href="https://www.xfyun.cn/doc/nlp/TextModeration/API.html">...</a>
 *
 * @author <zyding6@ifytek.com>
 */
public class WordLibClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(WordLibClient.class);

    public WordLibClient(Builder builder) {
        super(builder);
    }

    /**
     * @param category   指定检测的敏感分类：
     *                   pornDetection 色情
     *                   violentTerrorism 暴恐
     *                   political 涉政
     *                   lowQualityIrrigation 低质量灌水
     *                   contraband 违禁
     *                   advertisement 广告
     *                   uncivilizedLanguage 不文明用语
     * @param name       词库名称
     * @param suggestion 处理方式 block：违规
     * @return 返回结果
     * @throws IOException        请求异常
     * @throws SignatureException 鉴权异常
     */
    public String createBlackLib(String name, String category, String suggestion) throws IOException, SignatureException {
        return createLib(name, category, suggestion, true);
    }

    /**
     * @param name 词库名称
     * @return 返回结果
     * @throws IOException        请求异常
     * @throws SignatureException 鉴权异常
     */
    public String createWhiteLib(String name) throws IOException, SignatureException {
        return createLib(name, null, null, false);
    }

    /**
     * 根据lib_id添加黑名单词条
     *
     * @param libId    词库ID
     * @param wordList 词条列表：
     *                 单次添加不能超过 500，总数不能超过 10000；
     *                 仅支持中文词条，单个长度最长不超过 20；
     *                 不能包含特殊符号，如："[`~!@#$%^*_+\-=<>?,./;':" \t]"
     * @return 返回结果
     * @throws IOException        请求异常
     * @throws SignatureException 鉴权异常
     */
    public String addWord(String libId, List<String> wordList) throws IOException, SignatureException {
        // 参数校验
        if (StringUtils.isNullOrEmpty(libId) || wordList == null || wordList.isEmpty()) {
            throw new BusinessException("词库ID或词条列表为空");
        }

        // 参数
        JsonObject param = new JsonObject();
        param.addProperty("lib_id", libId);
        param.add("word_list", StringUtils.gson.toJsonTree(wordList));

        // 发送请求
        logger.debug("添加词条请求入参: {}", param);
        return send(WordLibEnum.ADD_WORD, param.toString());
    }

    /**
     * 根据lib_id删除词条
     *
     * @param libId    词库ID
     * @param wordList 待删除的词列表（单次不能超过 500条）
     * @return 返回结果
     * @throws IOException        请求异常
     * @throws SignatureException 鉴权异常
     */
    public String delWord(String libId, List<String> wordList) throws IOException, SignatureException {
        // 参数校验
        if (StringUtils.isNullOrEmpty(libId) || wordList == null || wordList.isEmpty()) {
            throw new BusinessException("词库ID或词条列表为空");
        }

        // 参数
        JsonObject param = new JsonObject();
        param.addProperty("lib_id", libId);
        param.add("word_list", StringUtils.gson.toJsonTree(wordList));

        // 发送请求
        logger.debug("删除词条请求入参: {}", param);
        return send(WordLibEnum.DEL_WORD, param.toString());
    }

    /**
     * 根据lib_id查询词条明细
     *
     * @param libId      词库ID
     * @param returnWord 决定是否返回词条明细，建议必传true
     * @return 返回结果
     * @throws IOException        请求异常
     * @throws SignatureException 鉴权异常
     */
    public String info(String libId, boolean returnWord) throws IOException, SignatureException {
        // 参数校验
        if (StringUtils.isNullOrEmpty(libId)) {
            throw new BusinessException("词库ID为空");
        }

        // 参数
        JsonObject param = new JsonObject();
        param.addProperty("lib_id", libId);
        param.addProperty("return_word", returnWord);

        // 发送请求
        logger.debug("查询词条明细请求入参: {}", param);
        return send(WordLibEnum.INFO, param.toString());
    }

    public String info(String libId) throws Exception {
        return info(libId, true);
    }

    /**
     * 根据appid查询账户下所有词库
     *
     * @return 返回结果
     * @throws IOException        请求异常
     * @throws SignatureException 鉴权异常
     */
    public String listLib() throws IOException, SignatureException {
        // 发送请求
        return send(WordLibEnum.LIST, "{}");
    }

    /**
     * 根据lib_id删除词库
     *
     * @return 返回结果
     * @throws IOException        请求异常
     * @throws SignatureException 鉴权异常
     */
    public String deleteLib(String libId) throws IOException, SignatureException {
        // 参数
        JsonObject param = new JsonObject();
        param.addProperty("lib_id", libId);

        // 发送请求
        logger.debug("删除词库请求入参: {}", param);
        return send(WordLibEnum.DEL_LIB, param.toString());
    }

    /**
     * 新增黑/白名单词库
     *
     * @param name       词库名称(不去重)
     * @param category   一级分类预设分类或自定义
     * @param suggestion block：违规
     * @param black      true-黑名单，false-白名单
     * @return 返回结果
     * @throws IOException        请求异常
     * @throws SignatureException 鉴权异常
     */
    private String createLib(String name, String category, String suggestion, boolean black) throws IOException, SignatureException {
        // 参数校验
        if ((StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(category)) && black) {
            throw new BusinessException("词库名称或词库策略为空");
        }

        // 参数
        JsonObject param = new JsonObject();
        param.addProperty("name", name);
        param.addProperty("category", category);
        param.addProperty("suggestion", StringUtils.isNullOrEmpty(suggestion) ? "block" : suggestion);

        // 发送请求
        logger.debug("创建词库请求入参: {}", param);
        return send(black ? WordLibEnum.CREATE_BLACK : WordLibEnum.CREATE_WHITE, param.toString());
    }

    /**
     * 公共请求
     *
     * @param wordLibEnum 词库请求类型
     * @param param       请求参数
     * @throws IOException        请求异常
     * @throws SignatureException 鉴权异常
     */
    private String send(WordLibEnum wordLibEnum, String param) throws IOException, SignatureException {
        // 获取鉴权参数
        Map<String, String> parameters = Signature.getAuth(appId, apiKey, apiSecret);

        // 构建请求体
        RequestBody body = RequestBody.create(JSON, param);

        // 发送请求
        return sendPost(hostUrl + wordLibEnum.getUrl(), null, body, parameters);
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://audit.iflyaisol.com/audit_res/v1";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
        }

        @Override
        public WordLibClient build() {
            return new WordLibClient(this);
        }
    }
}
