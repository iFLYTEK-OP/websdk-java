package cn.xfyun.service.lfasr.task;

import com.google.gson.Gson;
import cn.xfyun.model.response.lfasr.LfasrMessage;
import cn.xfyun.model.sign.LfasrSignature;
import cn.xfyun.util.HttpConnector;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * 任务抽象类
 *
 * @author : iflytek
 * @date : 2021年03月15日
 */
public abstract class AbstractTask implements Task {
    protected Map<String, String> param = new HashMap<>();

    protected HttpConnector connector;

    protected AbstractTask(LfasrSignature signature) throws SignatureException {
        this.param.put("app_id", signature.getId());
        this.param.put("signa", signature.getSigna());
        this.param.put("ts", signature.getTs());
    }

    @Override
    public HttpConnector getConnector() {
        return this.connector;
    }

    @Override
    public void setConnector(HttpConnector connector) {
        this.connector = connector;
    }

    protected LfasrMessage resolveMessage(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, LfasrMessage.class);
    }
}
