package cn.xfyun.model.voiceclone.response;

/**
 * 一句话复刻获取的token
 *
 * @author zyding
 */
public class TokenResponse {

    /**
     * 有效期：默认7200，单位秒
     */
    private String expiresin;

    /**
     * 状态码
     * 000000	成功	无
     * 999999	系统内部异常	联系业务方排查
     * 000004	请求参数缺失	按上述文档要求，检查请求参数格式
     * 000006	请求参数格式异常（时间戳参数为字符串）	检查请求参数格式
     * 000007	sign校验失败	检查确认token生成格式
     */
    private String retcode;

    /**
     * Access Token
     */
    private String accesstoken;

    public String getExpiresin() {
        return expiresin;
    }

    public String getRetcode() {
        return retcode;
    }

    public String getAccesstoken() {
        return accesstoken;
    }
}
