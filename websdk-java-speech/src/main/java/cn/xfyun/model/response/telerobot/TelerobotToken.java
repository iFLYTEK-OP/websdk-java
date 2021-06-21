package cn.xfyun.model.response.telerobot;

/**
 * token解析
 *
 * @author : jun
 * @date : 2021年06月21日
 */
public class TelerobotToken {
    /**
     * 令牌
     */
    private String token;
    /**
     * 有效期	单位：秒。默认3600。
     */
    private Long time_expire;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getTime_expire() {
        return time_expire;
    }

    public void setTime_expire(Long time_expire) {
        this.time_expire = time_expire;
    }
}
