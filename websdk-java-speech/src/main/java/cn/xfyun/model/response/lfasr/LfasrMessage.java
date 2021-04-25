package cn.xfyun.model.response.lfasr;

import cn.xfyun.config.LfasrTaskStatusEnum;

/**
 * 语音转写结果实体
 *
 * @author : iflytek
 * @date : 2021年03月15日
 */

public class LfasrMessage {
    private int ok = LfasrTaskStatusEnum.STATUS_0.getKey();

    private int errNo;

    private String failed;

    private String data;

    public LfasrMessage() {
    }

    public LfasrMessage(String failed) {
        this.ok = LfasrTaskStatusEnum.STATUS_FAILED.getKey();
        this.failed = failed;
        this.data = "";
    }

    public LfasrMessage(int ok, int errNo, String failed, String data) {
        this.ok = ok;
        this.errNo = errNo;
        this.failed = failed;
        this.data = data;
    }

    public int getOk() {
        return this.ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    public String getFailed() {
        return this.failed;
    }

    public void setFailed(String failed) {
        this.failed = failed;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getErrNo() {
        return errNo;
    }

    public void setErrNo(int errNo) {
        this.errNo = errNo;
    }

    @Override
    public String toString() {
        return String.format("ok:%s errNo:%s failed:%s data:%s", this.ok, this.errNo, this.failed, this.data);
    }
}
