package cn.xfyun.config;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/5 18:36
 */
public enum LtpFunctionEnum {

    CWS("cws"),
    NER("ner"),
    DP("dp"),
    SRL("srl"),
    SDP("sdp"),
    SDGP("sdgp"),
    KE("ke"),
    POS("pos");

    private String value;

    LtpFunctionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
