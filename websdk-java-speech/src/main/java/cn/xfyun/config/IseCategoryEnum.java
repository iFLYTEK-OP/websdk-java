package cn.xfyun.config;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/9 14:29
 */
public enum IseCategoryEnum {

    /**
     *  单字朗读，汉语专有
     */
    READ_SYLLABLE("read_syllable"),

    /**
     *  词语朗读
     */
    READ_WORD("read_word"),

    /**
     *  句子朗读
     */
    READ_SENTENCE("read_sentence"),

    /**
     *  篇章朗读
     */
    READ_CHAPTER("read_chapter");

    private String value;

    IseCategoryEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
