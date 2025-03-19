package cn.xfyun.service.lfasr.task;

import com.google.gson.Gson;
import cn.xfyun.model.response.lfasr.LfasrMessage;
import cn.xfyun.model.response.lfasr.LfasrResponse;
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
public abstract class AbstractTaskV2 implements TaskV2 {
    protected Map<String, String> param = new HashMap<>();

    protected HttpConnector connector;

    protected AbstractTaskV2(LfasrSignature signature) throws SignatureException {
        this.param.put("appId", signature.getId());
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

    protected LfasrResponse resolveMessage(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, LfasrResponse.class);
    }
}
