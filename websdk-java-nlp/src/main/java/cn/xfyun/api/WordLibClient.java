package cn.xfyun.api;

import cn.xfyun.base.http.HttpBuilder;
import cn.xfyun.base.http.HttpClient;
import cn.xfyun.exception.BusinessException;
import cn.xfyun.model.sign.Signature;
import cn.xfyun.util.StringUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 词条操作 API
 *
 * @author zyding
 * @version 1.0
 * @date 2025/3/13 15:32
 */
public class WordLibClient extends HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(WordLibClient.class);

    public WordLibClient(Builder builder) {
        super(builder);
    }

    /**
     * @param category 指定检测的敏感分类：
     *                 pornDetection 色情
     *                 violentTerrorism 暴恐
     *                 political 涉政
     *                 lowQualityIrrigation 低质量灌水
     *                 contraband 违禁
     *                 advertisement 广告
     *                 uncivilizedLanguage 不文明用语
     * @param name     词库名称
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String createBlackLib(String name, String category) throws Exception {
        return createLib(name, category, "block", true);
    }

    /**
     * @param name 词库名称
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String createWhiteLib(String name) throws Exception {
        return createLib(name, null, null, false);
    }

    /**
     * 新增黑名单词库
     *
     * @param name       词库名称(不去重)
     * @param category   处理方式：block：违规
     * @param suggestion block：违规
     * @param black      block：true-黑名单，false-白名单
     * @return 返回结果
     * @throws Exception 异常信息
     */
    private String createLib(String name, String category, String suggestion, boolean black) throws Exception {
        if ((StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(category)) && black) {
            throw new BusinessException("词库名称或词库策略为空");
        }
        // 参数
        JsonObject param = new JsonObject();
        param.addProperty("name", name);
        param.addProperty("category", category);
        param.addProperty("suggestion", suggestion);
        Map<String, String> parameters = Signature.getAuth(appId, apiKey, apiSecret);
        return sendPost(hostUrl + "wordLib/" + (black ? "createBlack" : "createWhite"), JSON, null, param.toString(), parameters);
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
     * @throws Exception 异常信息
     */
    public String addWord(String libId, List<String> wordList) throws Exception {
        if (StringUtils.isNullOrEmpty(libId) || wordList == null || wordList.isEmpty()) {
            throw new BusinessException("词库ID或词条列表为空");
        }
        // 参数
        Map<String, Object> param = new HashMap<>();
        param.put("lib_id", libId);
        param.put("word_list", wordList);
        Map<String, String> parameters = Signature.getAuth(appId, apiKey, apiSecret);
        return sendPost(hostUrl + "wordLib/addWord", JSON, null, StringUtils.toJson(param), parameters);
    }

    /**
     * 根据lib_id删除词条
     *
     * @param libId    词库ID
     * @param wordList 待删除的词列表（单次不能超过 500条）
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String delWord(String libId, List<String> wordList) throws Exception {
        if (StringUtils.isNullOrEmpty(libId) || wordList == null) {
            throw new BusinessException("词库ID或词条列表为空");
        }
        // 参数
        Map<String, Object> param = new HashMap<>();
        param.put("lib_id", libId);
        param.put("word_list", wordList);
        Map<String, String> parameters = Signature.getAuth(appId, apiKey, apiSecret);
        return sendPost(hostUrl + "wordLib/delWord", JSON, null, StringUtils.toJson(param), parameters);
    }

    /**
     * 根据lib_id查询词条明细
     *
     * @param libId      词库ID
     * @param returnWord 决定是否返回词条明细，建议必传true
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String info(String libId, boolean returnWord) throws Exception {
        if (StringUtils.isNullOrEmpty(libId)) {
            throw new BusinessException("词库ID为空");
        }
        // 参数
        JsonObject param = new JsonObject();
        param.addProperty("lib_id", libId);
        param.addProperty("return_word", returnWord);
        Map<String, String> parameters = Signature.getAuth(appId, apiKey, apiSecret);
        return sendPost(hostUrl + "wordLib/info", JSON, null, param.toString(), parameters);
    }

    public String info(String libId) throws Exception {
        return info(libId, true);
    }


    /**
     * 根据appid查询账户下所有词库
     *
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String listLib() throws Exception {
        // 鉴权
        Map<String, String> parameters = Signature.getAuth(appId, apiKey, apiSecret);
        return sendPost(hostUrl + "wordLib/list", JSON, null, "{}", parameters);
    }

    /**
     * 根据lib_id删除词库
     *
     * @return 返回结果
     * @throws Exception 异常信息
     */
    public String deleteLib(String libId) throws Exception {
        // 参数
        JsonObject param = new JsonObject();
        param.addProperty("lib_id", libId);
        Map<String, String> parameters = Signature.getAuth(appId, apiKey, apiSecret);
        return sendPost(hostUrl + "wordLib/delete", JSON, null, param.toString(), parameters);
    }

    public static final class Builder extends HttpBuilder<Builder> {

        private static final String HOST_URL = "https://audit.iflyaisol.com/audit_res/v1/";

        public Builder(String appId, String apiKey, String apiSecret) {
            super(HOST_URL, appId, apiKey, apiSecret);
        }

        @Override
        public WordLibClient build() {
            return new WordLibClient(this);
        }
    }
}
