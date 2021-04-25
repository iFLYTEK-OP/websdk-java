package cn.xfyun.service.lfasr.task;


import cn.xfyun.model.response.lfasr.LfasrMessage;
import cn.xfyun.util.HttpConnector;

import java.util.concurrent.Callable;

/**
 * @author iflytek
 */
public interface Task extends Callable<LfasrMessage> {

    /**
     * 获取HttpConnector
     *
     * @return http实体
     */
    HttpConnector getConnector();

    /**
     * 设置HttpConnector
     *
     * @param paramConnector http实体
     */
    void setConnector(HttpConnector paramConnector);

    /**
     * 返回task信息
     *
     * @return task信息，eg:taskId
     */
    String getIntro();
}