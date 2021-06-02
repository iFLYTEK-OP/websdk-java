package cn.xfyun.model.response.igr;

/**
 * @author: <flhong2@iflytek.com>
 * @description: 接口返回消息体
 * @version: v1.0
 * @create: 2021-06-02 15:20
 **/
public class IgrResponseData {
    private int code;
    private String message;
    private String sid;
    private IgrInnerData data;
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public String getSid() {
        return sid;
    }
    public IgrInnerData getData() {
        return data;
    }

    /**
     * 消息体内部的data消息体
     */
    public class IgrInnerData {
        private int status;
        private ResultBody result;

        public int getStatus() {
            return status;
        }

        public ResultBody getResult() {
            return result;
        }
    }

    private class ResultBody {
        private Age age;
        private Gender gender;
    }

    private class Age {
        private String age_type;
        private String child;
        private String middle;
        private String old;
    }

    private class Gender {
        private String gender_type;
        private String female;
        private String male;
    }
}
