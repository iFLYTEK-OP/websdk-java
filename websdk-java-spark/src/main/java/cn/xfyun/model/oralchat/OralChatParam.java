package cn.xfyun.model.oralchat;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.StringUtils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 超拟人聊天开启参数
 *
 * @author <zyding6@iflytek.com>
 **/
public class OralChatParam {

    /**
     * 授权的用户ID
     * maxLength:64，需保证在appid下唯一
     */
    private String uid;
    /**
     * 情景模式
     * maxLength:16，从AIUI/飞云平台创建
     */
    private String scene;
    /**
     * 纬度
     */
    private String mscLat;
    /**
     * 经度
     */
    private String mscLng;
    /**
     * 回复要求设定
     * 通过该参数设定大模型回复风格、格式以及其他回答要求等
     */
    private String prompt;
    /**
     * 系统
     */
    private String osSys;
    /**
     * 是否新会话
     * "true"/"global":清空历史
     * "false":保留历史
     */
    private String newSession;
    /**
     * 交互模式
     * continuous(全双工)
     * continuous_vad(单工)
     */
    private String interactMode;
    /**
     * 指定bot ID
     * maxLength:64
     * 示例：sos_app_deepseekv3
     */
    private String botId;
    /**
     * 发音人
     * x5_lingxiaoyue_flow(聆小玥，女性助理)
     * x5_lingfeiyi_flow(聆飞逸，男性助理)
     */
    private String vcn;
    /**
     * 语速
     * 0-100，默认50
     */
    private Integer speed;
    /**
     * 音量
     * 0-100，默认50
     */
    private Integer volume;
    /**
     * 音调
     * 0-100，默认50
     */
    private Integer pitch;
    /**
     * 人设 id
     */
    private String personal;
    /**
     * 声纹ID
     */
    private String resId;
    /**
     * 声纹性别
     */
    private String resGender;
    /**
     * 会话ID/交互轮数
     * maxLength:32
     * 单工模式(continuous_vad)必须递增
     * 双工模式(continuous)固定不变
     * 需为可解析为整数的字符串，如"0","1"
     */
    private AtomicInteger stmid;
    /**
     * 形象信息
     */
    private Avatar avatar;
    /**
     * 扩展参数
     */
    private String persParam;

    public OralChatParam(Builder builder) {
        this.uid = builder.uid;
        this.scene = builder.scene;
        this.mscLat = builder.mscLat;
        this.mscLng = builder.mscLng;
        this.prompt = builder.prompt;
        this.osSys = builder.osSys;
        this.newSession = builder.newSession;
        this.interactMode = builder.interactMode;
        this.botId = builder.botId;
        this.vcn = builder.vcn;
        this.speed = builder.speed;
        this.volume = builder.volume;
        this.pitch = builder.pitch;
        this.personal = builder.personal;
        this.avatar = builder.avatar;
        this.resId = builder.resId;
        this.resGender = builder.resGender;
        this.stmid = builder.stmid;
        this.persParam = builder.persParam;
    }

    public OralChatParam() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getMscLat() {
        return mscLat;
    }

    public void setMscLat(String mscLat) {
        this.mscLat = mscLat;
    }

    public String getMscLng() {
        return mscLng;
    }

    public void setMscLng(String mscLng) {
        this.mscLng = mscLng;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getOsSys() {
        return osSys;
    }

    public void setOsSys(String osSys) {
        this.osSys = osSys;
    }

    public String getNewSession() {
        return newSession;
    }

    public void setNewSession(String newSession) {
        this.newSession = newSession;
    }

    public String getInteractMode() {
        return interactMode;
    }

    public void setInteractMode(String interactMode) {
        this.interactMode = interactMode;
    }

    public String getBotId() {
        return botId;
    }

    public void setBotId(String botId) {
        this.botId = botId;
    }

    public String getVcn() {
        return vcn;
    }

    public void setVcn(String vcn) {
        this.vcn = vcn;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getPitch() {
        return pitch;
    }

    public void setPitch(Integer pitch) {
        this.pitch = pitch;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResGender() {
        return resGender;
    }

    public void setResGender(String resGender) {
        this.resGender = resGender;
    }

    public AtomicInteger getStmid() {
        return stmid;
    }

    public String getPersParam() {
        return persParam;
    }

    public void setPersParam(String persParam) {
        this.persParam = persParam;
    }

    public void selfCheck() {
        if (StringUtils.isNullOrEmpty(interactMode)) {
            throw new BusinessException("交互模式不能为空");
        }
        if (StringUtils.isNullOrEmpty(uid)) {
            throw new BusinessException("uid不能为空");
        }
        if (null == scene) {
            this.scene = "sos_app";
        }
        if (null == stmid) {
            this.stmid = new AtomicInteger(-1);
        }
        if (StringUtils.isNullOrEmpty(vcn) && StringUtils.isNullOrEmpty(resId)) {
            this.vcn = "x5_lingxiaoyue_flow";
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String uid;
        private String scene = "sos_app";
        private String mscLat;
        private String mscLng;
        private String prompt;
        private String osSys;
        private String newSession;
        private String interactMode;
        private String botId;
        private String vcn = "x5_lingxiaoyue_flow";
        private Integer speed = 50;
        private Integer volume = 50;
        private Integer pitch = 50;
        private String personal;
        private Avatar avatar;
        private String resId;
        private String resGender;
        private String persParam;
        private final AtomicInteger stmid = new AtomicInteger(-1);;

        private Builder() {
        }

        public OralChatParam build() {
            return new OralChatParam(this);
        }

        public Builder uid(String uid) {
            this.uid = uid;
            return this;
        }

        public Builder scene(String scene) {
            this.scene = scene;
            return this;
        }

        public Builder mscLat(String mscLat) {
            this.mscLat = mscLat;
            return this;
        }

        public Builder mscLng(String mscLng) {
            this.mscLng = mscLng;
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder osSys(String osSys) {
            this.osSys = osSys;
            return this;
        }

        public Builder newSession(String newSession) {
            this.newSession = newSession;
            return this;
        }

        public Builder interactMode(String interactMode) {
            this.interactMode = interactMode;
            return this;
        }

        public Builder vcn(String vcn) {
            this.vcn = vcn;
            return this;
        }

        public Builder botId(String botId) {
            this.botId = botId;
            return this;
        }

        public Builder volume(Integer volume) {
            this.volume = volume;
            return this;
        }

        public Builder pitch(Integer pitch) {
            this.pitch = pitch;
            return this;
        }

        public Builder speed(Integer speed) {
            this.speed = speed;
            return this;
        }

        public Builder personal(String personal) {
            this.personal = personal;
            return this;
        }

        public Builder avatar(Avatar avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder resId(String resId) {
            this.resId = resId;
            return this;
        }

        public Builder resGender(String resGender) {
            this.resGender = resGender;
            return this;
        }

        public Builder persParam(String persParam) {
            this.persParam = persParam;
            return this;
        }
    }
}
