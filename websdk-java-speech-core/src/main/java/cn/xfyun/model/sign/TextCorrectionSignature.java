package cn.xfyun.model.sign;


import java.net.MalformedURLException;
import java.net.URL;
import java.security.SignatureException;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/6/10 11:23
 */
public class TextCorrectionSignature extends Hmac256Signature{


	/**
	 * 构造函数
	 *
	 * @param apiKey
	 * @param secretKey
	 * @param hostUrl
	 */
	public TextCorrectionSignature(String apiKey, String secretKey, String hostUrl) {
		super(apiKey, secretKey, hostUrl);
	}

	/**
	 * 生成待加密原始字符
	 * 规则如下：
	 * host: iat-api.xfyun.cn
	 * date: Wed, 10 Jul 2019 07:35:43 GMT
	 * GET /v2/iat HTTP/1.1
	 *
	 * @throws SignatureException
	 */
	@Override
	public String generateOriginSign() throws SignatureException {
		try {
			URL url = new URL(this.getUrl());

			return "host: " + url.getHost() + "\n" +
					"date: " + this.getTs() + "\n" +
					"POST " + url.getPath() + " HTTP/1.1";
		} catch (MalformedURLException e) {
			throw new SignatureException("MalformedURLException:" + e.getMessage());
		}
	}
}
