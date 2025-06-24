package cn.xfyun.model.common.response;

/**
 * @program: websdk-java
 * @description: 通用响应头
 * @author: zyding6
 * @create: 2025/3/25 9:44
 **/
public class ResponseHeader {

    private Integer code;

    private String message;

    private String sid;

    private Integer status;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
