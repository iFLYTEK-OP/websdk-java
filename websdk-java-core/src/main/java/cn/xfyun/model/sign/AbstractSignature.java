package cn.xfyun.model.sign;

import java.security.SignatureException;

/**
 * 加签加密抽象类
 *
 * @author : jun
 * @date : 2021年04月08日
 */
public abstract class AbstractSignature {
    /**
     * 签名ID
     */
    private String id;

    /**
     * 加密key
     */
    private String key;

    /**
     * 服务url
     */
    private String url;

    /**
     * 加密算法
     */
    private String encryptType;

    /**
     * 待加密原始字符
     */
    private String originSign;

    /**
     * 最终生成的签名
     */
    protected String signa;

    /**
     * 时间戳timestamp
     */
    private String ts;

    /**
     *
     * @param id
     * @param key
     * @param url
     */
    public AbstractSignature(String id, String key, String url) {
        this.id = id;
        this.key = key;
        this.url = url;
        this.ts = generateTs();
    }

    /**
     * 生成ts时间
     */
    public String generateTs() {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }


    /**
     * 完成签名，返回完整签名
     * @return
     * @throws SignatureException
     */
    public abstract String getSigna() throws SignatureException;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOriginSign() {
        return originSign;
    }

    public void setOriginSign(String originSign) {
        this.originSign = originSign;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(String encryptType) {
        this.encryptType = encryptType;
    }
}
