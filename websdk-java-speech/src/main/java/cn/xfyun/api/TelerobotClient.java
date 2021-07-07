package cn.xfyun.api;

import cn.xfyun.exception.HttpException;
import cn.xfyun.model.request.telerobot.Callout;
import cn.xfyun.model.request.telerobot.TaskCreate;
import cn.xfyun.model.request.telerobot.TaskInsert;
import cn.xfyun.model.request.telerobot.TaskQuery;
import cn.xfyun.model.response.telerobot.*;
import cn.xfyun.util.HttpConnector;
import cn.xfyun.util.StringUtils;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * AI客服平台能力中间件
 *
 * @author : jun
 * @date : 2021年06月15日
 */
public class TelerobotClient {
    /**
     * 接口地址
     */
    private String hostUrl;

    /**
     * 接口密钥，在讯飞开放平台控制台添加相应服务后即可获取
     */
    private String appKey;

    /**
     * 接口密钥，在讯飞开放平台控制台添加相应服务后即可获取
     */
    private String appSecret;



    private HttpConnector connector;

    public TelerobotClient(Builder builder){
        this.appKey = builder.appKey;
        this.appSecret = builder.appSecret;
        this.connector = builder.connector;
    }

    /**
     * 获取token
     */
    public TelerobotResponse<TelerobotToken> token() throws IOException {
        hostUrl = "https://callapi.xfyun.cn/v1/service/v1/aicall/oauth/v1/token";
        Map<String, String> body = new HashMap<>();
        body.put("app_key", appKey);
        body.put("app_secret", appSecret);
        String result = connector.postByJson(hostUrl, StringUtils.gson.toJson(body));
        Type jsonType = new TypeToken<TelerobotResponse<TelerobotToken>>() {}.getType();
        return StringUtils.gson.fromJson(result, jsonType);
    }

    /**
     * 查询配置
     */
    public TelerobotResponse<TelerobotQuery> query(String token, Integer type) throws IOException {
        hostUrl = "https://callapi.xfyun.cn/v1/service/v1/aicall/config/v1/query?token="+token;
        String param = "{\"type\":\"" + type +"\"}";
        String result = connector.postByJson(hostUrl, param);
        return StringUtils.gson.fromJson(result, new TypeToken<TelerobotResponse<TelerobotQuery>>() {}.getType());
    }


    /**
     * 直接外呼
     */
    public TelerobotResponse<TelerobotCallout> callout(String token, Callout callout) throws IOException {
        hostUrl = "https://callapi.xfyun.cn/v1/service/v1/aicall/outbound/v1/task/callout?token="+token;
        String result = connector.postByJson(hostUrl, StringUtils.gson.toJson(callout));
        return StringUtils.gson.fromJson(result, new TypeToken<TelerobotResponse<TelerobotCallout>>() {}.getType());
    }


    /**
     * 创建外呼任务
     */
    public TelerobotResponse<TelerobotCreate> createTask(String token, TaskCreate taskCreate) throws IOException {
        hostUrl = "https://callapi.xfyun.cn/v1/service/v1/aicall/outbound/v1/task/create?token="+token;
        String result = connector.postByJson(hostUrl, StringUtils.gson.toJson(taskCreate));
        return StringUtils.gson.fromJson(result,  new TypeToken<TelerobotResponse<TelerobotCreate>>() {}.getType());
    }

    /**
     * 提交任务数据
     * 向指定任务提交号码数据，可以分多批次提交
     */
    public TelerobotResponse<TelerobotCallout> insertTask(String token, TaskInsert taskInsert) throws IOException {
        hostUrl = "https://callapi.xfyun.cn/v1/service/v1/aicall/outbound/v1/task/insert?token=" + token;
        String result = connector.postByJson(hostUrl, StringUtils.gson.toJson(taskInsert));
        return StringUtils.gson.fromJson(result, new TypeToken<TelerobotResponse<TelerobotCallout>>() {}.getType());
    }

    /**
     * 启动外呼任务
     * 启动外呼任务，任务将按照预设的开始时间和工作时段进行外呼。 任务启动之后，将不能再提交号码数据。
     */
    public TelerobotResponse<String> startTask(String token, String taskId) throws IOException {
        String param = "{\"task_id\":\"" + taskId +"\"}";
        hostUrl = "https://callapi.xfyun.cn/v1/service/v1/aicall/outbound/v1/task/start?token="+token;
        String result = connector.postByJson(hostUrl, param);
        return StringUtils.gson.fromJson(result, new TypeToken<TelerobotResponse<Object>>() {}.getType());
    }

    /**
     * 暂停外呼任务
     * 功能：暂时停止任务呼叫。可以通过启动外呼任务接口恢复任务呼叫
     */
    public TelerobotResponse<String> pauseTask(String token, String taskId) throws IOException {
        String param = "{\"task_id\":\"" + taskId +"\"}";
        hostUrl = "https://callapi.xfyun.cn/v1/service/v1/aicall/outbound/v1/task/pause?token="+token;
        String result = connector.postByJson(hostUrl, param);
        return StringUtils.gson.fromJson(result, new TypeToken<TelerobotResponse<Object>>() {}.getType());
    }

    /**
     * 删除外呼任务
     * 功能：对外呼任务进行强制停止并删除，删除后不能再次启动
     */
    public TelerobotResponse<String> deleteTask(String token, String taskId) throws IOException {
        String param = "{\"task_id\":\"" + taskId +"\"}";
        hostUrl = "https://callapi.xfyun.cn/v1/service/v1/aicall/outbound/v1/task/delete?token="+token;
        String result = connector.postByJson(hostUrl, param);
        return StringUtils.gson.fromJson(result, new TypeToken<TelerobotResponse<Object>>() {}.getType());
    }

    /**
     * 查询任务
     * 功能：查询任务信息和任务列表
     */
    public TelerobotResponse<TelerobotTaskQuery> queryTask(String token, TaskQuery taskQuery) throws IOException {
        hostUrl = "https://callapi.xfyun.cn/v1/service/v1/aicall/outbound/v1/task/query?token="+token;
        String result = connector.postByJson(hostUrl, StringUtils.gson.toJson(taskQuery));
        return StringUtils.gson.fromJson(result, new TypeToken<TelerobotResponse<TelerobotTaskQuery>>() {}.getType());
    }


    public static final class Builder {
        private final String appKey;
        private final String appSecret;

        /**
         * 最大连接数
         */
        private Integer maxConnections = 50;

        /**
         * 建立连接的超时时间
         */
        private Integer connTimeout = 10000;

        /**
         * 读数据包的超时时间
         */
        private Integer soTimeout = 30000;

        /**
         * 重试次数
         */
        private Integer retryCount = 3;
        private HttpConnector connector;

        public Builder(String appKey, String appSecret) {
            this.appKey = appKey;
            this.appSecret = appSecret;
        }

        public Builder maxConnections(Integer maxConnections) {
            this.maxConnections = maxConnections;
            return this;
        }

        public Builder connTimeout(Integer connTimeout) {
            this.connTimeout = connTimeout;
            return this;
        }

        public Builder soTimeout(Integer soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public Builder retryCount(Integer retryCount){
            this.retryCount = retryCount;
            return this;
        }

        public TelerobotClient build() {
            this.connector = HttpConnector.build(maxConnections, connTimeout, soTimeout, retryCount);
            return new TelerobotClient(this);
        }
    }
}
