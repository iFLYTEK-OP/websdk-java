package cn.xfyun.model.response.telerobot.vo;


/**
 * 发音人
 *
 * @author : jun
 * @date : 2021年06月21日
 */
public class Voice {
    /**
     * 发音人编码	见【常用发音人清单】
     */
    private String voice_code;

    /**
     * 发音人名称	见【常用发音人清单】
     */
    private String voice_name;

    public void setVoice_code(String voice_code) {
        this.voice_code = voice_code;
    }

    public void setVoice_name(String voice_name) {
        this.voice_name = voice_name;
    }
}
