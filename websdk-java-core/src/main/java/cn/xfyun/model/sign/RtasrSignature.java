package cn.xfyun.model.sign;


import cn.xfyun.util.CryptTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/4/8 15:37
 */
public class RtasrSignature extends AbstractSignature{

	private static final Logger logger = LoggerFactory.getLogger(RtasrSignature.class);

	public RtasrSignature(String id, String key) {
		super(id, key, null);
	}

	@Override
	public String getSigna() {
		String id = getId();
		String key = getKey();
		String ts = getTs();
		try {
			signa = CryptTools.hmacEncrypt(CryptTools.HMAC_SHA1, CryptTools.md5Encrypt(id + ts), key);
			return "?appid=" + id + "&ts=" + ts + "&signa=" + URLEncoder.encode(signa, "UTF-8");
		} catch (Exception e) {
			logger.error("签名失败", e);
		}

		return "";
	}
}
