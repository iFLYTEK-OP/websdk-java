package cn.xfyun.service.agent;

import okhttp3.*;
import okio.BufferedSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 智能体SSE请求监听类
 *
 * @author <zyding6@ifytek.com>
 */
public abstract class AgentCallback implements Callback {

    private static final Logger logger = LoggerFactory.getLogger(AgentCallback.class);

    /**
     * construction method
     */
    public AgentCallback() {
    }

    /**
     * sse返回成功时，需要用户重写的方法
     *
     * @param call ws
     * @param id sse返回结果
     * @param type sse返回结果
     * @param data sse返回结果
     */
    public abstract void onEvent(Call call, String id, String type, String data);

    /**
     * sse返回失败时，需要用户重写的方法
     *
     * @param call call
     * @param t    异常信息
     */
    public abstract void onFail(Call call, Throwable t);

    /**
     * sse关闭时，需要用户重写的方法
     *
     * @param call   call
     */
    public abstract void onClosed(Call call);

    /**
     * sse开启败时，需要用户重写的方法
     *
     * @param call   call
     * @param response 停止原因
     */
    public abstract void onOpen(Call call, Response response);

    @Override
    public void onFailure(Call call, IOException e) {
        onFail(call, e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        // 调用你的 onOpen()
        onOpen(call, response);

        if (!response.isSuccessful()) {
            logger.error("请求失败，状态码：{}，原因：{}", response.code(), response.message());
            return;
        }

        ResponseBody body = response.body();
        if (null != body) {

            String id = null;
            String type = "message"; // 默认 event 类型
            BufferedSource source = body.source();

            while (!source.exhausted() && !call.isCanceled()) {
                // 一行一行读取
                String line = source.readUtf8LineStrict();
                if (line.isEmpty()) {
                    // 重置
                    type = "message";
                    id = null;
                    continue;
                }
                // 解析前缀字段：id, event, data 等
                if (line.startsWith("id:")) {
                    id = line.substring(3).trim();
                } else if (line.startsWith("event:")) {
                    type = line.substring(6).trim();
                } else if (line.startsWith("data:")) {
                    String data = line.substring(5).trim();
                    onEvent(call, id, type, data);
                }
            }
        }

        response.close();
        // 流结束时回调
        onClosed(call);
    }
}
