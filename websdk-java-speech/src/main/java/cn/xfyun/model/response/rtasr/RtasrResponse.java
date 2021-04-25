package cn.xfyun.model.response.rtasr;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/3/23 14:08
 */
public class RtasrResponse {

	private String action;

	private String code;

	private String data;

	private String desc;

	private String sid;


	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public RtasrResponse() {

	}

}
