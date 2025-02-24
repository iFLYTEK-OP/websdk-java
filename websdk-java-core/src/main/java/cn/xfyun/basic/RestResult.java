package cn.xfyun.basic;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author: rblu2
 * @desc:
 * @create: 2025-02-24 11:02
 **/
public class RestResult {
    private Integer code;
    private String message;
    private String body;

    public static RestResult from(Response response) {
        RestResult data = new RestResult();
        data.code = response.code();
        data.message = response.message();
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            try {
                data.body = responseBody.string();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getBody() {
        return body;
    }
}
