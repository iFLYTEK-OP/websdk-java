package cn.xfyun.model.sign;

import cn.xfyun.util.CryptTools;
import cn.xfyun.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * Lfasr能力签名实体
 *
 * @author : jun
 * @date : 2021年03月29日
 */
public class LfasrSignature extends AbstractSignature {

    /**
     *
     * @param appId
     * @param keySecret
     */
    public LfasrSignature(String appId, String keySecret) {
        super(appId, keySecret, null);
    }

    @Override
    public String getSigna() throws SignatureException {
        if (StringUtils.isNullOrEmpty(this.signa)) {
            this.setOriginSign(generateOriginSign());
            this.signa = generateSignature();
        }
        return this.signa;
    }

    /**
     * 生成最终的签名，需要先生成原始sign
     *
     * @throws SignatureException
     */
    public String generateSignature() throws SignatureException {
        return CryptTools.hmacEncrypt(CryptTools.HMAC_SHA1, this.getOriginSign(), this.getKey());
    }

    /**
     * 生成待加密原始字符
     *
     * @throws NoSuchAlgorithmException
     */
    public String generateOriginSign() throws SignatureException {
        return CryptTools.md5Encrypt(this.getId() + this.getTs());
    }
}
