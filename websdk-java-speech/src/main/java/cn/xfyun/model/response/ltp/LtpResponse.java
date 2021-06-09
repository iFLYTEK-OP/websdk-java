package cn.xfyun.model.response.ltp;

import java.util.List;

/**
 * @author yingpeng
 * 语言技术平台返回参数
 */
public class LtpResponse {

    /**
     * 返回码，0表示成功，其它表示异常，详情请参考错误码。
     */
    private Integer code;

    /**
     * 描述信息
     */
    private String desc;

    /**
     * word: 中文分词结果
     * pos: 词性标注结果
     * dp: 依存句法分析结果,对象中字段parent,relate分别是 父节点，标注关系
     * ner: 命名实体识别结果
     * srl: 语义角色标注结果,对象中字段beg,end,id,type分别是语义角色 开始位置，结束位置，谓词位置，角色标签名
     * sdp: 语义依存 (依存树) 分析结果,对象中字段parent,relate分别是 父节点，语义关系
     * sdgp: 语义依存 (依存图) 分析结果,对象中字段id,parent,relate分别是 弧指向节点词索引，弧父节点词索引，语义关系
     */
    private Object data;

    /**
     * 本次会话的id
     */
    private String sid;

    public LtpResponse() {
    }

    public LtpResponse(Integer code, String desc, Object data, String sid) {
        this.code = code;
        this.desc = desc;
        this.data = data;
        this.sid = sid;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "LtsResponse{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                ", data=" + data +
                ", sid='" + sid + '\'' +
                '}';
    }
}
