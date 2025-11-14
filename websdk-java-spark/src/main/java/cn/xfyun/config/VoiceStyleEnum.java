package cn.xfyun.config;

/**
 * 一句话复刻风格枚举类
 *
 * @author <zyding6@ifytek.com>
 **/
public enum VoiceStyleEnum {

    // 方言类型
    TIANJIN("tianjin", "天津话"),
    DONGBEI("dongbei", "东北话"),
    SICHUAN("sichuan", "四川话"),

    // 语音类型
    CHAT("chat", "交互聊天"),
    NEWS("news", "新闻播报"),
    EXPLANATION("explanation", "通俗解说"),
    PICTURE_BOOK("picture_book", "绘本朗读"),
    TEACH("teach", "教育辅学"),
    LANGSONG("langsong", "朗诵"),
    NOVEL("novel", "小说旁白"),

    // 角色类型
    SUNWUKONG("sunwukong", "孙悟空"),
    LINDAIYU("lindaiyu", "林黛玉"),
    LABIXIAOXIN("labixiaoxin", "蜡笔小新"),
    XIONGER("xionger", "熊二"),
    PEIQI("peiqi", "小猪佩奇"),
    ZHUGELIANG("zhugeliang", "诸葛亮"),

    // 情感状态
    NEUTRAL("neutral", "平和"),
    HAPPY("happy", "高兴"),
    EXCITED("excited", "激动"),
    GLAD("glad", "开心"),
    COMFORT("comfort", "安慰"),
    ENCOURAGING("encouraging", "鼓励"),
    APOLOGETIC("apologetic", "抱歉"),
    SAD("sad", "悲伤"),
    DOWNHEARTED("downhearted", "低落"),
    CURIOUS("curious", "好奇"),
    CONFUSED("confused", "困惑"),
    REGRETFUL("regretful", "后悔"),
    SURPRISED("surprised", "惊讶"),
    CUTE("cute", "可爱"),
    LOVEY_DOVEY("lovey-dovey", "撒娇"),
    NAUGHTY("naughty", "调皮"),
    AFRAID("afraid", "害怕"),
    SCORNFUL("scornful", "轻蔑"),
    ANGRY("angry", "生气"),
    FEARFUL("fearful", "恐惧"),
    DISGUSTED("disgusted", "厌恶"),
    LYRICAL("lyrical", "抒情"),
    WRONGED("wronged", "委屈"),
    GENTLE("gentle", "温柔"),
    WEAK("weak", "虚弱"),
    SERIOUS("serious", "严肃");

    private final String code;
    private final String description;

    VoiceStyleEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return code + " -> " + description;
    }

    /**
     * 根据代码获取枚举值
     */
    public static VoiceStyleEnum getByCode(String code) {
        for (VoiceStyleEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根据描述获取枚举值
     */
    public static VoiceStyleEnum getByDescription(String description) {
        for (VoiceStyleEnum type : values()) {
            if (type.getDescription().equals(description)) {
                return type;
            }
        }
        return null;
    }
}
