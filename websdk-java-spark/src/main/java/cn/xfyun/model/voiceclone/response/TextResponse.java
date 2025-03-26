package cn.xfyun.model.voiceclone.response;

import java.util.List;

/**
 * 一句话复刻获取训练文本
 *
 * @author zyding
 */
public class TextResponse {

    /**
     * true or false
     */
    private String flag;

    /**
     * 返回码 0表示成功
     */
    private int code;

    /**
     * 返回描述
     */
    private String desc;

    /**
     * 响应数据
     */
    private Text data;

    public String getFlag() {
        return flag;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public Text getData() {
        return data;
    }

    public static class Text {
        /**
         * 文本ID
         */
        private int textId;

        /**
         * 文本标题
         */
        private String textName;

        /**
         * 文本列表
         */
        private List<Segs> textSegs;

        public int getTextId() {
            return textId;
        }

        public String getTextName() {
            return textName;
        }

        public List<Segs> getTextSegs() {
            return textSegs;
        }
    }

    public static class Segs {
        /**
         * 段落ID，表示第几条文本
         */
        private int segId;

        /**
         * 文本内容
         */
        private String segText;

        public int getSegId() {
            return segId;
        }

        public String getSegText() {
            return segText;
        }
    }
}
